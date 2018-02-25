package cn.yiya.shiji.entity;

import java.util.ArrayList;

/**
 * Created by yiya on 2015/9/17.
 */
public class ShoppingCartGoods {

    String cartId;
    String goodsId;
    String skuId;
    int num;
    ArrayList<ShoppingCartSku> sku;
    String cover;
    String title;
    int status;
    ArrayList<String> dimensions;
    float weight;
    String price;
    String brand;
    boolean isChecked;
    boolean isEdit;
    String list_price;
    String recommend;   //推荐人
    String source;
    String sourceId;
    String product_type_id;
    String category_id;
    String product_type;
    String category;
    String currency;
    String original_price;
    String share_profit;


    public String getList_price() {
        return list_price;
    }

    public void setList_price(String list_price) {
        this.list_price = list_price;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public ArrayList<ShoppingCartSku> getSku() {
        return sku;
    }

    public void setSku(ArrayList<ShoppingCartSku> sku) {
        this.sku = sku;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void toggle() {
        this.isChecked = !this.isChecked;
    }

    public boolean getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean bCheck) {
        this.isChecked = bCheck;
    }

    public ArrayList<String> getDimensions() {
        return dimensions;
    }

    public void setDimensions(ArrayList<String> dimensions) {
        this.dimensions = dimensions;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getProduct_type_id() {
        return product_type_id;
    }

    public void setProduct_type_id(String product_type_id) {
        this.product_type_id = product_type_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }

    public String getShare_profit() {
        return share_profit;
    }

    public void setShare_profit(String share_profit) {
        this.share_profit = share_profit;
    }
}
