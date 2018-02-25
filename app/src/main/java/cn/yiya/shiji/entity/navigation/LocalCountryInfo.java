package cn.yiya.shiji.entity.navigation;

import java.util.ArrayList;

/**
 * Created by chenjian on 2016/3/31.
 */
public class LocalCountryInfo {

    private String countryID;
    private String countryName;
    private String countryCnName;
    private boolean bSuccess;
    private ArrayList<LocalCityInfo> cities;

    public boolean isbSuccess() {
        return bSuccess;
    }

    public void setbSuccess(boolean bSuccess) {
        this.bSuccess = bSuccess;
    }

    public String getCountryID() {
        return countryID;
    }

    public void setCountryID(String countryID) {
        this.countryID = countryID;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public ArrayList<LocalCityInfo> getCities() {
        return cities;
    }

    public void setCities(ArrayList<LocalCityInfo> cities) {
        this.cities = cities;
    }

    public String getCountryCnName() {
        return countryCnName;
    }

    public void setCountryCnName(String countryCnName) {
        this.countryCnName = countryCnName;
    }
}
