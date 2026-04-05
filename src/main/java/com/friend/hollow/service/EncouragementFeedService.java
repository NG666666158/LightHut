package com.friend.hollow.service;

import com.friend.hollow.dto.EncouragementFeedResponse;

/**
 * 聚合个人配置、内置金句与可选外部 HTTP JSON，供首页挚友鼓励滚动区使用。
 */
public interface EncouragementFeedService {

    EncouragementFeedResponse getFeed();
}
