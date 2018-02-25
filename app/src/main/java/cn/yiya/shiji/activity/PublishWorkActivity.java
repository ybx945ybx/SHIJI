package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.seles.tusdk.FilterManager;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ShareTools;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.entity.PicDiaryUploadItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.tusdk.CutPicTuSdk;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.utils.DateTimeFormat;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.ProgressDialog;
import cn.yiya.shiji.views.TagAbleImageView;
import cn.yiya.shiji.views.TagView;

/**
 * Created by dell on 2015-11-03.
 */
public class PublishWorkActivity extends BaseAppCompatActivity implements TagAbleImageView.CustomEventListener, View.OnClickListener {

    private TagAbleImageView tagView;
    private String mSelectPath;
    private ArrayList<TagAbleImageView.TagInfo> mInfos;
    private boolean bAuto;
    public static final int INPUT_ADD_TAG = 301;
    public boolean bAdd = false;
    private RelativeLayout rlytGoods;
    private ImageView ivGoods;
    private TextView tvGoodsType;
    private TextView tvGoodsSite;
    private TextView tvGoodsMoney;
    private TextView tvTipsOne;
    private TextView tvTipsTwo;
    private ImageView ivDelete;
    private LinearLayout llytTagArrow;
    private TextView tvArrowBrands;
    private TextView tvActTag;
    // 顶部操作栏
    private TextView tvTitle;

    // 底部操作栏
    private RelativeLayout rlytNext;
    private TextView tvCancel;
    private TextView tvOK;
    private String strCover;
    private String strGoodId;
    private String strRecommend;
    private ImageView ivAdd;
    private boolean bSkip;
    CutPicTuSdk tuSdk;
    private String strActivityTag;
    public static final int PublishCode = 200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_work_preview);

        ShareTools.getInstance().initShare(this);
        TuSdk.messageHub().setStatus(this, R.string.lsq_initing);
        TuSdk.checkFilterManager(mFilterManagerDelegate);
        strActivityTag = getIntent().getStringExtra("tagname");
        bSkip = getIntent().getBooleanExtra("skip", false);

        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        tvTitle = (TextView) findViewById(R.id.work_preview_title);
        tvTitle.setVisibility(View.GONE);

        rlytNext = (RelativeLayout) findViewById(R.id.work_next_layout);
        tvCancel = (TextView) findViewById(R.id.work_cancel);
        tvOK = (TextView) findViewById(R.id.work_query);
        rlytGoods = (RelativeLayout) findViewById(R.id.work_link_layout);
        ivGoods = (ImageView) findViewById(R.id.work_goods_image);
        tvGoodsType = (TextView) findViewById(R.id.work_goods_type);
        tvGoodsSite = (TextView) findViewById(R.id.work_goods_site);
        tvGoodsMoney = (TextView) findViewById(R.id.work_link_money);
        ivDelete = (ImageView) findViewById(R.id.work_delete);
        tvTipsOne = (TextView) findViewById(R.id.work_tips_one);
        tvTipsTwo = (TextView) findViewById(R.id.work_tips_two);
        llytTagArrow = (LinearLayout) findViewById(R.id.work_tag_arrow);
        tvArrowBrands = (TextView) findViewById(R.id.preview_tv_brands);
        tvTipsOne.setText("点击照片");
        tvTipsTwo.setText("选择添加商品相关信息");
        ivAdd = (ImageView) findViewById(R.id.image_add);

        tagView = (TagAbleImageView) findViewById(R.id.tagAbleView);
        tagView.getLayoutParams().height = SimpleUtils.getScreenWidth(this);
        tagView.setEditable(true);
        tagView.setCustomEventListener(this);

        tvActTag = (TextView) findViewById(R.id.work_act_tag_txt);
        if (!TextUtils.isEmpty(strActivityTag)) {
            tvActTag.setText("#" + strActivityTag + "#");
        } else {
            tvActTag.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initEvents() {
        tvCancel.setOnClickListener(this);
        tvOK.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        llytTagArrow.setOnClickListener(this);
        rlytGoods.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
    }

    @Override
    protected void init() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.work_cancel:
                onBackPressed();
                break;
            case R.id.work_query:
                Intent intent = new Intent(PublishWorkActivity.this, ShowOrderActivity.class);
                intent.putExtra("path", mSelectPath);
                intent.putExtra("text", getIntent().getStringExtra("text"));
                String tagInfo = doNext();
                if (TextUtils.isEmpty(tagInfo)) {
                    return;
                } else {
                    intent.putExtra("tag", tagInfo);
                }
                startActivityForResult(intent, PublishCode);
                break;
            case R.id.work_delete:
                showDeleteDialog("删除商品信息同时标签也会删除？");
                break;
            case R.id.work_tag_arrow:
            case R.id.work_link_layout:
                goToGoodsDetail();
                break;
            case R.id.image_add:
                selectPics();
                break;
        }
    }

    private String doNext() {

        if (TextUtils.isEmpty(mSelectPath)) {
            showTips("请添加图片");
            return "";
        }
        PicDiaryUploadItem item = new PicDiaryUploadItem();
        item.images = new ArrayList<>();
        String key = BaseApplication.getInstance().readUserId() + "_work/diary_" + System.currentTimeMillis();
        PicDiaryUploadItem.ImageInfo image = new PicDiaryUploadItem.ImageInfo();
        image.url = "http://diary.cdnqiniu02.qnmami.com/" + key;
        image.tags = new ArrayList<>();

        // 如果添加了标签
        if (bAdd) {
            // 如果已经添加过标签，清空
            if (image.tags != null) {
                image.tags.clear();
            }
            image.tags = tagView.getTagsInfo();

            // 删除添加过的活动
            if (image.tags.size() > 3) {
                for (int i = 0; i < image.tags.size(); i++) {
                    if (image.tags.get(i).getGroup_id() == 2) {
                        image.tags.remove(i);
                    }
                }
            }

            image.goods_id = strGoodId;
        } else {
            image.goods_id = "";
        }
        // 添加活动标签
        if (!TextUtils.isEmpty(strActivityTag)) {
            TagAbleImageView.TagInfo actInfo = new TagAbleImageView.TagInfo();
            actInfo.setContent(strActivityTag);
            actInfo.setGroup_id(2);
            actInfo.setType(4);
            image.tags.add(actInfo);
        }
        item.images.add(image);
        item.key = key;
        item.date = DateTimeFormat.getCurrentDate();
        return new Gson().toJson(item);
    }

    private void selectPics() {
        tuSdk = new CutPicTuSdk();
        tuSdk.openTuSdkCutPic(this, true, new CutPicTuSdk.OnImageHandleListerner() {
            @Override
            public void onFinished(File imgeFile) {
                mSelectPath = imgeFile.getAbsolutePath();
                BitmapTool.disaplayImage("file://" + mSelectPath, tagView.getImageView(), null);
                if (bSkip) {
                    getTagInfo(getIntent());
                }
            }
        });
    }

    private FilterManager.FilterManagerDelegate mFilterManagerDelegate = new FilterManager.FilterManagerDelegate() {
        @Override
        public void onFilterManagerInited(FilterManager manager) {
            TuSdk.messageHub().showSuccess(PublishWorkActivity.this, R.string.lsq_inited);
            selectPics();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case INPUT_ADD_TAG:
                    bSkip = true;
                    tvTitle.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(strActivityTag)) {
                        tvTitle.setText("#" + strActivityTag + "#");
                    } else {
                        tvTitle.setText("预览");
                    }
                    getTagInfo(data);
                    break;
                case PublishCode:
                    if (data != null) {
                        String path = data.getStringExtra("path");
                        if (!TextUtils.isEmpty(path)) {
                            selectPics();
                            return;
                        }
                    }
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
//            onBackPressed();
        }
    }

    private void getTagInfo(Intent data) {
        String tag = data.getStringExtra("TAG");
        Type type = new TypeToken<ArrayList<TagAbleImageView.TagInfo>>() {
        }.getType();
        ArrayList<TagAbleImageView.TagInfo> infos = new Gson().fromJson(tag, type);
        if (infos == null) {
            ivAdd.setVisibility(View.GONE);
            if (bAdd) {
                tvTipsOne.setText("按住标签可以调整位置");
                tvTipsTwo.setText("长按可以删除标签");
            } else {
                tvTipsOne.setText("点击照片");
                tvTipsTwo.setText("选择添加商品相关信息");
            }
            return;
        }

        bAuto = data.getBooleanExtra("AUTO", false);
        strCover = data.getStringExtra("cover");
        strGoodId = data.getStringExtra("goodsId");
        strRecommend = data.getStringExtra("recommend");
        float x;
        float y;
        if (bAuto) {
            x = 0.1f;
            y = 0.1f;
        } else {
            x = ((float) tagView.getCycleX() / (float) tagView.getWidth());
            y = ((float) tagView.getCycleY() / (float) tagView.getHeight());
        }


        for (int i = 0; i < infos.size(); i++) {
            TagAbleImageView.TagInfo info = infos.get(i);
            if (mInfos == null) {
                info.setX(x);
                info.setY(y);
                info.setGroup_id(1);
            }
        }
        tagView.initTags(null, 0);
        tagView.addTag(infos, -1);
        mInfos = infos;
        bAdd = true;
        tvTipsOne.setText("按住标签可以调整位置");
        tvTipsTwo.setText("长按可以删除标签");
        if (bAuto) {
            showGoodsInfo();
        }
    }

    private void showGoodsInfo() {
        ivAdd.setVisibility(View.GONE);
        rlytGoods.setVisibility(View.VISIBLE);
        ivDelete.setVisibility(View.VISIBLE);

        tvGoodsType.setText(mInfos.get(0).getContent() + " " + mInfos.get(0).getDetails());
        tvGoodsSite.setText("由" + mInfos.get(1).getDetails());
        tvGoodsMoney.setText(mInfos.get(2).getDetails());
        Netroid.displayImage(Util.transfer2(strCover), ivGoods);

        llytTagArrow.setVisibility(View.VISIBLE);
        tvArrowBrands.setText(mInfos.get(0).getContent() + " " + mInfos.get(0).getDetails());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tuSdk != null && tuSdk.getCancel()) {
            ivAdd.setVisibility(View.VISIBLE);
            tvTipsOne.setText("请添加图片");
            tvTipsTwo.setText("");
        } else {
            if (!bSkip) {
                tvTipsOne.setText("点击照片");
                tvTipsTwo.setText("选择添加商品相关信息");
                ivAdd.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onAddTag() {
        if (!bAdd) {
            Intent intent = new Intent(PublishWorkActivity.this, SearchTagActivity.class);
            startActivityForResult(intent, INPUT_ADD_TAG);
        }
    }

    @Override
    public void onTagTextClick(TagView view) {
        onTagClick(view);
    }

    @Override
    public void onTagLinearClick(TagView tagView) {
        onTagClick(tagView);
    }

    private void onTagClick(TagView view) {
        if (bAuto) {
            showTips("自动添加标签不支持编辑");
            return;
        }
        Intent intent = new Intent(this, SearchTagActivity.class);
        Type type = new TypeToken<ArrayList<TagAbleImageView.TagInfo>>() {
        }.getType();
        String tag;
        if (tagView != null) {
            tag = new Gson().toJson(tagView.getTagsInfo(), type);
        } else {
            tag = new Gson().toJson(mInfos, type);
        }
        intent.putExtra("TAG", tag);
        startActivityForResult(intent, INPUT_ADD_TAG);
    }

    @Override
    public void onTagLongClick(TagView View) {
        if (bAuto) {
            showDeleteDialog("删除该标签会导致商品信息同时删除？");
        } else {
            showDeleteDialog("确定要删除该标签吗？");
        }
    }

    @Override
    public void onBackPressed() {
        showExitDialog("确定要退出晒单吗？");
    }

    private void showDeleteDialog(String title) {
        showCustomMutiDialog(title, new ProgressDialog.DialogClick() {
            @Override
            public void OkClick() {
                tagView.initTags(null, 0);
                mInfos = null;
                bAdd = false;
                rlytGoods.setVisibility(View.GONE);
                llytTagArrow.setVisibility(View.GONE);
                ivDelete.setVisibility(View.GONE);
                tvTipsOne.setText("点击照片");
                tvTipsTwo.setText("选择添加商品相关信息");
                tvTitle.setVisibility(View.GONE);
            }

            @Override
            public void CancelClick() {

            }
        });
    }

    private void goToGoodsDetail() {
        if (TextUtils.isEmpty(strGoodId)) {
            return;
        }
        Intent intent = new Intent(this, NewGoodsDetailActivity.class);
        intent.putExtra("goodsId", strGoodId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void showExitDialog(String title) {
        showCustomMutiDialog(title, new ProgressDialog.DialogClick() {
            @Override
            public void OkClick() {
                SimpleUtils.deleteCropImg(PublishWorkActivity.this, mSelectPath);
                setResult(RESULT_CANCELED);
                finish();
            }

            @Override
            public void CancelClick() {

            }
        });
    }
}
