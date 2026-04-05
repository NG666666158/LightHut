package com.friend.hollow.service.impl;

import com.friend.hollow.dto.ReminderCalendarCellResponse;
import com.friend.hollow.dto.ReminderCreateRequest;
import com.friend.hollow.dto.ReminderDayResponse;
import com.friend.hollow.dto.ReminderItemResponse;
import com.friend.hollow.dto.ReminderMonthResponse;
import com.friend.hollow.model.ReminderRecord;
import com.friend.hollow.repository.ReminderRepository;
import com.friend.hollow.service.ReminderService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * 温柔提醒（内存版）。
 */
@Service
public class ReminderServiceImpl implements ReminderService {

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");
    private static final String[] WEEKDAY_ZH = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    private final ReminderRepository reminderRepository;

    public ReminderServiceImpl(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    @PostConstruct
    public void seed() {
        if (reminderRepository.count() > 0) {
            return;
        }
        LocalDate today = LocalDate.now();
        addRecord(today, LocalTime.of(14, 0), "饮水时间", "让清澈的水唤醒身体的活力", "water_drop", false);
        addRecord(today, LocalTime.of(8, 30), "晨间冥想", "深呼吸，开启宁静的一天", "mindfulness", true);
        addRecord(today, LocalTime.of(18, 0), "散步十分钟", "看看天空和树影", "directions_walk", false);
        addRecord(today, LocalTime.of(22, 0), "睡前阅读", "读几页喜欢的书", "menu_book", false);
    }

    private void addRecord(LocalDate date, LocalTime time, String title, String note, String icon, boolean done) {
        ReminderRecord r = new ReminderRecord();
        r.setRemindDate(date);
        r.setRemindTime(time);
        r.setTitle(title);
        r.setNote(note);
        r.setIcon(icon);
        r.setDone(done);
        r.setCreatedAt(Instant.now());
        reminderRepository.save(r);
    }

    @Override
    public synchronized ReminderMonthResponse getMonth(int year, int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("月份应在 1-12 之间");
        }
        LocalDate first = LocalDate.of(year, month, 1);
        int startOffset = first.getDayOfWeek().getValue() - 1;
        int daysInMonth = first.lengthOfMonth();
        LocalDate today = LocalDate.now();

        List<ReminderCalendarCellResponse> cells = new ArrayList<>();
        for (int i = 0; i < startOffset; i++) {
            cells.add(emptyCell());
        }
        for (int d = 1; d <= daysInMonth; d++) {
            LocalDate date = LocalDate.of(year, month, d);
            ReminderCalendarCellResponse c = new ReminderCalendarCellResponse();
            c.setDay(d);
            c.setDate(date.toString());
            c.setInMonth(true);
            c.setToday(date.equals(today));
            c.setHasReminder(hasReminderOn(date));
            cells.add(c);
        }
        int trailing = (7 - cells.size() % 7) % 7;
        for (int i = 0; i < trailing; i++) {
            cells.add(emptyCell());
        }

        ReminderMonthResponse res = new ReminderMonthResponse();
        res.setYear(year);
        res.setMonth(month);
        res.setMonthTitle(year + "年 " + month + "月");
        res.setSubtitle(buildMonthSubtitle(year, month, today));
        res.setCells(cells);
        return res;
    }

    private String buildMonthSubtitle(int year, int month, LocalDate today) {
        if (year == today.getYear() && month == today.getMonthValue()) {
            int idx = today.getDayOfWeek().getValue() - 1;
            return "今天 " + today.getMonthValue() + "月" + today.getDayOfMonth() + "日，一个适合慢下来的" + WEEKDAY_ZH[idx];
        }
        return "正在查看 " + year + "年" + month + "月 · 点击日期查看待办";
    }

    private ReminderCalendarCellResponse emptyCell() {
        ReminderCalendarCellResponse c = new ReminderCalendarCellResponse();
        c.setDay(null);
        c.setDate(null);
        c.setInMonth(false);
        c.setToday(false);
        c.setHasReminder(false);
        return c;
    }

    private boolean hasReminderOn(LocalDate date) {
        return reminderRepository.findAll().stream().anyMatch(r -> r.getRemindDate().equals(date));
    }

    @Override
    public synchronized ReminderDayResponse listByDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("请指定日期");
        }
        List<ReminderItemResponse> items = reminderRepository.findAll().stream()
                .filter(r -> r.getRemindDate().equals(date))
                .sorted(Comparator.comparing(ReminderRecord::getRemindTime).thenComparing(ReminderRecord::getId))
                .map(this::toDto)
                .collect(Collectors.toList());

        ReminderDayResponse res = new ReminderDayResponse();
        res.setDate(date.toString());
        res.setTotalCount(items.size());
        res.setItems(items);
        return res;
    }

    @Override
    public synchronized ReminderItemResponse create(ReminderCreateRequest request) {
        if (request == null || !StringUtils.hasText(request.getTitle())) {
            throw new IllegalArgumentException("请填写提醒标题");
        }
        LocalDate date = request.getRemindDate() != null ? request.getRemindDate() : LocalDate.now();
        LocalTime time = parseTimeRequired(request.getTime());

        ReminderRecord r = new ReminderRecord();
        r.setRemindDate(date);
        r.setRemindTime(time);
        r.setTitle(request.getTitle().trim());
        r.setNote(request.getNote() != null ? request.getNote().trim() : null);
        r.setIcon(StringUtils.hasText(request.getIcon()) ? request.getIcon().trim() : "notifications");
        r.setDone(false);
        r.setCreatedAt(Instant.now());
        reminderRepository.save(r);
        return toDto(r);
    }

    LocalTime parseTimeRequired(String raw) {
        if (!StringUtils.hasText(raw)) {
            throw new IllegalArgumentException("请填写提醒时间（HH:mm）");
        }
        try {
            return LocalTime.parse(raw.trim(), TIME_FMT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("时间格式应为 HH:mm，例如 14:00");
        }
    }

    @Override
    public synchronized ReminderItemResponse setDone(long id, boolean done) {
        ReminderRecord r = reminderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("提醒不存在"));
        r.setDone(done);
        reminderRepository.save(r);
        return toDto(r);
    }

    private ReminderItemResponse toDto(ReminderRecord r) {
        ReminderItemResponse dto = new ReminderItemResponse();
        dto.setId(r.getId());
        dto.setRemindDate(r.getRemindDate());
        dto.setRemindTime(r.getRemindTime());
        dto.setTimeDisplay(String.format(Locale.ROOT, "%02d:%02d", r.getRemindTime().getHour(), r.getRemindTime().getMinute()));
        dto.setTitle(r.getTitle());
        dto.setNote(r.getNote());
        dto.setIcon(r.getIcon());
        dto.setDone(r.isDone());
        return dto;
    }
}
