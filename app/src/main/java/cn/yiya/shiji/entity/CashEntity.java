package cn.yiya.shiji.entity;

import java.util.List;

/**
 * Created by Tom on 2016/10/12.
 */
public class CashEntity {

    /**
     * month : 10
     * cash_info : [{"icon":"http://xxx","desc":"xxx","time":"2016-..","amount":223,"order_num":"XXX"}]
     */

    private List<ListEntity> list;
    private String next_offset;

    public String getNext_offset() {
        return next_offset;
    }

    public void setNext_offset(String next_offset) {
        this.next_offset = next_offset;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        private int month;
        /**
         * icon : http://xxx
         * desc : xxx
         * time : 2016-..
         * amount : 223
         * order_num : XXX
         */

        private List<CashInfoEntity> cash_info;

        public void setMonth(int month) {
            this.month = month;
        }

        public void setCash_info(List<CashInfoEntity> cash_info) {
            this.cash_info = cash_info;
        }

        public int getMonth() {
            return month;
        }

        public List<CashInfoEntity> getCash_info() {
            return cash_info;
        }

        public static class CashInfoEntity {
            private String icon;
            private String desc;
            private String desc_ext;
            private String status;
            private String time;
            private float amount;
            private String order_num;
            private int add_sub;     //   1为正 2为负

            public int getAdd_sub() {
                return add_sub;
            }

            public void setAdd_sub(int add_sub) {
                this.add_sub = add_sub;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getDesc_ext() {
                return desc_ext;
            }

            public void setDesc_ext(String desc_ext) {
                this.desc_ext = desc_ext;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public void setAmount(float amount) {
                this.amount = amount;
            }

            public void setOrder_num(String order_num) {
                this.order_num = order_num;
            }

            public String getIcon() {
                return icon;
            }

            public String getDesc() {
                return desc;
            }

            public String getTime() {
                return time;
            }

            public float getAmount() {
                return amount;
            }

            public String getOrder_num() {
                return order_num;
            }
        }
    }
}
