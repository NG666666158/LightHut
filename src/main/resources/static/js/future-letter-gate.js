/**
 * 全站「未来寄信」：到达约定日期后，任意页面首次加载时弹出本地保存的信件。
 * 数据：localStorage hollowFutureLetters —— [{ id, text, openOn: 'YYYY-MM-DD', createdAt }]
 */
(function () {
    var KEY = 'hollowFutureLetters';

    function pad(n) {
        return n < 10 ? '0' + n : String(n);
    }

    function todayStr() {
        var d = new Date();
        return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate());
    }

    function loadList() {
        try {
            var a = JSON.parse(localStorage.getItem(KEY) || '[]');
            return Array.isArray(a) ? a : [];
        } catch (e) {
            return [];
        }
    }

    function saveList(arr) {
        try {
            localStorage.setItem(KEY, JSON.stringify(arr));
        } catch (e) { /* ignore */ }
    }

    function injectStylesOnce() {
        if (document.getElementById('hollow-future-letter-gate-style')) {
            return;
        }
        var s = document.createElement('style');
        s.id = 'hollow-future-letter-gate-style';
        s.textContent =
            '.hollow-fl-backdrop{position:fixed;inset:0;z-index:100200;background:rgba(45,40,55,0.42);backdrop-filter:blur(6px);-webkit-backdrop-filter:blur(6px);display:flex;align-items:center;justify-content:center;padding:1.25rem;animation:hollowFlFade .35s ease}' +
            '.hollow-fl-card{max-width:min(420px,100%);width:100%;border-radius:1.25rem;padding:1.5rem 1.35rem 1.25rem;background:linear-gradient(165deg,#fffefb 0%,#faf4ff 45%,#fff8ef 100%);box-shadow:0 24px 60px rgba(80,60,100,0.22),0 0 0 1px rgba(253,171,171,0.35);font-family:system-ui,-apple-system,"Segoe UI",sans-serif}' +
            '.hollow-fl-title{margin:0 0 .35rem;font-size:1.15rem;font-weight:800;color:#5c3d4a;letter-spacing:.02em}' +
            '.hollow-fl-sub{margin:0 0 1rem;font-size:.8rem;color:#7a6570;line-height:1.45}' +
            '.hollow-fl-body{margin:0 0 1.25rem;font-size:.95rem;line-height:1.65;color:#3d3538;white-space:pre-wrap;max-height:min(50vh,320px);overflow-y:auto}' +
            '.hollow-fl-btn{width:100%;border:0;border-radius:999px;padding:.72rem 1rem;font-weight:700;font-size:.9rem;cursor:pointer;background:linear-gradient(45deg,#935252,#fdabab);color:#fff;box-shadow:0 8px 24px rgba(147,82,82,0.25)}' +
            '.hollow-fl-btn:hover{filter:brightness(1.05)}' +
            '@keyframes hollowFlFade{from{opacity:0}to{opacity:1}}';
        document.head.appendChild(s);
    }

    function showLetter(letter, done) {
        injectStylesOnce();
        var root = document.createElement('div');
        root.className = 'hollow-fl-backdrop';
        root.setAttribute('role', 'dialog');
        root.setAttribute('aria-modal', 'true');
        root.setAttribute('aria-labelledby', 'hollow-fl-title');
        root.innerHTML =
            '<div class="hollow-fl-card">' +
            '<h2 class="hollow-fl-title" id="hollow-fl-title">来自过去的你 · 时光信箱</h2>' +
            '<p class="hollow-fl-sub">这是你曾写给今天自己的话。慢慢读，不着急。</p>' +
            '<p class="hollow-fl-body"></p>' +
            '<button type="button" class="hollow-fl-btn">收进心里</button>' +
            '</div>';
        root.querySelector('.hollow-fl-body').textContent = letter.text || '';
        function close() {
            if (root.parentNode) {
                root.parentNode.removeChild(root);
            }
            if (typeof done === 'function') {
                done();
            }
        }
        root.querySelector('.hollow-fl-btn').addEventListener('click', close);
        root.addEventListener('click', function (e) {
            if (e.target === root) {
                close();
            }
        });
        document.body.appendChild(root);
    }

    function processQueue() {
        var list = loadList();
        var today = todayStr();
        var i;
        for (i = 0; i < list.length; i++) {
            if (list[i].openOn && list[i].openOn <= today) {
                break;
            }
        }
        if (i >= list.length) {
            return;
        }
        var letter = list[i];
        showLetter(letter, function () {
            list.splice(i, 1);
            saveList(list);
            setTimeout(processQueue, 320);
        });
    }

    function boot() {
        processQueue();
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', boot);
    } else {
        boot();
    }
})();
