package cn.yiya.shiji.entity;

import java.util.ArrayList;

/**
 * Created by tomyang on 2015/9/21.
 */
public class City {
    public String n;
    public String s;
    public String p;
    public ArrayList<Couny> l;

    public ArrayList<Couny> getL() {
        return l;
    }

    public void setL(ArrayList<Couny> l) {
        this.l = l;
    }

    public String getP() {

        return p;
    }

    public void setP(String p) {
        this.p = p;
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
