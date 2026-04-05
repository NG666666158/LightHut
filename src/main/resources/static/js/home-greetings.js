/**
 * 情绪树洞首页：按本地小时选择温柔问候（多组文案随机）。
 */
(function () {
    function pick(arr) {
        return arr[Math.floor(Math.random() * arr.length)];
    }

    function slotForHour(h) {
        if (h >= 0 && h < 5) return 'wee';
        if (h < 11) return 'morning';
        if (h < 14) return 'noon';
        if (h < 18) return 'afternoon';
        if (h < 22) return 'evening';
        return 'late';
    }

    var POOLS = {
        wee: [
            { headline: '亲爱的，夜深了。', sub: '如果睡不着也没关系，这里很静，我陪你慢慢呼吸。' },
            { headline: '凌晨好，我的朋友。', sub: '世界还在睡，你的心事可以轻轻放下一会儿。' },
            { headline: '还没休息吗？', sub: '夜很长，不用急着天亮。这一刻，只属于你。' },
            { headline: '凌晨的这一刻，你好。', sub: '星星还没散，你也值得被轻轻抱住。慢慢吐气，再吸气。' }
        ],
        morning: [
            { headline: '早安，我亲爱的朋友。', sub: '新的一天像晨光一样软，慢慢来就好。' },
            { headline: '早上好呀。', sub: '醒来已经很棒了，今天也对自己温柔一点。' },
            { headline: '早晨好，我的朋友。', sub: '深呼吸。阳光在来的路上，你也是。' },
            { headline: '早安。', sub: '愿你今天的第一口空气，都是安稳的。' }
        ],
        noon: [
            { headline: '中午好，我亲爱的朋友。', sub: '午饭时间，记得让自己的胃和心都暖一暖。' },
            { headline: '午安。', sub: '忙碌也好，休息也好，这一刻你值得被好好对待。' },
            { headline: '中午好。', sub: '停下来喝口水吧，你已经做得很好了。' },
            { headline: '午间好呀。', sub: '世界有点吵也没关系，这里永远给你留一块安静。' }
        ],
        afternoon: [
            { headline: '下午好，我的朋友。', sub: '有点倦是正常的，你不是机器，不必一直发光。' },
            { headline: '午后好呀。', sub: '把肩膀松一松，剩下的时间我们一起慢慢走。' },
            { headline: '下午好，亲爱的。', sub: '阳光斜斜的，像在为你的努力轻轻点头。' },
            { headline: '下午好。', sub: '累了就歇一小会儿，不必有愧疚。' }
        ],
        evening: [
            { headline: '晚上好，我亲爱的朋友。', sub: '天要黑了，把今天轻轻合上，你已经足够好了。' },
            { headline: '傍晚好。', sub: '回家的路上，别忘了给自己留一点温柔的光。' },
            { headline: '晚上好。', sub: '不论今天怎样，能走到这里，就很了不起。' },
            { headline: '夜晚好呀。', sub: '夜色会帮你把心事裹软一点，慢慢来。' }
        ],
        late: [
            { headline: '夜深了，亲爱的朋友。', sub: '该休息啦，心里的事明天再理也可以。' },
            { headline: '晚上好，夜很深了。', sub: '屏幕可以关上，你的感受我都接得住。' },
            { headline: '深夜好。', sub: '星星在亮，你也会好起来的——先睡一会儿，好吗？' },
            { headline: '夜深了。', sub: '今天辛苦了。把被子裹紧一点，明天的事交给明天。' }
        ]
    };

    function getHomeTimeGreeting(date) {
        var d = date || new Date();
        var slot = slotForHour(d.getHours());
        return pick(POOLS[slot]);
    }

    function applyHomeTimeGreeting(headlineEl, subEl, date) {
        if (!headlineEl || !subEl) return;
        var g = getHomeTimeGreeting(date);
        headlineEl.textContent = g.headline;
        subEl.textContent = g.sub;
    }

    window.HollowHomeGreetings = {
        getHomeTimeGreeting: getHomeTimeGreeting,
        applyHomeTimeGreeting: applyHomeTimeGreeting,
        slotForHour: slotForHour
    };
})();
