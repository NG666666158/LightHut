package com.friend.hollow.dto;

/**
 * 签到状态返回对象。
 *
 * <p>用途：
 * 前端加载首页时调用该接口，展示当前签到状态与成长树阶段。</p>
 */
public class SignInStatusResponse {

    /**
     * 今天是否已经签到。
     */
    private boolean signedToday;

    /**
     * 当前连续签到天数。
     */
    private int streakDays;

    /**
     * 成长进度百分比（0-100）。
     */
    private int growthProgress;

    /**
     * 树苗成长阶段（1-5）。
     */
    private int treeStage;

    public boolean isSignedToday() {
        return signedToday;
    }

    public void setSignedToday(boolean signedToday) {
        this.signedToday = signedToday;
    }

    public int getStreakDays() {
        return streakDays;
    }

    public void setStreakDays(int streakDays) {
        this.streakDays = streakDays;
    }

    public int getGrowthProgress() {
        return growthProgress;
    }

    public void setGrowthProgress(int growthProgress) {
        this.growthProgress = growthProgress;
    }

    public int getTreeStage() {
        return treeStage;
    }

    public void setTreeStage(int treeStage) {
        this.treeStage = treeStage;
    }
}
