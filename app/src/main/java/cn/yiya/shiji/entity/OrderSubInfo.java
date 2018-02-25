package cn.yiya.shiji.entity;

import java.util.ArrayList;

/**
 * Created by chenjian on 2016/3/3.
 */
public class OrderSubInfo {

    /**
     * sub_order_number : XXXXXXXXXXXXXXXXXXX_1
     * foreign_fee : 10
     * channel_fee : 20
     * service_fee : 5
     * tariff_fee : 10
     * refund_fee : 0
     * status : 0
     * delivery : 1
     */

    private String sub_order_number;
    private int foreign_fee;
    private int channel_fee;
    private int service_fee;
    private int tariff_fee;
    private int refund_fee;
    private int status;
    private int delivery;
    private OrderSubGroupInfo group_info;
    private OrderSubFeeInfo fee_info;
    private CountryInfo country;
    private SiteInfo site;
    private LogisticInfo logistic;
    private ArrayList<OrderGoodesInfo> goodses;

    public void setSub_order_number(String sub_order_number) {
        this.sub_order_number = sub_order_number;
    }

    public void setForeign_fee(int foreign_fee) {
        this.foreign_fee = foreign_fee;
    }

    public void setChannel_fee(int channel_fee) {
        this.channel_fee = channel_fee;
    }

    public void setService_fee(int service_fee) {
        this.service_fee = service_fee;
    }

    public void setTariff_fee(int tariff_fee) {
        this.tariff_fee = tariff_fee;
    }

    public void setRefund_fee(int refund_fee) {
        this.refund_fee = refund_fee;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }

    public String getSub_order_number() {
        return sub_order_number;
    }

    public int getForeign_fee() {
        return foreign_fee;
    }

    public int getChannel_fee() {
        return channel_fee;
    }

    public int getService_fee() {
        return service_fee;
    }

    public int getTariff_fee() {
        return tariff_fee;
    }

    public int getRefund_fee() {
        return refund_fee;
    }

    public int getStatus() {
        return status;
    }

    public int getDelivery() {
        return delivery;
    }

    public OrderSubGroupInfo getGroup_info() {
        return group_info;
    }

    public void setGroup_info(OrderSubGroupInfo group_info) {
        this.group_info = group_info;
    }

    public OrderSubFeeInfo getFee_info() {
        return fee_info;
    }

    public void setFee_info(OrderSubFeeInfo fee_info) {
        this.fee_info = fee_info;
    }

    public CountryInfo getCountry() {
        return country;
    }

    public void setCountry(CountryInfo country) {
        this.country = country;
    }

    public SiteInfo getSite() {
        return site;
    }

    public void setSite(SiteInfo site) {
        this.site = site;
    }

    public LogisticInfo getLogistic() {
        return logistic;
    }

    public void setLogistic(LogisticInfo logistic) {
        this.logistic = logistic;
    }

    public ArrayList<OrderGoodesInfo> getGoodses() {
        return goodses;
    }

    public void setGoodses(ArrayList<OrderGoodesInfo> goodses) {
        this.goodses = goodses;
    }

    public class OrderSubGroupInfo {

        /**
         * groupDes : 等待支付
         * group : 1
         * comment : 订单已提交,请在%M分%S秒内完成支付,超时系统将自动取消订单
         */

        private String groupDes;
        private int group;
        private String comment;

        public void setGroupDes(String groupDes) {
            this.groupDes = groupDes;
        }

        public void setGroup(int group) {
            this.group = group;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getGroupDes() {
            return groupDes;
        }

        public int getGroup() {
            return group;
        }

        public String getComment() {
            return comment;
        }
    }

    public class OrderSubFeeInfo {

        /**
         * fee_des : 关税全额补贴
         * tax_fee : 0
         */

        private String fee_des;
        private int tax_fee;

        public void setFee_des(String fee_des) {
            this.fee_des = fee_des;
        }

        public void setTax_fee(int tax_fee) {
            this.tax_fee = tax_fee;
        }

        public String getFee_des() {
            return fee_des;
        }

        public int getTax_fee() {
            return tax_fee;
        }
    }

    public class CountryInfo{
        /**
         * id : 1
         * name : 中国
         * flag : www.baidu.com
         */

        private int id;
        private String name;
        private String flag;

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getFlag() {
            return flag;
        }
    }

    public class SiteInfo{

        /**
         * id : 1
         * name : site
         * des : 美国XXX网站供货
         */

        private int id;
        private String name;
        private String des;

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDes() {
            return des;
        }
    }

    public class LogisticInfo {

        private int num;
        private ArrayList<LogisticPointInfo> list;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public ArrayList<LogisticPointInfo> getList() {
            return list;
        }

        public void setList(ArrayList<LogisticPointInfo> list) {
            this.list = list;
        }

        public class LogisticPointInfo {

            /**
             * stage : 1
             * logo :
             * desc : 您的订单已经成功下单
             * time : 2014-05-01 10:10:10
             */

            private int stage;
            private String logo;
            private String desc;
            private String time;

            public void setStage(int stage) {
                this.stage = stage;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getStage() {
                return stage;
            }

            public String getLogo() {
                return logo;
            }

            public String getDesc() {
                return desc;
            }

            public String getTime() {
                return time;
            }
        }
    }

    public class OrderGoodesInfo {

        /**
         * num : 4
         * price : 100
         * goodsId : XXXXXXXXXXXXX
         * title : 衣服
         * status : 1 //1:正常，2:退货
         * cover : www.baidu.com
         */

        private int num;
        private int price;
        private String goodsId;
        private String title;
        private int status;
        private String cover;
        private ArrayList<OrderSkuInfo> sku;

        public void setNum(int num) {
            this.num = num;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int getNum() {
            return num;
        }

        public int getPrice() {
            return price;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public String getTitle() {
            return title;
        }

        public int getStatus() {
            return status;
        }

        public String getCover() {
            return cover;
        }

        public ArrayList<OrderSkuInfo> getSku() {
            return sku;
        }

        public void setSku(ArrayList<OrderSkuInfo> sku) {
            this.sku = sku;
        }

        public class OrderSkuInfo {

            /**
             * key : size
             * value : SIZE ONE
             */

            private String key;
            private String value;

            public void setKey(String key) {
                this.key = key;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getKey() {
                return key;
            }

            public String getValue() {
                return value;
            }
        }
    }
}
