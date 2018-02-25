package cn.yiya.shiji.netroid;

import com.duowan.mobile.netroid.AuthFailureError;
import com.duowan.mobile.netroid.DefaultRetryPolicy;
import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.request.StringRequest;

import java.util.Map;

public class PostByParamsRequest extends StringRequest {

	private static final String TAG = "PostByParamsRequest";
    private Map<String, String> mParams;

    public PostByParamsRequest(String url, Map<String, String> params, Listener<String> listener) {
        super(Method.POST, url, listener);
        mParams = params;
//        LogUtil.i(TAG, "mParams#" + mParams);
        setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    
    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

}
