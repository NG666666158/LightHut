package com.friend.hollow.service;

import com.friend.hollow.dto.SignInActionResponse;
import com.friend.hollow.dto.SignInStatusResponse;

/**
 * 签到系统业务接口。
 *
 * <p>当前版本先使用内存存储，适合单机演示。
 * 后续可替换成数据库实现（按 userId 维度存储）。</p>
 */
public interface SignInService {

    /**
     * 查询签到状态。
     *
     * @return 状态对象
     */
    SignInStatusResponse getStatus();

    /**
     * 执行签到。
     *
     * @return 签到结果对象
     */
    SignInActionResponse signIn();
}
