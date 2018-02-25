package cn.yiya.shiji.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.UserGrowFinishTaskEntity;
import cn.yiya.shiji.entity.UserGrowRuleEntity;

/**
 * Created by Tom on 2017/1/6.
 */

public class RedTaskFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private TextView tvDapeiNum,tvDapeiGrow,tvDapeiPro;
    private TextView tvPengyouquanNum,tvPengyouquanGrow,tvPengyouquanPro;
    private TextView tvWeiboNum,tvWeiboGrow,tvWeiboPro;
    private TextView tvZanGrow,tvCommitGrow,tvCommitedGrow,tvActivityGrow,tvRecommendedGrow;
    private int dapeiNum,pengyouquanNum,weiboNum;
//    private TextView tvDapeiDesc,tvDapeiPro;
//    private TextView tvPengyouquanDesc,tvPengyouquanPro;
//    private TextView tvWeiboDesc,tvWeiboPro;
//    private TextView tvZanDesc;
//    private TextView tvCommentDesc;
//    private TextView tvReplyDesc;
//    private TextView tvActivityDesc;
//    private TextView tvRecommendDesc;
    private UserGrowRuleEntity entity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIntent();
    }

    private void initIntent() {
        entity = new Gson().fromJson(getArguments().getString("entity"), UserGrowRuleEntity.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.red_task_fragment, null);
        initViews();
        initEvents();
        init();
        return view;

    }

    public RedTaskFragment getInstance(UserGrowRuleEntity entity){
        RedTaskFragment redTaskFragment = new RedTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putString("entity", new Gson().toJson(entity));
        redTaskFragment.setArguments(bundle);
        return redTaskFragment;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initViews() {
        tvDapeiNum = (TextView) view.findViewById(R.id.tv_dapei_num);
        tvDapeiGrow = (TextView) view.findViewById(R.id.tv_dapei_grow);
        tvDapeiPro = (TextView) view.findViewById(R.id.tv_progress);
        tvPengyouquanNum = (TextView) view.findViewById(R.id.tv_pengyouquan_num);
        tvPengyouquanGrow = (TextView) view.findViewById(R.id.tv_pengyouquan_grow);
        tvPengyouquanPro = (TextView) view.findViewById(R.id.tv_pyq_progress);
        tvWeiboNum = (TextView) view.findViewById(R.id.tv_weibo_num);
        tvWeiboGrow = (TextView) view.findViewById(R.id.tv_weibo_grow);
        tvWeiboPro = (TextView) view.findViewById(R.id.tv_wb_progress);
        tvZanGrow = (TextView) view.findViewById(R.id.tv_beidianzan_desc);
        tvCommitGrow = (TextView) view.findViewById(R.id.tv_beipinglun_desc);
        tvCommitedGrow = (TextView) view.findViewById(R.id.tv_huifupinglun_desc);
        tvActivityGrow = (TextView) view.findViewById(R.id.tv_activity_grow);
        tvRecommendedGrow = (TextView) view.findViewById(R.id.tv_recommend_grow);

    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void init() {
        getGrowRule();
//        getGrowfinish();
    }

    private void getGrowfinish() {
        new RetrofitRequest<UserGrowFinishTaskEntity>(ApiRequest.getApiShiji().getUserGrowFinishTask()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if(msg.isSuccess()){//  1,4,5
                    UserGrowFinishTaskEntity entity = (UserGrowFinishTaskEntity) msg.obj;
                    if(entity.getTask() != null && entity.getTask().size() > 0){
                        for (int i = 0; i < entity.getTask().size(); i++){
                            if(entity.getTask().get(i).getAction() == 1){
                                if(entity.getTask().get(i).getFinish_times() >= dapeiNum){
                                    setComplete(tvDapeiPro);
                                }else {
                                    setProgress(tvDapeiPro, dapeiNum - entity.getTask().get(i).getFinish_times() + "");
                                }
                            }else if (entity.getTask().get(i).getAction() == 4){
                                if(entity.getTask().get(i).getFinish_times() >= pengyouquanNum){
                                    setComplete(tvPengyouquanPro);
                                }else {
                                    setProgress(tvPengyouquanPro, pengyouquanNum - entity.getTask().get(i).getFinish_times() + "");
                                }
                            }else if(entity.getTask().get(i).getAction() == 5){
                                if(entity.getTask().get(i).getFinish_times() >= weiboNum){
                                    setComplete(tvWeiboPro);
                                }else {
                                    setProgress(tvWeiboPro, weiboNum - entity.getTask().get(i).getFinish_times() + "");
                                }
                            }
                        }
                    }
                }
            }
        });

    }

    private void getGrowRule() {
        if(entity.getTask() != null && entity.getTask().size() > 0){
            for (int i = 0; i < entity.getTask().size(); i++){
                switch (entity.getTask().get(i).getAction()){  //1 '发布并被标星', 2 '作品被点赞', 3 '作品被评论', 4 '作品被分享到微信', 5 '作品被分享到微博', 6 '参加活动', 7 '被选为封面', 8 '作品被回复评论',
                    case 1:
                        dapeiNum = entity.getTask().get(i).getNum();
                        tvDapeiNum.setText(entity.getTask().get(i).getNum() + "");
                        tvDapeiGrow.setText(entity.getTask().get(i).getGrow() + "");
                        break;
                    case 4:
                        pengyouquanNum = entity.getTask().get(i).getNum();
                        tvPengyouquanNum.setText(entity.getTask().get(i).getNum() + "");
                        tvPengyouquanGrow.setText(entity.getTask().get(i).getGrow() + "");
                        break;
                    case 5:
                        weiboNum = entity.getTask().get(i).getNum();
                        tvWeiboNum.setText(entity.getTask().get(i).getNum() + "");
                        tvWeiboGrow.setText(entity.getTask().get(i).getGrow() + "");
                        break;
                    case 2:
                        tvZanGrow.setText(entity.getTask().get(i).getGrow() + "");
                        break;
                    case 3:
                        tvCommitGrow.setText(entity.getTask().get(i).getGrow() + "");
                        break;
                    case 8:
                        tvCommitedGrow.setText(entity.getTask().get(i).getGrow() + "");
                        break;
                    case 6:
                        tvActivityGrow.setText(entity.getTask().get(i).getGrow() + "");
                        break;
                    case 7:
                        tvRecommendedGrow.setText(entity.getTask().get(i).getGrow() + "");
                        break;
                }
            }
            getGrowfinish();
        }
//        new RetrofitRequest<UserGrowRuleEntity>(ApiRequest.getApiShiji().getUserGrowRule()).handRequest(new MsgCallBack() {
//            @Override
//            public void onResult(HttpMessage msg) {
//                if(msg.isSuccess()){
//                    UserGrowRuleEntity entity = (UserGrowRuleEntity) msg.obj;
//                    if(entity.getTask() != null && entity.getTask().size() > 0){
//                        for (int i = 0; i < entity.getTask().size(); i++){
//                            switch (entity.getTask().get(i).getAction()){  //1 '发布并被标星', 2 '作品被点赞', 3 '作品被评论', 4 '作品被分享到微信', 5 '作品被分享到微博', 6 '参加活动', 7 '被选为封面', 8 '作品被回复评论',
//                                case 1:
//                                    dapeiNum = entity.getTask().get(i).getNum();
//                                    tvDapeiNum.setText(entity.getTask().get(i).getNum() + "");
//                                    tvDapeiGrow.setText(entity.getTask().get(i).getGrow() + "");
//                                    break;
//                                case 4:
//                                    pengyouquanNum = entity.getTask().get(i).getNum();
//                                    tvPengyouquanNum.setText(entity.getTask().get(i).getNum() + "");
//                                    tvPengyouquanGrow.setText(entity.getTask().get(i).getGrow() + "");
//                                    break;
//                                case 5:
//                                    weiboNum = entity.getTask().get(i).getNum();
//                                    tvWeiboNum.setText(entity.getTask().get(i).getNum() + "");
//                                    tvWeiboGrow.setText(entity.getTask().get(i).getGrow() + "");
//                                    break;
//                                case 2:
//                                    tvZanGrow.setText(entity.getTask().get(i).getGrow() + "");
//                                    break;
//                                case 3:
//                                    tvCommitGrow.setText(entity.getTask().get(i).getGrow() + "");
//                                    break;
//                                case 8:
//                                    tvCommitedGrow.setText(entity.getTask().get(i).getGrow() + "");
//                                    break;
//                                case 6:
//                                    tvActivityGrow.setText(entity.getTask().get(i).getGrow() + "");
//                                    break;
//                                case 7:
//                                    tvRecommendedGrow.setText(entity.getTask().get(i).getGrow() + "");
//                                    break;
//                            }
//                        }
//                        getGrowfinish();
//                    }
//                }
//            }
//        });
    }

    private void setProgress(TextView textView, String progress){
        textView.setText("还差" + progress + "次");
        textView.setTextColor(Color.parseColor("#ed5137"));
        textView.setBackgroundResource(R.drawable.red_progress_bg);
    }

    private void setComplete(TextView textView){
        textView.setText("已完成");
        textView.setTextColor(Color.parseColor("#b5b5b5"));
        textView.setBackgroundColor(Color.parseColor("#f0f0f0"));
    }
}
