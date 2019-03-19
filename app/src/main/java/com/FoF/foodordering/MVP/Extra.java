package com.FoF.foodordering.MVP;

import java.util.HashMap;
import java.util.Map;

public class Extra {

    public String getExtraid() {
        return extraid;
    }

    public void setExtraid(String extraid) {
        this.extraid = extraid;
    }

    private String extraid;
    private String extraname;
    private String extraprice;

    public String getExtraquantity() {
        return extraquantity;
    }

    public void setExtraquantity(String extraquantity) {
        this.extraquantity = extraquantity;
    }

    private String extraquantity;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getExtraname() {
        return extraname;
    }

    public void setExtraname(String extraname) {
        this.extraname = extraname;
    }

    public String getExtraprice() {
        return extraprice;
    }

    public void setExtraprice(String extraprice) {
        this.extraprice = extraprice;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}