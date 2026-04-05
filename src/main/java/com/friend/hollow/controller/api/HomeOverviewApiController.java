package com.friend.hollow.controller.api;

import com.friend.hollow.dto.HomeOverviewResponse;
import com.friend.hollow.dto.SignInActionResponse;
import com.friend.hollow.dto.SignInStatusResponse;
import com.friend.hollow.service.HomeOverviewService;
import com.friend.hollow.service.SignInService;
import com.friend.hollow.util.PagePathUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 主页数据接口控制器（只处理 API，不负责页面跳转）。
 *
 * <p>分层原则：
 * - 页面跳转走 page controller
 * - 数据返回走 api controller
 * 这样后续新增页面或新增接口时结构更清晰。</p>
 */
@RestController
public class HomeOverviewApiController {

    private final HomeOverviewService homeOverviewService;
    private final SignInService signInService;

    public HomeOverviewApiController(HomeOverviewService homeOverviewService, SignInService signInService) {
        this.homeOverviewService = homeOverviewService;
        this.signInService = signInService;
    }

    /**
     * 主页概览接口。
     *
     * @return 主页数据（JSON）
     */
    @GetMapping(PagePathUtil.HOME_OVERVIEW_API)
    public HomeOverviewResponse homeOverview(HttpSession session) {
        long sessionStartMs = session != null ? session.getCreationTime() : System.currentTimeMillis();
        return homeOverviewService.getOverview(sessionStartMs);
    }

    /**
     * 查询签到状态接口。
     *
     * @return 当前签到状态
     */
    @GetMapping(PagePathUtil.HOME_SIGN_IN_STATUS_API)
    public SignInStatusResponse signInStatus() {
        return signInService.getStatus();
    }

    /**
     * 执行签到接口。
     *
     * @return 签到结果
     */
    @PostMapping(PagePathUtil.HOME_SIGN_IN_ACTION_API)
    public SignInActionResponse signInAction() {
        return signInService.signIn();
    }
}
