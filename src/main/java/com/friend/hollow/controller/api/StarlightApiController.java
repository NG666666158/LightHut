package com.friend.hollow.controller.api;

import com.friend.hollow.dto.MemoryCreateRequest;
import com.friend.hollow.dto.MemoryItemResponse;
import com.friend.hollow.dto.MemoryListResponse;
import com.friend.hollow.dto.StarlightUploadResponse;
import com.friend.hollow.service.StarlightService;
import com.friend.hollow.util.PagePathUtil;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 友谊星光墙 JSON 接口。
 */
@RestController
public class StarlightApiController {

    private final StarlightService starlightService;

    public StarlightApiController(StarlightService starlightService) {
        this.starlightService = starlightService;
    }

    @GetMapping(PagePathUtil.STARLIGHT_MEMORIES_API)
    public MemoryListResponse list(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        return starlightService.listMemories(category, sort, order, page, size);
    }

    @PostMapping(value = PagePathUtil.STARLIGHT_UPLOAD_API, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StarlightUploadResponse upload(@RequestPart("file") MultipartFile file) {
        return starlightService.uploadImage(file);
    }

    @PostMapping(value = PagePathUtil.STARLIGHT_MEMORIES_API, consumes = MediaType.APPLICATION_JSON_VALUE)
    public MemoryItemResponse createJson(@Valid @RequestBody MemoryCreateRequest request) {
        return starlightService.createMemory(request);
    }

    @PostMapping(value = PagePathUtil.STARLIGHT_MEMORIES_API, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MemoryItemResponse createMultipart(
            @RequestPart("image") MultipartFile image,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam String category,
            @RequestParam(required = false) String eventDate,
            @RequestParam(required = false) String companions,
            @RequestParam(defaultValue = "COMPACT") String layout
    ) {
        return starlightService.createMemoryWithImage(
                image, title, description, category, eventDate, companions, layout
        );
    }

    @DeleteMapping(PagePathUtil.STARLIGHT_MEMORIES_API + "/{id}")
    public Map<String, Object> delete(@PathVariable long id) {
        starlightService.deleteMemory(id);
        return Map.of("success", true, "id", id);
    }
}
