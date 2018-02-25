package cn.yiya.shiji.entity;

/**
 * Created by chenjian on 2015/11/27.
 * 手机设备信息
 */
public class DeviceInfo {

    String channel;            // 渠道号
    int code;                  // 版本号
    String phone;              // 手机型号
    String network;            // 网络类型
    String os;                 // 操作系统--这里Android
    String version;            // 当前版本号
    String idfa;               // 当前设备唯一信息

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIdfa() {
        return idfa;
    }

    public void setIdfa(String idfa) {
        this.idfa = idfa;
    }
}
