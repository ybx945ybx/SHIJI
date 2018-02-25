package cn.yiya.shiji.entity;

import java.util.ArrayList;

/**
 * Created by tomyang on 2015/9/21.
 */
public class Province {
    public String n;
    public String s;
    public ArrayList<City> l;

    public ArrayList<City> getL() {
        return l;
    }

    public void setL(ArrayList<City> l) {
        this.l = l;
    }

    public String getS() {

        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getN() {

        return n;
    }

    public void setN(String n) {
        this.n = n;
    }
}
