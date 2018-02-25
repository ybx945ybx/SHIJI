package cn.yiya.shiji.entity;

/**
 * Created by chenjian on 2016/4/20.
 */
public class PayModeInfo {

    /**
     * name : cmb
     * able : 1
     */

    private String name;
    private int able;
    private String words;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAble() {
        return able;
    }

    public void setAble(int able) {
        this.able = able;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public boolean bShow() {
        return able == 1 ? true : false;
    }

    public boolean bPayCMB () {
        return name.equals("cmb") ? true : false;
    }

    public boolean bPayWX () {
        return name.equals("alipay") ? true : false;
    }

    public boolean bPayZFB () {
        return name.equals("wx") ? true : false;
    }
}
