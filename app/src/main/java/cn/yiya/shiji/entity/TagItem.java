package cn.yiya.shiji.entity;

import java.util.ArrayList;

public class TagItem {
	//"id": "xxx", "image": "xxx", "url": "xxx", "title":"xxx"}
	private int tag_id;
	private String content;
	private String image;
	private String recent_upload;
	private String fans_count;
	private ArrayList<Work> works;

	public int getTag_id() {
		return tag_id;
	}

	public void setTag_id(int tag_id) {
		this.tag_id = tag_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getRecent_upload() {
		return recent_upload;
	}

	public void setRecent_upload(String recent_upload) {
		this.recent_upload = recent_upload;
	}

	public String getFans_count() {
		return fans_count;
	}

	public void setFans_count(String fans_count) {
		this.fans_count = fans_count;
	}

	public ArrayList<Work> getWorks() {
		return works;
	}

	public void setWorks(ArrayList<Work> works) {
		this.works = works;
	}

	public class Work {
		private String image;
		private int work_id;

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public int getWork_id() {
			return work_id;
		}

		public void setWork_id(int work_id) {
			this.work_id = work_id;
		}
	}
}
