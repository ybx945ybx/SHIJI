package cn.yiya.shiji.config;

import android.content.Context;

import com.google.android.gms.analytics.Tracker;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Tom on 2016/1/19.
 * 友盟、谷歌打点事件
 */
public class CustomEvent {
    public static final String WebUrlShare = "WebUrlShare";                     // 网页分享页面（发现里面的活动）
    public static final String EditeUserInfo = "EditeUserInfo";                 // 编辑用户信息
    public static final String ClickNotification = "ClickNotification";         // 用户点击了推送消息
    public static final String CheckUpdate ="CheckUpdate";                      // 用户点击了更新App
    public static final String Regist = "Regist";                               // 点击了注册
    public static final String Splash = "Splash";                               // App启动页面
    public static final String Login = "Login";                                 // 用户点击了登陆
    public static final String ToHomePage = "ToHomePage";                       // 从登陆进入到主页
    public static final String SwitchHomePage = "SwitchHomePage";               // 切换到底部导航栏--首页
    public static final String SwitchCategory = "SwitchCategory";               // 切换到底部导航栏--分类
    public static final String SwitchDiscover = "SwitchDiscover";               // 切换到底部导航栏--发现
    public static final String SwitchShoppingCart = "SwitchShoppingCart";       // 切换到底部导航栏--导航
    public static final String SwitchMine = "SwitchMine";                       // 切换到底部导航栏--我
    public static final String GoToPromotion = "GoToPromotion";                 // 发现-推荐-进入活动页面
    public static final String TopLevelCateGory="TopLevelCateGory";             // 进入了分类-顶部第一级分类
    public static final String PopularBrand = "PopularBrand";                   // 热门品牌进入到品牌详情界面
    public static final String AllBrands = "AllBrands";                         // 全部品牌
    public static final String SearchBrand = "SearchBrand";                     // 搜索品牌
    public static final String PopularMall = "PopularMall";                     // 热门商城
    public static final String AllMallList = "AllMallList";                     // 全部商城列表
    public static final String BrandsMallShow = "BrandsMallShow";               // 商城详细列表
    public static final String MallFilter = "MallFilter";                       // 进行筛选
    public static final String MallToDetail = "MallToDetail";                   // 商城列表进入到商品详情
    public static final String AddToCart = "AddToCart";                         // 加入购物车
    public static final String Checkout = "Checkout_Android";                   // 去结算(Android)
    public static final String SubmitOrder = "SubmitOrder_Android";             // 提交订单(Android)
    public static final String OrderSucess = "OrderSuccess_Android";            // 成功提交订单(Android)
    public static final String OrderCancel = "OrderCancel_Android";             // 取消订单(Android)
    public static final String PublishWork = "PublishWork";                     // 发表了购物笔记
    public static final String MallHomeToDetail = "MallHomeToDetail";           // 从首页进入了商品详细界面
    public static final String MallHomeToList = "MallHomeToList";               // 从首页进入了商品汇总界面
    public static final String MallHomeToSpecial = "MallHomeToSpecial";         // 从首页进入了活动专题页面
    public static final String MallHomeToFlashSale = "MallHomeToFlashSale";         // 从首页进入了闪购页面
    public static final String MallDetailToWorkDetail="MallDetailToWorkDetail"; // 从商品详细界面进入购物笔记详细界面
    public static final String HomeTabHome="HomeTabHome";                       // 底部首页中标签首页
    public static final String HomeTabClothes="HomeTabClothes";                 // 底部首页中标签服饰
    public static final String HomeTabShoes="HomeTabShoes";                     // 底部首页中标签鞋靴
    public static final String HomeTabLuggage="HomeTabLuggage";                 // 底部首页中标签箱包
    public static final String HomeTabGadgets="HomeTabGadgets";                 // 底部首页中标签配饰
    public static final String HomeTabBeauty="HomeTabBeauty";                   // 底部首页中标签美妆
    public static final String HomeTabBaby="HomeTabBaby";                       // 底部首页中标签母婴
    public static final String HomeTabHealth="HomeTabHealth";                   // 底部首页中标签保健品
    public static final String GoOrderDetailH5 = "GoOrderDetailH5";             // 进入订单详情页面 h5
    public static final String OrderDetailSubmitH5 = "OrderDetailSubmitH5";     // 订单详情页面提交了订单，h5
    public static final String OrderDetailSuccessH5 = "OrderDetailSuccessH5";   // 订单详情页面成功付款，h5
    public static final String OrderDetailCancelH5 = "OrderDetailCancelH5";     // 订单详情页面取消付款，h5
    public static final String SetFlashSaleNotice = "SetFlashSaleNotice";       // 闪购界面设置抢购提醒
    public static final String FlashSaleToIssue = "SetFlashSaleNotice";         // 闪购界面跳至活动页面
    public static final String HomeSearch = "HomeSearch";                       // 首页点击了搜索
    public static final String CategorySearch = "CategorySearch";               // 分类页面点击了搜索
    public static final String CleanSearchRecord = "CleanSearchRecord";         // 清空搜索历史记录
    public static final String HotSearch = "HotSearch";                         // 点击热门搜索
    public static final String RecordSearch = "RecordSearch";                   // 点击搜索记录


    public static void onEvent(Context context, Tracker tracker, String sreenName, String eventId, String label, Long value){
        tracker.enableAdvertisingIdCollection(true);
        GoogleAnalyticsEvent.gsScreenName(tracker, sreenName);
        GoogleAnalyticsEvent.onGsEvent(tracker, sreenName, eventId, label, value);
        MobclickAgent.onEvent(context, eventId);
    }

    public static void onEvent(Context context, Tracker tracker, String sreenName, String eventId, String label){
        tracker.enableAdvertisingIdCollection(true);
        GoogleAnalyticsEvent.gsScreenName(tracker,sreenName);
        GoogleAnalyticsEvent.onGsEvent(tracker, sreenName, eventId, label);
        MobclickAgent.onEvent(context, eventId);
    }

    public static void onEvent(Context context, Tracker tracker, String sreenName, String eventId){
        tracker.enableAdvertisingIdCollection(true);
        GoogleAnalyticsEvent.gsScreenName(tracker,sreenName);
        GoogleAnalyticsEvent.onGsEvent(tracker, sreenName, eventId);
        MobclickAgent.onEvent(context, eventId);
    }

}
