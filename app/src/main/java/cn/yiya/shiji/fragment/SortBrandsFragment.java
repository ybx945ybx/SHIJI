package cn.yiya.shiji.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Locale;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.NewSingleBrandActivity;
import cn.yiya.shiji.activity.PopularBrandsListActivity;
import cn.yiya.shiji.adapter.SortBrandsAdapter;
import cn.yiya.shiji.entity.BrandsSortItem;
import cn.yiya.shiji.entity.CharacterParser;
import cn.yiya.shiji.entity.Item;
import cn.yiya.shiji.views.PinnedSectionListView;
import cn.yiya.shiji.views.SideBar;

/**
 * Created by jerryzhang on 2015/10/14.
 */
public class SortBrandsFragment extends BaseFragment implements View.OnClickListener {
    private SideBar sideBar;
    private Handler mHandler;
    private PinnedSectionListView mPinnedSectionListView;
    public ArrayList<BrandsSortItem> nlist = new ArrayList<>();
    private ArrayList<BrandsSortItem> mList = new ArrayList<>();
    private CharacterParser characterParser;
    private SortBrandsAdapter adapter;
    private PopularBrandsListActivity mBrandsFourActivity;
    private String[] arr = new String['Z' - 'A' + 2];
    private boolean isLoadingFinished = false;
    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler(getActivity().getMainLooper());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_list_test, container, false);
        initViews();
        initEvents();
        init();
        return mView;
    }

    @Override
    protected void initViews() {
        sideBar = (SideBar)mView.findViewById(R.id.sidrbar);
        sideBar.setVisibility(View.INVISIBLE);
        mPinnedSectionListView = (PinnedSectionListView)mView.findViewById(R.id.list);
        mPinnedSectionListView.setFastScrollEnabled(false);
        characterParser = CharacterParser.getInstance();
        mBrandsFourActivity = (PopularBrandsListActivity) getActivity();
    }

    @Override
    protected void initEvents() {
        showPreDialog("正在加载中……");
        mPinnedSectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BrandsSortItem item = (BrandsSortItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), NewSingleBrandActivity.class);
                intent.putExtra("brand_id", item.getId());
                startActivity(intent);
            }
        });
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                if (adapter != null) {
                    int position = adapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        mPinnedSectionListView.setSelection(position);
                    }
                }
            }
        });


        mPinnedSectionListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        InputMethodManager imm = (InputMethodManager) getActivity().
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void init() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && !isLoadingFinished) {
        }
    }



    public void generateDataset(char from, char to) {

        final int sectionsNumber = to - from + 2;
        int sectionPosition = 0, listPosition = 0;
        boolean hasIndex = false;

        ArrayList<ArrayList> allList = new ArrayList<>();

        for (char i = 0; i < sectionsNumber; i++) {

            ArrayList<BrandsSortItem> tmp = new ArrayList<>();

            allList.add(tmp);
        }


        for (int j = 0; j < nlist.size(); j++) {
            String pinyin = characterParser.getSelling(nlist.get(j).getName());

            int charNumber = pinyin.toUpperCase(Locale.ENGLISH).charAt(0);

            if (charNumber - 'A' >= 0 && charNumber - 'A' <= 25) {
                ArrayList<BrandsSortItem> tmp = allList.get(charNumber - 'A');
                tmp.add(nlist.get(j));
            } else {
                ArrayList<BrandsSortItem> tmp = allList.get(26);
                tmp.add(nlist.get(j));
            }
        }

        for (char i = 0; i < sectionsNumber; i++) {
            hasIndex = false;
            BrandsSortItem section = new BrandsSortItem();
            section.setType(Item.SECTION);
            section.setName(String.valueOf((char) ('A' + i)));
            section.setCn_name("");
            section.sectionPosition = sectionPosition;
            section.listPosition = listPosition++;

            if (('A' + i) > 'Z') {
                section.setType(Item.SECTION);
                section.setName("#");
            }
            mList.add(section);

            ArrayList<BrandsSortItem> tmp = allList.get(i);
            if (tmp.size() > 0) {
                hasIndex = true;
            }

            mList.addAll(tmp);

            if (!hasIndex) {
                mList.remove(section);
            } else {
                arr[i] = (section.getName());
            }

        }
        SideBar.setStringArr(arr);
        sideBar.invalidate();
        sideBar.setVisibility(View.VISIBLE);
    }

    public void setList(ArrayList<BrandsSortItem> list) {
        this.nlist = list;
    }

    public ArrayList<BrandsSortItem> getList() {
        return this.nlist;
    }

    @Override
    public void onResume() {
        super.onResume();
        PopularBrandsListActivity popularBrandsListActivity = (PopularBrandsListActivity) getActivity();
        popularBrandsListActivity.setDataListenner(new PopularBrandsListActivity.TransforData() {
            @Override
            public void DataListenner(ArrayList<BrandsSortItem> nlist) {
                setList(nlist);
                generateDataset('A', 'Z');
                adapter = new SortBrandsAdapter(getActivity(), mList);
                mPinnedSectionListView.setAdapter(adapter);
                mBrandsFourActivity.hidePreDialog();
                hidePreDialog();
            }
        });
    }
}
