package com.friend.hollow.service;

import com.friend.hollow.dto.MemoryCreateRequest;
import com.friend.hollow.dto.MemoryItemResponse;
import com.friend.hollow.dto.MemoryListResponse;
import com.friend.hollow.dto.StarlightUploadResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * 友谊星光墙业务（回忆列表、上传、创建）。
 */
public interface StarlightService {

    /**
     * 分页查询回忆列表。
     *
     * @param category 分类编码，空或 ALL 表示全部
     * @param sort     createdAt 或 eventDate
     * @param order    asc 或 desc
     */
    MemoryListResponse listMemories(String category, String sort, String order, int page, int size);

    /**
     * 仅上传图片，返回可访问 URL。
     */
    StarlightUploadResponse uploadImage(MultipartFile file);

    /**
     * 使用 JSON 创建回忆（需先有 imageUrl，通常先调 upload）。
     */
    MemoryItemResponse createMemory(MemoryCreateRequest request);

    /**
     * 表单一次性上传图片并创建回忆。
     */
    MemoryItemResponse createMemoryWithImage(
            MultipartFile image,
            String title,
            String description,
            String category,
            String eventDate,
            String companions,
            String layout
    );

    /**
     * 删除一条回忆（若图片为本地上传文件则一并删除文件）。
     */
    void deleteMemory(long id);
}
