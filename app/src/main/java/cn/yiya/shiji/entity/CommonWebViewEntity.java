package cn.yiya.shiji.entity;

import java.util.List;

/**
 * Created by Tom on 2016/10/20.
 */
public class CommonWebViewEntity {

    /**
     * name : 文章
     * url : http://h5.qnmami.com/app/article/html/article-list.html
     */

    private List<ListEntity> list;
    private String cover;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        private String name;
        private String url;
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }
    }
}
