package com.friend.hollow.dto;

/**
 * 主页概览接口返回对象（DTO）。
 *
 * <p>为什么用 DTO 而不是 Map：
 * 1) 字段语义清晰，便于前后端联调；
 * 2) 未来接数据库时更容易扩展和维护；
 * 3) IDEA 可自动提示字段，减少拼写错误。</p>
 *
 * <p>可修改点：
 * 你后续有新卡片或统计信息时，在这里新增字段即可。</p>
 */
public class HomeOverviewResponse {

    /**
     * 用户昵称（左侧头像下方显示）。
     */
    private String nickname;

    /**
     * 在线时长文案（例如“已在线 2 小时”）。
     */
    private String onlineDuration;

    /**
     * 左侧栏第二行（与全站侧栏一致，如「今日心情：宁静」）。
     */
    private String sidebarSubtitle;

    /**
     * 左侧栏头像 URL。
     */
    private String avatarUrl;

    /**
     * 冥想连续天数（用于“成长森林”卡片）。
     */
    private Integer meditationStreakDays;

    /**
     * 成长进度百分比（0~100，对应进度条宽度）。
     */
    private Integer growthProgress;

    /**
     * 鼓励文案（用于“挚友的鼓励”卡片）。
     */
    private String encouragementText;

    /**
     * 便签署名（例如「来自林小暖的匿名便签」）。
     */
    private String encouragementSignature;

    /**
     * 今天是否已签到（与签到接口同源，便于首屏一次拉齐）。
     */
    private Boolean signedToday;

    /**
     * 树苗阶段 1–5（与签到接口同源）。
     */
    private Integer treeStage;

    /**
     * 每日冥想主区卡片。
     */
    private DailyMeditationResponse dailyMeditation;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOnlineDuration() {
        return onlineDuration;
    }

    public void setOnlineDuration(String onlineDuration) {
        this.onlineDuration = onlineDuration;
    }

    public String getSidebarSubtitle() {
        return sidebarSubtitle;
    }

    public void setSidebarSubtitle(String sidebarSubtitle) {
        this.sidebarSubtitle = sidebarSubtitle;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Integer getMeditationStreakDays() {
        return meditationStreakDays;
    }

    public void setMeditationStreakDays(Integer meditationStreakDays) {
        this.meditationStreakDays = meditationStreakDays;
    }

    public Integer getGrowthProgress() {
        return growthProgress;
    }

    public void setGrowthProgress(Integer growthProgress) {
        this.growthProgress = growthProgress;
    }

    public String getEncouragementText() {
        return encouragementText;
    }

    public void setEncouragementText(String encouragementText) {
        this.encouragementText = encouragementText;
    }

    public String getEncouragementSignature() {
        return encouragementSignature;
    }

    public void setEncouragementSignature(String encouragementSignature) {
        this.encouragementSignature = encouragementSignature;
    }

    public Boolean getSignedToday() {
        return signedToday;
    }

    public void setSignedToday(Boolean signedToday) {
        this.signedToday = signedToday;
    }

    public Integer getTreeStage() {
        return treeStage;
    }

    public void setTreeStage(Integer treeStage) {
        this.treeStage = treeStage;
    }

    public DailyMeditationResponse getDailyMeditation() {
        return dailyMeditation;
    }

    public void setDailyMeditation(DailyMeditationResponse dailyMeditation) {
        this.dailyMeditation = dailyMeditation;
    }
}
