package com.friend.hollow.util;

/**
 * 页面路径与模板名工具类（常量集中管理）。
 *
 * <p>为什么需要这个类：</p>
 * <ul>
 *     <li>避免在多个控制器里重复写字符串，减少手误风险</li>
 *     <li>后续如果改模板名，只改一处即可</li>
 * </ul>
 *
 * <p>可修改点：</p>
 * <ul>
 *     <li>如果你把 templates/index.html 改名为 templates/home.html，
 *     那么把 INDEX_TEMPLATE 从 "index" 改为 "home" 即可。</li>
 * </ul>
 */
public final class PagePathUtil {

    /**
     * 落地欢迎页模板（未登录展示）。
     */
    public static final String LANDING_TEMPLATE = "index";
    /**
     * 主页模板名（对应 src/main/resources/templates/home.html）。
     *
     * <p>可修改点：
     * 如果你把 home.html 改名为 dashboard.html，
     * 这里改成 "dashboard" 即可。</p>
     */
    public static final String HOME_TEMPLATE = "home";
    /**
     * 挚友打气站模板名（对应 src/main/resources/templates/cheer.html）。
     */
    public static final String CHEER_TEMPLATE = "cheer";
    /**
     * 友谊星光墙模板名（对应 src/main/resources/templates/starlight.html）。
     */
    public static final String STARLIGHT_TEMPLATE = "starlight";
    /**
     * 温柔提醒模板名（对应 src/main/resources/templates/reminder.html）。
     */
    public static final String REMINDER_TEMPLATE = "reminder";
    /**
     * 冥想引导页模板名（对应 templates/meditation.html）。
     */
    public static final String MEDITATION_TEMPLATE = "meditation";
    /**
     * 探索页模板名（对应 templates/explore.html）。
     */
    public static final String EXPLORE_TEMPLATE = "explore";

    /**
     * 登录页路由。
     */
    public static final String LOGIN_PAGE_ROUTE = "/";

    /**
     * 主页路由。
     */
    public static final String HOME_PAGE_ROUTE = "/home";
    /**
     * 挚友打气站路由。
     */
    public static final String CHEER_PAGE_ROUTE = "/cheer";
    /**
     * 友谊星光墙路由。
     */
    public static final String STARLIGHT_PAGE_ROUTE = "/starlight";
    /**
     * 温柔提醒路由。
     */
    public static final String REMINDER_PAGE_ROUTE = "/reminder";
    /**
     * 冥想引导页路由（顶栏「冥想」入口）。
     */
    public static final String MEDITATION_PAGE_ROUTE = "/meditation";
    /**
     * 探索页路由（顶栏「探索」入口）。
     */
    public static final String EXPLORE_PAGE_ROUTE = "/explore";

    /**
     * 冥想页：项目根目录 {@code mp3} 文件夹内白噪音列表（扫描 .mp3）。
     */
    public static final String MEDITATION_NOISE_TRACKS_API = "/api/meditation/noise-tracks";

    /**
     * 侧栏轻音乐：项目根目录 {@code musics} 文件夹内曲目列表（扫描 .mp3）。
     */
    public static final String HOME_MUSIC_TRACKS_API = "/api/home/music-tracks";

    /**
     * 探索页「互助社区」文案列表（内置 + 可选外部 JSON，服务端拉取）。
     */
    public static final String EXPLORE_COMMUNITY_FEED_API = "/api/explore/community-feed";

    /**
     * 主页数据接口路径（前端可通过 fetch 调用）。
     *
     * <p>可修改点：
     * 如果你想把接口改成 /api/v1/home/overview，只需要修改这里。</p>
     */
    public static final String HOME_OVERVIEW_API = "/api/home/overview";
    /**
     * 签到状态查询接口路径（返回是否已签、连续天数、进度等）。
     */
    public static final String HOME_SIGN_IN_STATUS_API = "/api/home/sign-in/status";

    /**
     * 执行签到接口路径（点击签到按钮后调用）。
     */
    public static final String HOME_SIGN_IN_ACTION_API = "/api/home/sign-in";

    /**
     * 记录心情（POST JSON）。
     */
    public static final String HOME_MOOD_API = "/api/home/mood";
    /**
     * 最近心情列表。
     */
    public static final String HOME_MOOD_RECENT_API = "/api/home/mood/recent";

    /**
     * 首页「挚友的鼓励」滚动列表（聚合配置、内置与可选外部 JSON）。
     */
    public static final String HOME_ENCOURAGEMENT_FEED_API = "/api/home/encouragement-feed";

    /**
     * 星光墙：回忆列表（支持分类、排序、分页）。
     */
    public static final String STARLIGHT_MEMORIES_API = "/api/starlight/memories";
    /**
     * 星光墙：仅上传图片，返回可访问 URL（可与创建分步调用）。
     */
    public static final String STARLIGHT_UPLOAD_API = "/api/starlight/upload";

    /**
     * 温柔提醒：月历数据。
     */
    public static final String REMINDER_MONTH_API = "/api/reminder/month";
    /**
     * 温柔提醒：某日待办列表。
     */
    public static final String REMINDER_DAY_API = "/api/reminder/day";
    /**
     * 温柔提醒：创建一条。
     */
    public static final String REMINDER_CREATE_API = "/api/reminder";

    /**
     * 挚友打气站：今日「接收拥抱」累计次数（按上海时区自然日）。
     */
    public static final String CHEER_HUG_TODAY_API = "/api/cheer/hug/today";
    /**
     * 挚友打气站：记录一次拥抱点击（可重复调用）。
     */
    public static final String CHEER_HUG_API = "/api/cheer/hug";
    /**
     * 挚友打气站：惊喜动态列表（所有访客点击累积，含统计展示字段）。
     */
    public static final String CHEER_SURPRISES_API = "/api/cheer/surprises";
    /**
     * 挚友打气站：记录一次「给我惊喜」点击（写入一条展示卡片）。
     */
    public static final String CHEER_SURPRISE_API = "/api/cheer/surprise";

    /**
     * 私有构造器：防止被实例化。
     *
     * <p>工具类只放静态常量/方法，不需要 new 对象。</p>
     */
    private PagePathUtil() {
        // 工具类无需实例化，这里故意留空。
    }
}
