package cn.yiya.shiji.entity;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.views.TagAbleImageView;

/**
 * Created by weixuewu on 15/7/29.
 */
public class WorkImage {
    private String url;
    private String content;
    public ArrayList<WorkTag> tags;
    public String goods_id;
    private String recommend;      //推荐人
    private List<GoodsEntity> goods_ids;

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<WorkTag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<WorkTag> tags) {
        this.tags = tags;
    }

    public ArrayList<TagAbleImageView.TagInfo> getTagInfos() {
        ArrayList<TagAbleImageView.TagInfo> list = new ArrayList<>();
        if (tags != null)
            for (int i = 0; i < tags.size(); i++) {
                list.add(tags.get(i).toTagInfo());
            }
        return list;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public List<GoodsEntity> getGoods_ids() {
        return goods_ids;
    }

    public void setGoods_ids(List<GoodsEntity> goods_ids) {
        this.goods_ids = goods_ids;
    }

    public static class GoodsEntity {
        private String id;
        private String cover;
        private String price;
        private String name;
        private String brand;
        private String category;
        private String recommend; //推荐人

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getRecommend() {
            return recommend;
        }

        public void setRecommend(String recommend) {
            this.recommend = recommend;
        }

    }

}
