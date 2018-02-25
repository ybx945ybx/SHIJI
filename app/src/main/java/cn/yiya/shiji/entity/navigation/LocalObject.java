package cn.yiya.shiji.entity.navigation;

import java.util.ArrayList;

/**
 * Created by chenjian on 2016/3/31.
 */
public class LocalObject {

    private int id;

    private ArrayList<LocalCountryInfo> pakages;

    public ArrayList<LocalCountryInfo> getPakages() {
        return pakages;
    }

    public void setPakages(ArrayList<LocalCountryInfo> pakages) {
        this.pakages = pakages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
