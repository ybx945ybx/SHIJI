package cn.yiya.shiji.entity;

/**
 * Created by tomyang on 2015/9/14.
 *
 */
public class RedPackagrItem {
    private int id;
    private String logo;
    private float consumption;
    private float handsel;
    private String des;
    private String end_time;
    private String start_time;
    private String apply;
    private String source;
    private String create_time;
    private boolean selected;
    private boolean click;

    public String getApply() {
        return apply;
    }

    public void setApply(String apply) {
        this.apply = apply;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getStart_time() {

        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {

        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDes() {

        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public float getHandsel() {

        return handsel;
    }

    public void setHandsel(float handsel) {
        this.handsel = handsel;
    }

    public float getConsumption() {

        return consumption;
    }

    public void setConsumption(float consumption) {
        this.consumption = consumption;
    }

    public String getLogo() {

        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isClick() {
        return click;
    }

    public void setClick(boolean click) {
        this.click = click;
    }
}
