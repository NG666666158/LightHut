package com.friend.hollow.service;

import com.friend.hollow.dto.HomeOverviewResponse;

/**
 * 主页概览业务接口。
 *
 * <p>职责：
 * 对外提供“主页需要的数据”，Controller 只负责调用，不直接写业务细节。</p>
 *
 * <p>可修改点：
 * 后续接 MySQL/Redis 时，保留该接口不变，替换实现类即可。</p>
 */
public interface HomeOverviewService {

    /**
     * 获取主页概览数据。
     *
     * @param sessionCreationTimeMs 当前会话创建时间（毫秒），用于计算「已在线」文案
     * @return 主页概览 DTO
     */
    HomeOverviewResponse getOverview(long sessionCreationTimeMs);
}
