package com.friend.hollow.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 根据 mp3 文件名（英文）生成 2～4 字中文标题；无法识别时用「声壹」…「声拾叁」等序名。
 */
public final class MeditationNoiseLabelMapper {

    private static final Pattern STEM_SPLIT = Pattern.compile("[_\\-\\s]+");
    private static final String[] ORDINAL = {
            "", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "拾", "拾壹", "拾贰", "拾叁", "拾肆", "拾伍"
    };

    private static final Map<String, String> STEM_CN = new HashMap<>();

    static {
        // 常见全词 / 复合词（文件名不含扩展名、小写）
        STEM_CN.put("rain", "细雨声");
        STEM_CN.put("gentle_rain", "细雨绵绵");
        STEM_CN.put("light_rain", "微雨轻落");
        STEM_CN.put("heavy_rain", "骤雨敲窗");
        STEM_CN.put("rainfall", "雨落如丝");
        STEM_CN.put("raindrops", "雨滴清响");
        STEM_CN.put("drizzle", "霏霏细雨");
        STEM_CN.put("storm", "远雷隐隐");
        STEM_CN.put("thunder", "雷鸣云外");
        STEM_CN.put("forest", "林间晨曦");
        STEM_CN.put("forest_morning", "林曦微光");
        STEM_CN.put("birds", "林鸟清啼");
        STEM_CN.put("bird", "鸟语林间");
        STEM_CN.put("morning", "朝露晨光");
        STEM_CN.put("ocean", "海天一色");
        STEM_CN.put("waves", "海浪拍岸");
        STEM_CN.put("wave", "潮声入耳");
        STEM_CN.put("seaside", "海风轻抚");
        STEM_CN.put("beach", "沙滩细浪");
        STEM_CN.put("wind", "微风过隙");
        STEM_CN.put("breeze", "清风徐来");
        STEM_CN.put("chime", "风铃微语");
        STEM_CN.put("chimes", "铃音入梦");
        STEM_CN.put("bell", "钟磬余音");
        STEM_CN.put("fire", "篝火暖意");
        STEM_CN.put("campfire", "营火噼啪");
        STEM_CN.put("fireplace", "炉火细暖");
        STEM_CN.put("crackle", "噼啪细响");
        STEM_CN.put("stream", "溪涧叮咚");
        STEM_CN.put("river", "河水潺潺");
        STEM_CN.put("waterfall", "飞瀑漱石");
        STEM_CN.put("brook", "小溪潺潺");
        STEM_CN.put("night", "夜色静谧");
        STEM_CN.put("crickets", "夏夜虫鸣");
        STEM_CN.put("cicada", "蝉声入梦");
        STEM_CN.put("snow", "雪落无声");
        STEM_CN.put("library", "书馆静阅");
        STEM_CN.put("cafe", "咖啡馆里");
        STEM_CN.put("coffee", "咖啡余香");
        STEM_CN.put("zen", "禅意空音");
        STEM_CN.put("meditation", "冥想之境");
        STEM_CN.put("ambient", "境韵悠长");
        STEM_CN.put("calm", "宁神静气");
        STEM_CN.put("peaceful", "平和心境");
        STEM_CN.put("soft", "柔声轻抚");
        STEM_CN.put("gentle", "温柔絮语");
        STEM_CN.put("deep", "深息如海");
        STEM_CN.put("white_noise", "白噪柔幕");
        STEM_CN.put("pink_noise", "粉噪轻抚");
        STEM_CN.put("brown_noise", "褐噪深息");
        STEM_CN.put("nature", "自然之境");
        STEM_CN.put("field", "旷野微风");
        STEM_CN.put("meadow", "草甸虫声");
        STEM_CN.put("spring", "春水叮咚");
        STEM_CN.put("autumn", "秋叶簌簌");
        STEM_CN.put("winter", "冬雪簌簌");
        STEM_CN.put("temple", "梵钟悠远");
        STEM_CN.put("singing_bowl", "颂钵清音");
        STEM_CN.put("bowl", "钵音入心");
        STEM_CN.put("piano", "琴键微光");
        STEM_CN.put("harp", "竖琴如露");
        STEM_CN.put("flute", "笛声入云");
        STEM_CN.put("lofi", "低保温柔");
        STEM_CN.put("lo_fi", "低保温柔");
        STEM_CN.put("sleep", "入眠轻抚");
        STEM_CN.put("relax", "松弛之境");
        STEM_CN.put("healing", "疗愈微光");
        STEM_CN.put("asia", "东方清韵");
        STEM_CN.put("bamboo", "竹林风响");
        STEM_CN.put("tea", "茶烟袅袅");
        STEM_CN.put("vinyl", "黑胶余温");
        STEM_CN.put("vinyl_crackle", "唱片细噪");
        STEM_CN.put("vinyl-effect", "黑胶余温");
        // 与项目 mp3 目录常见文件名一一对应（优先整词匹配，避免只命中 wind 等泛词）
        STEM_CN.put("bubbles", "水泡轻响");
        STEM_CN.put("jungle", "雨林深处");
        STEM_CN.put("keyboard", "键盘轻敲");
        STEM_CN.put("howling-wind", "狂风呼啸");
        STEM_CN.put("howling", "风啸长空");
        STEM_CN.put("rain-on-umbrella", "伞上细雨");
        STEM_CN.put("umbrella", "伞上细雨");
        STEM_CN.put("light-rain", "微雨轻落");
        STEM_CN.put("walk-on-gravel", "碎石步履");
        STEM_CN.put("gravel", "碎石轻踏");
        STEM_CN.put("wind-chimes", "风铃清音");
        STEM_CN.put("wind-in-trees", "林间风声");
        STEM_CN.put("trees", "林间叶响");
        // 单词碎片（用于组合）
        STEM_CN.put("light", "微光");
        STEM_CN.put("heavy", "沉厚");
        STEM_CN.put("softly", "轻软");
        STEM_CN.put("slow", "徐缓");
        STEM_CN.put("deep_breath", "深呼吸息");
    }

    private MeditationNoiseLabelMapper() {
    }

    /**
     * @param fileName 含 .mp3
     * @param indexOneBased 1 起，用于兜底序名
     */
    public static String map(String fileName, int indexOneBased) {
        if (fileName == null || fileName.isEmpty()) {
            return ordinalLabel(indexOneBased);
        }
        String lower = fileName.toLowerCase(Locale.ROOT);
        if (!lower.endsWith(".mp3")) {
            return ordinalLabel(indexOneBased);
        }
        String stem = lower.substring(0, lower.length() - 4);
        if (stem.isEmpty()) {
            return ordinalLabel(indexOneBased);
        }
        if (STEM_CN.containsKey(stem)) {
            return trimLabel(STEM_CN.get(stem));
        }
        String[] parts = STEM_SPLIT.split(stem);
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            String cn = STEM_CN.get(part);
            if (cn != null) {
                sb.append(cn);
            }
            if (sb.length() >= 4) {
                break;
            }
        }
        if (sb.length() >= 2) {
            return trimLabel(sb.toString());
        }
        return ordinalLabel(indexOneBased);
    }

    private static String trimLabel(String s) {
        if (s.length() <= 4) {
            return s;
        }
        return s.substring(0, 4);
    }

    private static String ordinalLabel(int indexOneBased) {
        if (indexOneBased < 1) {
            indexOneBased = 1;
        }
        if (indexOneBased < ORDINAL.length) {
            return "声" + ORDINAL[indexOneBased];
        }
        return "声景" + indexOneBased;
    }
}
