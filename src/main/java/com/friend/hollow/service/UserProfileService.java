package com.friend.hollow.service;

/**
 * 首页展示用用户档案（昵称、鼓励语、在线时长文案等），数据来自配置，可后续换持久化。
 */
public interface UserProfileService {

    String getNickname();

    String getEncouragementText();

    String getEncouragementSignature();

    /**
     * 左侧栏第二行展示（如「今日心情：宁静」），与各页模板一致。
     */
    String getSidebarSubtitle();

    /**
     * 左侧栏头像 URL（通常为 /images/... 静态资源）。
     */
    String getAvatarUrl();

    /**
     * 根据当前 HTTP 会话创建时间计算「已在线」文案。
     */
    String formatOnlineDuration(long sessionCreationTimeMs);
}
