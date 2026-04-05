package com.friend.hollow.service.impl;

import com.friend.hollow.service.UserProfileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 从 application.properties 读取默认展示信息。
 */
@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Value("${app.home.nickname:\u918b\u9178\u94a0}")
    private String nickname;

    @Value("${app.home.encouragement-text:\u4f60\u4eca\u5929\u770b\u8d77\u6765\u5f88\u6e29\u67d4\u3002}")
    private String encouragementText;

    @Value("${app.home.encouragement-signature:\u6765\u81ea\u918b\u9178\u94a0\u7684\u533f\u540d\u4fbf\u7b7e}")
    private String encouragementSignature;

    @Value("${app.home.sidebar-subtitle:\u4eca\u65e5\u5fc3\u60c5\uff1a\u5b81\u9759}")
    private String sidebarSubtitle;

    @Value("${app.home.avatar-url:/images/profile-avatar.png}")
    private String avatarUrl;

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public String getEncouragementText() {
        return encouragementText;
    }

    @Override
    public String getEncouragementSignature() {
        return encouragementSignature;
    }

    @Override
    public String getSidebarSubtitle() {
        return sidebarSubtitle;
    }

    @Override
    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public String formatOnlineDuration(long sessionCreationTimeMs) {
        long now = System.currentTimeMillis();
        long elapsed = Math.max(0L, now - sessionCreationTimeMs);
        long minutes = elapsed / 60_000L;
        if (minutes < 1L) {
            return "刚刚到来";
        }
        if (minutes < 60L) {
            return "已在线 " + minutes + " 分钟";
        }
        long hours = minutes / 60L;
        long restMin = minutes % 60L;
        if (hours < 24L) {
            if (restMin > 0L) {
                return "已在线 " + hours + " 小时 " + restMin + " 分钟";
            }
            return "已在线 " + hours + " 小时";
        }
        long days = hours / 24L;
        return "已在线 " + days + " 天+";
    }
}
