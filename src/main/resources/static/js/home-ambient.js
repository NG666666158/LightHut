/**
 * 首页背景粒子 + 跨页飘入（仅 pathname === /home）。
 * 依赖：#homeAmbientParticles、window.__hollowPrevPath（hollow-nav-trail.js）
 */
(function () {
    if (location.pathname !== '/home') {
        return;
    }

    var layer = document.getElementById('homeAmbientParticles');
    if (!layer) {
        return;
    }

    var MAX_PARTICLES = 45;
    var spawnTimer = null;
    var paused = false;
    var hadMeditateGlow = false;
    var burstTimer = null;
    var burstEnd = 0;

    function treeTarget() {
        var el = document.getElementById('homeTreeStageWrap');
        if (!el) {
            return { x: window.innerWidth * 0.82, y: window.innerHeight * 0.42 };
        }
        var r = el.getBoundingClientRect();
        return { x: r.left + r.width / 2, y: r.top + r.height / 2 };
    }

    function resolveAmbientTheme() {
        try {
            if (sessionStorage.getItem('hollowMeditateJustDone') === '1') {
                hadMeditateGlow = true;
                return 'meditate';
            }
        } catch (e) { /* ignore */ }
        try {
            var m = localStorage.getItem('hollowCheerLastMood');
            if (m === 'happy') {
                return 'happy';
            }
            if (m === 'tired') {
                return 'tired';
            }
        } catch (e2) { /* ignore */ }
        return 'daily';
    }

    function consumeMeditateFlag() {
        if (!hadMeditateGlow) {
            return;
        }
        try {
            sessionStorage.removeItem('hollowMeditateJustDone');
        } catch (e) { /* ignore */ }
    }

    function trimLayer() {
        while (layer.children.length > MAX_PARTICLES) {
            layer.removeChild(layer.firstChild);
        }
    }

    function spawnDaily() {
        var el = document.createElement('span');
        el.className = 'home-amb home-amb--daily';
        el.setAttribute('aria-hidden', 'true');
        var w = 2 + Math.random() * 3;
        el.style.cssText =
            'position:fixed;left:' +
            Math.random() * 100 +
            'vw;top:-8px;width:' +
            w +
            'px;height:' +
            w +
            'px;border-radius:50%;pointer-events:none;opacity:0.55;background:radial-gradient(circle,#fff7d6 0%,#fbbf24 70%,transparent 100%);box-shadow:0 0 6px rgba(251,191,36,0.35);z-index:0;animation:homeAmbFall ' +
            (14 + Math.random() * 10) +
            's linear forwards;';
        layer.appendChild(el);
        trimLayer();
        window.setTimeout(function () {
            if (el.parentNode) {
                el.parentNode.removeChild(el);
            }
        }, 24000);
    }

    function spawnHappy() {
        var el = document.createElement('span');
        el.className = 'home-amb home-amb--happy';
        el.setAttribute('aria-hidden', 'true');
        var isHeart = Math.random() < 0.55;
        if (isHeart) {
            el.textContent = '\u2665';
            el.style.cssText =
                'position:fixed;left:' +
                Math.random() * 100 +
                'vw;top:-12px;font-size:' +
                (10 + Math.random() * 8) +
                'px;color:rgba(244,114,182,0.55);pointer-events:none;z-index:0;text-shadow:0 0 8px rgba(253,186,200,0.6);animation:homeAmbFall ' +
                (12 + Math.random() * 8) +
                's linear forwards;';
        } else {
            var sz = 6 + Math.random() * 8;
            el.style.cssText =
                'position:fixed;left:' +
                Math.random() * 100 +
                'vw;top:-8px;width:' +
                sz +
                'px;height:' +
                (sz * 0.65) +
                'px;border-radius:50% 50% 50% 0;transform:rotate(-35deg);background:linear-gradient(135deg,#fce7f3,#f9a8d4);opacity:0.5;pointer-events:none;z-index:0;animation:homeAmbDrift ' +
                (16 + Math.random() * 10) +
                's linear forwards;';
        }
        layer.appendChild(el);
        trimLayer();
        window.setTimeout(function () {
            if (el.parentNode) {
                el.parentNode.removeChild(el);
            }
        }, 26000);
    }

    function spawnTired() {
        var el = document.createElement('span');
        el.setAttribute('aria-hidden', 'true');
        if (Math.random() < 0.45) {
            var sz = 2 + Math.random() * 3.5;
            el.style.cssText =
                'position:fixed;left:' +
                Math.random() * 100 +
                'vw;top:-6px;width:' +
                sz +
                'px;height:' +
                sz +
                'px;border-radius:50%;background:radial-gradient(circle,#fff,#dbeafe);opacity:0.65;box-shadow:0 0 4px rgba(255,255,255,0.8);pointer-events:none;z-index:0;animation:homeAmbSnow ' +
                (11 + Math.random() * 9) +
                's linear forwards;';
        } else {
            el.style.cssText =
                'position:fixed;left:' +
                Math.random() * 100 +
                'vw;top:-10px;width:12px;height:14px;pointer-events:none;z-index:0;opacity:0.55;animation:homeAmbMaple ' +
                (13 + Math.random() * 8) +
                's linear forwards;background:linear-gradient(145deg,#c2410c,#ea580c);border-radius:40% 60% 45% 55%;transform:rotate(12deg);';
        }
        layer.appendChild(el);
        trimLayer();
        window.setTimeout(function () {
            if (el.parentNode) {
                el.parentNode.removeChild(el);
            }
        }, 22000);
    }

    function spawnMeditate() {
        var el = document.createElement('span');
        el.className = 'home-amb home-amb--star';
        el.setAttribute('aria-hidden', 'true');
        var w = 2 + Math.random() * 2.5;
        el.style.cssText =
            'position:fixed;left:' +
            Math.random() * 100 +
            'vw;top:' +
            (Math.random() * 40 - 10) +
            '%;width:' +
            w +
            'px;height:' +
            w +
            'px;border-radius:50%;pointer-events:none;opacity:0.75;background:radial-gradient(circle,#ffffff 0%,#e2e8f0 55%,transparent 100%);box-shadow:0 0 10px rgba(255,255,255,0.9),0 0 4px rgba(199,210,254,0.8);z-index:0;animation:homeAmbTwinkle ' +
            (4 + Math.random() * 5) +
            's ease-in-out infinite, homeAmbFallSlow ' +
            (22 + Math.random() * 16) +
            's linear forwards;';
        layer.appendChild(el);
        trimLayer();
        window.setTimeout(function () {
            if (el.parentNode) {
                el.parentNode.removeChild(el);
            }
        }, 40000);
    }

    function spawnForTheme(theme) {
        if (paused) {
            return;
        }
        switch (theme) {
            case 'happy':
                spawnHappy();
                break;
            case 'tired':
                spawnTired();
                break;
            case 'meditate':
                spawnMeditate();
                break;
            default:
                spawnDaily();
        }
    }

    function runIngress(prevPath) {
        if (!prevPath || prevPath === '/home') {
            return;
        }
        var kind = 'gold';
        if (prevPath === '/cheer') {
            var m = localStorage.getItem('hollowCheerLastMood');
            if (m === 'tired') {
                kind = 'tired';
            } else if (m === 'happy') {
                kind = 'happy';
            }
        } else if (prevPath === '/meditation') {
            kind = 'star';
        }
        var target = treeTarget();
        var n = kind === 'tired' ? 22 : 16;
        var i;
        for (i = 0; i < n; i++) {
            (function (idx) {
                window.setTimeout(function () {
                    var el = document.createElement('span');
                    el.setAttribute('aria-hidden', 'true');
                    var startY = target.y + (Math.random() - 0.5) * window.innerHeight * 0.55;
                    var startX = window.innerWidth + 20 + Math.random() * 60;
                    if (kind === 'tired' && Math.random() < 0.5) {
                        el.style.cssText =
                            'position:fixed;left:' +
                            startX +
                            'px;top:' +
                            startY +
                            'px;width:10px;height:12px;border-radius:40% 60% 45% 55%;background:linear-gradient(145deg,#c2410c,#ea580c);opacity:0.7;pointer-events:none;z-index:1;transition:transform 1.85s cubic-bezier(0.22,0.61,0.36,1),opacity 1.85s ease;transform:translate(0,0) rotate(0deg);';
                    } else if (kind === 'happy') {
                        el.textContent = Math.random() < 0.5 ? '\u2665' : '';
                        if (el.textContent) {
                            el.style.cssText =
                                'position:fixed;left:' +
                                startX +
                                'px;top:' +
                                startY +
                                'px;font-size:14px;color:rgba(244,114,182,0.75);pointer-events:none;z-index:1;transition:transform 1.75s cubic-bezier(0.22,0.61,0.36,1),opacity 1.75s ease;';
                        } else {
                            el.style.cssText =
                                'position:fixed;left:' +
                                startX +
                                'px;top:' +
                                startY +
                                'px;width:8px;height:5px;border-radius:50% 50% 50% 0;transform:rotate(-30deg);background:#f9a8d4;opacity:0.65;pointer-events:none;z-index:1;transition:transform 1.75s cubic-bezier(0.22,0.61,0.36,1),opacity 1.75s ease;';
                        }
                    } else if (kind === 'star') {
                        el.style.cssText =
                            'position:fixed;left:' +
                            startX +
                            'px;top:' +
                            startY +
                            'px;width:4px;height:4px;border-radius:50%;background:#fff;box-shadow:0 0 10px rgba(255,255,255,0.95);opacity:0.9;pointer-events:none;z-index:1;transition:transform 1.65s cubic-bezier(0.22,0.61,0.36,1),opacity 1.65s ease;';
                    } else {
                        el.style.cssText =
                            'position:fixed;left:' +
                            startX +
                            'px;top:' +
                            startY +
                            'px;width:5px;height:5px;border-radius:50%;background:radial-gradient(circle,#fff7d6,#fbbf24);box-shadow:0 0 8px rgba(251,191,36,0.6);opacity:0.8;pointer-events:none;z-index:1;transition:transform 1.7s cubic-bezier(0.22,0.61,0.36,1),opacity 1.7s ease;';
                    }
                    document.body.appendChild(el);
                    requestAnimationFrame(function () {
                        requestAnimationFrame(function () {
                            var dx = target.x - startX;
                            var dy = target.y - startY;
                            el.style.transform = 'translate(' + dx + 'px,' + dy + 'px) scale(0.35)';
                            el.style.opacity = '0.15';
                        });
                    });
                    window.setTimeout(function () {
                        if (el.parentNode) {
                            el.parentNode.removeChild(el);
                        }
                    }, 2200);
                }, idx * 55);
            })(i);
        }
    }

    function injectKeyframes() {
        if (document.getElementById('home-ambient-keyframes')) {
            return;
        }
        var st = document.createElement('style');
        st.id = 'home-ambient-keyframes';
        st.textContent =
            '@keyframes homeAmbFall{0%{transform:translateY(0) translateX(0);opacity:0}8%{opacity:0.85}100%{transform:translateY(110vh) translateX(-12vw);opacity:0.35}}' +
            '@keyframes homeAmbFallSlow{0%{transform:translateY(0) translateX(0)}100%{transform:translateY(105vh) translateX(12vw)}}' +
            '@keyframes homeAmbDrift{0%{transform:translateY(0) translateX(0) rotate(-35deg);opacity:0}10%{opacity:0.65}100%{transform:translateY(110vh) translateX(-25vw) rotate(-20deg);opacity:0.2}}' +
            '@keyframes homeAmbSnow{0%{transform:translate(0,0);opacity:0}6%{opacity:0.85}100%{transform:translate(-12vw,110vh);opacity:0.25}}' +
            '@keyframes homeAmbMaple{0%{transform:translate(0,0) rotate(12deg);opacity:0}8%{opacity:0.7}100%{transform:translate(16vw,110vh) rotate(180deg);opacity:0.2}}' +
            '@keyframes homeAmbTwinkle{0%,100%{opacity:0.45;filter:blur(0)}50%{opacity:1;filter:blur(0.5px)}}';
        document.head.appendChild(st);
    }

    function burstTick(theme) {
        if (Date.now() > burstEnd) {
            if (burstTimer) {
                clearInterval(burstTimer);
                burstTimer = null;
            }
            return;
        }
        spawnForTheme(theme);
        spawnMeditate();
        spawnDaily();
    }

    /**
     * 开启旅程仪式：短时间内提高全局粒子生成频率（暖金 + 星粒）。
     */
    window.__hollowTriggerAmbientBurst = function (durationMs) {
        var d = durationMs || 8000;
        burstEnd = Date.now() + d;
        var theme = resolveAmbientTheme();
        if (burstTimer) {
            clearInterval(burstTimer);
        }
        burstTimer = window.setInterval(function () {
            burstTick(theme);
        }, 95);
        var j;
        for (j = 0; j < 14; j++) {
            window.setTimeout(function () {
                burstTick(theme);
            }, j * 40);
        }
    };

    function start() {
        injectKeyframes();
        var theme = resolveAmbientTheme();
        consumeMeditateFlag();

        var prev = typeof window.__hollowPrevPath === 'string' ? window.__hollowPrevPath : '';
        runIngress(prev);

        var interval = theme === 'meditate' ? 520 : theme === 'tired' ? 680 : 480;
        spawnTimer = window.setInterval(function () {
            spawnForTheme(theme);
        }, interval);
        for (var j = 0; j < 8; j++) {
            window.setTimeout(function () {
                spawnForTheme(theme);
            }, j * 90);
        }
    }

    document.addEventListener('visibilitychange', function () {
        paused = document.hidden;
    });

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', start);
    } else {
        start();
    }
})();
