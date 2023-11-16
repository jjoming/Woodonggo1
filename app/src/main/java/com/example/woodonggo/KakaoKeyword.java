package com.example.woodonggo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface KakaoKeyword {
    @GET("v2/local/search/keyword.json")
    Call<ResultSearchKeyword> getSearchKeyword(
            @Header("Authorization") String key, // 카카오 API 인증키 [필수]
            @Query("query") String query, // 검색을 원하는 질의어 [필수]
            @Query("x") double longitude, // 경도 [선택, 기본값: 0]
            @Query("y") double latitude, // 위도 [선택, 기본값: 0]
            @Query("radius") int radius // 반경 [선택, 기본값: 1000]
    );
}