package cn.yiya.shiji.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.City;
import cn.yiya.shiji.entity.Provinces;
import cn.yiya.shiji.utils.FileUtil;

/**
 * Created by tomyang on 2015/9/21.
 */
public class MyAreaSelect extends LinearLayout {
    private ScrollerPicker cityPicker,provincePicker,counyPicker;
    private OnSelectListener mSelectListener;
    private Provinces data;                                     //所有省市区
    private ArrayList<City> selectedCity;                       //当前选择省的所有市
//    private HashMap<String,HashMap<String,String>> dataResource;
//    private HashMap<String, HospitalCollection> hospiatlCollection;	//所选择市的所有区和该区内的医院
//    private HashMap<String, String> selectedCity;						//当前选择省的所有市和市编号
    private ArrayList<String> provinces;                             //所有省

    //	private int tempProvince=-1,tempCity=-1,tempCouny=-1;

    @SuppressLint("NewApi")
    public MyAreaSelect(Context context, AttributeSet attrs, int defStyleAttr,
                        int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public OnSelectListener getmSelectListener() {
        return mSelectListener;
    }
    public void setmSelectListener(OnSelectListener mSelectListener) {
        this.mSelectListener = mSelectListener;
    }
    @SuppressLint("NewApi")
    public MyAreaSelect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MyAreaSelect(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyAreaSelect(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        readDataResuorce();
        inflate(context, R.layout.layout_citypicker, this);
        provincePicker = (ScrollerPicker) findViewById(R.id.province);
        cityPicker = (ScrollerPicker) findViewById(R.id.city);
        counyPicker = (ScrollerPicker) findViewById(R.id.regin);
        provincePicker.setOnSelectListener(new ProvinceListener());
        cityPicker.setOnSelectListener(new CityListener());
        counyPicker.setOnSelectListener(new CounyListener());
        provincePicker.setData(getProvinces());
        provincePicker.setDefault(0);
        cityPicker.setData(getCityNames("北京市"));
        cityPicker.setDefault(0);
        counyPicker.setData(readDefaultCounies());
        counyPicker.setDefault(0);
    }

    public interface OnSelectListener {
        public void selected(String[] selectCity);
    }

    class ProvinceListener implements ScrollerPicker.OnSelectListener{
        @Override
        public void endSelect(int id, String text) {
            if (text.equals("") || text == null )return;// || id==tempProvince
            cityPicker.setData(getCityNames(text));
            cityPicker.setDefault(0);
            counyPicker.setData(readDefaultCounies());
            counyPicker.setDefault(0);
            if(mSelectListener!=null){
                mSelectListener.selected(getSelectCity());
            }
        }

        @Override
        public void selecting(int id, String text) {
        }
    }
    class CityListener implements ScrollerPicker.OnSelectListener{
        @Override
        public void endSelect(int id, String text) {
            if(readCounies(text) == null){     // tom 听云bug
                return;
            }
            counyPicker.setData(readCounies(text));
            counyPicker.setDefault(0);

            if(mSelectListener!=null){
                mSelectListener.selected(getSelectCity());
            }

        }
        @Override
        public void selecting(int id, String text) {
        }
    }
    class CounyListener implements ScrollerPicker.OnSelectListener{
        @Override
        public void endSelect(int id, String text) {
            if(mSelectListener!=null){
                mSelectListener.selected(getSelectCity());
            }
        }
        @Override
        public void selecting(int id, String text) {
        }
    }

    //读取省数组
    public ArrayList<String> getProvinces(){
        provinces = new ArrayList<String>();
        for(int i = 0;i<data.l.size();i++){
            provinces.add(data.l.get(i).getN());

        }
        Log.i("TAG",provinces.toString());
        return provinces;
    }

    /**
     * 读取省市资料
     */
    private void readDataResuorce(){
        String json = FileUtil.getFromAssets(getContext(), "area.min.json");
        data = new Gson().fromJson(json, new TypeToken<Provinces>(){}.getType());
    }
    /**
     * 根据城市获取counypicker的数据源
     * @return
     */
    private ArrayList<String> readCounies(String city){
//        String code = getCityCode(text);
//        hospiatlCollection = readHospitalCollections(code);
        if (selectedCity == null) {
            return null;
        }
        ArrayList<String> counies = new ArrayList<>();
        for(int i = 0;i<selectedCity.size();i++){
            if(TextUtils.equals(selectedCity.get(i).n,city) ) {
                if(selectedCity.get(i).l == null){      // tom 听云bug修改
                    return null;
                }
                for (int j = 0;j<selectedCity.get(i).l.size();j++) {
                    counies.add(selectedCity.get(i).l.get(j).n);
                }
            }

        }
        Log.i("TAG3-----------",counies.toString());
//        ArrayList<String> counies = new ArrayList<>();
//        for(Object obj : hospiatlCollection.keySet().toArray()){
//            counies.add(obj.toString());
//        }
        return counies;
    }

    /**
     * 获取counypicker的默认数据源
     * @return
     */
    private ArrayList<String> readDefaultCounies(){

        //String city = getDefoatCity();
        //selectedCity.get(0).l
        //hospiatlCollection = readHospitalCollections(code);
        ArrayList<String> counies = new ArrayList<>();
//        for(Object obj : hospiatlCollection.keySet().toArray()){
//            counies.add(obj.toString());
//        }
        for(int i = 0;i<selectedCity.get(0).l.size();i++){
            counies.add(selectedCity.get(0).l.get(i).n);
        }
        Log.i("TAG2------------",counies.toString());
        return counies;
    }
//    /**
//     * 读取区域医院json
//     * @param code
//     * @return
//     */
//    private HashMap<String, HospitalCollection> readHospitalCollections(String code){
//        String json = FileUtil.getFromAssets(getContext(), "hospital/"+code+".json");
//        Gson gson = new Gson();
//        hospiatlCollection = gson.fromJson(json, new TypeToken<HashMap<String,HospitalCollection>>(){}.getType());
//        return hospiatlCollection;
//    }
    /**
     * 获取citypicker数据源
     * @param province
     * @return
     */
    private ArrayList<String> getCityNames(String province){
        ArrayList<String> cityNames = new ArrayList<>();
//        selectedCity = dataResource.get(province);
//        for(Map.Entry<String,String> i: selectedCity.entrySet()){
//            cityNames.add(i.getKey());
//        }
        for(int i = 0;i<data.l.size();i++){
            if(TextUtils.equals(data.l.get(i).n,province) ) {
               selectedCity = data.l.get(i).l;
               // Log.i("TAG1-----------",selectedCity.toString());
                for (int j = 0;j<data.l.get(i).l.size();j++) {
                    cityNames.add(data.l.get(i).l.get(j).getN());
                }
            }
        }
        return cityNames;
    }

    /**
     * 获取默认的城市code  第一个
     * @return
     */
    private String getDefoatCity(){
//        for(Map.Entry<String,String> i: selectedCity.entrySet()){
//            return i.getValue();
//        }
//        return "";
        return selectedCity.get(0).n;
    }
//    private String getCityCode(String text){
//        return selectedCity.get(text);
//    }

    /**
     * 设置初始值
     * @param str  形如  省-市-县
     */
    public void setCityAddress(String str){
        if(TextUtils.isEmpty(str)){
            return;
        }
        String[] address = str.split("-");
        if(address.length != 3){
            Log.e("---", "length!=3 ");
            return;
        }
        int position = getPosition(provinces, address[0]);
        if(position<0){
            Log.e("---", "province position <0");
            return;
        }
        provincePicker.setDefault(position);
        ArrayList<String> cities = getCityNames(address[0]);
        cityPicker.setData(cities);
        position = getPosition(cities, address[1]);
        if(position<0){
            position = 0;
        }
        cityPicker.setDefault(position);
        ArrayList<String> counies = readCounies(cities.get(position));
        counyPicker.setData(counies);
        position = getPosition(counies, address[2]);
        if(position<0){
            position = 0;
        }
        counyPicker.setDefault(position);
    }
    /**
     * 获取选中位置
     * @param list
     * @param selected
     * @return
     */
    private static int getPosition(ArrayList<String> list,String selected){
        int position = -1;
        for(int i=0;i<list.size();i++){
            if(list.get(i).trim().equals(selected.trim())){
                position=i;
                break;
            }
        }
        return position;
    }
//    /**
//     * 获取选中地区的医院列表
//     * @return
//     */
//    public ArrayList<Hospital> getHospitals(){
//        return hospiatlCollection.get(counyPicker.getSelectedText()).list;
//    }
    /**
     * 获取选中地址字符串
     * @return
     */
    public String[] getSelectCity(){
        String[] str = new String[3];
        str[0] = provincePicker.getSelectedText();
        str[1] = cityPicker.getSelectedText();
        str[2] = counyPicker.getSelectedText();
        return str;
    }
}
