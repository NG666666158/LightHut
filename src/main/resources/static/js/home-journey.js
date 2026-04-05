/**
 * 首页「开启旅程」仪式：卡片放大、逐行发光、金涟漪、粒子爆发、自我对话弹层、星光投喂成长树。
 * 依赖：hollow-time-echo.js、home-ambient.js 的 burst API、#homeTreeStageWrap 等 DOM。
 */
(function () {
    if (location.pathname !== '/home') {
        return;
    }

    function qs(id) {
        return document.getElementById(id);
    }

    function spawnRippleAt(x, y) {
        var layer = qs('homeJourneyRippleLayer');
        if (!layer) {
            return;
        }
        var ring = document.createElement('div');
        ring.className = 'home-journey-ripple-ring';
        ring.style.left = x + 'px';
        ring.style.top = y + 'px';
        layer.appendChild(ring);
        window.setTimeout(function () {
            if (ring.parentNode) {
                ring.parentNode.removeChild(ring);
            }
        }, 1600);
    }

    function runLineGlow() {
        var a = qs('dmTitleInner');
        var b = qs('dmSubtitleInner');
        if (a) {
            a.classList.add('dm-journey-line--glow');
        }
        window.setTimeout(function () {
            if (b) {
                b.classList.add('dm-journey-line--glow');
            }
        }, 220);
    }

    function clearLineGlow() {
        var a = qs('dmTitleInner');
        var b = qs('dmSubtitleInner');
        if (a) {
            a.classList.remove('dm-journey-line--glow');
        }
        if (b) {
            b.classList.remove('dm-journey-line--glow');
        }
    }

    function openJourneyModal() {
        var m = qs('journeyDialogModal');
        var ta = qs('journeyDialogInput');
        if (!m || !ta) {
            return;
        }
        ta.value = '';
        delete ta.dataset.journeyFromInspire;
        m.classList.remove('hidden');
        m.classList.add('flex');
        window.setTimeout(function () {
            ta.focus();
        }, 80);
    }

    function closeJourneyModal() {
        var m = qs('journeyDialogModal');
        if (!m) {
            return;
        }
        m.classList.add('hidden');
        m.classList.remove('flex');
    }

    function spawnStarFlightToTree(fromRect) {
        var tree = qs('homeTreeStageWrap');
        if (!tree || !fromRect) {
            return;
        }
        var tr = tree.getBoundingClientRect();
        var tx = tr.left + tr.width / 2 + (Math.random() - 0.5) * 24;
        var ty = tr.top + tr.height * 0.38 + (Math.random() - 0.5) * 20;
        var sx = fromRect.left + fromRect.width / 2;
        var sy = fromRect.top + fromRect.height / 2;
        var n = 28;
        var i;
        for (i = 0; i < n; i++) {
            (function (idx) {
                window.setTimeout(function () {
                    var el = document.createElement('span');
                    el.className = 'home-journey-star-particle';
                    el.setAttribute('aria-hidden', 'true');
                    var ox = (Math.random() - 0.5) * 36;
                    var oy = (Math.random() - 0.5) * 20;
                    el.style.left = sx + ox + 'px';
                    el.style.top = sy + oy + 'px';
                    el.style.setProperty('--jdx', tx - sx - ox + 'px');
                    el.style.setProperty('--jdy', ty - sy - oy + 'px');
                    document.body.appendChild(el);
                    window.setTimeout(function () {
                        if (el.parentNode) {
                            el.parentNode.removeChild(el);
                        }
                    }, 1100);
                }, idx * 28);
            })(i);
        }
    }

    function bumpInspireEnergy() {
        try {
            var v = parseInt(localStorage.getItem('hollowForestInspireBonus') || '0', 10);
            if (isNaN(v)) {
                v = 0;
            }
            localStorage.setItem('hollowForestInspireBonus', String(Math.min(12, v + 6)));
        } catch (e) { /* ignore */ }
        if (typeof window.__applyHomeGrowthBar === 'function') {
            window.__applyHomeGrowthBar();
        }
    }

    function bumpEchoEnergy() {
        try {
            var v = parseInt(localStorage.getItem('hollowForestEchoBonus') || '0', 10);
            if (isNaN(v)) {
                v = 0;
            }
            localStorage.setItem('hollowForestEchoBonus', String(Math.min(25, v + 12)));
        } catch (e) { /* ignore */ }
        if (typeof window.__applyHomeGrowthBar === 'function') {
            window.__applyHomeGrowthBar();
        }
        var stem = qs('treeStem');
        var leaves = document.querySelectorAll('.tree-stage-wrap .leaf');
        if (stem) {
            stem.classList.add('joy-energy-pulse');
        }
        leaves.forEach(function (el) {
            el.classList.add('joy-leaf-pulse');
        });
        window.setTimeout(function () {
            if (stem) {
                stem.classList.remove('joy-energy-pulse');
            }
            leaves.forEach(function (el) {
                el.classList.remove('joy-leaf-pulse');
            });
        }, 2600);
    }

    function onJourneySubmit() {
        var ta = qs('journeyDialogInput');
        var err = qs('journeyDialogError');
        if (!ta || !window.HollowTimeEcho) {
            return;
        }
        var text = ta.value.trim();
        if (!text) {
            if (err) {
                err.textContent = '写一句给自己听的话吧，哪怕很短。';
                err.classList.remove('hidden');
            }
            return;
        }
        if (err) {
            err.classList.add('hidden');
        }
        var entry = HollowTimeEcho.addEntry(text);
        if (!entry) {
            if (err) {
                err.textContent = '保存失败，请稍后再试。';
                err.classList.remove('hidden');
            }
            return;
        }
        if (ta.dataset.journeyFromInspire === '1' && window.HollowExploreBonds) {
            HollowExploreBonds.addGalleryLine(text);
            bumpInspireEnergy();
        }
        delete ta.dataset.journeyFromInspire;
        try {
            sessionStorage.setItem('hollowEchoSuggestReminder', '1');
        } catch (e2) { /* ignore */ }
        var modalBox = qs('journeyDialogPanel');
        var r = modalBox ? modalBox.getBoundingClientRect() : ta.getBoundingClientRect();
        closeJourneyModal();
        spawnStarFlightToTree(r);
        window.setTimeout(bumpEchoEnergy, 400);
        if (typeof window.__hollowSpawnEchoStickyNote === 'function') {
            window.__hollowSpawnEchoStickyNote(entry.text);
        }
    }

    function bindJourneyWave() {
        var ta = qs('journeyDialogInput');
        var wave = qs('journeyDialogWave');
        if (!ta || !wave) {
            return;
        }
        function updateWave() {
            var rect = ta.getBoundingClientRect();
            wave.style.width = rect.width + 'px';
            wave.style.left = rect.left + 'px';
            wave.style.top = rect.bottom + window.scrollY + 2 + 'px';
        }
        ta.addEventListener('focus', function () {
            wave.classList.remove('hidden');
            updateWave();
        });
        ta.addEventListener('blur', function () {
            wave.classList.add('hidden');
        });
        ta.addEventListener('input', updateWave);
        window.addEventListener('resize', updateWave);
    }

    function init() {
        var card = qs('dmJourneyCard');
        var btn = qs('dmCtaBtn');
        var rippleLayer = qs('homeJourneyRippleLayer');
        if (!card || !btn) {
            return;
        }

        btn.addEventListener('click', function (e) {
            e.preventDefault();
            e.stopPropagation();
            var r = card.getBoundingClientRect();
            var cx = r.left + r.width / 2;
            var cy = r.top + r.height / 2;
            card.classList.add('dm-journey-card--ritual');
            runLineGlow();
            if (typeof window.__hollowTriggerAmbientBurst === 'function') {
                window.__hollowTriggerAmbientBurst(9000);
            }
            spawnRippleAt(cx, cy);
            window.setTimeout(function () {
                spawnRippleAt(cx, cy);
            }, 200);
            window.setTimeout(function () {
                openJourneyModal();
            }, 720);
            window.setTimeout(function () {
                card.classList.remove('dm-journey-card--ritual');
                clearLineGlow();
            }, 1400);
        });

        var cancelBtn = qs('journeyDialogCancel');
        var goMedBtn = qs('journeyDialogGoMeditation');
        var okBtn = qs('journeyDialogSubmit');
        var backdrop = qs('journeyDialogBackdrop');

        if (cancelBtn) {
            cancelBtn.addEventListener('click', closeJourneyModal);
        }
        if (backdrop) {
            backdrop.addEventListener('click', closeJourneyModal);
        }
        if (goMedBtn) {
            goMedBtn.addEventListener('click', function () {
                var href = (btn.dataset.ctaHref || '/meditation').trim();
                if (href && href !== '#') {
                    window.location.href = href;
                }
            });
        }
        if (okBtn) {
            okBtn.addEventListener('click', onJourneySubmit);
        }

        var insBtn = qs('journeyInspireAssistBtn');
        if (insBtn && window.HollowExploreBonds) {
            insBtn.addEventListener('click', function () {
                var ta2 = qs('journeyDialogInput');
                if (!ta2) {
                    return;
                }
                var line = HollowExploreBonds.pickRandomGalleryLine();
                if (line) {
                    ta2.value = line;
                    ta2.dataset.journeyFromInspire = '1';
                }
            });
        }

        bindJourneyWave();
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }
})();
