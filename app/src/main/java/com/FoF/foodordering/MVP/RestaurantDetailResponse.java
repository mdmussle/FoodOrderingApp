package com.FoF.foodordering.MVP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantDetailResponse {




private String bannerimage;
private String offerammount;
private String offerpercentage;

    public String getBannerimage() {
        return bannerimage;
    }

    public void setBannerimage(String bannerimage) {
        this.bannerimage = bannerimage;
    }

    public String getOfferammount() {
        return offerammount;
    }

    public void setOfferammount(String offerammount) {
        this.offerammount = offerammount;
    }

    public String getOfferpercentage() {
        return offerpercentage;
    }

    public void setOfferpercentage(String offerpercentage) {
        this.offerpercentage = offerpercentage;
    }

    public String getOfferterms() {
        return offerterms;
    }

    public void setOfferterms(String offerterms) {
        this.offerterms = offerterms;
    }

    private String offerterms;


private String name;
private String address;
private String description;
private String phone;
private String web;
private String lat;
private String lon;
private String time;
private String tax;
private String currency;
private String minorder;
private List<String> images = null;
private List<String> deliverycity = null;
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getAddress() {
return address;
}

public void setAddress(String address) {
this.address = address;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public String getPhone() {
return phone;
}

public void setPhone(String phone) {
this.phone = phone;
}

public String getWeb() {
return web;
}

public void setWeb(String web) {
this.web = web;
}

public String getLat() {
return lat;
}

public void setLat(String lat) {
this.lat = lat;
}

public String getLon() {
return lon;
}

public void setLon(String lon) {
this.lon = lon;
}

public String getTime() {
return time;
}

public void setTime(String time) {
this.time = time;
}

public String getTax() {
return tax;
}

public void setTax(String tax) {
this.tax = tax;
}

public String getCurrency() {
return currency;
}

public void setCurrency(String currency) {
this.currency = currency;
}

public String getMinorder() {
return minorder;
}

public void setMinorder(String minorder) {
this.minorder = minorder;
}

public List<String> getImages() {
return images;
}

public void setImages(List<String> images) {
this.images = images;
}

public List<String> getDeliverycity() {
return deliverycity;
}

public void setDeliverycity(List<String> deliverycity) {
this.deliverycity = deliverycity;
}

public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}