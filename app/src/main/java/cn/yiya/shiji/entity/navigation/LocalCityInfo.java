package cn.yiya.shiji.entity.navigation;

import java.util.ArrayList;

/**
 * Created by chenjian on 2016/3/31.
 */
public class LocalCityInfo {

    private String cityID;
    private String cityName;
    private int newVersion;
    private boolean bSuccess;
    private String cityCnName;
    private ArrayList<LocalPackageInfo> versions;

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(int newVersion) {
        this.newVersion = newVersion;
    }

    public ArrayList<LocalPackageInfo> getVersions() {
        return versions;
    }

    public String getCityCnName() {
        return cityCnName;
    }

    public void setCityCnName(String cityCnName) {
        this.cityCnName = cityCnName;
    }

    public void setVersions(ArrayList<LocalPackageInfo> versions) {
        this.versions = versions;
    }

    public boolean isbSuccess() {
        return bSuccess;
    }

    public void setbSuccess(boolean bSuccess) {
        this.bSuccess = bSuccess;
    }
}
