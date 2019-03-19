package com.FoF.foodordering.MVP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartistResponse {


    private String success;
    private String cartid;
    private String userid;
    private String useremail;
    private String tax;
    private String delivery;
    private List<CartProducts> products = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getCartid() {
        return cartid;
    }

    public void setCartid(String cartid) {
        this.cartid = cartid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
    public String getShipping() {
        return delivery;
    }

    public void setShipping(String delivery) {
        this.delivery = delivery;
    }

    public List<CartProducts> getProducts() {
        return products;
    }

    public void setProducts(List<CartProducts> products) {
        this.products = products;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}