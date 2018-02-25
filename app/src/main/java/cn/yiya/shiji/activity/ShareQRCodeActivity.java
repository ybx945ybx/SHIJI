package cn.yiya.shiji.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.User;
import cn.yiya.shiji.utils.DebugUtil;
import cn.yiya.shiji.utils.Util;

/**
 * 开店二维码
 * Created by Amy on 2016/10/20.
 */

public class ShareQRCodeActivity extends BaseAppCompatActivity {
    private int shopId;

    private ImageView ivBack;
    private TextView tvTitle;

    private CardView cardView;
    private SimpleDraweeView ivAvatar;
    private TextView tvName;
    private SimpleDraweeView ivQRCode;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_share);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            shopId = intent.getIntExtra("shop_id", 0);
        }
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("开店二维码");
        findViewById(R.id.title_right).setVisibility(View.GONE);

        cardView = (CardView) findViewById(R.id.cardview);
        ivAvatar = (SimpleDraweeView) findViewById(R.id.iv_avatar);
        tvName = (TextView) findViewById(R.id.tv_name);
        ivQRCode = (SimpleDraweeView) findViewById(R.id.iv_qrcode);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialog();
                return false;
            }
        });

    }

    @Override
    protected void init() {
        getUserInfo();
    }

    private void getUserInfo() {
        new RetrofitRequest<User>(ApiRequest.getApiShiji().getUserDetail()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    User user = (User) msg.obj;
                    ivAvatar.setImageURI(Util.transfer(user.getHead()));
                    tvName.setText(user.getName());
                    String imageUrl = Configration.SERVER + "/shop/personal-shop/qr-code?user_id="+ user.getUser_id();
                    DebugUtil.e("qrcode:"+imageUrl);
                    ivQRCode.setImageURI(imageUrl);
                }
            }
        });
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this, R.style.ActionSheetDialogStyle);

        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_save_qrcode, null);

        TextView tvSave = (TextView) view.findViewById(R.id.tv_save);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                saveViewToGallery(cardView);
                showTips("图片保存成功！");
            }
        });
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Window dialogWindow = dialog.getWindow();

        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);

        dialog.show();
    }

    /**
     * base64转Bitmap
     *
     * @param strBase64
     * @return
     */
    public static Bitmap base64ToBitmap(String strBase64) {
        byte[] decode = Base64.decode(strBase64, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        return bitmap;
    }

    /**
     * 保存到系统相册并更新相册
     *
     * @param view
     */
    private void saveViewToGallery(View view) {
        // 创建对应大小的bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "", "");
        MediaScannerConnection msc = null;
        final MediaScannerConnection finalMsc = msc;
        msc = new MediaScannerConnection(this, new MediaScannerConnection.MediaScannerConnectionClient() {
            @Override
            public void onMediaScannerConnected() {
                finalMsc.scanFile("/sdcard/image.jpg", "image/jpeg");
            }

            @Override
            public void onScanCompleted(String path, Uri uri) {
                finalMsc.disconnect();
            }
        });
    }

}
