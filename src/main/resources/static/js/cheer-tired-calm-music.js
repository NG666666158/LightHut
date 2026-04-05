/**
 * 「我累了」静心音乐：播放侧栏同源 #homeMusicAudio、月光色波浪频谱、点击音符爆发、播放文案轮换。
 */
(function () {
    var PLAYING_LINES = [
        '♪ 小屋轻音乐正在流淌，把自己轻轻放进这段旋律里吧。',
        '♫ 听见了吗？节拍很轻，像月光落在水面上。',
        '♬ 此刻的音符，都是为你留的一点点甜。',
        '🎵 正在播放：愿你愁眉舒展，心事也跟着变轻。',
        '🎶 音乐在场，疲惫就可以先下班一会儿。',
        '♪ 旋律慢慢铺展，像有人轻轻拍你的肩。',
        '♫ 试着把呼吸对齐鼓点——一呼一吸，都是疗愈。',
        '♬ 正在播放：你值得拥有这片安静的底色。',
        '🎵 声浪很轻，却把你裹得很暖。',
        '🎶 就让这一段，陪你把今天轻轻合上。'
    ];

    var NOTE_CHARS = ['♪', '♫', '♬', '🎵', '🎶', '♩', '𝄞', '♭', '♯'];
    var NOTE_PALETTE = ['#c7d9fb', '#fde68a', '#fbcfe8', '#a5d8ff', '#e9d5ff', '#fef08a', '#bae6fd'];

    var audio = document.getElementById('homeMusicAudio');
    var btn = document.getElementById('tiredCalmMusicBtn');
    var wrap = document.getElementById('tiredMusicSpectrumWrap');
    var canvas = document.getElementById('tiredMusicSpectrum');
    var statusEl = document.getElementById('tiredMusicPlayingStatus');

    if (!btn || !audio || !wrap || !canvas || !statusEl) {
        return;
    }

    var reduceMotion = window.matchMedia && window.matchMedia('(prefers-reduced-motion: reduce)').matches;

    var graph = null;
    var rafId = null;
    var phase = 0;
    var linePick = 0;
    var dataArray = null;
    var flatPhase = 0;
    function tiredPanelActive() {
        var p = document.querySelector('[data-mood-panel="tired"]');
        return !!(p && !p.hidden);
    }

    function showStatusLine() {
        statusEl.classList.remove('hidden');
        statusEl.textContent = PLAYING_LINES[linePick % PLAYING_LINES.length];
        linePick += 1;
    }

    function hideStatus() {
        statusEl.classList.add('hidden');
    }

    function injectRippleStyle() {
        if (document.getElementById('tiredMusicRippleStyle')) {
            return;
        }
        var st = document.createElement('style');
        st.id = 'tiredMusicRippleStyle';
        st.textContent =
            '@keyframes tiredMusicRipple{0%{box-shadow:0 0 0 0 rgba(199,210,255,0.8);opacity:1}100%{box-shadow:0 0 0 48px rgba(199,210,255,0);opacity:0}}';
        document.head.appendChild(st);
    }

    function burstNotes(clientX, clientY) {
        injectRippleStyle();
        var n = 17 + Math.floor(Math.random() * 8);
        var i;
        for (i = 0; i < n; i++) {
            (function (idx) {
                window.setTimeout(function () {
                    var el = document.createElement('span');
                    el.className = 'tired-music-burst-note';
                    el.setAttribute('aria-hidden', 'true');
                    el.textContent = NOTE_CHARS[Math.floor(Math.random() * NOTE_CHARS.length)];
                    el.style.left = clientX + 'px';
                    el.style.top = clientY + 'px';
                    el.style.setProperty('--rot', (Math.random() * 160 - 80).toFixed(1) + 'deg');
                    el.style.setProperty('--dx', (Math.random() * 240 - 120).toFixed(1) + 'px');
                    el.style.setProperty('--dy', (-70 - Math.random() * 170).toFixed(1) + 'px');
                    var c = NOTE_PALETTE[Math.floor(Math.random() * NOTE_PALETTE.length)];
                    el.style.color = c;
                    el.style.textShadow =
                        '0 0 14px rgba(255,255,255,0.95), 0 0 28px rgba(200,220,255,0.75), 0 2px 8px rgba(147,82,82,0.15)';
                    document.body.appendChild(el);
                    window.setTimeout(function () {
                        if (el.parentNode) {
                            el.parentNode.removeChild(el);
                        }
                    }, 1150);
                }, idx * 32);
            })(i);
        }
        var ring = document.createElement('div');
        ring.setAttribute('aria-hidden', 'true');
        ring.style.cssText =
            'position:fixed;left:' +
            clientX +
            'px;top:' +
            clientY +
            'px;width:10px;height:10px;margin:-5px 0 0 -5px;border-radius:50%;pointer-events:none;z-index:12035;animation:tiredMusicRipple .7s ease-out forwards;background:rgba(230,240,255,0.35)';
        document.body.appendChild(ring);
        window.setTimeout(function () {
            if (ring.parentNode) {
                ring.parentNode.removeChild(ring);
            }
        }, 750);
    }

    function ensureGraph() {
        if (graph) {
            return graph;
        }
        if (audio.__hollowTiredMusicGraph) {
            graph = audio.__hollowTiredMusicGraph;
            dataArray = new Uint8Array(graph.analyser.frequencyBinCount);
            return graph;
        }
        var AC = window.AudioContext || window.webkitAudioContext;
        if (!AC) {
            return null;
        }
        try {
            var actx = new AC();
            var src = actx.createMediaElementSource(audio);
            var analyser = actx.createAnalyser();
            analyser.fftSize = 512;
            analyser.smoothingTimeConstant = 0.82;
            src.connect(analyser);
            analyser.connect(actx.destination);
            graph = { actx: actx, analyser: analyser };
            audio.__hollowTiredMusicGraph = graph;
            dataArray = new Uint8Array(analyser.frequencyBinCount);
            return graph;
        } catch (e) {
            return null;
        }
    }

    function resizeCanvas() {
        var dpr = Math.min(window.devicePixelRatio || 1, 2);
        var rect = wrap.getBoundingClientRect();
        var w = Math.max(100, Math.floor(rect.width));
        var rectC = canvas.getBoundingClientRect();
        var h = Math.max(48, Math.floor(rectC.height) || 56);
        canvas.width = Math.floor(w * dpr);
        canvas.height = Math.floor(h * dpr);
        canvas.style.width = '100%';
        var ctx = canvas.getContext('2d');
        if (ctx) {
            ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
        }
    }

    function drawStillLine() {
        var ctx = canvas.getContext('2d');
        if (!ctx) {
            return;
        }
        var dpr = Math.min(window.devicePixelRatio || 1, 2);
        var w = canvas.width / dpr;
        var h = canvas.height / dpr;
        ctx.clearRect(0, 0, w, h);
        ctx.strokeStyle = 'rgba(210, 226, 252, 0.5)';
        ctx.lineWidth = 1.2;
        ctx.beginPath();
        var mid = h * 0.55;
        ctx.moveTo(0, mid);
        ctx.lineTo(w, mid);
        ctx.stroke();
    }

    function tickWave() {
        if (!tiredPanelActive() || audio.paused) {
            stopLoop();
            return;
        }
        rafId = requestAnimationFrame(tickWave);
        var ctx = canvas.getContext('2d');
        if (!ctx || !graph) {
            return;
        }
        var dpr = Math.min(window.devicePixelRatio || 1, 2);
        var w = canvas.width / dpr;
        var h = canvas.height / dpr;
        graph.analyser.getByteFrequencyData(dataArray);
        var bass = 0;
        var j;
        for (j = 2; j < 22; j++) {
            bass += dataArray[j];
        }
        bass = bass / 20 / 255;
        phase += 0.055 + bass * 0.13;
        ctx.clearRect(0, 0, w, h);
        ctx.lineWidth = 1.35;
        ctx.lineCap = 'round';
        ctx.lineJoin = 'round';
        var lg = ctx.createLinearGradient(0, 0, w, 0);
        lg.addColorStop(0, 'rgba(248, 250, 255, 0.22)');
        lg.addColorStop(0.35, 'rgba(200, 220, 248, 0.9)');
        lg.addColorStop(0.62, 'rgba(184, 208, 245, 0.85)');
        lg.addColorStop(1, 'rgba(236, 242, 255, 0.28)');
        ctx.strokeStyle = lg;
        ctx.beginPath();
        var mid = h * 0.52;
        var x;
        for (x = 0; x <= w; x += 3) {
            var bin = Math.min(dataArray.length - 1, Math.floor((x / w) * 96));
            var v = dataArray[bin] / 255;
            var y = mid + Math.sin(x * 0.024 + phase) * (5 + v * 28) + Math.sin(x * 0.07 + phase * 1.35) * (v * 10);
            if (x === 0) {
                ctx.moveTo(x, y);
            } else {
                ctx.lineTo(x, y);
            }
        }
        ctx.stroke();
        ctx.strokeStyle = 'rgba(255, 255, 255, 0.38)';
        ctx.lineWidth = 0.9;
        ctx.beginPath();
        for (x = 0; x <= w; x += 4) {
            var bin2 = Math.min(dataArray.length - 1, Math.floor((x / w) * 64) + 4);
            var v2 = dataArray[bin2] / 255;
            var y2 = mid + Math.sin(x * 0.03 + phase * 0.88) * (3 + v2 * 15);
            if (x === 0) {
                ctx.moveTo(x, y2);
            } else {
                ctx.lineTo(x, y2);
            }
        }
        ctx.stroke();
    }

    function tickFlat() {
        if (!tiredPanelActive() || audio.paused) {
            stopLoop();
            return;
        }
        rafId = requestAnimationFrame(tickFlat);
        var ctx = canvas.getContext('2d');
        if (!ctx) {
            return;
        }
        var dpr = Math.min(window.devicePixelRatio || 1, 2);
        var w = canvas.width / dpr;
        var h = canvas.height / dpr;
        flatPhase += 0.065;
        ctx.clearRect(0, 0, w, h);
        ctx.strokeStyle = 'rgba(186, 210, 245, 0.58)';
        ctx.lineWidth = 1.15;
        ctx.beginPath();
        var mid = h * 0.52;
        var x;
        for (x = 0; x <= w; x += 3) {
            var y = mid + Math.sin(x * 0.027 + flatPhase) * 11;
            if (x === 0) {
                ctx.moveTo(x, y);
            } else {
                ctx.lineTo(x, y);
            }
        }
        ctx.stroke();
    }

    function stopLoop() {
        if (rafId) {
            cancelAnimationFrame(rafId);
            rafId = null;
        }
    }

    function startLoop() {
        stopLoop();
        if (!tiredPanelActive()) {
            return;
        }
        resizeCanvas();
        if (reduceMotion) {
            drawStillLine();
            return;
        }
        if (!graph) {
            rafId = requestAnimationFrame(tickFlat);
            return;
        }
        if (graph.actx.state === 'suspended') {
            graph.actx.resume().catch(function () {
                /* ignore */
            });
        }
        if (!dataArray) {
            dataArray = new Uint8Array(graph.analyser.frequencyBinCount);
        }
        rafId = requestAnimationFrame(tickWave);
    }

    function showSpectrum(want) {
        if (want && !tiredPanelActive()) {
            return;
        }
        if (want) {
            wrap.classList.remove('hidden');
            wrap.setAttribute('aria-hidden', 'false');
            window.requestAnimationFrame(function () {
                resizeCanvas();
                startLoop();
            });
        } else {
            wrap.classList.add('hidden');
            wrap.setAttribute('aria-hidden', 'true');
            stopLoop();
        }
    }

    btn.addEventListener(
        'click',
        function (e) {
            var cx = e.clientX;
            var cy = e.clientY;
            burstNotes(cx, cy);
            try {
                ensureGraph();
                if (graph && graph.actx && graph.actx.state === 'suspended') {
                    graph.actx.resume().catch(function () {
                        /* ignore */
                    });
                }
            } catch (e0) {
                /* ignore */
            }
            var wasPlaying = !audio.paused;
            if (!wasPlaying) {
                var p = audio.play();
                if (p && typeof p.then === 'function') {
                    p.then(function () {
                        showStatusLine();
                        showSpectrum(true);
                    }).catch(function () {
                        showStatusLine();
                        showSpectrum(true);
                    });
                } else {
                    showStatusLine();
                    showSpectrum(true);
                }
            } else {
                showStatusLine();
                if (tiredPanelActive() && !audio.paused) {
                    showSpectrum(true);
                }
            }
        },
        { passive: true }
    );

    audio.addEventListener('play', function () {
        try {
            if (!graph) {
                ensureGraph();
            }
            if (graph && graph.actx && graph.actx.state === 'suspended') {
                graph.actx.resume().catch(function () {
                    /* ignore */
                });
            }
        } catch (e1) {
            /* ignore */
        }
        if (tiredPanelActive()) {
            showSpectrum(true);
        }
    });

    audio.addEventListener('pause', function () {
        if (!audio.ended) {
            showSpectrum(false);
            hideStatus();
        }
    });

    audio.addEventListener('ended', function () {
        showSpectrum(false);
    });

    window.addEventListener('resize', function () {
        if (!wrap.classList.contains('hidden')) {
            resizeCanvas();
            if (reduceMotion) {
                drawStillLine();
            }
        }
    });

    var moodWrap = document.getElementById('moodButtons');
    if (moodWrap) {
        moodWrap.addEventListener('click', function () {
            window.setTimeout(function () {
                if (tiredPanelActive() && !audio.paused) {
                    try {
                        ensureGraph();
                        if (graph && graph.actx && graph.actx.state === 'suspended') {
                            graph.actx.resume().catch(function () {
                                /* ignore */
                            });
                        }
                    } catch (e2) {
                        /* ignore */
                    }
                    showSpectrum(true);
                } else if (!tiredPanelActive()) {
                    showSpectrum(false);
                }
            }, 30);
        });
    }

    if (!audio.paused && tiredPanelActive()) {
        try {
            ensureGraph();
        } catch (e3) {
            /* ignore */
        }
        showSpectrum(true);
    }
})();
