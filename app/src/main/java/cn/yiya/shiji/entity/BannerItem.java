package cn.yiya.shiji.entity;

public class BannerItem {
	//"id": "xxx", "image": "xxx", "url": "xxx", "title":"xxx"}
	private String CouponId;
	private String id;
	private String image;
	private String url;
	private String title;
	private int imageResource;

	public String getCouponId() {
		return CouponId;
	}

	public void setCouponId(String couponId) {
		CouponId = couponId;
	}

	public int getImageResource() {
		return imageResource;
	}

	public void setImageResource(int imageResource) {
		this.imageResource = imageResource;
	}

//	public int getId() {
//		return id;
//	}
//	public void setId(int id) {
//		this.id = id;
//	}
//
//	public String getImage() {
//		return image;
//	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
