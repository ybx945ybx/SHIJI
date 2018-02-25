package cn.yiya.shiji.entity;


import cn.yiya.shiji.views.TagAbleImageView;

/**
 * Created by weixuewu on 15/7/29.
 */
public class WorkTag {
    private int tag_id;
    private String content;
    private int type;
    private String x;
    private String y;
    private String direction;
    private String details;
    private int group_id;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public TagAbleImageView.TagInfo toTagInfo(){
        TagAbleImageView.TagInfo tagInfo=new TagAbleImageView.TagInfo();
        tagInfo.setX(Float.parseFloat(x));
        tagInfo.setY(Float.parseFloat(y));
        tagInfo.setType(this.type);
        tagInfo.setContent(this.content);
        tagInfo.setDetails(this.details);
        if(direction==null||"".equals(direction))
            tagInfo.setDirection(0);
        else
            tagInfo.setDirection(Integer.parseInt(this.direction));
        return tagInfo;
    }
}
