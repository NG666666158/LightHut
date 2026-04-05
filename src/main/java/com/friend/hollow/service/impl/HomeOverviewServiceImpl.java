package com.friend.hollow.service.impl;

import com.friend.hollow.dto.DailyMeditationResponse;
import com.friend.hollow.dto.HomeOverviewResponse;
import com.friend.hollow.service.HomeOverviewService;
import com.friend.hollow.service.SignInService;
import com.friend.hollow.service.UserProfileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * 主页概览：与签到同源；昵称/鼓励语来自配置；每日冥想按星期轮换。
 */
@Service
public class HomeOverviewServiceImpl implements HomeOverviewService {

    private final SignInService signInService;
    private final UserProfileService userProfileService;

    @Value("${app.home.daily-meditation-bg}")
    private String dailyMeditationBgUrl;

    public HomeOverviewServiceImpl(SignInService signInService, UserProfileService userProfileService) {
        this.signInService = signInService;
        this.userProfileService = userProfileService;
    }

    @Override
    public HomeOverviewResponse getOverview(long sessionCreationTimeMs) {
        var status = signInService.getStatus();
        HomeOverviewResponse response = new HomeOverviewResponse();
        response.setNickname(userProfileService.getNickname());
        response.setOnlineDuration(userProfileService.formatOnlineDuration(sessionCreationTimeMs));
        response.setSidebarSubtitle(userProfileService.getSidebarSubtitle());
        response.setAvatarUrl(userProfileService.getAvatarUrl());
        response.setMeditationStreakDays(status.getStreakDays());
        response.setGrowthProgress(status.getGrowthProgress());
        response.setEncouragementText(userProfileService.getEncouragementText());
        response.setEncouragementSignature(userProfileService.getEncouragementSignature());
        response.setSignedToday(status.isSignedToday());
        response.setTreeStage(status.getTreeStage());
        response.setDailyMeditation(buildDailyMeditation(dailyMeditationBgUrl));
        return response;
    }

    private DailyMeditationResponse buildDailyMeditation(String bgUrl) {
        int idx = LocalDate.now().getDayOfWeek().getValue() - 1;
        String[] tags = {"每日冥想", "每日冥想", "午间静心", "每日冥想", "温柔收尾", "周末疗愈", "周日觉察"};
        String[] titles = {
                "晨间的呼吸练习",
                "专注当下的力量",
                "午间三分钟放空",
                "傍晚的情绪梳理",
                "睡前的身体扫描",
                "周末的自然之声",
                "周日的自我对话"
        };
        String[] subtitles = {
                "用三分钟的时间，找回你内在的宁静与力量。今天，让我们从觉察呼吸开始。",
                "把注意力轻轻带回此刻，不必评判，只需看见。",
                "闭眼片刻，让肩膀与眉心一起松开。",
                "把今天的情绪轻轻摊开，像整理一页便签。",
                "从脚尖到头顶，慢慢感受身体的重量与温度。",
                "想象一片树林或海浪，让呼吸与它们同频。",
                "写下一句想对自己说的话，温柔即可。"
        };
        DailyMeditationResponse d = new DailyMeditationResponse();
        d.setTag(tags[idx]);
        d.setTitle(titles[idx]);
        d.setSubtitle(subtitles[idx]);
        d.setCtaLabel("开启旅程");
        d.setCtaHref("#");
        d.setBackgroundImageUrl(bgUrl);
        return d;
    }
}
