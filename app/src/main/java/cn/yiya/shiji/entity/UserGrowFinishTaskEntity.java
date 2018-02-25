package cn.yiya.shiji.entity;

import java.util.List;

/**
 * Created by Tom on 2017/1/11.
 */

public class UserGrowFinishTaskEntity {

    /**
     * action : 1
     * finish_times : 3
     */

    private List<TaskEntity> task;

    public void setTask(List<TaskEntity> task) {
        this.task = task;
    }

    public List<TaskEntity> getTask() {
        return task;
    }

    public static class TaskEntity {
        private int action;
        private int finish_times;

        public void setAction(int action) {
            this.action = action;
        }

        public void setFinish_times(int finish_times) {
            this.finish_times = finish_times;
        }

        public int getAction() {
            return action;
        }

        public int getFinish_times() {
            return finish_times;
        }
    }
}
