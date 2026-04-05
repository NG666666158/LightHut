/**
 * 探索页联动：灵感图库、互助社区点赞、打气站推荐、惊喜盲盒陌生人来信。
 */
(function () {
    var LS_GALLERY = 'hollowExploreInspirationGallery';
    var LS_COMMUNITY = 'hollowExploreCommunityPosts';
    var LS_CHEER_TOP = 'hollowCheerCommunityComfort';
    var LS_STRANGER = 'hollowSurpriseStrangerLetters';
    var SS_PENDING = 'hollowExplorePendingGreeting';

    var DEFAULT_GALLERY = [
        '让温柔的晨光洗去昨日的忧伤，今天又是全新绽放的时刻。',
        '慢慢来，允许自己有低落的日子。我们在云端拥抱你。',
        '今天的焦虑好像少了一点点，虽然只有一点点，但也值得庆祝。',
        '看到大家的留言，觉得不是一个人在战斗。谢谢你们的存在。',
        '把心事折成纸飞机吧，风会带走一点重量，留下轻盈的你。',
        '深呼吸。在这里，你是安全的。',
        '把肩膀松一松，剩下的时间我们一起慢慢走。',
        '愿你被世界温柔接住，像你接住别人的难过那样。'
    ];

    /**
     * 接口失败时的兜底列表（与后端内置前几则一致，避免空白页）。
     */
    var FALLBACK_COMMUNITY_POSTS = [
        { id: 'c-seed-1', text: '慢慢来，允许自己有低落的日子。我们在云端拥抱你。', author: '匿名飞鸟', likes: 24, timeLabel: '2 小时前' },
        { id: 'c-seed-2', text: '今天的焦虑好像少了一点点，虽然只有一点点，但也值得庆祝。', author: '寻找微光', likes: 18, timeLabel: '5 小时前' },
        { id: 'c-seed-3', text: '看到大家的留言，觉得不是一个人在战斗。谢谢你们的存在。', author: '星空守望', likes: 31, timeLabel: '昨天' },
        { id: 'c-seed-4', text: '把心事折成纸飞机吧，风会带走一点重量，留下轻盈的你。', author: '晚风信笺', likes: 15, timeLabel: '3 天前' },
        { id: 'c-seed-5', text: '今天没有做得很好也没关系，你还在，就已经很棒了。', author: '云朵邮差', likes: 22, timeLabel: '1 小时前' },
        { id: 'c-seed-6', text: '给自己泡一杯热的吧，暖胃也暖心。', author: '橘子汽水', likes: 19, timeLabel: '4 小时前' },
        { id: 'c-seed-7', text: '你不是麻烦，你是值得被温柔对待的人。', author: '薄荷午后', likes: 27, timeLabel: '昨天' },
        { id: 'c-seed-8', text: '天黑了就点灯，累了就歇一歇，世界不会怪你。', author: '月亮饼干', likes: 14, timeLabel: '2 天前' },
        { id: 'c-seed-9', text: '有人正在远方为你加油，只是你还不知道。', author: '小森林', likes: 33, timeLabel: '6 小时前' },
        { id: 'c-seed-10', text: '哭完记得擦擦脸，你笑起来真的很好看。', author: '晚风信箱', likes: 21, timeLabel: '昨天' },
        { id: 'c-seed-11', text: '不必强撑坚强，柔软也是一种力量。', author: '星光旅人', likes: 17, timeLabel: '3 小时前' },
        { id: 'c-seed-12', text: '今天的你，已经比昨天多走了一步。', author: '藏青林', likes: 26, timeLabel: '5 天前' }
    ];

    function isUserCommunityPost(p) {
        return p && p.id && /^c-\d{10,}-/.test(p.id);
    }

    function mergeServerFeedPosts(serverList) {
        if (!Array.isArray(serverList)) {
            return;
        }
        var raw = loadCommunity();
        if (!raw) {
            raw = [];
        }
        var userPosts = raw.filter(isUserCommunityPost);
        var likeById = {};
        raw.forEach(function (p) {
            if (p && p.id) {
                likeById[p.id] = p.likes;
            }
        });
        var newSeeds = serverList.map(function (p) {
            if (!p || !p.id || !p.text) {
                return null;
            }
            var baseLikes = typeof p.likes === 'number' ? p.likes : parseInt(p.likes, 10) || 0;
            var prev = likeById[p.id];
            var likes = Math.max(baseLikes, prev != null ? prev : 0);
            return {
                id: p.id,
                text: String(p.text).trim(),
                author: (p.author && String(p.author).trim()) || '匿名旅人',
                likes: likes,
                ts: typeof p.ts === 'number' ? p.ts : Date.now(),
                timeLabel: (p.timeLabel && String(p.timeLabel).trim()) || ''
            };
        }).filter(Boolean);
        saveCommunity(newSeeds.concat(userPosts));
    }

    function formatCommunityPostTime(ts) {
        if (ts == null || ts <= 0) {
            return '';
        }
        var diff = Date.now() - ts;
        if (diff < 60000) {
            return '刚刚';
        }
        if (diff < 3600000) {
            return Math.floor(diff / 60000) + ' 分钟前';
        }
        if (diff < 86400000) {
            return Math.floor(diff / 3600000) + ' 小时前';
        }
        if (diff < 86400000 * 2) {
            return '昨天';
        }
        var d = Math.floor(diff / 86400000);
        if (d < 7) {
            return d + ' 天前';
        }
        return '不久前';
    }

    function loadGalleryLines() {
        try {
            var a = JSON.parse(localStorage.getItem(LS_GALLERY) || '[]');
            if (!Array.isArray(a)) {
                return DEFAULT_GALLERY.slice();
            }
            var lines = a
                .map(function (x) {
                    return typeof x === 'string' ? x : x && x.text;
                })
                .filter(Boolean);
            var merged = DEFAULT_GALLERY.concat(lines);
            var seen = {};
            var out = [];
            merged.forEach(function (t) {
                var k = String(t).trim();
                if (k && !seen[k]) {
                    seen[k] = 1;
                    out.push(k);
                }
            });
            return out.length ? out : DEFAULT_GALLERY.slice();
        } catch (e) {
            return DEFAULT_GALLERY.slice();
        }
    }

    function addGalleryLine(text) {
        var t = String(text || '').trim();
        if (!t || t.length > 600) {
            return;
        }
        try {
            var a = JSON.parse(localStorage.getItem(LS_GALLERY) || '[]');
            if (!Array.isArray(a)) {
                a = [];
            }
            a.push({ text: t, ts: Date.now() });
            localStorage.setItem(LS_GALLERY, JSON.stringify(a.slice(-200)));
        } catch (e2) { /* ignore */ }
    }

    function pickRandomGalleryLine() {
        var pool = loadGalleryLines();
        return pool[Math.floor(Math.random() * pool.length)] || '';
    }

    function loadCommunity() {
        try {
            var a = JSON.parse(localStorage.getItem(LS_COMMUNITY) || '[]');
            return Array.isArray(a) && a.length ? a : null;
        } catch (e) {
            return null;
        }
    }

    function saveCommunity(arr) {
        try {
            localStorage.setItem(LS_COMMUNITY, JSON.stringify(arr));
        } catch (e) { /* ignore */ }
        syncCheerTopThree(arr);
    }

    function ensureCommunity() {
        var a = loadCommunity();
        if (!a) {
            saveCommunity([]);
            return [];
        }
        return a;
    }

    function syncCheerTopThree(arr) {
        var list = arr || loadCommunity() || [];
        var sorted = list.slice().sort(function (a, b) {
            return (b.likes || 0) - (a.likes || 0);
        });
        var texts = sorted.slice(0, 3).map(function (p) {
            return p.text;
        }).filter(Boolean);
        try {
            localStorage.setItem(LS_CHEER_TOP, JSON.stringify({ texts: texts, updated: Date.now() }));
        } catch (e) { /* ignore */ }
    }

    function getCheerCommunityTexts() {
        try {
            var o = JSON.parse(localStorage.getItem(LS_CHEER_TOP) || '{}');
            return Array.isArray(o.texts) ? o.texts : [];
        } catch (e) {
            return [];
        }
    }

    function pickComfortFromCommunity() {
        var t = getCheerCommunityTexts();
        if (!t.length) {
            return null;
        }
        return t[Math.floor(Math.random() * t.length)];
    }

    function likePost(postId) {
        var list = ensureCommunity();
        var i;
        for (i = 0; i < list.length; i++) {
            if (list[i].id === postId) {
                list[i].likes = (list[i].likes || 0) + 1;
                saveCommunity(list);
                return list[i].likes;
            }
        }
        return null;
    }

    function addCommunityPost(text, author) {
        var list = ensureCommunity();
        var t = String(text || '').replace(/^「|」$/g, '').trim();
        if (!t || t.length > 400) {
            return null;
        }
        var id = 'c-' + Date.now() + '-' + Math.random().toString(36).slice(2, 8);
        list.push({
            id: id,
            text: t,
            author: author || '匿名旅人',
            likes: 1,
            ts: Date.now()
        });
        saveCommunity(list);
        return id;
    }

    function addStrangerLetter(text) {
        var t = String(text || '').trim();
        if (!t || t.length > 400) {
            return;
        }
        try {
            var a = JSON.parse(localStorage.getItem(LS_STRANGER) || '[]');
            if (!Array.isArray(a)) {
                a = [];
            }
            a.push({ id: 'str-' + Date.now(), text: t, ts: Date.now() });
            localStorage.setItem(LS_STRANGER, JSON.stringify(a.slice(-100)));
        } catch (e) { /* ignore */ }
    }

    function pickStrangerLetterText() {
        try {
            var a = JSON.parse(localStorage.getItem(LS_STRANGER) || '[]');
            if (!Array.isArray(a) || !a.length) {
                return null;
            }
            var it = a[Math.floor(Math.random() * a.length)];
            return it && it.text ? it.text : null;
        } catch (e) {
            return null;
        }
    }

    function armHomeGreeting(headline, sub) {
        var h = String(headline || '').trim();
        if (!h) {
            return;
        }
        try {
            sessionStorage.setItem(
                SS_PENDING,
                JSON.stringify({
                    headline: h,
                    sub: sub || '来自探索页 · 今日灵感礼物'
                })
            );
        } catch (e) { /* ignore */ }
    }

    function consumePendingHomeGreeting() {
        try {
            var s = sessionStorage.getItem(SS_PENDING);
            if (!s) {
                return null;
            }
            sessionStorage.removeItem(SS_PENDING);
            return JSON.parse(s);
        } catch (e) {
            return null;
        }
    }

    function getPostById(id) {
        var list = ensureCommunity();
        var i;
        for (i = 0; i < list.length; i++) {
            if (list[i].id === id) {
                return list[i];
            }
        }
        return null;
    }

    window.HollowExploreBonds = {
        LS_GALLERY: LS_GALLERY,
        LS_COMMUNITY: LS_COMMUNITY,
        FALLBACK_COMMUNITY_POSTS: FALLBACK_COMMUNITY_POSTS,
        loadGalleryLines: loadGalleryLines,
        pickRandomGalleryLine: pickRandomGalleryLine,
        addGalleryLine: addGalleryLine,
        armHomeGreeting: armHomeGreeting,
        consumePendingHomeGreeting: consumePendingHomeGreeting,
        likePost: likePost,
        addCommunityPost: addCommunityPost,
        mergeServerFeedPosts: mergeServerFeedPosts,
        formatCommunityPostTime: formatCommunityPostTime,
        addStrangerLetter: addStrangerLetter,
        pickStrangerLetterText: pickStrangerLetterText,
        pickComfortFromCommunity: pickComfortFromCommunity,
        getCheerCommunityTexts: getCheerCommunityTexts,
        getPostById: getPostById,
        listCommunityPosts: function () {
            return ensureCommunity();
        },
        syncCheerTopThree: function () {
            syncCheerTopThree(ensureCommunity());
        }
    };
})();
