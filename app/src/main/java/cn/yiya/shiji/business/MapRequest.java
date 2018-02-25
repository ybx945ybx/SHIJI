package cn.yiya.shiji.business;

import android.text.TextUtils;

import java.util.HashMap;

import cn.yiya.shiji.entity.ParaObject;

/**
 * Created by chenjian on 2016/8/2.
 */
public class MapRequest {

    public static final int THREE = 3;
    public static final int FIVE = 5;
    public static final int SIX = 6;
    public static final int TEN = 10;
    public static final int TWENTY = 20;
    public static final int FORTY = 40;

    // 一次性获取三条数据
    public static HashMap<String, String> setMapThree(int offset) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(THREE));
        return maps;
    }

    // 一次性获取五条数据
    public static HashMap<String, String> setMapFive(int offset) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(FIVE));
        return maps;
    }

    // 一次性获取十条数据
    public static HashMap<String, String> setMapTen(int offset) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(TEN));
        return maps;
    }

    // 一次性获取二十条数据
    public static HashMap<String, String> setMapTwenty(int offset) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(TWENTY));
        return maps;
    }

    // 一次性获取四十条数据
    public static HashMap<String, String> setMapForty(int offset) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(FORTY));
        return maps;
    }

    public static HashMap<String, String> setTagId(int tagId, int offset) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("tag_id", String.valueOf(tagId));
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(TEN));
        return maps;
    }

    public static HashMap<String, String> setTagIdTwenty(int id, int offset) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("tag_id", String.valueOf(id));
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(TWENTY));
        return maps;
    }

    public static HashMap<String, String> setTagId(int tagId, int offset, int limit, int type) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("tag_id", String.valueOf(tagId));
        maps.put("type", String.valueOf(type));
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(limit));
        return maps;
    }

    public static HashMap<String, String> setTagId(int tagId, int offset, int type) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("tag_id", String.valueOf(tagId));
        maps.put("type", String.valueOf(type));
        maps.put("offset", String.valueOf(offset));
        return maps;
    }

    public static HashMap<String, String> setId(int id, int offset) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("id", String.valueOf(id));
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(FORTY));
        return maps;
    }

    public static HashMap<String, String> setOrderNo(String orderno, int offset) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("sub_order_num", orderno);
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(TWENTY));
        return maps;
    }

    // 获取某一城市下的店铺列表
    public static HashMap<String, String> setStoreListOfCity(int offset, String cityId, String categoryList, String longitude, String latitude, String countryId, int count) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(SIX));
        maps.put("id", cityId);
        maps.put("category_list", categoryList);
        maps.put("longitude", longitude);
        maps.put("latitude", latitude);
        maps.put("count", String.valueOf(count));
        return maps;
    }

    // 获取某一商场下的店铺列表
    public static HashMap<String, String> setStoreListOfMall(int offset, String mallId, String countryId, String cityId, String categoryList, int count) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(SIX));
        maps.put("id", mallId);
        maps.put("category_list", categoryList);
        maps.put("count", String.valueOf(count));
        return maps;
    }

    // 设置某一闪购活动的开抢提醒
    public static HashMap<String, String> setFlashNotice(String id, int notice) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("id", String.valueOf(id));
        maps.put("notice", String.valueOf(notice));
        return maps;
    }

    // 获取推荐搭配列表
    public static HashMap<String, String> setMapMatchRecommendList(int offset, int limit, String type) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(limit));
        maps.put("type", type);
        return maps;
    }

    // 某一状态订单列表
    public static HashMap<String, String> setAllOrderList(String status, int offset) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("status", status);
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(TEN));
        return maps;
    }

    // 热门单品
    public static HashMap<String, String> setHotSingleList(int brand_id, int limit) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("brand_id", String.valueOf(brand_id));
        maps.put("limit", String.valueOf(limit));
        return maps;
    }

    public static HashMap<String, String> setUserIdMap(String userId, int offset) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("user_id", userId);
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(TEN));
        return maps;
    }

    // 最新上架
    public static HashMap<String, String> setNewGoodsList(int brand_id, int offset) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("brand_id", String.valueOf(brand_id));
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(TEN));
        return maps;
    }

    public static HashMap<String, String> setMallListMap(int offset, ParaObject object) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("id", String.valueOf(object.getId()));
        maps.put("brand_ids", object.getBrand_ids());
        maps.put("sort_id", object.getSort_id());
        maps.put("genders", object.getGenders());
        maps.put("price_ranges_id", object.getPrice_ranges_id());
        maps.put("count", String.valueOf(object.getCount()));
        maps.put("category_ids", object.getCategory_ids());
        maps.put("size", object.getSize());
        maps.put("color", object.getColor());
        maps.put("offset", String.valueOf(offset));
        if (!TextUtils.isEmpty(object.getWord())) {
            maps.put("word", object.getWord());
        }
        maps.put("limit", String.valueOf(FORTY));
        return maps;
    }

    // 获取热门线路列表
    public static HashMap<String, String> setIdSix(String id, int offset) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("id", id);
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(SIX));
        return maps;
    }

    // 用户的公开作品列表(搭配、购物笔记)
    public static HashMap<String, String> setWorkListMap(int userId, int type, int offset) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("user_id", String.valueOf(userId));
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(TWENTY));
        maps.put("type", String.valueOf(type));
        return maps;
    }

    // 获取商品详情
    public static HashMap<String, String> setGoodsDetailMap(String goodsId) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("goodsId", String.valueOf(goodsId));
        maps.put("size", String.valueOf(1));
        return maps;
    }

    /**
     * 店主添加或删除推荐商品
     *
     * @param goodsId
     * @param status  1 添加   2 删除
     * @return
     */
    public static HashMap<String, String> setRecommendGoodsAddMap(String goodsId, int status) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("goods_id", String.valueOf(goodsId));
        maps.put("status", String.valueOf(status));
        return maps;
    }

    /**
     * 店主转发商品搜索
     */
    public static HashMap<String, String> setForwardSearchMap(String word, int offset) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("word", String.valueOf(word));
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(TWENTY));
        return maps;
    }
}
