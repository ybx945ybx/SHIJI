package cn.yiya.shiji.entity;

import java.util.List;

/**
 * Created by Tom on 2016/10/20.
 */
public class ShopTypeEntity {

    /**
     * id : 1
     * name : 时尚潮流
     * cover : http://information.cdnqiniu02.qnmami.com/36_1_1476676294.png
     */

    private List<ListEntity> list;

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        private int id;
        private String name;
        private String cover;
        private String sku_id;
        private String desc_title;
        private String desc;
        private String price;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getDesc_title() {
            return desc_title;
        }

        public void setDesc_title(String desc_title) {
            this.desc_title = desc_title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getSkuId() {
            return sku_id;
        }

        public void setSkuId(String skuId) {
            this.sku_id = skuId;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCover() {
            return cover;
        }
    }
}
