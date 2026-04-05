package com.friend.hollow.dto;

/**
 * 冥想页白噪音音轨（根目录 mp3 文件夹内文件）。
 */
public class MeditationNoiseTrackDto {

    private String id;
    private String fileName;
    private String url;
    private String label;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
