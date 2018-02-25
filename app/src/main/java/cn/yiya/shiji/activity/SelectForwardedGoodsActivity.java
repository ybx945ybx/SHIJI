package cn.yiya.shiji.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.SelectForwardGoodsAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.config.Constants;
import cn.yiya.shiji.entity.ForwardGoodsEntity;
import cn.yiya.shiji.entity.GoodsDetailEntity;
import cn.yiya.shiji.entity.ShortUrlEntity;
import cn.yiya.shiji.utils.ShareUtils;
import cn.yiya.shiji.views.ProgressDialog;
import cn.yiya.shiji.views.ShareDialog;

/**
 * Created by Tom on 2016/11/29.
 */

public class SelectForwardedGoodsActivity extends BaseAppCompatActivity implements View.OnClickListener, IWeiboHandler.Response{

    private ImageView ivBack;
    private TextView tvTitle;

    private RecyclerView rycvSelectGoods;
    private SelectForwardGoodsAdapter mAdapter;
    private List<ForwardGoodsEntity> mList = new ArrayList<>();

    private ImageView ivLink;
    private TextView tvLink;
    private LinearLayout llytLink;
    private TextView tvDownLoad;
    private TextView tvCreatForward;

    private List<String> paths = new ArrayList<>();
    private List<String> imgs = new ArrayList<>();
    private Bitmap bitmap;
    private int selectedNum = 0;
    private boolean linkSelected = false;

    private IWeiboShareAPI mWeiboShareAPI = null;

    private String imgsData;
    private String forwardDesc;
    private String identifier;
    private String url;
    private String goodsId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecte_forwarded_goods);
        // 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constants.APP_KEY);
        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        }
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if(intent != null){
            imgsData = intent.getStringExtra("imgs");
            forwardDesc = intent.getStringExtra("forwardDesc");
            url = intent.getStringExtra("url");
//            identifier = intent.getStringExtra("identifier");
        }
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("选择商品图");
        findViewById(R.id.title_right).setVisibility(View.GONE);

        rycvSelectGoods = (RecyclerView) findViewById(R.id.rycv_select_goods);
        rycvSelectGoods.setItemAnimator(new DefaultItemAnimator());
        rycvSelectGoods.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new SelectForwardGoodsAdapter(this);
        rycvSelectGoods.setAdapter(mAdapter);

        llytLink = (LinearLayout) findViewById(R.id.llyt_link);
        ivLink = (ImageView) findViewById(R.id.iv_link);
        tvLink = (TextView) findViewById(R.id.tv_link);
        tvDownLoad = (TextView) findViewById(R.id.tv_download);
        tvCreatForward = (TextView) findViewById(R.id.tv_creat);

    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        llytLink.setOnClickListener(this);
        tvDownLoad.setOnClickListener(this);
        tvCreatForward.setOnClickListener(this);

        mAdapter.setOnItemSelectedListener(new SelectForwardGoodsAdapter.OnItemSelectedListener() {
            @Override
            public void OnItemSelected(int position, boolean selected) {
                mList.get(position).setSelected(selected);
                if(selected) {
                    selectedNum += 1;
                }else {
                    selectedNum -= 1;
                }
            }
        });

    }

    @Override
    protected void init() {
        showPreDialog("");
        GoodsDetailEntity goodsDetailEntity = new Gson().fromJson(imgsData, GoodsDetailEntity.class);
        goodsId = goodsDetailEntity.getGoods().getId();
        if(goodsDetailEntity.getGoods_colors()!= null && goodsDetailEntity.getGoods_colors().size() > 0){
            for(int i = 0; i < goodsDetailEntity.getGoods_colors().size(); i++){
                GoodsDetailEntity.GoodsColorsEntity goodsColorsEntity = goodsDetailEntity.getGoods_colors().get(i);
                if(goodsColorsEntity.getImages() != null && goodsColorsEntity.getImages().size() > 0){
                    for(int j = 0; j < goodsColorsEntity.getImages().size(); j++){
                        ForwardGoodsEntity entity = new ForwardGoodsEntity();
                        entity.setUrl(goodsColorsEntity.getImages().get(j).getImage());
                        mList.add(entity);
                    }
                }

            }
        }
        mAdapter.setmList(mList);
        mAdapter.notifyDataSetChanged();

        // 长链接转换成短连接
        new RetrofitRequest<ShortUrlEntity>(ApiRequest.getApiShiji().getShortUrl(url)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                hidePreDialog();
                if(msg.isSuccess()){
                    ShortUrlEntity entity = (ShortUrlEntity) msg.obj;
                    url = entity.getShort_url();
//                    showTips(url);
                }
            }
        });
//        hidePreDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.llyt_link:
//                linkSelected = !linkSelected;
                ivLink.setSelected(linkSelected = !linkSelected);
                if(linkSelected){
                    tvLink.setTextColor(Color.parseColor("#353535"));
                }else {
                    tvLink.setTextColor(Color.parseColor("#b5b5b5"));
                }
//                if(linkSelected){
//                    linkSelected = false;
//                    ivLink.setSelected(linkSelected);
//                }else {
//                    linkSelected = true;
//                    ivLink.setSelected(linkSelected);
//                }
                break;
            case R.id.tv_download:
                if(selectedNum == 0)
                    showSelectTips("最少添加一张商品图");
                else
                    downloadImage(true);

                break;
            case R.id.tv_creat:
                if(selectedNum == 0){
                    showSelectTips("最少添加一张商品图");
                }else if (selectedNum > 9){
                    showSelectTips("最多选择九张商品图");
                }else {
                    gotoShare();
                }
                break;
        }

    }

    private void getImgs(){
        imgs.clear();
        for (int i = 0; i < mList.size(); i++){
            if(mList.get(i).isSelected()){
               imgs.add(mList.get(i).getUrl() + "?imageView2/2/w/300");
            }
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    showDownloadTips();
                    break;
            }
        }
    };

    // 下载商品图片
    private void downloadImage(final boolean showTips) {
        getImgs();
        showPreDialog("图片处理中");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for(int i = 0; i < imgs.size(); i++){
                        URL downUrl = new URL(imgs.get(i));
                        InputStream is = downUrl.openStream();
                        bitmap = BitmapFactory.decodeStream(is);
                        is.close();
                        saveFile(Configration.FORWARD_PATH, bitmap, "forward" + i + ".png");
                    }
                    hidePreDialog();
                    if(showTips) {
                        mHandler.sendEmptyMessage(0);
                    }else {

                    }
                }catch (Exception e){
                    hidePreDialog();
                }
            }
        }).start();
    }

    // 保存图片
    private void saveFile(String forwardPath, Bitmap bitmap, String s) throws IOException{
        File dirFile = new File(forwardPath);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(forwardPath + s);

        if (!myCaptureFile.exists()) {
            myCaptureFile.createNewFile();
        }

        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(myCaptureFile));

        if (!myCaptureFile.exists()) {
            myCaptureFile.createNewFile();
        }

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bos.flush();
        bos.close();
    }

    // 下载图片完成后弹出提示框
    private void showDownloadTips(){
        showCustomDialog("提示", "图片已下载到手机相册\n文案已复制到剪贴板", "去微信分享", new ProgressDialog.DialogClick(){
            @Override
            public void OkClick() {
                for (int i = 0; i < imgs.size(); i++){
                    paths.add(Configration.FORWARD_PATH + "forward" + i + ".png");
                }
                copy();
                ShareUtils.share9PicToFriendNoSDK(SelectForwardedGoodsActivity.this, "", paths);
            }

            @Override
            public void CancelClick() {

            }
        }, "知道了", new ProgressDialog.DialogClick(){

            @Override
            public void OkClick() {

            }

            @Override
            public void CancelClick() {

            }
        });
    }

    // 图片未选择及超过九张提示
    private void showSelectTips(String tips){
        View layoutTips = LayoutInflater.from(this).inflate(R.layout.forward_custom_toast_layout, (ViewGroup) findViewById(R.id.toast_layout_root));
        TextView textView = (TextView) layoutTips.findViewById(R.id.toast_context);
        textView.setText(tips);
        final Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(layoutTips);
        toast.show();
    }

    // 创建转发弹出dialog
    private void gotoShare() {
        getImgs();
        String content = "";
        if(linkSelected) {
            content = Html.fromHtml(forwardDesc + "<br>" + "购买链接:" + url).toString();
        }else {
            content = Html.fromHtml(forwardDesc).toString();
        }
        new ShareDialog(this, imgs, content, true).build().show();
        copy();
    }

    // 复制文本到剪贴板
    private void copy(){
        String content = "";
        if(linkSelected) {
            content = Html.fromHtml(forwardDesc + "<br>" + "购买链接:" + url).toString();
        }else {
            content = Html.fromHtml(forwardDesc).toString();
        }
        ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        if(baseResponse!= null){
            switch (baseResponse.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    Toast.makeText(this, "转发成功", Toast.LENGTH_LONG).show();
                    new RetrofitRequest<>(ApiRequest.getApiShiji().forwardGoods(goodsId)).handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {

                        }
                    });
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    Toast.makeText(this, "转发取消", Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    Toast.makeText(this, "转发失败", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 111){
            new RetrofitRequest<>(ApiRequest.getApiShiji().forwardGoods(goodsId)).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {

                }
            });
        }
    }
}
