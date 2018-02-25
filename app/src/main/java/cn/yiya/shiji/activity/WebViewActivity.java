package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ShareTools;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.CustomEvent;
import cn.yiya.shiji.utils.DebugUtil;
import cn.yiya.shiji.views.ShareDialog;

public class WebViewActivity extends BaseAppCompatActivity implements View.OnClickListener {
	private WebView webView;
	private String url;
	String strImageUrl;
	private TextView tvTitle;
	private ImageView ivBack;
	private ImageView ivRight;
	private String title="加载中...";
	private Handler handler;
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.title_right:
			CustomEvent.onEvent(this, ((BaseApplication)getApplication()).getDefaultTracker(), "WebViewActivity", CustomEvent.WebUrlShare);
			String shareUrl = url.substring(0, url.length() - 9);
            DebugUtil.v(shareUrl + "\n" + strImageUrl);
			new ShareDialog(this, title, strImageUrl, shareUrl,title).build().show();
//			ShareTools.getInstance().showShare(this, title, strImageUrl, shareUrl,title);
			break;
		default:
			break;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		initViews();
		initEvents();
		init();
	}

	@Override
	protected void initViews() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			WebView.setWebContentsDebuggingEnabled(true);
		}

		url = getIntent().getStringExtra("url");
		strImageUrl = getIntent().getStringExtra("imageurl");

		ShareTools.getInstance().initShare(this);

		tvTitle = (TextView) findViewById(R.id.title_txt);
		if(!TextUtils.isEmpty(title)){
			tvTitle.setText(title);
		}
		ivBack= (ImageView) findViewById(R.id.title_back);
		ivRight= (ImageView) findViewById(R.id.title_right);
		webView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
		webSettings.setLoadsImagesAutomatically(true); // 支持自动加载图片
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					// 网页加载完成
					if(!TextUtils.isEmpty(title)){
						tvTitle.setText(title);
					}

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

		webSettings.setJavaScriptEnabled(true);

		webView.addJavascriptInterface(this, "control");

		webView.loadUrl(url);

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message message) {
				tvTitle.setText(title);
				return false;
			}
		});
	}

	@Override
	protected void initEvents() {
		ivBack.setOnClickListener(this);
		ivRight.setOnClickListener(this);
	}

	@Override
	protected void init() {

	}

	@JavascriptInterface
	public void postWork(int tagid,String tagname){
		Intent intent1 = new Intent(WebViewActivity.this, PublishWorkActivity.class);
        intent1.putExtra("tagname", tagname);
		startActivity(intent1);
	}

	@JavascriptInterface
	public void showWorkDetail(String work_id){
		Intent intent1 = new Intent(WebViewActivity.this, NewWorkDetailsActivity.class);
		intent1.putExtra("work_id", Integer.parseInt(work_id));
		startActivity(intent1);
	}

	@JavascriptInterface
	public void setTitle(String title){
		this.title = title;
		new Thread(){
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		}.start();
	}

	/**
	 * 跳转搭配详情
	 */
	@JavascriptInterface
	public void gotoPairDetail (String workId) {
		Intent intent = new Intent(this, NewMatchDetailActivity.class);
		try {
			JSONObject jsonObject = new JSONObject(workId);
			intent.putExtra("work_id", jsonObject.getInt("workId"));
			startActivity(intent);
		}catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onBackPressed() {
		finish();
	}

	private void goBackKey() {
		if (webView.canGoBack()) {
			webView.goBack();
		} else {
			setResult(RESULT_CANCELED);
			finish();
		}
	}
}
