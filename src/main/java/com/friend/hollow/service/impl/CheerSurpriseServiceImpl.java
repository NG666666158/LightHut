package com.friend.hollow.service.impl;

import com.friend.hollow.dto.CheerSurpriseCreateRequest;
import com.friend.hollow.dto.CheerSurpriseItemResponse;
import com.friend.hollow.dto.CheerSurpriseListResponse;
import com.friend.hollow.entity.CheerSurpriseEntity;
import com.friend.hollow.repository.jpa.CheerSurpriseJpaDao;
import com.friend.hollow.service.CheerSurpriseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class CheerSurpriseServiceImpl implements CheerSurpriseService {

    private static final ZoneId SHANGHAI = ZoneId.of("Asia/Shanghai");
    private static final String AVATAR_BASE = "https://api.dicebear.com/7.x/fun-emoji/svg?seed=";

    private final CheerSurpriseJpaDao dao;

    public CheerSurpriseServiceImpl(CheerSurpriseJpaDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(readOnly = true)
    public CheerSurpriseListResponse listAll() {
        return buildResponse();
    }

    @Override
    @Transactional
    public CheerSurpriseListResponse add(CheerSurpriseCreateRequest request) {
        CheerSurpriseEntity e = new CheerSurpriseEntity();
        e.setDisplayName(trimTo(request.getDisplayName(), 32));
        e.setAvatarSeed(trimTo(request.getAvatarSeed(), 64));
        e.setBlessing(trimTo(request.getBlessing(), 200));
        dao.save(e);
        return buildResponse();
    }

    private static String trimTo(String s, int max) {
        if (s == null) {
            return "";
        }
        String t = s.trim();
        return t.length() <= max ? t : t.substring(0, max);
    }

    private CheerSurpriseListResponse buildResponse() {
        List<CheerSurpriseItemResponse> items = dao.findTop500ByOrderByCreatedAtDesc().stream()
                .map(this::toItem)
                .toList();
        long totalAll = dao.count();
        Instant startOfTodayShanghai = ZonedDateTime.now(SHANGHAI).toLocalDate()
                .atStartOfDay(SHANGHAI).toInstant();
        long totalToday = dao.countByCreatedAtAfter(startOfTodayShanghai);
        return new CheerSurpriseListResponse(
                items,
                totalAll,
                formatLarge(totalAll, true),
                totalToday,
                formatLarge(totalToday, false)
        );
    }

    private CheerSurpriseItemResponse toItem(CheerSurpriseEntity e) {
        return new CheerSurpriseItemResponse(
                e.getId(),
                e.getDisplayName(),
                avatarUrl(e.getAvatarSeed()),
                e.getBlessing(),
                e.getCreatedAt().toEpochMilli()
        );
    }

    static String avatarUrl(String seed) {
        return AVATAR_BASE + URLEncoder.encode(seed, StandardCharsets.UTF_8);
    }

    /** today 用较小上限展示；全部用 k 缩写 */
    static String formatLarge(long n, boolean useK) {
        if (n <= 0) {
            return "0";
        }
        if (!useK) {
            return n > 999 ? "999+" : Long.toString(n);
        }
        if (n >= 1_000_000) {
            return (n / 1_000_000) + "m+";
        }
        if (n >= 10_000) {
            return (n / 1000) + "k";
        }
        if (n >= 1000) {
            double k = n / 1000.0;
            return (Math.abs(k - Math.round(k)) < 0.05 ? Math.round(k) : String.format(java.util.Locale.US, "%.1f", k)) + "k";
        }
        return Long.toString(n);
    }
}
