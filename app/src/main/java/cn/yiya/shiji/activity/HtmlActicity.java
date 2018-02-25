package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.HtmlZoomListAdapter;
import cn.yiya.shiji.entity.HtmlImageListItem;
import cn.yiya.shiji.entity.HtmlImageListObject;
import cn.yiya.shiji.views.ZoomListView;

/**
 * Created by jerry on 2016/2/1.
 */
public class HtmlActicity extends BaseAppCompatActivity {
    private ZoomListView mZoomListView;
    private HtmlZoomListAdapter mHtmlZoomListAdapter;
    private ArrayList<HtmlImageListItem> mlist;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.html_image_layout);

        url = getIntent().getStringExtra("imageurl");
        HtmlImageListObject object = new HtmlImageListObject();
        object = new Gson().fromJson(url, HtmlImageListObject.class);
        mlist = object.list;

        initViews();
        initEvents();
        init();

    }

    @Override
    protected void initViews() {
        mHtmlZoomListAdapter = new HtmlZoomListAdapter(mlist, HtmlActicity.this);
        mZoomListView = (ZoomListView) findViewById(R.id.list_view);
        mZoomListView.setDividerHeight(0);
        mZoomListView.setDivider(null);
        mZoomListView.setAdapter(mHtmlZoomListAdapter);
    }

    @Override
    protected void initEvents() {
        mZoomListView.setCustomEventListener(new ZoomListView.CustomEventListener() {
            @Override
            public void onCustomClick() {
                finish();

            }
        });
    }

    @Override
    protected void init() {

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.silde_in_center, R.anim.silde_out_border);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.silde_in_center, R.anim.silde_out_border);
    }
}
