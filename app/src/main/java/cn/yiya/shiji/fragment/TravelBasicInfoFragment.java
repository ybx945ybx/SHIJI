package cn.yiya.shiji.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;

import cn.yiya.shiji.R;
import cn.yiya.shiji.h5.BridgeHighWebView;

/**
 * Created by Tom on 2016/4/9.
 */
public class TravelBasicInfoFragment extends BaseFragment {
    private BridgeHighWebView mWebview;
    private String des;
    private View mView;

    public static TravelBasicInfoFragment instancePage(String des){
        Bundle args = new Bundle();
        args.putString("des", des);
        TravelBasicInfoFragment pageFragment = new TravelBasicInfoFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        des = getArguments().getString("des");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_basic_info, null);

        initViews();
        initEvents();
        init();

        return mView;
    }

    @Override
    protected void initViews() {
        mWebview = (BridgeHighWebView)mView.findViewById(R.id.webview);
        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebview.loadUrl(des);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void init() {

    }
}
