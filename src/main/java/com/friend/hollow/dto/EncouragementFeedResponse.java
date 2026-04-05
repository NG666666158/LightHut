package com.friend.hollow.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页「挚友的鼓励」滚动区：多行文案。
 */
public class EncouragementFeedResponse {

    private List<EncouragementLineDto> lines = new ArrayList<>();

    public List<EncouragementLineDto> getLines() {
        return lines;
    }

    public void setLines(List<EncouragementLineDto> lines) {
        this.lines = lines != null ? lines : new ArrayList<>();
    }
}
