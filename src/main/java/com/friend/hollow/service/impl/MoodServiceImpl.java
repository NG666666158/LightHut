package com.friend.hollow.service.impl;

import com.friend.hollow.dto.MoodCreateRequest;
import com.friend.hollow.dto.MoodEntryResponse;
import com.friend.hollow.dto.MoodRecentResponse;
import com.friend.hollow.repository.MoodRepository;
import com.friend.hollow.service.MoodService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 心情记录（内存版，重启清空）。
 */
@Service
public class MoodServiceImpl implements MoodService {

    private static final int MAX_STORE = 200;

    private final MoodRepository moodRepository;

    public MoodServiceImpl(MoodRepository moodRepository) {
        this.moodRepository = moodRepository;
    }

    @Override
    public synchronized MoodEntryResponse add(MoodCreateRequest request) {
        if (request == null || !StringUtils.hasText(request.getContent())) {
            throw new IllegalArgumentException("请填写心情内容");
        }
        String content = request.getContent().trim();
        if (content.length() > 500) {
            throw new IllegalArgumentException("内容请控制在 500 字以内");
        }
        String mood = request.getMood() != null ? request.getMood().trim() : null;
        if (mood != null && mood.length() > 32) {
            throw new IllegalArgumentException("心情标签过长");
        }

        MoodEntryResponse e = new MoodEntryResponse();
        e.setContent(content);
        e.setMood(StringUtils.hasText(mood) ? mood : null);
        e.setCreatedAt(Instant.now());
        moodRepository.save(e);
        while (moodRepository.size() > MAX_STORE) {
            moodRepository.removeOldest();
        }
        return e;
    }

    @Override
    public synchronized MoodRecentResponse recent(int limit) {
        int n = Math.min(50, Math.max(1, limit));
        List<MoodEntryResponse> sorted = new ArrayList<>(moodRepository.findAll());
        sorted.sort(Comparator.comparing(MoodEntryResponse::getCreatedAt).reversed());
        List<MoodEntryResponse> slice = sorted.stream().limit(n).collect(Collectors.toList());
        MoodRecentResponse r = new MoodRecentResponse();
        r.setItems(slice);
        return r;
    }
}
