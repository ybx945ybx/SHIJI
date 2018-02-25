package cn.yiya.shiji.entity;

import java.util.List;

/**
 * Created by Tom on 2016/7/26.
 */
public class HotCategroyThridObject {

    /**
     * id : 1
     * name : 母婴
     */

    private List<ThridListEntity> list;

    public void setList(List<ThridListEntity> list) {
        this.list = list;
    }

    public List<ThridListEntity> getList() {
        return list;
    }

    public static class ThridListEntity {
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
