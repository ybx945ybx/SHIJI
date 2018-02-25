package cn.yiya.shiji.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.yiya.shiji.R;

public class AgreementActivity extends BaseAppCompatActivity implements View.OnClickListener{
	private WebView webView;
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agreement);
		initViews();
		initEvents();
		init();
	}

	@Override
	protected void initViews() {
		webView = (WebView) findViewById(R.id.webview);
		webView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
		webSettings.setLoadsImagesAutomatically(true); // 支持自动加载图片
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					// 网页加载完成

				} else {
					// 加载中

				}

			}
		});
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});
		//webView.loadDataWithBaseURL(null, data, mimeType, encoding, failUrl);
		webView.loadUrl("file:///android_asset/agreement.html");
	}

	@Override
	protected void initEvents() {
		findViewById(R.id.back).setOnClickListener(this);
	}

	@Override
	protected void init() {

	}

}
