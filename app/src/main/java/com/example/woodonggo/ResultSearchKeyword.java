package com.example.woodonggo;

import java.util.List;

public class ResultSearchKeyword {
    public PlaceMeta meta;
    public List<Place> documents;

    public String id;
    public String place_name;
    public String category_name;
    public String category_group_code;
    public String category_group_name;
    public String phone;
    public String address_name;
    public String road_address_name;
    public String x;
    public String y;
    public String place_url;
    public String distance;

    public String getId() {
        return id;
    }

    public String getPlace_name() {
        return place_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getCategory_group_code() {
        return category_group_code;
    }

    public String getCategory_group_name() {
        return category_group_name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress_name() {
        return address_name;
    }

    public String getRoad_address_name() {
        return road_address_name;
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public String getPlace_url() {
        return place_url;
    }

    public String getDistance() {
        return distance;
    }

    public static class PlaceMeta {
        public int total_count;
        public int pageable_count;
        public boolean is_end;
        public RegionInfo same_name;
    }

    public static class RegionInfo {
        public List<String> region;
        public String keyword;
        public String selected_region;
    }

    public static class Place {
        public String id;
        public String place_name;
        public String category_name;
        public String category_group_code;
        public String category_group_name;
        public String phone;
        public String address_name;
        public String road_address_name;
        public String x;
        public String y;
        public String place_url;
        public String distance;

        public String getId() {
            return id;
        }

        public String getPlace_name() {
            return place_name;
        }

        public String getCategory_name() {
            return category_name;
        }

        public String getCategory_group_code() {
            return category_group_code;
        }

        public String getCategory_group_name() {
            return category_group_name;
        }

        public String getPhone() {
            return phone;
        }

        public String getAddress_name() {
            return address_name;
        }

        public String getRoad_address_name() {
            return road_address_name;
        }

        public String getX() {
            return x;
        }

        public String getY() {
            return y;
        }

        public String getPlace_url() {
            return place_url;
        }

        public String getDistance() {
            return distance;
        }
    }
}
