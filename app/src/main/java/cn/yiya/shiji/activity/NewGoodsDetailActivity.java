package cn.yiya.shiji.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.NewGoodsDetailAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.business.YiChuangYun;
import cn.yiya.shiji.entity.ActivityListObject;
import cn.yiya.shiji.entity.CarCountInfo;
import cn.yiya.shiji.entity.GoodsDetailEntity;
import cn.yiya.shiji.entity.GoodsTranlateDesc;
import cn.yiya.shiji.entity.GoodsUpdateInfo;
import cn.yiya.shiji.entity.GoodsUpdateStatusInfo;
import cn.yiya.shiji.entity.MallGoodsDetailObject;
import cn.yiya.shiji.entity.TagProduceObject;
import cn.yiya.shiji.entity.VisitGoodsEntity;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.GoodsBuyDialog;
import cn.yiya.shiji.views.ShareDialog;

/**
 * Created by Tom on 2016/11/3.
 */
public class NewGoodsDetailActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private RelativeLayout rlRoot;
    // 标题栏部分
    private ImageView ivBack;
    private ImageView ivCollect;
    private ImageView ivShare;
    private RelativeLayout rlytCart;
    private TextView tvCartCount;

    // 商品详情列表
    private RecyclerView rycvGoodsDetail;
    private NewGoodsDetailAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;

    // 底部购买
    private LinearLayout llytBottom;
    private TextView tvDrictBuy;
    private TextView tvAddShoppingCart;
    private TextView tvSoldOut;

    private String goodsId;
    private static int REQUEST_LOGIN = 100;
    private boolean isGoodsCollected;

    private JsonObject goodsJson;
    private GoodsDetailEntity goodsDetailEntity = new GoodsDetailEntity();
    private JsonElement skusJson;

    private boolean beBrandAct;
    private boolean beMatchList;
    private boolean beWorkList;
    private boolean beVisitList;
    private boolean beRecommendList;

    private ActivityListObject brandActObj;
    private TagProduceObject matchObj;
    private TagProduceObject workObj;
    private VisitGoodsEntity visitObj;
    private MallGoodsDetailObject recommendObj;

    private String shareUrl;
    private String shareImageUrl;
    private String shareTitle;
    private String shareContent;

    private boolean isLogin;
    private int brandFollow;
    private boolean isGroupBuy;

    private Dialog goodsBuyDialog;
    private String translat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goods_detail);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            goodsId = intent.getStringExtra("goodsId");
            int group = intent.getIntExtra("group", 0);
            if (group == 1) {
                isGroupBuy = true;
            } else {
                isGroupBuy = false;
            }
        }
    }

    @Override
    protected void initViews() {
        rlRoot = (RelativeLayout) findViewById(R.id.layout_root);
        ivBack = (ImageView) findViewById(R.id.title_back);
        ivCollect = (ImageView) findViewById(R.id.iv_collect);
        ivShare = (ImageView) findViewById(R.id.iv_share);
        rlytCart = (RelativeLayout) findViewById(R.id.toolbar_right_layout);
        tvCartCount = (TextView) findViewById(R.id.toolbar_right_count);
        rycvGoodsDetail = (RecyclerView) findViewById(R.id.rycv_goods_detail);
        rycvGoodsDetail.setItemAnimator(new DefaultItemAnimator());
        linearLayoutManager = new LinearLayoutManager(this);
        rycvGoodsDetail.setLayoutManager(linearLayoutManager);

        llytBottom = (LinearLayout) findViewById(R.id.llyt_bottom);
        tvDrictBuy = (TextView) findViewById(R.id.tv_drict_buy);
        tvAddShoppingCart = (TextView) findViewById(R.id.tv_add_shopping_cart);
        tvSoldOut = (TextView) findViewById(R.id.tv_sold_out);
        if (isGroupBuy) {
            tvAddShoppingCart.setVisibility(View.GONE);
        }

        addDefaultNullView();
        initOffLineView(this);

    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        ivCollect.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        rlytCart.setOnClickListener(this);
        tvDrictBuy.setOnClickListener(this);
        tvAddShoppingCart.setOnClickListener(this);
        tvSoldOut.setOnClickListener(this);

    }

    @Override
    protected void init() {
        checkLogin();
        getGoodsDetail();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.tv_reload:
                init();
                break;
            case R.id.iv_collect:
                if (isLogin) {
                    collectGoods();
                } else {
                    gotoLogin();
                }
                break;
            case R.id.iv_share:
                gotoShare();
                break;
            case R.id.toolbar_right_layout:
                if (isLogin) {
                    gotoShoppingCart();
                } else {
                    gotoLogin();
                }
                break;
            case R.id.tv_drict_buy:
                if (isLogin) {
                    showBuyPopWindow(1);
                } else {
                    gotoLogin();
                }
                break;
            case R.id.tv_add_shopping_cart:
                if (isLogin) {
                    showBuyPopWindow(2);
                } else {
                    gotoLogin();
                }
                break;
            case R.id.tv_sold_out:
                // 滚动至推荐商品
                linearLayoutManager.scrollToPositionWithOffset(9,0);
                break;
        }

    }

    private void gotoLogin() {
        Intent intent = new Intent(NewGoodsDetailActivity.this, LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
        overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);

    }

    //  添加购物车及直接购买弹出
    private void showBuyPopWindow(int type) {
        goodsBuyDialog = new GoodsBuyDialog(this, goodsJson, skusJson, type, isGroupBuy).build();
        goodsBuyDialog.show();
    }

    //  跳至转发售卖界面
    private void gotoForward() {

        String imgs = new Gson().toJson(goodsDetailEntity);
        String forwardDesc = goodsDetailEntity.getGoods().getTitle();
        String url = goodsDetailEntity.getGoods().getShare_url();
        String identifier = goodsDetailEntity.getGoods().getIdentifier();

        Intent intent = new Intent(this, SelectForwardedGoodsActivity.class);
        intent.putExtra("imgs", imgs);
        intent.putExtra("forwardDesc", forwardDesc + "<br>" + "商品编号:" + identifier + "<br>" + SimpleUtils.transHtml(translat));
        intent.putExtra("url", url);
//        intent.putExtra("identifier", identifier);
        startActivity(intent);
    }

    // 跳转购物车
    private void gotoShoppingCart() {
        Intent intentGoShopCart = new Intent(NewGoodsDetailActivity.this, NewShoppingCartActivity.class);
        startActivity(intentGoShopCart);
    }

    // 弹出分享框
    private void gotoShare() {
        new ShareDialog(this, shareTitle, shareImageUrl, shareUrl, shareContent).build().show();
    }

    // 收藏和取消收藏商品
    private void collectGoods() {
        if (isGoodsCollected) {
            new RetrofitRequest<>(ApiRequest.getApiShiji().deleteWishGoods(goodsId)).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if (msg.isSuccess()) {
                        isGoodsCollected = false;
                        ivCollect.setSelected(false);
                        showCollectGoods(false);
                    }
                }
            });
        } else {
            new RetrofitRequest<>(ApiRequest.getApiShiji().followGoods(goodsId)).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if (msg.isSuccess()) {
                        isGoodsCollected = true;
                        ivCollect.setSelected(true);
                        showCollectGoods(true);
                    }
                }
            });
        }
    }

    // 打开客服
    private void gotoService() {
        showPreDialog("正在加载客服系统");
        YiChuangYun.openKF5(YiChuangYun.KF5Chat, NewGoodsDetailActivity.this, YiChuangYun.GOODES_DETAIL, goodsDetailEntity.getGoods().getId(), new YiChuangYun.onFinishInitListener() {
            @Override
            public void onFinishInit() {
                hidePreDialog();
            }
        });
    }

    // 取消和关注品牌
    private void followBrand(final ImageView fbFollowBrand) {
        if (brandFollow == 1) {
            new RetrofitRequest<>(ApiRequest.getApiShiji().postUnFollowBrands(String.valueOf(goodsDetailEntity.getBrand().getTag_id()))).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if (msg.isSuccess()) {
                        brandFollow = 2;
                        fbFollowBrand.setImageResource(R.mipmap.ic_tofollow);
                    }
                }
            });
        } else {
            new RetrofitRequest<>(ApiRequest.getApiShiji().postFollowBrands(String.valueOf(goodsDetailEntity.getBrand().getTag_id()))).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if (msg.isSuccess()) {
                        brandFollow = 1;
                        fbFollowBrand.setImageResource(R.mipmap.ic_followed);
                    }
                }
            });
        }
    }

    // 判断是否登录
    private void checkLogin() {
        new RetrofitRequest<>(ApiRequest.getApiShiji().isLogin()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    initCartCount();
                    isLogin = true;
                } else if (msg.isLossLogin()) {
                    isLogin = false;
                } else {
                    if (!NetUtil.IsInNetwork(NewGoodsDetailActivity.this)) {
                        setOffLine();
                    }
                }
            }
        });

    }

    // 判断商品是否有更新，有更新的话 更新商品，没有的话
    int i = 0;
    private Handler mHandler = new Handler(){
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    upDateGoodsStatus();
                    break;
            }
        }
    };

    private void checkGoodsUpDate() {
        new RetrofitRequest<GoodsUpdateInfo>(ApiRequest.getApiShiji().checkGoodsUpDate(goodsId)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    GoodsUpdateInfo info = (GoodsUpdateInfo) msg.obj;
                    if (info.isNeed_update()) {
                        upDateGoodsStatus();
                    }else {
                        tvAddShoppingCart.setEnabled(true);
                        tvDrictBuy.setEnabled(true);
                    }
                }
            }
        });
    }

    // 更新商品
    private void upDateGoodsStatus() {
        new RetrofitRequest<GoodsUpdateStatusInfo>(ApiRequest.getApiShiji().goodsUpDateStatus(goodsId)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    GoodsUpdateStatusInfo info = (GoodsUpdateStatusInfo) msg.obj;
                    if (info.isIs_updated()) {
                        getGoodsSkus();
                    }else {
                        if(goodsDetailEntity.getGoods().getStatus() == 2) {
                            i++;
                            if (i > 4) {
                                return;
                            }
                            mHandler.sendEmptyMessageDelayed(1, 2000);
                        }else {
                            tvDrictBuy.setEnabled(true);
                            tvAddShoppingCart.setEnabled(true);
                        }
                    }
                }
            }
        });
    }

    // 更新商品的SKUS
    private void getGoodsSkus() {
        new RetrofitRequest<JsonObject>(ApiRequest.getApiShiji().getGoodsSkus(goodsId)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    JsonObject obj = (JsonObject) msg.obj;
                    skusJson = obj.get("skus");
                    tvDrictBuy.setVisibility(View.VISIBLE);
                    tvAddShoppingCart.setVisibility(View.VISIBLE);
                    tvSoldOut.setVisibility(View.GONE);
                    tvDrictBuy.setEnabled(true);
                    tvAddShoppingCart.setEnabled(true);
                }
            }
        });
    }

    // 获取商品详情信息
    private void getGoodsDetail() {
        showPreDialog("");
        new RetrofitRequest<JsonObject>(ApiRequest.getApiShiji().getGoodsDetailJson(MapRequest.setGoodsDetailMap(goodsId))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    goodsJson = (JsonObject) msg.obj;
                    goodsDetailEntity = new Gson().fromJson(goodsJson, GoodsDetailEntity.class);
                    if (goodsDetailEntity.getGoods().getStatus() == 2) {   //  1是有货2是售罄
                        tvDrictBuy.setVisibility(View.GONE);
                        tvAddShoppingCart.setVisibility(View.GONE);
                        tvSoldOut.setVisibility(View.VISIBLE);
                    }
                    checkGoodsUpDate();
                    initShareInfo(goodsDetailEntity);
                    if (goodsDetailEntity.getFollow_status() == 1) {    // 1是收藏2是未收藏
                        isGoodsCollected = true;
                        ivCollect.setSelected(true);
                    } else {
                        isGoodsCollected = false;
                        ivCollect.setSelected(false);
                    }
                    brandFollow = goodsDetailEntity.getBrand().getFollowed();

                    getBrandActList(String.valueOf(goodsDetailEntity.getBrand().getId()));
                    getMatchList();
                    getWorkList();
                    getVisitList();
                    getRecommendList();
                    setTotalAdapter();
                    translatDes();
                } else {
                    hidePreDialog();
                }
            }
        });
    }

    // 获取翻译过得商品描述
    private void translatDes() {
        new RetrofitRequest<GoodsTranlateDesc>(ApiRequest.getApiShiji().getGoodsTranslateDesc(goodsDetailEntity.getGoods().getId())).handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            GoodsTranlateDesc desc = (GoodsTranlateDesc) msg.obj;
                            translat = desc.getTranslated_desc();
                        }
                    }
                });
    }


    private void getBrandActList(String brandId) {
        beBrandAct = false;
        new RetrofitRequest<ActivityListObject>(ApiRequest.getApiShiji().getActivityList(brandId)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                beBrandAct = true;
                if (msg.isSuccess()) {
                    brandActObj = (ActivityListObject) msg.obj;
                } else {
                    hidePreDialog();
                }
                setTotalAdapter();
            }
        });
    }

    private void getMatchList() {
        beMatchList = false;
        new RetrofitRequest<TagProduceObject>(ApiRequest.getApiShiji().tagStar(MapRequest.setTagId(goodsDetailEntity.getBrand().getTag_id(), 0, 2))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                beMatchList = true;
                if (msg.isSuccess()) {
                    matchObj = (TagProduceObject) msg.obj;
                } else {
                    hidePreDialog();
                }
                setTotalAdapter();
            }
        });
    }

    private void getWorkList() {
        beWorkList = false;
        new RetrofitRequest<TagProduceObject>(ApiRequest.getApiShiji().tagStar(MapRequest.setTagIdTwenty(goodsDetailEntity.getBrand().getTag_id(), 0))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                beWorkList = true;
                if (msg.isSuccess()) {
                    workObj = (TagProduceObject) msg.obj;
                } else {
                    hidePreDialog();
                }
                setTotalAdapter();
            }
        });
    }

    private void getVisitList() {
        beVisitList = false;
        new RetrofitRequest<VisitGoodsEntity>(ApiRequest.getApiShiji().getVisitGoods()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                beVisitList = true;
                if (msg.isSuccess()) {
                    visitObj = (VisitGoodsEntity) msg.obj;
                } else {
                    hidePreDialog();
                }
                setTotalAdapter();
            }
        });
    }

    private void getRecommendList() {
        beRecommendList = false;
        final HashMap<String, String> maps = new HashMap<>();
        maps.put("goodsId", goodsDetailEntity.getGoods().getId());
        maps.put("offset", String.valueOf(0));
        maps.put("limit", String.valueOf(6));
        new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getNearGoodesList(maps)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        beRecommendList = true;
                        if (msg.isSuccess()) {
                            recommendObj = (MallGoodsDetailObject) msg.obj;
                        } else {
                            hidePreDialog();
                        }
                        setTotalAdapter();
                    }
                }
        );
    }

    private synchronized void setTotalAdapter() {
        if (beBrandAct && beMatchList && beWorkList && beVisitList && beRecommendList) {
            mAdapter = new NewGoodsDetailAdapter(NewGoodsDetailActivity.this, goodsDetailEntity, brandActObj, matchObj, workObj, visitObj, recommendObj, isGroupBuy);
            rycvGoodsDetail.setAdapter(mAdapter);
            mAdapter.setOnActionClickListener(new NewGoodsDetailAdapter.OnActionClickListener() {
                @Override
                public void OnBrandFollowClick(ImageView floatingActionButton) {
                    if (isLogin) {
                        followBrand(floatingActionButton);
                    } else {
                        gotoLogin();
                    }
                }

                @Override
                public void OnServiceClick() {
                    if (isLogin) {
                        gotoService();
                    } else {
                        gotoLogin();
                    }

                }

                @Override
                public void OnForwardClick() {
                    gotoForward();
                }

                @Override
                public void OnRecommendClick(TextView tvRecommend) {
                    new RetrofitRequest<>(ApiRequest.getApiShiji().getRecommendGoodsEdit(MapRequest.setRecommendGoodsAddMap(goodsDetailEntity.getGoods().getId(), 1))).handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {

                        }
                    });
                    tvRecommend.setBackgroundColor(Color.parseColor("#ffffff"));
                    tvRecommend.setText("[ 已推荐 ]");
                    tvRecommend.setTextColor(Color.parseColor("#9b9b9b"));
                    tvRecommend.setEnabled(false);
                    showRecommendGoods();
                }
            });
            setSuccess();
            hidePreDialog();
        }
    }

    private void initShareInfo(GoodsDetailEntity goodsDetailEntity) {
        shareTitle = "刚在柿集发现了件特实惠的正品大牌，来帮我参谋参谋";
        shareUrl = goodsDetailEntity.getShare_item_url() + goodsId;
        shareImageUrl = goodsDetailEntity.getGoods_colors().get(0).getImages().get(0).getImage() + "?imageView2/2/w/300";
        shareContent = "在【柿集】海淘的正品好货：" + goodsDetailEntity.getGoods().getTitle();
    }

    // 获取购物车数量
    private void initCartCount() {
        tvCartCount.setVisibility(View.GONE);
        new RetrofitRequest<CarCountInfo>(ApiRequest.getApiShiji().getCarCount()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    CarCountInfo info = (CarCountInfo) msg.obj;
                    if (info.getCount() > 0) {
                        tvCartCount.setVisibility(View.VISIBLE);
                        tvCartCount.setText("" + info.getCount());
                    }
                }
            }
        });
    }

    // 成功加载数据
    private void setSuccess() {
        setSuccessView(rycvGoodsDetail);
        llytBottom.setVisibility(View.VISIBLE);
    }

    // 无网络
    private void setOffLine() {
        setOffNetView(rycvGoodsDetail);
        llytBottom.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_LOGIN) {
                isLogin = true;
                initCartCount();
                Util.getNewUserPullLayer(this, data);
            }
        } else {

        }
    }

    private void showCollectGoods(boolean isCollected) {
        View layoutTips = LayoutInflater.from(this).inflate(R.layout.collect_goods_toast, (ViewGroup) findViewById(R.id.toast_layout_root));
        TextView tvContent = (TextView) layoutTips.findViewById(R.id.toast_context);
        if (isCollected) {
            tvContent.setText("收藏成功");
        } else {
            tvContent.setText("取消收藏");
        }
        final Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, SimpleUtils.getScreenHeight(this) * 7 / 24);
        toast.setView(layoutTips);
        toast.show();
    }

    private void showRecommendGoods() {
        View layoutTips = LayoutInflater.from(this).inflate(R.layout.collect_goods_toast, (ViewGroup) findViewById(R.id.toast_layout_root));
        TextView tvContent = (TextView) layoutTips.findViewById(R.id.toast_context);
        tvContent.setText("已添加至个人中心－店主推荐");
        final Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, SimpleUtils.getScreenHeight(this) * 7 / 24);
        toast.setView(layoutTips);
        toast.show();
    }

    /**
     * 贝塞尔曲线实现商品添加到购物车动画
     *
     * @param startLoc 动画开始坐标
     */
    public void addToCartAnimator(int[] startLoc, final int num, String imagUrl) {
        // 一、创造出执行动画的主题---imageview
        final SimpleDraweeView goods = new SimpleDraweeView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(SimpleUtils.dp2px(this, 80), SimpleUtils.dp2px(this, 80));
        goods.setLayoutParams(params);
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE)
                .build();
        goods.setHierarchy(hierarchy);
        goods.setImageURI(imagUrl);
        rlRoot.addView(goods);


        //二、计算动画开始/结束点的坐标的准备工作
        //得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLocation = new int[2];
        rlRoot.getLocationInWindow(parentLocation);

        //得到购物车图片的坐标(用于计算动画结束后的坐标)
        int endLoc[] = new int[2];
        rlytCart.getLocationInWindow(endLoc);

        //正式开始计算动画开始/结束的坐标
        //商品的起始点
        float startX = startLoc[0] - parentLocation[0] - startLoc[0] / 2;
        float startY = startLoc[1] - parentLocation[1] + startLoc[1] / 2;

        //商品的终点坐标
        float toX = endLoc[0] - parentLocation[0];
        float toY = endLoc[1] - parentLocation[1];

        //三、计算中间动画的插值坐标（贝塞尔曲线）（其实就是用贝塞尔曲线来完成起终点的过程）
        //开始绘制贝塞尔曲线
        final float[] mCurrentPosition = new float[2];//贝塞尔曲线中间过程的点的坐标
        Path path = new Path();
        //移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startX, startY);
        //使用二次贝塞尔曲线 P1控制点可按照自己需求取
        path.quadTo((startX + toX) / 2, toY + ((startY - toY) / 2), toX, toY);
        //mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，
        // 如果是true，path会形成一个闭环
        final PathMeasure mPathMeasure = new PathMeasure(path, false);

        //四、★★★属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(500);
        // 匀速线性插值器
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                // ★★★★★获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距
                // 离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
                mPathMeasure.getPosTan(value, mCurrentPosition, null);//mCurrentPosition此时就是中间距离点的坐标值
                // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
                goods.setTranslationX(mCurrentPosition[0]);
                goods.setTranslationY(mCurrentPosition[1]);
            }
        });

        if (goodsBuyDialog != null && goodsBuyDialog.isShowing()) {
            goodsBuyDialog.dismiss();
            Util.toast(this, "加入成功", true);
        }
        //五、开始执行动画
        valueAnimator.start();

        //六、动画结束后的处理
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (tvCartCount.getVisibility() == View.GONE) {
                    tvCartCount.setText(String.valueOf(num));
                    tvCartCount.setVisibility(View.VISIBLE);
                } else {
                    // 购物车的数量加1
                    tvCartCount.setText(String.valueOf(Integer.valueOf(tvCartCount.getText().toString()) + num));
                }
                // 把移动的图片imageview从父布局里移除
                rlRoot.removeView(goods);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

}
