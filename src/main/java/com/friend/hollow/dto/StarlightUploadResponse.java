package com.friend.hollow.dto;

/**
 * 星光墙图片上传结果（前端用返回的 url 作为图片地址）。
 */
public class StarlightUploadResponse {

    private String url;
    private String originalFilename;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }
}
