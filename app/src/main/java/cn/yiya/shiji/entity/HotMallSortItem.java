package cn.yiya.shiji.entity;

/**
 * Created by jerryzhang on 2015/9/16.
 */
public class HotMallSortItem {
    public static final int ITEM = 0;
    public static final int SECTION = 1;
    private String logo;      //商城照片
    private String title;     //商城名
    private int id;
    public String text;
    public int type;
    public int sectionPosition;
    public int listPosition;
    public int getSectionPosition() {
        return sectionPosition;
    }

    public void setSectionPosition(int sectionPosition) {
        this.sectionPosition = sectionPosition;
    }
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setType(int type) {
        this.type = type;
    }

    public void setText(String text) {
        this.text = text;
    }
    public int getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
