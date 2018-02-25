package cn.yiya.shiji.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.GoodsListFilterActivity;
import cn.yiya.shiji.adapter.FilterBrandsAdapter;
import cn.yiya.shiji.adapter.LetterAdapter;
import cn.yiya.shiji.adapter.LetterResultAdapter;
import cn.yiya.shiji.entity.BrandLetterInfo;
import cn.yiya.shiji.entity.MallLimitOptionInfo;
import cn.yiya.shiji.entity.SortInfo;
import cn.yiya.shiji.utils.RegexValidateUtil;
import cn.yiya.shiji.utils.SimpleUtils;

/**
 * 商品列表的品牌搜索fragment
 * Created by jerry on 2016/5/12.
 */
public class GoodsListBrandsFragment extends BaseFragment implements View.OnClickListener{

    /*
    品牌搜索条
     */
    private AutoCompleteTextView mAutoCompleteTextView;
    private FilterBrandsAdapter filterBrandsAdapter;
    private ImageView ivSearchCancle;
    /*
    首字母快捷搜索列表
     */
    private RecyclerView rycvLetter;
    private LetterAdapter letterAdapter;
    private ArrayList<BrandLetterInfo> letterList = new ArrayList<>();
    /*
    已选品牌板块
     */
    private RelativeLayout rlytSelectedBrands;
    private ScrollView sclvSelectedBrands;
    private TagFlowLayout tflytSelectedBrands;
    private TagAdapter selectedBrandsAdapter;
    private RelativeLayout rlytMoreSelectedBrands;
    private ImageView ivMoreSelectedBrands;
    private ArrayList<SortInfo> selectedBrandsList = new ArrayList<>();
    /*
    首字母快捷搜索结果列表
     */
    private TextView tvSelectedLetter;
    private RecyclerView rycvLetterResult;
    private LetterResultAdapter letterResultAdapter;

    private ArrayList<SortInfo> brandsData = new ArrayList<>();
    private MallLimitOptionInfo mInfos;
    private ArrayList<SortInfo> letterFilterBrandsList = new ArrayList<>();         // 首字母快速搜索结果列表
    private int firstPostion;                                                       // 字母列表默认第一个亮的
    private boolean needPosition = true;                                            // 是否还需要判断第一个亮的字母位置

    private boolean isExpand = false;

    private View mView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String data = this.getArguments().getString("data");
        mInfos = new Gson().fromJson(data, MallLimitOptionInfo.class);
        brandsData = mInfos.getBrands();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_goods_list_brands, container, false);

        initLetterList();
        initSelectedBrandList();

        initViews();
        initEvents();
        init();
        return mView;
    }

    @Override
    protected void initViews() {
        mAutoCompleteTextView = (AutoCompleteTextView)mView.findViewById(R.id.brand_search);
        if (brandsData != null) {
            filterBrandsAdapter = new FilterBrandsAdapter(getActivity(), brandsData);
            mAutoCompleteTextView.setAdapter(filterBrandsAdapter);
        }
        ivSearchCancle = (ImageView)mView.findViewById(R.id.iv_cancle);

        rycvLetter = (RecyclerView)mView.findViewById(R.id.rycv_letter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),9);
        rycvLetter.setItemAnimator(new DefaultItemAnimator());
        rycvLetter.setLayoutManager(gridLayoutManager);
        letterAdapter = new LetterAdapter(getActivity(), letterList);
        rycvLetter.setAdapter(letterAdapter);
        ViewGroup.LayoutParams layoutParams =  rycvLetter.getLayoutParams();
        int heigh;
        if(letterList.size() < 10){
            heigh = SimpleUtils.dp2px(getActivity(), 21);
        }else if(letterList.size() < 19){
            heigh = SimpleUtils.dp2px(getActivity(), 21)*2;
        }else {
            heigh = SimpleUtils.dp2px(getActivity(), 21)*3;
        }
        layoutParams.height = heigh;
        rycvLetter.setLayoutParams(layoutParams);

        rlytSelectedBrands = (RelativeLayout)mView.findViewById(R.id.rlyt_selected_brands);
        sclvSelectedBrands = (ScrollView)mView.findViewById(R.id.scroll_selected_brands);
        sclvSelectedBrands.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;                                                // 设置scrollview不可滚动
            }
        });
        rlytMoreSelectedBrands = (RelativeLayout)mView.findViewById(R.id.rlyt_more_brands);
        ivMoreSelectedBrands = (ImageView)mView.findViewById(R.id.iv_selected_brands_more);
        if(selectedBrandsList != null && selectedBrandsList.size() > 0){
            rlytSelectedBrands.setVisibility(View.VISIBLE);
            ((GoodsListFilterActivity)getActivity()).setIndicatorLight(2, true);
            ivMoreSelectedBrands.setVisibility(View.VISIBLE);
            ivMoreSelectedBrands.setImageResource(R.mipmap.xiala);
        }
        tflytSelectedBrands = (TagFlowLayout)mView.findViewById(R.id.tflyt_selected_brands);
        selectedBrandsAdapter = new TagAdapter<SortInfo>(selectedBrandsList) {
            @Override
            public View getView(FlowLayout parent, final int position, SortInfo info) {
                LinearLayout llytSelectedBrands = (LinearLayout)LayoutInflater.from(getActivity()).inflate(R.layout.goods_list_selected_brands_item, tflytSelectedBrands, false);
                TextView tvSelectedBrands = (TextView) llytSelectedBrands.findViewById(R.id.tv_brands);
                tvSelectedBrands.setText(info.getName());
                ImageView ivDeleteBrands = (ImageView) llytSelectedBrands.findViewById(R.id.iv_delete_brands);
                ivDeleteBrands.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updataLetterResultSelectedState(selectedBrandsList.get(position).getName(), false);
                        removeTag(position);
                    }
                });
                return llytSelectedBrands;
            }
        };
        tflytSelectedBrands.setAdapter(selectedBrandsAdapter);

        tvSelectedLetter = (TextView)mView.findViewById(R.id.tv_selected_letter);
        if(letterList != null && letterList.size() > 0) {
            tvSelectedLetter.setText(letterList.get(0).getName());
            getLetterFilterBrands(letterList.get(0).getName());
        }
        rycvLetterResult = (RecyclerView)mView.findViewById(R.id.rycv_letter_result);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rycvLetterResult.setItemAnimator(new DefaultItemAnimator());
        rycvLetterResult.setLayoutManager(linearLayoutManager);
        letterResultAdapter = new LetterResultAdapter(getActivity(), letterFilterBrandsList);
        rycvLetterResult.setAdapter(letterResultAdapter);
    }

    @Override
    protected void initEvents() {
        ivSearchCancle.setOnClickListener(this);
        if(filterBrandsAdapter != null){
            filterBrandsAdapter.setOnItemClickListener(new FilterBrandsAdapter.OnItemClickListener() {
                @Override
                public void onClickListener(SortInfo sortInfo) {
                    mAutoCompleteTextView.clearFocus();
                    hideSoftInput();
                    if(selectedBrandsList != null && selectedBrandsList.size() > 0) {
                        for (int i = 0; i < selectedBrandsList.size(); i++){
                            if(selectedBrandsList.get(i).getName().equals(sortInfo.getName())){
                                return;
                            }
                        }
                    }
                    addBrands(sortInfo.getName());
                    updataLetterResultSelectedState(sortInfo.getName(), true);
                }
            });
        }
        letterAdapter.setOnItemClickListener(new LetterAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(BrandLetterInfo sortInfo) {
                getLetterFilterBrands(sortInfo.getName());
                letterResultAdapter.notifyDataSetChanged();
                tvSelectedLetter.setText(sortInfo.getName());
            }
        });
        ivMoreSelectedBrands.setOnClickListener(this);
        letterResultAdapter.setOnItemClickListener(new LetterResultAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(String brandName, boolean isSelected) {
                if(isSelected){
                    addBrands(brandName);
                }else {
                    deleteBrands(brandName);
                }
            }
        });
    }

    @Override
    protected void init() {

    }


    // 初始化快捷首字母列表
    private void initLetterList() {
        String[] letterArry = new String[]{"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
                "T", "U", "V", "W", "X", "Y", "Z", };
        for (int i = 0; i < letterArry.length; i++ ){
            BrandLetterInfo brandLetterInfo = new BrandLetterInfo();
            brandLetterInfo.setName(letterArry[i]);
            if (brandsData != null) {
                if (i == 0) {
                    for (int j = 0; j < brandsData.size(); j++) {
                        String brand = brandsData.get(j).getName();
                        String letter = brand.substring(0, 1);
                        if (!RegexValidateUtil.checkLetter(letter)) {
//                            brandLetterInfo.setClickable(true);
//                            brandLetterInfo.setCheck(true);
//                            firstPostion = i;
//                            needPosition = false;
                            letterList.add(brandLetterInfo);
                            break;
                        }
                    }

                } else {
                    String key = letterArry[i];
                    for (int j = 0; j < brandsData.size(); j++) {
                        String brand = brandsData.get(j).getName();
                        if (brand.toUpperCase().startsWith(key)) {
//                            brandLetterInfo.setClickable(true);
//                            if(needPosition){
//                                firstPostion = i;
//                                needPosition = false;
//                                brandLetterInfo.setCheck(true);
//                            }
                            letterList.add(brandLetterInfo);
                            break;
                        }
                    }

                }
            }
//            letterList.add(brandLetterInfo);
        }
        if(letterList != null && letterList.size() > 0){
            letterList.get(0).setCheck(true);
        }

    }

    // 初始化已选品牌列表
    private void initSelectedBrandList() {
        if (brandsData != null) {
            for(int i = 0; i < brandsData.size(); i++){
                if(brandsData.get(i).isCheck()){
                    selectedBrandsList.add(brandsData.get(i));
                }
            }
        }
    }

    private void initView() {


    }

    private void initEvent() {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_cancle:
                mAutoCompleteTextView.setText("");
                hideSoftInput();
                break;
            case R.id.iv_selected_brands_more:
                if(!isExpand) {
                    sclvSelectedBrands.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;                                               // 设置scrollview可以滚动
                        }
                    });
                    setLayoutParams(180);
                    ivMoreSelectedBrands.setImageResource(R.mipmap.shousuo);
                    isExpand = true;
                }else {
                    sclvSelectedBrands.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return true;                                               // 设置scrollview不可以滚动
                        }
                    });
                    setLayoutParams(112);
                    ivMoreSelectedBrands.setImageResource(R.mipmap.xiala);
                    isExpand = false;
                }
                break;

        }
    }

    private void setLayoutParams(int heigh){    // 收缩 112   展开180
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.height = SimpleUtils.dp2px(getActivity(), heigh);
        rlytSelectedBrands.setLayoutParams(layoutParams);

        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams)sclvSelectedBrands.getLayoutParams();
        if(heigh == 180) {
            layoutParams1.bottomMargin = SimpleUtils.dp2px(getActivity(), 39);
        }else {
            layoutParams1.bottomMargin = SimpleUtils.dp2px(getActivity(), 0);
            sclvSelectedBrands.scrollTo(0, 0);
        }
        sclvSelectedBrands.setLayoutParams(layoutParams1);

    }

    //收起软键盘
    private void hideSoftInput(){
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mAutoCompleteTextView.getWindowToken(),0);
    }

    // 获取首字母筛选后的品牌列表
    private void getLetterFilterBrands(CharSequence constraint){
        if(letterFilterBrandsList != null){
            letterFilterBrandsList.clear();
        }
        if(constraint.equals("#")){
            if (brandsData != null) {
                for (int i = 0; i < brandsData.size(); i++) {
                    String brand = brandsData.get(i).getName();
                    String letter = brand.substring(0, 1);
                    if (!RegexValidateUtil.checkLetter(letter)) {
                        letterFilterBrandsList.add(brandsData.get(i));
                    }
                }
            }
        }else {
            String key = constraint.toString().toUpperCase();
            if (brandsData != null) {
                for (int i = 0; i < brandsData.size(); i++) {
                    String brand = brandsData.get(i).getName();
                    if (brand.toUpperCase().startsWith(key)) {
                        letterFilterBrandsList.add(brandsData.get(i));
                    }
                }
            }
        }
    }

    // 在首字母搜索结果列表中选择品牌
    private void addBrands(String brandName){
        SortInfo sortInfo = new SortInfo();
        sortInfo.setName(brandName);
        selectedBrandsList.add(sortInfo);

        selectedBrandsAdapter.notifyDataChanged();
        ((GoodsListFilterActivity)getActivity()).setIndicatorLight(2, true);

        if(rlytSelectedBrands.getVisibility() == View.GONE){        // 已选品牌板块未显示的 显示该板块
            rlytSelectedBrands.setVisibility(View.VISIBLE);
        }else {                                                     // 已选数量大于一行的 显示向下箭头
            ViewTreeObserver vto = tflytSelectedBrands.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    tflytSelectedBrands.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int heigh = tflytSelectedBrands.getMeasuredHeight();
                    int baseHeigh = SimpleUtils.dp2px(getActivity(), 34);
                    if(heigh > baseHeigh){
                        ivMoreSelectedBrands.setVisibility(View.VISIBLE);
                    }
                }
            });

        }
    }

    // 在首字母搜索结果列表中取消选择的品牌，并移除已选择板块中的该标签
    private void deleteBrands(String brandName){
        for(int i = 0; i < selectedBrandsList.size(); i++){
            if(brandName.equals(selectedBrandsList.get(i).getName())){
                removeTag(i);
            }
        }
    }

    // 删除已选择的品牌板块中的标签 更新ui
    private void removeTag(int position){
        selectedBrandsList.remove(position);

        selectedBrandsAdapter.notifyDataChanged();
        checkIndicatorLight();

        if(selectedBrandsList.size() == 0){
            rlytSelectedBrands.setVisibility(View.GONE);
            rlytMoreSelectedBrands.setVisibility(View.VISIBLE);
            ivMoreSelectedBrands.setImageResource(R.mipmap.xiala);
            ivMoreSelectedBrands.setVisibility(View.GONE);
            setLayoutParams(112);
            isExpand = false;
            sclvSelectedBrands.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }

//        if(tflytSelectedBrands.getMeasuredHeight() < SimpleUtils.dp2px(getActivity(), 35)){
//            if(ivMoreSelectedBrands.getVisibility() == View.VISIBLE) {
//                ivMoreSelectedBrands.setVisibility(View.GONE);
//            }
//        }

        ViewTreeObserver vto = tflytSelectedBrands.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tflytSelectedBrands.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int heigh = tflytSelectedBrands.getMeasuredHeight();
                int baseHeigh = SimpleUtils.dp2px(getActivity(), 35);
                if(heigh < baseHeigh){
//                    if(ivMoreSelectedBrands.getVisibility() == View.GONE) {
//                        ivMoreSelectedBrands.setVisibility(View.VISIBLE);
//                    }
                    ivMoreSelectedBrands.setVisibility(View.GONE);
                    ivMoreSelectedBrands.setImageResource(R.mipmap.xiala);
                    isExpand = false;
                    setLayoutParams(112);
                }
            }
        });

    }

    // 搜索框中选了品牌、已选品牌板块删除了品牌后，最下面的品牌列表中对应的跟新打钩状态及界面
    private void updataLetterResultSelectedState(String brand, boolean isChecked){
        for (int i = 0; i < brandsData.size(); i++){
            if(brandsData.get(i).getName().equals(brand)) {
                brandsData.get(i).setCheck(isChecked);
            }
            letterResultAdapter.notifyDataSetChanged();
        }
    }


    // 检查小红点是否要亮
    private void checkIndicatorLight(){
        if(selectedBrandsList == null || selectedBrandsList.size() == 0){
            ((GoodsListFilterActivity)getActivity()).setIndicatorLight(2, false);
        }else {
            ((GoodsListFilterActivity)getActivity()).setIndicatorLight(2, true);
        }

    }

    // 重置筛选条件
    public void reset() {
        for (int i = 0; i < brandsData.size(); i++){
            brandsData.get(i).setCheck(false);
        }
        mAutoCompleteTextView.setText("");
        if(letterList != null && letterList.size() > 0) {
            tvSelectedLetter.setText(letterList.get(0).getName());
        }
        selectedBrandsList.clear();
        selectedBrandsAdapter.notifyDataChanged();
        setLayoutParams(112);
        rlytSelectedBrands.setVisibility(View.GONE);
        rlytMoreSelectedBrands.setVisibility(View.VISIBLE);
        ivMoreSelectedBrands.setImageResource(R.mipmap.xiala);
        ivMoreSelectedBrands.setVisibility(View.GONE);
        isExpand = false;
        sclvSelectedBrands.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        letterAdapter.getmList().get(0).setCheck(true);
        for (int i = 1; i < letterAdapter.getmList().size(); i++){
//            if(i == firstPostion) {
//                letterAdapter.getmList().get(i).setCheck(true);
//            }else {
                letterAdapter.getmList().get(i).setCheck(false);
//            }
        }
        letterAdapter.notifyDataSetChanged();
        if(letterList != null && letterList.size() > 0) {
            getLetterFilterBrands(letterList.get(0).getName());
        }
        letterResultAdapter.notifyDataSetChanged();
        ((GoodsListFilterActivity)getActivity()).setIndicatorLight(2, false);
    }

    // 提交筛选条件
    public MallLimitOptionInfo commit(){
        return mInfos;
    }

}
