package cn.yiya.shiji.business;

import java.util.List;

public class HttpMessage {
	public int code;
	public String message;
	public String data;
	public Object obj;			//对象
	public List<Object> list;	//数组
	
	public static HttpMessage getDefault(){
		HttpMessage msg= new HttpMessage();
		msg.code = -1;
		msg.message = "联网失败";
		return msg;
	}
	
	public boolean isSuccess(){
		if(code == 0){
			return true;
		}
		return false;
	}

    public boolean isNull() {
        if (data == null) {
            return true;
        }
        return false;
    }

	// 判断是否失去登录态  tomyang  2016-5-27
	public boolean isLossLogin(){
		if(code == 101){
			return true;
		}
		return false;
	}
/**
     * 判断是否开店
     *
     * @return
     */
    public boolean isNotHasShop() {
        if (code == 200) {
            return true;
        }
        return false;
    }
}
