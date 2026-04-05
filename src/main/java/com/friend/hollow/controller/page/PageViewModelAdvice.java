package com.friend.hollow.controller.page;

import com.friend.hollow.service.UserProfileService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * 为 {@link PageViewController} 返回的 Thymeleaf 页面注入侧栏档案，保证各页与首页一致。
 */
@ControllerAdvice(assignableTypes = PageViewController.class)
public class PageViewModelAdvice {

    private final UserProfileService userProfileService;

    public PageViewModelAdvice(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @ModelAttribute("navNickname")
    public String navNickname() {
        return userProfileService.getNickname();
    }

    @ModelAttribute("navSidebarSubtitle")
    public String navSidebarSubtitle() {
        return userProfileService.getSidebarSubtitle();
    }

    @ModelAttribute("navAvatarUrl")
    public String navAvatarUrl() {
        return userProfileService.getAvatarUrl();
    }
}
