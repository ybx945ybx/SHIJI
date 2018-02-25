package cn.yiya.shiji.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.NewCategoryActivity;
import cn.yiya.shiji.activity.NewSingleBrandActivity;
import cn.yiya.shiji.adapter.NewBrandLetterAdapter;
import cn.yiya.shiji.adapter.NewBrandSortAdapter;
import cn.yiya.shiji.entity.BrandsSortItem;
import cn.yiya.shiji.entity.Item;
import cn.yiya.shiji.views.PinnedSectionListView;


/**
 * Created by Amy on 2016/6/20.
 */
public class NewCategoryBrandFragment extends BaseFragment {
    private NewCategoryActivity mActivity;
    private View mView;

    private RecyclerView rvLetter;
    private NewBrandLetterAdapter letterAdapter;
    private String[] letters = new String[]{"#", "A", "B", "C", "D", "E", "F", "G", "H"
            , "I", "J", "K", "L", "M", "N", "O", "P", "Q"
            , "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private PinnedSectionListView lvBrand;
    private NewBrandSortAdapter brandAdapter;
    private ArrayList<BrandsSortItem> mList = new ArrayList<>();
    private int letterCurrentPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (NewCategoryActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_new_category_brand, null);
        initViews();
        initEvents();
        init();
        return mView;
    }

    @Override
    protected void initViews() {
        rvLetter = (RecyclerView) mView.findViewById(R.id.rv_letter);
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 9);
        rvLetter.setLayoutManager(layoutManager);
        letterAdapter = new NewBrandLetterAdapter(mActivity, letters);
        rvLetter.setAdapter(letterAdapter);

        lvBrand = (PinnedSectionListView) mView.findViewById(R.id.lv_brand);
        lvBrand.setFastScrollEnabled(false);
    }

    @Override
    protected void initEvents() {
        letterAdapter.setOnItemClickListener(new NewBrandLetterAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                try {
                    int sposition = brandAdapter.getPositionForSection(letters[position].charAt(0));
                    if (sposition != -1) {
                        lvBrand.setSelection(sposition);
                    }
                } catch (Exception ex) {

                }

            }

        });

        lvBrand.setLetterChangeListener(new PinnedSectionListView.OnLetterChangeListener() {
            @Override
            public void OnLetterChange(int position) {
                String letter = mList.get(position).getName();
                for (int i = 0; i < letters.length; i++) {
                    if (letter.equals(letters[i])) {
                        letterCurrentPosition = i;
                        break;
                    }
                }
                letterAdapter.setClickPosition(letterCurrentPosition);

            }
        });
        lvBrand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BrandsSortItem item = (BrandsSortItem) parent.getItemAtPosition(position);
                if (item.type == Item.SECTION) return;
                Intent intent = new Intent(mActivity, NewSingleBrandActivity.class);
                intent.putExtra("brand_id", item.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void init() {
        mActivity.setTransforListener(new NewCategoryActivity.TransforDataListener() {
            @Override
//            public void transfor(ArrayMap<String, ArrayList<BrandsSortItem>> sortMap) {
            public void transfor(ArrayList<BrandsSortItem> list) {
                mList = list;
                brandAdapter = new NewBrandSortAdapter(getActivity(), mList);
                lvBrand.setAdapter(brandAdapter);
            }
        });

    }
}
