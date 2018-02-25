package cn.yiya.shiji.entity;

/**
 * Created by jerry on 2016/2/25.
 * 订单列表状态信息 group： //1:待付款，2:待缴税，3:运输中，4:完成，5:失效
 */
public class GroupInfo {
    private String groupDes;
    private int group;

    public String getGroupDes() {
        return groupDes;
    }

    public void setGroupDes(String groupDes) {
        this.groupDes = groupDes;
    }

    public int
    getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }
}
