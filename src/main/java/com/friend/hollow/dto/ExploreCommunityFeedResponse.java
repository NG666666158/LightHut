package com.friend.hollow.dto;

import java.util.ArrayList;
import java.util.List;

public class ExploreCommunityFeedResponse {

    private List<ExploreCommunityPostDto> posts = new ArrayList<>();

    public List<ExploreCommunityPostDto> getPosts() {
        return posts;
    }

    public void setPosts(List<ExploreCommunityPostDto> posts) {
        this.posts = posts != null ? posts : new ArrayList<>();
    }
}
