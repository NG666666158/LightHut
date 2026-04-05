package com.friend.hollow.service.impl;

import com.friend.hollow.repository.impl.InMemoryMemoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Path;

class StarlightServiceImplTest {

    @TempDir
    Path tempDir;

    @Test
    void uploadImageRejectsNonImageMimeType() throws IOException {
        StarlightServiceImpl service = new StarlightServiceImpl(tempDir.toString(), new InMemoryMemoryRepository());
        service.init();
        MockMultipartFile file = new MockMultipartFile("file", "a.txt", "text/plain", "hello".getBytes());

        Assertions.assertThrows(IllegalArgumentException.class, () -> service.uploadImage(file));
    }
}
