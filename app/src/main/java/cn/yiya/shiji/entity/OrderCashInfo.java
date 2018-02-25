package cn.yiya.shiji.entity;

import java.util.List;

/**	店铺单个订单佣金明细
 * Created by Amy on 2016/10/20.
 */

public class OrderCashInfo {

    /**
     * cash_amount : 200 //佣金总计
     * goods_list : [{"id":"1324jj23h11","num":2,"title":"nike","price":22,"goods_cash":100}]
     */

    private int cash_amount;
    /**
     * id : 1324jj23h11  //商品id
     * num : 2 //商品数量
     * title : nike //商品名
     * price : 22 //商品价格
     * goods_cash : 100 //商品佣金
     */

    private List<GoodsListEntity> goods_list;

    public int getCash_amount() {
        return cash_amount;
    }

    public void setCash_amount(int cash_amount) {
        this.cash_amount = cash_amount;
    }

    public List<GoodsListEntity> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<GoodsListEntity> goods_list) {
        this.goods_list = goods_list;
    }

    public static class GoodsListEntity {
        private String id;
        private int num;
        private String title;
        private int price;
        private int goods_cash;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getGoods_cash() {
            return goods_cash;
        }

        public void setGoods_cash(int goods_cash) {
            this.goods_cash = goods_cash;
        }
    }
}
