package cn.yiya.shiji.entity;

import java.util.ArrayList;

/**
 * Created by tomyang on 2015/9/21.
 */
public class Provinces {
    public String n;
    public ArrayList<Province> l;

    public ArrayList<Province> getL() {
        return l;
    }

    public void setL(ArrayList<Province> l) {
        this.l = l;
    }

    public String getN() {

        return n;
    }

    public void setN(String n) {
        this.n = n;
    }
}
