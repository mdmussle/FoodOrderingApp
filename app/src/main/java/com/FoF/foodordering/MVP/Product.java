
package com.FoF.foodordering.MVP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {

    private String success;
    private String productId;
    private String iteam_id;
    private String plimit;
    private String orderstatus;
    private String productName;
    private String mrp;
    private String sellprice;
    private List<Variants> variants = null;
    private List<Extra> extra = null;
    private String status;
    private String primaryimage;
    private String currency;
    private String quantity;
    private String stock;
    private String description;
    private List<String> images = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public List<Variants> getVariants() {
        return variants;
    }

    public void setVariants(List<Variants> variants) {
        this.variants = variants;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getPlimit() {
        return plimit;
    }

    public void setPlimit(String plimit) {
        this.plimit = plimit;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getIteam_id() {
        return iteam_id;
    }

    public void setItemId(String iteam_id) {
        this.iteam_id = iteam_id;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrimaryImage() {
        return primaryimage;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getMrpprice() {
        return mrp;
    }

    public void setMrpprice(String mrp) {
        this.mrp = mrp;
    }

    public String getSellprice() {
        return sellprice;
    }

    public void setSellprice(String sellprice) {
        this.sellprice = sellprice;
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<Extra> getExtra() {
        return extra;
    }

    public void setExtra(List<Extra> extra) {
        this.extra = extra;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status)

    {
        this.status = status;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}