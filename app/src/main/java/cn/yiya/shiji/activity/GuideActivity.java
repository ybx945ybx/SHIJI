package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;

import cn.yiya.shiji.R;
import cn.yiya.shiji.views.ThemeManager;

/**
 * Created by chenjian on 2015/9/30.
 */
public class GuideActivity extends FragmentActivity {

    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;

    private String adContent;
    private int adType;
    private String adShare;
    private String bottomName;
    private String bottomUrl;
    private String bottomTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIntent();
        ThemeManager.init(this, 2, 0, null);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);
        ViewPager viewPager = (ViewPager) findViewById(R.id.guide_viewPager);
        Indicator indicator = (Indicator) findViewById(R.id.guide_indicator);
        indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        inflate = LayoutInflater.from(getApplicationContext());
        indicatorViewPager.setAdapter(adapter);

    }

    private void initIntent() {
        Intent intent = getIntent();
        if(intent != null) {
            adContent = intent.getStringExtra("adContent");
            adType = intent.getIntExtra("adType", -1);
            adShare = intent.getStringExtra("adShare");
            bottomName = intent.getStringExtra("bottomName");
            bottomUrl = intent.getStringExtra("bottomUrl");
            bottomTitle = intent.getStringExtra("bottomTitle");
        }
    }

    private IndicatorViewPager.IndicatorPagerAdapter adapter = new IndicatorViewPager.IndicatorViewPagerAdapter() {
        private int[] images = { R.mipmap.guide_one, R.mipmap.guide_two, R.mipmap.guide_three, R.mipmap.guide_four};

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.tab_guide, container, false);
            }
            return convertView;
        }


        @Override
        public View getViewForPage(int position, View convertView, ViewGroup container) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.guide_image_layout, container, false);
                viewHolder = new ViewHolder();
                viewHolder.tvGoApp = (TextView)convertView.findViewById(R.id.go_to_app);
                viewHolder.ivBg = (ImageView)convertView.findViewById(R.id.guide_image);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder)convertView.getTag();
            if (position == images.length - 1) {
                viewHolder.tvGoApp.setVisibility(View.VISIBLE);
                viewHolder.tvGoApp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GuideActivity.this, ADActivity.class);
                        intent.putExtra("adType", adType);
                        intent.putExtra("adContent", adContent);
                        intent.putExtra("adShare", adShare);
                        intent.putExtra("bottomName", bottomName);
                        intent.putExtra("bottomUrl", bottomUrl);
                        intent.putExtra("bottomTitle", bottomTitle);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                viewHolder.tvGoApp.setVisibility(View.GONE);
            }

            viewHolder.ivBg.setBackgroundResource(images[position]);

            return convertView;
        }

        @Override
        public int getCount() {
            return images.length;
        }
    };

    public class ViewHolder{
        TextView tvGoApp;
        ImageView ivBg;
    }
}
