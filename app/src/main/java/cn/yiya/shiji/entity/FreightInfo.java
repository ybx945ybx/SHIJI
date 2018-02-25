package cn.yiya.shiji.entity;

/**
 * Created by chenjian on 2016/1/20.
 */
public class FreightInfo {

    /**
     * id : 1
     * name : 包税渠道
     * des : 不需缴关税,慢
     * detail : 40元/首500g,5元/继100g
     * delivery : 1
     * select : true
     * tax : false
     * days_des : 18-20工作日
     * tax_fee : 0
     * tax_des : 未含税
     * fee : 465
     */

    private int id;
    private String name;
    private String des;
    private String detail;
    private int delivery;
    private boolean select;
    private boolean tax;
    private String days_des;
    private String tax_fee;
    private boolean show;
    private String freight;
    private String fee_des;
    private String tax_des;
    private String fee;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public void setTax(boolean tax) {
        this.tax = tax;
    }

    public void setDays_des(String days_des) {
        this.days_des = days_des;
    }

    public void setTax_fee(String tax_fee) {
        this.tax_fee = tax_fee;
    }

    public void setTax_des(String tax_des) {
        this.tax_des = tax_des;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDes() {
        return des;
    }

    public String getDetail() {
        return detail;
    }

    public int getDelivery() {
        return delivery;
    }

    public boolean isTax() {
        return tax;
    }

    public String getDays_des() {
        return days_des;
    }

    public String getTax_fee() {
        return tax_fee;
    }

    public String getTax_des() {
        return tax_des;
    }

    public String getFee() {
        return fee;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getFee_des() {
        return fee_des;
    }

    public void setFee_des(String fee_des) {
        this.fee_des = fee_des;
    }
}
