/**
 * 时光回响库：访客自我对话永久保存在 localStorage，供全站温柔素材随机抽取。
 */
(function () {
    var LS_KEY = 'hollowTimeEchoLibrary';
    var MAX_ENTRIES = 320;

    function load() {
        try {
            var a = JSON.parse(localStorage.getItem(LS_KEY) || '[]');
            return Array.isArray(a) ? a : [];
        } catch (e) {
            return [];
        }
    }

    function save(arr) {
        try {
            localStorage.setItem(LS_KEY, JSON.stringify(arr.slice(-MAX_ENTRIES)));
        } catch (e) { /* quota */ }
    }

    /**
     * @returns {{ id: string, text: string, ts: number }|null}
     */
    function addEntry(text) {
        var t = String(text || '').trim();
        if (!t || t.length > 800) {
            return null;
        }
        var entry = {
            id: 'echo-' + Date.now() + '-' + Math.random().toString(36).slice(2, 10),
            text: t,
            ts: Date.now()
        };
        var a = load();
        a.push(entry);
        save(a);
        return entry;
    }

    function getRandom() {
        var a = load();
        if (!a.length) {
            return null;
        }
        return a[Math.floor(Math.random() * a.length)];
    }

    function count() {
        return load().length;
    }

    window.HollowTimeEcho = {
        LS_KEY: LS_KEY,
        load: load,
        addEntry: addEntry,
        getRandom: getRandom,
        count: count
    };
})();
