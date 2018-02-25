package cn.yiya.shiji.entity;

import java.util.List;

/**
 * Created by chenjian on 2016/5/31.
 */
public class MainFlashSaleInfo {

    /**
     * now : 2016-01-08 15:46:05
     * list : [{"id":"1","name":"test","cover":"http://7xjc5h.com2.z0.glb.qiniucdn.com/_1_1442036518","foreimage":"http://7xjc5h.com2.z0.glb.qiniucdn.com/flash-sale-in_1_1452140171","discount":"(全场低至4折起)","notice":1,"begin_time":"2015-12-28 10:50:00","end_time":"2015-12-31 10:50:36"},{"id":"2","cover":"http://7xjc5h.com2.z0.glb.qiniucdn.com/_1_1442036518","notice":2,"begin_time":"2015-12-28 10:50:00","end_time":"2015-12-31 10:50:36"}]
     */

    private String now;
    /**
     * id : 1
     * name : test
     * cover : http://7xjc5h.com2.z0.glb.qiniucdn.com/_1_1442036518
     * foreimage : http://7xjc5h.com2.z0.glb.qiniucdn.com/flash-sale-in_1_1452140171
     * discount : (全场低至4折起)
     * notice : 1  //1表示已设置开枪提醒，2表示未设置开枪提醒；无该字段则不出现按钮
     * begin_time : 2015-12-28 10:50:00
     * end_time : 2015-12-31 10:50:36
     */

    private List<ListEntity> list;

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public static class ListEntity {
        private String id;
        private String name;
        private String cover;
        private String cover_origin;
        private String foreimage;
        private String discount;
        private int notice;
        private String begin_time;
        private String end_time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCover_origin() {
            return cover_origin;
        }

        public void setCover_origin(String cover_origin) {
            this.cover_origin = cover_origin;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getForeimage() {
            return foreimage;
        }

        public void setForeimage(String foreimage) {
            this.foreimage = foreimage;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public int getNotice() {
            return notice;
        }

        public void setNotice(int notice) {
            this.notice = notice;
        }

        public String getBegin_time() {
            return begin_time;
        }

        public void setBegin_time(String begin_time) {
            this.begin_time = begin_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }
    }
}
