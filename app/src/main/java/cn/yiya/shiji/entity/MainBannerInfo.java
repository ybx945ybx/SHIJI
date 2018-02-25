package cn.yiya.shiji.entity;

import java.util.List;

/**
 * Created by chenjian on 2016/5/31.
 */
public class MainBannerInfo {


    /**
     * id : 1
     * cover : http://7xjc5h.com2.z0.glb.qiniucdn.com/_1_1442036518
     * type : 1
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
        private String cover;
        private int type;
        private String url;
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public ShareEntity getShare() {
            return share;
        }

        public void setShare(ShareEntity share) {
            this.share = share;
        }

        private ShareEntity share;

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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
