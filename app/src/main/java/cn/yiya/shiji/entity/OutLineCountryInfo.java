package cn.yiya.shiji.entity;

import java.util.ArrayList;

/**
 * Created by chenjian on 2016/3/30.
 */
public class OutLineCountryInfo {

    private String country_id;
    private String country_name;
    private String country_cn_name;

    private ArrayList<CityListEntity> city_list;

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_cn_name() {
        return country_cn_name;
    }

    public void setCountry_cn_name(String country_cn_name) {
        this.country_cn_name = country_cn_name;
    }

    public ArrayList<CityListEntity> getCity_list() {
        return city_list;
    }

    public void setCity_list(ArrayList<CityListEntity> city_list) {
        this.city_list = city_list;
    }

    public static class CityListEntity {
        private String id;
        private String name;
        private String cn_name;
        private String version;
        private String download_url;
        private String md5;
        private String real_size;
        private String size;
        private int status;

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

        public String getCn_name() {
            return cn_name;
        }

        public void setCn_name(String cn_name) {
            this.cn_name = cn_name;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getDownload_url() {
            return download_url;
        }

        public void setDownload_url(String download_url) {
            this.download_url = download_url;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getReal_size() {
            return real_size;
        }

        public void setReal_size(String real_size) {
            this.real_size = real_size;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }
    }
}
