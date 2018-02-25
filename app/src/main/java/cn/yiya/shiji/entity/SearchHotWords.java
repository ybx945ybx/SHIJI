package cn.yiya.shiji.entity;

/**
 * Created by jerry on 2015/12/25.
 */
public class SearchHotWords {
    String[] words;
    String stamp;
    String[] data;

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public String[] getWords() {
        return words;
    }

    public void setWords(String[] words) {
        this.words = words;
    }
}
