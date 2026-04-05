package com.friend.hollow.dto;

/**
 * 单条鼓励文案（挚友卡片滚动列表 / 外部接口一行）。
 */
public class EncouragementLineDto {

    private String text;
    private String signature;

    public EncouragementLineDto() {
    }

    public EncouragementLineDto(String text, String signature) {
        this.text = text;
        this.signature = signature;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
