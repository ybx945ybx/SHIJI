package cn.yiya.shiji.entity;

/**
 * Created by tomyang on 2015/9/16.
 */
public class AddressListItem {
    private int id;
    private String recipient;
    private String mobile;
    private String post_code;
    private String province;
    private String city;
    private String district;
    private String address;
    private String identity_number;
    private String identity_copy_front;
    private String identity_copy_back;
    private boolean selected;


    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getIdentity_copy_back() {
        return identity_copy_back;
    }

    public void setIdentity_copy_back(String getIdentity_copy_back) {
        this.identity_copy_back = getIdentity_copy_back;
    }

    public String getIdentity_copy_front() {

        return identity_copy_front;
    }

    public void setIdentity_copy_front(String identity_copy_front) {
        this.identity_copy_front = identity_copy_front;
    }

    public String getIdentity_number() {

        return identity_number;
    }

    public void setIdentity_number(String identity_number) {
        this.identity_number = identity_number;
    }

    public String getAddress() {

        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {

        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {

        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {

        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPost_code() {

        return post_code;
    }

    public void setPost_code(String post_code) {
        this.post_code = post_code;
    }

    public String getMobile() {

        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
}
