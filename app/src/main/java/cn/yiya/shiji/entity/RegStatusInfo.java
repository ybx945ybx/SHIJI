package cn.yiya.shiji.entity;

/**
 * Created by chenjian on 2016/2/17.
 */
public class RegStatusInfo {
    int reg_status;
    boolean is_complete;
    int new_user;

    public int getNew_user() {
        return new_user;
    }

    public void setNew_user(int new_user) {
        this.new_user = new_user;
    }

    public boolean is_complete() {
        return is_complete;
    }

    public void setIs_complete(boolean is_complete) {
        this.is_complete = is_complete;
    }

    public int getReg_status() {
        return reg_status;
    }

    public void setReg_status(int reg_status) {
        this.reg_status = reg_status;
    }
}
