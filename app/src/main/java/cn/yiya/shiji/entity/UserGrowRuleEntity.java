package cn.yiya.shiji.entity;

import java.util.List;

/**
 * Created by Tom on 2017/1/11.
 */

public class UserGrowRuleEntity {
    /**
     * first_prize : 2000
     * second_prize : 1000
     * third_prize : 500
     * settle_date : 2017-02-01
     */

    private RankingEntity ranking;
    /**
     * action : 1
     * grow : 6
     * num : 1
     */

    private List<TaskEntity> task;
    /**
     * task : [{"action":1,"grow":6,"num":1},{"action":2,"grow":1},{"action":3,"grow":2},{"action":4,"grow":6,"num":0},{"action":5,"grow":6,"num":0},{"action":6,"grow":3},{"action":7,"grow":100},{"action":8,"grow":2}]
     * ranking : {"first_prize":"2000","second_prize":"1000","third_prize":"500","settle_date":"2017-02-01"}
     * level : [1000,3000,8000,16000,1215752192]
     * power_desc : [[["粉丝购买获得返佣","红人推荐的商品卖出后相应得到酬劳"],["享受红人成长值计划","每个动作都有成长值记录，提升在柿集的等级"]],[["粉丝购买获得返佣","红人推荐的商品卖出后相应得到酬劳"],["享受红人成长值计划","每个动作都有成长值记录，提升在柿集的等级"],["享受红人置装津贴","置装津贴按月用现金券的形式发放","满500元减50元优惠券"]],[["粉丝购买获得返佣","红人推荐的商品卖出后相应得到酬劳"],["享受红人成长值计划","每个动作都有成长值记录，提升在柿集的等级"],["享受红人置装津贴","置装津贴按月用现金券的形式发放","满1000元减100元优惠券"]],[["粉丝购买获得返佣","红人推荐的商品卖出后相应得到酬劳"],["享受红人成长值计划","每个动作都有成长值记录，提升在柿集的等级"],["享受红人置装津贴","置装津贴按月用现金券的形式发放","满1500元减150元优惠券"],["获得体验商品权益","有机会免费获得体验商品、免费试用"]],[["粉丝购买获得返佣","红人推荐的商品卖出后相应得到酬劳"],["享受红人成长值计划","每个动作都有成长值记录，提升在柿集的等级"],["享受红人置装津贴","置装津贴按月用现金券的形式发放","满2000元减200元优惠券"],["获得体验商品权益","有机会免费获得体验商品、免费试用"],["派发指定福利","定期答谢自己粉丝，让他们更拥戴你"]],[]]
     */

    private List<Integer> level;
    private List<List<List<String>>> power_desc;

    public void setRanking(RankingEntity ranking) {
        this.ranking = ranking;
    }

    public void setTask(List<TaskEntity> task) {
        this.task = task;
    }

    public void setLevel(List<Integer> level) {
        this.level = level;
    }

    public void setPower_desc(List<List<List<String>>> power_desc) {
        this.power_desc = power_desc;
    }

    public RankingEntity getRanking() {
        return ranking;
    }

    public List<TaskEntity> getTask() {
        return task;
    }

    public List<Integer> getLevel() {
        return level;
    }

    public List<List<List<String>>> getPower_desc() {
        return power_desc;
    }

    public static class RankingEntity {
        private String first_prize;
        private String second_prize;
        private String third_prize;
        private String settle_date;

        public void setFirst_prize(String first_prize) {
            this.first_prize = first_prize;
        }

        public void setSecond_prize(String second_prize) {
            this.second_prize = second_prize;
        }

        public void setThird_prize(String third_prize) {
            this.third_prize = third_prize;
        }

        public void setSettle_date(String settle_date) {
            this.settle_date = settle_date;
        }

        public String getFirst_prize() {
            return first_prize;
        }

        public String getSecond_prize() {
            return second_prize;
        }

        public String getThird_prize() {
            return third_prize;
        }

        public String getSettle_date() {
            return settle_date;
        }
    }

    public static class TaskEntity {
        private int action;
        private int grow;
        private int num;

        public void setAction(int action) {
            this.action = action;
        }

        public void setGrow(int grow) {
            this.grow = grow;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getAction() {
            return action;
        }

        public int getGrow() {
            return grow;
        }

        public int getNum() {
            return num;
        }
    }

//
//    /**
//     * first_prize : 2000积分...
//     * second_prize : 2000积分...
//     * third_prize : 2000积分...
//     * settle_date : 2017-01-04
//     */
//
//    private RankingEntity ranking;
//    /**
//     * action : 1
//     * grow : 3
//     * num : 10
//     */
//
//    private List<TaskEntity> task;
//    /**
//     * task : [{"action":1,"grow":3,"num":10}]
//     * ranking : {"first_prize":"2000积分...","second_prize":"2000积分...","third_prize":"2000积分...","settle_date":"2017-01-04"}
//     * level : [400,800,9000]
//     */
//
//    private List<Integer> level;
//
//    public void setRanking(RankingEntity ranking) {
//        this.ranking = ranking;
//    }
//
//    public void setTask(List<TaskEntity> task) {
//        this.task = task;
//    }
//
//    public void setLevel(List<Integer> level) {
//        this.level = level;
//    }
//
//    public RankingEntity getRanking() {
//        return ranking;
//    }
//
//    public List<TaskEntity> getTask() {
//        return task;
//    }
//
//    public List<Integer> getLevel() {
//        return level;
//    }
//
//    public static class RankingEntity {
//        private String first_prize;
//        private String second_prize;
//        private String third_prize;
//        private String settle_date;
//
//        public void setFirst_prize(String first_prize) {
//            this.first_prize = first_prize;
//        }
//
//        public void setSecond_prize(String second_prize) {
//            this.second_prize = second_prize;
//        }
//
//        public void setThird_prize(String third_prize) {
//            this.third_prize = third_prize;
//        }
//
//        public void setSettle_date(String settle_date) {
//            this.settle_date = settle_date;
//        }
//
//        public String getFirst_prize() {
//            return first_prize;
//        }
//
//        public String getSecond_prize() {
//            return second_prize;
//        }
//
//        public String getThird_prize() {
//            return third_prize;
//        }
//
//        public String getSettle_date() {
//            return settle_date;
//        }
//    }
//
//    public static class TaskEntity {
//        private int action;
//        private int grow;
//        private int num;
//
//        public void setAction(int action) {
//            this.action = action;
//        }
//
//        public void setGrow(int grow) {
//            this.grow = grow;
//        }
//
//        public void setNum(int num) {
//            this.num = num;
//        }
//
//        public int getAction() {
//            return action;
//        }
//
//        public int getGrow() {
//            return grow;
//        }
//
//        public int getNum() {
//            return num;
//        }
//    }


}
