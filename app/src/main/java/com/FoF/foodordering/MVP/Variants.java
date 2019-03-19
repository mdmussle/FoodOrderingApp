package com.FoF.foodordering.MVP;

/**
 * Created by AbhiAndroid
 */

public class Variants {
    public String getVariantname() {
        return variantname;
    }

    public void setVariantname(String variantname) {
        this.variantname = variantname;
    }

    public String getVarprice() {
        return varprice;
    }

    public void setVarprice(String varprice) {
        this.varprice = varprice;
    }

    public String getVarientid() {
        return varientid;
    }

    public void setVarientid(String varientid) {
        this.varientid = varientid;
    }

    private String varientid;
    private String variantname;
    private String varprice;

    public String getVarquantity() {
        return varquantity;
    }

    public void setVarquantity(String varquantity) {
        this.varquantity = varquantity;
    }

    private String varquantity;
}
