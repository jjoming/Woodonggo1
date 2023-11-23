package com.example.woodonggo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface KakaoMapService {
    @GET("v2/local/geo/coord2regioncode.json")
    Call<KakaoMapResponse> getAddress(
            @Header("Authorization") String key, // 카카오 API 인증키 [필수]
            @Query("x") double longitude, // 경도 [선택, 기본값: 0]
            @Query("y") double latitude // 위도 [선택, 기본값: 0]
    );
}