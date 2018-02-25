package cn.yiya.shiji.entity;

import java.util.List;

/**
 * 获取一二级热门分类
 * Created by Tom on 2016/7/22.
 */
public class HotCategoryFirstSecondObject {

    /**
     * icon : http://www.baidu.com
     * id : 1
     * name : 母婴
     * list : [{"icon":"http://www.baidu.com","id":1,"name":"母婴","category":"母婴"},{"icon":"http://www.baidu.com","id":2,"name":"母婴","category":"母婴"}]
     */

    private List<ListEntity> list;

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        private String icon;
        private int id;
        private String name;
        /**
         * icon : http://www.baidu.com
         * id : 1
         * name : 母婴
         * category : 母婴
         */

        private List<SecondListEntity> list;

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setList(List<SecondListEntity> list) {
            this.list = list;
        }

        public String getIcon() {
            return icon;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<SecondListEntity> getList() {
            return list;
        }

        public static class SecondListEntity {
            private String icon;
            private int id;
            private String name;
            private String category;

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getIcon() {
                return icon;
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getCategory() {
                return category;
            }
        }
    }
}
