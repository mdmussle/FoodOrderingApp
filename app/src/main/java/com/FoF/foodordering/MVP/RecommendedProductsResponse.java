package com.FoF.foodordering.MVP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AbhiAndroid
 */

public class RecommendedProductsResponse {
    private String success;
    private List<Product> rcomitem = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<Product> getProductList() {
        return rcomitem;
    }

    public void setProductList(List<Product> rcomitem) {
        this.rcomitem = rcomitem;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
