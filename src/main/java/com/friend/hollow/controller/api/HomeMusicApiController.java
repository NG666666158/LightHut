package com.friend.hollow.controller.api;

import com.friend.hollow.dto.MeditationNoiseTrackDto;
import com.friend.hollow.service.HomeMusicService;
import com.friend.hollow.util.PagePathUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeMusicApiController {

    private final HomeMusicService homeMusicService;

    public HomeMusicApiController(HomeMusicService homeMusicService) {
        this.homeMusicService = homeMusicService;
    }

    /**
     * 扫描 {@code app.home-music-dir}（默认项目根目录 {@code musics}）下全部 .mp3。
     */
    @GetMapping(PagePathUtil.HOME_MUSIC_TRACKS_API)
    public List<MeditationNoiseTrackDto> musicTracks() {
        return homeMusicService.listTracks();
    }
}
