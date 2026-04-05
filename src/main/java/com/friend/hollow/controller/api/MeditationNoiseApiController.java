package com.friend.hollow.controller.api;

import com.friend.hollow.dto.MeditationNoiseTrackDto;
import com.friend.hollow.service.MeditationNoiseService;
import com.friend.hollow.util.PagePathUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MeditationNoiseApiController {

    private final MeditationNoiseService meditationNoiseService;

    public MeditationNoiseApiController(MeditationNoiseService meditationNoiseService) {
        this.meditationNoiseService = meditationNoiseService;
    }

    /**
     * 扫描 {@code app.meditation-noise-dir}（默认项目根目录 {@code mp3}）下全部 .mp3，返回标题与播放 URL。
     */
    @GetMapping(PagePathUtil.MEDITATION_NOISE_TRACKS_API)
    public List<MeditationNoiseTrackDto> noiseTracks() {
        return meditationNoiseService.listTracks();
    }
}
