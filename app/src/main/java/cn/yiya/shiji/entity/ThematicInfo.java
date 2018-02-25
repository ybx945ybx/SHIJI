package cn.yiya.shiji.entity;

import java.util.List;

/**
 * Created by chenjian on 2016/6/7.
 */
public class ThematicInfo {

    /**
     * id : 1
     * name : 专题名
     * icon : http://7xjc5h.com2.z0.glb.qiniucdn.com/_1_1442036518
     * cover : http://7xjc5h.com2.z0.glb.qiniucdn.com/_1_1442036518
     * wordless_cover : s
     */
    private String thematic_id; //专题编号
    private List<ListEntity> list;

    public String getThematic_id() {
        return thematic_id;
    }

    public void setThematic_id(String thematic_id) {
        this.thematic_id = thematic_id;
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
        private String icon;
        private String cover;
        private String wordless_cover;
        private boolean bExpand;

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

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getWordless_cover() {
            return wordless_cover;
        }

        public boolean isbExpand() {
            return bExpand;
        }

        public void setbExpand(boolean bExpand) {
            this.bExpand = bExpand;
        }

        public void setWordless_cover(String wordless_cover) {
            this.wordless_cover = wordless_cover;
        }
    }
}
