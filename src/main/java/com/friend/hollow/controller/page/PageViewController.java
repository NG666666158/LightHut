package com.friend.hollow.controller.page;

import com.friend.hollow.util.PagePathUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面路由控制器（只负责返回模板，不处理 JSON 数据）。
 */
@Controller
public class PageViewController {

    /**
     * 落地欢迎页（未登录）。
     */
    @GetMapping(PagePathUtil.LOGIN_PAGE_ROUTE)
    public String showLandingPage() {
        return PagePathUtil.LANDING_TEMPLATE;
    }

    /**
     * 主页（欢迎页点击进入）。
     */
    @GetMapping(PagePathUtil.HOME_PAGE_ROUTE)
    public String showHomePage() {
        return PagePathUtil.HOME_TEMPLATE;
    }

    @GetMapping(PagePathUtil.CHEER_PAGE_ROUTE)
    public String showCheerPage() {
        return PagePathUtil.CHEER_TEMPLATE;
    }

    @GetMapping(PagePathUtil.STARLIGHT_PAGE_ROUTE)
    public String showStarlightPage() {
        return PagePathUtil.STARLIGHT_TEMPLATE;
    }

    @GetMapping(PagePathUtil.REMINDER_PAGE_ROUTE)
    public String showReminderPage() {
        return PagePathUtil.REMINDER_TEMPLATE;
    }

    @GetMapping(PagePathUtil.MEDITATION_PAGE_ROUTE)
    public String showMeditationPage() {
        return PagePathUtil.MEDITATION_TEMPLATE;
    }

    @GetMapping(PagePathUtil.EXPLORE_PAGE_ROUTE)
    public String showExplorePage() {
        return PagePathUtil.EXPLORE_TEMPLATE;
    }
}
