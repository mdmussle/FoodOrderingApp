package com.FoF.foodordering.MVP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrdersResponse {
    private String success;

    private String userid;
    private String useremail;
    private String tax;
    private String shipping;
    private List<Ordere> orderdata = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public List<Ordere> getOrderdata() {
        return orderdata;
    }

    public void setOrderdata(List<Ordere> orderdata) {
        this.orderdata = orderdata;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
