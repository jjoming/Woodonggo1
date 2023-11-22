package com.example.woodonggo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KakaoMapResponse {
    @SerializedName("documents")
    private List<Address> documents;

    public List<Address> getDocuments() {
        return documents;
    }

    public class Address {
        @SerializedName("address_name")
        public String addressName;
        public String region1depthName;
        public String region2depthName;
        public String region3depthName;

        public String getAddressName() {
            return addressName;
        }
        public String getRegion1depthName() { return region1depthName; }
        public String getRegion2depthName() { return region2depthName; }
        public String getRegion3depthName() { return region3depthName; }
    }
}
