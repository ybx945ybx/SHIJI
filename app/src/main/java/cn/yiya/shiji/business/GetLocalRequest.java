package cn.yiya.shiji.business;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.download.FileHelper;
import cn.yiya.shiji.entity.StatusCode;
import cn.yiya.shiji.entity.navigation.CityInfo;
import cn.yiya.shiji.entity.navigation.CityListObject;
import cn.yiya.shiji.entity.navigation.CountryListInfo;
import cn.yiya.shiji.entity.navigation.CountryListObject;
import cn.yiya.shiji.entity.navigation.CouponDetailInfo;
import cn.yiya.shiji.entity.navigation.CouponDetailObject;
import cn.yiya.shiji.entity.navigation.LocalCityInfo;
import cn.yiya.shiji.entity.navigation.LocalCountryInfo;
import cn.yiya.shiji.entity.navigation.LocalObject;
import cn.yiya.shiji.entity.navigation.MallDetailInfo;
import cn.yiya.shiji.entity.navigation.MallDetailObject;
import cn.yiya.shiji.entity.navigation.RecommendInfo;
import cn.yiya.shiji.entity.navigation.RecommendList;
import cn.yiya.shiji.entity.navigation.StoreObject;
import cn.yiya.shiji.entity.navigation.TaxAndInfos;
import cn.yiya.shiji.utils.FileUtil;

/**
 * Created by chenjian on 2016/4/9.
 */
public class GetLocalRequest {

    private static GetLocalRequest mInstance;
//    private static final int LIMIT = 10;

    private static final int LITTLELIMIT = 3;
    private static final int MIDDLELIMIT = 6;
    private static final String PATHHEAD = "file://";
//    private JsonPraser jsonPraser;
    private final static String SAVEFLAG = "saveflag";
    private static final String localImagePath = "/version/navi/list_image/";
    private static final String localFilePath = "/version/navi/country/";

    // city
    private static final String localFilePathCity = "/version/navi/city/";
    // mall
    private static final String localFilePathOfMall = "/version/navi/mall/";
    // coupon
    private static final String localFilePathOfCoupon = "/version/navi/coupon/";
    // store
    private static final String locaFilePathStore = "/version/navi/store/";

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
//                if (msg.code == 100) {
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

    public static GetLocalRequest getInstance() {
        if (mInstance == null) {
            mInstance = new GetLocalRequest();
        }
        return mInstance;
    }

    // 获取本地国家列表
    public void getCountryList(int offset, Handler handler, final MsgCallBack callBack) {
        LocalObject object;
        final HttpMessage msg;
        if (BaseApplication.localObject == null) {
            object = getLocalData();
            if (object == null) {
                msg = new HttpMessage();
                msg.code = 3001;
                msg.obj = "无本地离线包";
                if (callBack != null) {
                    if (handler != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onResult(msg);
                            }
                        });
                    } else {
                        callBack.onResult(msg);
                    }
                }

                return;
            }
        } else {
            object = BaseApplication.localObject;
        }

        CountryListObject cObject = new CountryListObject();
        ArrayList<CountryListInfo> infos = new ArrayList<>();

        ArrayList<LocalCountryInfo> localInfos = object.getPakages();
        for (int i = 0; i < localInfos.size(); i++) {
            if (localInfos.get(i).isbSuccess()) {
                CountryListInfo info = new CountryListInfo();
                info.setCn_name(localInfos.get(i).getCountryCnName());
                info.setName(localInfos.get(i).getCountryName());
                info.setCity_num(localInfos.get(i).getCities().size());
                info.setId(localInfos.get(i).getCountryID());
                String imgPath = getLocalImgPath(localInfos.get(i).getCountryID(), localInfos.get(i).getCities()
                        .get(0).getCityID(), 1);
                info.setCover(imgPath);
                infos.add(info);
            }

        }
        cObject.setList(infos);

        msg = new HttpMessage();
        msg.code = 0;
        msg.obj = cObject;
        if (callBack != null) {
            if (handler != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onResult(msg);
                    }
                });
            } else {
                callBack.onResult(msg);
            }
        }
    }

    // 获取某一个国家信息
    public void getLocalCountry(String countryId, Handler mHandler, final MsgCallBack callBack) {
        LocalObject object;
        if (BaseApplication.localObject == null) {
            object = getLocalData();
            if (object == null) {
                noLocationObject(mHandler, callBack);
                return;
            }
        } else {
            object = BaseApplication.localObject;
        }

        CountryListInfo info = new CountryListInfo();
        ArrayList<LocalCountryInfo> localInfos = object.getPakages();

        String cityId;
        for (int i = 0; i < localInfos.size(); i++) {
            if (countryId.equals(localInfos.get(i).getCountryID())) {
                if (localInfos.get(i).isbSuccess()) {
                    info.setId(countryId);
                    info.setCn_name(localInfos.get(i).getCountryCnName());
                    info.setName(localInfos.get(i).getCountryName());

                    String imgPath = getLocalImgPath(localInfos.get(i).getCountryID(), localInfos.get(i).getCities()
                            .get(0).getCityID(), 1);
                    info.setCover(imgPath);

                    String id = "id=" + countryId;
                    String base64 = android.util.Base64.encodeToString(id.getBytes(), Base64.NO_WRAP);

                    String json = getLocalJson("detail-info", localFilePath, base64, localInfos.get(i).getCountryID(), localInfos.get(i).getCities()
                            .get(0).getCityID());

                    cityId = localInfos.get(i).getCities().get(0).getCityID();

                    if (!TextUtils.isEmpty(json)) {
                        CountryListInfo jInfo = new Gson().fromJson(json, CountryListInfo.class);

                        // jerry
                        for (int j = 0; j < jInfo.getInfos().size(); j++) {
                            String htmlPath = getLocalLogoPath(countryId, cityId, jInfo.getInfos().get(j).getDes());
                            String localHtmlPath = PATHHEAD + htmlPath;
                            jInfo.getInfos().get(j).setDes(localHtmlPath);
                        }

                        String taxHtmlPath = getLocalLogoPath(countryId, cityId, jInfo.getTax().getContent());
                        String localTAxHtmlPath = PATHHEAD + taxHtmlPath;
                        jInfo.getTax().setContent(localTAxHtmlPath);


                        if (jInfo != null) {
                            info.setBrief(jInfo.getBrief());
                            info.setInfos(jInfo.getInfos());
                            info.setTax(jInfo.getTax());
                            info.setCoordinate(jInfo.getCoordinate());
                        }

                    }
                }

            }
        }

        final HttpMessage msg = new HttpMessage();
        msg.code = 0;
        msg.obj = info;
        if (callBack != null) {
            if (mHandler != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onResult(msg);
                    }
                });
            } else {
                callBack.onResult(msg);
            }
        }

    }

    // 获取某个国家下的城市列表
    public void getCityList(int offset, String countryId, Handler mHandler, final MsgCallBack callBack) {
        LocalObject object;
        if (BaseApplication.localObject == null) {
            object = getLocalData();
            if (object == null) {
                noLocationObject(mHandler, callBack);
                return;
            }
        } else {
            object = BaseApplication.localObject;
        }

        CityListObject cObject = new CityListObject();
        ArrayList<CityInfo> cityInfo = new ArrayList<>();

        ArrayList<LocalCountryInfo> localInfos = object.getPakages();
        for (int i = 0; i < localInfos.size(); i++) {
            if (countryId.equals(localInfos.get(i).getCountryID())) {
                if (localInfos.get(i).isbSuccess()) {
                    ArrayList<LocalCityInfo> localInfo = localInfos.get(i).getCities();
                    for (int j = 0; j < localInfo.size(); j++) {
                        if (localInfo.get(j).isbSuccess()) {
                            CityInfo cInfo = new CityInfo();
                            cInfo.setName(localInfo.get(j).getCityName());
                            cInfo.setCn_name(localInfo.get(j).getCityCnName());
                            cInfo.setId(localInfo.get(j).getCityID());
                            String imgPath = getLocalImgPath(localInfos.get(i).getCountryID(), localInfo.get(j).getCityID(), 0);
                            cInfo.setCover(imgPath);
                            cityInfo.add(cInfo);
                        }
                    }

                    cObject.setList(cityInfo);
                }

            }
        }

        final HttpMessage msg = new HttpMessage();
        msg.code = 0;
        msg.obj = cObject;
        if (callBack != null) {
            if (mHandler != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onResult(msg);
                    }
                });
            } else {
                callBack.onResult(msg);
            }
        }
    }

    // 获取本地单个国家退税信息
    public void getTaxInfo(String countryId, Handler mHandler, final MsgCallBack callBack) {
        LocalObject object;
        if (BaseApplication.localObject == null) {
            object = getLocalData();
            if (object == null) {
                noLocationObject(mHandler, callBack);
                return;
            }
        } else {
            object = BaseApplication.localObject;
        }

        TaxAndInfos tInfo = new TaxAndInfos();

        ArrayList<LocalCountryInfo> localInfos = object.getPakages();
        for (int i = 0; i < localInfos.size(); i++) {
            if (countryId.equals(localInfos.get(i).getCountryID())) {

                String id = "id=" + countryId;
                String base64 = android.util.Base64.encodeToString(id.getBytes(), Base64.NO_WRAP);

                String json = getLocalJson("tax-rebate-info", localFilePath, base64, localInfos.get(i).getCountryID(), localInfos.get(i).getCities()
                        .get(0).getCityID());

                String htmlPath = getLocalLogoPath(countryId, localInfos.get(i).getCities().get(0).getCityID(), tInfo.getBrief());
                String localHtmlPath = PATHHEAD + htmlPath;
                tInfo.setBrief(localHtmlPath);


                if (!TextUtils.isEmpty(json)) {
                    tInfo = new Gson().fromJson(json, TaxAndInfos.class);
                }
            }
        }

        final HttpMessage msg = new HttpMessage();
        msg.code = 0;
        msg.obj = tInfo;
        if (callBack != null) {
            if (mHandler != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onResult(msg);
                    }
                });
            } else {
                callBack.onResult(msg);
            }
        }
    }

    // 获取本地必买推荐
    public void getRecommendInfo(String countryId, Handler mHandler, final MsgCallBack callBack) {
        LocalObject object;
        if (BaseApplication.localObject == null) {
            object = getLocalData();
            if (object == null) {
                noLocationObject(mHandler, callBack);
                return;
            }
        } else {
            object = BaseApplication.localObject;
        }

        RecommendList mObject = new RecommendList();
        ArrayList<LocalCountryInfo> localInfos = object.getPakages();
        for (int i = 0; i < localInfos.size(); i++) {
            if (countryId.equals(localInfos.get(i).getCountryID())) {

                String id = "id=" + countryId;
                String base64 = android.util.Base64.encodeToString(id.getBytes(), Base64.NO_WRAP);

                String json = getLocalJson("recommend-info", localFilePath, base64, localInfos.get(i).getCountryID(), localInfos.get(i).getCities()
                        .get(0).getCityID());

                if (!TextUtils.isEmpty(json)) {
                    RecommendList getObject = new Gson().fromJson(json, RecommendList.class);
                    for (int j = 0; j < getObject.getList().size(); j++) {
                        RecommendInfo dInfo = getObject.getList().get(j);
                        String imgUrl = getLocalLogoPath(countryId, localInfos.get(i).getCities()
                                .get(0).getCityID(), dInfo.getLogo());
                        dInfo.setLogo(imgUrl);
                    }

                    mObject = getObject;
                }
            }
        }

        final HttpMessage msg = new HttpMessage();
        msg.code = 0;
        msg.obj = mObject;
        if (callBack != null) {
            if (mHandler != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onResult(msg);
                    }
                });
            } else {
                callBack.onResult(msg);
            }
        }
    }


    //  获取本地数据
    private LocalObject getLocalData() {
        Context mContext = BaseApplication.getInstance().getApplicationContext();
        SharedPreferences sp = mContext.getSharedPreferences(Configration.SHAREDPREFERENCE, mContext.MODE_PRIVATE);
        String jsonSave = sp.getString(SAVEFLAG, "");
        LocalObject object;
        if (TextUtils.isEmpty(jsonSave)) {
            return null;
        } else {
            object = new Gson().fromJson(jsonSave, LocalObject.class);
            BaseApplication.localObject = object;
        }

        if (object == null || object.getPakages() == null) {
            return null;
        }

        return object;
    }

    private String getLocalImgPath(String country, String city, int type) {
        String temp = type > 0 ? "country_" : "city_";
        String tempId = type > 0 ? country : city;

        return FileHelper.getFileDefaultPath() + "/" + country
                + "/" + city + localImagePath + temp + tempId;

    }

    public static String getLocalLogoPath(String country, String city, String path) {
        return FileHelper.getFileDefaultPath() + "/" + country
                + "/" + city + "/version/" + path;
    }

    // 通过接口名字和对应ID获取本地JSON数据
    private String getLocalJson(String url, String path, String base64, String country, String city) {
        String temp = url + "?" + base64;
        String file = FileHelper.getFileDefaultPath() + "/" + country
                + "/" + city + path + temp;
        Log.i("path", file);
        String json = FileUtil.readFile(file);
        if (json == null) {
            return "";
        }

        try {
            JSONObject obj = new JSONObject(json);
            int code = obj.getInt("code");
            String data = IsNull(obj.getString("data"));

            if (code == 0) {
                return data;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    private String IsNull(String object) {
        if (object == null || object.equals("[]")) {
            return null;
        }

        return object;
    }


    // 获得本地某一城市下的mall列表
    public void getLocalMallList(String cityId, Handler mHandler, String countryId, int offset, final MsgCallBack callBack) {
        LocalObject object;
        if (BaseApplication.localObject == null) {
            object = getLocalData();
            if (object == null) {
                noLocationObject(mHandler, callBack);
                return;
            }
        } else {
            object = BaseApplication.localObject;
        }

        MallDetailObject mallDetailObject = new MallDetailObject();
        String id = "id=" + cityId + "&limit=" + LITTLELIMIT + "&offset=" + offset;
        String base64 = android.util.Base64.encodeToString(id.getBytes(), Base64.NO_WRAP);
        String json = getLocalJson("list-ofcity", localFilePathOfMall, base64, countryId, cityId);   //
        if (!TextUtils.isEmpty(json)) {
            MallDetailObject mallDetailObject1 = new Gson().fromJson(json, MallDetailObject.class);
            for (int i = 0; i < mallDetailObject1.list.size(); i++) {
                String imgPath = getLocalLogoPath(countryId, cityId, mallDetailObject1.list.get(i).getCover());
                mallDetailObject1.list.get(i).setCover(imgPath);
            }
            mallDetailObject = mallDetailObject1;
        }
        final HttpMessage msg = new HttpMessage();
        msg.code = 0;
        msg.obj = mallDetailObject;
        if (callBack != null) {
            if (mHandler != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onResult(msg);
                    }
                });
            } else {
                callBack.onResult(msg);
            }
        }
    }

    // 获得mall的详情
    public void getLocalMallInfo(Handler mHandler, String mallId, String countryId, String cityId, final MsgCallBack callBack) {
        LocalObject object;
        if (BaseApplication.localObject == null) {
            object = getLocalData();
            if (object == null) {
                noLocationObject(mHandler, callBack);
                return;
            }
        } else {
            object = BaseApplication.localObject;
        }

        MallDetailInfo mallDetailInfo = new MallDetailInfo();
        String id = "id=" + mallId;
        String base64 = android.util.Base64.encodeToString(id.getBytes(), Base64.NO_WRAP);
        String json = getLocalJson("detail-info", localFilePathOfMall, base64, countryId, cityId);
        if (!TextUtils.isEmpty(json)) {
            mallDetailInfo = new Gson().fromJson(json, MallDetailInfo.class);
            String imgPath = getLocalLogoPath(countryId, cityId, mallDetailInfo.getCover());
            mallDetailInfo.setCover(imgPath);
        }
        final HttpMessage msg = new HttpMessage();
        msg.code = 0;
        msg.obj = mallDetailInfo;
        if (callBack != null) {
            if (mHandler != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onResult(msg);
                    }
                });
            } else {
                callBack.onResult(msg);
            }
        }
    }

    // 某一商场下城市简介列表
    public void getCityBriefInfo(String type, String cityId, String countryId, Handler mHandler, final MsgCallBack callBack) {
        LocalObject object;
        if (BaseApplication.localObject == null) {
            object = getLocalData();
            if (object == null) {
                noLocationObject(mHandler, callBack);
                return;
            }
        } else {
            object = BaseApplication.localObject;
        }

        CityInfo cityInfo = new CityInfo();
        String id = "id=" + cityId;
        String base64 = android.util.Base64.encodeToString(id.getBytes(), Base64.NO_WRAP);
        String json = "";
        if (type.equals(Configration.DETAIL)) {                                                      // 城市的简要信息和详细信息
            json = getLocalJson("detail-info", localFilePathCity, base64, countryId, cityId);
        } else {
            json = getLocalJson("brief-info", localFilePathCity, base64, countryId, cityId);
        }

        final HttpMessage msg = new HttpMessage();

        if (!TextUtils.isEmpty(json)) {
            Log.i("json", json);
            cityInfo = new Gson().fromJson(json, CityInfo.class);
            String imgPath = getLocalLogoPath(countryId, cityId, cityInfo.getCover());
            cityInfo.setCover(imgPath);

            if (type.equals(Configration.DETAIL)) {
                for (int i = 0; i < cityInfo.getInfos().size(); i++) {
                    String htmlPath = getLocalLogoPath(countryId, cityId, cityInfo.getInfos().get(i).getDes());
                    String localHtmlPath = PATHHEAD + htmlPath;
                    cityInfo.getInfos().get(i).setDes(localHtmlPath);
                }
            }

            msg.code = 0;
            msg.obj = cityInfo;
        }
        else{
            msg.code = 0;
            msg.obj = cityInfo;
        }




        if (callBack != null) {
            if (mHandler != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onResult(msg);
                    }
                });
            } else {
                callBack.onResult(msg);
            }
        }
    }


    // 加载本地的coupon券列表
    public void getLocalCouponList(String type, String typeId, String cityId, Handler mHandler, String countryId, final MsgCallBack callBack) {
        LocalObject object;
        if (BaseApplication.localObject == null) {
            object = getLocalData();
            if (object == null) {
                noLocationObject(mHandler, callBack);
                return;
            }
        } else {
            object = BaseApplication.localObject;
        }

        CouponDetailObject couponDetailObject = new CouponDetailObject();
        String id = "id=" + typeId;         // coupon列表的三个来源（city,mall和store）
        String base64 = android.util.Base64.encodeToString(id.getBytes(), Base64.NO_WRAP);
        String json = "";
        switch (type) {                                                                             // type分为城市、mall、store
            case Configration.CITY:
                json = getLocalJson("list-ofcity", localFilePathOfCoupon, base64, countryId, cityId);
                break;
            case Configration.MALL:
                json = getLocalJson("list-ofmall", localFilePathOfCoupon, base64, countryId, cityId);
                break;
            case Configration.STORE:
                json = getLocalJson("list-ofstore", localFilePathOfCoupon, base64, countryId, cityId);
                break;
        }
        if (!TextUtils.isEmpty(json)) {
            couponDetailObject = new Gson().fromJson(json, CouponDetailObject.class);
            for (int i = 0; i < couponDetailObject.list.size(); i++) {
                String imgPath = getLocalLogoPath(countryId, cityId, couponDetailObject.list.get(i).getCover());
                couponDetailObject.list.get(i).setCover(imgPath);
            }
        }

        final HttpMessage msg = new HttpMessage();
        msg.code = 0;
        msg.obj = couponDetailObject;
        if (callBack != null) {
            if (mHandler != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onResult(msg);
                    }
                });
            }
        }
    }

    // 加载本地的coupon详情
    public void getLocalCouponDetail(String couponId, Handler mHandler, String countryId, String cityId, final MsgCallBack callBack) {
        LocalObject object;
        if (BaseApplication.localObject == null) {
            object = getLocalData();
            if (object == null) {
                noLocationObject(mHandler, callBack);
                return;
            }
        } else {
            object = BaseApplication.localObject;
        }

        CouponDetailInfo couponDetailInfo = new CouponDetailInfo();
        // coupon列表的三个来源（city,mall和store）
        String id = "id=" + couponId;
        String base64 = android.util.Base64.encodeToString(id.getBytes(), Base64.NO_WRAP);

        String json = getLocalJson("detail-info", localFilePathOfCoupon, base64, countryId, cityId);
        if (!TextUtils.isEmpty(json)) {
            couponDetailInfo = new Gson().fromJson(json, CouponDetailInfo.class);
            String imgPath = getLocalLogoPath(countryId, cityId, couponDetailInfo.getCover());
            couponDetailInfo.setCover(imgPath);
        }

        final HttpMessage msg = new HttpMessage();
        msg.code = 0;
        msg.obj = couponDetailInfo;
        if (callBack != null) {
            if (mHandler != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onResult(msg);
                    }
                });
            }
        }

    }

    // 获取本地的store列表
    public void getLocalStoreList(String type, String typeId, Handler mHandler, String countryId, String cityId, int offset, final MsgCallBack callBack) {
        LocalObject object;
        if (BaseApplication.localObject == null) {
            object = getLocalData();
            if (object == null) {
                noLocationObject(mHandler, callBack);
                return;
            }
        } else {
            object = BaseApplication.localObject;
        }

        StoreObject storeObject = new StoreObject();
        String id = "count=1" + "&id=" + typeId + "&limit=" + MIDDLELIMIT + "&offset=" + offset;
        String base64 = android.util.Base64.encodeToString(id.getBytes(), Base64.NO_WRAP);
        String json = "";
        switch (type) {                                                                             // type分为城市、mall、store
            case Configration.CITY:
                json = getLocalJson("list-ofcity", locaFilePathStore, base64, countryId, cityId);
                break;
            case Configration.MALL:
                json = getLocalJson("list-ofmall", locaFilePathStore, base64, countryId, cityId);
                break;
        }
        if (!TextUtils.isEmpty(json)) {
            storeObject = new Gson().fromJson(json, StoreObject.class);
            for (int i = 0; i < storeObject.list.size(); i++) {
                String imgPath = getLocalLogoPath(countryId, cityId, storeObject.list.get(i).getCover());
                storeObject.list.get(i).setCover(imgPath);
            }
        }
        final HttpMessage msg = new HttpMessage();
        msg.code = 0;
        msg.obj = storeObject;
        if (callBack != null) {
            if (mHandler != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onResult(msg);
                    }
                });
            }
        }
    }

    // 获得本地store详情
    public void getLocalStoreInfo(String storeId, Handler mHandler, String countryId, String cityId, final MsgCallBack callBack) {
        LocalObject object;
        if (BaseApplication.localObject == null) {
            object = getLocalData();
            if (object == null) {
                noLocationObject(mHandler, callBack);
                return;
            }
        } else {
            object = BaseApplication.localObject;
        }

        MallDetailInfo storeLongInfo = new MallDetailInfo();
        String id = "id=" + storeId;
        String base64 = android.util.Base64.encodeToString(id.getBytes(), Base64.NO_WRAP);
        String json = getLocalJson("detail-info", locaFilePathStore, base64, countryId, cityId);
        Log.i("json=", json);
        if (!TextUtils.isEmpty(json)) {
            storeLongInfo = new Gson().fromJson(json, MallDetailInfo.class);
            String imgPath = getLocalLogoPath(countryId, cityId, storeLongInfo.getCover());
            storeLongInfo.setCover(imgPath);
        }

        final HttpMessage msg = new HttpMessage();
        msg.code = 0;
        msg.obj = storeLongInfo;
        if (callBack != null) {
            if (mHandler != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onResult(msg);
                    }
                });
            }
        }
    }

    private void noLocationObject(Handler handler, final MsgCallBack callBack){
        final HttpMessage msg = new HttpMessage();
        msg.code = StatusCode.NoLocationData;
        msg.obj = "无本地离线包";
        if (callBack != null) {
            if (handler != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onResult(msg);
                    }
                });
            } else {
                callBack.onResult(msg);
            }
        }
    }

}
