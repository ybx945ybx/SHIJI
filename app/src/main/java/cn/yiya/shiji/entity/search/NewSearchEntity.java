package cn.yiya.shiji.entity.search;

import java.util.List;

import cn.yiya.shiji.entity.ShareEntity;

/**
 * Created by Tom on 2016/8/22.
 */
public class NewSearchEntity {

    /**
     * id : 852
     * name : 10 Crosby Derek Lam
     * num : 0
     * logo : http://xx.jpg
     */

    private List<BrandListEntity> brand_list;
    /**
     * id : 9
     * name : 1111
     * num : 0
     */

    private List<SiteListEntity> site_list;
    /**
     * id : 27
     * title : 1111
     * cover : http://7xjc5h.com2.z0.glb.qiniucdn.com/shop_activity_3_1443492589
     * type : 1
     */

    private List<ShopActListEntity> shop_act_list;
    /**
     * id : 9
     * name : 1111
     */

    private List<GoodsTypeListEntity> goods_type_list;

    public void setBrand_list(List<BrandListEntity> brand_list) {
        this.brand_list = brand_list;
    }

    public void setSite_list(List<SiteListEntity> site_list) {
        this.site_list = site_list;
    }

    public void setShop_act_list(List<ShopActListEntity> shop_act_list) {
        this.shop_act_list = shop_act_list;
    }

    public void setGoods_type_list(List<GoodsTypeListEntity> goods_type_list) {
        this.goods_type_list = goods_type_list;
    }

    public List<BrandListEntity> getBrand_list() {
        return brand_list;
    }

    public List<SiteListEntity> getSite_list() {
        return site_list;
    }

    public List<ShopActListEntity> getShop_act_list() {
        return shop_act_list;
    }

    public List<GoodsTypeListEntity> getGoods_type_list() {
        return goods_type_list;
    }

    public static class BrandListEntity {
        private int id;
        private String name;
        private int num;
        private String logo;

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getNum() {
            return num;
        }

        public String getLogo() {
            return logo;
        }
    }

    public static class SiteListEntity {
        private int id;
        private String name;
        private int num;

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getNum() {
            return num;
        }
    }

    public static class ShopActListEntity {
        private int id;
        private String title;
        private String cover;
        private int type;
        private String url;
        private ShareEntity share;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public ShareEntity getShare() {
            return share;
        }

        public void setShare(ShareEntity share) {
            this.share = share;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getCover() {
            return cover;
        }

        public int getType() {
            return type;
        }
    }

    public static class GoodsTypeListEntity {
        private int id;
        private String name;

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
