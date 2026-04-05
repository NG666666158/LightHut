package com.friend.hollow.dto;

import java.util.List;

/**
 * 星光墙分页列表响应。
 */
public class MemoryListResponse {

    private List<MemoryItemResponse> items;
    private int page;
    private int size;
    private long total;
    private boolean hasMore;

    public List<MemoryItemResponse> getItems() {
        return items;
    }

    public void setItems(List<MemoryItemResponse> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }
}
