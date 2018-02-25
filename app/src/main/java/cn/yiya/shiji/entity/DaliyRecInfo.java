package cn.yiya.shiji.entity;

import java.util.List;

/**
 * Created by chenjian on 2016/6/13.
 */
public class DaliyRecInfo {

    /**
     * brand_id : 1
     * tag_id : 1
     * brand_name : nike
     * cover :
     * goods : [{"cover":"","status":2,"goods_id":"55f8de39d4a8cd61ac0f8d02"}]
     * follow : 1
     */

    private List<ListEntity> list;

    public List<ListEntity> getList() {
        return list;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public static class ListEntity {
        private int brand_id;
        private int tag_id;
        private String brand_name;
        private String cover;
        private int follow;
        /**
         * cover :
         * status : 2
         * goods_id : 55f8de39d4a8cd61ac0f8d02
         */

        private List<GoodsEntity> goods;

        public int getBrand_id() {
            return brand_id;
        }

        public void setBrand_id(int brand_id) {
            this.brand_id = brand_id;
        }

        public int getTag_id() {
            return tag_id;
        }

        public void setTag_id(int tag_id) {
            this.tag_id = tag_id;
        }

        public String getBrand_name() {
            return brand_name;
        }

        public void setBrand_name(String brand_name) {
            this.brand_name = brand_name;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int getFollow() {
            return follow;
        }

        public void setFollow(int follow) {
            this.follow = follow;
        }

        public List<GoodsEntity> getGoods() {
            return goods;
        }

        public void setGoods(List<GoodsEntity> goods) {
            this.goods = goods;
        }

        public static class GoodsEntity {
            private String cover;
            private String goods_id;
            private String recommend;  //推荐人

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public String getRecommend() {
                return recommend;
            }

            public void setRecommend(String recommend) {
                this.recommend = recommend;
            }
        }
    }
}
