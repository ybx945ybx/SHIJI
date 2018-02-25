package cn.yiya.shiji.entity;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.navigation.CountryListInfo;

public class BannerObject {

	public ArrayList<BannerItem> list;
	public ArrayList<BannerItem> mlist;
	public ArrayList<ParallaxModel> mCountrylist;
	public ArrayList<TravleLineInfo> mLinelist;
	public ArrayList<CountryListInfo> mList;

	public ArrayList<BannerItem> addData(){
		mlist = new ArrayList<BannerItem>();
		for(int i = 0;i<3;i++){
			BannerItem banner = new BannerItem();
			banner.setId(i+"");
			banner.setTitle("标题");
			banner.setImageResource(R.mipmap.exchange_coupon);
			mlist.add(banner);
		}
		return mlist;
	}
//	public ArrayList<ParallaxModel> addCountryData(){
//		mCountrylist = new ArrayList<ParallaxModel>();
//		for(int i = 0;i<10;i++){
//			ParallaxModel banner = new ParallaxModel();
//			banner.setId(i);
//			banner.setTitle("标题");
//			banner.setCity_num(i);
//			banner.setCn_name("美国" + i);
//			banner.setName("Amercian" + i);
//			banner.setImageResource(R.mipmap.lorempixel);
//			mCountrylist.add(banner);
//		}
//		return mCountrylist;
//	}
	public ArrayList<TravleLineInfo> addLineData(){
		mLinelist = new ArrayList<TravleLineInfo>();
		for(int i = 0;i<5;i++){
			TravleLineInfo banner = new TravleLineInfo();
			banner.setId(i);
			banner.setCover("标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题");
			banner.setImageResource(R.mipmap.exchange_coupon);
			mLinelist.add(banner);
		}
		return mLinelist;
	}

	public ArrayList<CountryListInfo> addHotcityData(){
		mList = new ArrayList<CountryListInfo>();
		for(int i = 0;i<5;i++){
			CountryListInfo banner = new CountryListInfo();
			banner.setId(i+"");
			banner.setCover("标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题");
			banner.setImageResource(R.mipmap.exchange_coupon);
			banner.setCn_name("中国");
//			banner.setName("china");banner.setShop_num(i);
			mList.add(banner);
		}
		return mList;
	}

	public ArrayList<CountryListInfo> addHotMessageData(){
		mList = new ArrayList<CountryListInfo>();
		for(int i = 0;i<5;i++){
			CountryListInfo banner = new CountryListInfo();
			banner.setId(i+"");
			banner.setCover("标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题");
			banner.setImageResource(R.mipmap.exchange_coupon);
			banner.setCn_name("中国");
			banner.setName("china");
			mList.add(banner);
		}
		return mList;
	}

	public ArrayList<CountryListInfo> addRecommendData(){
		mList = new ArrayList<CountryListInfo>();
		for(int i = 0;i<5;i++){
			CountryListInfo banner = new CountryListInfo();
			banner.setId(i+"");
			banner.setCover("标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题");
			banner.setImageResource(R.mipmap.exchange_coupon);
			banner.setCn_name("中国");
			banner.setName("china");
			mList.add(banner);
		}
		return mList;
	}
}
