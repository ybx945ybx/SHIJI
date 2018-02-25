package cn.yiya.shiji.entity;

import java.util.List;

/**
 * Created by weixuewu on 15/8/3.
 */
public class RememberUserItem {
    private int stage;
    private int user_id;
    private String expected_date;
    private String birth_date;
    private String name;
    private String head;
    private int liked_count;
    private int red;


    private List<Work> works;
    private List<Tag> tags;

    private boolean is_follow;

    public boolean is_follow() {
        return is_follow;
    }

    public void setIs_follow(boolean is_follow) {
        this.is_follow = is_follow;
    }

    public String getTagString(){
        if(tags==null||tags.equals(""))
            return "";
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<tags.size();i++){
            sb.append("#"+tags.get(i).getContent()+"  ");
        }
        return sb.toString();
    }
    public Tag finTag(String tag){
        if(tags==null||tags.equals("")||tag==null)
            return null;
        for (int i=0;i<tags.size();i++){
            if(tag.equals(tags.get(i).getContent().trim()))
             return tags.get(i);
        }
        return null;
    }
    public String[] getTagArray(){
        if(tags==null||tags.equals(""))
            return null;
        String[] arrs=new String[tags.size()];
        for (int i=0;i<tags.size();i++){
            arrs[i]="#"+tags.get(i).getContent().trim()+" ";
        }
        return arrs;
    }
    public class Work{
        private String image;
        private int work_id;
        private boolean isfollow;

        public boolean isfollow() {
            return isfollow;
        }

        public void setIsfollow(boolean isfollow) {
            this.isfollow = isfollow;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getWork_id() {
            return work_id;
        }

        public void setWork_id(int work_id) {
            this.work_id = work_id;
        }
    }
    public class Tag{
        private String content;
        private int tag_id;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getTag_id() {
            return tag_id;
        }

        public void setTag_id(int tag_id) {
            this.tag_id = tag_id;
        }
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getExpected_date() {
        return expected_date;
    }

    public void setExpected_date(String expected_date) {
        this.expected_date = expected_date;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getLiked_count() {
        return liked_count;
    }

    public void setLiked_count(int liked_count) {
        this.liked_count = liked_count;
    }

    public List<Work> getWorks() {
        return works;
    }

    public void setWorks(List<Work> works) {
        this.works = works;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
