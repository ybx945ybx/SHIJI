package cn.yiya.shiji.entity;

public class Item {
	
	public static final int ITEM = 0;
	public static final int SECTION = 1;

//	public final int type;
//	public final String text;

	public int sectionPosition;
	public int listPosition;
	public String url;
	public String text;
	public int type;
	private String sortLetters;              // ��ʾ����ƴ��������ĸ

//	public Item(int type, String text) {
//	    this.type = type;
//	    this.text = text;
//	}

	@Override
	public String toString() {
		return text;
	}

	public int getSectionPosition() {
		return sectionPosition;
	}

	public void setSectionPosition(int sectionPosition) {
		this.sectionPosition = sectionPosition;
	}

	public int getListPosition() {
		return listPosition;
	}

	public void setListPosition(int listPosition) {
		this.listPosition = listPosition;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getType() {
		return type;
	}

	public String getText() {
		return text;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setText(String text) {
		this.text = text;
	}
}
