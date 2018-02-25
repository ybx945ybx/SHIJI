package cn.yiya.shiji.entity;

import android.text.TextUtils;

public class UserInfo {
	
	private String name;
	private String head;
	private int stage;
	private int user_id;
	private int gender;
	private String child_gender;
	private String expected_date;
	private String birth_date;
	private String hospital;
	private String city;
	private String child_name;
	private String child_weight;
	private boolean has_spouse;     
	private String spouse_head;
	private String m_id;
    private String phone;
	private String reg_status;

	public String getReg_status() {
		return reg_status;
	}

	public void setReg_status(String reg_status) {
		this.reg_status = reg_status;
	}

	public String getM_id() {
		return m_id;
	}

	public void setM_id(String m_id) {
		this.m_id = m_id;
	}

	public boolean isHas_spouse() {
		return has_spouse;
	}
	public void setHas_spouse(boolean has_spouse) {
		this.has_spouse = has_spouse;
	}
	public String getSpouse_head() {
		return spouse_head;
	}
	public void setSpouse_head(String spouse_head) {
		this.spouse_head = spouse_head;
	}
	public String getChild_name() {
		return child_name;
	}
	public void setChild_name(String child_name) {
		this.child_name = child_name;
	}
	public String getChild_weight() {
		return child_weight;
	}
	public void setChild_weight(String child_weight) {
		this.child_weight = child_weight;
	}
	public String getChild_gender() {
		return child_gender;
	}
	public void setChild_gender(String child_gender) {
		this.child_gender = child_gender;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public int getStage() {
		return stage;
	}
	public void setStage(int stage) {
		this.stage = stage;
	}
	public String getExpected_date() {
		return expected_date;
	}
	public void setExpected_date(String expected_date) {
		this.expected_date = expected_date;
	}
	public String getBirth_date() {
		return birth_date;
	}
	public void setBirth_date(String birth_date) {
		this.birth_date = birth_date;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public int getExpectedYear(){
		if(TextUtils.isEmpty(expected_date) || expected_date.length()!=10){
			return -1;
		}
		return Integer.parseInt(expected_date.substring(0,4));
	}
	public int getExpectedMonth(){
		if(TextUtils.isEmpty(expected_date) || expected_date.length()!=10){
			return -1;
		}
		return Integer.parseInt(expected_date.substring(5,7));
	}
	public int getExpectedDay(){
		if(TextUtils.isEmpty(expected_date) || expected_date.length()!=10){
			return -1;
		}
		return Integer.parseInt(expected_date.substring(9));
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
