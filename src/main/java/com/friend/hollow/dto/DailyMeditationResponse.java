package com.friend.hollow.dto;

/**
 * 首页「每日冥想」主区卡片数据。
 */
public class DailyMeditationResponse {

    private String tag;
    private String title;
    private String subtitle;
    private String ctaLabel;
    /** 可为空，前端无跳转则忽略 */
    private String ctaHref;
    private String backgroundImageUrl;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getCtaLabel() {
        return ctaLabel;
    }

    public void setCtaLabel(String ctaLabel) {
        this.ctaLabel = ctaLabel;
    }

    public String getCtaHref() {
        return ctaHref;
    }

    public void setCtaHref(String ctaHref) {
        this.ctaHref = ctaHref;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public void setBackgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
    }
}
