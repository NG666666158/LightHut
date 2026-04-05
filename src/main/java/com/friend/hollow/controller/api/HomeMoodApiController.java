package com.friend.hollow.controller.api;

import com.friend.hollow.dto.MoodCreateRequest;
import com.friend.hollow.dto.MoodEntryResponse;
import com.friend.hollow.dto.MoodRecentResponse;
import com.friend.hollow.service.MoodService;
import com.friend.hollow.util.PagePathUtil;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页「记录此刻心情」接口。
 */
@RestController
public class HomeMoodApiController {

    private final MoodService moodService;

    public HomeMoodApiController(MoodService moodService) {
        this.moodService = moodService;
    }

    @PostMapping(value = PagePathUtil.HOME_MOOD_API, consumes = MediaType.APPLICATION_JSON_VALUE)
    public MoodEntryResponse create(@Valid @RequestBody MoodCreateRequest request) {
        return moodService.add(request);
    }

    @GetMapping(PagePathUtil.HOME_MOOD_RECENT_API)
    public MoodRecentResponse recent(@RequestParam(defaultValue = "5") int limit) {
        return moodService.recent(limit);
    }
}
