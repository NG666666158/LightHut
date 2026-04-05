package com.friend.hollow.controller.api;

import com.friend.hollow.dto.ReminderCreateRequest;
import com.friend.hollow.dto.ReminderDayResponse;
import com.friend.hollow.dto.ReminderDoneRequest;
import com.friend.hollow.dto.ReminderItemResponse;
import com.friend.hollow.dto.ReminderMonthResponse;
import com.friend.hollow.service.ReminderService;
import com.friend.hollow.util.PagePathUtil;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * 温柔提醒 JSON 接口。
 */
@RestController
public class ReminderApiController {

    private final ReminderService reminderService;

    public ReminderApiController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @GetMapping(PagePathUtil.REMINDER_MONTH_API)
    public ReminderMonthResponse month(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month
    ) {
        LocalDate now = LocalDate.now();
        int y = year != null ? year : now.getYear();
        int m = month != null ? month : now.getMonthValue();
        return reminderService.getMonth(y, m);
    }

    @GetMapping(PagePathUtil.REMINDER_DAY_API)
    public ReminderDayResponse day(@RequestParam LocalDate date) {
        return reminderService.listByDate(date);
    }

    @PostMapping(value = PagePathUtil.REMINDER_CREATE_API, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReminderItemResponse create(@Valid @RequestBody ReminderCreateRequest request) {
        return reminderService.create(request);
    }

    @PatchMapping(value = "/api/reminder/{id}/done", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReminderItemResponse setDone(@PathVariable long id, @RequestBody ReminderDoneRequest body) {
        return reminderService.setDone(id, body.isDone());
    }
}
