/**
 * 侧栏轻音乐：musics 曲目、随机起播、sessionStorage 跨页续播；
 * 曲目结束后随机下一首；换页时用 canplay/缓存尽量无缝续播。
 */
(function () {
    var STORAGE_KEY = 'hollowHomeMusicV1';
    var PLAYLIST_CACHE_KEY = 'hollowHomeMusicPlaylistJson';

    var root = document.getElementById('homeSidebarMusicRoot');
    var audio = document.getElementById('homeMusicAudio');
    var disc = document.getElementById('homeMusicDisc');
    var discWrap = document.getElementById('homeMusicDiscWrap');
    var hintEl = document.getElementById('homeMusicHint');
    var btnPlay = document.getElementById('homeMusicPlay');
    var btnPrev = document.getElementById('homeMusicPrev');
    var btnNext = document.getElementById('homeMusicNext');
    var volEl = document.getElementById('homeMusicVolume');
    if (!root || !audio || !disc || !btnPlay || !btnPrev || !btnNext || !volEl) {
        return;
    }

    var playlist = [];
    var playlistSig = '';
    var idx = 0;
    var saveTimer = null;
    var bootstrapped = false;
    var preloadBoosted = false;
    var warmOtherTracksScheduled = false;

    function setPlayIcon(playing) {
        var icon = btnPlay.querySelector('.material-symbols-outlined');
        if (icon) {
            icon.textContent = playing ? 'pause' : 'play_arrow';
        }
        btnPlay.setAttribute('aria-pressed', playing ? 'true' : 'false');
    }

    function boostPreload() {
        if (preloadBoosted) {
            return;
        }
        preloadBoosted = true;
        try {
            audio.preload = 'auto';
        } catch (e) {
            /* ignore */
        }
    }

    function persistSoon() {
        if (saveTimer) {
            clearTimeout(saveTimer);
        }
        saveTimer = setTimeout(persist, 200);
    }

    function persist() {
        saveTimer = null;
        if (!playlist.length) {
            return;
        }
        try {
            sessionStorage.setItem(
                STORAGE_KEY,
                JSON.stringify({
                    sig: playlistSig,
                    idx: idx,
                    t: audio.currentTime,
                    paused: audio.paused,
                    vol: audio.volume
                })
            );
        } catch (e) {
            /* ignore */
        }
    }

    function loadIndex(i) {
        idx = (i + playlist.length * 100) % playlist.length;
        var item = playlist[idx];
        audio.src = item.src;
    }

    function tryAutoplay() {
        boostPreload();
        var p = audio.play();
        if (p && typeof p.then === 'function') {
            p.then(function () {
                if (hintEl) {
                    hintEl.classList.add('hidden');
                }
                setPlayIcon(true);
            }).catch(function () {
                if (hintEl) {
                    hintEl.classList.remove('hidden');
                }
                setPlayIcon(false);
            });
        }
    }

    /**
     * 在已有足够缓冲后再 play，减少换页/切歌时的卡顿与中断感。
     */
    function runWhenPlayable(callback, fallbackMs) {
        var done = false;
        var timer = null;
        function finish() {
            if (done) {
                return;
            }
            done = true;
            audio.removeEventListener('canplay', onEv);
            audio.removeEventListener('loadeddata', onEv);
            if (timer != null) {
                clearTimeout(timer);
            }
            callback();
        }
        function onEv() {
            finish();
        }
        if (audio.readyState >= 3) {
            window.requestAnimationFrame(function () {
                callback();
            });
            return;
        }
        timer = setTimeout(finish, fallbackMs != null ? fallbackMs : 720);
        audio.addEventListener('canplay', onEv, { once: true });
        audio.addEventListener('loadeddata', onEv, { once: true });
    }

    function playAfterTrackChange() {
        runWhenPlayable(function () {
            tryAutoplay();
        }, 650);
    }

    function togglePlay() {
        if (audio.paused) {
            tryAutoplay();
        } else {
            audio.pause();
            setPlayIcon(false);
            persist();
        }
    }

    function go(delta) {
        var wasPlaying = !audio.paused;
        loadIndex(idx + delta);
        if (wasPlaying) {
            playAfterTrackChange();
        } else {
            setPlayIcon(false);
        }
        persistSoon();
    }

    function pickRandomNextIndex() {
        if (playlist.length <= 1) {
            return idx;
        }
        var next = idx;
        var guard = 0;
        while (next === idx && guard++ < 64) {
            next = Math.floor(Math.random() * playlist.length);
        }
        return next;
    }

    function onTrackEndedPlayRandomNext() {
        if (!playlist.length) {
            return;
        }
        if (playlist.length === 1) {
            try {
                audio.currentTime = 0;
            } catch (e) {
                /* ignore */
            }
            runWhenPlayable(function () {
                tryAutoplay();
            }, 500);
            persistSoon();
            return;
        }
        loadIndex(pickRandomNextIndex());
        playAfterTrackChange();
        persistSoon();
    }

    function scheduleIdleWarmOtherTracks() {
        if (warmOtherTracksScheduled || playlist.length < 2) {
            return;
        }
        warmOtherTracksScheduled = true;
        var urls = [];
        var k;
        for (k = 0; k < playlist.length && urls.length < 3; k++) {
            var j = (idx + 1 + k) % playlist.length;
            if (playlist[j] && playlist[j].src) {
                urls.push(playlist[j].src);
            }
        }
        function run() {
            urls.forEach(function (url, i) {
                window.setTimeout(function () {
                    try {
                        var w = new Audio();
                        w.preload = 'auto';
                        w.src = url;
                    } catch (e2) {
                        /* ignore */
                    }
                }, i * 280);
            });
        }
        if (window.requestIdleCallback) {
            window.requestIdleCallback(run, { timeout: 3200 });
        } else {
            window.setTimeout(run, 1800);
        }
    }

    audio.addEventListener('play', function () {
        boostPreload();
        disc.classList.add('home-music-disc--spinning');
        if (discWrap) {
            discWrap.classList.add('home-music-disc-wrap--playing');
        }
        setPlayIcon(true);
        scheduleIdleWarmOtherTracks();
    });
    audio.addEventListener('pause', function () {
        disc.classList.remove('home-music-disc--spinning');
        if (discWrap) {
            discWrap.classList.remove('home-music-disc-wrap--playing');
        }
        setPlayIcon(false);
    });
    audio.addEventListener('ended', onTrackEndedPlayRandomNext);

    btnPlay.addEventListener('click', function () {
        togglePlay();
    });
    btnPrev.addEventListener('click', function () {
        go(-1);
    });
    btnNext.addEventListener('click', function () {
        go(1);
    });

    volEl.addEventListener(
        'input',
        function () {
            var v = parseInt(volEl.value, 10);
            if (isNaN(v)) {
                v = 70;
            }
            audio.volume = Math.min(1, Math.max(0, v / 100));
            persistSoon();
        },
        { passive: true }
    );

    window.addEventListener('pagehide', persist, { capture: true });
    window.addEventListener('pageshow', function (ev) {
        if (ev.persisted && !audio.paused) {
            audio.play().catch(function () {
                /* ignore */
            });
        }
    });
    document.addEventListener('visibilitychange', function () {
        if (document.visibilityState === 'hidden') {
            persist();
        }
    });

    function buildSig(apiTracks) {
        return apiTracks
            .map(function (t) {
                return t.fileName;
            })
            .join('|');
    }

    function bootstrapPlaylist(apiTracks) {
        if (!apiTracks || !apiTracks.length) {
            root.classList.add('hidden');
            return;
        }
        root.classList.remove('hidden');
        playlist = apiTracks.map(function (t) {
            return { title: t.label, src: t.url, fileName: t.fileName };
        });
        playlistSig = playlist
            .map(function (p) {
                return p.fileName;
            })
            .join('|');

        var saved = null;
        try {
            saved = JSON.parse(sessionStorage.getItem(STORAGE_KEY) || 'null');
        } catch (e) {
            saved = null;
        }

        var restoring = !!(
            saved &&
            saved.sig === playlistSig &&
            typeof saved.idx === 'number' &&
            saved.idx >= 0 &&
            saved.idx < playlist.length
        );

        if (restoring) {
            idx = saved.idx;
            if (typeof saved.vol === 'number' && !isNaN(saved.vol)) {
                audio.volume = Math.min(1, Math.max(0, saved.vol));
                volEl.value = String(Math.round(audio.volume * 100));
            } else {
                audio.volume = 0.7;
                volEl.value = '70';
            }
        } else {
            idx = Math.floor(Math.random() * playlist.length);
            audio.volume = 0.7;
            volEl.value = '70';
        }

        loadIndex(idx);
        setPlayIcon(false);

        var savedRef = saved;

        function afterMeta() {
            if (restoring && savedRef && typeof savedRef.t === 'number' && savedRef.t > 0) {
                try {
                    var d = audio.duration;
                    audio.currentTime = !isNaN(d) && d > 0 ? Math.min(savedRef.t, d - 0.25) : savedRef.t;
                } catch (e) {
                    /* ignore */
                }
                if (!savedRef.paused) {
                    boostPreload();
                    runWhenPlayable(function () {
                        tryAutoplay();
                    }, 900);
                } else {
                    setPlayIcon(false);
                }
            } else if (!restoring) {
                runWhenPlayable(function () {
                    tryAutoplay();
                }, 520);
            } else {
                setPlayIcon(false);
            }
        }

        if (audio.readyState >= 1) {
            afterMeta();
        } else {
            audio.addEventListener('loadedmetadata', afterMeta, { once: true });
        }

        bootstrapped = true;
    }

    try {
        var cachedRaw = sessionStorage.getItem(PLAYLIST_CACHE_KEY);
        if (cachedRaw) {
            var cached = JSON.parse(cachedRaw);
            if (Array.isArray(cached) && cached.length) {
                bootstrapPlaylist(cached);
            }
        }
    } catch (e) {
        /* ignore */
    }

    fetch('/api/home/music-tracks')
        .then(function (res) {
            if (!res.ok) {
                throw new Error('load');
            }
            return res.json();
        })
        .then(function (tracks) {
            try {
                sessionStorage.setItem(PLAYLIST_CACHE_KEY, JSON.stringify(tracks || []));
            } catch (e) {
                /* ignore */
            }
            if (!tracks || !tracks.length) {
                root.classList.add('hidden');
                try {
                    sessionStorage.removeItem(PLAYLIST_CACHE_KEY);
                } catch (e2) {
                    /* ignore */
                }
                if (bootstrapped) {
                    try {
                        audio.pause();
                    } catch (e3) {
                        /* ignore */
                    }
                }
                return;
            }
            var newSig = buildSig(tracks);
            if (bootstrapped && newSig === playlistSig) {
                return;
            }
            bootstrapPlaylist(tracks);
        })
        .catch(function () {
            if (!bootstrapped) {
                root.classList.add('hidden');
            }
        });
})();
