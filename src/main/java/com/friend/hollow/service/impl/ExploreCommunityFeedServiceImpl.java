package com.friend.hollow.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.friend.hollow.dto.ExploreCommunityFeedResponse;
import com.friend.hollow.dto.ExploreCommunityPostDto;
import com.friend.hollow.service.ExploreCommunityFeedService;
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
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ExploreCommunityFeedServiceImpl implements ExploreCommunityFeedService {

    private static final List<ExploreCommunityPostDto> BUILT_IN = List.of(
            new ExploreCommunityPostDto("c-seed-1", "慢慢来，允许自己有低落的日子。我们在云端拥抱你。", "匿名飞鸟", 24, "2 小时前"),
            new ExploreCommunityPostDto("c-seed-2", "今天的焦虑好像少了一点点，虽然只有一点点，但也值得庆祝。", "寻找微光", 18, "5 小时前"),
            new ExploreCommunityPostDto("c-seed-3", "看到大家的留言，觉得不是一个人在战斗。谢谢你们的存在。", "星空守望", 31, "昨天"),
            new ExploreCommunityPostDto("c-seed-4", "把心事折成纸飞机吧，风会带走一点重量，留下轻盈的你。", "晚风信笺", 15, "3 天前"),
            new ExploreCommunityPostDto("c-seed-5", "今天没有做得很好也没关系，你还在，就已经很棒了。", "云朵邮差", 22, "1 小时前"),
            new ExploreCommunityPostDto("c-seed-6", "给自己泡一杯热的吧，暖胃也暖心。", "橘子汽水", 19, "4 小时前"),
            new ExploreCommunityPostDto("c-seed-7", "你不是麻烦，你是值得被温柔对待的人。", "薄荷午后", 27, "昨天"),
            new ExploreCommunityPostDto("c-seed-8", "天黑了就点灯，累了就歇一歇，世界不会怪你。", "月亮饼干", 14, "2 天前"),
            new ExploreCommunityPostDto("c-seed-9", "有人正在远方为你加油，只是你还不知道。", "小森林", 33, "6 小时前"),
            new ExploreCommunityPostDto("c-seed-10", "哭完记得擦擦脸，你笑起来真的很好看。", "晚风信箱", 21, "昨天"),
            new ExploreCommunityPostDto("c-seed-11", "不必强撑坚强，柔软也是一种力量。", "星光旅人", 17, "3 小时前"),
            new ExploreCommunityPostDto("c-seed-12", "今天的你，已经比昨天多走了一步。", "藏青林", 26, "5 天前"),
            new ExploreCommunityPostDto("c-seed-13", "如果睡不着，就听听雨声或自己的呼吸，都没关系的。", "雨声笔记", 20, "8 小时前"),
            new ExploreCommunityPostDto("c-seed-14", "被误解的时候，先抱抱自己，真相会慢慢浮上来。", "灯塔少年", 16, "2 天前"),
            new ExploreCommunityPostDto("c-seed-15", "生活有时会卡壳，但你不是卡住的机器，你是会呼吸的人。", "松果与茶", 29, "昨天"),
            new ExploreCommunityPostDto("c-seed-16", "谢谢你还在坚持，这本身就像一束光。", "向日葵边", 35, "1 天前"),
            new ExploreCommunityPostDto("c-seed-17", "不必和任何人比进度，你的时区刚刚好。", "时钟兔子", 13, "4 天前"),
            new ExploreCommunityPostDto("c-seed-18", "难过的时候，允许自己什么都不想做。", "空白日历", 23, "12 小时前"),
            new ExploreCommunityPostDto("c-seed-19", "朋友不在多，有人懂你的沉默就够了。", "沉默海岸", 28, "3 天前"),
            new ExploreCommunityPostDto("c-seed-20", "明天的事明天再想，今晚先好好睡觉。", "晚安岛屿", 30, "刚刚")
    );

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final String externalUrl;

    public ExploreCommunityFeedServiceImpl(
            ObjectMapper objectMapper,
            @Value("${app.explore.community-external-url:}") String externalUrl
    ) {
        this.objectMapper = objectMapper;
        this.externalUrl = StringUtils.hasText(externalUrl) ? externalUrl.trim() : null;
        SimpleClientHttpRequestFactory f = new SimpleClientHttpRequestFactory();
        f.setConnectTimeout(Duration.ofSeconds(3));
        f.setReadTimeout(Duration.ofSeconds(5));
        this.restTemplate = new RestTemplate(f);
    }

    @Override
    public ExploreCommunityFeedResponse getFeed() {
        List<ExploreCommunityPostDto> out = new ArrayList<>();
        Set<String> seenIds = new LinkedHashSet<>();
        Set<String> seenText = new LinkedHashSet<>();

        for (ExploreCommunityPostDto p : BUILT_IN) {
            addPost(out, seenIds, seenText, p);
        }

        AtomicInteger extCounter = new AtomicInteger(1);
        for (ExploreCommunityPostDto p : fetchExternalPosts(extCounter)) {
            addPost(out, seenIds, seenText, p);
        }

        ExploreCommunityFeedResponse res = new ExploreCommunityFeedResponse();
        res.setPosts(out);
        return res;
    }

    private static void addPost(
            List<ExploreCommunityPostDto> out,
            Set<String> seenIds,
            Set<String> seenText,
            ExploreCommunityPostDto p
    ) {
        if (p == null || !StringUtils.hasText(p.getText())) {
            return;
        }
        String tid = StringUtils.hasText(p.getId()) ? p.getId().trim() : null;
        String tkey = p.getText().trim();
        if (seenText.contains(tkey)) {
            return;
        }
        if (tid != null && !seenIds.add(tid)) {
            return;
        }
        if (tid == null) {
            return;
        }
        seenText.add(tkey);
        out.add(p);
    }

    private List<ExploreCommunityPostDto> fetchExternalPosts(AtomicInteger idCounter) {
        if (!StringUtils.hasText(externalUrl)) {
            return List.of();
        }
        try {
            String body = restTemplate.getForObject(externalUrl, String.class);
            if (!StringUtils.hasText(body)) {
                return List.of();
            }
            JsonNode root = objectMapper.readTree(body);
            if (!root.isArray()) {
                return List.of();
            }
            List<ExploreCommunityPostDto> list = new ArrayList<>();
            for (JsonNode n : root) {
                String text = firstNonBlank(
                        textField(n, "text"),
                        textField(n, "content"),
                        textField(n, "message")
                );
                if (!StringUtils.hasText(text)) {
                    continue;
                }
                String author = firstNonBlank(
                        textField(n, "author"),
                        textField(n, "nickname"),
                        textField(n, "name")
                );
                if (!StringUtils.hasText(author)) {
                    author = "来自远方";
                }
                String id = textField(n, "id");
                if (!StringUtils.hasText(id)) {
                    id = "ec-ext-" + idCounter.getAndIncrement();
                }
                int likes = n.path("likes").isNumber() ? n.path("likes").asInt(0) : 0;
                String timeLabel = firstNonBlank(
                        textField(n, "timeLabel"),
                        textField(n, "time"),
                        textField(n, "ago")
                );
                if (!StringUtils.hasText(timeLabel)) {
                    timeLabel = "近期";
                }
                ExploreCommunityPostDto p = new ExploreCommunityPostDto();
                p.setId(id.trim());
                p.setText(text.trim());
                p.setAuthor(author.trim());
                p.setLikes(Math.max(0, likes));
                p.setTimeLabel(timeLabel.trim());
                list.add(p);
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
