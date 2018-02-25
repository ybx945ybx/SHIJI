package cn.yiya.shiji.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zhy.view.flowlayout.TagView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.GoodsListFilterActivity;
import cn.yiya.shiji.entity.MallLimitOptionInfo;
import cn.yiya.shiji.entity.SortInfo;


/**
 * 商品列表筛选过滤界面
 * Created by jerry on 2016/5/13.
 */
public class GoodsListFilterFragment extends BaseFragment {

    private LinearLayout llytGenders;
    private TagFlowLayout tflytGendrs;
    private TagAdapter genderAdapter;

    private LinearLayout llytCategorys;
    private TagFlowLayout tflytCategorys;
    private TagAdapter categoryAdapter;
    private ImageView ivMoreCatigorys;

    private LinearLayout llytSize;
    private TagFlowLayout tflytSize;
    private TagAdapter sizeAdapter;

    private LinearLayout llytPrice;
    private TagFlowLayout tflytPrice;
    private TagAdapter priceAdapter;

    private LinearLayout llytColor;
    private TagFlowLayout tflytColor;
    private TagAdapter colorAdapter;

    private MallLimitOptionInfo mInfos;
    private ArrayList<SortInfo> mCategoryLists = new ArrayList<>();
    private ArrayList<SortInfo> mSizeLists = new ArrayList<>();
    private String data;
    private String categoryId="";
    LayoutInflater mLayoutInflater;
    int pro_type_id;
    boolean isSame = true;
    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = this.getArguments().getString("data");
        categoryId=this.getArguments().getString("categoryid");
        mInfos = new Gson().fromJson(data, MallLimitOptionInfo.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_goods_list_filter, container, false);
        initViews();
        initEvents();
        init();
        return mView;
    }

    @Override
    protected void initViews() {
        mLayoutInflater = LayoutInflater.from(getActivity());

        if(mInfos.getGenders() != null && mInfos.getGenders().size() > 1) {
            llytGenders = (LinearLayout)mView.findViewById(R.id.llyt_genders);
            tflytGendrs = (TagFlowLayout)mView.findViewById(R.id.genders_flow_layout);
            genderAdapter = new TagAdapter<SortInfo>(mInfos.getGenders()) {
                @Override
                public View getView(FlowLayout parent, int position, SortInfo info) {
                    TextView tvGenders = (TextView) mLayoutInflater.inflate(R.layout.goods_list_tagflow_item, tflytGendrs, false);
                    tvGenders.setText(info.getName());
                    return tvGenders;
                }
            };
            tflytGendrs.setAdapter(genderAdapter);
            initChooseState(tflytGendrs, mInfos.getGenders());
            tflytGendrs.setOnTagClickListener(new onTabTextClickListener(tflytGendrs, mInfos.getGenders(), genderAdapter));
            llytGenders.setVisibility(View.VISIBLE);
        }

        if(mInfos.getCategories() != null && mInfos.getCategories().size() > 1) {
            llytCategorys = (LinearLayout)mView.findViewById(R.id.llyt_catergroys);
            tflytCategorys = (TagFlowLayout)mView.findViewById(R.id.categorys_flow_layout);
            ivMoreCatigorys = (ImageView)mView.findViewById(R.id.iv_more_categorys);
            if (mInfos.getCategories().size() < 7) {
                ivMoreCatigorys.setVisibility(View.INVISIBLE);
                mCategoryLists = mInfos.getCategories();
            } else {
                ivMoreCatigorys.setVisibility(View.VISIBLE);
                for (int i = 0; i < 6; i++) {
                    mCategoryLists.add(mInfos.getCategories().get(i));
                }
            }

            // 判断是否全部是相同的productid    tomyang 2016-7-14
            pro_type_id = mInfos.getCategories().get(1).getProduct_type_id();
            for(int i = 2; i < mInfos.getCategories().size(); i++ ){
                if(mInfos.getCategories().get(i).getProduct_type_id() != pro_type_id){
                    isSame = false;
                    break;
                }
            }

            categoryAdapter = new TagAdapter<SortInfo>(mCategoryLists) {
                @Override
                public View getView(FlowLayout parent, int position, SortInfo info) {
                    TextView tvCategorys = (TextView) mLayoutInflater.inflate(R.layout.goods_list_tagflow_item, tflytCategorys, false);
                    tvCategorys.setText(info.getName());
                    return tvCategorys;
                }
            };
            tflytCategorys.setAdapter(categoryAdapter);
            initChooseState(tflytCategorys, mCategoryLists);
            tflytCategorys.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    for (int i = 0; i < mCategoryLists.size(); i++) {
                        if (i == position) {
                            mCategoryLists.get(i).setCheck(true);
                        } else {
                            mCategoryLists.get(i).setCheck(false);
                        }
                    }
                    initChooseState(tflytCategorys, mCategoryLists);
                    if(isSame){
                        checkHasSize(pro_type_id);
                    }else {
                        checkHasSize(mCategoryLists.get(position).getProduct_type_id());
                    }
                    checkIndicatorLight();
                    return false;
                }
            });
            ivMoreCatigorys.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 6; i < mInfos.getCategories().size(); i++) {
                        mCategoryLists.add(mInfos.getCategories().get(i));
                    }
                    categoryAdapter.notifyDataChanged();
                    initChooseState(tflytCategorys, mCategoryLists);
                    ivMoreCatigorys.setVisibility(View.GONE);
                }
            });
            llytCategorys.setVisibility(View.VISIBLE);


        }

        llytSize = (LinearLayout)mView.findViewById(R.id.llyt_size);
        tflytSize = (TagFlowLayout)mView.findViewById(R.id.size_flow_layout);
        if(mInfos.getSize() != null) {
            for (int i = 0; i < mInfos.getSize().size(); i++) {
                for (int j = 0; j < mInfos.getSize().get(i).getSizes().size(); j++) {
                    if (mInfos.getSize().get(i).getSizes().get(j).isCheck() && j != 0) {
                        mSizeLists = mInfos.getSize().get(i).getSizes();
                        sizeAdapter = new TagAdapter<SortInfo>(mSizeLists) {
                            @Override
                            public View getView(FlowLayout parent, int position, SortInfo info) {
                                TextView tvSize = (TextView) mLayoutInflater.inflate(R.layout.goods_list_tagflow_item, tflytSize, false);
                                tvSize.setText(info.getName());
                                return tvSize;
                            }
                        };
                        tflytSize.setAdapter(sizeAdapter);
                        llytSize.setVisibility(View.VISIBLE);
                        initChooseState(tflytSize, mSizeLists);
                        tflytSize.setOnTagClickListener(new onTabTextClickListener(tflytSize, mSizeLists, sizeAdapter));
                    }
                }
            }
        }

        if(mInfos.getPrice_ranges() != null && mInfos.getPrice_ranges().size() > 1) {
            llytPrice = (LinearLayout)mView.findViewById(R.id.llyt_price);
            tflytPrice = (TagFlowLayout)mView.findViewById(R.id.price_flow_layout);
            priceAdapter = new TagAdapter<SortInfo>(mInfos.getPrice_ranges()) {
                @Override
                public View getView(FlowLayout parent, int position, SortInfo info) {
                    TextView tvPrice = (TextView) mLayoutInflater.inflate(R.layout.goods_list_tagflow_item, tflytPrice, false);
                    tvPrice.setText(info.getName());
                    return tvPrice;
                }
            };
            tflytPrice.setAdapter(priceAdapter);
            initChooseState(tflytPrice, mInfos.getPrice_ranges());
            tflytPrice.setOnTagClickListener(new onTabTextClickListener(tflytPrice, mInfos.getPrice_ranges(), priceAdapter));
            llytPrice.setVisibility(View.VISIBLE);
        }

        if(mInfos.getColor() != null && mInfos.getColor().size() > 1) {
            llytColor = (LinearLayout)mView.findViewById(R.id.llyt_color);
            tflytColor = (TagFlowLayout)mView.findViewById(R.id.color_flow_layout);
            colorAdapter = new TagAdapter<SortInfo>(mInfos.getColor()) {
                @Override
                public View getView(FlowLayout parent, int position, SortInfo info) {
                    RelativeLayout rlytColor = (RelativeLayout) mLayoutInflater.inflate(R.layout.goods_list_filter_color_item, tflytColor, false);
                    ImageView ivColor = (ImageView) rlytColor.findViewById(R.id.iv_color);
                    TextView tvColorAll = (TextView) rlytColor.findViewById(R.id.tv_all);
                    if(position == 0){
                        ivColor.setVisibility(View.GONE);
                        tvColorAll.setVisibility(View.VISIBLE);
                        tvColorAll.setText(info.getName());
                    }else {
                        ivColor.setVisibility(View.VISIBLE);
                        tvColorAll.setVisibility(View.GONE);
                        ivColor.setBackgroundColor(Color.rgb(info.getRed(), info.getGreen(), info.getBlue()));
                    }
                    return rlytColor;
                }
            };
            tflytColor.setAdapter(colorAdapter);
            initChooseState(tflytColor, mInfos.getColor());
            tflytColor.setOnTagClickListener(new onTabTextClickListener(tflytColor, mInfos.getColor(), colorAdapter));
            llytColor.setVisibility(View.VISIBLE);
        }

        checkIndicatorLight();

        if(!TextUtils.isEmpty(categoryId)){
            for(int i=0;i<mInfos.getCategories().size();i++){
                if(Integer.valueOf(categoryId)==mInfos.getCategories().get(i).getId()){
                    checkHasSize(mInfos.getCategories().get(i).getProduct_type_id());
                    break;
                }
            }
        }else if(isSame){
            checkHasSize(pro_type_id);
        }
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void init() {

    }

    // 标签选择状态更新ui
    private void initChooseState(TagFlowLayout layout, ArrayList<SortInfo> list) {
        for (int i = 0; i < list.size(); i++) {
            ((TagView)layout.getChildAt(i)).setChecked(list.get(i).isCheck());
        }
    }

    // 判断这个品类是否有尺码选择,有的话显示尺码选择,没有则隐藏尺码板块
    private boolean checkHasSize(int product_type_id ) {
        if(mInfos.getSize() != null) {
            for (int i = 0; i < mInfos.getSize().size(); i++) {
                if (mInfos.getSize().get(i).getProduct_type_id() == product_type_id) {
                    mSizeLists = mInfos.getSize().get(i).getSizes();
                    if (sizeAdapter == null) {
                        sizeAdapter = new TagAdapter<SortInfo>(mSizeLists) {
                            @Override
                            public View getView(FlowLayout parent, int position, SortInfo info) {
                                TextView tvSize = (TextView) mLayoutInflater.inflate(R.layout.goods_list_tagflow_item, tflytSize, false);
                                tvSize.setText(info.getName());
                                return tvSize;
                            }
                        };
                        tflytSize.setAdapter(sizeAdapter);
                        tflytSize.setOnTagClickListener(new onTabTextClickListener(tflytSize, mSizeLists, sizeAdapter));
                    } else {
                        sizeAdapter.notifyDataChanged();
                    }
                    initChooseState(tflytSize, mSizeLists);
                    llytSize.setVisibility(View.VISIBLE);
                    return true;
                }
            }
        }
        llytSize.setVisibility(View.GONE);
        return false;
    }


    // 清除全部选择
    public void reset(){
        if(mInfos.getGenders() != null && mInfos.getGenders().size() > 1) {
            for (int i = 0; i < mInfos.getGenders().size(); i++) {
                if (i == 0) {
                    mInfos.getGenders().get(i).setCheck(true);
                } else {
                    mInfos.getGenders().get(i).setCheck(false);
                }
            }
            genderAdapter.notifyDataChanged();
            initChooseState(tflytGendrs, mInfos.getGenders());
        }

        if(mInfos.getCategories() != null && mInfos.getCategories().size() > 1) {
            for (int i = 0; i < mInfos.getCategories().size(); i++) {
                if (i == 0) {
                    mInfos.getCategories().get(i).setCheck(true);
                } else {
                    mInfos.getCategories().get(i).setCheck(false);
                }
            }
            categoryAdapter.notifyDataChanged();
            initChooseState(tflytCategorys, mCategoryLists);
        }

        if(mInfos.getPrice_ranges() != null && mInfos.getPrice_ranges().size() > 1) {
            for (int i = 0; i < mInfos.getPrice_ranges().size(); i++) {
                if (i == 0) {
                    mInfos.getPrice_ranges().get(i).setCheck(true);
                } else {
                    mInfos.getPrice_ranges().get(i).setCheck(false);
                }
            }
            priceAdapter.notifyDataChanged();
            initChooseState(tflytPrice, mInfos.getPrice_ranges());
        }

        if(mInfos.getSize() != null) {
            for (int i = 0; i < mInfos.getSize().size(); i++) {
                for (int j = 0; j < mInfos.getSize().get(i).getSizes().size(); j++) {
                    if (j == 0) {
                        mInfos.getSize().get(i).getSizes().get(j).setCheck(true);
                    } else {
                        mInfos.getSize().get(i).getSizes().get(j).setCheck(false);
                    }
                }

            }
//            if (sizeAdapter != null) {
//                sizeAdapter.notifyDataChanged();
//                initChooseState(tflytSize, mInfos.getSize());
//                llytSize.setVisibility(View.GONE);
//            }
            if(isSame){
                checkHasSize(pro_type_id);
            }
        }

        if(mInfos.getColor() != null && mInfos.getColor().size() > 1) {
            for (int i = 0; i < mInfos.getColor().size(); i++) {
                if (i == 0) {
                    mInfos.getColor().get(i).setCheck(true);
                } else {
                    mInfos.getColor().get(i).setCheck(false);
                }
            }
            colorAdapter.notifyDataChanged();
            initChooseState(tflytColor, mInfos.getColor());
        }

        ((GoodsListFilterActivity)getActivity()).setIndicatorLight(1, false);
        showToast("筛选条件已清除");
    }

    // 提交筛选条件
    public MallLimitOptionInfo commit(){
        return mInfos;
    }

    // 检查小红点是否要亮
    private void checkIndicatorLight(){
        boolean genderAll = mInfos.getGenders().get(0).isCheck();
        boolean categoryAll = mInfos.getCategories().get(0).isCheck();
        boolean sizeAll = true;
        if(mSizeLists != null && mSizeLists.size() > 0) {
            sizeAll = mSizeLists.get(0).isCheck();
        }
        boolean priceAll = mInfos.getPrice_ranges().get(0).isCheck();
        boolean colorAll = mInfos.getColor().get(0).isCheck();

        if(genderAll && categoryAll && sizeAll && priceAll && colorAll ){
            ((GoodsListFilterActivity)getActivity()).setIndicatorLight(1, false);
        }else {
            ((GoodsListFilterActivity)getActivity()).setIndicatorLight(1, true);
        }

    }

    private class onTabTextClickListener implements TagFlowLayout.OnTagClickListener{
        private ArrayList<SortInfo> mSorts;
        private TagAdapter tagAdapter;
        private TagFlowLayout mLayout;

        public onTabTextClickListener(TagFlowLayout layout, ArrayList<SortInfo> info, TagAdapter tagAdapter) {
            this.mSorts = info;
            this.tagAdapter = tagAdapter;
            this.mLayout = layout;
        }

        @Override
        public boolean onTagClick(View view, int position, FlowLayout parent) {
            for (int i = 0; i < mSorts.size(); i++) {
                if (i == position) {
                    mSorts.get(i).setCheck(true);
                }else {
                    mSorts.get(i).setCheck(false);
                }
            }
            tagAdapter.notifyDataChanged();
            initChooseState(mLayout, mSorts);
            checkIndicatorLight();
            return false;
        }
    }

}
