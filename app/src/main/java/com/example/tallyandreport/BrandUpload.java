package com.example.tallyandreport;

public class BrandUpload {
    String brandName;

    public BrandUpload(){

    }

    public BrandUpload(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String toString(){
        return this.brandName;
    }
}
