package com.example.woodonggo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentPlace extends Fragment {

    private MapView mapView;
    private RadioGroup radioGroup;
    private RadioButton btnGolf, btnBowling, btnPingpong;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    RecyclerView recyclerView;
    Place_RecyclerView_Adapter adapter;

    public static final String BASE_URL = "https://dapi.kakao.com/";
    public static final String API_KEY = "10079a2678836fa1cd9862adca8211cc"; // REST API 키



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place, container, false);

        radioGroup = view.findViewById(R.id.radioGroup);
        btnGolf = view.findViewById(R.id.btnGolf);
        btnBowling = view.findViewById(R.id.btnBowling);
        btnPingpong = view.findViewById(R.id.btnPingpong);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btnGolf) {
                    btnGolf.setTextColor(Color.WHITE);
                    btnBowling.setTextColor(Color.DKGRAY);
                    btnPingpong.setTextColor(Color.DKGRAY);
                    searchKeyword("골프");
                } else if (checkedId == R.id.btnBowling) {
                    btnBowling.setTextColor(Color.WHITE);
                    btnGolf.setTextColor(Color.DKGRAY);
                    btnPingpong.setTextColor(Color.DKGRAY);
                    searchKeyword("볼링");
                } else if (checkedId == R.id.btnPingpong) {
                    btnPingpong.setTextColor(Color.WHITE);
                    btnGolf.setTextColor(Color.DKGRAY);
                    btnBowling.setTextColor(Color.DKGRAY);
                    searchKeyword("탁구");
                }
            }
        });

        //데이터 모델리스트
        ArrayList<DataModel> dataModels = new ArrayList<>();

//        dataModels.add(new DataModel("data0", "data0", "data0"));
//        dataModels.add(new DataModel("data1", "data1", "data1"));
//        dataModels.add(new DataModel("data2", "data2", "data2"));
//        dataModels.add(new DataModel("data3", "data3", "data3"));
//        dataModels.add(new DataModel("data4", "data4", "data4"));
//        dataModels.add(new DataModel("data5", "data5", "data5"));
//        dataModels.add(new DataModel("data6", "data6", "data6"));

        recyclerView = view.findViewById(R.id.recyclerViewPlace);
        adapter = new Place_RecyclerView_Adapter(getActivity(), dataModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        mapView = view.findViewById(R.id.mapView);
        mapView.setDaumMapApiKey("6e57980f9050faf730dbb4af45ab8602");

        // 위치 권한 확인 및 요청
        if (shouldRequestLocationPermission()) {
            requestLocationPermission();
        } else {
            startTracking();
        }

        return view;
    }

    @SuppressLint("MissingPermission")
    private void startTracking() {
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

        LocationManager lm = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location userNowLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (userNowLocation != null) {
                double uLatitude = userNowLocation.getLatitude();
                double uLongitude = userNowLocation.getLongitude();
                MapPoint uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude, uLongitude);

                MapPOIItem marker = new MapPOIItem();
                marker.setItemName("현 위치");
                marker.setMapPoint(uNowPosition);
                marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
                marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                mapView.addPOIItem(marker);
            }
        }
    }

    private void stopTracking() {
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
    }

    private boolean shouldRequestLocationPermission() {
        // 위치 권한 확인 로직
        return ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        // 위치 권한 요청 로직
        requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startTracking();
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopTracking();
        mapView.destroyDrawingCache();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.notification){
            //TODO: 인텐트 완성
            //Intent intent = new Intent(getContext().getApplicationContext(), );
            //startActivity(intent);
        }
        return false;
    }

    // 키워드 검색 함수
    private void searchKeyword(String keyword) {
        Retrofit retrofit = new Retrofit.Builder() // Retrofit 구성
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String apiKey = "KakaoAK " + API_KEY;
        KakaoKeyword api = retrofit.create(KakaoKeyword.class); // 통신 인터페이스를 객체로 생성
        //Call<ResultSearchKeyword> call = api.getSearchKeyword(API_KEY, 127.06283102249932, 37.514322572335935, 20000); // 검색 조건 입력
        Call<ResultSearchKeyword> call = api.getSearchKeyword(apiKey, keyword,127.06283102249932, 37.514322572335935, 20000); // 검색 조건 입력

        // API 서버에 요청
        call.enqueue(new Callback<ResultSearchKeyword>() {
            @Override
            public void onResponse(Call<ResultSearchKeyword> call, Response<ResultSearchKeyword> response) {
                // 통신 성공 (검색 결과는 response.body()에 담겨있음)
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        // 성공적인 응답이 있을 때 처리
                        Log.d("Test", "Body: " + response.body());
                        Log.d("Test", "Raw: " + response.raw());
                        Log.d("Test", "API URL: " + call.request().url());
                        Log.d("Test", "Response Code: " + response.code());
                        Log.d("Test", "Error Response: " + response.errorBody().toString());

                        ResultSearchKeyword result = response.body();

                        List<DataModel> dataModels = convertToDataModelList(result);

                        adapter.setData(dataModels);
                        adapter.notifyDataSetChanged();

                    } else {
                        // 응답이 실패하거나 body가 null인 경우 처리
                        Log.w("Retrofit", "응답이 실패하거나 body가 null입니다.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            // ResultSearchKeyword를 DataModel로 변환하는 메서드
            private List<DataModel> convertToDataModelList(ResultSearchKeyword result) {
                List<DataModel> dataModels = new ArrayList<>();

                // result에서 필요한 정보를 추출하여 DataModel로 변환
                for (ResultSearchKeyword.Place place : result.documents) {
                    // 예시: 장소명, 카테고리, 주소를 추출하여 DataModel을 생성하고 리스트에 추가
                    DataModel dataModel = new DataModel(place.getPlace_name(), place.getCategory_name(), place.getAddress_name());
                    dataModels.add(dataModel);
                }
                return dataModels;
            }

            @Override
            public void onFailure(Call<ResultSearchKeyword> call, Throwable t) {
                // 통신 실패
                Log.w("Retrofit", "통신 실패: " + t.getMessage());
            }
        });
    }

}
