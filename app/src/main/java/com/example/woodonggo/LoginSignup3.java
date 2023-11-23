package com.example.woodonggo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginSignup3 extends AppCompatActivity {

    ImageView close;
    Button townButton1, townButton2, townCheckBtn, townCorrectBtn;
    private MapView mapView;
    TextView addressTv;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_ACCESS_CALL_PHONE = 2;

    public LocationManager lm;
    public double longitude; //경도
    public double latitude; //위도
    public double altitude; //고도
    public float accuracy; //정확도
    public String provider; //위치제공자
    public String currentLocation; // 그래서 최종 위치

    FusedLocationProviderClient mFusedLocationClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup3);

        close = findViewById(R.id.close);
        townButton1 = findViewById(R.id.townButton1);
        townButton2 = findViewById(R.id.townButton2);
        townCheckBtn = findViewById(R.id.townCheckBtn);
        townCorrectBtn = findViewById(R.id.townCorrectBtn);
        addressTv = findViewById(R.id.addressTv);
        mapView = findViewById(R.id.mapView);
        mapView.setDaumMapApiKey("6e57980f9050faf730dbb4af45ab8602");


        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        setLocation();

        initSet();

        Toast.makeText(getApplicationContext(), "현재 위치를 찾고있습니다...", Toast.LENGTH_SHORT).show();
        getLocation();

        // todo : 지역설정1
        townButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                townCheckBtn.setVisibility(View.VISIBLE);
                addressTv.setVisibility(View.VISIBLE);

            }
        });


        //todo : 지역설정2
        townButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                townCheckBtn.setVisibility(View.VISIBLE);
                addressTv.setVisibility(View.VISIBLE);
            }
        });

        // todo :
        townCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // todo : 지역설정완료
        townCorrectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 위치 권한이 허용된 경우 위치 정보를 가져오는 메서드 호출
                getLocation();
            } else {
                // 권한이 거부된 경우 처리
                Toast.makeText(this, "위치 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        mapView.setShowCurrentLocationMarker(false);
    }

    public void setLocation() {

        lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        // 위치정보를 얻는다
        Toast.makeText(getApplicationContext(), "현재 위치를 찾고있습니다...", Toast.LENGTH_SHORT).show();
        getLocation();

    }

    public void initSet() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
    }


    public void getLocation() {

        try {
            addressTv.setText("수신중..");
            // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기~!!!
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);

            //txtCurrentPositionInfo.setText("위치정보 미수신중");
            //lm.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체를 해주어야 한다.

        } catch (SecurityException ex) {

        }
    }

    public void setDaumMapCurrentLocation(double latitude, double longitude) {

        // 중심점 변경
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
        // 줌 레벨 변경
        mapView.setZoomLevel(4, true);
        // 중심점 변경 + 줌 레벨 변경
        //mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude, longitude), 9, true);
        // 줌 인
        mapView.zoomIn(true);
        // 줌 아웃
        //mapView.zoomOut(true);
        // 마커 생성
        setDaumMapCurrentMarker();
    }

    public void setDaumMapCurrentMarker(){

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("현재 위치");
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);
    }
    public static String getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                Log.d("ljh", "Thoroughfare: " + returnedAddress.getThoroughfare());
                Log.d("ljh", "Thoroughfare: " + returnedAddress.getSubLocality());
                Log.d("ljh", "SubThoroughfare: " + returnedAddress.getSubThoroughfare());
                StringBuilder strReturnedAddress = new StringBuilder("");


                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("MyCurrentloctionaddress", strReturnedAddress.toString());
            } else {
                Log.w("MyCurrentloctionaddress", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("MyCurrentloctionaddress", "Canont get Address!");
        }

        // "대한민국 " 글자 지워버림
        if (strAdd.length() > 5) {
            strAdd = strAdd.substring(5);
        } else {

        }

        return strAdd;
    }

    private final LocationListener mLocationListener = new LocationListener() {
        private Handler handler;
        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("test", "onLocationChanged, location:" + location);
                    longitude = location.getLongitude(); //경도
                    latitude = location.getLatitude();   //위도
                    altitude = location.getAltitude();   //고도
                    accuracy = location.getAccuracy();    //정확도
                    provider = location.getProvider();   //위치제공자


                    currentLocation = getCompleteAddressString(getApplicationContext(), latitude, longitude);

//            txtCurrentMoney.setText("위치정보 : " + provider + "\n위도 : " + longitude + "\n경도 : " + latitude
//                    + "\n고도 : " + altitude + "\n정확도 : "  + accuracy);

                    // 위치 정보를 글로 나타낸다
                    addressTv.setText(currentLocation.toString());

                    // 지도를 움직인다
                    setDaumMapCurrentLocation(latitude, longitude);

                    lm.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체를 해주어야 한다.
                }
            });
        }

        public void onProviderDisabled(String provider) {
            // Disabled시
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        @Override
        public void onLocationChanged(@NonNull List<Location> locations) {
            LocationListener.super.onLocationChanged(locations);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 변경시
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };
}