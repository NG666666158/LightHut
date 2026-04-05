package com.friend.hollow.controller.api;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthApiController {

    @GetMapping("/api/auth/me")
    public Map<String, Object> me(Authentication authentication) {
        boolean authed = authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
        if (!authed) {
            return Map.of("authenticated", false);
        }
        return Map.of("authenticated", true, "username", authentication.getName());
    }
}
