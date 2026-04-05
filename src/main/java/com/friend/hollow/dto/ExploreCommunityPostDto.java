package com.friend.hollow.dto;

/**
 * 探索页「互助社区」单条展示（内置、外部 JSON 或用户本地投递）。
 */
public class ExploreCommunityPostDto {

    private String id;
    private String text;
    private String author;
    private int likes;
    private String timeLabel;

    public ExploreCommunityPostDto() {
    }

    public ExploreCommunityPostDto(String id, String text, String author, int likes, String timeLabel) {
        this.id = id;
        this.text = text;
        this.author = author;
        this.likes = likes;
        this.timeLabel = timeLabel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getTimeLabel() {
        return timeLabel;
    }

    public void setTimeLabel(String timeLabel) {
        this.timeLabel = timeLabel;
    }
}
