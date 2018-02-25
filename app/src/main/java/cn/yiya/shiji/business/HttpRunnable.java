package cn.yiya.shiji.business;

public class HttpRunnable implements Runnable {

//    private HttpClientUtils httpUtils;
//    private String url;
//    private Map<String, Object> maps;
//    private int httpType;
//    private CallBack callBack;
//    private int contentType = 0;
//    private String json;
//    public static final int HTTP_GET = 0;
//    public static final int HTTP_POST = 1;
//
//    public HttpRunnable(String url, Map<String, Object> maps, int httpType, CallBack callBack) {
//        super();
//        this.url = url;
//        this.maps = maps;
//        this.httpUtils = HttpClientUtils.getInstance();
//        this.httpType = httpType;
//        this.callBack = callBack;
//    }
//
//    public HttpRunnable(String url, String json, CallBack callBack) {
//        super();
//        this.url = url;
//        this.json = json;
//        this.httpUtils = HttpClientUtils.getInstance();
//        this.httpType = HTTP_POST;
//        this.callBack = callBack;
//        this.contentType = HttpClientUtils.ContentTypeJson;
//    }
//
//    public HttpRunnable(String url, CallBack callBack) {
//        this.url = url;
//        this.httpType = HTTP_GET;
//        this.callBack = callBack;
//    }

    @Override
    public void run() {
//        String result = "";
//        if (httpType == HTTP_POST) {
//            if (contentType == HttpClientUtils.ContentTypeForm) {
//                result = httpUtils.doPost(url, maps, contentType);
//            } else if (contentType == HttpClientUtils.ContentTypeJson) {
//                result = httpUtils.doPost(url, json, HttpClientUtils.ContentTypeJson);
//            } else {
//                result = "Unknow contentType";
//            }
//        } else {
//
//            result = httpUtils.doGet(url, maps);
//            Log.i("ZYL", "result" + result);
//        }
//        if (callBack != null) {
//            callBack.onResult(result);
//        }
    }

//    public static interface CallBack {
//        public void onResult(String result);
//    }
}