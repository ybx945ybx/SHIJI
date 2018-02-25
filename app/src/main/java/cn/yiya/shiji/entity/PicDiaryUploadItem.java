package cn.yiya.shiji.entity;

import java.util.ArrayList;

import cn.yiya.shiji.views.TagAbleImageView;

/**
 * Created by dell on 2015-07-14.
 */
public class PicDiaryUploadItem {
    public String text;
    public String date;
    public String key;
    public int type;
    public ArrayList<ImageInfo> images;

    public static class ImageInfo{
        public String goods_id;
        public String url;
        public ArrayList<TagAbleImageView.TagInfo> tags;
        public String[] goods_ids;
    }

}
