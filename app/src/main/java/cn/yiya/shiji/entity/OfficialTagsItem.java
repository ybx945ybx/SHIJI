package cn.yiya.shiji.entity;

/**
 * Created by jerryzhang on 2015/9/23.
 */
public class OfficialTagsItem {
    private int tag_id;
    private String image;

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public OfficialTagsItem(int tag_id) {
        this.tag_id = tag_id;
    }
}
