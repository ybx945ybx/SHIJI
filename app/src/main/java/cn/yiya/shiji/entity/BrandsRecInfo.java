package cn.yiya.shiji.entity;

import java.util.List;

/**
 * Created by chenjian on 2016/6/13.
 */
public class BrandsRecInfo {

    /**
     * id : 123c21e3123f
     * brand_id : 12321
     * brand_name : lv
     * brand_icon : http://xxxx
     * bottom_left : xxxXXX...
     * bottom_right : xxxXXX...
     * goods_list : [{"id":"123213123","cover":"图片","title":"商品名称","brand":"","status":3,"price":"90","list_price":"100","site":"美国catters官网供货","cover_info":{"width":1600,"height":1526},"discount":8.76}]
     */

    private List<ListEntity> list;

    public List<ListEntity> getList() {
        return list;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public static class ListEntity {
        private String id;
        private int brand_id;
        private String brand_name;
        private String brand_icon;
        private String bottom_left;
        private String bottom_right;
        private String reason;
        private int tag_id;
        private int follow;

        /**
         * id : 123213123
         * cover : 图片
         * title : 商品名称
         * brand :
         * status : 3
         * price : 90
         * list_price : 100
         * site : 美国catters官网供货
         * cover_info : {"width":1600,"height":1526}
         * discount : 8.76
         */

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        private List<GoodsListEntity> goods_list;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getBrand_id() {
            return brand_id;
        }

        public void setBrand_id(int brand_id) {
            this.brand_id = brand_id;
        }

        public String getBrand_name() {
            return brand_name;
        }

        public void setBrand_name(String brand_name) {
            this.brand_name = brand_name;
        }

        public String getBrand_icon() {
            return brand_icon;
        }

        public void setBrand_icon(String brand_icon) {
            this.brand_icon = brand_icon;
        }

        public String getBottom_left() {
            return bottom_left;
        }

        public void setBottom_left(String bottom_left) {
            this.bottom_left = bottom_left;
        }

        public String getBottom_right() {
            return bottom_right;
        }

        public void setBottom_right(String bottom_right) {
            this.bottom_right = bottom_right;
        }

        public int getTag_id() {
            return tag_id;
        }

        public void setTag_id(int tag_id) {
            this.tag_id = tag_id;
        }

        public int getFollow() {
            return follow;
        }

        public void setFollow(int follow) {
            this.follow = follow;
        }

        public List<GoodsListEntity> getGoods_list() {
            return goods_list;
        }

        public void setGoods_list(List<GoodsListEntity> goods_list) {
            this.goods_list = goods_list;
        }

        public static class GoodsListEntity {
            private String id;
            private String cover;
            private String title;
            private String brand;
            private int status;
            private String price;
            private String list_price;
            private String site;
            private String recommend; //推荐人
            /**
             * width : 1600
             * height : 1526
             */

            private CoverInfoEntity cover_info;
            private double discount;

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

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getBrand() {
                return brand;
            }

            public void setBrand(String brand) {
                this.brand = brand;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getList_price() {
                return list_price;
            }

            public void setList_price(String list_price) {
                this.list_price = list_price;
            }

            public String getSite() {
                return site;
            }

            public void setSite(String site) {
                this.site = site;
            }

            public CoverInfoEntity getCover_info() {
                return cover_info;
            }

            public void setCover_info(CoverInfoEntity cover_info) {
                this.cover_info = cover_info;
            }

            public double getDiscount() {
                return discount;
            }

            public void setDiscount(double discount) {
                this.discount = discount;
            }

            public static class CoverInfoEntity {
                private int width;
                private int height;

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }
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
