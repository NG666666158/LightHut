package com.friend.hollow.dto;

public record CheerSurpriseItemResponse(
        long id,
        String displayName,
        String avatarUrl,
        String blessing,
        long createdAtEpochMs
) {}
