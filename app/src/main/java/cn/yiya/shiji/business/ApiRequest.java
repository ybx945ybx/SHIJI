package cn.yiya.shiji.business;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.ActivityListObject;
import cn.yiya.shiji.entity.AddressListItem;
import cn.yiya.shiji.entity.AddressListObject;
import cn.yiya.shiji.entity.AppRecommendObject;
import cn.yiya.shiji.entity.AppScreenImageEntity;
import cn.yiya.shiji.entity.BackgroundItem;
import cn.yiya.shiji.entity.BannerObject;
import cn.yiya.shiji.entity.BrandsItem;
import cn.yiya.shiji.entity.BrandsObject;
import cn.yiya.shiji.entity.BrandsRecInfo;
import cn.yiya.shiji.entity.BrandsSortObject;
import cn.yiya.shiji.entity.CMBPayInfo;
import cn.yiya.shiji.entity.CampaignDetailInfo;
import cn.yiya.shiji.entity.CarCountInfo;
import cn.yiya.shiji.entity.CashAccountExplain;
import cn.yiya.shiji.entity.CashAggregationEntity;
import cn.yiya.shiji.entity.CashEntity;
import cn.yiya.shiji.entity.CommentObject;
import cn.yiya.shiji.entity.CommonWebViewEntity;
import cn.yiya.shiji.entity.DaliyRecInfo;
import cn.yiya.shiji.entity.ExchangeCodeObject;
import cn.yiya.shiji.entity.FansObject;
import cn.yiya.shiji.entity.FlashActivityDetailInfo;
import cn.yiya.shiji.entity.GoodsDetailEntity;
import cn.yiya.shiji.entity.GoodsTranlateDesc;
import cn.yiya.shiji.entity.GoodsUpdateInfo;
import cn.yiya.shiji.entity.GoodsUpdateStatusInfo;
import cn.yiya.shiji.entity.HotBrandsObject;
import cn.yiya.shiji.entity.HotCategoryFirstSecondObject;
import cn.yiya.shiji.entity.HotCategoryObject;
import cn.yiya.shiji.entity.HotCategroyThridObject;
import cn.yiya.shiji.entity.HotClassObject;
import cn.yiya.shiji.entity.HotMallObject;
import cn.yiya.shiji.entity.HotMallSortObject;
import cn.yiya.shiji.entity.HtmlVersionInfo;
import cn.yiya.shiji.entity.ImageTag;
import cn.yiya.shiji.entity.IssueActivityDetailInfo;
import cn.yiya.shiji.entity.LayerItem;
import cn.yiya.shiji.entity.LikerObject;
import cn.yiya.shiji.entity.LogstashReport.CategoryBrandLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.CategoryLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.CategorySiteLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.MainBannerLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.MainMenuHotCategoryLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.MainMenuLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.MainMenuSpecialSaleLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.MainMenuThemeLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.MainMenuTopBannerLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.MainMenuTopClotheLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.MainMenuTopInLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.MainRecBrandGoodsLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.MainRecBrandLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.MatchGoodsLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.MatchLogstashItem;
import cn.yiya.shiji.entity.MainActImgInfo;
import cn.yiya.shiji.entity.MainBannerInfo;
import cn.yiya.shiji.entity.MainFlashSaleInfo;
import cn.yiya.shiji.entity.MallGoodsDetailObject;
import cn.yiya.shiji.entity.MallLimitOptionObject;
import cn.yiya.shiji.entity.MyCouponCountryList;
import cn.yiya.shiji.entity.NewGoodsObject;
import cn.yiya.shiji.entity.NotifyCommonListObject;
import cn.yiya.shiji.entity.NotifyCount;
import cn.yiya.shiji.entity.NotifyDetailCount;
import cn.yiya.shiji.entity.NotifyItem;
import cn.yiya.shiji.entity.NotifyListObject;
import cn.yiya.shiji.entity.OfficialTagsItem;
import cn.yiya.shiji.entity.OrderCashInfo;
import cn.yiya.shiji.entity.OrderDetailObject;
import cn.yiya.shiji.entity.OrderListObject;
import cn.yiya.shiji.entity.OrderNo;
import cn.yiya.shiji.entity.OutLineCountryObject;
import cn.yiya.shiji.entity.PayModeObject;
import cn.yiya.shiji.entity.PayOrderNoPost;
import cn.yiya.shiji.entity.PicDiaryUploadItem;
import cn.yiya.shiji.entity.RedPackageCountEntity;
import cn.yiya.shiji.entity.RedPackageObject;
import cn.yiya.shiji.entity.RegStatusInfo;
import cn.yiya.shiji.entity.RegisterInfo;
import cn.yiya.shiji.entity.RememberUserObject;
import cn.yiya.shiji.entity.ScoreObject;
import cn.yiya.shiji.entity.SearchHotWords;
import cn.yiya.shiji.entity.SearchPopularWords;
import cn.yiya.shiji.entity.ShareFinishEntity;
import cn.yiya.shiji.entity.ShareWxInfo;
import cn.yiya.shiji.entity.ShopCommissionInfo;
import cn.yiya.shiji.entity.ShopGoodsBrandGoodsObject;
import cn.yiya.shiji.entity.ShopTypeEntity;
import cn.yiya.shiji.entity.ShoppingCarSourceObject;
import cn.yiya.shiji.entity.ShoppingCartObject;
import cn.yiya.shiji.entity.ShortUrlEntity;
import cn.yiya.shiji.entity.SkusObject;
import cn.yiya.shiji.entity.StartImageObject;
import cn.yiya.shiji.entity.SystemSkinObject;
import cn.yiya.shiji.entity.TagObject;
import cn.yiya.shiji.entity.TagProduceObject;
import cn.yiya.shiji.entity.TagTypeInfo;
import cn.yiya.shiji.entity.ThematicInfo;
import cn.yiya.shiji.entity.TotalInfo;
import cn.yiya.shiji.entity.User;
import cn.yiya.shiji.entity.UserGrowFinishTaskEntity;
import cn.yiya.shiji.entity.UserGrowRankingEntity;
import cn.yiya.shiji.entity.UserGrowRuleEntity;
import cn.yiya.shiji.entity.UserGrowValueEntity;
import cn.yiya.shiji.entity.UserShopInfoEntity;
import cn.yiya.shiji.entity.VersionData;
import cn.yiya.shiji.entity.VisitGoodsEntity;
import cn.yiya.shiji.entity.WorkGoodsObject;
import cn.yiya.shiji.entity.WorkItem;
import cn.yiya.shiji.entity.WorkObject;
import cn.yiya.shiji.entity.navigation.CityInfo;
import cn.yiya.shiji.entity.navigation.CityListObject;
import cn.yiya.shiji.entity.navigation.CountryListInfo;
import cn.yiya.shiji.entity.navigation.CountryListObject;
import cn.yiya.shiji.entity.navigation.CouponDetailInfo;
import cn.yiya.shiji.entity.navigation.CouponDetailObject;
import cn.yiya.shiji.entity.navigation.HotLineObject;
import cn.yiya.shiji.entity.navigation.MallDetailInfo;
import cn.yiya.shiji.entity.navigation.MallDetailObject;
import cn.yiya.shiji.entity.navigation.NewsInfo;
import cn.yiya.shiji.entity.navigation.NewsObject;
import cn.yiya.shiji.entity.navigation.RecommendList;
import cn.yiya.shiji.entity.navigation.StoreCategoryObject;
import cn.yiya.shiji.entity.navigation.StoreObject;
import cn.yiya.shiji.entity.navigation.TaxAndInfos;
import cn.yiya.shiji.entity.search.NewSearchEntity;
import cn.yiya.shiji.utils.NetUtil;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by chenjian on 2016/7/26.
 * Retrofit的注解方法，和后台API管理
 */

public class ApiRequest {

    // 获取后台api方法
    public static ApiShiji getApiShiji() {
        return retrofit().create(ApiRequest.ApiShiji.class);
    }

    public interface ApiShiji {

        // 获取启动图以及广告图片
        @GET("/common/app-screen-image-info")
        Call<TotalInfo<StartImageObject>> getAppStartImage();

        // 获取app最新启动图
        @GET("/common/screen-image-info")
        Call<TotalInfo<AppScreenImageEntity>> getAppScreenImage();

        // 每日推荐列表 （首页）
        @GET("/shop/recommended-daily/list")
        Call<TotalInfo<DaliyRecInfo>> getDailyRec();

        // 获取购物车数量
        @GET("/shop/shopping-cart/count")
        Call<TotalInfo<CarCountInfo>> getCarCount();

        // 退出登陆
        @GET("/user/logout")
        Call<TotalInfo> setLoginOut();

        // 获取离线信息列表
        @GET("/navi/off-line/list")
        Call<TotalInfo<OutLineCountryObject>> getOutLineList();

        // 获取收件人列表
        @GET("/shop/consignee-address/list")
        Call<TotalInfo<AddressListObject>> addressList();

        // 获取App推荐列表（个人中心）
        @GET("/activity/app-recommend/app-list")
        Call<TotalInfo<AppRecommendObject>> getAppRecommend();

        // 获取首页活动图片
        @GET("/common/activity-image")
        Call<TotalInfo<MainActImgInfo>> getMainActImage();

        // 获取个人中心红包列表
        @GET("/shop/red-package/list-all")
        Call<TotalInfo<RedPackageObject>> getRedPackageAll();

        // 首页闪购
        @GET("/shop/flash-sale/list")
        Call<TotalInfo<MainFlashSaleInfo>> getMainFlashSale();

        // 轮播列表(推荐-购物笔记)
        @GET("/banner/list")
        Call<TotalInfo<BannerObject>> getBannerList();

        // 轮播列表(推荐-搭配)
        @GET("/banner/collocation-list")
        Call<TotalInfo<BannerObject>> getMatchBannerList();

        // 获取热门搜索
        @GET("/shop/search/hot-words")
        Call<TotalInfo<SearchHotWords>> getHotSearch();

        // 获取首页弹层图片
        @GET("/user/layer")
        Call<TotalInfo<LayerItem>> getPullLayer();

        // 获取新用户弹层图片
        @GET("/user/new-user-layer")
        Call<TotalInfo<LayerItem>> getNewUserPullLayer();

        // 获取用户消息数量
        @GET("/notify/count")
        Call<TotalInfo<NotifyCount>> getNotifyCount();

        // 获取商城列表数据
        @GET("/shop/site/list")
        Call<TotalInfo<HotMallSortObject>> getHotMallInfo();

        // 获取社区和物流详细个数
        @GET("/notify/detail-count")
        Call<TotalInfo<NotifyDetailCount>> getNotifyDetailCount();

        // 获取注册界面热门品类
        @GET("/shop/hot-category/reg-list")
        Call<TotalInfo<HotClassObject>> getCategoriesRegister();

        // 获取注册页面热门品牌
        @GET("/shop/brand/reg-char-list")
        Call<TotalInfo<HotBrandsObject>> getBrandsRegister();

        // 获取导航的banner信息
        @GET("/banner/navi-list")
        Call<TotalInfo<BannerObject>> getNaviBannerList();

        // 获取收藏的coupon列表
        @GET("/navi/coupon/list-ofuser")
        Call<TotalInfo<MyCouponCountryList>> getCounponList();

        // 获取当前图标皮肤
        @GET("/common/system-skin")
        Call<TotalInfo<SystemSkinObject>> getSkin();

        // 获取菜单列表
        @GET("/shop/menu/list")
        Call<TotalInfo<HotCategoryObject>> getMenuList();

        // 现金账户总计
        @GET("/shop/cash-acount/cash-aggregation")
        Call<TotalInfo<CashAggregationEntity>> getCashAggregation();

        // 获取用户收藏列表
        @GET("/shop/goods/follow-goods-list")
        Call<TotalInfo<MallGoodsDetailObject>> getWishGoods();

        // 获取店铺类型
        @GET("/shop/personal-shop/shop-type")
        Call<TotalInfo<ShopTypeEntity>> getShopType();

        // 获取底部按钮的文字及链接
        @GET("/common/web-view")
        Call<TotalInfo<CommonWebViewEntity>> getCommonWebView();

        // 获取邀请开店页面信息
        @GET("/shop/personal-shop/commission-info")
        Call<TotalInfo<ShopCommissionInfo>> getShopCommissionInfo();

        // 获取现金账户说明
        @GET("/shop/cash-acount/explain-info")
        Call<TotalInfo<CashAccountExplain>> getCashAccountExplainInfo();

        // 获取个人店铺信息
        @GET("/shop/personal-shop/user-shop-info")
        Call<TotalInfo<UserShopInfoEntity>> getUserShopInfo();

		// 精选专题推荐
        @GET("/shop/shop-activity/shopkeeper-activity")
        Call<TotalInfo<ActivityListObject>> getShopRecommendActivity();

        //热门推荐
        @GET("/shop/top-today/list")
        Call<TotalInfo<MallGoodsDetailObject>> getShopTopList();

        // 精选商品推荐
        @GET("/shop/shopkeeper-recommend/list")
        Call<TotalInfo<MallGoodsDetailObject>> getShopkeeperRecommend();

        // 用户成长规则
        @GET("/user-grow/rule")
        Call<TotalInfo<UserGrowRuleEntity>> getUserGrowRule();

        // 用户已完成任务
        @GET("/user-grow/finish-task")
        Call<TotalInfo<UserGrowFinishTaskEntity>> getUserGrowFinishTask();

        // 用户成长排行榜
        @GET("/user-grow/ranking")
        Call<TotalInfo<UserGrowRankingEntity>> getUserGrowRanking();

        // 用户成长总成长值
        @GET("/user-grow/grow-value")
        Call<TotalInfo<UserGrowValueEntity>> getUserGrowValue();

        // 获取最新的搜索数据
        @GET("/shop/search/search-datav2")
        Call<TotalInfo<SearchPopularWords>> getPopularData(@Query("stamp") String stamp);

        // 获取搜索辅助梯度
        @GET("/shop/search/search-auxiliary")
        Call<TotalInfo<NewSearchEntity>> getSearchAuxiliary(@Query("name") String name);

        // 获取他人用户详细信息
        @GET("/user/sns-info")
        Call<TotalInfo<User>> getUserSNSDetail(@Query("user_id") String userId);

        // 获取coupon信息 需判断网络
        @GET("/navi/coupon/detail-info")
        Call<TotalInfo<CouponDetailInfo>> getCouponDetail(@Query("id") String id);

        // 关注用户
        @GET("/user/follow-user")
        Call<TotalInfo> setFollow(@Query("user_id") String userId);

        // 取消对用户关注
        @GET("/user/unfollow-user")
        Call<TotalInfo> setUnFollow(@Query("user_id") String userId);

        // 获取App版本信息
        @GET("/common/app-version-info")
        Call<TotalInfo<VersionData>> getVersion(@Query("code") String code);

        // 获取某一城市下店铺的品牌与分类列表
        @GET("/navi/store/brands-categorys")
        Call<TotalInfo<StoreCategoryObject>> getStoreBrandsCategoryOfCity(@Query("id") String id);

        // 获取搭配详情
        @GET("/work/detail")
        Call<TotalInfo<WorkItem>> getMatchDetails(@Query("work_id") String workId);

        // 兑换优惠码
        @GET("/shop/exchange-code/exchange")
        Call<TotalInfo<NotifyItem>> exchangeCode(@Query("code") String code);

        // 获取活动标签详情
        @GET("/campaign/campaign-detail")
        Call<TotalInfo<CampaignDetailInfo>> getActTagDetail(@Query("tag_id") String tagId);

        // 根据Tag获取品牌
        @GET("/shop/brand/tag-brand")
        Call<TotalInfo<TagTypeInfo>> getTagBrand(@Query("tagId") String tagId);

        // 商城轮播图（首页、服饰等）
        @GET("/shop/menu/menu-activity-thematic-chain")
        Call<TotalInfo<MainBannerInfo>> getMainBanner(@Query("id") String id);

        // 获取专题列表（首页）
        @GET("/shop/thematic/thematic-list")
        Call<TotalInfo<ThematicInfo>> getMainThematicList(@Query("type") String type);

        // 订单商品加回购物车
        @GET("/shop/order/to-cart")
        Call<TotalInfo> gobackShoppingCar(@Query("orderNumber") String orderNumber);

        // 获取某一品牌下过滤选项
        @GET("/shop/goods/brand-option")
        Call<TotalInfo<MallLimitOptionObject>> getShopGoodsBrandOption(@Query("id") String id);

        // 获取某一品牌下过滤选项
        @GET("/shop/goods/brand-option")
        Call<TotalInfo<MallLimitOptionObject>> getShopGoodsBrandOption(@QueryMap Map<String, String> maps);

        // 某一品牌活动列表
        @GET("/shop/brand/activity-list")
        Call<TotalInfo<ActivityListObject>> getActivityList(@Query("id") String id);

        // 搜索官方标签
        @GET("/tag/search")
        Call<TotalInfo<OfficialTagsItem>> getOfficialTagsList(@Query("tag") String tag);

        // 获取一二级热门分类
        @GET("/shop/hot-category/first-second")
        Call<TotalInfo<HotCategoryFirstSecondObject>> getHotCategoryFirstSecond(@Query("version") String version);

        // 获取三级热门分类
        @GET("/shop/hot-category/third-list")
        Call<TotalInfo<HotCategroyThridObject>> getHotCategoryThrid(@Query("id") String id);

        // 获取二三级热门分类
        @GET("/shop/hot-category/second-third")
        Call<TotalInfo<HotCategoryObject>> getHotCategorySecondThird(@Query("id") String id);

        // 获取一级热门分类
        @GET("/shop/hot-category/top-list")
        Call<TotalInfo<HotCategoryObject>> getHotCategoryFirst(@Query("version") String version);

        // 获取搜索页面热门品牌
        @GET("/shop/brand/hot-brand")
        Call<TotalInfo<BrandsObject>> getHotBrandsSearch(@Query("limit") String limit);

        // 搜索页面热门商城
        @GET("/shop/site/hot-list")
        Call<TotalInfo<HotMallObject>> getHotMallsSearch(@Query("limit") String limit);

        // 搜索之后的商品筛选中品牌列表
        @GET("/shop/goods/search-brand")
        Call<TotalInfo<MallLimitOptionObject>> getSearchBrandOption(@Query("word") String word);

        // 搜索之后商品的过滤选项
        @GET("/shop/goods/search-option")
        Call<TotalInfo<MallLimitOptionObject>> getSearchOption(@QueryMap Map<String, String> maps);

        // 获取菜单下某一分类商品过滤选项  从首页进入
        @GET("/shop/goods/menu-category-option")
        Call<TotalInfo<MallLimitOptionObject>> getMenuCategoryOption(@QueryMap Map<String, String> maps);

        // 获取菜单下某一分类商品品牌列表  从首页进入
        @GET("/shop/goods/menu-category-brand")
        Call<TotalInfo<MallLimitOptionObject>> getMenuCategoryBrandOption(@Query("id") String id);

        // 获取某一类型商品过滤选项 从分类的二级菜单进入
        @GET("/shop/goods/category-option")
        Call<TotalInfo<MallLimitOptionObject>> getCategoryOption(@QueryMap Map<String, String> maps);

        // 获取某一类型商品的品牌列表 从分类的二级菜单进入
        @GET("/shop/goods/category-brand")
        Call<TotalInfo<MallLimitOptionObject>> getCategoryBrandOption(@Query("id") String id);

        // 获取某一网站商品过滤选项
        @GET("/shop/goods/site-option")
        Call<TotalInfo<MallLimitOptionObject>> getSiteOption(@QueryMap Map<String, String> maps);

        // 获取某一网站商品品牌列表
        @GET("/shop/goods/site-brand")
        Call<TotalInfo<MallLimitOptionObject>> getSiteBrandOption(@Query("id") String id);

        // 获取某一闪购详情
        @GET("/shop/flash-sale/detail")
        Call<TotalInfo<FlashActivityDetailInfo>> getFlashDetail(@Query("id") String id);

        // 获取用户积分列表
        @GET("/score/history-list")
        Call<TotalInfo<ScoreObject>> getScore(@Query("offset") String offset);

        // 获取某一活动详情
        @GET("/shop/shop-activity/detail")
        Call<TotalInfo<IssueActivityDetailInfo>> getActDetail(@Query("id") String id);

        // 获取某菜单下活动列表
        @GET("/shop/menu/menu-activity")
        Call<TotalInfo<NewGoodsObject>> getMenuList(@Query("id") String id);

        // 获取单个店铺下的推荐信息
        @GET("/navi/store/recommend-info")
        Call<TotalInfo<RecommendList>> getStoreRecommendInfo(@Query("id") String id);

        // 获取单个商场下的推荐信息
        @GET("/navi/mall/recommend-info")
        Call<TotalInfo<RecommendList>> getMallRecommendInfo(@Query("id") String id);

        // 获取其他支付方式列表包
        @GET("/pay/other-list")
        Call<TotalInfo<PayModeObject>> getPayModeList(@Query("order_no") String order_no);

        // 获取专题列表
        @GET("/shop/thematic/thematic-activity")
        Call<TotalInfo<NewGoodsObject>> getThematicAct(@Query("id") String id);

        // 国家详细信息接口 需判断网络
        @GET("/navi/country/detail-info")
        Call<TotalInfo<CountryListInfo>> getCountryDetailInfo(@Query("id") String id);

        // 获取单个国家简要信息 需要判断网络
        @GET("/navi/country/brief-info")
        Call<TotalInfo<CountryListInfo>> getCountryBriefInfo(@Query("id") String id);

        // 城市详细信息接口 需判断网络
        @GET("/navi/city/detail-info")
        Call<TotalInfo<CityInfo>> getCityDetailInfo(@Query("id") String id);

        // 获取单个国家退税信息 需判断网络
        @GET("/navi/country/tax-rebate-info")
        Call<TotalInfo<TaxAndInfos>> getTaxRebateOfCountry(@Query("id") String id);

        // 城市简要信息 需判断网络
        @GET("/navi/city/brief-info")
        Call<TotalInfo<CityInfo>> getCityBriefInfo(@Query("id") String id);

        // 获取单个商城详细信息 需判断网络
        @GET("/navi/mall/detail-info")
        Call<TotalInfo<MallDetailInfo>> getMallDetailInfo(@Query("id") String id);

        // 获取单个店铺详细信息 需判断网络
        @GET("/navi/store/detail-info")
        Call<TotalInfo<MallDetailInfo>> getStoreDetailInfo(@Query("id") String id);

        // 获取品牌详情头部基本信息
        @GET("/shop/brand/detail")
        Call<TotalInfo<BackgroundItem>> getBrandsHeaderDetail(@Query("brand_id") String brand_id);

        // 获取单个资讯的详细信息
        @GET("/navi/news/detail-info")
        Call<TotalInfo<NewsInfo>> getNewsInfo(@Query("id") String id);

        // 获取HTML版本信息
        @GET("/common/hybrid-html-info")
        Call<TotalInfo<HtmlVersionInfo>> getHtmlFileInfo(@Query("code") String code);

        // 获取某一件商品概要信息
        @GET("/shop/goods/goods-profile")
        Call<TotalInfo<WorkGoodsObject>> getGoodsProfileList(@Query("goodsId") String goodsId);

        // 通过ID获取某一活动详情
        @GET("/shop/shop-activity/detail")
        Call<TotalInfo<IssueActivityDetailInfo>> getIssueActivityDetail(@Query("id") String id);

        // 获取某一菜单下分类列表
        @GET("/shop/menu/category-list")
        Call<TotalInfo<HotCategoryObject>> getMenuCategoryList(@Query("id") String id);

        // 获取菜单下某一专题信息
        @GET("/shop/menu/menu-thematic")
        Call<TotalInfo<ThematicInfo>> getMenuThematic(@Query("id") String id);

        // 获取某一菜单下Top10品牌
        @GET("/shop/menu/menu-brand-rank")
        Call<TotalInfo<BrandsObject>> getMenuBrandRank(@Query("id") String id);

        // 获取某一菜单下促销专题
        @GET("/shop/menu/menu-promotion-thematic")
        Call<TotalInfo<MainBannerInfo>> getMenuPromotion(@Query("id") String id);

        // 获取某一菜单下今日特卖商品
        @GET("/shop/today-special/list")
        Call<TotalInfo<NewGoodsObject>> getMenuTodaySpecial(@Query("id") String id);

        // 获取某一菜单下商品排行榜
        @GET("/shop/top-today/list")
        Call<TotalInfo<NewGoodsObject>> getMenuTopToday(@Query("id") String id);

        // 获取某一活动下商品的品牌详情
        @GET("/shop/shop-activity/brand-detail")
        Call<TotalInfo<BrandsItem>> getBrandInfo(@Query("id") String id);

        // 获取商品是否需要更新
        @GET("/shop/goods/check-update")
        Call<TotalInfo<GoodsUpdateInfo>> checkGoodsUpDate(@Query("goodsId") String goodsId);

        // 获取商品更新状态
        @GET("/shop/goods/update-status")
        Call<TotalInfo<GoodsUpdateStatusInfo>> goodsUpDateStatus(@Query("goodsId") String goodsId);

        // 获取用户浏览商品
        @GET("/shop/goods/visit-goods")
        Call<TotalInfo<VisitGoodsEntity>> getVisitGoods(@Query("goods_id") String goodsId);

        // 获取用户浏览商品   不上传goodsId则不显示当前浏览的商品
        @GET("/shop/goods/visit-goods")
        Call<TotalInfo<VisitGoodsEntity>> getVisitGoods();

        // 获取商品SKUS
        @GET("/shop/goods/goods-skus")
        Call<TotalInfo<JsonObject>> getGoodsSkus(@Query("goodsId") String goodsId);

        // google翻译商品描述
        @GET("/shop/goods/translate-desc")
        Call<TotalInfo<GoodsTranlateDesc>> getGoodsTranslateDesc(@Query("goodsId") String goodsId);

        // 店主转发商品
        @GET("/shop/shopkeeper-forward/forward-goods")
        Call<TotalInfo> forwardGoods(@Query("goods_id") String goodsId);

        // 获取短链接
        @GET("/weixin/common/short-url")
        Call<TotalInfo<ShortUrlEntity>> getShortUrl(@Query("long_url") String longUrl);

        // 获取商品筛选条件（从品牌进入）
        @GET("/shop/goods/brand-goods")
        Call<TotalInfo<ShopGoodsBrandGoodsObject>> getShopGoodsBrandGoods(@QueryMap Map<String, String> maps);

        // 分享红包
        @GET("/shop/red-package/share-red-package-url")
        Call<TotalInfo<ShareWxInfo>> getRedPackageInfo(@QueryMap Map<String, String> maps);

        // 点赞用户笔记
        @GET("/work/like-user-list")
        Call<TotalInfo<LikerObject>> likedUser(@QueryMap Map<String, String> maps);

        // 获取某一店铺附近的店铺列表
        @GET("/navi/store/list-ofaround")
        Call<TotalInfo<StoreObject>> getStoreListOfAround(@QueryMap Map<String, String> maps);

        // 获取某一城市的资讯信息列表
        @GET("/navi/news/list-ofcity")
        Call<TotalInfo<NewsObject>> getNewsListOfCity(@QueryMap Map<String, String> maps);

        // 获取某一国家的资讯信息列表
        @GET("/navi/news/list-ofcountry")
        Call<TotalInfo<NewsObject>> getNewsOfCountry(@QueryMap Map<String, String> maps);

        // 获取某一国家下的城市列表 需判断网络
        @GET("/navi/city/list")
        Call<TotalInfo<CityListObject>> getCityList(@QueryMap Map<String, String> maps);

        // 获取单个国家的必买推荐信息 需判断网络
        @GET("/navi/country/recommend-info")
        Call<TotalInfo<RecommendList>> getCountryRecommend(@QueryMap Map<String, String> maps);

        // 获取某一城市下的coupon列表 需判断网络
        @GET("/navi/coupon/list-ofcity")
        Call<TotalInfo<CouponDetailObject>> getCouponListOfCity(@QueryMap Map<String, String> maps);

        // 获取某一城市下的商场列表 需判断网络
        @GET("/navi/mall/list-ofcity")
        Call<TotalInfo<MallDetailObject>> getMallListOfCity(@QueryMap Map<String, String> maps);

        // 获取某一商场下的coupon列表 需判断网络
        @GET("/navi/coupon/list-ofmall")
        Call<TotalInfo<CouponDetailObject>> getCouponListOfMall(@QueryMap Map<String, String> maps);

        // 获取某一店铺下的coupon列表 需判断网络
        @GET("/navi/coupon/list-ofstore")
        Call<TotalInfo<CouponDetailObject>> getCouponListOfStore(@QueryMap Map<String, String> maps);

        // 获取某一商场下店铺的品牌与分类列表
        @GET("/navi/store/brands-categorys")
        Call<TotalInfo<StoreCategoryObject>> getStoreBrandsCategoryOfMall(@QueryMap Map<String, String> maps);

        // 获取某一店铺下的分店列表
        @GET("/navi/store/list-ofsame")
        Call<TotalInfo<StoreObject>> getStoreListOfSame(@QueryMap Map<String, String> maps);

        // 用户关注列表
        @GET("/user/follow-list")
        Call<TotalInfo<FansObject>> userFollowing(@QueryMap Map<String, String> maps);

        // 获取笔记评论列表
        @GET("/comment/list")
        Call<TotalInfo<CommentObject>> getWorkCommentList(@QueryMap Map<String, String> maps);

        // 用户所有标签列表
        @GET("/tag/get-user-tags")
        Call<TotalInfo<TagObject>> userTagList(@QueryMap Map<String, String> maps);

        // 用户某一标签下的作品列表接口
        @GET("/work/user-tag-works")
        Call<TotalInfo<WorkObject>> userTagWorks(@QueryMap Map<String, String> maps);

        // 用户粉丝列表
        @GET("/user/fans-list")
        Call<TotalInfo<FansObject>> userFans(@QueryMap Map<String, String> maps);

        // 热门品牌排序列表
        @GET("/shop/brand/char-list")
        Call<TotalInfo<BrandsSortObject>> getBrandsSortList(@QueryMap Map<String, String> maps);

        // 获取用户消息列表
        @GET("/notify/list")
        Call<TotalInfo<NotifyListObject>> getNotifyList(@QueryMap Map<String, String> maps);

        // 获取用户公共消息列表
        @GET("/notify/common-list")
        Call<TotalInfo<NotifyCommonListObject>> getNotifyCommonList(@QueryMap Map<String, String> maps);

        // 获取用户社区消息列表
        @GET("/notify/community-list")
        Call<TotalInfo<NotifyListObject>> getNotifyCommunityList(@QueryMap Map<String, String> maps);

        // 获取某一闪购商品列表
        @GET("/shop/flash-sale/goodses-list")
        Call<TotalInfo<MallGoodsDetailObject>> getFlashSaleList(@QueryMap Map<String, String> maps);

        // 获取某一活动商品列表
        @GET("/shop/shop-activity/goodses-list")
        Call<TotalInfo<MallGoodsDetailObject>> getActList(@QueryMap Map<String, String> maps);

        // 兑换记录
        @GET("/shop/exchange-code/exchange-list")
        Call<TotalInfo<ExchangeCodeObject>> exchangeList(@QueryMap Map<String, String> maps);

        // 发现同龄好友获取列表
        @GET("/user/age-recommend-list")
        Call<TotalInfo<RememberUserObject>> getRecFriends(@QueryMap Map<String, String> maps);

        // 发现好友列表
        @GET("/user/recommend-list")
        Call<TotalInfo<RememberUserObject>> getUserFriends(@QueryMap Map<String, String> maps);

        // 订阅品牌推荐列表（首页）
        @GET("/shop/brand-recommend/list")
        Call<TotalInfo<BrandsRecInfo>> getBrandsRec(@QueryMap Map<String, String> maps);

        // 获取用户购买的商品列表
        @GET("/shop/order/order-goodses")
        Call<TotalInfo<NewGoodsObject>> getGoodsBoughtList(@QueryMap Map<String, String> maps);

        // 获取某一城市下的店铺列表 需判断网络
        @GET("/navi/store/list-ofcity")
        Call<TotalInfo<StoreObject>> getStoreListOfCity(@QueryMap Map<String, String> maps);

        // 获取某一商场下的店铺列表 需判断网络
        @GET("/navi/store/list-ofmall")
        Call<TotalInfo<StoreObject>> getStoreListOfMall(@QueryMap Map<String, String> maps);

        // 获取推荐列表(搭配、购物笔记)
        @GET("/work/recommend-list")
        Call<TotalInfo<WorkObject>> getRecommendList(@QueryMap Map<String, String> maps);

        // 获取关注列表(搭配、购物笔记)
        @GET("/work/follow-list")
        Call<TotalInfo<WorkObject>> getFollowList(@QueryMap Map<String, String> maps);

        // 某一状态订单列表
        @GET("/shop/order/order-list")
        Call<TotalInfo<OrderListObject>> getAllOrderList(@QueryMap Map<String, String> maps);

        //获取店铺订单
        @GET("/shop/order/shop-order-list")
        Call<TotalInfo<OrderListObject>> getStoreOrderList(@QueryMap Map<String, String> maps);

        // 根据订单号获取订单详情
        @GET("/shop/order/detail")
        Call<TotalInfo<OrderDetailObject>> getOrderDetail(@Query("orderNumber") String orderNumber);

        //获取店铺单个订单佣金明细
        @GET("/shop/order/one-shop-order-cash-info")
        Call<TotalInfo<OrderCashInfo>> getStoreOrderCashInfo(@Query("order_number") String orderNumber);

        // 热门单品
        @GET("/shop/brand/hot-goods")
        Call<TotalInfo<NewGoodsObject>> getHotSingleList(@QueryMap Map<String, String> maps);

        // 最新上架
        @GET("/shop/brand/new-goods")
        Call<TotalInfo<NewGoodsObject>> getNewGoodsList(@QueryMap Map<String, String> maps);

        // 获取最低价商品
        @GET("/shop/brand/cheap-goods")
        Call<TotalInfo<NewGoodsObject>> getCheapGoods(@QueryMap Map<String, String> maps);

        // 获取最低折扣商品
        @GET("/shop/brand/discount-goods")
        Call<TotalInfo<NewGoodsObject>> getDiscountGoods(@QueryMap Map<String, String> maps);

        // 某一标签下的推荐 搭配列表
        @GET("/work/tag-star-works")
        Call<TotalInfo<TagProduceObject>> tagStar(@QueryMap Map<String, String> maps);

        // 从首页进入商城列表获取数据
        @GET("/shop/goods/menu-category-goods")
        Call<TotalInfo<MallGoodsDetailObject>> getMallHomeList(@QueryMap Map<String, String> maps);

        // 从二级分类进入商城列表获取数据
        @GET("/shop/goods/category-goods")
        Call<TotalInfo<MallGoodsDetailObject>> getCategoryList(@QueryMap Map<String, String> maps);

        // 从热门商城进入商城列表获取数据
        @GET("/shop/goods/site-goods")
        Call<TotalInfo<MallGoodsDetailObject>> getHotMallList(@QueryMap Map<String, String> maps);

        // 上传七牛token
        @GET("/qiniu/upload-token")
        Call<TotalInfo> getQiNiuToken(@QueryMap Map<String, String> maps);

        // 获取搜索之后的商品列表
        @GET("/shop/goods/search-goods")
        Call<TotalInfo<MallGoodsDetailObject>> getSearchGoods(@QueryMap Map<String, String> maps);

        // 获取某一商品下的推荐列表
        @GET("/shop/goods/recommend-goods")
        Call<TotalInfo<MallGoodsDetailObject>> getNearGoodesList(@QueryMap Map<String, String> maps);

        // 获取热门品牌列表
        @GET("/shop/brand/hot-list")
        Call<TotalInfo<HotBrandsObject>> getHotBrandsList(@QueryMap Map<String, String> maps);

        // 获取国家列表 需判断网络
        @GET("/navi/country/list")
        Call<TotalInfo<CountryListObject>> getCountryList(@QueryMap Map<String, String> maps);

        // 获取热门线路列表
        @GET("/navi/shop-line/list")
        Call<TotalInfo<HotLineObject>> getShopLineList(@QueryMap Map<String, String> maps);

        // 用户的公开作品列表(搭配、购物笔记)
        @GET("/work/list")
        Call<TotalInfo<WorkObject>> getWorkList(@QueryMap Map<String, String> maps);

        // 用户点赞的公开作品列表(搭配、购物笔记)
        @GET("/work/like-work-list")
        Call<TotalInfo<WorkObject>> getLikeWorkList(@QueryMap Map<String, String> maps);

        // 获取使用过得商品
        @GET("/work/used-goodses")
        Call<TotalInfo<MallGoodsDetailObject>> getUsedGoods(@QueryMap Map<String, String> maps);

        // 更改店铺信息
        @GET("/shop/personal-shop/update-shop-info")
        Call<TotalInfo> upDateShopInfo(@QueryMap Map<String, String> maps);

        // 现金账户列表
        @GET("/shop/cash-acount/cash-order-detail")
        Call<TotalInfo<CashEntity>> getCashOrder(@QueryMap Map<String, String> maps);

        // 获取某一商品详情
        @GET("/shop/goods/goods-detail")
        Call<TotalInfo<GoodsDetailEntity>> getGoodsDetail(@QueryMap Map<String, String> maps);

        // 获取某一商品详情
        @GET("/shop/goods/goods-detail")
        Call<TotalInfo<JsonObject>> getGoodsDetailJson(@QueryMap Map<String, String> maps);

        // 获取店主转发商品列表
        @GET("/shop/shopkeeper-forward/forward-goods-list")
        Call<TotalInfo<MallGoodsDetailObject>> getForwardGoods(@QueryMap Map<String, String> maps);

        // 获取店主推荐商品列表
        @GET("/shop/shopkeeper-recommend/recommend-goods-list")
        Call<TotalInfo<MallGoodsDetailObject>> getRecommendGoods(@QueryMap Map<String, String> maps);

        // 店主添加或删除推荐商品
        @GET("/shop/shopkeeper-recommend/edit-goods")
        Call<TotalInfo> getRecommendGoodsEdit(@QueryMap Map<String, String> maps);

        // 店主转发商品搜索
        @GET("/shop/shopkeeper-forward/search-goods")
        Call<TotalInfo<MallGoodsDetailObject>> getForwardSearchGoods(@QueryMap Map<String, String> maps);

        // 获取用户基本信息
        @POST("/user/get-info")
        Call<TotalInfo<User>> getUsrInfo();

        // 获取当前用户详细信息
        @GET("/user/info")
        Call<TotalInfo<User>> getUserDetail();

        // 检查是否登陆
        @POST("/user/islogin")
        Call<TotalInfo> isLogin();

        // 跳过注册关注
        @POST("/user/skip-reg-follow")
        Call<TotalInfo> postRegisterFolloeSkip();

        // 删除作品
        @POST("/work/delete")
        @FormUrlEncoded
        Call<TotalInfo> deleteWork(@Field("work_id") int work_id);

        // 搜索 标签列表
        @POST("/work/get-sys-image-tag")
        @FormUrlEncoded
        Call<TotalInfo<ArrayList<ImageTag>>> getImageTagsByType(@Field("type") String type);

        // 更改用户头像
        @POST("/user/change-head")
        @FormUrlEncoded
        Call<TotalInfo> changeHead(@Field("head") String head);

        // 更改用户头像
        @POST("/user/red-info-chg")
        @FormUrlEncoded
        Call<TotalInfo> changeRedInfo(@Field("desc") String desc);


        // 删除某个收件人
        @POST("/shop/consignee-address/delete")
        @FormUrlEncoded
        Call<TotalInfo> deleteAdress(@Field("id") String id);

        // 删除购物车中商品
        @POST("/shop/shopping-cart/delete")
        @FormUrlEncoded
        Call<TotalInfo> deleteShoppingCart(@Field("ids") String cart_ids);

        // 收藏商品
        @POST("/shop/goods/follow-goods")
        @FormUrlEncoded
        Call<TotalInfo> followGoods(@Field("goods_id") String goods_id);

        // 取消收藏
        @POST("/shop/goods/unfollow-goods")
        @FormUrlEncoded
        Call<TotalInfo> deleteWishGoods(@Field("goods_id") String goods_id);

        // 删除某个物流或者社区消息
        @POST("/notify/delete")
        @FormUrlEncoded
        Call<TotalInfo> deleteNotify(@Field("id") String id);

        // 批量关注品牌
        @POST("/user/follow-tags")
        @FormUrlEncoded
        Call<TotalInfo> postFollowBrands(@Field("tag_list") String tag_list);

        // 取消批量关注品牌
        @POST("/user/unfollow-tags")
        @FormUrlEncoded
        Call<TotalInfo> postUnFollowBrands(@Field("tag_list") String tag_list);

        // 批量关注品类
        @POST("/user/follow-categorys")
        @FormUrlEncoded
        Call<TotalInfo> postFollowCategorys(@Field("category_list") String category_list);

        // 取消批量关注品类
        @POST("/user/unfollow-categorys")
        @FormUrlEncoded
        Call<TotalInfo> postUnFollowCategorys(@Field("category_list") String category_list);

        // 点赞
        @POST("/work/like")
        @FormUrlEncoded
        Call<TotalInfo> setWorkLike(@Field("work_id") String workId);

        // 开店提交获取订单号
        @POST("/shop/personal-shop/create-personal-shop")
        @FormUrlEncoded
        Call<TotalInfo<OrderNo>> openShop(@Field("type_id") String typeId);

        // 添加评论
        @POST("/comment/add")
        @FormUrlEncoded
        Call<TotalInfo> addComment(@FieldMap Map<String, String> maps);

        // 获取验证码
        @POST("/sms/verifycode")
        @FormUrlEncoded
        Call<TotalInfo<RegisterInfo>> getVerifyCode(@FieldMap Map<String, String> maps);

        // device token修改
        @POST("/user/device-token")
        @FormUrlEncoded
        Call<TotalInfo> postDeviceToken(@FieldMap Map<String, String> maps);

        // 批量关注品牌品类
        @POST("/user/follow-brands-categorys")
        @FormUrlEncoded
        Call<TotalInfo> postFolloeBrandsCategorise(@FieldMap Map<String, String> maps);

        // 微信登陆
        @POST("/weixin/common/app-login")
        @FormUrlEncoded
        Call<TotalInfo<RegStatusInfo>> loginWexin(@FieldMap Map<String, String> maps);

        // 用户注册
        @POST("/user/register")
        @FormUrlEncoded
        Call<TotalInfo> register(@FieldMap Map<String, String> maps);

        // 设置某一闪购活动的开抢提醒（首页）
        @POST("/shop/flash-sale/set-notice")
        @FormUrlEncoded
        Call<TotalInfo> setFlashSaleNotice(@FieldMap Map<String, String> maps);

        // 招行支付
        @POST("/cmb-pay/create-charge")
        @FormUrlEncoded
        Call<TotalInfo<CMBPayInfo>> getCMBPay(@Field("order_no") String no);

        // 修改用户名
        @POST("/user/change-name")
        @FormUrlEncoded
        Call<TotalInfo> changeName(@Field("name") String name);

        // 收藏某个coupon
        @POST("/navi/coupon/collect")
        @FormUrlEncoded
        Call<TotalInfo> storeCoupon(@Field("id") String id);

        // 上传个推CID
        @POST("/user/update-login")
        @FormUrlEncoded
        Call<TotalInfo> uploadCID(@Field("cid") String cid);

        // 用户登陆
        @POST("/user/login")
        @FormUrlEncoded
        Call<TotalInfo<RegStatusInfo>> usrLogin(@FieldMap Map<String, String> maps);

        // 提交订单到P++
        @POST("/pingpp/create-charge")
        @FormUrlEncoded
        Call<TotalInfo> PayShoppingCartListToPing(@FieldMap Map<String, String> maps);

        // 现金账户提现
        @POST("/shop/cash-acount/be-money")
        @FormUrlEncoded
        Call<TotalInfo> cashBeMoney(@FieldMap Map<String, String> maps);

        // 现金账户提现
        @POST("/common/share-finish")
        @FormUrlEncoded
        Call<TotalInfo<ShareFinishEntity>> shareFinish(@FieldMap Map<String, String> maps);

        // 添加收件人
        @POST("/shop/consignee-address/add")
        Call<TotalInfo> addAdress(@Body AddressListItem item);

        // 修改收件地址
        @POST("/shop/consignee-address/update")
        Call<TotalInfo> updateAdress(@Body AddressListItem item);

        // 获取购物车列表
        @POST("/shop/shopping-cart/calculate")
        Call<TotalInfo<ShoppingCartObject>> getShoppingCartList(@Body ShoppingCarSourceObject obj);

        // 发表购物笔记
        @POST("/work/save-images")
        Call<TotalInfo> publishOnePic(@Body PicDiaryUploadItem item);

        // 提交购物车信息到服务器
        @POST("/shop/shopping-cart/settle")
        Call<TotalInfo<ShoppingCartObject>> setShoppingCartList(@Body PayOrderNoPost item);

        // 提交订单到服务器
        @POST("/shop/order/submit-order")
        Call<TotalInfo<OrderNo>> PayShoppingCartList(@Body PayOrderNoPost item);

        // 加入到购物车
        @POST("/shop/shopping-cart/add")
        Call<TotalInfo> addShoppingCartList(@Body ShoppingCarSourceObject item);

        //修改购物车中的商品
        @POST("/shop/shopping-cart/update")
        Call<TotalInfo> updateShoppingCartGoods(@Body ShoppingCarSourceObject item);

        // 用户红包sku列表
        @POST("/shop/red-package/list-bysku")
        Call<TotalInfo<RedPackageObject>> getRedPackageSku(@Body SkusObject object);

        // 用户红包sku列表数量
        @POST("/shop/red-package/count-bysku")
        Call<TotalInfo<RedPackageCountEntity>> getRedPackageSkuCount(@Body SkusObject object);


    }


    // 获取打点上报api方法
    public static ApiShijiLogstash getApiShijiLogstash() {
        return retrofitLogstash().create(ApiRequest.ApiShijiLogstash.class);
    }

    // 打点上报相关接口
    public interface ApiShijiLogstash {

        // 首页个性化品牌推荐 关注品牌推荐  00101
        @POST("/00101")
        Call<TotalInfo> reportMainRecFollowEvent(@Body MainRecBrandLogstashItem logstashItem);

        // 首页个性化品牌推荐 取消关注品牌推荐  00102
        @POST("/00102")
        Call<TotalInfo> reportMainRecUnfollowEvent(@Body MainRecBrandLogstashItem logstashItem);

        // 首页个性化品牌推荐 品牌性情  00103
        @POST("/00103")
        Call<TotalInfo> reportMainRecBrandEvent(@Body MainRecBrandLogstashItem logstashItem);

        // 首页个性化品牌推荐 商品详情  00104
        @POST("/00104")
        Call<TotalInfo> reportMainRecGoodsEvent(@Body MainRecBrandGoodsLogstashItem logstashItem);


        // 首页顶部Banner点击事件	00201
        @POST("/00201")
        Call<TotalInfo> reportMainTopBannerEvent(@Body MainBannerLogstashItem logstashItem);

        // 首页分类栏点击事件	00202
        @POST("/00202")
        Call<TotalInfo> reportMainCagetoryEvent(@Body CategoryLogstashItem logstashItem);

        // 首页活动Banner点击事件	00203
        @POST("/00203")
        Call<TotalInfo> reportMainActBannerEvent(@Body MainBannerLogstashItem logstashItem);


        // 首页搭配点击事件事件	00401
        @POST("/00401")
        Call<TotalInfo> reportMainMatchEvent(@Body MatchLogstashItem logstashItem);

        // 首页搭配商品点击事件事件	00402
        @POST("/00402")
        Call<TotalInfo> reportMainMatchGoodsEvent(@Body MatchGoodsLogstashItem logstashItem);

        // 分类顶部事件 00501
        @POST("/00501")
        Call<TotalInfo> reportCategoryEvent(@Body CategoryLogstashItem categoryLogstashItem);

        // 分类品牌事件 00502
        @POST("/00502")
        Call<TotalInfo> reportCategoryBrandEvent(@Body CategoryBrandLogstashItem categoryLogstashItem);

        // 分类商城事件 00503
        @POST("/00503")
        Call<TotalInfo> reportCategorySiteEvent(@Body CategorySiteLogstashItem categoryLogstashItem);

        // 搭配点击事件事件	00601
        @POST("/00601")
        Call<TotalInfo> reportMatchEvent(@Body MatchLogstashItem logstashItem);

        // 搭配商品点击事件事件	00602
        @POST("/00602")
        Call<TotalInfo> reportMatchGoodsEvent(@Body MatchGoodsLogstashItem logstashItem);

        // 3.2版本首页顶部分类条  00701
        @POST("/00701")
        Call<TotalInfo> reportMainTopMenuEvent(@Body MainMenuLogstashItem logstashItem);

        // 3.2版本首页顶部banner  00702
        @POST("/00702")
        Call<TotalInfo> reportMainMenuBannerEvent(@Body MainMenuTopBannerLogstashItem logstashItem);

        // 3.2版本首页今日特卖  00703
        @POST("/00703")
        Call<TotalInfo> reportMainMenuSpecialSaleEvent(@Body MainMenuSpecialSaleLogstashItem logstashItem);

        // 3.2版本首页至in单品  00704
        @POST("/00704")
        Call<TotalInfo> reportMainMenuTopInEvent(@Body MainMenuTopInLogstashItem logstashItem);

        // 3.2版本首页top服饰  00705
        @POST("/00705")
        Call<TotalInfo> reportMainMenuTopClotheEvent(@Body MainMenuTopClotheLogstashItem logstashItem);

        // 3.2版本首页专题精选banner  00706
        @POST("/00706")
        Call<TotalInfo> reportMainMenuThemeEvent(@Body MainMenuThemeLogstashItem logstashItem);

        // 3.2版本首页热门品类  00707
        @POST("/00707")
        Call<TotalInfo> reportMainMenuHotCategoryEvent(@Body MainMenuHotCategoryLogstashItem logstashItem);


//		// 设置某一闪购活动的开抢提醒（首页）
//		@POST("/shop/flash-sale/set-notice")
//		@FormUrlEncoded
//		Call<TotalInfo> setFlashSaleNotice(@FieldMap Map<String, String> maps);
    }

    static Retrofit mRetrofit;
    static Retrofit mRetrofitLogstash;

    public static Retrofit retrofit() {
        if (mRetrofit == null) {

            // 持久化cookie管理
            ClearableCookieJar cookieJar =
                    new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(BaseApplication.getInstance()));

            OkHttpClient.Builder builder = new OkHttpClient.Builder().cookieJar(cookieJar);
            setCache(builder);
//			setParam(builder);
//			setCookies(builder);
            setHeader(builder);
            setConnect(builder);
            OkHttpClient okHttpClient = builder.build();
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(Configration.SERVER)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return mRetrofit;
    }

    public static Retrofit retrofitLogstash() {
        if (mRetrofitLogstash == null) {

            // 持久化cookie管理
            ClearableCookieJar cookieJar =
                    new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(BaseApplication.getInstance()));

            OkHttpClient.Builder builder = new OkHttpClient.Builder().cookieJar(cookieJar);
            setCache(builder);
//			setParam(builder);
//			setCookies(builder);
            setHeader(builder);
            setConnect(builder);
            OkHttpClient okHttpClient = builder.build();
            mRetrofitLogstash = new Retrofit.Builder()
                    .baseUrl(Configration.SERVER)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return mRetrofitLogstash;
    }

    public static void setCache(OkHttpClient.Builder builder) {
        File cacheFile = new File(BaseApplication.getInstance().getExternalCacheDir(), "shiji");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetUtil.IsInNetwork(BaseApplication.getInstance())) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (NetUtil.IsInNetwork(BaseApplication.getInstance())) {
                    int maxAge = 0;
                    // 有网络时 设置缓存超时时间0个小时
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("device")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("nyn")
                            .build();
                }
                return response;
            }
        };
        builder.cache(cache).addInterceptor(cacheInterceptor);
    }

    // 主要设置一些公共属性参数，比如http://www.baidu.com/?param=xxx， 其中？param=xxx就是接入的参数
    public static void setParam(OkHttpClient.Builder builder) {
        final String deviceInfo = BaseApplication.getDeviceInfo();
        Interceptor addQueryParameterInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request request;
                HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                        // Provide your custom parameter here
                        .addQueryParameter("device", deviceInfo)
                        .build();
                request = originalRequest.newBuilder().url(modifiedUrl).build();
                return chain.proceed(request);
            }
        };
        builder.addInterceptor(addQueryParameterInterceptor);
    }

    // 设置头信息
    public static void setHeader(OkHttpClient.Builder builder) {
        final String deviceInfo = BaseApplication.getDeviceInfo();
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder()
                        .header("accept", "*/*")
                        .header("Content-Type", "application/json")
                        .header("connection", "Keep-Alive")
                        .header("device", deviceInfo)
                        .method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        builder.addInterceptor(headerInterceptor);
    }

    public static void setConnect(OkHttpClient.Builder builder) {
        //设置超时
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
    }
}
