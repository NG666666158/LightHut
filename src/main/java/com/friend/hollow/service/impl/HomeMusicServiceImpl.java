package com.friend.hollow.service.impl;

import com.friend.hollow.dto.MeditationNoiseTrackDto;
import com.friend.hollow.service.HomeMusicService;
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
public class HomeMusicServiceImpl implements HomeMusicService {

    private final Path musicRoot;

    public HomeMusicServiceImpl(@Value("${app.home-music-dir:musics}") String musicDir) {
        this.musicRoot = Path.of(musicDir).toAbsolutePath().normalize();
    }

    @Override
    public List<MeditationNoiseTrackDto> listTracks() {
        if (!Files.isDirectory(musicRoot)) {
            return List.of();
        }
        Properties overrides = loadMusicLabels();
        List<Path> mp3Files;
        try (Stream<Path> stream = Files.list(musicRoot)) {
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
            String url = "/home-musics/" + encoded;

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

    private Properties loadMusicLabels() {
        Properties p = new Properties();
        Path f = musicRoot.resolve("music-labels.properties");
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
