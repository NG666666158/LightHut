/**
 * 全站导航痕迹：上一页 pathname 供首页粒子「飘回」等联动使用。
 * 须在页面尽早执行（建议 head 内联或首个同步脚本）。
 */
(function () {
    try {
        var prev = sessionStorage.getItem('hollowLastPath') || '';
        sessionStorage.setItem('hollowLastPath', window.location.pathname || '');
        window.__hollowPrevPath = prev;
    } catch (e) {
        window.__hollowPrevPath = '';
    }
})();
