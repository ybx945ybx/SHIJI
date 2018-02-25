package cn.yiya.shiji.entity;

import java.io.Serializable;

/**
 * 每个size匹配的尺码表
 * Created by Amy on 2016/11/16.
 */

public class SizeTableEntity implements Serializable {
    String name;
    String value;
    boolean isMark;//用来设置背景的颜色

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isMark() {
        return isMark;
    }

    public void setMark(boolean mark) {
        isMark = mark;
    }
}
