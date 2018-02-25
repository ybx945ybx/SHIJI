package cn.yiya.shiji.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amy on 2016/6/20.
 */
public class HotCategoryObject {
    /**
     * name : 衬衫
     * id : 10
     * icon : http://information.cdnqiniu02.qnmami.com/hot_category_11_1444820397
     * category : 上衣
     * list : [{"id":8,"name":"衬衫"}]
     */

    public List<SecondItem> list;

    public List<SecondItem> getList() {
        return list;
    }

    public void setList(List<SecondItem> list) {
        this.list = list;
    }


    public static class SecondItem {
        private String name;
        private int id;
        private String icon;
        private String category;
        private String cover;
        /**
         * id : 8
         * name : 衬衫
         */

        private List<ThirdItem> list;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public List<ThirdItem> getList() {
            List<ThirdItem> thirdlist = new ArrayList<>();
            ThirdItem thirditem = new ThirdItem();
            thirditem.setName("全部");
            thirdlist.add(thirditem);
            thirdlist.addAll(list);
            for (int i = 0; i < thirdlist.size(); i++) {
                thirdlist.get(i).setSecondId(getId());
            }
            return thirdlist;
        }

        public void setList(List<ThirdItem> list) {
            this.list = list;
        }

        public static class ThirdItem {
            private int id;
            private String name;

            public int getSecondId() {
                return secondId;
            }

            public void setSecondId(int secondId) {
                this.secondId = secondId;
            }

            private int secondId;


            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
