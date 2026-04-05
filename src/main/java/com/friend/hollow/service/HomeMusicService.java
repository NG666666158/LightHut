package com.friend.hollow.service;

import com.friend.hollow.dto.MeditationNoiseTrackDto;

import java.util.List;

/**
 * 侧栏「小屋轻音乐」：扫描项目根目录 {@code musics} 下 .mp3。
 */
public interface HomeMusicService {

    List<MeditationNoiseTrackDto> listTracks();
}
