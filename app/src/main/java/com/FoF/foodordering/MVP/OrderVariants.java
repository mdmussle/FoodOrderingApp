package com.FoF.foodordering.MVP;

import java.util.List;

/**
 * Created by AbhiAndroid
 */

public class OrderVariants {

    public String getVarientid() {
        return varientid;
    }

    public void setVarientid(String varientid) {
        this.varientid = varientid;
    }

    public String getVariantname() {
        return variantname;
    }

    public void setVariantname(String variantname) {
        this.variantname = variantname;
    }

    public String getVarquantity() {
        return varquantity;
    }

    public void setVarquantity(String varquantity) {
        this.varquantity = varquantity;
    }

    public String getVarprice() {
        return varprice;
    }

    public void setVarprice(String varprice) {
        this.varprice = varprice;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public List<Extra> getExtra() {
        return extra;
    }

    public void setExtra(List<Extra> extra) {
        this.extra = extra;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    private String varientid;
    private String variantname;
    private String varquantity;
    private String varprice;
    private String product_id;
    private String productname;
    private String image;
    private List<Extra> extra = null;

}
