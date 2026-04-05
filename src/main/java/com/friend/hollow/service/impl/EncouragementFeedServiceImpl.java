package com.friend.hollow.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.friend.hollow.dto.EncouragementFeedResponse;
import com.friend.hollow.dto.EncouragementLineDto;
import com.friend.hollow.service.EncouragementFeedService;
import com.friend.hollow.service.UserProfileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class EncouragementFeedServiceImpl implements EncouragementFeedService {

    private static final List<EncouragementLineDto> BUILT_IN = List.of(
            new EncouragementLineDto("慢慢来，今天的你已经足够勇敢。", "清新小屋"),
            new EncouragementLineDto("允许自己休息，不是偷懒，是在充电。", "清新小屋"),
            new EncouragementLineDto("夜里也会有星光，你也是。", "清新小屋"),
            new EncouragementLineDto("被看见、被在乎，你值得。", "清新小屋"),
            new EncouragementLineDto("风雨会停，你会被温柔接住。", "清新小屋"),
            new EncouragementLineDto("再坚持一下下，光就在拐弯处。", "清新小屋"),
            new EncouragementLineDto("你笑起来的样子，世界都亮一点。", "清新小屋"),
            new EncouragementLineDto("心事可以变轻，像云被风吹散一点点。", "清新小屋"),
            new EncouragementLineDto("Good night · 月亮替你关一盏 worry。", "清新小屋"),
            new EncouragementLineDto("你的心跳很值得被珍惜。", "清新小屋")
    );

    private final UserProfileService userProfileService;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final String externalUrl;

    public EncouragementFeedServiceImpl(
            UserProfileService userProfileService,
            ObjectMapper objectMapper,
            @Value("${app.home.encouragement-external-url:}") String externalUrl
    ) {
        this.userProfileService = userProfileService;
        this.objectMapper = objectMapper;
        this.externalUrl = StringUtils.hasText(externalUrl) ? externalUrl.trim() : null;
        SimpleClientHttpRequestFactory f = new SimpleClientHttpRequestFactory();
        f.setConnectTimeout(Duration.ofSeconds(3));
        f.setReadTimeout(Duration.ofSeconds(5));
        this.restTemplate = new RestTemplate(f);
    }

    @Override
    public EncouragementFeedResponse getFeed() {
        List<EncouragementLineDto> merged = new ArrayList<>();
        Set<String> seen = new LinkedHashSet<>();

        addUnique(merged, seen,
                userProfileService.getEncouragementText(),
                userProfileService.getEncouragementSignature());

        for (EncouragementLineDto line : BUILT_IN) {
            addUnique(merged, seen, line.getText(), line.getSignature());
        }

        for (EncouragementLineDto ext : fetchExternalLines()) {
            addUnique(merged, seen, ext.getText(), ext.getSignature());
        }

        EncouragementFeedResponse res = new EncouragementFeedResponse();
        res.setLines(merged);
        return res;
    }

    private void addUnique(List<EncouragementLineDto> out, Set<String> seen, String text, String signature) {
        if (!StringUtils.hasText(text)) {
            return;
        }
        String key = text.trim();
        if (!seen.add(key)) {
            return;
        }
        String sig = StringUtils.hasText(signature) ? signature.trim() : "匿名便签";
        out.add(new EncouragementLineDto(key, sig));
    }

    private List<EncouragementLineDto> fetchExternalLines() {
        if (!StringUtils.hasText(externalUrl)) {
            return List.of();
        }
        try {
            String body = restTemplate.getForObject(externalUrl, String.class);
            if (!StringUtils.hasText(body)) {
                return List.of();
            }
            JsonNode root = objectMapper.readTree(body);
            List<EncouragementLineDto> list = new ArrayList<>();
            if (root.isArray()) {
                for (JsonNode n : root) {
                    String text = firstNonBlank(
                            textField(n, "text"),
                            textField(n, "content"),
                            textField(n, "quote")
                    );
                    if (!StringUtils.hasText(text)) {
                        continue;
                    }
                    String sig = firstNonBlank(
                            textField(n, "signature"),
                            textField(n, "author"),
                            textField(n, "source")
                    );
                    if (!StringUtils.hasText(sig)) {
                        sig = "来自网络";
                    }
                    list.add(new EncouragementLineDto(text.trim(), sig.trim()));
                }
            }
            return list;
        } catch (Exception ignored) {
            return List.of();
        }
    }

    private static String firstNonBlank(String... xs) {
        if (xs == null) {
            return null;
        }
        for (String x : xs) {
            if (StringUtils.hasText(x)) {
                return x;
            }
        }
        return null;
    }

    private static String textField(JsonNode n, String field) {
        JsonNode v = n.get(field);
        if (v == null || v.isNull()) {
            return null;
        }
        if (v.isTextual()) {
            String s = v.asText();
            return StringUtils.hasText(s) ? s.trim() : null;
        }
        if (v.isNumber()) {
            return v.asText();
        }
        return null;
    }
}
