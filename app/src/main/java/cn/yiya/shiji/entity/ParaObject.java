package cn.yiya.shiji.entity;

/**
 * Created by jerry on 2015/11/10.
 */
public class ParaObject {
    int id;
    int limit;
    int offset;
    String brand_ids;
    String price;
    String category_ids;
    String sort_id;
    String price_ranges_id;
    String genders;
    int count;
    String word;
    String size;
    String color;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getGenders() {
        return genders;
    }

    public void setGenders(String genders) {
        this.genders = genders;
    }

    public int getId() {
        return id;
    }

    public void  setId(int id) {
        this.id = id;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getBrand_ids() {
        return brand_ids;
    }

    public void setBrand_ids(String brand_ids) {
        this.brand_ids = brand_ids;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory_ids() {
        return category_ids;
    }

    public void setCategory_ids(String category_ids) {
        this.category_ids = category_ids;
    }

    public String getSort_id() {
        return sort_id;
    }

    public void setSort_id(String sort_id) {
        this.sort_id = sort_id;
    }

    public String getPrice_ranges_id() {
        return price_ranges_id;
    }

    public void setPrice_ranges_id(String price_ranges_id) {
        this.price_ranges_id = price_ranges_id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
