package com.friend.hollow.service.impl;

import com.friend.hollow.dto.MemoryCreateRequest;
import com.friend.hollow.dto.MemoryItemResponse;
import com.friend.hollow.dto.MemoryListResponse;
import com.friend.hollow.dto.StarlightUploadResponse;
import com.friend.hollow.model.MemoryCategory;
import com.friend.hollow.model.MemoryLayout;
import com.friend.hollow.model.MemoryRecord;
import com.friend.hollow.repository.MemoryRepository;
import com.friend.hollow.service.StarlightService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 星光墙实现：内存存储回忆元数据；图片文件落盘到本地目录。
 */
@Service
public class StarlightServiceImpl implements StarlightService {

    private static final List<String> ALLOWED_IMAGE_TYPES = List.of(
            "image/jpeg", "image/png", "image/webp", "image/gif"
    );

    private final Path uploadRoot;
    private final MemoryRepository memoryRepository;

    public StarlightServiceImpl(
            @Value("${app.starlight.upload-dir:uploads/starlight}") String uploadDir,
            MemoryRepository memoryRepository
    ) {
        this.uploadRoot = Path.of(uploadDir).toAbsolutePath().normalize();
        this.memoryRepository = memoryRepository;
    }

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(uploadRoot);
        if (memoryRepository.findAll().isEmpty()) {
            seedDemoMemories();
        }
    }

    @Override
    public synchronized MemoryListResponse listMemories(String category, String sort, String order, int page, int size) {
        String sortField = StringUtils.hasText(sort) ? sort.trim().toLowerCase(Locale.ROOT) : "createdat";
        boolean desc = !"asc".equalsIgnoreCase(order);
        MemoryCategory filter = parseCategoryFilter(category);

        List<MemoryRecord> filtered = memoryRepository.findAll().stream()
                .filter(m -> filter == null || m.getCategory() == filter)
                .collect(Collectors.toList());

        Comparator<MemoryRecord> cmp;
        if ("eventdate".equals(sortField)) {
            cmp = Comparator.comparing(MemoryRecord::getEventDate, Comparator.nullsLast(Comparator.naturalOrder()));
        } else {
            cmp = Comparator.comparing(MemoryRecord::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder()));
        }
        if (desc) {
            cmp = cmp.reversed();
        }
        Comparator<MemoryRecord> idCmp = desc
                ? Comparator.comparing(MemoryRecord::getId, Comparator.nullsLast(Comparator.reverseOrder()))
                : Comparator.comparing(MemoryRecord::getId, Comparator.nullsLast(Comparator.naturalOrder()));
        filtered.sort(cmp.thenComparing(idCmp));

        int p = Math.max(0, page);
        int s = Math.min(50, Math.max(1, size));
        long total = filtered.size();
        int from = p * s;
        int to = (int) Math.min(from + s, total);
        List<MemoryItemResponse> items = new ArrayList<>();
        if (from < filtered.size()) {
            for (MemoryRecord m : filtered.subList(from, to)) {
                items.add(toDto(m));
            }
        }

        MemoryListResponse res = new MemoryListResponse();
        res.setItems(items);
        res.setPage(p);
        res.setSize(s);
        res.setTotal(total);
        res.setHasMore(to < total);
        return res;
    }

    /**
     * null = 不过滤；ALL 或空字符串 = 不过滤。
     */
    private MemoryCategory parseCategoryFilter(String category) {
        if (!StringUtils.hasText(category)) {
            return null;
        }
        String t = category.trim();
        if ("ALL".equalsIgnoreCase(t)) {
            return null;
        }
        return MemoryCategory.fromCode(t);
    }

    @Override
    public synchronized StarlightUploadResponse uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("请选择图片文件");
        }
        validateImage(file);
        String url = saveFile(file);
        StarlightUploadResponse r = new StarlightUploadResponse();
        r.setUrl(url);
        r.setOriginalFilename(file.getOriginalFilename());
        return r;
    }

    @Override
    public synchronized MemoryItemResponse createMemory(MemoryCreateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("请求体不能为空");
        }
        if (!StringUtils.hasText(request.getTitle())) {
            throw new IllegalArgumentException("标题不能为空");
        }
        if (!StringUtils.hasText(request.getImageUrl())) {
            throw new IllegalArgumentException("请先上传图片或填写 imageUrl");
        }
        MemoryCategory cat = MemoryCategory.fromCode(request.getCategory());
        LocalDate eventDate = request.getEventDate() != null ? request.getEventDate() : LocalDate.now();
        MemoryLayout layout = MemoryLayout.fromCode(request.getLayout());

        MemoryRecord m = new MemoryRecord();
        m.setTitle(request.getTitle().trim());
        m.setDescription(request.getDescription() != null ? request.getDescription().trim() : null);
        m.setImageUrl(request.getImageUrl().trim());
        m.setCategory(cat);
        m.setEventDate(eventDate);
        m.setCompanions(normalizeCompanions(request.getCompanions()));
        m.setCreatedAt(Instant.now());
        m.setLayout(layout);
        memoryRepository.save(m);
        return toDto(m);
    }

    @Override
    public synchronized MemoryItemResponse createMemoryWithImage(
            MultipartFile image,
            String title,
            String description,
            String category,
            String eventDate,
            String companions,
            String layout
    ) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("请选择图片文件");
        }
        validateImage(image);
        String url = saveFile(image);

        MemoryCreateRequest req = new MemoryCreateRequest();
        req.setTitle(title);
        req.setDescription(description);
        req.setCategory(category);
        req.setLayout(layout);
        req.setImageUrl(url);
        if (StringUtils.hasText(eventDate)) {
            try {
                req.setEventDate(LocalDate.parse(eventDate.trim()));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("日期格式应为 yyyy-MM-dd");
            }
        } else {
            req.setEventDate(LocalDate.now());
        }
        req.setCompanions(parseCompanionsString(companions));
        return createMemory(req);
    }

    @Override
    public synchronized void deleteMemory(long id) {
        MemoryRecord record = memoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("回忆不存在或已删除"));
        deleteLocalUploadedImageIfNeeded(record.getImageUrl());
        memoryRepository.deleteById(id);
    }

    private void validateImage(MultipartFile file) {
        String ct = file.getContentType();
        if (ct == null || ALLOWED_IMAGE_TYPES.stream().noneMatch(ct::equalsIgnoreCase)) {
            throw new IllegalArgumentException("仅支持 JPEG、PNG、WebP、GIF 图片");
        }
    }

    private String saveFile(MultipartFile file) {
        String original = file.getOriginalFilename();
        String ext = ".jpg";
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf('.')).toLowerCase(Locale.ROOT);
            if (ext.length() > 8) {
                ext = ".jpg";
            }
        }
        String name = UUID.randomUUID() + ext;
        Path target = uploadRoot.resolve(name);
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("保存图片失败", e);
        }
        return "/uploads/starlight/" + name;
    }

    private void deleteLocalUploadedImageIfNeeded(String imageUrl) {
        if (!StringUtils.hasText(imageUrl)) {
            return;
        }
        String prefix = "/uploads/starlight/";
        if (!imageUrl.startsWith(prefix)) {
            return;
        }
        String filename = imageUrl.substring(prefix.length()).trim();
        if (!StringUtils.hasText(filename)) {
            return;
        }
        Path target = uploadRoot.resolve(filename).normalize();
        if (!target.startsWith(uploadRoot)) {
            return;
        }
        try {
            Files.deleteIfExists(target);
        } catch (IOException ignored) {
            // 删除文件失败不影响记录删除，避免把数据卡死。
        }
    }

    private List<String> normalizeCompanions(List<String> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        return list.stream()
                .filter(StringUtils::hasText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    private List<String> parseCompanionsString(String raw) {
        if (!StringUtils.hasText(raw)) {
            return new ArrayList<>();
        }
        return Arrays.stream(raw.split("[,，、]"))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
    }

    private MemoryItemResponse toDto(MemoryRecord m) {
        MemoryItemResponse dto = new MemoryItemResponse();
        dto.setId(m.getId());
        dto.setTitle(m.getTitle());
        dto.setDescription(m.getDescription());
        dto.setImageUrl(m.getImageUrl());
        if (m.getCategory() != null) {
            dto.setCategory(m.getCategory().getCode());
            dto.setCategoryLabel(m.getCategory().getLabel());
        }
        dto.setEventDate(m.getEventDate());
        dto.setCompanions(m.getCompanions() != null ? new ArrayList<>(m.getCompanions()) : new ArrayList<>());
        dto.setCreatedAt(m.getCreatedAt());
        dto.setLayout(m.getLayout() != null ? m.getLayout().name() : MemoryLayout.COMPACT.name());
        return dto;
    }

    private void seedDemoMemories() {
        Instant base = Instant.now();
        addSeed(
                "在大理洱海的那个午后",
                null,
                "/images/starlight-seed-1.jpg",
                MemoryCategory.TRAVEL,
                LocalDate.of(2023, 10, 15),
                List.of("小雨", "阿强"),
                MemoryLayout.HERO,
                base.minusSeconds(40)
        );
        addSeed(
                "重聚的拥抱",
                "两年没见，你还是那个爱大笑的女孩。我们在老操场坐了很久，聊着那些回不去的青葱岁月。",
                "/images/starlight-seed-2.jpg",
                MemoryCategory.DAILY,
                LocalDate.of(2024, 2, 12),
                List.of(),
                MemoryLayout.MEDIUM,
                base.minusSeconds(30)
        );
        addSeed(
                "周末的拿铁时光",
                null,
                "/images/starlight-seed-3.jpg",
                MemoryCategory.DAILY,
                LocalDate.of(2024, 3, 20),
                List.of(),
                MemoryLayout.COMPACT,
                base.minusSeconds(20)
        );
        addSeed(
                "草莓音乐节狂欢",
                null,
                "/images/starlight-seed-4.jpg",
                MemoryCategory.SPORT,
                LocalDate.of(2023, 5, 2),
                List.of(),
                MemoryLayout.COMPACT,
                base.minusSeconds(10)
        );
    }

    /**
     * 演示数据：createdAt 略错开，便于「最近添加」排序稳定可预期。
     */
    private void addSeed(
            String title,
            String description,
            String imageUrl,
            MemoryCategory category,
            LocalDate eventDate,
            List<String> companions,
            MemoryLayout layout,
            Instant createdAt
    ) {
        MemoryRecord m = new MemoryRecord();
        m.setTitle(title);
        m.setDescription(description);
        m.setImageUrl(imageUrl);
        m.setCategory(category);
        m.setEventDate(eventDate);
        m.setCompanions(new ArrayList<>(companions));
        m.setCreatedAt(createdAt);
        m.setLayout(layout);
        memoryRepository.save(m);
    }
}
