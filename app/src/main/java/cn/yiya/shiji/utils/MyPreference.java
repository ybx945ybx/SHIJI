package cn.yiya.shiji.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Tom on 2016/3/9.
 */
public class MyPreference {
    private static SharedPreferences sp;
    public static String FIND_GUIDE = "find_guide";                            // 发现界面发布购物笔记引导
    public static String WORK_DETAIL_FOLLOW = "work_detail_follow";            // 笔记详情加关注引导
    public static String WORK_DETAIL_FOLLOW_TAGS = "work_detail_follow_tags";  // 笔记详情加关注和标签同时显示引导
    public static String WORK_DETAIL_LIKE = "work_detail_like";                // 笔记详情点赞
    public static String BANER_FILTER = "baner_filter";                        // 商品列表品牌筛选可多选引导
    public static String GOODS_DETAIL_CONSULT = "goods_detail_consult";        // 商品详情咨询客服引导
    public static String FREE_DETAIL = "free_detail";                          // 订单填写国际运费引导
    public static String TRAVEL_GUIDE = "travel_guide";                        // 导航首页引导
    public static String MAIN_RECOMMEND_FOLLOW_BRAND = "main_recommend_follow_brand";                        // 首页品牌关注按钮引导
    public static String MAIN_TRAVEL_GUIDE = "main_travel_guide";                                            // 首页导航按钮引导
    public static String MAIN_WORK_GUIDE = "main_work_guide";                                                // 发现相机按钮引导
    public static String WORK_DETAIL_GOODS_GUIDE = "work_detail_goods_guide";                                // 笔记详情跳转商品详情引导
    public static String COLLOCATION_GOODS_ORDER = "collocation_goods_order";                                // 搭配排序引导
    // 保存
    public static void saveSharedPreferences(Context context, String key, Boolean save) {
        sp = context.getSharedPreferences("yiyaGuide", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, save);
        editor.commit();// 保存新数据
    }

    // 取出
    public static boolean takeSharedPreferences(Context context, String key) {
        sp = context.getSharedPreferences("yiyaGuide", context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }
}
