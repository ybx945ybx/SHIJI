package cn.yiya.shiji.business;

public class HttpRequest {

//    private static HttpRequest mInstance;
//    private ExecutorService pool;
//
//    private JsonPraser jsonPraser;
//    private static final int LIMIT = 10;
//    private static final int LIMITSIX = 6;
//    private static final int LIMITTHREE = 3;
//
//    public static final String DETAIL = "detail";
//    public static final String CITY = "city";
//    public static final String MALL = "mall";
//    public static final String STORE = "store";
//
//    private static final int LITTLELIMIT = 3;
//    private static final int MIDDLELIMIT = 6;
//
//    private HttpRequest() {
//        pool = Executors.newFixedThreadPool(5);
//        jsonPraser = JsonPraser.getInstance();
//    }
//
//    public static HttpRequest getInstance() {
//        if (mInstance == null) {
//            mInstance = new HttpRequest();
//        }
//        return mInstance;
//    }
//
//    public void shutDown() {
//        if (pool != null) {
//            pool.shutdownNow();
//        }
//    }
//
//
//    public class MyCallBack implements HttpRunnable.CallBack {
//        private MsgCallBack callBack;
//        private Handler mHandler;
//        private int praseType = 0;
//
//        public MyCallBack(MsgCallBack callBack, int praseType, Handler handler) {
//            super();
//            this.callBack = callBack;
//            this.praseType = praseType;
//            mHandler = handler;
//        }
//
//        @SuppressWarnings("unchecked")
//        @Override
//        public void onResult(String result) {
//
//            // 无网络状态的判断
//            if (TextUtils.isEmpty(result)) {
//                Context mContext = BaseApplication.getInstance().getApplicationContext();
//                final HttpMessage msg = new HttpMessage();
//
//                if (!NetUtil.NetAvailable(mContext)) {
//                    msg.code = StatusCode.NoNetWork;
//                    msg.obj = "当前没有连接网络,请检查网络设置" + "(" + msg.code + ")";
//                } else if (!NetworkUtils.isConnectInternet(mContext)) {
//                    msg.code = StatusCode.NoInternet;
//                    msg.obj = "当前网络无法连接" + "(" + msg.code + ")";
//                } else {
//                    msg.code = StatusCode.UnKnow;
//                    msg.obj = "网络未知错误连接" + "(" + msg.code + ")";
//                }
//
//                if (callBack != null) {
//                    if (mHandler != null) {
//                        mHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                callBack.onResult(msg);
//                            }
//                        });
//
//                        return;
//                    }
//                }
//                // 有数据显示判断
//            } else {
//                HttpMessage msg = jsonPraser.getHttpMessage(result);
//                if (msg.code == 101) {
//                    msg.obj = "账号信息已过期，请重新登陆！" + "(" + msg.code + ")";
//                } else if (TextUtils.isEmpty(msg.data)) {
//                    msg.code = StatusCode.NoData;
//                    msg.obj = "服务器无数据显示" + "(" + msg.code + ")";
//                }
//                if (msg.isSuccess() && praseType > 0 && msg.data != null) {
//                    //次级解析
//                    Object obj = jsonPraser.prase(praseType, msg.data.toString());
//                    msg.obj = obj;
//                }
//                if (callBack != null) {
//                    if (mHandler != null) {
//                        final HttpMessage fmsg = msg;
//                        mHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                callBack.onResult(fmsg);
//                            }
//                        });
//                    } else {
//                        callBack.onResult(msg);
//                    }
//                }
//            }
//        }
//    }
//
//
////    /**
////     * 登陆
////     *
////     * @param callBack
////     */
//    public void login(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_LOGIN, handler);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_Login;
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
////
////    /**
////     * 检查是否登陆
////     *
////     * @param callBack
////     */
//    public void isLogin(Handler handler, MsgCallBack callBack) {
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_ISLOGIN, handler);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_IsLogin;
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_POST, mCallBack));
//    }
////
////
//    /**
//     * 获取验证码
//     *
//     * @param callBack
//     */
//    public void getVerifyCode(String phone, Handler handler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put(Configration.HttpString.GetVerifycode_Mobile, phone);
//        maps.put("mode", "register");
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_UNDO, handler);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_GetVerifycode;
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//
//    /**
//     * 注册
//     *
//     * @param callBack
//     */
//    public void regist(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_REGIST, handler);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_Register;
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 注册保存个人性别信息
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void saveGenderInfo(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_SubmitGender;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_UNDO, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 注册保存个人信息
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void saveInfo(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_SaveInfo;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_UNDO, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 获取个人信息
//     *
//     * @param callBack
//     */
//    public void getUserInfo(Handler handler, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_GetInfo;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_GETINFO, handler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 获取七牛上传token
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void getQiNiuToken(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_QiNiuUploadToken;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_QINIU_TOKEN, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 获取七牛私有下载连接
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void getQiNiuDownloadUrl(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_QiNiuDownLoad;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_QINIU_URL, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 上传个推CID
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void uploadCid(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_UploadCID;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_UNDO, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 一周官方任务
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void getWeekTask(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_WeekTask;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_WEEK_TASK, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 一周自定义任务
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void getWeekCustomTask(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_WeekCustomTask;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_CUSTOM_WEEK_TASK, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 特殊任务
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void getSpecialTask(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_SPECIAL_TASK;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_SPECIAL_TASK, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 完成任务
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void chekTask(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_Check;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_CHECK_TASK, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 完成特殊任务
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void chekSpecialTask(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_CheckSpecialTask;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_CHECK_SPECIAL_TASK, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 新增自定义任务
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void addCustomTask(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_AddCustom;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_ADD_CUSTOM_TASK, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 获取官方任务总览
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void getOverview(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_Overview;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_OVERVIEW, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 获取自定义任务列表总览
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void getCustomOverview(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_OverviewCustom;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_CUSTOM_OVERVIEW, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 获取时光机offset
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void getRecordOffset(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_Record_Offset;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_RECORD_OFFSET, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 获取用户商城积分
//     */
//    public void getScore(int offset, MsgCallBack callBack, Handler handler) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put(Configration.HttpString.Offset, offset);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_SCORE_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_SCORE, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 获取用户商城红包
//     */
//    public void getRedPackage(MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_REDPACKAGE_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_REDPACKAGE, handler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, mCallBack));
//    }
////
////    /**
////     * 获取用户消息数量
////     */
//    public void getNotifyCount(MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_NOTIFY_COUNT;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_NOTIFY, handler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, mCallBack));
//    }
////
//    /**
//     * 获取社区和物流消息数量
//     */
//    public void getNotifyDetailCount(MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_NOTIFY_DETAIL_COUNT;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_NOTIFY_DETAIL, handler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 用户消息列表
//     */
//    public void getNotifyList(int type, int offset, Handler handler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put(Configration.HttpString.TYPE, type);
//        maps.put(Configration.HttpString.Offset, offset);
//        maps.put(Configration.HttpString.Limit, 20);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_NOTIFY_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_NOTIFY_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 用户消息删除
//     */
//    public void delateNotify(int id, Handler handler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", id);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_NOTIFY_DELETE;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_NOTIFY_DELETE, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 添加收件人
//     */
//    public void addAddress(MsgCallBack callBack, String json, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_CONSIGNEE_ADDRESS_ADD;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_CONSIGNEE_ADDRESS_ADD, handler);
//        pool.execute(new HttpRunnable(url, json, mCallBack));
//    }
//
//    /**
//     * 获取收件人列表
//     */
//    public void addressList(Handler handler, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_CONSIGNEE_ADDRESS_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_CONSIGNEE_ADDRESS_LIST, handler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, mCallBack));
//    }
////
////    /**
////     * 修改收件人
////     */
//    public void updateAddress(MsgCallBack callBack, AddressListItem info, Handler handler) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", info.getId());
//        maps.put("mobile", info.getMobile());
//        maps.put("recipient", info.getRecipient());
//        maps.put("post_code", info.getPost_code());
//        maps.put("province", info.getProvince());
//        maps.put("city", info.getCity());
//        maps.put("district", info.getDistrict());
//        maps.put("address", info.getAddress());
//        maps.put("identity_number", info.getIdentity_number());
//        maps.put("identity_copy_front", info.getIdentity_copy_front());
//        maps.put("identity_copy_back", info.getIdentity_copy_back());
//        String url = Configration.SERVER + Configration.HttpString.HTTP_CONSIGNEE_ADDRESS_UPDATE;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_CONSIGNEE_ADDRESS_UPDATE, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
////
//    /**
//     * 删除收件人
//     */
//    public void deleteAddress(MsgCallBack callBack, int id, Handler handler) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", id);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_CONSIGNEE_ADDRESS_DELETE;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_CONSIGNEE_ADDRESS_DELETE, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 查询付款状态接口
//     */
//    public void shopOrderList(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_SHOP_ORDER_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_SHOP_ORDER_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 获取某一订单详情
//     */
//    public void shopOrderDetail(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_SHOP_ORDER_DETAIL;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_SHOP_ORDER_DETAIL, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 获取活动标签详情
//     */
//    public void getActTagDetail(int tag_id, Handler handler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("tag_id", tag_id);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_CAMPAIGN_DETAIL;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_CAMPAIGN_DETAIL, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//
//    /**
//     * 四期新增接口
//     * 获取购物车列表
//     * chenjian-----2015-09-25
//     */
//    public void getShoppingCartList(MsgCallBack callBack, String json, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_GET_SHOPPINGCART;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_SHOPPLING_LIST, handler);
//        pool.execute(new HttpRunnable(url, json, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * 获取购物车列表
//     * jerryzhang-----2015-12-11
//     */
//    public void getServerShoppingCartList(MsgCallBack callBack, String source, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_GET_SHOPPINGCART;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_SHOPPLING_LIST, handler);
//        pool.execute(new HttpRunnable(url, source, mCallBack));
//    }
////
////    /**
////     * 四期新增接口
////     * 设置购物车结算列表到服务器
////     * chenjian-----2015-09-28
////     */
//    public void setShoppingCartList(MsgCallBack callBack, String json, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_SET_SHOPPINGCART;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_SHOPPLING_LIST, handler);
//        pool.execute(new HttpRunnable(url, json, mCallBack));
//    }
////
////    /**
////     * 四期新增接口
////     * 提交订单到服务器
////     * chenjian-----2015-09-29
////     */
//    public void PayShoppingCartList(MsgCallBack callBack, String json, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_PAY_SHOPPINGCART;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_SHOPPLING_ORDERNO, handler);
//        pool.execute(new HttpRunnable(url, json, mCallBack));
//    }
////
////    /**
////     * 四期新增接口
////     * 提交订单到p++支付平台
////     * chenjian-----2015-10-12
////     */
//    public void PayShoppingCartListToPing(MsgCallBack callBack, String orderNo, String channel, Handler handler) {
//        Map<String, Object> orderMaps = new HashMap<>();
//        orderMaps.put("order_no", orderNo);
//        orderMaps.put("channel", channel);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_PAY_SHOPPINGCART_TO_PING;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_SHOPPLING_PING, handler);
//        pool.execute(new HttpRunnable(url, orderMaps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    public void QueryOrderStatus(MsgCallBack callBack, String orderNo, Handler handler) {
//        Map<String, Object> orderMaps = new HashMap<>();
//        orderMaps.put("orderNumber", orderNo);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ORDER_STATUS;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_STATUS_ORDERNO, handler);
//        pool.execute(new HttpRunnable(url, orderMaps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 获取复习
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void getReview(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_Review;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_REVIEW, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 获取banner
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void getBannerList(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_Banner;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_BANNER, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 保存工具数据 v
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void saveToolData(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ToolSave;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_TOOLS_SAVE, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 拉取工具记录
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void getToolHistory(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ToolHistory;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_TOOLS_HISTORY, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 计步器
//     *
//     * @param callBack
//     * @param maps
//     * @param handler
//     */
//    public void getToolStepHistory(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ToolHistory;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_TOOLS_STEP_HISTORY, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * BMI记录
//     *
//     * @param callBack
//     * @param maps
//     * @param handler
//     */
//    public void getToolBMIHistory(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ToolHistory;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_TOOLS_BMI_HISTORY, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 宫缩
//     *
//     * @param callBack
//     * @param maps
//     * @param handler
//     */
//    public void getToolConstrationHistory(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ToolHistory;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_TOOLS_CONSTRATION_HISTORY, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 胎动
//     *
//     * @param callBack
//     * @param maps
//     * @param handler
//     */
//    public void getToolFetalHistory(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ToolHistory;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_TOOLS_FETAL_HISTORY, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 喂奶记录
//     *
//     * @param callBack
//     * @param maps
//     * @param handler
//     */
//    public void getToolMilkHistory(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ToolHistory;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_TOOLS_MILK_HISTORY, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 大便记录
//     *
//     * @param callBack
//     * @param maps
//     * @param handler
//     */
//    public void getToolShitHistory(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ToolHistory;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_TOOLS_SHIT_HISTORY, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 更改到出生
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void changeToBorn(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ChangeBorn;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_CHANGE_TO_BORN, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 更改预产期
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void changeExpectDate(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ChangeExpect;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_CHANGE_EXPECT, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 更改用户头像
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void changeHead(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ChangeHead;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_CHANGE_HEAD, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 更改时光机背景图
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void changeBack(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_SetBackGround;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_SET_TIMEIMAGE, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 更改城市
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void changeCity(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ChangeCity;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_CHANGE_CITY, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 更改医院
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void changeHospital(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ChangeHospital;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_CHANGE_HOSPITAL, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 更改用户昵称
//     *
//     * @param callBack
//     * @param
//     */
//    public void changeName(MsgCallBack callBack, String newName, Handler handler) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put(Configration.HttpString.Name, newName);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ChangeName;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_CHANGE_NAME, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 更改用户头像
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void getMedalInfo(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_MedalInfo;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_MEDAL_INFO, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 发送互动表情
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void interaction(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_Interaction;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_INTERACTION, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 通知同伴完成任务
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void notifyPartner(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ToComplete;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_NOTIFY_PARTNER, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 获取邀请码
//     *
//     * @param callBack
//     */
//    public void getInviteCode(MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_GetInviteCode;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_INVITECODE, handler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 修改宝宝信息
//     *
//     * @param callBack
//     */
//    public void saveChildInfo(Map<String, Object> maps, MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ChangeBabyInfo;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_SAVE_BABY_INFO, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    public void checkUpdate(Map<String, Object> maps, MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_CheckUpdate;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_CHECK_UPDATE, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//    public void PayShoppingCartListToPing(MsgCallBack callBack, String orderNo, String channel, Handler handler) {
//        Map<String, Object> orderMaps = new HashMap<>();
//        orderMaps.put("order_no", orderNo);
//        orderMaps.put("channel", channel);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_PAY_SHOPPINGCART_TO_PING;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_SHOPPLING_PING, handler);
//        pool.execute(new HttpRunnable(url, orderMaps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    public void QueryOrderStatus(MsgCallBack callBack, String orderNo, Handler handler) {
//        Map<String, Object> orderMaps = new HashMap<>();
//        orderMaps.put("orderNumber", orderNo);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ORDER_STATUS;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_STATUS_ORDERNO, handler);
//        pool.execute(new HttpRunnable(url, orderMaps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 获取复习
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void getReview(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_Review;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_REVIEW, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 获取banner
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void getBannerList(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_Banner;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_BANNER, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 保存工具数据 v
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void saveToolData(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ToolSave;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_TOOLS_SAVE, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 拉取工具记录
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void getToolHistory(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ToolHistory;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_TOOLS_HISTORY, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 计步器
//     *
//     * @param callBack
//     * @param maps
//     * @param handler
//     */
//    public void getToolStepHistory(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ToolHistory;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_TOOLS_STEP_HISTORY, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * BMI记录
//     *
//     * @param callBack
//     * @param maps
//     * @param handler
//     */
//    public void getToolBMIHistory(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ToolHistory;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_TOOLS_BMI_HISTORY, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 宫缩
//     *
//     * @param callBack
//     * @param maps
//     * @param handler
//     */
//    public void getToolConstrationHistory(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ToolHistory;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_TOOLS_CONSTRATION_HISTORY, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 胎动
//     *
//     * @param callBack
//     * @param maps
//     * @param handler
//     */
//    public void getToolFetalHistory(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ToolHistory;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_TOOLS_FETAL_HISTORY, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 喂奶记录
//     *
//     * @param callBack
//     * @param maps
//     * @param handler
//     */
//    public void getToolMilkHistory(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ToolHistory;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_TOOLS_MILK_HISTORY, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 大便记录
//     *
//     * @param callBack
//     * @param maps
//     * @param handler
//     */
//    public void getToolShitHistory(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ToolHistory;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_TOOLS_SHIT_HISTORY, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 更改到出生
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void changeToBorn(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ChangeBorn;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_CHANGE_TO_BORN, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 更改预产期
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void changeExpectDate(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ChangeExpect;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_CHANGE_EXPECT, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 更改用户头像
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void changeHead(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ChangeHead;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_CHANGE_HEAD, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 更改时光机背景图
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void changeBack(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_SetBackGround;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_SET_TIMEIMAGE, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 更改城市
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void changeCity(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ChangeCity;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_CHANGE_CITY, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 更改医院
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void changeHospital(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ChangeHospital;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_CHANGE_HOSPITAL, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 更改用户昵称
//     *
//     * @param callBack
//     * @param
//     */
//    public void changeName(MsgCallBack callBack, String newName, Handler handler) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put(Configration.HttpString.Name, newName);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ChangeName;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_CHANGE_NAME, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 更改用户头像
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void getMedalInfo(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_MedalInfo;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_MEDAL_INFO, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 发送互动表情
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void interaction(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_Interaction;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_INTERACTION, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 通知同伴完成任务
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void notifyPartner(MsgCallBack callBack, Map<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ToComplete;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_NOTIFY_PARTNER, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    /**
//     * 获取邀请码
//     *
//     * @param callBack
//     */
//    public void getInviteCode(MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_GetInviteCode;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_INVITECODE, handler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 修改宝宝信息
//     *
//     * @param callBack
//     */
//    public void saveChildInfo(Map<String, Object> maps, MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ChangeBabyInfo;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_SAVE_BABY_INFO, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    public void checkUpdate(Map<String, Object> maps, MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_CheckUpdate;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_CHECK_UPDATE, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void publishOnePic(String json, MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_SAVEIMAGE;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_SAVA_IMAGE, handler);
//        pool.execute(new HttpRunnable(url, json, mCallBack));
//    }
//
//    public void publishTimeLineBg(String json, MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_TIMELINE_BG;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_SAVA_IMAGE, handler);
//        pool.execute(new HttpRunnable(url, json, mCallBack));
//    }
//
//    public void publishImages(String json, MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_SAVEMOREIMAGES;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_SAVA_IMAGES, handler);
//        pool.execute(new HttpRunnable(url, json, mCallBack));
//    }
//
//    public void getImageTagsByType(MsgCallBack callBack, Handler handler) {
//        HashMap<String,Object> map = new HashMap<>();
//        map.put(Configration.HttpString.TYPE, 1);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_GETIMAGETAGS;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_GET_IMAGE_TAGS, handler);
//        pool.execute(new HttpRunnable(url, map, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    public void getFoodTypes(Map<String, Object> maps, MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_GET_FOOD_TYPE_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_GET_FOOD_TYPE_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void getTimeBack(MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_BackGround;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_GET_TIMEIMAGE, handler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void getFoodList(Map<String, Object> maps, MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_GET_FOOD_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_GET_FOOD_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void getFoodDetailt(Map<String, Object> maps, MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_GET_FOOD_DETATIL;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_GET_FOOD_DETAIL, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void searchFoodList(Map<String, Object> maps, MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_GET_FOOD_SEARCH;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_GET_FOOD_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void getHotTag(MsgCallBack callBack, HashMap<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_TAG_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TAG_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void getHotTag2(MsgCallBack callBack, HashMap<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_TAG_LIST2;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TAG_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void bannerList(Handler handler, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_BANNER_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_BANNER_LIST, handler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void recommendList(Handler handler, HashMap<String, Object> maps, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_WORK_RECOMMEND_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_WORK_RECOMMEND_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void followList(Handler handler, HashMap<String, Object> maps, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_WORK_FOLLOW_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_WORK_FOLLOW_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**获取推荐搭配列表
//     * @param handler
//     * @param maps
//     * @param callBack
//     */
//    public void matchRecommendList(Handler handler, HashMap<String, Object> maps, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_WORK_RECOMMEND_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_WORK_RECOMMEND_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//    /**获取关注搭配列表
//     * @param handler
//     * @param maps
//     * @param callBack
//     */
//    public void matchFollowList(Handler handler, HashMap<String, Object> maps, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_WORK_FOLLOW_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_WORK_FOLLOW_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void details(Handler handler, HashMap<String, Object> maps, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_WORK_DETAIL;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_WORK_DETAIL, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**搭配详情
//     * @param handler
//     * @param maps
//     * @param callBack
//     */
//    public void matchDetails(Handler handler, HashMap<String, Object> maps, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_WORK_DETAIL;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_WORK_DETAIL, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void addComment(Handler handler, HashMap<String, Object> maps, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_COMMENT_ADD;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_COMMENT_ADD, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    public void commnetList(Handler handler, HashMap<String, Object> maps, MsgCallBack callBack ) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_COMMENT_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_COMMENT_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void workList(Handler handler, HashMap<String, Object> maps, boolean isLike, MsgCallBack callBack) {
//        String url;
//        if(!isLike){
//            url = Configration.SERVER + Configration.HttpString.HTTP_WORK_LIST;
//        }else{
//            url = Configration.SERVER + Configration.HttpString.HTTP_LIKE_WORK_LIST;
//        }
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_WORK_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void likedUser(Handler handler, HashMap<String, Object> maps, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_LIKE_USER_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_LIKE_USER_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//
//    public void report(MsgCallBack callBack, HashMap<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_WORK_REPORT;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_WORK_REPORT, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    public void like(Handler handler, HashMap<String, Object> maps, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_WORK_LIKE;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_WORK_LIKE, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    public void tagDetails(MsgCallBack callBack, HashMap<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_TAG_DETAILS;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TAG_DETAILS, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void tagRank(MsgCallBack callBack, HashMap<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_TAG_RANK;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TAG_RANK, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void tagFollow(MsgCallBack callBack, HashMap<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_FOLLOW_TAG;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_FOLLOW_TAG, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void tagUnFollow(MsgCallBack callBack, HashMap<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_UNFOLLOW_TAG;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_UNFOLLOW_TAG, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void tagStar(MsgCallBack callBack, HashMap<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_TAG_STAR;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TAG_STAR, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void tagLike(MsgCallBack callBack, HashMap<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_TAG_LIKE;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TAG_LIKE, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void userTag(int user_id, int offset, Handler handler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("user_id", user_id);
//        maps.put(Configration.HttpString.Offset, offset);
//        maps.put(Configration.HttpString.Limit, LIMIT);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_USER_TAG;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_USER_TAG, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//
//    public void userInfo(Handler handler, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_USER_INFO;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_USER_INFO, handler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void userSnsInfo(int user_id, Handler handler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("user_id", user_id);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_USER_SNS_INFO;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_USER_SNS_INFO, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void userFollow(Handler handler, int user_id, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("user_id", user_id);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_FOLLOW_USER;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_FOLLOW_USER, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void userUnFollow(Handler handler, int user_id, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("user_id", user_id);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_UNFOLLOW_USER;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_UNFOLLOW_USER, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void userFollowing(int user_id, int offset, Handler handler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("user_id", user_id);
//        maps.put(Configration.HttpString.Offset, offset);
//        maps.put(Configration.HttpString.Limit, LIMIT);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_USER_FOLLOW;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_USER_FOLLOW, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void userFans(int user_id, int offset, Handler handler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("user_id", user_id);
//        maps.put(Configration.HttpString.Offset, offset);
//        maps.put(Configration.HttpString.Limit, LIMIT);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_USER_FANS;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_USER_FANS, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void photoCount(MsgCallBack callBack, HashMap<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_PHOTO_COUNT;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_PHOTO_COUNT, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void photoList(MsgCallBack callBack, HashMap<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_PHOTO_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_PHOTO_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void photoUpload(MsgCallBack callBack, HashMap<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_PHOTO_UPLOAD;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_PHOTO_UPLOAD, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    public void rememberUserList(int offset, Handler handler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put(Configration.HttpString.Offset, offset);
//        maps.put(Configration.HttpString.Limit, LIMIT);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_RECOMMEND_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_RECOMMEND_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void rememberAgeUserList(int user_id, int offset, Handler handler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("user_id", user_id);
//        maps.put(Configration.HttpString.Offset, offset);
//        maps.put(Configration.HttpString.Limit, LIMIT);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_RECOMMEND_AGE_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_RECOMMEND_AGE_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void recommendPhase(MsgCallBack callBack, HashMap<String, Object> maps, Handler mHandler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_CARD_PHASE;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_CARD_PHASE, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void cardList(MsgCallBack callBack, HashMap<String, Object> maps, Handler mHandler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_CARD_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_CARD_LIST, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void cardDetail(MsgCallBack callBack, HashMap<String, Object> maps, Handler mHandler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_CARD_DETAIL;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_CARD_DETAIL, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-17
//     * 首页热门品牌
//     *
//     * @param callBack
//     */
//    public void brandsList(Handler mHandler, MsgCallBack callBack) {
//        HashMap<String,Object> maps = new HashMap<>();
//        maps.put(Configration.HttpString.Limit, LIMIT);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_HOT_BRANDS;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_HOT_BRANDS, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-17
//     * 热门品牌列表
//     *
//     * @param callBack
//     */
//    public void hotHrandsList(int offset,  Handler mHandler, MsgCallBack callBack) {
//        HashMap<String,Object> maps = new HashMap<>();
//        maps.put(Configration.HttpString.Offset, offset);
//        maps.put(Configration.HttpString.Limit, LIMIT);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_HOT_BRANDS_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_HOT_BRANDS_LIST, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-17
//     * 搜索页面热门商城
//     *
//     * @param callBack
//     */
//    public void hotMallCtrl(Handler mHandler, MsgCallBack callBack) {
//        HashMap<String,Object> maps = new HashMap<>();
//        maps.put(Configration.HttpString.Limit, LIMIT);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_HOT_HEAD_MALL;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_HOT_HEAD_MALL, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-17
//     * 热门单品
//     *
//     * @param callBack
//     */
//    public void hotSingleList(int brand_id, Handler mHandler, MsgCallBack callBack) {
//        Map<String, Object> orderMaps = new HashMap<>();
//        orderMaps.put("brand_id", brand_id);
//        orderMaps.put(Configration.HttpString.Limit, 10);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_HOT_GOODS;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_HOT_GOODS, mHandler);
//        pool.execute(new HttpRunnable(url, orderMaps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-18
//     * 最新上架
//     *
//     * @param callBack
//     */
//    public void newGoodsList(int brand_id, Handler mHandler, int offset, int limit, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("brand_id", brand_id);
//        maps.put(Configration.HttpString.Offset, offset);
//        maps.put(Configration.HttpString.Limit, limit);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_NEW_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_NEW_LIST, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void newGoodsList(int brand_id, Handler mHandler, MsgCallBack callBack) {
//        newGoodsList(brand_id, mHandler, 0, 5, callBack);
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-17
//     * 热门品牌排序
//     *
//     * @param callBack
//     */
//    public void brandsSortCtrl(int offset, Handler mHandler, MsgCallBack callBack) {
//        HashMap<String,Object> maps = new HashMap<>();
//        maps.put(Configration.HttpString.Offset, offset);
//        maps.put(Configration.HttpString.Limit, LIMIT);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_HOT_BRANDS_SORT;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_HOT_BRANDS_SORT, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * chenjian，2015-10-17
//     * 获取某一品牌头部详情
//     *
//     * @param callBack
//     */
//    public void getBrandsHeaderDetail(MsgCallBack callBack, int brand_id, Handler mHandler) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("brand_id", brand_id);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_BRANDS_DETAILS;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_BRANDS_DETAILS, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-17
//     * 热门商城排序
//     *
//     * @param callBack
//     */
//    public void hotMallSortCtrl(Handler mHandler, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_HOT_MALL_SORT;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_HOT_MALL_SORT, mHandler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-19
//     * 一级热门分类
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void hotClassCtrl(MsgCallBack callBack, HashMap<String, Object> maps, Handler mHandler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_FIRST_HOT_CLASSS;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_FIRST_HOT_CLASSS, mHandler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-19
//     * 二级热门分类
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void secomdClassCtrl(MsgCallBack callBack, HashMap<String, Object> maps, Handler mHandler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_SEROND_HOT_CLASS;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_SEROND_HOT_CLASS, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-21
//     * 单子类详情
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void singleClassList(MsgCallBack callBack, HashMap<String, Object> maps, Handler mHandler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_CLASS_DETAILS_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_CLASS_DETAILS_LIST, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-22
//     * 某一品牌活动列表
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void bannerActivityList(MsgCallBack callBack, HashMap<String, Object> maps, Handler mHandler) {
////		String url = Configration.SERVER+Configration.HttpString.HTTP_BRAND_ACTIVITY_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_BRAND_ACTIVITY_LIST, mHandler);
////		pool.execute(new HttpRunnable(url,maps,HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-22
//     * 某一品牌详情背景
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void backgroundlist(MsgCallBack callBack, HashMap<String, Object> maps, Handler mHandler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_BRANDS_DETAILS;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_BRANDS_DETAILS, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-23
//     * 搜索官方标签
//     *
//     * @param callBack
//     */
//    public void officialTagsList(MsgCallBack callBack, String tag, Handler mHandler) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("tag", tag);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_OFFICIAL_TAG;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_OFFICIAL_TAG, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * chenjian，2015-10-28
//     * 搜索官方标签
//     *
//     * @param callBack
//     */
//    public void getTagBrand(int tagId, Handler mHandler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("tagId", tagId);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_GET_TAG_BRAND;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_TAG_BRAND, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-23
//     * 某一标签下的推荐列表
//     *
//     * @param callBack
//     */
//    public void tagProduceList(MsgCallBack callBack, int tag_id, Handler mHandler, int offset) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("tag_id", tag_id);
//        maps.put(Configration.HttpString.Offset, offset);
//        maps.put(Configration.HttpString.Limit, 10);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_TAG_PRODUCCE_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TAG_PRODUCCE_LIST, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口，
//     * chenjian， 2015-10-13
//     * 获取商城首页顶部标签菜单
//     */
//    public void getMallHOmeMenuList(MsgCallBack callBack, Handler mHandler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_MALL_MENU_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_MALL_MENU_LIST, mHandler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口，
//     * chenjian， 2015-10-13
//     * 获取商城首页顶部标签菜单
//     */
//    public void getOrderList(MsgCallBack callBack, String status, String limit, Handler mHandler) {
//
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("status", status);
//        maps.put("limit", limit);
//
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ORDER_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_ORDER_LIST, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口，
//     * chenjian， 2015-10-13
//     * 获取商城首页顶部标签菜单
//     */
//    public void getOrderDetail(MsgCallBack callBack, String orderId, Handler mHandler) {
//
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("orderNumber", orderId);
//
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ORDER_DETAIL;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_ORDER_DETAIL, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口，
//     * chenjian， 2015-10-13
//     * 获取首页弹层图片
//     */
//    public void pullLayer(Handler mHandler, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_PULL_LAYER;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_PULL_LAYER, mHandler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口，
//     * chenjian， 2015-10-13
//     * 获取商城首页顶部标签菜单
//     */
//    public void recordVisitGoods(MsgCallBack callBack, String goodsId, Handler mHandler) {
//
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("goodsId", goodsId);
//
//        String url = Configration.SERVER + Configration.HttpString.HTTP_RECORD_VISIT_GOODS;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_RECORD_VISIT_GOODS, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    public static final int MALL_HOME = 1001;                // 表示从首页跳转进来
//    public static final int SECONDE_TYPE = 1002;             // 表示从商城二级分类进来
//    public static final int HOT_GOODS = 1003;                // 表示从热门商城进来
//
//    /**
//     * 四期新增接口，
//     * chenjian， 2015-10-29
//     * 获取某一商城详细列表数据，具体筛选数据 1001-->首页进入 1002-->二级分类进入 1003-->热门商城进入
//     */
//    public void getMallHomeDetailOrderGoods(int type, int offset, ParaObject object, Handler mHandler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", object.getId());
//        maps.put("brand_ids", object.getBrand_ids());
//        maps.put("sort_id", object.getSort_id());
//        maps.put("genders", object.getGenders());
//        maps.put("price_ranges_id", object.getPrice_ranges_id());
//        maps.put("count", object.getCount());
//        maps.put("category_ids", object.getCategory_ids());
//        maps.put("size", object.getSize());
//        maps.put("color", object.getColor());
//        maps.put(Configration.HttpString.Offset, offset);
//        maps.put(Configration.HttpString.Limit, 40);
//        String url = "";
//        switch (type) {
//            case MALL_HOME:
//                url = Configration.SERVER + Configration.HttpString.MALL_DETAIL_GOODS;
//                break;
//            case SECONDE_TYPE:
//                url = Configration.SERVER + Configration.HttpString.CLASSIFY_DETAIL_GOODS;
//                break;
//            case HOT_GOODS:
//                url = Configration.SERVER + Configration.HttpString.HOTGOODS_DETAIL_GOODS;
//                break;
//        }
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.MALL_HOME_DETAIL_GOODS, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口，
//     * chenjian， 2015-10-29
//     * 获取某一商城详细列表数据，获取筛选条件 1001-->首页进入 1002-->二级分类进入 1003-->热门商城进入
//     */
//    public void getMallHomeLimitOption(int type, HashMap<String, Object> maps, Handler mHandler, MsgCallBack callBack) {
//        String url = "";
//        switch (type) {
//            case MALL_HOME:
//                url = Configration.SERVER + Configration.HttpString.MALL_LIMIT_OPTION;
//                break;
//            case SECONDE_TYPE:
//                url = Configration.SERVER + Configration.HttpString.CLASSIFY_LIMIT_OPTION;
//                break;
//            case HOT_GOODS:
//                url = Configration.SERVER + Configration.HttpString.HOTGOODS_LIMIT_OPTION;
//                break;
//        }
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.MALL_LIMIT_OPTION, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口，
//     * chenjian， 2015-10-29
//     * 获取历史浏览商品
//     */
//    public void getVisitGoods(Handler mHandler, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.SHOPPING_VISIT_GOODS;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.MALL_HOME_DETAIL_GOODS, mHandler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增功能接口
//     * Jerryzhang   2015-11-09
//     * 获取价格最低商品
//     */
//    public void getCheapGoods(int brand_id, Handler mHandler, int limit, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<String, Object>();
//        maps.put("brand_id", brand_id);
//        maps.put("limit", limit);
//        String url = Configration.SERVER + Configration.HttpString.CHEAP_GOODS;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.HTTP_CHEAP_GOODS, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang   2015-11-09
//     * 获取折扣最多商品
//     */
//    public void getDiscountGoods(int brand_id, Handler mHandler, int limit, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("brand_id", brand_id);
//        maps.put("limit", limit);
//        String url = Configration.SERVER + Configration.HttpString.DISCOUNT_GOODS;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.HTTP_DISCOUNT_GOODS, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang 2015-11-09
//     * 获取某一品牌活动列表
//     */
//    public void getActivityList(int id, Handler mHandler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", id);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ACTIVITY_LIST;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.HTTP_ACTIVITY_LIST, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang 2015-11-09
//     * 获取菜单下某一分类商品列表
//     * <p>
//     * id	是	编号
//     * limit	是	拉取数据条数
//     * offset	是	偏移
//     * brand_ids	否	多个用逗号连接
//     * price	否	asc,desc,或者空
//     * category_ids	否	多个用逗号连接
//     * sort_id	否	排序方式，单选。1：默认排序,2：价格由高到低，3：价格由低到高，4：折扣由多到少，5：最热
//     * price_ranges_id	否	价格范围，单选。
//     */
////	public void getMenuCategoryGoods(MsgCallBack callBack,Handler mHandler,int id,int limit,int offset,String brand_ids,String price,
////									 String category_ids,int sort_id,String price_ranges_id){
//    public void getShopGoodsBrandOption(ParaObject object, Handler mHandler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", object.getId());
//        maps.put("limit", object.getLimit());
//        maps.put("offset", object.getOffset());
//        maps.put("brand_ids", object.getBrand_ids());
//        maps.put("genders", object.getGenders());
//        maps.put("category_ids", object.getCategory_ids());
//        maps.put("sort_id", object.getSort_id());
//        maps.put("count", object.getCount());
//        maps.put("price_ranges_id", object.getPrice_ranges_id());
//        maps.put("size", object.getSize());
//        maps.put("color", object.getColor());
//        String url = Configration.SERVER + Configration.HttpString.SHOP_GOODS_BRAND_GOODS;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.SHOP_GOODS_BRAND_GOODS, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 新增四期接口
//     * jerryzhang 2015-11-11
//     * 获取某一菜单下分类列表
//     */
//    public void getMenuCategoryList(MsgCallBack callBack, Handler mHandler, int id) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", id);
//        String url = Configration.SERVER + Configration.HttpString.MENU_CATEGORY_LIST;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.MENU_CATEGORY_LIST, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 新增四期接口
//     * jerryzhang 2015-11-11
//     * 获取某一品牌下过滤选项
//     * id 	品牌id brandId
//     * 全部人群，全部分类，全部品牌，价格区间，排序方式
//     */
//    public void getShopGoodsBrandOption(int id, Handler mHandler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", id);
//        String url = Configration.SERVER + Configration.HttpString.SHOP_GOODS_BRAND_OPTION;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.SHOP_GOODS_BRAND_OPTION, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 新增四期接口
//     * chenjian 2015-11-12
//     * 获取服务器App版本信息
//     */
//    public void getVersionInfo(Handler mHandler, int code, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("code", code);
//        String url = Configration.SERVER + Configration.HttpString.APP_VERSION_INFO;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.APP_VERSION_INFO, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 新增四期接口
//     * chenjian 2015-11-12
//     * 获取服务器App启动图片
//     */
//    public void getAppStartImage(Handler mHandler, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.APP_START_IMAGE;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.APP_START_IMAGE, mHandler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 新增四期接口
//     * jerryzhang 2015-12-07
//     * 接口名：获取用户购买的商品列表
//     * 参数 limit ,offset ,sub_order_num 为空的时候：返回所有购买的商品，不为空的时候，返回指定订单的商品.
//     * 返回值：购买记录商品列表
//     */
//    public void getGoodsBoughtList(Handler mHandler, int limit, int offset, String sub_order_num, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("limit", limit);
//        maps.put("offset", offset);
//        maps.put("sub_order_num", sub_order_num);
//        String url = Configration.SERVER + Configration.HttpString.GOODS_BOUGHT;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.GOODS_BOUGHT, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    public void getGoodsBoughtList(Handler mHandler, int limit, int offset, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("limit", limit);
//        maps.put("offset", offset);
//        String url = Configration.SERVER + Configration.HttpString.GOODS_BOUGHT;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.GOODS_BOUGHT, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 新增四期接口
//     * jerryzhang 2015-12-08
//     * 接口名：获取某一商品的概要信息
//     * 参数：goodsId
//     * 返回值：goods列表
//     */
//    public void getGoodsProfileList(Handler mHandler, String goodsId, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("goodsId", goodsId);
//        String url = Configration.SERVER + Configration.HttpString.GOODS_PROFILE;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.GOODS_PROFILE, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 新增四期接口
//     * jerryzhang 2015-12-10
//     * 接口名：把商品添加到购物车
//     * 参数：format": “base64”,    "data": "
//     * 返回值：
//     */
//    public void commitShoppingCarData(String format, String data, Handler mHandler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("format", format);
//        maps.put("data", data);
//        String url = Configration.SERVER + Configration.HttpString.SHOPPING_CAR_COMMIT;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.SHOPPING_CAR_COMMIT, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, myCallBack));
//    }
//
//    // 同步服务器购物车数据
//    public void syncShoppingCarData(String format, String data, Handler mHandler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("format", format);
//        maps.put("data", data);
//        String url = Configration.SERVER + Configration.HttpString.SYNC_SHOPPINGCART;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.SHOPPING_CAR_COMMIT, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, myCallBack));
//    }
//
////
////    /**
////     * 四期新增接口
////     * Jerryzhang 2015-12-11
////     * 接口名：删除购物车中的商品
////     * 参数:ids 购物车id，多个用逗号连接
////     * 返回值; data  购物车数据
////     */
////    public void shoppingCarDelete(Handler mhandler, String ids, MsgCallBack callBack) {
////        String url = Configration.SERVER + Configration.HttpString.SHOPPING_CAR_DELETE;
////        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.SHOPPING_CAR_DELETE, mhandler);
////        pool.execute(new HttpRunnable(url, ids, mCallBack));
////    }
////
////    /**
////     * 四期新增接口
////     * Jerryzhang 2015-12-11
////     * 接口名：修改购物车中的商品
////     * 参数: id, num
////     * 返回值; data  购物车数据
////     */
////    public void shoppingCarUpdata(Handler mhandler, String json, MsgCallBack callBack) {
////        String url = Configration.SERVER + Configration.HttpString.SHOPPING_CAR_UPDATA;
////        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.SHOPPING_CAR_UPDATA, mhandler);
////        pool.execute(new HttpRunnable(url, json, myCallBack));
////    }
////
////    /**
////     * 四期新增接口
////     * Jerryzhang 2015-12-17
////     * 接口名：
////     * 参数:
////     * 返回值; data
////     */
////    public void getShoppingCarInfo(Handler mhandler, MsgCallBack callBack) {
////        String url = Configration.SERVER + Configration.HttpString.SHOPPING_CAR_INFO;
////        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.SHOPPING_CAR_INFO, mhandler);
////        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, myCallBack));
////    }
////
////    /**
////     * 四期新增接口
////     * Jerryzhang 2015-12-24
////     * 接口名：获取注册页面热门品牌
////     * 参数: 无
////     * 返回值; list
////     */
////    public void getBrandsRegister(Handler mhandler, MsgCallBack callBack) {
////        String url = Configration.SERVER + Configration.HttpString.BRAND_REGISTER_LIST;
////        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.BRAND_REGISTER_LIST, mhandler);
////        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, myCallBack));
////    }
////
////    /**
////     * 四期新增接口
////     * Jerryzhang 2015-12-24
////     * 接口名：获取注册页面热门品类
////     * 参数: 无
////     * 返回值; list
////     */
////    public void getCategoriesRegister(Handler mhandler, MsgCallBack callBack) {
////        String url = Configration.SERVER + Configration.HttpString.HOT_CATEGORY_REGISTER_LIST;
////        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.HOT_CATEGORY_REGISTER_LIST, mhandler);
////        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, myCallBack));
////    }
////
////    /**
////     * 四期新增接口
////     * Jerryzhang 2015-12-24
////     * 接口名：批量关注品牌品类
////     * 参数: tag_list 标签id,多个用逗号隔开，category_list 类型id，多个用逗号隔开
////     * 返回值; data
////     */
////    public void postFolloeBrandsCategorise(Handler mhandler, String tag_list, String category_list, MsgCallBack callBack) {
////        HashMap<String, Object> maps = new HashMap<>();
////        maps.put("tag_list", tag_list);
////        maps.put("category_list", category_list);
////        String url = Configration.SERVER + Configration.HttpString.FOLLOW_BRANDS_CATEGORIES;
////        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.FOLLOW_BRANDS_CATEGORIES, mhandler);
////        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, myCallBack));
////    }
////
////    /**
////     * 四期新增接口
////     * Jerryzhang 2015-12-24
////     * 接口名：跳过注册关注
////     * 参数: 无
////     * 返回值; data
////     */
////    public void postRegisterFolloeSkip(Handler mhandler, MsgCallBack callBack) {
////        String url = Configration.SERVER + Configration.HttpString.REGISTER_FOLLOW_SKIP;
////        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.REGISTER_FOLLOW_SKIP, mhandler);
////        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_POST, myCallBack));
////    }
////
////    /**
////     * 四期新增接口
////     * Jerryzhang 2015-12-25
////     * 接口名：获取热门搜索
////     * 参数: 无
////     * 返回值; words数组
////     */
////    public void getSearchHotWords(Handler mhandler, MsgCallBack callBack) {
////        String url = Configration.SERVER + Configration.HttpString.SEARCH_HOT_WORDS;
////        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.SEARCH_HOT_WORDS, mhandler);
////        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, myCallBack));
////    }
////
////    /**
////     * 四期新增接口
////     * Jerryzhang 2015-12-25
////     * 接口名：获取最新的搜索数据
////     * 参数: stamp 传上次请求的stamp的参数，第一次传空
////     * 返回值; stamp参数字段，data数组
////     */
////    public void getShopSearchData(Handler mhandler, String stamp, MsgCallBack callBack) {
////        HashMap<String, Object> maps = new HashMap<>();
////        maps.put("stamp", stamp);
////        String url = Configration.SERVER + Configration.HttpString.SHOP_SEARCH_DATA;
////        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.SHOP_SEARCH_DATA, mhandler);
////        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
////    }
////
////    /**
////     * 四期新增接口
////     * chenjian 2015-12-30
////     * 接口名：
////     * 参数:
////     * 返回值; data
////     */
////    public void getHtmlFileInfo(Handler mhandler, int code, MsgCallBack callBack) {
////        HashMap<String, Object> maps = new HashMap<>();
////        maps.put("code", code);
////        String url = Configration.SERVER + Configration.HttpString.HTML_INFO;
////        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.HTML_INFO, mhandler);
////        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
////    }
////
////    /**
////     * <<<<<<< Updated upstream
////     * 四期新增接口
////     * Jerryzhang 2015-01-04
////     * 接口名：搜索之后的商品列表
////     * 参数: ParaObject
////     * 返回值; lsit
////     */
////    public void getSearchGoods(Handler mhandler, int offset, ParaObject object, MsgCallBack callBack) {
////        HashMap<String, Object> maps = new HashMap<>();
////        maps.put("id", object.getId());
////        maps.put("brand_ids", object.getBrand_ids());
////        maps.put("sort_id", object.getSort_id());
////        maps.put("genders", object.getGenders());
////        maps.put("price_ranges_id", object.getPrice_ranges_id());
////        maps.put("count", object.getCount());
////        maps.put("category_ids", object.getCategory_ids());
////        maps.put("word", object.getWord());
////        maps.put("size", object.getSize());
////        maps.put("color", object.getColor());
////        maps.put(Configration.HttpString.Offset, offset);
////        maps.put(Configration.HttpString.Limit, 40);
////        String url = Configration.SERVER + Configration.HttpString.SEARCH_GOODS;
////        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.SEARCH_GOODS, mhandler);
////        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
////    }
////
////    /**
////     * 四期新增接口
////     * Jerryzhang 2015-01-04
////     * 接口名：搜索之后的商品的过滤选项
////     * 参数: ParaObject
////     * 返回值; data 选项数组
////     */
////
////    /**
////     * 四期新增接口
////     * Jerryzhang 2015-01-07
////     * 接口名：获取推荐App列表
////     * 参数: 无
////     * 返回值; list
////     */
////    public void getAppRecommend(Handler mHandler, MsgCallBack callBack) {
////        String url = Configration.SERVER + Configration.HttpString.APP_RECOMMEND;
////        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.APP_RECOMMEND, mHandler);
////        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, myCallBack));
////    }
////
////    /**
////     * 闪购接口
////     * tomyang 2016-1-6
////     * 接口名：设置某一闪购活动的开抢提醒
////     * 参数：id：活动编号  notice：设置状态，1为需提醒，2为不需提醒
////     */
////    public void setFlashSaleNotice(String id, int notice, Handler mHandler, MsgCallBack callBack) {
////        HashMap<String, Object> maps = new HashMap<>();
////        maps.put("id", id);
////        maps.put("notice", notice);
////        String url = Configration.SERVER + Configration.HttpString.HTTP_FLASH_SALE_SET_NOTICE;
////        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.HTTP_FLASH_SALE_SET_NOTICE, mHandler);
////        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, myCallBack));
////    }
////
////    /**
////     * tomyang 2016-1-8
////     * 获取闪购活动列表
////     */
////    public void flashSaleList(MsgCallBack callBack, Handler mHandler) {
////        String url = Configration.SERVER + Configration.HttpString.HTTP_FLASH_SALE_LIST;
////        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.HTTP_FLASH_SALE_LIST, mHandler);
////        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, myCallBack));
////    }
////
////    /**
////     * 新增接口
////     * Jerryzhang 2015-01-20
////     * 接口名：兑换兑换码
////     * 参数: code 兑换码, r
////     * 返回值; msg 兑换码提示信息
////     */
////    public void getExchangeCode(Handler mHandler, String code, int r, MsgCallBack callBack) {
////        HashMap<String, Object> map = new HashMap<>();
////        map.put("code", code);
////        String url = Configration.SERVER + Configration.HttpString.EXCHANGE_CODE;
////        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.EXCHANGE_CODE, mHandler);
////        pool.execute(new HttpRunnable(url, map, HttpRunnable.HTTP_GET, myCallBack));
////    }
////
////    /**
////     * 新增接口
////     * Jerryzhang 2015-01-20
////     * 接口名：兑换历史
////     * 参数: offset , limit
////     * 返回值; list列表
////     */
////    public void getExchangeCodeRecord(Handler mHandler, int offset, int limit, MsgCallBack msgCallBack) {
////        HashMap<String, Object> map = new HashMap<>();
////        map.put("offset", offset);
////        map.put("limit", limit);
////        String url = Configration.SERVER + Configration.HttpString.EXCHANGE_CODE_RECORD;
////        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.EXCHANGE_CODE_RECORD, mHandler);
////        pool.execute(new HttpRunnable(url, map, HttpRunnable.HTTP_GET, myCallBack));
////    }
////
////    /**
////     * 新增接口
////     * Jerryzhang 2015-02-03
////     * 接口名：某一专题下活动列表
////     * 参数: id 专题编号
////     * 返回值; list列表
////     */
////    public void getThematicList(Handler handler, int id, MsgCallBack msgCallBack) {
////        HashMap<String, Object> map = new HashMap<>();
////        map.put("id", id);
////        String url = Configration.SERVER + Configration.HttpString.THEMATIC_ACTIVITY;
////        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.THEMATIC_ACTIVITY, handler);
////        pool.execute(new HttpRunnable(url, map, HttpRunnable.HTTP_GET, myCallBack));
////    }
////
////    /**
////     * 新增接口
////     * Tomyang 2015-03-21
////     * 接口名：某一菜单下活动列表
////     * 参数: id 专题编号
////     * 返回值; list列表
////     */
////    public void getMenuList(Handler handler, int id, MsgCallBack msgCallBack) {
////        HashMap<String, Object> map = new HashMap<>();
////        map.put("id", id);
////        String url = Configration.SERVER + Configration.HttpString.HTTP_SHOP_MENU_ACTIVITY;
////        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.TYPE_SHOP_MENU_ACTIVITY, handler);
////        pool.execute(new HttpRunnable(url, map, HttpRunnable.HTTP_GET, myCallBack));
////    }
////
////    /**
////     * 新增接口
////     * Jerryzhang 2015-02-03
////     * 接口名：某一活动商品列表
////     * 参数: id 专题编号， offset, limit
////     * 返回值; list列表
////     */
////    public void getActivityGoodsList(Handler handler, int id, int offset, int limit, MsgCallBack msgCallBack) {
////        HashMap<String, Object> map = new HashMap<>();
////        map.put("id", id);
////        map.put("offset", offset);
////        map.put("limit", limit);
////        String url = Configration.SERVER + Configration.HttpString.ACTIVITY_GOODS;
////        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.ACTIVITY_GOODS, handler);
////        pool.execute(new HttpRunnable(url, map, HttpRunnable.HTTP_GET, myCallBack));
////    }
////
//    public void publishOnePic(String json, MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_SAVEIMAGE;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_SAVA_IMAGE, handler);
//        pool.execute(new HttpRunnable(url, json, mCallBack));
//    }
////
//    public void publishTimeLineBg(String json, MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_TIMELINE_BG;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_SAVA_IMAGE, handler);
//        pool.execute(new HttpRunnable(url, json, mCallBack));
//    }
//
//    public void publishImages(String json, MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_SAVEMOREIMAGES;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_SAVA_IMAGES, handler);
//        pool.execute(new HttpRunnable(url, json, mCallBack));
//    }
//
//    public void getImageTagsByType(MsgCallBack callBack, Handler handler) {
//        HashMap<String,Object> map = new HashMap<>();
//        map.put(Configration.HttpString.TYPE, 1);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_GETIMAGETAGS;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_GET_IMAGE_TAGS, handler);
//        pool.execute(new HttpRunnable(url, map, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    public void getFoodTypes(Map<String, Object> maps, MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_GET_FOOD_TYPE_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_GET_FOOD_TYPE_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void getTimeBack(MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_BackGround;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_GET_TIMEIMAGE, handler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void getFoodList(Map<String, Object> maps, MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_GET_FOOD_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_GET_FOOD_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void getFoodDetailt(Map<String, Object> maps, MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_GET_FOOD_DETATIL;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_GET_FOOD_DETAIL, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void searchFoodList(Map<String, Object> maps, MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_GET_FOOD_SEARCH;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.TYPE_GET_FOOD_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void getHotTag(MsgCallBack callBack, HashMap<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_TAG_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TAG_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void getHotTag2(MsgCallBack callBack, HashMap<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_TAG_LIST2;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TAG_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void bannerList(Handler handler, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_BANNER_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_BANNER_LIST, handler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void recommendList(Handler handler, HashMap<String, Object> maps, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_WORK_RECOMMEND_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_WORK_RECOMMEND_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void followList(Handler handler, HashMap<String, Object> maps, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_WORK_FOLLOW_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_WORK_FOLLOW_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**获取推荐搭配列表
//     * @param handler
//     * @param maps
//     * @param callBack
//     */
//    public void matchRecommendList(Handler handler, HashMap<String, Object> maps, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_WORK_RECOMMEND_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_WORK_RECOMMEND_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//    /**获取关注搭配列表
//     * @param handler
//     * @param maps
//     * @param callBack
//     */
//    public void matchFollowList(Handler handler, HashMap<String, Object> maps, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_WORK_FOLLOW_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_WORK_FOLLOW_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void details(Handler handler, HashMap<String, Object> maps, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_WORK_DETAIL;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_WORK_DETAIL, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**搭配详情
//     * @param handler
//     * @param maps
//     * @param callBack
//     */
//    public void matchDetails(Handler handler, HashMap<String, Object> maps, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_WORK_DETAIL;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_WORK_DETAIL, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void addComment(Handler handler, HashMap<String, Object> maps, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_COMMENT_ADD;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_COMMENT_ADD, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    public void commnetList(Handler handler, HashMap<String, Object> maps, MsgCallBack callBack ) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_COMMENT_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_COMMENT_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void workList(Handler handler, HashMap<String, Object> maps, boolean isLike, MsgCallBack callBack) {
//        String url;
//        if(!isLike){
//            url = Configration.SERVER + Configration.HttpString.HTTP_WORK_LIST;
//        }else{
//            url = Configration.SERVER + Configration.HttpString.HTTP_LIKE_WORK_LIST;
//        }
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_WORK_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void likedUser(Handler handler, HashMap<String, Object> maps, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_LIKE_USER_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_LIKE_USER_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void userTagWorks(int user_id, int tag_id, int offset, Handler handler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("user_id", user_id);
//        maps.put("tag_id", tag_id);
//        maps.put(Configration.HttpString.Offset, offset);
//        maps.put(Configration.HttpString.Limit, LIMIT);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_TAG_WORK;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TAG_WORK, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void report(MsgCallBack callBack, HashMap<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_WORK_REPORT;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_WORK_REPORT, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    public void like(Handler handler, HashMap<String, Object> maps, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_WORK_LIKE;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_WORK_LIKE, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    public void tagDetails(MsgCallBack callBack, HashMap<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_TAG_DETAILS;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TAG_DETAILS, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void tagRank(MsgCallBack callBack, HashMap<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_TAG_RANK;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TAG_RANK, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void tagFollow(MsgCallBack callBack, HashMap<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_FOLLOW_TAG;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_FOLLOW_TAG, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void tagUnFollow(MsgCallBack callBack, HashMap<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_UNFOLLOW_TAG;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_UNFOLLOW_TAG, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void tagStar(MsgCallBack callBack, HashMap<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_TAG_STAR;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TAG_STAR, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void tagLike(MsgCallBack callBack, HashMap<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_TAG_LIKE;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TAG_LIKE, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void userTag(int user_id, int offset, Handler handler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("user_id", user_id);
//        maps.put(Configration.HttpString.Offset, offset);
//        maps.put(Configration.HttpString.Limit, LIMIT);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_USER_TAG;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_USER_TAG, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
////
////
//    public void userInfo(Handler handler, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_USER_INFO;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_USER_INFO, handler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, mCallBack));
//    }
////
//    public void userSnsInfo(int user_id, Handler handler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("user_id", user_id);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_USER_SNS_INFO;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_USER_SNS_INFO, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
////
//    public void userFollow(Handler handler, int user_id, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("user_id", user_id);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_FOLLOW_USER;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_FOLLOW_USER, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
////
//    public void userUnFollow(Handler handler, int user_id, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("user_id", user_id);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_UNFOLLOW_USER;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_UNFOLLOW_USER, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void userFollowing(int user_id, int offset, Handler handler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("user_id", user_id);
//        maps.put(Configration.HttpString.Offset, offset);
//        maps.put(Configration.HttpString.Limit, LIMIT);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_USER_FOLLOW;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_USER_FOLLOW, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void userFans(int user_id, int offset, Handler handler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("user_id", user_id);
//        maps.put(Configration.HttpString.Offset, offset);
//        maps.put(Configration.HttpString.Limit, LIMIT);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_USER_FANS;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_USER_FANS, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void photoCount(MsgCallBack callBack, HashMap<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_PHOTO_COUNT;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_PHOTO_COUNT, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void photoList(MsgCallBack callBack, HashMap<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_PHOTO_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_PHOTO_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void photoUpload(MsgCallBack callBack, HashMap<String, Object> maps, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_PHOTO_UPLOAD;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_PHOTO_UPLOAD, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
//
//    public void rememberUserList(int offset, Handler handler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put(Configration.HttpString.Offset, offset);
//        maps.put(Configration.HttpString.Limit, LIMIT);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_RECOMMEND_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_RECOMMEND_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void rememberAgeUserList(int user_id, int offset, Handler handler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("user_id", user_id);
//        maps.put(Configration.HttpString.Offset, offset);
//        maps.put(Configration.HttpString.Limit, LIMIT);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_RECOMMEND_AGE_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_RECOMMEND_AGE_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void recommendPhase(MsgCallBack callBack, HashMap<String, Object> maps, Handler mHandler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_CARD_PHASE;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_CARD_PHASE, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void cardList(MsgCallBack callBack, HashMap<String, Object> maps, Handler mHandler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_CARD_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_CARD_LIST, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    public void cardDetail(MsgCallBack callBack, HashMap<String, Object> maps, Handler mHandler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_CARD_DETAIL;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_CARD_DETAIL, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-17
//     * 首页热门品牌
//     *
//     * @param callBack
//     */
//    public void brandsList(Handler mHandler, MsgCallBack callBack) {
//        HashMap<String,Object> maps = new HashMap<>();
//        maps.put(Configration.HttpString.Limit, LIMIT);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_HOT_BRANDS;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_HOT_BRANDS, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-17
//     * 热门品牌列表
//     *
//     * @param callBack
//     */
//    public void hotHrandsList(int offset,  Handler mHandler, MsgCallBack callBack) {
//        HashMap<String,Object> maps = new HashMap<>();
//        maps.put(Configration.HttpString.Offset, offset);
//        maps.put(Configration.HttpString.Limit, LIMIT);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_HOT_BRANDS_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_HOT_BRANDS_LIST, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-17
//     * 搜索页面热门商城
//     *
//     * @param callBack
//     */
//    public void hotMallCtrl(Handler mHandler, MsgCallBack callBack) {
//        HashMap<String,Object> maps = new HashMap<>();
//        maps.put(Configration.HttpString.Limit, LIMIT);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_HOT_HEAD_MALL;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_HOT_HEAD_MALL, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
////    /**
////     * 四期新增接口
////     * jerryzhang，2015-09-17
////     * 热门单品
////     *
////     * @param callBack
////     */
//    public void hotSingleList(int brand_id, Handler mHandler, MsgCallBack callBack) {
//        Map<String, Object> orderMaps = new HashMap<>();
//        orderMaps.put("brand_id", brand_id);
//        orderMaps.put(Configration.HttpString.Limit, 10);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_HOT_GOODS;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_HOT_GOODS, mHandler);
//        pool.execute(new HttpRunnable(url, orderMaps, HttpRunnable.HTTP_GET, mCallBack));
//    }
////
////    /**
////     * 四期新增接口
////     * jerryzhang，2015-09-18
////     * 最新上架
////     *
////     * @param callBack
////     */
//    public void newGoodsList(int brand_id, Handler mHandler, int offset, int limit, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("brand_id", brand_id);
//        maps.put(Configration.HttpString.Offset, offset);
//        maps.put(Configration.HttpString.Limit, limit);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_NEW_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_NEW_LIST, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
////
//    public void newGoodsList(int brand_id, Handler mHandler, MsgCallBack callBack) {
//        newGoodsList(brand_id, mHandler, 0, 5, callBack);
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-17
//     * 热门品牌排序
//     *
//     * @param callBack
//     */
//    public void brandsSortCtrl(int offset, Handler mHandler, MsgCallBack callBack) {
//        HashMap<String,Object> maps = new HashMap<>();
//        maps.put(Configration.HttpString.Offset, offset);
//        maps.put(Configration.HttpString.Limit, LIMIT);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_HOT_BRANDS_SORT;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_HOT_BRANDS_SORT, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
////    /**
////     * 四期新增接口
////     * chenjian，2015-10-17
////     * 获取某一品牌头部详情
////     *
////     * @param callBack
////     */
//    public void getBrandsHeaderDetail(MsgCallBack callBack, int brand_id, Handler mHandler) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("brand_id", brand_id);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_BRANDS_DETAILS;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_BRANDS_DETAILS, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
////
////    /**
////     * 四期新增接口
////     * jerryzhang，2015-09-17
////     * 热门商城排序
////     *
////     * @param callBack
////     */
//    public void hotMallSortCtrl(Handler mHandler, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_HOT_MALL_SORT;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_HOT_MALL_SORT, mHandler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, mCallBack));
//    }
////
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-19
//     * 一级热门分类
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void hotClassCtrl(MsgCallBack callBack, HashMap<String, Object> maps, Handler mHandler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_FIRST_HOT_CLASSS;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_FIRST_HOT_CLASSS, mHandler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-19
//     * 二级热门分类
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void secomdClassCtrl(MsgCallBack callBack, HashMap<String, Object> maps, Handler mHandler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_SEROND_HOT_CLASS;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_SEROND_HOT_CLASS, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-21
//     * 单子类详情
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void singleClassList(MsgCallBack callBack, HashMap<String, Object> maps, Handler mHandler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_CLASS_DETAILS_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_CLASS_DETAILS_LIST, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-22
//     * 某一品牌活动列表
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void bannerActivityList(MsgCallBack callBack, HashMap<String, Object> maps, Handler mHandler) {
////		String url = Configration.SERVER+Configration.HttpString.HTTP_BRAND_ACTIVITY_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_BRAND_ACTIVITY_LIST, mHandler);
////		pool.execute(new HttpRunnable(url,maps,HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-22
//     * 某一品牌详情背景
//     *
//     * @param callBack
//     * @param maps
//     */
//    public void backgroundlist(MsgCallBack callBack, HashMap<String, Object> maps, Handler mHandler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_BRANDS_DETAILS;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_BRANDS_DETAILS, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-23
//     * 搜索官方标签
//     *
//     * @param callBack
//     */
//    public void officialTagsList(MsgCallBack callBack, String tag, Handler mHandler) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("tag", tag);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_OFFICIAL_TAG;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_OFFICIAL_TAG, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * chenjian，2015-10-28
//     * 搜索官方标签
//     *
//     * @param callBack
//     */
//    public void getTagBrand(int tagId, Handler mHandler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("tagId", tagId);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_GET_TAG_BRAND;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_TAG_BRAND, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * jerryzhang，2015-09-23
//     * 某一标签下的推荐列表
//     *
//     * @param callBack
//     */
//    public void tagProduceList(MsgCallBack callBack, int tag_id, Handler mHandler, int offset) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("tag_id", tag_id);
//        maps.put(Configration.HttpString.Offset, offset);
//        maps.put(Configration.HttpString.Limit, 10);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_TAG_PRODUCCE_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TAG_PRODUCCE_LIST, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口，
//     * chenjian， 2015-10-13
//     * 获取商城首页顶部标签菜单
//     */
//    public void getMallHOmeMenuList(MsgCallBack callBack, Handler mHandler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_MALL_MENU_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_MALL_MENU_LIST, mHandler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口，
//     * chenjian， 2015-10-13
//     * 获取商城首页顶部标签菜单
//     */
//    public void getOrderList(MsgCallBack callBack, String status, String limit, Handler mHandler) {
//
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("status", status);
//        maps.put("limit", limit);
//
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ORDER_LIST;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_ORDER_LIST, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
//    /**
//     * 四期新增接口，
//     * chenjian， 2015-10-13
//     * 获取商城首页顶部标签菜单
//     */
//    public void getOrderDetail(MsgCallBack callBack, String orderId, Handler mHandler) {
//
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("orderNumber", orderId);
//
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ORDER_DETAIL;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_ORDER_DETAIL, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
////    /**
////     * 四期新增接口，
////     * chenjian， 2015-10-13
////     * 获取首页弹层图片
////     */
//    public void pullLayer(Handler mHandler, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_PULL_LAYER;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_PULL_LAYER, mHandler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, mCallBack));
//    }
//
////    /**
////     * 四期新增接口，
////     * chenjian， 2015-10-13
////     * 获取商城首页顶部标签菜单
////     */
//    public void recordVisitGoods(MsgCallBack callBack, String goodsId, Handler mHandler) {
//
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("goodsId", goodsId);
//
//        String url = Configration.SERVER + Configration.HttpString.HTTP_RECORD_VISIT_GOODS;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_RECORD_VISIT_GOODS, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, mCallBack));
//    }
////
//    public static final int MALL_HOME = 1001;                // 表示从首页跳转进来
//    public static final int SECONDE_TYPE = 1002;             // 表示从商城二级分类进来
//    public static final int HOT_GOODS = 1003;                // 表示从热门商城进来
////
////    /**
////     * 四期新增接口，
////     * chenjian， 2015-10-29
////     * 获取某一商城详细列表数据，具体筛选数据 1001-->首页进入 1002-->二级分类进入 1003-->热门商城进入
////     */
//    public void getMallHomeDetailOrderGoods(int type, int offset, ParaObject object, Handler mHandler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", object.getId());
//        maps.put("brand_ids", object.getBrand_ids());
//        maps.put("sort_id", object.getSort_id());
//        maps.put("genders", object.getGenders());
//        maps.put("price_ranges_id", object.getPrice_ranges_id());
//        maps.put("count", object.getCount());
//        maps.put("category_ids", object.getCategory_ids());
//        maps.put("size", object.getSize());
//        maps.put("color", object.getColor());
//        maps.put(Configration.HttpString.Offset, offset);
//        maps.put(Configration.HttpString.Limit, 40);
//        String url = "";
//        switch (type) {
//            case MALL_HOME:
//                url = Configration.SERVER + Configration.HttpString.MALL_DETAIL_GOODS;
//                break;
//            case SECONDE_TYPE:
//                url = Configration.SERVER + Configration.HttpString.CLASSIFY_DETAIL_GOODS;
//                break;
//            case HOT_GOODS:
//                url = Configration.SERVER + Configration.HttpString.HOTGOODS_DETAIL_GOODS;
//                break;
//        }
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.MALL_HOME_DETAIL_GOODS, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
////
////    /**
////     * 四期新增接口，
////     * chenjian， 2015-10-29
////     * 获取某一商城详细列表数据，获取筛选条件 1001-->首页进入 1002-->二级分类进入 1003-->热门商城进入
////     */
//    public void getMallHomeLimitOption(int type, HashMap<String, Object> maps, Handler mHandler, MsgCallBack callBack) {
//        String url = "";
//        switch (type) {
//            case MALL_HOME:
//                url = Configration.SERVER + Configration.HttpString.MALL_LIMIT_OPTION;
//                break;
//            case SECONDE_TYPE:
//                url = Configration.SERVER + Configration.HttpString.CLASSIFY_LIMIT_OPTION;
//                break;
//            case HOT_GOODS:
//                url = Configration.SERVER + Configration.HttpString.HOTGOODS_LIMIT_OPTION;
//                break;
//        }
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.MALL_LIMIT_OPTION, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }
////
////    /**
////     * 四期新增接口，
////     * chenjian， 2015-10-29
////     * 获取历史浏览商品
////     */
//    public void getVisitGoods(Handler mHandler, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.SHOPPING_VISIT_GOODS;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.MALL_HOME_DETAIL_GOODS, mHandler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, mCallBack));
//    }
////
////    /**
////     * 四期新增功能接口
////     * Jerryzhang   2015-11-09
////     * 获取价格最低商品
////     */
//    public void getCheapGoods(int brand_id, Handler mHandler, int limit, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<String, Object>();
//        maps.put("brand_id", brand_id);
//        maps.put("limit", limit);
//        String url = Configration.SERVER + Configration.HttpString.CHEAP_GOODS;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.HTTP_CHEAP_GOODS, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
////    /**
////     * 四期新增接口
////     * jerryzhang   2015-11-09
////     * 获取折扣最多商品
////     */
//    public void getDiscountGoods(int brand_id, Handler mHandler, int limit, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("brand_id", brand_id);
//        maps.put("limit", limit);
//        String url = Configration.SERVER + Configration.HttpString.DISCOUNT_GOODS;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.HTTP_DISCOUNT_GOODS, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
////
////    /**
////     * 四期新增接口
////     * jerryzhang 2015-11-09
////     * 获取某一品牌活动列表
////     */
//    public void getActivityList(int id, Handler mHandler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", id);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_ACTIVITY_LIST;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.HTTP_ACTIVITY_LIST, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
////
////    /**
////     * 四期新增接口
////     * jerryzhang 2015-11-09
////     * 获取菜单下某一分类商品列表
////     * <p>
////     * id	是	编号
////     * limit	是	拉取数据条数
////     * offset	是	偏移
////     * brand_ids	否	多个用逗号连接
////     * price	否	asc,desc,或者空
////     * category_ids	否	多个用逗号连接
////     * sort_id	否	排序方式，单选。1：默认排序,2：价格由高到低，3：价格由低到高，4：折扣由多到少，5：最热
////     * price_ranges_id	否	价格范围，单选。
////     */
//////	public void getMenuCategoryGoods(MsgCallBack callBack,Handler mHandler,int id,int limit,int offset,String brand_ids,String price,
//////									 String category_ids,int sort_id,String price_ranges_id){
//    public void getShopGoodsBrandOption(ParaObject object, Handler mHandler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", object.getId());
//        maps.put("limit", object.getLimit());
//        maps.put("offset", object.getOffset());
//        maps.put("brand_ids", object.getBrand_ids());
//        maps.put("genders", object.getGenders());
//        maps.put("category_ids", object.getCategory_ids());
//        maps.put("sort_id", object.getSort_id());
//        maps.put("count", object.getCount());
//        maps.put("price_ranges_id", object.getPrice_ranges_id());
//        maps.put("size", object.getSize());
//        maps.put("color", object.getColor());
//        String url = Configration.SERVER + Configration.HttpString.SHOP_GOODS_BRAND_GOODS;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.SHOP_GOODS_BRAND_GOODS, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
////
////    /**
////     * 新增四期接口
////     * jerryzhang 2015-11-11
////     * 获取某一菜单下分类列表
////     */
//    public void getMenuCategoryList(MsgCallBack callBack, Handler mHandler, int id) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", id);
//        String url = Configration.SERVER + Configration.HttpString.MENU_CATEGORY_LIST;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.MENU_CATEGORY_LIST, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
////
////    /**
////     * 新增四期接口
////     * jerryzhang 2015-11-11
////     * 获取某一品牌下过滤选项
////     * id 	品牌id brandId
////     * 全部人群，全部分类，全部品牌，价格区间，排序方式
////     */
//    public void getShopGoodsBrandOption(int id, Handler mHandler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", id);
//        String url = Configration.SERVER + Configration.HttpString.SHOP_GOODS_BRAND_OPTION;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.SHOP_GOODS_BRAND_OPTION, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
////
////    /**
////     * 新增四期接口
////     * chenjian 2015-11-12
////     * 获取服务器App版本信息
////     */
//    public void getVersionInfo(Handler mHandler, int code, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("code", code);
//        String url = Configration.SERVER + Configration.HttpString.APP_VERSION_INFO;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.APP_VERSION_INFO, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 新增四期接口
//     * chenjian 2015-11-12
//     * 获取服务器App启动图片
//     */
//    public void getAppStartImage(Handler mHandler, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.APP_START_IMAGE;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.APP_START_IMAGE, mHandler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 新增四期接口
//     * jerryzhang 2015-12-07
//     * 接口名：获取用户购买的商品列表
//     * 参数 limit ,offset ,sub_order_num 为空的时候：返回所有购买的商品，不为空的时候，返回指定订单的商品.
//     * 返回值：购买记录商品列表
//     */
//    public void getGoodsBoughtList(Handler mHandler, int limit, int offset, String sub_order_num, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("limit", limit);
//        maps.put("offset", offset);
//        maps.put("sub_order_num", sub_order_num);
//        String url = Configration.SERVER + Configration.HttpString.GOODS_BOUGHT;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.GOODS_BOUGHT, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    public void getGoodsBoughtList(Handler mHandler, int limit, int offset, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("limit", limit);
//        maps.put("offset", offset);
//        String url = Configration.SERVER + Configration.HttpString.GOODS_BOUGHT;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.GOODS_BOUGHT, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 新增四期接口
//     * jerryzhang 2015-12-08
//     * 接口名：获取某一商品的概要信息
//     * 参数：goodsId
//     * 返回值：goods列表
//     */
//    public void getGoodsProfileList(Handler mHandler, String goodsId, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("goodsId", goodsId);
//        String url = Configration.SERVER + Configration.HttpString.GOODS_PROFILE;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.GOODS_PROFILE, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 新增四期接口
//     * jerryzhang 2015-12-10
//     * 接口名：把商品添加到购物车
//     * 参数：format": “base64”,    "data": "
//     * 返回值：
//     */
//    public void commitShoppingCarData(String format, String data, Handler mHandler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("format", format);
//        maps.put("data", data);
//        String url = Configration.SERVER + Configration.HttpString.SHOPPING_CAR_COMMIT;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.SHOPPING_CAR_COMMIT, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, myCallBack));
//    }
//
//    // 同步服务器购物车数据
//    public void syncShoppingCarData(String format, String data, Handler mHandler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("format", format);
//        maps.put("data", data);
//        String url = Configration.SERVER + Configration.HttpString.SYNC_SHOPPINGCART;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.SHOPPING_CAR_COMMIT, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, myCallBack));
//    }
//
//
//    /**
//     * 四期新增接口
//     * Jerryzhang 2015-12-11
//     * 接口名：删除购物车中的商品
//     * 参数:ids 购物车id，多个用逗号连接
//     * 返回值; data  购物车数据
//     */
//    public void shoppingCarDelete(Handler mhandler, String ids, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.SHOPPING_CAR_DELETE;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.SHOPPING_CAR_DELETE, mhandler);
//        pool.execute(new HttpRunnable(url, ids, mCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * Jerryzhang 2015-12-11
//     * 接口名：修改购物车中的商品
//     * 参数: id, num
//     * 返回值; data  购物车数据
//     */
//    public void shoppingCarUpdata(Handler mhandler, String json, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.SHOPPING_CAR_UPDATA;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.SHOPPING_CAR_UPDATA, mhandler);
//        pool.execute(new HttpRunnable(url, json, myCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * Jerryzhang 2015-12-17
//     * 接口名：
//     * 参数:
//     * 返回值; data
//     */
//    public void getShoppingCarInfo(Handler mhandler, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.SHOPPING_CAR_INFO;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.SHOPPING_CAR_INFO, mhandler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * Jerryzhang 2015-12-24
//     * 接口名：获取注册页面热门品牌
//     * 参数: 无
//     * 返回值; list
//     */
//    public void getBrandsRegister(Handler mhandler, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.BRAND_REGISTER_LIST;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.BRAND_REGISTER_LIST, mhandler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * Jerryzhang 2015-12-24
//     * 接口名：获取注册页面热门品类
//     * 参数: 无
//     * 返回值; list
//     */
//    public void getCategoriesRegister(Handler mhandler, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.HOT_CATEGORY_REGISTER_LIST;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.HOT_CATEGORY_REGISTER_LIST, mhandler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * Jerryzhang 2015-12-24
//     * 接口名：批量关注品牌品类
//     * 参数: tag_list 标签id,多个用逗号隔开，category_list 类型id，多个用逗号隔开
//     * 返回值; data
//     */
//    public void postFolloeBrandsCategorise(Handler mhandler, String tag_list, String category_list, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("tag_list", tag_list);
//        maps.put("category_list", category_list);
//        String url = Configration.SERVER + Configration.HttpString.FOLLOW_BRANDS_CATEGORIES;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.FOLLOW_BRANDS_CATEGORIES, mhandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, myCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * Jerryzhang 2015-12-24
//     * 接口名：跳过注册关注
//     * 参数: 无
//     * 返回值; data
//     */
//    public void postRegisterFolloeSkip(Handler mhandler, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.REGISTER_FOLLOW_SKIP;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.REGISTER_FOLLOW_SKIP, mhandler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_POST, myCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * Jerryzhang 2015-12-25
//     * 接口名：获取热门搜索
//     * 参数: 无
//     * 返回值; words数组
//     */
//    public void getSearchHotWords(Handler mhandler, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.SEARCH_HOT_WORDS;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.SEARCH_HOT_WORDS, mhandler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * Jerryzhang 2015-12-25
//     * 接口名：获取最新的搜索数据
//     * 参数: stamp 传上次请求的stamp的参数，第一次传空
//     * 返回值; stamp参数字段，data数组
//     */
//    public void getShopSearchData(Handler mhandler, String stamp, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("stamp", stamp);
//        String url = Configration.SERVER + Configration.HttpString.SHOP_SEARCH_DATA;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.SHOP_SEARCH_DATA, mhandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 四期新增接口
//     * chenjian 2015-12-30
//     * 接口名：
//     * 参数:
//     * 返回值; data
//     */
//    public void getHtmlFileInfo(Handler mhandler, int code, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("code", code);
//        String url = Configration.SERVER + Configration.HttpString.HTML_INFO;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.HTML_INFO, mhandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * <<<<<<< Updated upstream
//     * 四期新增接口
//     * Jerryzhang 2015-01-04
//     * 接口名：搜索之后的商品列表
//     * 参数: ParaObject
//     * 返回值; lsit
//     */
//    public void getSearchGoods(Handler mhandler, int offset, ParaObject object, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", object.getId());
//        maps.put("brand_ids", object.getBrand_ids());
//        maps.put("sort_id", object.getSort_id());
//        maps.put("genders", object.getGenders());
//        maps.put("price_ranges_id", object.getPrice_ranges_id());
//        maps.put("count", object.getCount());
//        maps.put("category_ids", object.getCategory_ids());
//        maps.put("word", object.getWord());
//        maps.put("size", object.getSize());
//        maps.put("color", object.getColor());
//        maps.put(Configration.HttpString.Offset, offset);
//        maps.put(Configration.HttpString.Limit, 40);
//        String url = Configration.SERVER + Configration.HttpString.SEARCH_GOODS;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.SEARCH_GOODS, mhandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
////
////    /**
////     * 四期新增接口
////     * Jerryzhang 2015-01-04
////     * 接口名：搜索之后的商品的过滤选项
////     * 参数: ParaObject
////     * 返回值; data 选项数组
////     */
//    public void getSearchLimitList(Handler mhandler, String word, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("word", word);
//        maps.put("brand", 0);
//        String url = Configration.SERVER + Configration.HttpString.SEARCH_OPTION;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.SEARCH_OPTION, mhandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
////
////    /**
////     * 四期新增接口
////     * Jerryzhang 2015-01-07
////     * 接口名：获取推荐App列表
////     * 参数: 无
////     * 返回值; list
////     */
//    public void getAppRecommend(Handler mHandler, MsgCallBack callBack) {
//        String url = Configration.SERVER + Configration.HttpString.APP_RECOMMEND;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.APP_RECOMMEND, mHandler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
////    /**
////     * 闪购接口
////     * tomyang 2016-1-6
////     * 接口名：设置某一闪购活动的开抢提醒
////     * 参数：id：活动编号  notice：设置状态，1为需提醒，2为不需提醒
////     */
//    public void setFlashSaleNotice(String id, int notice, Handler mHandler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", id);
//        maps.put("notice", notice);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_FLASH_SALE_SET_NOTICE;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.HTTP_FLASH_SALE_SET_NOTICE, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, myCallBack));
//    }
//
//    /**
//     * tomyang 2016-1-8
//     * 获取闪购活动列表
//     */
//    public void flashSaleList(MsgCallBack callBack, Handler mHandler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_FLASH_SALE_LIST;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.HTTP_FLASH_SALE_LIST, mHandler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 新增接口
//     * Jerryzhang 2015-01-20
//     * 接口名：兑换兑换码
//     * 参数: code 兑换码, r
//     * 返回值; msg 兑换码提示信息
//     */
//    public void getExchangeCode(Handler mHandler, String code, int r, MsgCallBack callBack) {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("code", code);
//        String url = Configration.SERVER + Configration.HttpString.EXCHANGE_CODE;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.EXCHANGE_CODE, mHandler);
//        pool.execute(new HttpRunnable(url, map, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 新增接口
//     * Jerryzhang 2015-01-20
//     * 接口名：兑换历史
//     * 参数: offset , limit
//     * 返回值; list列表
//     */
//    public void getExchangeCodeRecord(Handler mHandler, int offset, int limit, MsgCallBack msgCallBack) {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("offset", offset);
//        map.put("limit", limit);
//        String url = Configration.SERVER + Configration.HttpString.EXCHANGE_CODE_RECORD;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.EXCHANGE_CODE_RECORD, mHandler);
//        pool.execute(new HttpRunnable(url, map, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 新增接口
//     * Jerryzhang 2015-02-03
//     * 接口名：某一专题下活动列表
//     * 参数: id 专题编号
//     * 返回值; list列表
//     */
//    public void getThematicList(Handler handler, int id, MsgCallBack msgCallBack) {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("id", id);
//        String url = Configration.SERVER + Configration.HttpString.THEMATIC_ACTIVITY;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.THEMATIC_ACTIVITY, handler);
//        pool.execute(new HttpRunnable(url, map, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 新增接口
//     * Tomyang 2015-03-21
//     * 接口名：某一菜单下活动列表
//     * 参数: id 专题编号
//     * 返回值; list列表
//     */
//    public void getMenuList(Handler handler, int id, MsgCallBack msgCallBack) {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("id", id);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_SHOP_MENU_ACTIVITY;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.TYPE_SHOP_MENU_ACTIVITY, handler);
//        pool.execute(new HttpRunnable(url, map, HttpRunnable.HTTP_GET, myCallBack));
//    }
////
////    /**
////     * 新增接口
////     * Jerryzhang 2015-02-03
////     * 接口名：某一活动商品列表
////     * 参数: id 专题编号， offset, limit
////     * 返回值; list列表
////     */
//    public void getActivityGoodsList(Handler handler, int id, int offset, int limit, MsgCallBack msgCallBack) {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("id", id);
//        map.put("offset", offset);
//        map.put("limit", limit);
//        String url = Configration.SERVER + Configration.HttpString.ACTIVITY_GOODS;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.ACTIVITY_GOODS, handler);
//        pool.execute(new HttpRunnable(url, map, HttpRunnable.HTTP_GET, myCallBack));
//    }
////
//    /**
//     * 新增接口
//     * Jerryzhang 2016-02-03
//     * 接口名：某一活动商品列表
//     * 参数: id 专题编号， offset, limit
//     * 返回值; list列表
//     */
//    public void getFlashSale(Handler handler, String id, int offset, int limit, MsgCallBack msgCallBack) {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("id", id);
//        map.put("offset", offset);
//        map.put("limit", limit);
//        String url = Configration.SERVER + Configration.HttpString.FLASH_SALE;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.FLASH_SALE, handler);
//        pool.execute(new HttpRunnable(url, map, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 新增接口
//     * tomyang 2016-02-24
//     * 接口名：获取某一活动详情
//     * 参数：id 活动编号
//     */
//    public void getIssueActivityDetail(Handler handler, int id, MsgCallBack msgCallBack) {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("id", id);
//        String url = Configration.SERVER + Configration.HttpString.ACTIVITY_DETAIL;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.TYPE_ACTIVITY_DETAIL, handler);
//        pool.execute(new HttpRunnable(url, map, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 新增接口
//     * tomyang 2016-02-25
//     * 接口名：获取某一闪购活动详情
//     * 参数：id 活动编号
//     */
//    public void getFlashActivityDetail(Handler handler, String id, MsgCallBack msgCallBack) {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("id", id);
//        String url = Configration.SERVER + Configration.HttpString.FLASH_SALE_DETAIL;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.TYPE_FLASH_SALE_DETAIL, handler);
//        pool.execute(new HttpRunnable(url, map, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//
//    /**
//     * 新增接口
//     * chenjian 2016-02-17
//     * 接口名：app微信登录接口
//     */
//    public void loginWeixin(Handler handler, String code, String cid, MsgCallBack msgCallBack) {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("code", code);
//        map.put("cid", cid);
//        String url = Configration.SERVER + Configration.HttpString.WX_LOGIN;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.TYPE_WX_LOGIN, handler);
//        pool.execute(new HttpRunnable(url, map, HttpRunnable.HTTP_POST, myCallBack));
//    }
//
////    /**
////     * 修改订阅接口
////     * tomyang 2016-2-26
////     * 接口名：批量关注品牌
////     * 参数: tag_list 标签id
////     * 返回值; data
////     */
//    public void postFolloedBrands(Handler mhandler, String tag_list, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("tag_list", tag_list);
//        String url = Configration.SERVER + Configration.HttpString.FOLLOW_TAGS;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.TYPE_FOLLOW_TAGS, mhandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, myCallBack));
//    }
//
////    /**
////     * 修改订阅接口
////     * tomyang 2016-2-26
////     * 接口名：取消批量关注品牌
////     * 参数: tag_list 标签id
////     * 返回值; data
////     */
//    public void postUnFolloedBrands(Handler mhandler, String tag_list, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("tag_list", tag_list);
//        String url = Configration.SERVER + Configration.HttpString.UNFOLLOW_TAGS;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.TYPE_UNFOLLOW_TAGS, mhandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, myCallBack));
//    }
//
//    /**
//     * 修改订阅接口
//     * tomyang 2016-2-26
//     * 接口名：批量关注品类
//     * 参数: category_list 品类id
//     * 返回值; data
//     */
//    public void postFolloedCategorys(Handler mhandler, String category_list, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("category_list", category_list);
//        String url = Configration.SERVER + Configration.HttpString.FOLLOW_CATEGORYS;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.TYPE_FOLLOW_CATEGORIES, mhandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, myCallBack));
//    }
//
//    /**
//     * 修改订阅接口
//     * tomyang 2016-2-26
//     * 接口名：批量取消关注品类
//     * 参数: category_list 品类id
//     * 返回值; data
//     */
//    public void postUnFolloedCategorys(Handler mhandler, String category_list, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("category_list", category_list);
//        String url = Configration.SERVER + Configration.HttpString.UNFOLLOW_CATEGORYS;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.TYPE_UNFOLLOW_CATEGORIES, mhandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, myCallBack));
//    }
//
//    /**
//     * <<<<<<< Updated upstream
//     * 新增接口
//     * Jerryzhang 2015-02-25
//     * 接口名：某一状态订单列表
//     * 参数: status 状态（0:全部,1:待付款,2:待缴费,3:在途,4:完成,5:失效）， offset, limit
//     * 返回值; list列表
//     */
//    public void getAllOrderList(Handler handler, String status, int offset, int limit, MsgCallBack msgCallBack) {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("status", status);
//        map.put("offset", offset);
//        map.put("limit", limit);
//        String url = Configration.SERVER + Configration.HttpString.ORDER_LIST;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.ORDER_LIST, handler);
//        pool.execute(new HttpRunnable(url, map, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 新增接口
//     * Jerryzhang 2015-02-25
//     * 接口名：订单商品加回购物车
//     * 参数: orderNumber
//     * 返回值; []
//     */
//    public void gobackShoppingCar(Handler handler, String orderNumber, MsgCallBack msgCallBack) {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("orderNumber", orderNumber);
//        String url = Configration.SERVER + Configration.HttpString.SHOP_ORDER_TO_CAR;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.SHOP_ORDER_TO_CAR, handler);
//        pool.execute(new HttpRunnable(url, map, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
////    /**
////     * 新增接口
////     * Jerryzhang 2016-03-01
////     * 接口名：获取当前系统默认皮肤
////     * 参数: 无
////     * 返回值; list
////     */
//    public void getSkin(Handler handler, MsgCallBack msgCallBack) {
//        String url = Configration.SERVER + Configration.HttpString.SYSTEM_SKIN;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.SYSTEM_SKIN, handler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, myCallBack));
//    }
////
//    /**
//     * 新增接口
//     * Jerryzhang 2016-03-07
//     * 接口名：获取最新的搜索数据
//     * 参数: stamp
//     * 返回值;
//     */
//    public void getPopularData(Handler mhandler, String stamp, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("stamp", stamp);
//        String url = Configration.SERVER + Configration.HttpString.SEARCH_DATA;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.SEARCH_DATA, mhandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * tomyang 2016-3-9
//     * 用户获取闪购提醒活动列表
//     */
//    public void flashSaleNoticeList(MsgCallBack callBack, Handler mHandler) {
//        String url = Configration.SERVER + Configration.HttpString.FLASH_SALE_NOTICE_LIST;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.TYPE_FLASH_SALE_NOTICE_LIST, mHandler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * chenjian 2016-3-07
//     * 接口名：获取某一订单详情
//     * 参数: 订单号
//     * 返回值; data
//     */
//    public void getOrderDetail(Handler mhandler, String orderNumber, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("orderNumber", orderNumber);
//        String url = Configration.SERVER + Configration.HttpString.ORDER_DETAIL;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.ORDER_DETAIL, mhandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 获取红包分享信息
//     * chenjian 2016-3-09
//     * 参数: 订单号
//     * 返回值; data
//     */
//    public void getRedPackageInfo(Handler mhandler, String orderNumber, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("uid", orderNumber);
//        maps.put("source", 1);
//        String url = Configration.SERVER + Configration.HttpString.RED_PACKAGE_URL;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.RED_PACKAGE_URL, mhandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//
//    // 导航功能接口
//
//    /**
//     * 接口名：导航的banner接口
//     * jerryzhang 2016-03-18
//     * 参数：无
//     * 返回值 list列表
//     */
//    public void getNaviBannerList(Handler handler, MsgCallBack msgCallBack) {
//        String url = Configration.SERVER + Configration.HttpString.NAVI_BANNER_LIST;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.NAVI_BANNER_LIST, handler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 获取国家列表
//     * jerryzhang 2016-3-18
//     * 参数:无
//     * 返回值; list
//     */
//    public void getCountryList(boolean bNet, Handler mhandler, int offset, MsgCallBack callBack) {
//        if (bNet) {
//            HashMap<String, Object> maps = new HashMap<>();
//            maps.clear();
//            maps.put("offset", offset);
//            maps.put("limit", LIMIT);
//            String url = Configration.SERVER + Configration.HttpString.NAVI_COUNTRY_LIST;
//            MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.NAVI_COUNTRY_LIST, mhandler);
//            pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//        } else {
//            GetLocalRequest.getInstance().getCountryList(offset, mhandler, callBack);
//        }
//
//    }
//
//    /**
//     * 获取热门线路列表
//     * jerryzhang 2016-3-18
//     * 参数:id (国家ID，不填则为所有)
//     * 返回值; list
//     */
//    public void getShopLineList(Handler mhandler, int offset, String countryId, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("offset", offset);
//        maps.put("limit", LIMITSIX);
//        maps.put("id", countryId);
//        String url = Configration.SERVER + Configration.HttpString.SHOP_LINE_LIST;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.SHOP_LINE_LIST, mhandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 获取某一国家下的城市列表
//     * jerryzhang 2016-3-18
//     * 参数: id 国家id
//     * 返回值; list
//     */
//    public void getCityList(boolean bNet, Handler mhandler, int offset, String countryId, MsgCallBack callBack) {
//        if (bNet) {
//            HashMap<String, Object> maps = new HashMap<>();
//            maps.put("offset", offset);
//            maps.put("limit", LIMIT);
//            maps.put("id", countryId);
//            String url = Configration.SERVER + Configration.HttpString.CITY_LIST;
//            MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.CITY_LIST, mhandler);
//            pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//        } else {
//            GetLocalRequest.getInstance().getCityList(offset, countryId, mhandler, callBack);
//        }
//
//    }
//
//    /**
//     * 获取单个国家的必买推荐信息
//     * jerryzhang 2016-3-18
//     * 参数: id 国家id
//     * 返回值; list
//     */
//    public void getCountryRecommend(boolean bNet, Handler mhandler, String countryId, MsgCallBack callBack) {
//        if (bNet) {
//            HashMap<String, Object> maps = new HashMap<>();
//            maps.put("limit", LIMIT);
//            maps.put("id", countryId);
//            String url = Configration.SERVER + Configration.HttpString.COUNTRY_RECOMMEND_INFO;
//            MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.COUNTRY_RECOMMEND_INFO, mhandler);
//            pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//        } else {
//            GetLocalRequest.getInstance().getRecommendInfo(countryId, mhandler, callBack);
//        }
//
//    }
//
//    /**
//     * 接口名：获取某一国家的资讯信息列表
//     * jerryzhang 2016-3-18
//     * 参数：id (国家id)
//     * 返回值： list列表
//     *
//     * @param countryId
//     * @param offset
//     */
//    public void getNewsOfCountry(Handler mhandler, int offset, String countryId, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("offset", offset);
//        maps.put("limit", LIMITSIX);
//        maps.put("id", countryId);
//        String url = Configration.SERVER + Configration.HttpString.NEWS_LIST_OF_COUNTRY;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.NEWS_LIST_OF_COUNTRY, mhandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * * 接口名：获取单个国家退税信息
//     * jerryzhang 2016-3-18
//     * 参数：id (国家id)
//     * 返回值： list列表
//     *
//     * @param countryId
//     */
//    public void getTaxRebateOfCountry(boolean bNet, Handler mhandler, String countryId, MsgCallBack callBack) {
//        if (bNet) {
//            HashMap<String, Object> maps = new HashMap<>();
//            maps.put("id", countryId);
//            String url = Configration.SERVER + Configration.HttpString.TAX_REBATE_OF_COUNTRY;
//            MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.TAX_REBATE_OF_COUNTRY, mhandler);
//            pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//        } else {
//            GetLocalRequest.getInstance().getTaxInfo(countryId, mhandler, callBack);
//        }
//
//    }
//
//    /**
//     * 接口名：国家简要信息接口
//     * jerryzhang 2016-03-21
//     * 参数; id (国家id)
//     * 返回值：对象
//     *
//     * @param countryId
//     */
//    public void getCountryBriefInfo(boolean bNet, Handler mhandler, String countryId, MsgCallBack msgCallBack) {
//        if (bNet) {
//            HashMap<String, Object> maps = new HashMap<>();
//            maps.put("id", countryId);
//            String url = Configration.SERVER + Configration.HttpString.COUNTRY_BRIEF_INFO;
//            MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.COUNTRY_BRIEF_INFO, mhandler);
//            pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//        } else {
//            GetLocalRequest.getInstance().getLocalCountry(countryId, mhandler, msgCallBack);
//        }
//
//    }
//
//    /**
//     * 接口名:国家详细信息接口
//     * jerryzhang 2016-03-21
//     * 参数：id(国家id)
//     * 返回值:CountryListInfo对象
//     *
//     * @param countryId
//     */
//    public void getCountryDetailInfo(boolean bNet, Handler handler, String countryId, MsgCallBack msgCallBack) {
//        ///TODO 临时参数
//        if (bNet) {
//            HashMap<String, Object> maps = new HashMap<>();
//            maps.put("id", countryId);
//            String url = Configration.SERVER + Configration.HttpString.COUNTRY_DETAIL_INFO;
//            MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.COUNTRY_DETAIL_INFO, handler);
//            pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//        } else {
//            GetLocalRequest.getInstance().getLocalCountry(countryId, handler, msgCallBack);
//        }
//    }
//
//    /**
//     * 接口名：获取单个城市简要信息
//     * jerryzhang 2016-03-21
//     * 参数：id(城市id)
//     * 返回值：CountryListInfo对象
//     *
//     * @param handler
//     * @param cityId
//     */
//    public void getCityBriefInfo(boolean bNet, Handler handler, String cityId, String countryId, MsgCallBack msgCallBack) {
//        if (bNet) {
//            HashMap<String, Object> maps = new HashMap<>();
//            maps.put("id", cityId);
//            String url = Configration.SERVER + Configration.HttpString.CITY_BRIEF_INFO;
//            MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.CITY_BRIEF_INFO, handler);
//            pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//        } else {
//            GetLocalRequest.getInstance().getCityBriefInfo("", cityId, countryId, handler, msgCallBack);
//        }
//    }
//
//    /**
//     * 接口名：获取单个城市详细信息
//     * jerryzhang 2016-03-21
//     * 参数：id（城市id）
//     * 返回值：CountryListInfo对象
//     *
//     * @param handler
//     * @param cityId
//     */
//    public void getCityDetailInfo(boolean bNet, Handler handler, String cityId, String countryId, MsgCallBack msgCallBack) {
//        if (bNet) {
//            HashMap<String, Object> maps = new HashMap<>();
//            maps.put("id", cityId);
//            String url = Configration.SERVER + Configration.HttpString.CITY_DETAIL_INFO;
//            MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.CITY_DETAIL_INFO, handler);
//            pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//        } else {
//            GetLocalRequest.getInstance().getCityBriefInfo(DETAIL, cityId, countryId, handler, msgCallBack);
//        }
//
//    }
//
//    /**
//     * 接口名：获取某一城市的资讯信息列表
//     * jerryzhang 2016-03-23
//     * 参数：id(城市id)
//     * 返回值：list列表
//     *
//     * @param handler
//     * @param offset
//     * @param cityId
//     * @param msgCallBack
//     */
//    public void getNewsListOfCity(Handler handler, int offset, String cityId, MsgCallBack msgCallBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("offset", offset);
//        maps.put("limit", LIMITSIX);
//        maps.put("id", cityId);
//        String url = Configration.SERVER + Configration.HttpString.NEWS_LIST_OF_CITY;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.NEWS_LIST_OF_CITY, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 接口名：获取某一城市下的商场列表
//     * jerryzhang 2016-03-22
//     * 参数：id(城市id)
//     * 返回值；list列表
//     *
//     * @param offset
//     * @param cityId
//     */
//    public void getMallListOfCity(boolean bNet, Handler handler, int offset, String cityId, String countryId, MsgCallBack msgCallBack) {
//        if (bNet) {
//            HashMap<String, Object> maps = new HashMap<>();
//            maps.put("offset", offset);
//            maps.put("limit", LITTLELIMIT);
//            maps.put("id", cityId);
//            String url = Configration.SERVER + Configration.HttpString.MALL_LIST_OF_CITY;
//            MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.MALL_LIST_OF_CITY, handler);
//            pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//        } else {
//            GetLocalRequest.getInstance().getLocalMallList(cityId, handler, countryId, offset, msgCallBack);
//        }
//    }
//
//    /**
//     * 接口名;获取某一城市下的店铺列表
//     * jerryzhang 2016 - 03 - 22
//     * 参数：id（城市id） brand_list(品牌id字符串，多个用逗号隔开)，
//     * category_lisy(类型id字符串，多个用逗号隔开)，count（为0表示不用计算总数，为1表示计算总数）
//     *
//     * @param offset
//     * @param cityId
//     * @param categoryList
//     * @param count
//     */
//    public void getStoreListOfCity(boolean bNet, Handler handler, int offset, String cityId, String categoryList, String longitude, String latitude, String countryId, int count, MsgCallBack msgCallBack) {
//        if (bNet) {
//            HashMap<String, Object> maps = new HashMap<>();
//            maps.put("offset", offset);
//            maps.put("limit", MIDDLELIMIT);
//            maps.put("id", cityId);
////        maps.put("brand-list", brandList);
//            maps.put("category_list", categoryList);
//            maps.put("longitude", longitude);
//            maps.put("latitude", latitude);
//            maps.put("count", count);
//            String url = Configration.SERVER + Configration.HttpString.STORE_LIST_OF_CITY;
//            MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.STORE_LIST_OF_CITY, handler);
//            pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//        } else {
//            GetLocalRequest.getInstance().getLocalStoreList(CITY, cityId, handler, countryId, cityId, offset, msgCallBack);
//        }
//
//    }
//
//    /**
//     * 接口名：获取单个资讯的详细信息
//     * jerryzhang 2016-03-23
//     * 参数：id（资讯id）
//     * 返回值：CountryListInfo对象
//     *
//     * @param informationId
//     */
//    public void getNewsDetailInfo(Handler handler, String informationId, MsgCallBack msgCallBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", informationId);
//        String url = Configration.SERVER + Configration.HttpString.NEWS_DETAIL_INFO;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.NEWS_DETAIL_INFO, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 接口名：获取某一城市下的coupon列表
//     * jerryzhang 2016-03-23
//     * 参数：id(城市id)
//     * 返回值：list列表
//     *
//     * @param offset
//     * @param cityId
//     */
//    public void getCouponListOfCity(boolean bNet, Handler handler, int offset, String cityId, String countryId, MsgCallBack msgCallBack) {
//        if (bNet) {
//            HashMap<String, Object> maps = new HashMap<>();
//            maps.put("offset", offset);
//            maps.put("limit", LIMIT);
//            maps.put("id", cityId);
//            String url = Configration.SERVER + Configration.HttpString.COUPON_LIST_OF_CITY;
//            MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.COUPON_LIST_OF_CITY, handler);
//            pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//        } else {
//            GetLocalRequest.getInstance().getLocalCouponList(CITY, cityId, cityId, handler, countryId, msgCallBack);
//        }
//
//    }
//
//    /**
//     * 接口名：获取单个商场详细信息
//     * jerryzhang 2016-03-23
//     * 参数：id（商场id）
//     * 返回值：MallDetailInfo对象
//     *
//     * @param mallId
//     */
//    public void getMallDetailInfo(boolean bNet, Handler handler, String mallId, String countryId, String cityId, MsgCallBack msgCallBack) {
//        if (bNet) {
//            HashMap<String, Object> maps = new HashMap<>();
////        maps.put("offset", offset);
////        maps.put("limit", LIMIT);
//            maps.put("id", mallId);
//            String url = Configration.SERVER + Configration.HttpString.MALL_DETAIL_INFO;
//            MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.MALL_DETAIL_INFO, handler);
//            pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//        } else {
//            GetLocalRequest.getInstance().getLocalMallInfo(handler, mallId, countryId, cityId, msgCallBack);
//        }
//
//    }
//
//    /**
//     * 接口名;获取某一商场下的店铺列表
//     * jerryzhang 2016 - 03 - 25
//     * 参数：id（城市id） brandList(品牌id字符串，多个用逗号隔开)，
//     * categoryLisy(类型id字符串，多个用逗号隔开)，count（为0表示不用计算总数，为1表示计算总数）
//     * 返回值：int (count) 总数， list列表
//     *
//     * @param handler
//     * @param offset
//     * @param mallId
//     * @param categoryList
//     * @param count
//     * @param msgCallBack
//     */
//    public void getStoreListOfMall(boolean bNet, Handler handler, int offset, String mallId, String countryId, String cityId, String categoryList, int count, MsgCallBack msgCallBack) {
//        if (bNet) {
//            HashMap<String, Object> maps = new HashMap<>();
//            maps.put("offset", offset);
//            maps.put("limit", MIDDLELIMIT);
//            maps.put("id", mallId);
//            maps.put("category_list", categoryList);
//            maps.put("count", count);
//            String url = Configration.SERVER + Configration.HttpString.STORE_LIST_OF_MALL;
//            MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.STORE_LIST_OF_MALL, handler);
//            pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//        } else {
//            GetLocalRequest.getInstance().getLocalStoreList(MALL, mallId, handler, countryId, cityId, offset, msgCallBack);
//        }
//    }
//
//    /**
//     * 接口名：获取某一商场下店铺的品牌与分类列表
//     * jerryzhang 2016-03-25
//     * 参数：id（商场id）
//     * 返回值 ：MallDetailInfo对象
//     *
//     * @param handler
//     * @param type
//     * @param msgCallBack
//     */
//    public void getStoreBrandsCategoryOfMall(Handler handler, String id, String type, MsgCallBack msgCallBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", id);
//        maps.put("type", type);
//        String url = Configration.SERVER + Configration.HttpString.STORE_BRANDS_CATEGORY_OF_MALL;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.STORE_BRANDS_CATEGORY_OF_MALL, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 接口名;获取单个商场下的推荐信息
//     * jerryzhang 2016-03-25
//     * 参数：id(商场id)
//     * 返回值：list列表
//     *
//     * @param handler
//     * @param mallId
//     * @param msgCallBack
//     */
//    public void getMallRecommendInfo(Handler handler, String mallId, MsgCallBack msgCallBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", mallId);
//        String url = Configration.SERVER + Configration.HttpString.MALL_RECOMMEND_INFO;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.MALL_RECOMMEND_INFO, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 接口名;获取某一商场下的coupon列表
//     * jerryzhang2016-03-25
//     * 参数：id(商场id)
//     * 返回值：list列表
//     *
//     * @param handler
//     * @param offset
//     * @param mallId
//     * @param msgCallBack
//     */
//    public void getCouponListOfMall(boolean bNet, Handler handler, int offset, String mallId, String countryId, String cityId, MsgCallBack msgCallBack) {
//        if (bNet) {
//            HashMap<String, Object> maps = new HashMap<>();
//            maps.put("offset", offset);
//            maps.put("limit", LIMIT);
//            maps.put("id", mallId);
//            String url = Configration.SERVER + Configration.HttpString.COUPON_LIST_OF_MALL;
//            MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.COUPON_LIST_OF_MALL, handler);
//            pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//        } else {
//            GetLocalRequest.getInstance().getLocalCouponList(MALL, mallId, cityId, handler, countryId, msgCallBack);
//        }
//
//    }
//
////    /**
////     * 接口名; 获取某一店铺下的分店列表
////     * jerryzhang 2015-03-25
////     * 参数：id（商场id或店铺id）
////     * 返回值; list列表
////     *
////     * @param offset
////     * @param mallId
////     */
//    public void getStoreListOfSame(Handler handler, int offset, String mallId, MsgCallBack msgCallBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("offset", offset);
//        maps.put("limit", LIMIT);
//        maps.put("id", mallId);
//        String url = Configration.SERVER + Configration.HttpString.STORE_LIST_OF_SAME;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.STORE_LIST_OF_SAME, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
////
//    /**
//     * 接口名：获取某一店铺附近的店列表
//     * <p>
//     * jerryzhang 2016-03-25
//     * 参数：id(商场id或店铺id)
//     * 返回值；list列表
//     *
//     * @param handler
//     * @param offset
//     * @param mallId
//     * @param msgCallBack
//     */
//    public void getStoreListOfAround(Handler handler, int offset, String mallId, MsgCallBack msgCallBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("offset", offset);
//        maps.put("limit", LIMITTHREE);
//        maps.put("id", mallId);
//        String url = Configration.SERVER + Configration.HttpString.STORE_LIST_OF_AROUND;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.STORE_LIST_OF_AROUND, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 接口名: 获取单个店铺详细信息
//     * jerryzhang 2016-03-25
//     * 参数;id(店铺id)
//     * 返回值：MallDetailInfo对象
//     *
//     * @param msgCallBack
//     * @param handler
//     * @param storeId
//     */
//    public void getStoreDetailInfo(boolean bNet, Handler handler, String storeId, String countryId, String cityId, MsgCallBack msgCallBack) {
//        if (bNet) {
//            HashMap<String, Object> maps = new HashMap<>();
//            maps.put("id", storeId);
//            String url = Configration.SERVER + Configration.HttpString.STORE_DETAIL_INFO;
//            MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.STORE_DETAIL_INFO, handler);
//            pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//        } else {
//            GetLocalRequest.getInstance().getLocalStoreInfo(storeId, handler, countryId, cityId, msgCallBack);
//        }
//
//    }
//
//    /**
//     * 接口名：店铺下面coupon券列表
//     * jerryzhang 2015-03-25
//     * 参数：id(店铺id)
//     * 返回值：list列表
//     *
//     * @param msgCallBack
//     * @param handler
//     * @param offset
//     * @param storeId
//     */
//    public void getCouponListOfStore(boolean bNet, Handler handler, int offset, String storeId, String countryId, String cityId, MsgCallBack msgCallBack) {
//        if (bNet) {
//            HashMap<String, Object> maps = new HashMap<>();
//            maps.put("offset", offset);
//            maps.put("limit", LIMIT);
//            maps.put("id", storeId);
//            String url = Configration.SERVER + Configration.HttpString.COUPON_LIST_OF_STORE;
//            MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.COUPON_LIST_OF_STORE, handler);
//            pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//        } else {
//            GetLocalRequest.getInstance().getLocalCouponList(STORE, storeId, cityId, handler, countryId, msgCallBack);
//        }
//
//    }
//
//    /**
//     * 获取单个店铺下的推荐信息
//     * jerryzhang 2015-03-25
//     * 参数：id（店铺id）
//     * 返回值：CountryListObject列表
//     *
//     * @param msgCallBack
//     * @param handler
//     * @param storeId
//     */
//    public void getStoreRecommendInfo(Handler handler, String storeId, MsgCallBack msgCallBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", storeId);
//        String url = Configration.SERVER + Configration.HttpString.STORE_RECOMMEND_INFO;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.STORE_RECOMMEND_INFO, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 接口名：获取coupon详情信息
//     * jerryzhang 2015-03-25
//     * 参数： id（coupon券id）
//     * 返回值 ：CouponDetailInfo对象
//     *
//     * @param msgCallBack
//     * @param handler
//     * @param couponId
//     */
//    public void getCouponDetailInfo(boolean bNet, Handler handler, String couponId, String countryId, String cityId, MsgCallBack msgCallBack) {
//        if (bNet) {
//            HashMap<String, Object> maps = new HashMap<>();
//            maps.put("id", couponId);
//            String url = Configration.SERVER + Configration.HttpString.COUPON_DETAIL_INFO;
//            MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.COUPON_DETAIL_INFO, handler);
//            pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//        } else {
//            GetLocalRequest.getInstance().getLocalCouponDetail(couponId, handler, countryId, cityId, msgCallBack);
//        }
//
//    }
//
//    /**
//     * 接口名：收藏某一coupon卷
//     * jerryzhang 2016-03-28
//     * 参数：id（couponId）
//     * 返回值：Object对象
//     *
//     * @param msgCallBack
//     * @param handler
//     * @param couponId
//     */
//    public void getCouponCollect(Handler handler, String couponId, MsgCallBack msgCallBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", couponId);
//        String url = Configration.SERVER + Configration.HttpString.COUPON_COLLECT;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.COUPON_COLLECT, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, myCallBack));
//    }
//
//    /**
//     * 接口名：取消收藏某一coupon卷
//     * jerryzhang 2016-03-28
//     * 参数：id（couponId）
//     * 返回值：Object对象
//     *
//     * @param msgCallBack
//     * @param handler
//     * @param couponId
//     */
//    public void getCouponUncollect(Handler handler, String couponId, MsgCallBack msgCallBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", couponId);
//        String url = Configration.SERVER + Configration.HttpString.COUPON_UNCOLLECT;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.COUPON_UNCOLLECT, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, myCallBack));
//    }
//
//    /**
//     * 接口名 ：获取收藏的coupon列表
//     * jeccyzhang 2016-03-28
//     * 参数：无
//     * 返回值：CountryListObject.list列表
//     *
//     * @param msgCallBack
//     * @param handler
//     */
//    public void getCouponListOfUser(Handler handler, MsgCallBack msgCallBack) {
//        String url = Configration.SERVER + Configration.HttpString.COUPON_LIST_OF_USER;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.COUPON_LIST_OF_USER, handler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 接口名;获取某一商场下店铺的品牌与分类列表
//     * jerryzhang 2016-03-28
//     * 参数：id（商场id）
//     * 返回值 ：MallDetailInfo对象
//     *
//     * @param msgCallBack
//     * @param handler
//     * @param cityId
//     */
//    public void getStoreBrandsCategoryOfCity(Handler handler, String cityId, MsgCallBack msgCallBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", cityId);
//        String url = Configration.SERVER + Configration.HttpString.STORE_BRANDS_CATEGORY_OF_CITY;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.STORE_BRANDS_CATEGORY_OF_CITY, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 接口名：获取支持离线的城市列表
//     * jerryzhang 2016-03-28
//     * 参数： 无
//     * 返回值： CountryListObject.list列表
//     *
//     * @param msgCallBack
//     * @param handler
//     */
//    public void getOffLineList(Handler handler, MsgCallBack msgCallBack) {
//        String url = Configration.SERVER + Configration.HttpString.OFF_LINE_LIST;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.OFF_LINE_LIST, handler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 接口名：获取单个城市的离线包
//     * jerryzhang2016-03-28
//     * 参数 id（城市id）
//     * 返回值：CityListInfo对象
//     *
//     * @param msgCallBack
//     * @param handler
//     * @param cityId
//     */
//    public void getOffLineDownload(Handler handler, String cityId, MsgCallBack msgCallBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", cityId);
//        String url = Configration.SERVER + Configration.HttpString.OFF_LINE_DOWNLOAD;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.OFF_LINE_DOWNLOAD, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
////
////    /**
////     * 接口名：获取其他支付方式列表包
////     * chenjian 2016-04-20
////     * 参数 orderNo（订单号）
////     * 返回值：PayModeObject对象
////     */
//    public void getPayModeList(Handler handler, String OrderNo, MsgCallBack msgCallBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("order_no", OrderNo);
//        String url = Configration.SERVER + Configration.HttpString.GET_PAYMODE_LIST;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.GET_PAYMODE_LIST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
////
////    /**
////     * 接口名：招行支付
////     * chenjian 2016-04-20
////     * 参数 orderNo（订单号）
////     * 返回值：CMBPayInfo对象
////     */
//    public void goToCmbPay(Handler handler, String OrderNo, MsgCallBack msgCallBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("order_no", OrderNo);
//        String url = Configration.SERVER + Configration.HttpString.GO_CMB_PAY;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.GO_CMB_PAY, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_POST, myCallBack));
//    }
////
////    /**
////     * 接口名：首页轮播图
////     * chenjian 2016-05-31
////     * 参数 菜单编号，7:首页
////     * 返回值：MainBannerInfo
////     */
//    public void getMainBanner(Handler handler, MsgCallBack msgCallBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", "7");
//        String url = Configration.SERVER + Configration.HttpString.Main_BANNER;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.MAIN_BANNER, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
////
////    /**
////     * 接口名：闪购
////     * chenjian 2016-05-31
////     */
//    public void getMainFlashSale(Handler handler, MsgCallBack msgCallBack) {
//        String url = Configration.SERVER + Configration.HttpString.Main_FLASH_SALE;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.MAIN_FLASH_SALE, handler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 接口名：获取首页活动图片
//     * chenjian 2016-05-31
//     */
//    public void getMainActImage(Handler handler, MsgCallBack msgCallBack) {
//        String url = Configration.SERVER + Configration.HttpString.Main_ACT_IMAGE;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.MAIN_ACT_IMAGE, handler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, myCallBack));
//    }

    /**
     * 接口名：获取专题列表
     * chenjian 2016-6-7
     */
//    public void getThematicList(Handler handler, MsgCallBack msgCallBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("type", "2");
//        String url = Configration.SERVER + Configration.HttpString.Main_Thematic_List;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.Main_Thematic_List, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }

    /**
     * 改版新增接口，
     * tomyang， 2016-06-12
     * 获取某一商城详细列表数据，获取筛选条件 1001-->首页进入 1002-->二级分类进入 1003-->热门商城进入
     */
//    public void getNewMallHomeLimitOption(int type, int id, Handler mHandler, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", id);
//        String url = "";
//        switch (type) {
//            case MALL_HOME:
//                url = Configration.SERVER + Configration.HttpString.MALL_LIMIT_BRAND;
//                break;
//            case SECONDE_TYPE:
//                url = Configration.SERVER + Configration.HttpString.CLASSIFY_LIMIT_BRAND;
//                break;
//            case HOT_GOODS:
//                url = Configration.SERVER + Configration.HttpString.HOTGOODS_LIMIT_BRAND;
//                break;
//        }
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.MALL_LIMIT_OPTION, mHandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, mCallBack));
//    }

    /**
     * 改版新增接口
     * tomyang 2016-06-12
     * 接口名：搜索之后的商品的过滤选项
     * 参数: ParaObject
     * 返回值; data 选项数组
     */
//    public void getNewSearchLimitList(Handler mhandler, String word, MsgCallBack callBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("word", word);
//        String url = Configration.SERVER + Configration.HttpString.SEARCH_BRAND;
//        MyCallBack myCallBack = new MyCallBack(callBack, JsonPraser.SEARCH_OPTION, mhandler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }

    /**
     * 接口名：获取每日推荐
     * chenjian 2016-6-7
     */

//    public void getDaliyRecList(Handler handler, MsgCallBack msgCallBack) {
//        String url = Configration.SERVER + Configration.HttpString.Main_DaliyRec_List;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.Main_DaliyRec_List, handler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, myCallBack));
//    }

//    /**
//     * 接口名：获取品牌推荐
//     * chenjian 2016-6-7
//     */
//    public void getBrandsRecList(Handler handler, int offset, MsgCallBack msgCallBack) {
//        String url = Configration.SERVER + Configration.HttpString.Main_BrandsRec_List;
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("offset", offset);
//        maps.put("limit", 20);
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.Main_BrandsRec_List, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 改版新增接口：获取一级热门分类
//     * amymu 2016-6-20
//     *
//     * @param handler
//     * @param msgCallBack
//     */
//    public void getHotCategoryFirst(Handler handler, MsgCallBack msgCallBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("version", 1);
//        String url = Configration.SERVER + Configration.HttpString.HOT_CATEGORY_FIRST;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.HOT_CATEGORY_FIRST, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }

    /**
     * 改版新增接口：获取二三级热门分类
     * amymu 2016-6-20
     *
     * @param handler
     * @param firstId     一级热门分类的编号
     * @param msgCallBack
     */
//    public void getHotCategorySecondThird(Handler handler, int firstId, MsgCallBack msgCallBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", firstId);
//        String url = Configration.SERVER + Configration.HttpString.HOT_CATEGORY_SECOND_THIRD;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.HOT_CATEGORY_SECOND_THIRD, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 接口名：获取购物车数量
//     * chenjian 2016-6-7
//     */
//    public void getCarCount(Handler handler, MsgCallBack msgCallBack) {
//        String url = Configration.SERVER + Configration.HttpString.Main_Car_Count;
//        HashMap<String, Object> maps = new HashMap<>();
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.Main_Car_Count, handler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 退出登陆
//     *
//     * @param handler
//     * @param msgCallBack
//     */
//    public void setLoginOut(Handler handler, MsgCallBack msgCallBack) {
//        String url = Configration.SERVER + Configration.HttpString.Login_Out;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.Login_Out, handler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 改版新增接口：获取一二级热门分类
//     * tomyang 2016-7-25
//     *
//     * @param handler
//     * @param msgCallBack
//     */
//    public void getHotCategoryFirstSecond(Handler handler, MsgCallBack msgCallBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("version", 1);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_HOT_CATEGORY_FIRST_SECOND;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.HOT_CATEGORY_FIRST_SECOND, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 改版新增接口：获取三级热门分类
//     * tomyang 2016-7-26
//     *
//     * @param handler
//     * @param msgCallBack
//     */
//    public void getHotCategoryThrid(Handler handler, int secondId, MsgCallBack msgCallBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("id", secondId);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_HOT_CATEGORY_THRID;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.HOT_CATEGORY_THRID, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 改版新增接口：获取使用过得商品
//     * tomyang 2016-7-28
//     *
//     * @param handler
//     * @param msgCallBack
//     */
//    public void getUsedGoods(Handler handler, int offset, MsgCallBack msgCallBack) {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("offset", offset);
//        maps.put("limit", 40);
//        String url = Configration.SERVER + Configration.HttpString.HTTP_WORK_USED_GOODSES;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.WORK_USED_GOODSES, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 获取某一商品下的推荐列表
//     *
//     * @param handler
//     * @param msgCallBack
//     */
//    public void getRecommendGoods(Handler handler, HashMap<String, Object> maps, MsgCallBack msgCallBack) {
//        String url = Configration.SERVER + Configration.HttpString.GET_RECOMMEND_GOODS;
//        MyCallBack myCallBack = new MyCallBack(msgCallBack, JsonPraser.MALL_HOME_DETAIL_GOODS, handler);
//        pool.execute(new HttpRunnable(url, maps, HttpRunnable.HTTP_GET, myCallBack));
//    }
//
//    /**
//     * 获取用户商城红包(个人中心)
//     */
//    public void getRedPackageAll(MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_REDPACKAGE_LIST_ALL;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_REDPACKAGE, handler);
//        pool.execute(new HttpRunnable(url, null, HttpRunnable.HTTP_GET, mCallBack));
//    }
//    /**
//     * 获取用户商城红包（支付界面过来）
//     */
//    public void getRedPackageSku(String json, MsgCallBack callBack, Handler handler) {
//        String url = Configration.SERVER + Configration.HttpString.HTTP_REDPACKAGE_LIST_SKU;
//        MyCallBack mCallBack = new MyCallBack(callBack, JsonPraser.HTTP_TYPE_REDPACKAGE, handler);
//        pool.execute(new HttpRunnable(url, json, mCallBack));
//    }
}
