package com.friend.hollow.service.impl;

import com.friend.hollow.dto.MeditationNoiseTrackDto;
import com.friend.hollow.service.MeditationNoiseService;
import com.friend.hollow.util.MeditationNoiseLabelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MeditationNoiseServiceImpl implements MeditationNoiseService {

    private final Path noiseRoot;

    public MeditationNoiseServiceImpl(@Value("${app.meditation-noise-dir:mp3}") String noiseDir) {
        this.noiseRoot = Path.of(noiseDir).toAbsolutePath().normalize();
    }

    @Override
    public List<MeditationNoiseTrackDto> listTracks() {
        if (!Files.isDirectory(noiseRoot)) {
            return List.of();
        }
        Properties overrides = loadNoiseLabels();
        List<Path> mp3Files;
        try (Stream<Path> stream = Files.list(noiseRoot)) {
            mp3Files = stream
                    .filter(p -> Files.isRegularFile(p))
                    .filter(p -> p.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".mp3"))
                    .sorted(Comparator.comparing(p -> p.getFileName().toString(), String.CASE_INSENSITIVE_ORDER))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return List.of();
        }
        List<MeditationNoiseTrackDto> out = new ArrayList<>();
        int idx = 1;
        for (Path path : mp3Files) {
            String fileName = path.getFileName().toString();
            String label = overrides.getProperty(fileName);
            if (!StringUtils.hasText(label)) {
                label = MeditationNoiseLabelMapper.map(fileName, idx);
            } else {
                label = label.trim();
            }
            String id = Base64.getUrlEncoder().withoutPadding().encodeToString(fileName.getBytes(StandardCharsets.UTF_8));
            String encoded = UriUtils.encodePathSegment(fileName, StandardCharsets.UTF_8);
            String url = "/meditation-mp3/" + encoded;

            MeditationNoiseTrackDto dto = new MeditationNoiseTrackDto();
            dto.setId(id);
            dto.setFileName(fileName);
            dto.setUrl(url);
            dto.setLabel(label);
            out.add(dto);
            idx++;
        }
        return out;
    }

    private Properties loadNoiseLabels() {
        Properties p = new Properties();
        Path f = noiseRoot.resolve("noise-labels.properties");
        if (!Files.isRegularFile(f)) {
            return p;
        }
        try (Reader r = Files.newBufferedReader(f, StandardCharsets.UTF_8)) {
            p.load(r);
        } catch (IOException ignored) {
            // 忽略：仅用自动映射
        }
        return p;
    }
}
