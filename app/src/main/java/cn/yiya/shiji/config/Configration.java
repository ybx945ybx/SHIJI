package cn.yiya.shiji.config;

import android.os.Environment;

public class Configration {

	// activity跳转类型
	public final static int FLASH_SALE = 11;   // 表示闪购，闪购不显示底部推荐

	public static final int MALL_HOME = 1001;                // 表示从首页跳转进来
    public static final int SECONDE_TYPE = 1002;             // 表示从商城二级分类进来
    public static final int HOT_GOODS = 1003;                // 表示从热门商城进来

	public static final String DETAIL = "detail";
    public static final String CITY = "city";
    public static final String MALL = "mall";
    public static final String STORE = "store";

	public static final String WX_APPID = "wx64b3e2ac8464e920";
	public static final String WX_APP_SCOPE = "snsapi_userinfo";
	public static final String WX_APP_STATE = "wechat_sdk_demo";

	public static final String SHAREDPREFERENCE = "supermammy";
	public static final String TRAVELCOUPON = "travelCoupon";
	public static final String TingYunKey = "36e5f7a1c5034709ad12ba954dfcb612";

	public static String SERVER = "";

	public static final String TUKEY = "2704559f7ca36596-02-y90rn1";

	public final static String COUPON_PATH = Environment.getExternalStorageDirectory() + "/shiji/couponCover";
	public final static String OFF_LINE_TIPS = "您尚未接入网络";
	public final static String ON_DOWONLOAD_SOURCE = "没有该板块离线包";
	public final static String THEME_ICON = "http://information.cdnqiniu02.qnmami.com/83_1_1479369361.png";
	public final static String HTML_STYLE_HEAD = "<style>\n"
			+ "    html,body{\n"
			+ "        margin: 0;\n"
			+ "        padding: 0;\n"
			+ "        line-height: 0;\n"
			+ "    }\n"
			+ "    .info-content{\n"
			+ "        font-family: \"Helvetica Neue\",Helvetica,sans-serif;\n"
			+ "        width: auto;\n"
			+ "        height: auto;\n"
			+ "        color: #3d4145 !important;\n"
			+ "        font-size: 12px !important;\n"
			+ "        line-height: 14px;\n"
			+ "        font-weight: normal;\n"
			+ "        word-wrap: break-word;\n"
			+ "        padding-left: 14px;\n"
			+ "        padding-right: 14px;\n"
			+ "        padding-bottom: 10px;\n"
			+ "        padding-top: 10px;\n"
			+ "        -webkit-tap-highlight-color: transparent;\n"
			+ "    }\n"
			+ "    .info-content ul{\n"
			+ "        padding: 0 !important;\n"
			+ "        margin: 0 !important;\n"
			+ "    }\n"
			+ "    .info-content li{\n"
			+ "        list-style-position: inside;\n"
			+ "        margin-bottom: 5px;\n"
			+ "        display: list-item;\n"
			+ "        text-align: -webkit-match-parent;\n"
			+ "        list-style-type: disc;\n"
			+ "    }\n"
			+ "    .info-content a{\n"
			+ "        color: #3d4145 !important;\n"
			+ "    }\n"
			+ "    ::-webkit-scrollbar {\n"
			+ "        width: 0;\n"
			+ "        height: 0;\n"
			+ "    }\n"
			+ "    .info-content table{\n"
			+ "        width: 100%;\n"
			+ "        overflow: hidden;\n"
			+ "        margin-top: 10px;\n"
			+ "        text-align: center;\n"
			+ "        line-height: 15px;\n"
			+ "        border: 1px solid #ffffff;\n"
			+ "				 font-size: 12px !important;\n"
			+ "    }\n"
			+ "    .info-content table tr:nth-child(odd){\n"
			+ "        background-color: #cbcbcb;\n"
			+ "    }\n"
			+ "    .info-content table tr:nth-child(even){\n"
			+ "        background-color: #ededed;\n"
			+ "    }\n"
			+ "</style>\n"
			+ "<div class=\"info-content\">";
	public final static String HTML_STYLE_TAIL = "</div>";
	public final static String WEIBO_APP_KEY = "463040478";
	public final static String FORWARD_PATH = Environment.getExternalStorageDirectory() + "/shiji/forward/";     //  获得SD卡的路径转发售卖图片
	public final static String FORWARD_PATH_CASH = Environment.getExternalStorageDirectory() + "/shiji/forwardcash/";     //  获得SD卡的路径转发售卖图片

	static {
		if (HttpOptions.DeBug) {
			SERVER = "http://t1.api.qnmami.com";
		}else {
			SERVER = "http://api.qnmami.com";
		}
	}

//	public static final String QQ_APPID = "1104804492";
//	public static final String QQ_APPKEY = "aX33diOR252Gu6t2";
//	public static final String QQ_TARGET_URL = "http://www.quannengmami.com";
	

//	public static final String WX_SECRET = "4e45844fcd1cc2ae32987bc93dffbee3";


	
//	public static final String SHARESDK_APPPKEY = "77088cb3c60a";
//	public static final String SHARESDK_APPSECRET = "186bfd9df93f73558e775e57be78e4cd";
//
//	public static final String UDESK_DEFAULT_SUB_DOMAIN="qnmami.udesk.cn";
//	public static final String UDESK_DEFAULT_SECRET_KEY="e7239152d3a50515a605c26bb7ca4681";
//
//
//	public static String UMENGKEY = "557ebdfa67e58e6c1f001bf3";
//	public static final String SINA_APPKEY = "463040478";
//	public static final String SINA_SECRET = "c17b6cf7959730f3392e6313d74ea0fc";
//
////	public static final String SERVER = "http://alpha.api.qnmami.com";
//										//657899640f53a4c5-00-y90rn1
////	public static final String TUKEY = "657899640f53a4c5-00-y90rn1";
//
//	public static final String QiNiuDownloadUrl = "http://diary.cdnqiniu02.qnmami.com/";
//
//	public final static String LOSS_LOGIN = "登录状态已过期，请重新登录";
//
//
//
//	public static class HttpString{
//		public static final int Male = 1;
//		public static final int Female = 2;
//		public static final int NoBaby = 1;
//		public static final int Born = 2;
////		类型 11 表示宫缩，12 表示胎动，13 表示奶量，14 表示大便，15 表示计步，16 表示 bmi 17表示疫苗
//		public static final int TOOL_CONTRACTION = 11;
//		public static final int TOOL_FETAL = 12;
//		public static final int TOOL_MILK = 13;
//		public static final int TOOL_SHIT = 14;
//		public static final int TOOL_STEP = 15;
//		public static final int TOOL_BMI = 16;
//		public static final int TOOL_VACCINE = 17;
//
//		/**
//		 *  获取验证码
//		 *  mobile 是  手机号码
//		 */
//		public static final String HTTP_GetVerifycode = "/sms/verifycode";
//		public static final String GetVerifycode_Mobile = "mobile";
//
//		/**
//		 * 注册
//		 * mobile 是  手机号码
//		 * code 是 验证码
//		 * cid
//		 */
//		public static final String HTTP_Register = "/user/register";
//		public static final String Register_Mobile = "mobile";
//		public static final String Register_Code = "code";
//		public static final String CID = "cid";
//		public static final String TYPE= "type";
//		/**
//		 * 登陆
//		 */
//		public static final String HTTP_Login = "/user/login";
//
//		/**
//		 * 提交个人信息
//		 */
//		public static final String HTTP_SubmitGender = "/user/save-backup-info"; //用户注册后保存备用信息调用接口
//		public static final String HTTP_SaveInfo = "/user/save-info";
//		public static final String SaveInfo_Gender =  "gender";	//性别
//		public static final String SaveInfo_Stage = "stage";	//阶段
//		public static final String SaveInfo_LastMenses  = "last_menses";	//最后一次月经
//		public static final String SaveInfo_ExpectedDate = "expected_date";	//预产期
//		public static final String SaveInfo_ChildName = "child_name";	//宝宝昵称
//		public static final String SaveInfo_Weight = "weight";			//宝宝体重
//		public static final String SaveInfo_BirthDate = "birth_date";	//宝宝生日
//		public static final String SaveInfo_ChildGender = "child_gender";	//宝宝性别
//		public static final String SaveInfo_InvitCode  = "invit_code";	//宝爸邀请码
//		/**
//		 * 查询个人信息
//		 */
//		public static final String HTTP_GetInfo = "/user/get-info";
//
//		public static final String HTTP_QiNiuUploadToken= "/qiniu/upload-token";
//		public static final String QiNiu_Key = "key";
//		public static final String QiNiu_Expires = "expires";
//		public static final String HTTP_QiNiuDownLoad = "/qiniu/download-url";
//
//		public static final String HTTP_IsLogin = "/user/islogin";
//		public static final String HTTP_UploadCID = "/user/update-login";
//		/**
//		 * 一周任务 自定义任务  特殊任务
//		 */
//		public static final String HTTP_WeekTask = "/task/week-tasks";
//		public static final String HTTP_WeekCustomTask = "/task/week-custom-tasks";
//		public static final String HTTP_SPECIAL_TASK = "/task/special-tasks";
//		public static final String DATE = "date";
//		public static final String Task_Spouse = "spouse";  //是否获取配偶的任务，1 为获取
//		/**
//		 * 完成任务
//		 */
//		public static final String HTTP_Check  = "/task/check";
//		public static final String HTTP_CheckSpecialTask = "/task/check-special-task";
//		//public static final String HTTP_CheckCustomTask = "";
//		public static final String Check_Type = "type";		//提交任务的类型 1 表示官方 2 表示自定义
//		public static final String Check_PlanId = "plan_id"; //当 type＝1 的时候，plan_id 需要传入，使用返回列表中的p_id
//		public static final String Check_Id = "id";	//表示任务 id，当 type=2 的时候，传入
//		/**
//		 * 新增自定义任务
//		 */
//		public static final String HTTP_AddCustom = "/task/add-custom";
//		public static final String AddCustom_Type = "type";     //类型，1 表示单次任务，2 表示重复任务
//		public static final String AddCustom_Name = "name";
//		public static final String AddCustom_Stage = "stage";   //阶段 1 表示怀孕，2 表示产后
//		public static final String AddCustom_Start = "start";
//		public static final String AddCustom_Repeat = "repeat"; //长度 7 位的字符串，1000000 表示每周一
//		public static final String AddCustom_RepeatCount = "repeat_count"; //重复周数
//		/**
//		 * 任务总览
//		 */
//		public static final String HTTP_Overview  = "/overview/official-progress";
//		public static final String Overview_Stage = "stage";
//		public static final String Overview_Tag = "tag"; //标签，不传默认拉取全部
//
//		public static final String HTTP_OverviewCustom = "/overview/custom-progress";
//		/**
//		 * 时光机
//		 */
//		public static final String HTTP_Record = "/timeline/records";
//		public static final String HTTP_Record_Offset = "/timeline/date-offset";
//		public static final String HTTP_BackGround = "/timeline/get-cover";
//		public static final String HTTP_SetBackGround = "/timeline/cover";
//		public static final String Limit = "limit";
//		public static final String Offset = "offset";
//
//		public static final String HTTP_Review = "/info/review-list";
//		public static final String HTTP_Banner = "/banner/list";
//		public static final String HTTP_ToolSave = "/tool/save-data";
//		//11 表示宫缩，12 表示胎动，13 表示奶量，14 表示大便，15表示计步，16 表示 bmi，17 表示疫苗
//		public static final String ToolSave_RecordType = "record_type";
//
//		public static final String ToolSave_StartTime = "start_time";
//		public static final String ToolSave_Duration = "duration";		//持续时间
//		public static final String ToolSave_Interval = "interval";		//间隔
//		public static final String ToolSave_Space = "space";
//		public static final String ToolSave_Count = "count";
//		public static final String ToolSave_RecordTime= "record_time";
//		public static final String ToolSave_Ml = "ml";
//		public static final String Height = "height";
//		public static final String Weight = "weight";
//		public static final String ToolSave_BMI = "bmi";
//		public static final String HTTP_ToolHistory = "/tool/histories";
//		public static final String HTTP_ChangeBorn = "/user/change-born-stage";
//		public static final String HTTP_ChangeExpect= "/user/change-expected-date";
//		public static final String BirthDate = "birth_date";
//		public static final String ExpectDate = "expected_date";
//		public static final String HTTP_ChangeBabyInfo = "/child/save-info";
//		public static final String HTTP_ChangeName = "/user/change-name";
//		public static final String Name = "name";
//		public static final String HTTP_ChangeHead = "/user/change-head";
//		public static final String Head = "head";
//		public static final String HTTP_ChangeCity = "/user/change-city";
//		public static final String City = "city";
//		public static final String HTTP_ChangeHospital = "/user/change-hospital";
//		public static final String Hospital = "hospital";
//		public static final String HTTP_MedalInfo = "/user/medal-info";
//		public static final String HTTP_Interaction = "/notify/interaction";
//		public static final String HTTP_ToComplete = "/notify/to-complete";
//
//		public static final String HTTP_GetInviteCode= "/user/get-invite-code";
//
//		public static final String HTTP_CheckUpdate = "/common/init";
//
//		public static final String HTTP_SAVEIMAGE = "/work/save-images";
//		public static final String HTTP_TIMELINE_BG = "/timeline/cover";
//		public static final String HTTP_SAVEMOREIMAGES = "/private-photo/upload";
//		public static final String HTTP_GETIMAGETAGS = "/work/get-sys-image-tag";
//
//
//		public static final String HTTP_GET_FOOD_LIST = "/food/list";
//		public static final String HTTP_GET_FOOD_DETATIL = "/food/one";
//		public static final String HTTP_GET_FOOD_SEARCH = "/food/search";
//		public static final String HTTP_GET_FOOD_TYPE_LIST = "/food/types";
//		public static final String HTTP_BANNER_LIST = "/banner/list";
//
//		public static final String HTTP_WORK_RECOMMEND_LIST ="/work/recommend-list";
//		public static final String HTTP_WORK_FOLLOW_LIST ="/work/follow-list";
//		public static final String HTTP_WORK_DETAIL ="/work/detail";
//		public static final String HTTP_COMMENT_ADD ="/comment/add";
//		public static final String HTTP_COMMENT_LIST ="/comment/list";
//		public static final String HTTP_LIKE_USER_LIST ="/work/like-user-list";
//
//
//		public static final String HTTP_WORK_LIST = "/work/list";
//		public static final String HTTP_LIKE_WORK_LIST = "/work/like-work-list";
//		public static final String HTTP_TAG_WORK = "/work/user-tag-works";
//		public static final String HTTP_WORK_REPORT = "/report/report-work";
//		public static final String HTTP_WORK_LIKE = "/work/like";
//
//
//		public static final String HTTP_TAG_LIST = "/tag/get-hot-tags";
//		public static final String HTTP_TAG_LIST2 = "/tag/recommend-hot-tags";
//		public static final String HTTP_TAG_DETAILS = "/tag/detail";
//		public static final String HTTP_TAG_RANK = "/tag/get-tag-rank";
//		public static final String HTTP_FOLLOW_TAG="/user/follow-tag";
//		public static final String HTTP_UNFOLLOW_TAG="/user/unfollow-tag";
//		public static final String HTTP_TAG_STAR="/work/tag-star-works";
//		public static final String HTTP_TAG_LIKE="/tag/like-tag";
//		public static final String HTTP_USER_TAG="/tag/get-user-tags";
//
//		public static final String HTTP_USER_FOLLOW = "/user/follow-list";
//		public static final String HTTP_USER_FANS = "/user/fans-list";
//		public static final String HTTP_USER_INFO = "/user/info";
//		public static final String HTTP_USER_SNS_INFO = "/user/sns-info";
//		public static final String HTTP_FOLLOW_USER = "/user/follow-user";
//		public static final String HTTP_UNFOLLOW_USER = "/user/unfollow-user";
//		public static final String HTTP_PHOTO_COUNT = "/private-photo/count";
//		public static final String HTTP_PHOTO_LIST = "/private-photo/list";
//		public static final String HTTP_PHOTO_UPLOAD = "/private-photo/upload";
//
//		public static final String HTTP_RECOMMEND_LIST = "/user/recommend-list";
//		public static final String HTTP_RECOMMEND_AGE_LIST= "/user/age-recommend-list";
//
//		public static final String HTTP_CARD_LIST = "/card/card-list";
//		public static final String HTTP_CARD_DETAIL= "/card/card-detail";
//		public static final String HTTP_CARD_PHASE = "/card/recommend-phase";
//
//
//		public static final String HTTP_SCORE_LIST = "/score/history-list";								// 用户积分明细列表
//		public static final String HTTP_REDPACKAGE_LIST = "/shop/red-package/list"; 					// 用户红包列表
//		public static final String HTTP_REDPACKAGE_LIST_ALL = "/shop/red-package/list-all"; 				// 用户红包列表个人中心
//		public static final String HTTP_REDPACKAGE_LIST_SKU = "/shop/red-package/list-bysku"; 				// 用户红包列表sku支付
//		public static final String HTTP_NOTIFY_COUNT = "/notify/count";            						// 用户消息数量
//		public static final String HTTP_NOTIFY_DETAIL_COUNT = "/notify/detail-count"; 					// 获取社区和物流消息数量
//		public static final String HTTP_NOTIFY_LIST = "/notify/list";               					// 用户消息列表
//		public static final String HTTP_NOTIFY_DELETE = "/notify/delete";								// 用户消息删除
//		public static final String HTTP_CONSIGNEE_ADDRESS_ADD = "/shop/consignee-address/add"; 			// 添加收件人
//		public static final String HTTP_CONSIGNEE_ADDRESS_LIST = "/shop/consignee-address/list";		// 收件人列表
//		public static final String HTTP_CONSIGNEE_ADDRESS_UPDATE = "/shop/consignee-address/update";	// 修改收件人
//		public static final String HTTP_CONSIGNEE_ADDRESS_DELETE = "/shop/consignee-address/delete";	// 删除收件人
//		public static final String HTTP_SHOP_ORDER_LIST = "/shop/order/order-list";						// 查询付款状态接口
//		public static final String HTTP_SHOP_ORDER_DETAIL = "/shop/order/detail";						// 获取某一订单详情
//		public static final String HTTP_CAMPAIGN_DETAIL= "/campaign/campaign-detail";      				// 活动标签详情
//		public static final String HTTP_HOT_CATEGORY_FIRST_SECOND = "/shop/hot-category/first-second";  // 获取一二级热门分类
//		public static final String HTTP_HOT_CATEGORY_THRID = "/shop/hot-category/third-list";			// 获取三级热门分类
//		public static final String HTTP_WORK_USED_GOODSES = "/work/used-goodses";						// 发布搭配 过去使用过的商品
//
//		//新增四期接口，jerryzhang,2015-09-17
//		public static final String HTTP_HOT_BRANDS = "/shop/brand/hot-brand";       			// 获取搜索页面热门品牌
//		public static final String HTTP_HOT_BRANDS_LIST = "/shop/brand/hot-list";   			// 获取热门品牌列表
//		public static final String HTTP_HOT_BRANDS_SORT ="/shop/brand/char-list";   			// 按照名称排序品牌
//		public static final String HTTP_BRANDS_DETAILS = "/shop/brand/detail";      			// 品牌详情
////		public static final String HTTP_ACTIVITY_LIST = "/brand/activity-list";     			// 品牌活动详情
//		public static final String HTTP_HOT_GOODS = "/shop/brand/hot-goods";        			// 热门单品
//		public static final String HTTP_NEW_LIST = "/shop/brand/new-goods";						// 最新上架
//		public static final String HTTP_HOT_HEAD_MALL = "/shop/site/hot-list";     				// 所搜页面热门商城
//		public static final String HTTP_HOT_MALL_SORT = "/shop/site/list";        				// 热门商城排序
//		public static final String HTTP_FIRST_HOT_CLASSS = "/shop/hot-category/top-list";		// 获取一级热门分类
//		public static final String HTTP_SEROND_HOT_CLASS = "/shop/hot-category/list";			// 获取二级热门分类
//		public static final String HTTP_CLASS_DETAILS_LIST = "/shop/goods/category-list";		// 获取某一类型商品列表
//		public static final String HTTP_MALL_LIST = "/goods/site-option";						// 获取某一商城选项
//		public static final String HTTP_MALL_DETAILS = "/goods/site-goods";						// 获取某一商城详情
//		public static final String HTTP_GOODS_DETAILS = "/shop/goods/goods-detail";				// 获取商品详情
//		public static final String HTTP_SKU_NEW = "/goods/check-update";						// 获取是否需要更行SKU
//		public static final String HTTP_BOOK_LIST = "/shop/menu/menu-list";						// 获取菜单列表
//		public static final String HTTP_SHOP_MENU_ACTIVITY = "/shop/menu/menu-activity";		// 获取某一菜单下活动列表
//		public static final String HTTP_THEME_LIST = "/shop/thematic/thematic-list";			// 获取专题列表
//		public static final String HTTP_THEME_ACTIVITY_LIST = "/shop/thematic/thematic-cover";	// 获取某一专题活动列表
//		public static final String HTTP_ACTIVITY_DETAILS = "/shop/thematic/thematic-cover";		// 获取某一活动详情
//		public static final String HTTP_ACTIVITY_LIST = "/shop/brand/activity-list";			// 某一品牌活动列表
//		public static final String HTTP_OFFICIAL_TAG = "/tag/search";							// 搜索官方标签
//		public static final String HTTP_TAG_PRODUCCE_LIST = "/work/tag-star-works";             // 某一标签下的推荐列表
//        // 新增四期接口，chenjian 2015-09-25
//        public static final String HTTP_GET_SHOPPINGCART = "/shop/shopping-cart/calculate";     // 获取购物车列表
//        public static final String HTTP_SET_SHOPPINGCART = "/shop/shopping-cart/settle";        // 设置购物车结算列表到服务器
//        public static final String HTTP_PAY_SHOPPINGCART = "/shop/order/submit-order";          // 将订单列表提交到服务器
//        public static final String HTTP_PAY_SHOPPINGCART_TO_PING = "/pingpp/create-charge";     // 提交订到到Ping++平台
//        public static final String HTTP_ORDER_STATUS = "/shop/order/order-pay-status";          // 查询支付订单状态
//        public static final String HTTP_MALL_MENU_LIST = "/shop/menu/list";                     // 获取商城首页顶部菜单列表
//		public static final String HTTP_ORDER_LIST = "/shop/order/order-list";                  // 获取订单列表
//		public static final String HTTP_ORDER_DETAIL = "/shop/order/detail";                    // 获取订单列表
//		public static final String HTTP_PULL_LAYER = "/user/layer";                             // 获取首页弹层图片
//		public static final String HTTP_RECORD_VISIT_GOODS = "/shop/goods/visit-goods";         // 获取订单列表
//        public static final String HTTP_GET_TAG_BRAND = "/shop/brand/tag-brand";                // 根据Tag获取品牌
//        public static final String MALL_DETAIL_GOODS = "/shop/goods/menu-category-goods";       // 获取从首页进入某一种类商品
//        public static final String MALL_LIMIT_OPTION = "/shop/goods/menu-category-option";      // 获取商品筛选条件(从首页进入)
//        public static final String CLASSIFY_DETAIL_GOODS = "/shop/goods/category-goods";        // 获取某一种类商品(从分类二级菜单进入)
//        public static final String CLASSIFY_LIMIT_OPTION = "/shop/goods/category-option";       // 获取商品筛选条件(从分类二级菜单进入)
//        public static final String HOTGOODS_DETAIL_GOODS = "/shop/goods/site-goods";            // 获取某一种类商品(从热门商城进入)
//        public static final String HOTGOODS_LIMIT_OPTION = "/shop/goods/site-option";           // 获取商品筛选条件(从热门商城进入)
//        public static final String SHOPPING_VISIT_GOODS = "/shop/goods/visit-goods";            // 获取购物车历史浏览记录
//        public static final String APP_VERSION_INFO = "/common/app-version-info";               // 获取版本信息
//        public static final String APP_START_IMAGE = "/common/app-screen-image-info";           // 获取app启动图片
//        public static final String HTML_INFO = "/common/hybrid-html-info";                      // 获取html的版本信息
//        public static final String WX_LOGIN = "/weixin/common/app-login";                       // 微信登陆
//        public static final String ORDER_DETAIL = "/shop/order/detail";                         // 订单详情
//        public static final String RED_PACKAGE_URL = "/shop/red-package/share-red-package-url"; // 获取分享红包地址
//        public static final String GET_PAYMODE_LIST = "/pay/other-list";				        // 获取支付方式列表
//        public static final String GO_CMB_PAY = "/cmb-pay/create-charge";				        // 招行支付
//		public static final String Main_BANNER = "/shop/menu/menu-activity-thematic-chain";     // 首页轮播图
//		public static final String Main_FLASH_SALE = "/shop/flash-sale/list";                   // 首页闪购
//		public static final String Main_ACT_IMAGE = "/common/activity-image";                   // 首页活动图片
//		public static final String Main_Thematic_List = "/shop/thematic/thematic-list";         // 首页优惠活动
//		public static final String Main_DaliyRec_List = "/shop/recommended-daily/list";         // 首页每日推荐
//		public static final String Main_BrandsRec_List = "/shop/brand-recommend/list";          // 首页品牌推荐
//		public static final String Main_Car_Count = "/shop/shopping-cart/count";                // 首页购物车数量
//		public static final String Login_Out = "/user/logout";                                  // 退出登陆
//
//		// jerryzhang 四期新增接口
//		public static final String CHEAP_GOODS = "/shop/brand/cheap-goods";                     // 获取价格最低商品
//		public static final String DISCOUNT_GOODS = "/shop/brand/discount-goods";               // 获取折扣最低商品
//		public static final String MENU_CATEGORY_LIST = "/shop/menu/category-list";				// 获取某一菜单下分类列表
//		public static final String SHOP_GOODS_BRAND_GOODS = "/shop/goods/brand-goods";		    // 获取商品筛选条件（从品牌进入）
//		public static final String SHOP_GOODS_BRAND_OPTION = "/shop/goods/brand-option";		// 获取某一品牌下过滤选项
//
//		// jerryzhang 四期新增接口 2015-12-07
//		public static final String GOODS_BOUGHT = "/shop/order/order-goodses";					// 获取用户购买的商品列表
//		public static final String GOODS_PROFILE = "/shop/goods/goods-profile";					// 获取某一商品的概要信息
//		public static final String SHOPPING_CAR_COMMIT = "/shop/shopping-cart/add";				// 把商品添加到购物车
//		public static final String SHOPPING_CAR_DELETE = "/shop/shopping-cart/delete";			// 删除购物车中的商品
//		public static final String SHOPPING_CAR_UPDATA = "/shop/shopping-cart/update";			// 修改购物车中的商品
//		public static final String SHOPPING_CAR_INFO = "/shop/shopping-cart/info";				// 获取购物车列表概要信息 （传H5用）
//        public static final String SYNC_SHOPPINGCART = "/shop/shopping-cart/syn";			    // 同步服务器购物车数据
//
//		// jerryzhang 四期新增接口 2015-12-24
//		public static final String BRAND_REGISTER_LIST = "/shop/brand/reg-char-list";			// 获取注册界面热门品牌
//		public static final String HOT_CATEGORY_REGISTER_LIST = "/shop/hot-category/reg-list";	// 获取注册界面热门品类
//		public static final String FOLLOW_BRANDS_CATEGORIES = "/user/follow-brands-categorys";  // 批量关注品牌品类
//		public static final String REGISTER_FOLLOW_SKIP = "/user/skip-reg-follow";				// 跳过注册关注
//		public static final String SEARCH_HOT_WORDS = "/shop/search/hot-words";					// 获得热门搜索列表
//		public static final String SHOP_SEARCH_DATA = "/shop/search/search-data";				// 获取最新的搜索数据
//		public static final String SEARCH_GOODS = "/shop/goods/search-goods"; 					// 搜索之后的商品列表
//		public static final String SEARCH_OPTION = "/shop/goods/search-option";					// 搜索之后的商品的过滤选项
//		public static final String APP_RECOMMEND = "/activity/app-recommend/app-list";			// 获取推荐App列表
//
//		//闪购接口 tomyang 2016-1-6
//		public static final String HTTP_FLASH_SALE_SET_NOTICE = "/shop/flash-sale/set-notice";  // 设置某一闪购活动的开抢提醒
//		public static final String HTTP_FLASH_SALE_LIST = "/shop/flash-sale/list";        		// 获取闪购活动列表
//
//
//		// 兑换码
//		public static final String EXCHANGE_CODE = "/shop/exchange-code/exchange";				// 兑换兑换码的接口
//		public static final String EXCHANGE_CODE_RECORD = "/shop/exchange-code/exchange-list";  // 兑换记录
//
//		public static final String THEMATIC_ACTIVITY = "/shop/thematic/thematic-activity" ;		// 获取某一专题下活动列表
//		public static final String ACTIVITY_GOODS = "/shop/shop-activity/goodses-list" ;		// 获取某一活动商品列表
//		public static final String FLASH_SALE = "/shop/flash-sale/goodses-list";				// 获取某一闪购商品列表
//		public static final String ACTIVITY_DETAIL = "/shop/shop-activity/detail";				// 获取某一活动详情
//		public static final String FLASH_SALE_DETAIL = "/shop/flash-sale/detail";				// 获取某一闪购活动详情
//
//		//修改订阅
//		public static final String FOLLOW_TAGS = "/user/follow-tags" ;							// 批量关注品牌
//		public static final String UNFOLLOW_TAGS = "/user/unfollow-tags";						// 批量取消关注品牌
//		public static final String FOLLOW_CATEGORYS = "/user/follow-categorys";					// 批量关注品类
//		public static final String UNFOLLOW_CATEGORYS = "/user/unfollow-categorys";				// 批量取消品类关注
//		public static final String ORDER_LIST = "/shop/order/order-list";						// 某一状态下订单列表
//		public static final String SHOP_ORDER_TO_CAR = "/shop/order/to-cart";					// 订单商品加回购物车
//		public static final String SYSTEM_SKIN = "/common/system-skin";							// 获取当前系统默认皮肤
//
//		public static final String SEARCH_DATA = "/shop/search/search-datav2";                  // 获取最新的搜索数据
//
//		public static final String FLASH_SALE_NOTICE_LIST = "/shop/flash-sale/notice-list";     // 用户获取闪购提醒活动列表
//
//		// 旅游导航相关接口
//		public static final String NAVI_BANNER_LIST = "/banner/navi-list ";						// 获取导航的banner信息
//		public static final String NAVI_COUNTRY_LIST = "/navi/country/list";                    // 获取国家列表
//		public static final String SHOP_LINE_LIST = "/navi/shop-line/list";						// 获取热门线路列表
//
//		public static final String CITY_LIST = "/navi/city/list";								// 获取某一国家下的城市列表
//		public static final String COUNTRY_RECOMMEND_INFO = "/navi/country/recommend-info";		// 获取单个国家的必买推荐信息
//		public static final String NEWS_LIST_OF_COUNTRY = "/navi/news/list-ofcountry";			// 获取某一国家的资讯信息列表
//		public static final String TAX_REBATE_OF_COUNTRY = "/navi/country/tax-rebate-info";		// 获取单个国家退税信息
//		public static final String COUNTRY_BRIEF_INFO = "/navi/country/brief-info";				// 获取单个国家简要信息
//		public static final String COUNTRY_DETAIL_INFO = "/navi/country/detail-info";			// 获取单个国家详细信息
//
//		public static final String CITY_BRIEF_INFO = "/navi/city/brief-info";					// 获取单个城市简要信息
//		public static final String CITY_DETAIL_INFO = "/navi/city/detail-info";					// 获取单个城市详细信息
//		public static final String MALL_LIST_OF_CITY = "/navi/mall/list-ofcity";                // 获取某一城市下的商场列表
//		public static final String STORE_LIST_OF_CITY = "/navi/store/list-ofcity";				// 获取某一城市下的店铺列表
//		public static final String NEWS_LIST_OF_CITY = "/navi/news/list-ofcity";				// 获取某一城市的资讯信息列表
//		public static final String NEWS_DETAIL_INFO = "/navi/news/detail-info";                 // 获取单个资讯的详细信息
//		public static final String COUPON_LIST_OF_CITY = "/navi/coupon/list-ofcity";			// 获取某一城市下的coupon列表
//
//		public static final String MALL_DETAIL_INFO = "/navi/mall/detail-info";					// 获取单个商场详细信息
//		public static final String STORE_LIST_OF_MALL = "/navi/store/list-ofmall";				// 获取某一商场下的店铺列表
//		public static final String STORE_BRANDS_CATEGORY_OF_MALL = "/navi/store/brands-categorys";    // 获取某一商场下店铺的品牌与分类列表
//		public static final String MALL_RECOMMEND_INFO = "/navi/mall/recommend-info";			// 获取单个商场下的推荐信息
//		public static final String COUPON_LIST_OF_MALL = "/navi/coupon/list-ofmall";			// 获取某一商场下的coupon列表
//		public static final String STORE_LIST_OF_SAME = "/navi/store/list-ofsame";				// 获取某一店铺下的分店列表
//		public static final String STORE_LIST_OF_AROUND = "/navi/store/list-ofaround";			// 获取某一店铺附近的店铺列表
//		public static final String STORE_DETAIL_INFO = "/navi/store/detail-info";				// 获取单个店铺详细信息
//		public static final String COUPON_LIST_OF_STORE = "/navi/coupon/list-ofstore";			// 获取某一店铺下的coupon列表
//		public static final String STORE_RECOMMEND_INFO = "/navi/store/recommend-info";	 		// 获取单个店铺下的推荐信息
//
//		public static final String COUPON_DETAIL_INFO = "/navi/coupon/detail-info";				// 获取coupon详细信息
//		public static final String COUPON_COLLECT = "/navi/coupon/collect";						// 收藏某一coupon卷
//		public static final String COUPON_UNCOLLECT = "/navi/coupon/un-collect";				// 取消收藏某一coupon卷
//		public static final String COUPON_LIST_OF_USER = "/navi/coupon/list-ofuser";			// 获取收藏的coupon列表
//		public static final String STORE_BRANDS_CATEGORY_OF_CITY = "/navi/store/brands-categorys";   // 获取某一城市下店铺的品牌与分类列表
//
//		public static final String OFF_LINE_LIST = "/navi/off-line/list"; 						// 获取支持离线的城市列表
//		public static final String OFF_LINE_DOWNLOAD = "/navi/off-line/download";				// 获取单个城市的离线包
//
//		public static final String MALL_LIMIT_BRAND = "/shop/goods/menu-category-brand";        // 获取商品筛选条件(从首页进入) (可以不用拉取品牌)
//		public static final String CLASSIFY_LIMIT_BRAND = "/shop/goods/category-brand";       	// 获取商品筛选条件(从分类二级菜单进入) (可以不用拉取品牌)
//		public static final String HOTGOODS_LIMIT_BRAND = "/shop/goods/site-brand";             // 获取商品筛选条件(从热门商城进入) (可以不用拉取品牌)
//		public static final String SEARCH_BRAND = "/shop/goods/search-brand";					// 搜索之后的商品的过滤选项  (可以不用拉取品牌)
//
//		//Amy   四期商品相关  2016-6-20
//		public static final String HOT_CATEGORY_FIRST="/shop/hot-category/top-list";            //获取一级热门分类
//		public static final String HOT_CATEGORY_SECOND_THIRD="/shop/hot-category/second-third";      //获取二三级热门分类
//		public static final String GET_RECOMMEND_GOODS="/shop/goods/recommend-goods";              //获取某一商品下的推荐列表
//	}
}
