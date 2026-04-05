/**
 * Hotwire Turbo：站内链接无整页刷新，保留侧栏 data-turbo-permanent 内的轻音乐 <audio> 不中断。
 * 在 turbo:before-cache 执行各页注册的清理函数，避免 window 级监听重复叠加。
 */
(function () {
    /** 是否为站内主页路由 /home（与 PagePathUtil.HOME_PAGE_ROUTE 一致） */
    window.__hollowPathIsHome = function () {
        try {
            var p = window.location.pathname || '';
            if (p.length > 1 && p.charAt(p.length - 1) === '/') {
                p = p.slice(0, -1);
            }
            return p === '/home';
        } catch (e) {
            return false;
        }
    };

    window.__hollowTurboCleanups = window.__hollowTurboCleanups || [];

    window.__hollowRegisterTurboCleanup = function (fn) {
        if (typeof fn !== 'function') {
            return;
        }
        window.__hollowTurboCleanups.push(fn);
    };

    document.addEventListener('turbo:before-cache', function () {
        var list = window.__hollowTurboCleanups;
        window.__hollowTurboCleanups = [];
        if (!list || !list.length) {
            return;
        }
        for (var i = 0; i < list.length; i++) {
            try {
                list[i]();
            } catch (e) {
                /* ignore */
            }
        }
    });

    document.addEventListener('turbo:load', function () {
        try {
            var prev = sessionStorage.getItem('hollowLastPath') || '';
            sessionStorage.setItem('hollowLastPath', window.location.pathname || '');
            window.__hollowPrevPath = prev;
        } catch (e) {
            window.__hollowPrevPath = '';
        }
    });
})();
