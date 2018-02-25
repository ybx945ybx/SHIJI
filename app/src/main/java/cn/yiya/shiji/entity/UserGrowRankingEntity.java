package cn.yiya.shiji.entity;

import java.util.List;

/**
 * Created by Tom on 2017/1/11.
 */

public class UserGrowRankingEntity {

    /**
     * user_id : 1
     * grow_month : 30
     * user_name : SB
     * user_head : http://this_is_sb
     */

    private List<RankingEntity> ranking;

    public void setRanking(List<RankingEntity> ranking) {
        this.ranking = ranking;
    }

    public List<RankingEntity> getRanking() {
        return ranking;
    }

    public static class RankingEntity {
        private int user_id;
        private int grow_month;
        private String user_name;
        private String user_head;

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public void setGrow_month(int grow_month) {
            this.grow_month = grow_month;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public void setUser_head(String user_head) {
            this.user_head = user_head;
        }

        public int getUser_id() {
            return user_id;
        }

        public int getGrow_month() {
            return grow_month;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getUser_head() {
            return user_head;
        }
    }
}
