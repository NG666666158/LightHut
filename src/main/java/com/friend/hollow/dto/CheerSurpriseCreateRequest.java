package com.friend.hollow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CheerSurpriseCreateRequest {

    @NotBlank
    @Size(max = 32)
    private String displayName;

    @NotBlank
    @Size(max = 64)
    private String avatarSeed;

    @NotBlank
    @Size(max = 200)
    private String blessing;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAvatarSeed() {
        return avatarSeed;
    }

    public void setAvatarSeed(String avatarSeed) {
        this.avatarSeed = avatarSeed;
    }

    public String getBlessing() {
        return blessing;
    }

    public void setBlessing(String blessing) {
        this.blessing = blessing;
    }
}
