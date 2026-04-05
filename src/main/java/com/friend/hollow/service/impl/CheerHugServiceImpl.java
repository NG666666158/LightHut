package com.friend.hollow.service.impl;

import com.friend.hollow.dto.HugTodayResponse;
import com.friend.hollow.entity.HugClickDailyEntity;
import com.friend.hollow.repository.jpa.HugClickDailyJpaDao;
import com.friend.hollow.service.CheerHugService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class CheerHugServiceImpl implements CheerHugService {

    /** 按中国日期切日，与访客「今天」体感一致 */
    private static final ZoneId HUG_ZONE = ZoneId.of("Asia/Shanghai");

    private final HugClickDailyJpaDao dao;

    public CheerHugServiceImpl(HugClickDailyJpaDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(readOnly = true)
    public HugTodayResponse today() {
        LocalDate day = LocalDate.now(HUG_ZONE);
        return dao.findById(day)
                .map(e -> toResponse(e.getCount()))
                .orElse(toResponse(0));
    }

    @Override
    @Transactional
    public HugTodayResponse increment() {
        LocalDate day = LocalDate.now(HUG_ZONE);
        HugClickDailyEntity row = dao.findById(day).orElseGet(() -> {
            HugClickDailyEntity e = new HugClickDailyEntity();
            e.setDay(day);
            e.setCount(0);
            return e;
        });
        row.setCount(row.getCount() + 1);
        dao.save(row);
        return toResponse(row.getCount());
    }

    static HugTodayResponse toResponse(long count) {
        String display = count > 999 ? "999+" : Long.toString(count);
        return new HugTodayResponse(count, display);
    }
}
