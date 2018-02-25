package cn.yiya.shiji.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.yiya.shiji.R;

/**
 * Created by Tom on 2016/4/20.
 */
public class AskWayActivity extends Activity {
    private String name;
    private String country;
    private String address;
    private TextView tvName;
    private TextView tvAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_way);
        name = getIntent().getStringExtra("name");
        country = getIntent().getStringExtra("country");
        address = getIntent().getStringExtra("address");

        tvName = (TextView)findViewById(R.id.tv_ask);
        tvName.setText(name);
        tvAddress = (TextView)findViewById(R.id.tv_address);
        tvAddress.setText(address + "," + country);
        findViewById(R.id.iv_ask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        initViews();
//        initEvents();
//        init();
    }

//    @Override
//    protected void initViews() {
//        tvName = (TextView)findViewById(R.id.tv_ask);
//        tvName.setText(name);
//        tvAddress = (TextView)findViewById(R.id.tv_address);
//        tvAddress.setText(address + "," + country);
//    }
//
//    @Override
//    protected void initEvents() {
//        findViewById(R.id.iv_ask).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }
//
//    @Override
//    protected void init() {
//
//    }

}
