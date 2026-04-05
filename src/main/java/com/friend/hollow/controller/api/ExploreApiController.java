package com.friend.hollow.controller.api;

import com.friend.hollow.dto.ExploreCommunityFeedResponse;
import com.friend.hollow.service.ExploreCommunityFeedService;
import com.friend.hollow.util.PagePathUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 探索页相关 JSON 接口。
 */
@RestController
public class ExploreApiController {

    private final ExploreCommunityFeedService exploreCommunityFeedService;

    public ExploreApiController(ExploreCommunityFeedService exploreCommunityFeedService) {
        this.exploreCommunityFeedService = exploreCommunityFeedService;
    }

    /**
     * 互助社区滚动卡片数据；可选 {@code app.explore.community-external-url} 拉取 JSON 数组合并（字段与文档说明一致）。
     */
    @GetMapping(PagePathUtil.EXPLORE_COMMUNITY_FEED_API)
    public ExploreCommunityFeedResponse communityFeed() {
        return exploreCommunityFeedService.getFeed();
    }
}
