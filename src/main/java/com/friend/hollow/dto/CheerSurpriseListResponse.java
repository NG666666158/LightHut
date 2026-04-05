package com.friend.hollow.dto;

import java.util.List;

public record CheerSurpriseListResponse(
        List<CheerSurpriseItemResponse> items,
        long totalAll,
        String totalAllDisplay,
        long totalToday,
        String totalTodayDisplay
) {}
