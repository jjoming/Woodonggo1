package com.example.woodonggo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class Fragment_place extends Fragment {

    private MapView mapView;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    RecyclerView recyclerView;
    Place_RecyclerView_Adapter adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place, container, false);

        //데이터 모델리스트
        ArrayList<DataModel> dataModels = new ArrayList<>();

        dataModels.add(new DataModel("data0", "data0", "data0"));
        dataModels.add(new DataModel("data1", "data1", "data1"));
        dataModels.add(new DataModel("data2", "data2", "data2"));
        dataModels.add(new DataModel("data3", "data3", "data3"));
        dataModels.add(new DataModel("data4", "data4", "data4"));
        dataModels.add(new DataModel("data5", "data5", "data5"));
        dataModels.add(new DataModel("data6", "data6", "data6"));

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

}
