package cn.yiya.shiji.entity;

public class NotifyItem {
	
	private String title;
	private String describe;
	private int type;
	private long time;
	private int subType;
	private String msg;
	private int status;
	private String des;

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	@Override
	public String toString() {
		return "NotifyItem [title=" + title + ", describe=" + describe
				+ ", type=" + type + ", time=" + time + ", subType=" + subType
				+ ", msg=" + msg + ", status=" + status + "]";
	}
	public int getStatus() {
		
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getSubType() {
		return subType;
	}
	public void setSubType(int subType) {
		this.subType = subType;
	}
	
//	public static void sortById(ArrayList<NotifyItem> list){
//		if(list==null || list.size()<1){
//
//		}
//		Collections.sort(list,new Comparator<NotifyItem>() {
//			@Override
//			public int compare(NotifyItem lhs, NotifyItem rhs) {
//				return (int)(lhs.getBaseObjId() - rhs.getBaseObjId());
//			}
//		});
//	}
	
}
