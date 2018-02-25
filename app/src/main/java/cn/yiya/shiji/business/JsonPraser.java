package cn.yiya.shiji.business;

public class JsonPraser {
//    public static final int TYPE_UNDO = 0;
//    public static final int TYPE_LOGIN = 1;
//    public static final int TYPE_REGIST = 2;
//    public static final int TYPE_GETINFO = 3;
//    public static final int TYPE_QINIU_TOKEN = 4;
//    public static final int TYPE_QINIU_URL = 5;
//    public static final int TYPE_UPLOAD_CID = 6;
//    public static final int TYPE_WEEK_TASK = 7;
//    public static final int TYPE_CUSTOM_WEEK_TASK = 8;
//    public static final int TYPE_SPECIAL_TASK = 9;
//    public static final int TYPE_CHECK_TASK = 10;
//    public static final int TYPE_CHECK_SPECIAL_TASK = 11;
//    public static final int TYPE_ADD_CUSTOM_TASK = 12;
//    public static final int TYPE_OVERVIEW = 13;
//    public static final int TYPE_CUSTOM_OVERVIEW = 14;
//    public static final int TYPE_RECORD_OFFSET = 15;
//    public static final int TYPE_REVIEW = 16;
//    public static final int TYPE_BANNER = 17;
//    public static final int TYPE_TOOLS_SAVE = 18;
//    public static final int TYPE_TOOLS_HISTORY = 19;
//    public static final int TYPE_CHANGE_TO_BORN = 20;
//    public static final int TYPE_CHANGE_EXPECT = 21;
//    public static final int TYPE_CHANGE_NAME = 22;
//    public static final int TYPE_CHANGE_CITY = 23;
//    public static final int TYPE_CHANGE_HOSPITAL = 24;
//    public static final int TYPE_CHANGE_HEAD = 25;
//    public static final int TYPE_MEDAL_INFO = 26;
//    public static final int TYPE_INTERACTION = 27;
//    public static final int TYPE_NOTIFY_PARTNER = 28;
//    public static final int TYPE_ISLOGIN = 29;
//    public static final int TYPE_INVITECODE = 30;
//    public static final int TYPE_SAVE_BABY_INFO = 31;
//    public static final int TYPE_RECORD = 32;
//    public static final int TYPE_TOOLS_STEP_HISTORY = 33;
//    public static final int TYPE_TOOLS_MILK_HISTORY = 34;
//    public static final int TYPE_TOOLS_SHIT_HISTORY = 35;
//    public static final int TYPE_TOOLS_FETAL_HISTORY = 36;
//    public static final int TYPE_TOOLS_CONSTRATION_HISTORY = 37;
//    public static final int TYPE_TOOLS_BMI_HISTORY = 38;
//    public static final int TYPE_CHECK_UPDATE = 39;
//    public static final int TYPE_SAVA_IMAGE = 40;
//    public static final int TYPE_SAVA_IMAGES = 41;
//    public static final int TYPE_GET_IMAGE_TAGS = 42;
//    public static final int TYPE_GET_FOOD_TYPE_LIST = 43;
//    public static final int TYPE_GET_FOOD_LIST = 44;
//    public static final int TYPE_GET_FOOD_DETAIL = 145;
//    public static final int HTTP_WORK_RECOMMEND_LIST = 45;
//    public static final int HTTP_WORK_FOLLOW_LIST = 46;
//    public static final int HTTP_WORK_DETAIL = 447;
//    public static final int HTTP_COMMENT_ADD = 48;
//    public static final int HTTP_COMMENT_LIST = 49;
//    public static final int HTTP_LIKE_USER_LIST = 50;
//
//
//    public static final int HTTP_WORK_LIST = 51;
//    public static final int HTTP_TAG_WORK = 52;
//    public static final int HTTP_WORK_REPORT = 53;
//    public static final int HTTP_WORK_LIKE = 53;
//
//    public static final int HTTP_TAG_LIST = 55;
//    public static final int HTTP_TAG_DETAILS = 56;
//    public static final int HTTP_TAG_RANK = 57;
//    public static final int HTTP_FOLLOW_TAG = 58;
//    public static final int HTTP_UNFOLLOW_TAG = 59;
//    public static final int HTTP_TAG_STAR = 60;
//    public static final int HTTP_TAG_LIKE = 61;
//    public static final int HTTP_USER_TAG = 62;
//
//    public static final int HTTP_USER_FOLLOW = 63;
//    public static final int HTTP_USER_FANS = 64;
//    public static final int HTTP_USER_INFO = 65;
//    public static final int HTTP_USER_SNS_INFO = 66;
//    public static final int HTTP_FOLLOW_USER = 67;
//    public static final int HTTP_UNFOLLOW_USER = 68;
//    public static final int HTTP_PHOTO_COUNT = 69;
//    public static final int HTTP_PHOTO_LIST = 70;
//    public static final int HTTP_PHOTO_UPLOAD = 71;
//
//
//    public static final int HTTP_BANNER_LIST = 72;
//
//    public static final int HTTP_RECOMMEND_LIST = 73;
//    public static final int HTTP_RECOMMEND_AGE_LIST = 74;
//
//    public static final int HTTP_CARD_PHASE = 75;
//    public static final int HTTP_CARD_LIST = 76;
//    public static final int HTTP_CARD_DETAIL = 77;
//
//    public static final int HTTP_GET_TIMEIMAGE = 96;
//    public static final int HTTP_SET_TIMEIMAGE = 97;
//    // 新增四期接口，jerryzhang
//    public static final int HTTP_HOT_BRANDS = 98;
//    public static final int HTTP_TYPE_SCORE = 99;                // 商城积分
//    public static final int HTTP_HOT_BRANDS_LIST = 100;        // 热门品牌列表
//    public static final int HTTP_HOT_BRANDS_SORT = 101;        // 热门品牌排序
//    public static final int HTTP_HOT_HEAD_MALL = 102;        // 搜索页热门商城
//    public static final int HTTP_HOT_GOODS = 103;                // 热门单品
//    public static final int HTTP_HOT_MALL_SORT = 104;            // 热门商城排序
//    public static final int HTTP_NEW_LIST = 105;                // 最新上架
//    public static final int HTTP_FIRST_HOT_CLASSS = 106;        //一级热门分类
//    public static final int HTTP_SEROND_HOT_CLASS = 107;        //二级热门分类
//    public static final int HTTP_CLASS_DETAILS_LIST = 108;      //获取某一类型商品列表
//    public static final int HTTP_BRAND_ACTIVITY_LIST = 109;                        // 某一品牌活动列表
//    public static final int HTTP_BRANDS_DETAILS = 110;                            // 某一品牌详情背景
//    public static final int HTTP_OFFICIAL_TAG = 111;                            // 搜索官方标签
//    public static final int HTTP_TAG_PRODUCCE_LIST = 112;                        // 某一标签下的推荐列表
//    public static final int APP_RECOMMEND = 113;                                // 获取推荐App列表
//    public static final int EXCHANGE_CODE = 114;                                // 兑换兑换码
//    public static final int EXCHANGE_CODE_RECORD = 115;                            // 兑换记录
//    public static final int THEMATIC_ACTIVITY = 116;                            // 获取某一专题下活动列表
//    public static final int ACTIVITY_GOODS = 117;                                // 获取某一活动商品列表
//    public static final int FLASH_SALE = 118;                                    // 获取某一闪购商品列表
//    public static final int ORDER_LIST = 119;                                    // 获取某一状态订单列表
//    public static final int SHOP_ORDER_TO_CAR = 120;                            // 订单商品加回购物车
//    public static final int SYSTEM_SKIN = 121;                                    // 获取当前系统默认皮肤
//    public static final int SEARCH_DATA = 122;                                 // 获取最新的搜索数据
//    public static final int HOT_CATEGORY_FIRST = 156;                             //获取一级热门分类
//    public static final int HOT_CATEGORY_SECOND_THIRD = 157;                             //获取二三级热门分类
//
//    // 绿玉导航相关接口
//    public static final int NAVI_BANNER_LIST = 123;                                // 获取导航的banner信息
//    public static final int NAVI_COUNTRY_LIST = 124;                            // 获取国家列表
//    public static final int SHOP_LINE_LIST = 125;                                // 获取热门线路列表
//    public static final int CITY_LIST = 126;                                    // 获取某一国家下的城市列表
//    public static final int COUNTRY_RECOMMEND_INFO = 127;                        // 获取单个国家的必买推荐信息
//    public static final int NEWS_LIST_OF_COUNTRY = 128;                         // 获取某一国家的资讯信息列表
//    public static final int TAX_REBATE_OF_COUNTRY = 129;                        // 获取单个国家退税信息
//    public static final int COUNTRY_BRIEF_INFO = 130;                            // 获取单个国家简要信息
//    public static final int COUNTRY_DETAIL_INFO = 131;                            // 获取单个国家详细信息
//    public static final int CITY_BRIEF_INFO = 132;                                // 获取单个城市简要信息
//    public static final int CITY_DETAIL_INFO = 133;                                // 获取单个城市详细信息
//    public static final int MALL_LIST_OF_CITY = 134;                            // 获取某一城市下的商场列表
//    public static final int STORE_LIST_OF_CITY = 135;                            // 获取某一城市下的店铺列表
//    public static final int NEWS_LIST_OF_CITY = 136;                            // 获取某一城市的资讯信息列表
//    public static final int NEWS_DETAIL_INFO = 137;                                // 获取单个资讯的详细信息
//    public static final int COUPON_LIST_OF_CITY = 138;                            // 获取某一城市下的coupon列表
//    public static final int MALL_DETAIL_INFO = 139;                                // 获取单个商场详细信息
//    public static final int STORE_LIST_OF_MALL = 140;                            // 获取某一商场下的店铺列表
//    public static final int STORE_BRANDS_CATEGORY_OF_MALL = 141;                // 获取某一商场下店铺的品牌与分类列表
//    public static final int MALL_RECOMMEND_INFO = 142;                            // 获取单个商场下的推荐信息
//    public static final int COUPON_LIST_OF_MALL = 143;                            // 获取某一商场下的coupon列表
//    public static final int STORE_LIST_OF_SAME = 144;                            // 获取某一店铺下的分店列表
//    public static final int STORE_LIST_OF_AROUND = 145;                            // 获取某一店铺附近的店列表
//    public static final int STORE_DETAIL_INFO = 146;                            // 获取单个店铺详细信息
//    public static final int COUPON_LIST_OF_STORE = 147;                            // 获取某一店铺下的coupon列表
//    public static final int STORE_RECOMMEND_INFO = 148;                            // 获取单个店铺下的推荐信息
//    public static final int COUPON_DETAIL_INFO = 149;                            // 获取coupon详细信息
//    public static final int COUPON_COLLECT = 150;                                // 收藏某一coupon卷
//    public static final int COUPON_UNCOLLECT = 151;                                // 取消收藏某一coupon卷
//    public static final int COUPON_LIST_OF_USER = 152;                            // 获取收藏的coupon列表
//    public static final int STORE_BRANDS_CATEGORY_OF_CITY = 153;                // 获取某一城市下店铺的品牌与分类列表
//    public static final int OFF_LINE_LIST = 154;                                // 获取支持离线的城市列表
//    public static final int OFF_LINE_DOWNLOAD = 155;                            // 获取单个城市的离线包
//
//    // 新增四期接口，tomyang
//    public static final int HTTP_TYPE_REDPACKAGE = 199;                        // 商城红包
//    public static final int HTTP_TYPE_NOTIFY = 200;                                // 用户消息数量
//    public static final int HTTP_TYPE_NOTIFY_DETAIL = 201;                        // 获取社区和物流消息数量
//    public static final int HTTP_TYPE_NOTIFY_LIST = 202;                        // 用户消息列表
//    public static final int HTTP_TYPE_NOTIFY_DELETE = 203;                        // 用户消息删除
//    public static final int HTTP_TYPE_CONSIGNEE_ADDRESS_ADD = 204;                // 添加收件人
//    public static final int HTTP_TYPE_CONSIGNEE_ADDRESS_LIST = 205;                // 收件人列表
//    public static final int HTTP_TYPE_CONSIGNEE_ADDRESS_UPDATE = 206;            // 修改收件人
//    public static final int HTTP_TYPE_CONSIGNEE_ADDRESS_DELETE = 207;            // 删除收件人
//    public static final int HTTP_TYPE_SHOP_ORDER_LIST = 208;                    // 查询付款状态接口
//    public static final int HTTP_TYPE_SHOP_ORDER_DETAIL = 209;                    // 获取某一订单详情
//    public static final int TYPE_SHOP_MENU_ACTIVITY = 210;                        // 获取某一菜单下活动列表
//    public static final int HTTP_TYPE_CAMPAIGN_DETAIL = 211;                      // 获取活动标签详情
//    public static final int HOT_CATEGORY_FIRST_SECOND = 212;                      //获取一二级热门分类
//    public static final int HOT_CATEGORY_THRID = 213;                             // 获取三级热门分类
//    public static final int WORK_USED_GOODSES = 214;                              // 获取使用过得商品
//
//    // 新增四期接口，chenjian
//    public static final int HTTP_TYPE_SHOPPLING_LIST = 300;                     // 获取购物车列表
//    public static final int HTTP_TYPE_SHOPPLING_ORDERNO = 301;                  // 获取订单号
//    public static final int HTTP_TYPE_SHOPPLING_PING = 302;                     // 提交订单到Ping++
//    public static final int HTTP_TYPE_STATUS_ORDERNO = 303;                     // 查询支付状态
//    public static final int HTTP_TYPE_MALL_MENU_LIST = 304;                     // 获取顶部菜单列表
//    public static final int HTTP_TYPE_TAG_BRAND = 305;                          // 根据Tag获取品牌
//    public static final int MALL_HOME_DETAIL_GOODS = 306;                       // 从首页进入获取某类商品数据
//    public static final int MALL_LIMIT_OPTION = 307;                            // 获取商品筛选条件
//
//    public static final int HTTP_TYPE_ORDER_LIST = 400;
//    public static final int HTTP_TYPE_ORDER_DETAIL = 401;
//    public static final int HTTP_TYPE_PULL_LAYER = 402;
//    public static final int HTTP_TYPE_RECORD_VISIT_GOODS = 403;
//    public static final int APP_VERSION_INFO = 404;                             // 获取版本信息
//    public static final int APP_START_IMAGE = 405;                              // 获取App启动图片
//
//    public static final int HTTP_CHEAP_GOODS = 450;                             // 获取某一品牌的价格最低商品
//    public static final int HTTP_DISCOUNT_GOODS = 451;                            // 获取某一商品的折扣最多商品
//    public static final int HTTP_ACTIVITY_LIST = 452;                            // 获取某一品牌活动列表商品
//    public static final int SHOP_GOODS_BRAND_GOODS = 453;                        // 获取菜单下某一分类商品列表
//    public static final int MENU_CATEGORY_LIST = 454;                            // 获取某一菜单下分类列表
//    public static final int SHOP_GOODS_BRAND_OPTION = 455;                        // 获取某一品牌下过滤选项
//    public static final int GOODS_BOUGHT = 456;                                // 获取用户购买的商品列表
//    public static final int GOODS_PROFILE = 457;                                // 获取某一商品的概要信息
//    public static final int SHOPPING_CAR_COMMIT = 458;                            // 把商品添加到购物车
//    public static final int SHOPPING_CAR_DELETE = 459;                            // 删除购物车中的商品
//    public static final int SHOPPING_CAR_UPDATA = 460;                            // 修改购物车中的商品
//    public static final int SHOPPING_CAR_INFO = 461;                            // 获取购物车列表概要信息
//    public static final int BRAND_REGISTER_LIST = 462;                            // 获取注册界面热门品牌
//    public static final int HOT_CATEGORY_REGISTER_LIST = 463;                    // 获取注册界面热门品类
//    public static final int FOLLOW_BRANDS_CATEGORIES = 464;                        // 批量关注品牌品类
//    public static final int REGISTER_FOLLOW_SKIP = 465;                            // 跳过注册关注
//    public static final int SEARCH_HOT_WORDS = 466;                                // 获取热门搜索列表
//    public static final int SHOP_SEARCH_DATA = 467;                                // 获取最新的搜索数据
//    public static final int SEARCH_GOODS = 468;                                    // 搜索之后的商品列表
//    public static final int SEARCH_OPTION = 469;                                // 搜索之后的商品的过滤选项
//    public static final int HTML_INFO = 472;                                    // 获取HTML的版本信息
//    public static final int HTTP_FLASH_SALE_SET_NOTICE = 470;                    // 设置某一闪购活动开抢提醒
//    public static final int HTTP_FLASH_SALE_LIST = 471;                            // 获取闪购活动列表
//    public static final int TYPE_WX_LOGIN = 473;                                // 微信登陆
//    public static final int TYPE_ACTIVITY_DETAIL = 474;                         // 获取某一活动详情
//    public static final int TYPE_FLASH_SALE_DETAIL = 475;                       // 获取某一闪购活动详情
//    public static final int TYPE_FOLLOW_TAGS = 476;                                // 批量关注品牌
//    public static final int TYPE_UNFOLLOW_TAGS = 477;                            // 取消批量关注品牌
//    public static final int TYPE_FOLLOW_CATEGORIES = 478;                        // 批量关注品类
//    public static final int TYPE_UNFOLLOW_CATEGORIES = 479;                        // 取消批量关注品类
//    public static final int TYPE_FLASH_SALE_NOTICE_LIST = 480;                    // 用户获取闪购提醒活动列表
//
//    public static final int ORDER_DETAIL = 481;                                    // 订单详情
//    public static final int RED_PACKAGE_URL = 482;                              // 获取红包地址
//    public static final int GET_PAYMODE_LIST = 483;                             // 支付方式列表
//    public static final int GO_CMB_PAY = 484;                                   // 招行支付
//    public static final int MAIN_BANNER = 485;                                  // 首页轮播
//    public static final int MAIN_FLASH_SALE = 486;
//    public static final int MAIN_ACT_IMAGE = 487;
//    public static final int Main_Thematic_List = 488;
//    public static final int Main_DaliyRec_List = 489;
//    public static final int Main_BrandsRec_List = 490;
//    public static final int Main_Car_Count = 491;
//    public static final int Login_Out = 492;
//
//    private Gson gson;
//    private static JsonPraser mInstance;
//
//
//    private JsonPraser() {
//        //排除指定修饰符的字段  protected字段  private 字段不参与生成json
////		gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PROTECTED,Modifier.PRIVATE)
////				.create();
//        gson = new Gson();
//    }
//
//    public static JsonPraser getInstance() {
//        if (mInstance == null) {
//            mInstance = new JsonPraser();
//        }
//        return mInstance;
//    }
//
//    public String getJson(Object obj) {
//        return gson.toJson(obj);
//    }
//
//    public HttpMessage getHttpMessage(String json) {
//
//        HttpMessage msg = HttpMessage.getDefault();
//        if (TextUtils.isEmpty(json)) {
//            return msg;
//        } else {
//            //Log.e("HttpJson", json);
//            try {
//                JSONObject obj = new JSONObject(json);
//                msg.code = obj.getInt("code");
//                msg.message = obj.getString("message");
//                msg.data = obj.getString("data");
//            } catch (JSONException e) {
//                return msg;
//            }
//            return msg;
//        }
//    }
//
//    public Object prase(int type, String obj) {
//        if (TextUtils.isEmpty(obj) || obj.equals("[]")) {
//            return null;
//        }
//        switch (type) {
//            case TYPE_LOGIN:        //登陆
//                return praserLogin(obj);
//            case TYPE_REGIST:        //注册
//                return praserRegist(obj);
//            case TYPE_GETINFO:        //获取个人信息
//                return getUserInfo(obj);
//            case TYPE_QINIU_TOKEN:    //七牛token
//                return getQiNiuToken(obj);
//            case TYPE_ISLOGIN:
//                return getIsLogin(obj);
//            case TYPE_BANNER:
//                return getBanners(obj);
//            case TYPE_INVITECODE:
//                return getInviteCode(obj);
//            case TYPE_QINIU_URL:
//                return getQiNiuDownloadUrl(obj);
//            case TYPE_CHECK_TASK:
//                return getCheckTask(obj);
//            case TYPE_RECORD_OFFSET:
//                return getRecordOffset(obj);
//            case GET_PAYMODE_LIST:
//                return getPayModeList(obj);
//            case HTTP_TYPE_SCORE:
//                return getScore(obj);
//            case HTTP_TYPE_REDPACKAGE:
//                return getRedPackage(obj);
//            case HTTP_TYPE_NOTIFY:
//                return getNotifyCount(obj);
//            case HTTP_TYPE_NOTIFY_DETAIL:
//                return getNotifyDetailCount(obj);
//            case HTTP_TYPE_NOTIFY_LIST:
//                return getNotifyList(obj);
//            case HTTP_TYPE_CONSIGNEE_ADDRESS_LIST:
//                return getAddressList(obj);
//            case TYPE_MEDAL_INFO:
//                return getMedalInfo(obj);
//            case TYPE_GET_IMAGE_TAGS:
//                return getImageTagArrayList(obj);
//            case HTTP_WORK_RECOMMEND_LIST:
//                return recommendList(obj);
//            case HTTP_WORK_FOLLOW_LIST:
//                return followList(obj);
//            case HTTP_WORK_DETAIL:
//                return details(obj);
//            case HTTP_COMMENT_LIST:
//                return commentList(obj);
//            case HTTP_LIKE_USER_LIST:
//                return likeUserList(obj);
//            case HTTP_WORK_LIST:
//                return workList(obj);
//            case HTTP_TAG_WORK:
//                return workList(obj);
//            case HTTP_TAG_DETAILS:
//                return tagDetails(obj);
//            case HTTP_TAG_RANK:
//                return tagRank(obj);
//            case HTTP_TAG_STAR:
//                return tagWork(obj);
//            case HTTP_TAG_LIKE:
//                return tagLike(obj);
//            case HTTP_USER_TAG:
//                return userTag(obj);
//            case HTTP_USER_INFO:
//                return userInfo(obj);
//            case HTTP_USER_SNS_INFO:
//                return userInfo(obj);
//            case HTTP_PHOTO_LIST:
//                return userPhoto(obj);
//            case HTTP_USER_FANS:
//                return userFans(obj);
//            case HTTP_USER_FOLLOW:
//                return userFans(obj);
//            case ORDER_DETAIL:
//                return getOrderDetai(obj);
//            case RED_PACKAGE_URL:
//                return getRedPackageUrl(obj);
//            case HTTP_TAG_LIST:
//                return getHotTag(obj);
//            case HTTP_BANNER_LIST:
//                return bannerList(obj);
//            case HTTP_RECOMMEND_LIST:
//                return recommendUserList(obj);
//            case HTTP_RECOMMEND_AGE_LIST:
//                return recommendAgeUserList(obj);
//            case HTTP_HOT_BRANDS:
//                return brandsList(obj);
//            case HTTP_HOT_BRANDS_LIST:
//                return hotBrandsList(obj);
//            case HTTP_HOT_BRANDS_SORT:
//                return sortBrandsList(obj);
//            case HTTP_HOT_HEAD_MALL:
//                return hotMallList(obj);
//            case HTTP_HOT_GOODS:
//                return hotSingleList(obj);
//            case HTTP_HOT_MALL_SORT:
//                return mallSortList(obj);
//            case HTTP_NEW_LIST:
//                return newGoodsList(obj);
//            case HTTP_FIRST_HOT_CLASSS:
//                return hotClassList(obj);
//            case HTTP_SEROND_HOT_CLASS:
//                return secondClassList(obj);
//            case HTTP_CLASS_DETAILS_LIST:
//                return singleClassList(obj);
//            case HTTP_TYPE_SHOPPLING_LIST:
//                return getShopCartList(obj);
//            case HTTP_BRAND_ACTIVITY_LIST:
//                return brandActivityList(obj);
//            case HTTP_BRANDS_DETAILS:
//                return brandDetailsList(obj);
//            case HTTP_OFFICIAL_TAG:
//                return officialTagList(obj);
//            case HTTP_TAG_PRODUCCE_LIST:
//                return tagProduceList(obj);
//            case HTTP_TYPE_SHOPPLING_ORDERNO:
//                return getOrderNO(obj);
//            case HTTP_TYPE_STATUS_ORDERNO:
//                return getOrderStatus(obj);
//            case MALL_HOME_DETAIL_GOODS:
//                return mallGoodsList(obj);
//            case MALL_LIMIT_OPTION:
//                return mallLimitOption(obj);
//            case HTTP_CHEAP_GOODS:
//                return cheapGoodsList(obj);
//            case HTTP_DISCOUNT_GOODS:
//                return discountGoods(obj);
//            case HTTP_ACTIVITY_LIST:
//                return activityList(obj);
//            case SHOP_GOODS_BRAND_GOODS:
//                return shopGoodsBrandGoods(obj);
//            case MENU_CATEGORY_LIST:
//                return menuCategoryList(obj);
//            case SHOP_GOODS_BRAND_OPTION:
//                return shopGoodsBrandOption(obj);
//            case APP_VERSION_INFO:
//                return getVersionInfo(obj);
//            case APP_START_IMAGE:
//                return getStartImage(obj);
//            case GOODS_BOUGHT:
//                return getOrderGoodsInfo(obj);
//            case GOODS_PROFILE:
//                return getGoodsProfile(obj);
//            case SHOPPING_CAR_COMMIT:
//                return getShopCartData(obj);
//            case SHOPPING_CAR_DELETE:
//                return getShopCartData(obj);
//            case SHOPPING_CAR_UPDATA:
//                return getShopCartData(obj);
//            case SHOPPING_CAR_INFO:
//                return getShoppingCarInfo(obj);
//            case BRAND_REGISTER_LIST:
//                return getBrandsRegisterList(obj);
//            case HOT_CATEGORY_REGISTER_LIST:
//                return getCategoriesRegister(obj);
//            case FOLLOW_BRANDS_CATEGORIES:
//                return getFollowBrandsCategory(obj);
//            case REGISTER_FOLLOW_SKIP:
//                return getRegisterFolloeSkip(obj);
//            case SEARCH_HOT_WORDS:
//                return getSearchHotWords(obj);
//            case SHOP_SEARCH_DATA:
//                return getShopSearchWords(obj);
//            case SEARCH_GOODS:
//                return getSearchGoods(obj);
//            case SEARCH_OPTION:
//                return getSearchOption(obj);
//            case HTML_INFO:
//                return getHtmlVersionInfo(obj);
//            case APP_RECOMMEND:
//                return getAppRecommend(obj);
//            case EXCHANGE_CODE:
//                return getExchangeCode(obj);
//            case EXCHANGE_CODE_RECORD:
//                return getExchangeCodeRecode(obj);
//            case THEMATIC_ACTIVITY:
//                return getThematicList(obj);
//            case ACTIVITY_GOODS:
//                return getActivityGoodsList(obj);
//            case FLASH_SALE:
//                return getFlashSale(obj);
//            case TYPE_WX_LOGIN:
//                return getWXLoginInfo(obj);
//            case TYPE_ACTIVITY_DETAIL:
//                return getActivityDetail(obj);
//            case TYPE_FLASH_SALE_DETAIL:
//                return getFlashActivityDetail(obj);
//            case ORDER_LIST:
//                return getAllOrderList(obj);
//            case SHOP_ORDER_TO_CAR:
//                return gobackShoppingCar(obj);
//            case SYSTEM_SKIN:
//                return getSkin(obj);
//            case SEARCH_DATA:
//                return getPopularData(obj);
//            case TYPE_SHOP_MENU_ACTIVITY:
//                return getMenuActivityList(obj);
//            case HTTP_TYPE_CAMPAIGN_DETAIL:
//                return getActTagDetail(obj);
//            case NAVI_BANNER_LIST:
//                return getNaviBannerList(obj);
//            case NAVI_COUNTRY_LIST:
//                return getCountryList(obj);
//            case SHOP_LINE_LIST:
//                return getShopLineList(obj);
//            case CITY_LIST:
//                return getCityList(obj);
//            case COUNTRY_RECOMMEND_INFO:
//                return getCountryRecommendList(obj);
//            case NEWS_LIST_OF_COUNTRY:
//                return getNewsOfCountry(obj);
//            case TAX_REBATE_OF_COUNTRY:
//                return getTaxRebateOfCountry(obj);
//            case COUNTRY_BRIEF_INFO:
//                return getCountryBriefInfo(obj);
//            case COUNTRY_DETAIL_INFO:
//                return getCountryDetailInfo(obj);
//            case CITY_BRIEF_INFO:
//                return getCityBriefInfo(obj);
//            case CITY_DETAIL_INFO:
//                return getCityDetailInfo(obj);
//            case MALL_LIST_OF_CITY:
//                return getMallListOfCity(obj);
//            case STORE_LIST_OF_CITY:
//                return getStoreListOfCity(obj);
//            case NEWS_LIST_OF_CITY:
//                return getNewsListOfCity(obj);
//            case NEWS_DETAIL_INFO:
//                return getNewsDetailInfo(obj);
//            case COUPON_LIST_OF_CITY:
//                return getCountryListOfCity(obj);
//            case MALL_DETAIL_INFO:
//                return getMallDetailInfo(obj);
//            case STORE_LIST_OF_MALL:
//                return getStoreListOfMall(obj);
//            case STORE_BRANDS_CATEGORY_OF_MALL:
//                return getStoreBrandsCategoryOfMall(obj);
//            case MALL_RECOMMEND_INFO:
//                return getMallRecommendInfo(obj);
//            case COUPON_LIST_OF_MALL:
//                return getCouponListOfMall(obj);
//            case STORE_LIST_OF_SAME:
//                return getStoreListOfSame(obj);
//            case STORE_LIST_OF_AROUND:
//                return getStoreListOfAround(obj);
//            case STORE_DETAIL_INFO:
//                return getStoreDetailInfo(obj);
//            case COUPON_LIST_OF_STORE:
//                return getCouponListOfStore(obj);
//            case STORE_RECOMMEND_INFO:
//                return getStoreRecommendInfo(obj);
//            case COUPON_DETAIL_INFO:
//                return getCouponDetailInfo(obj);
//            case COUPON_COLLECT:
//                return getCouponCollect(obj);
//            case COUPON_UNCOLLECT:
//                return getCouponUncollect(obj);
//            case COUPON_LIST_OF_USER:
//                return getCouponListOfUser(obj);
//            case STORE_BRANDS_CATEGORY_OF_CITY:
//                return getStoreBrandsCategoryOfCity(obj);
//            case OFF_LINE_LIST:
//                return getOffLineList(obj);
//            case OFF_LINE_DOWNLOAD:
//                return getOffLineDownload(obj);
//            case GO_CMB_PAY:
//                return getGoCMBParam(obj);
//            case MAIN_BANNER:
//                return getMainBanner(obj);
//            case MAIN_FLASH_SALE:
//                return getMainFlashSale(obj);
//            case MAIN_ACT_IMAGE:
//                return getMainActImage(obj);
//            case Main_Thematic_List:
//                return getMainThematicList(obj);
//            case Main_DaliyRec_List:
//                return getDaliyRecList(obj);
//            case Main_BrandsRec_List:
//                return getBrandsRecList(obj);
//            case Main_Car_Count:
//                return getCarCount(obj);
//            case HOT_CATEGORY_FIRST:
//            case HOT_CATEGORY_SECOND_THIRD:
//                return getHotCategory(obj);
//            case HTTP_TYPE_PULL_LAYER:
//                return getPullLayer(obj);
//            case HOT_CATEGORY_FIRST_SECOND:
//                return getHotCategoryFirstSecond(obj);
//            case HOT_CATEGORY_THRID:
//                return getHotCategoryThrid(obj);
//            case WORK_USED_GOODSES:
//                return getUsedGoods(obj);
//            default:
//                break;
//        }
//        return null;
//    }
//
//    /**
//     * 登陆
//     *
//     * @param obj
//     * @return
//     */
//    private Object praserLogin(String obj) {
//        boolean is_complete = false;
//        try {
//            JSONObject jObject = new JSONObject(obj);
//            is_complete = jObject.getBoolean("is_complete");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return is_complete;
//    }
//
//    // 微信登陆
//    private Object getWXLoginInfo(String obj) {
//        return gson.fromJson(obj, RegStatusInfo.class);
//    }
//
//    //活动详情
//    private Object getActivityDetail(String obj) {
//        return gson.fromJson(obj, IssueActivityDetailInfo.class);
//    }
//
//    //闪购活动详情
//    private Object getFlashActivityDetail(String obj) {
//        return gson.fromJson(obj, FlashActivityDetailInfo.class);
//    }
//
//    /**
//     * 注册
//     *
//     * @param obj
//     * @return
//     */
//    private Object praserRegist(String obj) {
//        String user_id = "";
//        try {
//            JSONObject jObject = new JSONObject(obj);
//            user_id = jObject.getString("user_id");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return user_id;
//    }
//
//    /**
//     * 个人信息
//     *
//     * @param obj
//     * @return
//     */
//    public UserInfo getUserInfo(String obj) {
//        Log.i("json", (String) obj);
//        UserInfo user = gson.fromJson(obj, UserInfo.class);
//        return user;
//    }
//
//    /**
//     * 七牛上传token
//     *
//     * @param json
//     * @return
//     */
//    public String getQiNiuToken(String json) {
//        String token = "";
//        try {
//            JSONObject jObject = new JSONObject(json);
//            token = jObject.getString("token");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return token;
//    }
//
//    /**
//     * 七牛downloadurl
//     *
//     * @param json
//     * @return
//     */
//    public String getQiNiuDownloadUrl(String json) {
//        String str = "";
//        try {
//            JSONObject jObject = new JSONObject(json);
//            str = jObject.getString("url");
//        } catch (JSONException e) {
//
//            e.printStackTrace();
//        }
//        return str;
//    }
//
//    /**
//     * 是否登陆
//     *
//     * @param json
//     * @return
//     */
//
//    /**
//     * 宝爸邀请码
//     *
//     * @param json
//     * @return
//     */
//    public String getInviteCode(String json) {
//        String str = "";
//        try {
//            JSONObject jObject = new JSONObject(json);
//            str = jObject.getString("code");
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return str;
//    }
//    /**
//     * 获取一周官方任务列表
//     * @param json
//     * @return
//     */
//
//    /**
//     * 完成任务
//     *
//     * @param json
//     * @return
//     */
//    public boolean getCheckTask(String json) {
//        boolean str = false;
//        try {
//            JSONObject jObject = new JSONObject(json);
//            str = jObject.getBoolean("complete");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return str;
//    }
//    /**
//     * 官方任务总览
//     * @param json
//     * @return
//     */
//
//    /**
//     * record offset
//     *
//     * @param json
//     * @return
//     */
//    public int getRecordOffset(String json) {
//        int str = 0;
//        try {
//            JSONObject jObject = new JSONObject(json);
//            str = jObject.getInt("offset");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return str;
//    }
//
//    /**
//     * 我的钱包
//     * 个人积分获取
//     */
//    public ScoreObject getScore(String json) {
//        return gson.fromJson(json, ScoreObject.class);
//    }
//
//    /**
//     * 我的钱包
//     * 红包获取
//     */
//    public RedPackageObject getRedPackage(String json) {
//        return gson.fromJson(json, RedPackageObject.class);
//    }
//
//    /**
//     * 消息中心
//     * 用户消息数量
//     */
//    public NotifyCount getNotifyCount(String json) {
//        return gson.fromJson(json, NotifyCount.class);
//    }
//
//    /**
//     * 消息中心
//     * 获取社区和物流消息数量
//     */
//    public NotifyDetailCount getNotifyDetailCount(String json) {
//        return gson.fromJson(json, NotifyDetailCount.class);
//    }
//
//    /**
//     * 消息中心
//     * 用户消息列表
//     */
//    public NotifyListObject getNotifyList(String json) {
//        return gson.fromJson(json, NotifyListObject.class);
//    }
//
//    /**
//     * 地址管理
//     * 收件人列表
//     */
//    public AddressListObject getAddressList(String json) {
//        return gson.fromJson(json, AddressListObject.class);
//    }
//    /**
//     * 订单列表
//     * 查询付款状态接口
//     */
//    /**
//     * 订单列表
//     * 获取某一订单详情
//     */
//    //public ShopOrderDetail getShopOrderDetail(String json){
//    //	return gson.fromJson(json,ShopOrderDetail.class);
//    //}
//
//
//    /**
//     * 计步器历史记录
//     * @param json
//     * @return
//     */
//
//    /**
//     * 胎动历史
//     * @param json
//     * @return
//     */
//
////	private BMIRecordObject getShitRecord(String json){
////		return gson.fromJson(json, BMIRecordObject.class);
////	}
//
//    /**
//     * 勋章信息
//     *
//     * @param json
//     * @return
//     */
//    private int getMedalInfo(String json) {
//        int str = 0;
//        try {
//            JSONObject jObject = new JSONObject(json);
//            str = jObject.getInt("medal");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return str;
//    }
//    /**
//     * 获取自定义任务列表
//     * @param json
//     * @return
//     */
//
//
//    /**
//     * 获取banner信息
//     *
//     * @param json
//     * @return
//     */
//    private BannerObject getBanners(String json) {
//        return gson.fromJson(json, BannerObject.class);
//    }
//
//    /**
//     * 检查更新
//     *
//     * @param json
//     * @return
//     */
//
//
//    public ArrayList<ImageTag> getImageTagArrayList(String json) {
//        return gson.fromJson(json, new TypeToken<ArrayList<ImageTag>>() {
//        }.getType());
//    }
//
//
//    public Object recommendList(String json) {
//        return gson.fromJson(json, WorkObject.class);
//    }
//
//    public Object followList(String json) {
//        DebugUtil.v(json);
//        return gson.fromJson(json, WorkObject.class);
//    }
//
//    private Object details(String json) {
//        return gson.fromJson(json, WorkItem.class);
//    }
//
//    private Object commentList(String json) {
//        return gson.fromJson(json, CommentObject.class);
//    }
//
//    private Object likeUserList(String json) {
//        return gson.fromJson(json, LikerObject.class);
//    }
//
//    private Object workList(String json) {
//        return gson.fromJson(json, WorkObject.class);
//    }
//
//    private Object tagDetails(String json) {
//    }
//
//    private Object tagRank(String json) {
//    }
//
//    private Object tagWork(String json) {
//        return gson.fromJson(json, TagProduceObject.class);
//    }
//
//    private Object tagLike(String json) {
//    }
//
//    private Object userTag(String json) {
//        return gson.fromJson(json, TagObject.class);
//    }
//
//    private Object userInfo(String obj) {
//        return gson.fromJson(obj, User.class);
//    }
//
//    private Object userPhoto(String obj) {
//    }
//
//    private Object userFans(String obj) {
//        return gson.fromJson(obj, FansObject.class);
//    }
//
//    public Object getHotTag(String json) {
//        return gson.fromJson(json, TagObject.class);
//    }
//
//    public Object bannerList(String json) {
//        return gson.fromJson(json, BannerObject.class);
//    }
//
//    public Object recommendUserList(String json) {
//        return gson.fromJson(json, RememberUserObject.class);
//    }
//
//    public Object recommendAgeUserList(String json) {
//        return gson.fromJson(json, RememberUserObject.class);
//    }
//
//
//    private Object brandsList(String json) {
//        return gson.fromJson(json, BrandsObject.class);
//    }
//
//    private Object hotBrandsList(String json) {
//        return gson.fromJson(json, HotBrandsObject.class);
//    }
//
//    private Object sortBrandsList(String json) {
//        return gson.fromJson(json, BrandsSortObject.class);
//    }
//
//    private Object hotMallList(String json) {
//        return gson.fromJson(json, HotMallObject.class);
//    }
//
//    private Object hotSingleList(String json) {
//        return gson.fromJson(json, NewGoodsObject.class);
//    }
//
//    private Object mallSortList(String json) {
//        return gson.fromJson(json, HotMallSortObject.class);
//    }
//
//    private Object newGoodsList(String json) {
//        return gson.fromJson(json, NewGoodsObject.class);
//    }
//
//    private Object hotClassList(String json) {
//        return gson.fromJson(json, HotClassObject.class);
//    }
//
//    private Object secondClassList(String json) {
//    }
//
//    private Object singleClassList(String json) {
//    }            // 解析某一类型商品列表
//
//    private Object brandActivityList(String json) {
//    }      // 某一品牌活动列表
//
//    private Object brandDetailsList(String json) {
//        return gson.fromJson(json, BackgroundItem.class);
//    }                // 某一品牌详情背景
//
//    private Object officialTagList(String json) {
//        if (TextUtils.equals("[]", json)) {
//            return null;
//        }
//        return gson.fromJson(json, OfficialTagsItem.class);
//    }                                                    // 搜索官方标签
//
//    private Object tagProduceList(String json) {
//        return gson.fromJson(json, TagProduceObject.class);
//    }            // 某一标签下的推荐列表
//
//    // 解析购物车列表数据
//    private Object getShopCartList(String json) {
//        return gson.fromJson(json, ShoppingCartObject.class);
//    }
//
//    // 解析获取到订单号
//    private Object getOrderNO(String json) {
//        return gson.fromJson(json, OrderNo.class);
//    }
//
//    // 解析获取到的支付状态
//    private Object getOrderStatus(String json) {
//    }
//
//    // 解析获取到的商城首页进入的分类商品
//    private Object mallGoodsList(String json) {
//        return gson.fromJson(json, MallGoodsDetailObject.class);
//    }
//
//    private Object mallLimitOption(String json) {
//        return gson.fromJson(json, MallLimitOptionObject.class);
//    }
//
//    // 解析版本信息
//    private Object getVersionInfo(String json) {
//        return gson.fromJson(json, VersionData.class);
//    }
//
//    // 解析App启动图片
//    private Object getStartImage(String json) {
//        return gson.fromJson(json, StartImageObject.class);
//    }
//
//    private Object getHtmlVersionInfo(String json) {
//        return gson.fromJson(json, HtmlVersionInfo.class);
//    }
//
//    // 获取订单详情
//    private Object getOrderDetai(String json) {
//        return gson.fromJson(json, OrderDetailObject.class);
//    }
//
//    // 获取支付方式列表
//    private Object getPayModeList(String json) {
//        return gson.fromJson(json, PayModeObject.class);
//    }
//
//    // 获取招行支付参数
//    private Object getGoCMBParam(String json) {
//        return gson.fromJson(json, CMBPayInfo.class);
//    }
//
//    private Object getRedPackageUrl(String json) {
//        return gson.fromJson(json, ShareWxInfo.class);
//    }
//
//    // jerryzhang 2015-11-09
//    private Object cheapGoodsList(String json) {
//        return gson.fromJson(json, NewGoodsObject.class);
//    }                  // 获取某一品牌价格最低的商品列表
//
//    private Object discountGoods(String json) {
//        return gson.fromJson(json, NewGoodsObject.class);
//    }                      // 获取某一品牌折扣最多的商品
//
//    private Object activityList(String json) {
//        return gson.fromJson(json, ActivityListObject.class);
//    }                  // 获取某一品牌下的活动列表
//
//    private Object shopGoodsBrandGoods(String json) {
//        return gson.fromJson(json, ShopGoodsBrandGoodsObject.class);
//    } // 获取菜单下某一分类商品列表
//
//    private Object menuCategoryList(String json) {
//    }          // 获取某一菜单下分类列表
//
//    private Object shopGoodsBrandOption(String json) {
//        return gson.fromJson(json, MallLimitOptionObject.class);
//    }     // 获取某一品牌下过滤选项
//    // jerryzhang 2015-12-07
//
////	private Object getOrderGoodsInfo(String json){return gson.fromJson(json, NewGoodsObject.class);}			  // 获取用户购买的商品列表
////	private Object getGoodsProfile(String json){return gson.fromJson(json, WorkDetailGoodsInfo.class);}			  // 获取某一商品的概要信息
//
//    private Object getOrderGoodsInfo(String json) {
//        return gson.fromJson(json, NewGoodsObject.class);
//    }              // 获取用户购买的商品列表
//
//    private Object getGoodsProfile(String json) {
//        return gson.fromJson(json, WorkGoodsObject.class);
//    }                  // 获取某一商品的概要信息
//
//    private Object getShopCartData(String json) {
//        return gson.fromJson(json, ShoppingCartObject.class);
//    }              // 提交购物车数据返回值
//
//    private Object getShoppingCarInfo(String json) {
//    }      // 获取购物车列表概要信息
//
//    // 2015-12-24
//    private Object getBrandsRegisterList(String json) {
//        return gson.fromJson(json, HotBrandsObject.class);
//    }          // 获取注册页面热门品牌
//
//    private Object getCategoriesRegister(String json) {
//        return gson.fromJson(json, HotClassObject.class);
//    }          // 获取注册页面热门品类
//
//    private Object getFollowBrandsCategory(String json) {
//        return gson.fromJson(json, Object.class);
//    }                  // 批量关注品牌品类
//
//    private Object getRegisterFolloeSkip(String json) {
//        return gson.fromJson(json, Object.class);
//    }                  // 跳过关注注册
//
//    private Object getSearchHotWords(String json) {
//        return gson.fromJson(json, SearchHotWords.class);
//    }              // 获取热门搜索列表
//
//    private Object getShopSearchWords(String json) {
//        return gson.fromJson(json, SearchHotWords.class);
//    }              // 获取最新的搜索数据
//
//    private Object getSearchGoods(String json) {
//        return gson.fromJson(json, MallGoodsDetailObject.class);
//    }          // 搜索之后的商品列表
//
//    private Object getSearchOption(String json) {
//        return gson.fromJson(json, MallLimitOptionObject.class);
//    }          // 搜索之后的商品的过滤选项
//
//    private Object getAppRecommend(String json) {
//        return gson.fromJson(json, AppRecommendObject.class);
//    }              // 获取推荐App列表
//
//    private Object getExchangeCode(String json) {
//        return gson.fromJson(json, NotifyItem.class);
//    }                      // 兑换兑换码
//
//    private Object getExchangeCodeRecode(String json) {
//        return gson.fromJson(json, ExchangeCodeObject.class);
//    }      // 兑换记录
//
//    private Object getThematicList(String json) {
//        return gson.fromJson(json, NewGoodsObject.class);
//    }                  // 获取某一专题下活动列表
//
//    private Object getMenuActivityList(String json) {
//        return gson.fromJson(json, NewGoodsObject.class);
//    }              // 获取某一菜单下活动列表
//
//    private Object getActTagDetail(String json) {
//        return gson.fromJson(json, CampaignDetailInfo.class);
//    }              // 获取活动标签详情
//
//    private Object getActivityGoodsList(String json) {
//        return gson.fromJson(json, MallGoodsDetailObject.class);
//    }      // 获取某一活动商品列表
//
//    private Object getFlashSale(String json) {
//        return gson.fromJson(json, MallGoodsDetailObject.class);
//    }              // 获取某一闪购商品列表
//
//    //2016-1-8
////	private Object getFlashSaleList(String json){return gson.fromJson(json, AppRecommendObject.class);}			  // 获取闪购活动列表
//    private Object getAllOrderList(String json) {
//        return gson.fromJson(json, OrderListObject.class);
//    }                  // 获取某一状态订单列表
//
//    private Object gobackShoppingCar(String json) {
//        return gson.fromJson(json, Object.class);
//    }                      // 订单商品加回购物车
//
//    private Object getSkin(String json) {
//        return gson.fromJson(json, SystemSkinObject.class);
//    }                      // 获取当前系统默认皮肤
//
//    private Object getPopularData(String json) {
//        return gson.fromJson(json, SearchPopularWords.class);
//    }              // 获取最新的搜索数据
//
//    // 旅游导航相关接口
//    private Object getNaviBannerList(String json) {
//        return gson.fromJson(json, BannerObject.class);
//    }                  // 获取导航的banner信息
//
//    private Object getCountryList(String json) {
//        return gson.fromJson(json, CountryListObject.class);
//    }              // 获取国家列表
//
//    private Object getShopLineList(String json) {
//        return gson.fromJson(json, HotLineObject.class);
//    }                  // 获取热门线路列表
//
//    private Object getCityList(String json) {
//        return gson.fromJson(json, CityListObject.class);
//    }                      // 获取某一国家下的城市列表
//
//    private Object getCountryRecommendList(String json) {
//        return gson.fromJson(json, RecommendList.class);
//    }          // 获取单个国家的必买推荐信息
//
//    private Object getNewsOfCountry(String json) {
//        return gson.fromJson(json, NewsObject.class);
//    }                      // 获取某一国家的资讯信息列表
//
//    private Object getTaxRebateOfCountry(String json) {
//        return gson.fromJson(json, TaxAndInfos.class);
//    }              // 获取单个国家退税信息
//
//    private Object getCountryBriefInfo(String json) {
//        return gson.fromJson(json, CountryListInfo.class);
//    }              // 获取单个国家简要信息
//
//    private Object getCountryDetailInfo(String json) {
//        return gson.fromJson(json, CountryListInfo.class);
//    }          // 获取单个国家详细信息
//
//    private Object getCityBriefInfo(String json) {
//        return gson.fromJson(json, CityInfo.class);
//    }                      // 获取单个城市简要信息
//
//    private Object getCityDetailInfo(String json) {
//        return gson.fromJson(json, CityInfo.class);
//    }                      // 获取单个城市详细信息
//
//    private Object getMallListOfCity(String json) {
//        return gson.fromJson(json, MallDetailObject.class);
//    }              // 获取某一城市下的商场列表
//
//    private Object getStoreListOfCity(String json) {
//        return gson.fromJson(json, StoreObject.class);
//    }                  // 获取某一城市下的店铺列表
//
//    private Object getNewsListOfCity(String json) {
//        return gson.fromJson(json, NewsObject.class);
//    }                      // 获取某一城市的资讯信息列表
//
//    private Object getNewsDetailInfo(String json) {
//        return gson.fromJson(json, NewsInfo.class);
//    }                      // 获取单个资讯的详细信息
//
//    private Object getCountryListOfCity(String json) {
//        return gson.fromJson(json, CouponDetailObject.class);
//    }          // 获取某一城市下的coupon列表
//
//    private Object getMallDetailInfo(String json) {
//        return gson.fromJson(json, MallDetailInfo.class);
//    }              // 获取单个商场详细信息
//
//    private Object getStoreListOfMall(String json) {
//        return gson.fromJson(json, StoreObject.class);
//    }                  // 获取某一商场下的店铺列表
//
//    private Object getStoreBrandsCategoryOfMall(String json) {
//        return gson.fromJson(json, StoreCategoryObject.class);
//    }      // 获取某一商场下店铺的品牌与分类列表
//
//    private Object getMallRecommendInfo(String json) {
//        return gson.fromJson(json, RecommendList.class);
//    }              // 获取单个商场下的推荐信息
//
//    private Object getCouponListOfMall(String json) {
//        return gson.fromJson(json, CouponDetailObject.class);
//    }          // 获取某一商场下的coupon列表
//
//    private Object getStoreListOfSame(String json) {
//        return gson.fromJson(json, StoreObject.class);
//    }              // 获取某一店铺下的分店列表
//
//    private Object getStoreListOfAround(String json) {
//        return gson.fromJson(json, StoreObject.class);
//    }              // 获取某一店铺附近的店列表
//
//    private Object getStoreDetailInfo(String json) {
//        return gson.fromJson(json, MallDetailInfo.class);
//    }              // 获取单个店铺详细信息
//
//    private Object getCouponListOfStore(String json) {
//        return gson.fromJson(json, CouponDetailObject.class);
//    }          // 获取某一店铺下的coupon列表
//
//    private Object getStoreRecommendInfo(String json) {
//        return gson.fromJson(json, RecommendList.class);
//    }              // 获取单个店铺下的推荐信息
//
//    private Object getCouponDetailInfo(String json) {
//        return gson.fromJson(json, CouponDetailInfo.class);
//    }          // 获取coupon详细信息
//
//    private Object getCouponCollect(String json) {
//        return gson.fromJson(json, Object.class);
//    }                          // 收藏某一coupon卷
//
//    private Object getCouponUncollect(String json) {
//        return gson.fromJson(json, Object.class);
//    }                      // 取消收藏某一coupon卷
//
//    private Object getCouponListOfUser(String json) {
//        return gson.fromJson(json, MyCouponCountryList.class);
//    }          // 获取收藏的coupon列表
//
//    private Object getStoreBrandsCategoryOfCity(String json) {
//        return gson.fromJson(json, StoreCategoryObject.class);
//    }   // 获取某一城市下店铺的品牌与分类列表
//
//    private Object getOffLineList(String json) {
//        return gson.fromJson(json, OutLineCountryObject.class);
//    }              // 获取支持离线的城市列表
//
//    private Object getOffLineDownload(String json) {
//        return gson.fromJson(json, Object.class);
//    }                  // 获取单个城市的离线包
//
//    private Object getMainBanner(String json) {
//        return gson.fromJson(json, MainBannerInfo.class);
//    }
//
//    private Object getMainFlashSale(String json) {
//        return gson.fromJson(json, MainFlashSaleInfo.class);
//    }
//
//    private Object getMainActImage(String json) {
//        return gson.fromJson(json, MainActImgInfo.class);
//    }
//
//    private Object getMainThematicList(String json) {
//        return gson.fromJson(json, ThematicInfo.class);
//    }
//
//    private Object getDaliyRecList(String json) {
//        return gson.fromJson(json, DaliyRecInfo.class);
//    }
//
//    private Object getBrandsRecList(String json) {
//        return gson.fromJson(json, BrandsRecInfo.class);
//    }
//
//    private Object getCarCount(String json) {
//        return gson.fromJson(json, CarCountInfo.class);
//    }
//
//    private Object getHotCategory(String json) {
//        return gson.fromJson(json, HotCategoryObject.class);
//    }
//
//    private Object getHotCategoryFirstSecond(String json) {
//        return gson.fromJson(json, HotCategoryFirstSecondObject.class);
//    }
//    private Object getHotCategoryThrid(String json) {
//        return gson.fromJson(json, HotCategroyThridObject.class);
//    }
//
//    private Object getPullLayer(String json) {
//        return gson.fromJson(json, LayerItem.class);
//    }
//
//    private Object getUsedGoods(String obj) {
//        return gson.fromJson(obj, MallGoodsDetailObject.class);
//    }
}
