package com.example.woodonggo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import net.daum.mf.map.api.MapLayout;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LoginSignup3 extends AppCompatActivity {

    String userId;
    ImageView close;
    Button townButton1, townButton2, townCheckBtn, townEnd;
    private static Button previousButton;
    static String town1, town2, town12;
    MapView mapView;
    TextView addressTv;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    //private static final int PERMISSIONS_REQUEST_ACCESS_CALL_PHONE = 2;

    public LocationManager lm;
    public double longitude; //경도
    public double latitude; //위도
    public double altitude; //고도
    public float accuracy; //정확도
    public String provider; //위치제공자
    public String currentLocation; // 그래서 최종 위치

    FusedLocationProviderClient mFusedLocationClient;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup3);

        close = findViewById(R.id.close);
        townButton1 = findViewById(R.id.townButton1);
        townButton2 = findViewById(R.id.townButton2);
        townCheckBtn = findViewById(R.id.townCheckBtn);
        townEnd = findViewById(R.id.townEnd);
        addressTv = findViewById(R.id.addressTv);

        mapView = findViewById(R.id.mapView);
        mapView.setDaumMapApiKey("6e57980f9050faf730dbb4af45ab8602");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getString("UserId", "");

        // 위치 권한 확인 및 요청
        if (shouldRequestLocationPermission()) {
            requestLocationPermission();
        } else {
            startTracking();
        }

        setLocation();

        initSet();

        Toast.makeText(getApplicationContext(), "현재 위치를 찾고있습니다...", Toast.LENGTH_SHORT).show();
        getLocation();
        

        
        // 지역설정1
        townButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                townCheckBtn.setVisibility(View.VISIBLE);
                addressTv.setVisibility(View.VISIBLE);
                previousButton = townButton1;
            }
        });


        // 지역설정2
        townButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                townCheckBtn.setVisibility(View.VISIBLE);
                addressTv.setVisibility(View.VISIBLE);
                previousButton = townButton2;
            }
        });

        // 내 동네 맞아요
        townCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (previousButton == townButton1) {
                    town1 = town12;
                    previousButton.setText(town1);
                } else {
                    town2 = town12;
                    previousButton.setText(town2);
                }
                townCheckBtn.setVisibility(View.INVISIBLE);
                addressTv.setVisibility(View.INVISIBLE);

            }
        });

        // 지역설정완료
        townEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFirestoreDocument(userId, town1, town2);
                //화면 종료 MapView 종료
                if (mapView != null) {
                    // MapView의 내부 MapLayout을 가져와서 removeAllViews 호출
                    try {
                        Field mapLayoutField = net.daum.android.map.MapView.class.getDeclaredField("mMapLayout");
                        mapLayoutField.setAccessible(true);
                        MapLayout mapLayout = (MapLayout) mapLayoutField.get(mapView);

                        if (mapLayout != null) {
                            mapLayout.removeAllViews();
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    // MapView를 null로 설정하여 참조 해제
                    mapView = null;
                }
                Intent intent = new Intent(LoginSignup3.this, MainActivity.class);
                intent.putExtra("userId", userId);  // userId를 인텐트에 추가
                startActivity(intent);
                finish();
            }
            // todo : 지역 파이어베이스에 넣기 town1, town2
            // MainActivity로 이동하는 코드 추가
        });
    }

    private void updateFirestoreDocument(String userId, String town1, String town2) {
        // Cloud Firestore 인스턴스에 액세스
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // 문서의 필드를 업데이트할 Map 생성
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("region1", town1);
        updateData.put("region2", town2);

        // "User" 컬렉션의 문서 업데이트
        db.collection("User")
                .document(userId)
                .update(updateData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // 성공적으로 업데이트된 경우 수행할 작업
                        // 예: 성공 메시지 표시
                        Toast.makeText(getApplicationContext(), "Firestore 문서가 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 업데이트 중에 오류가 발생한 경우 수행할 작업
                        // 예: 오류 메시지 표시
                        Toast.makeText(getApplicationContext(), "Firestore 문서 업데이트 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
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


    @SuppressLint("MissingPermission")
    private void startTracking() {
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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

    private boolean shouldRequestLocationPermission() {
        // 위치 권한 확인 로직
        return ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        // 위치 권한 요청 로직
        requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
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
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                    1000, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                    1000, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    public void setDaumMapCurrentLocation(double latitude, double longitude) {
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
        mapView.setZoomLevel(4, true);
        mapView.zoomIn(true);
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

                town12 = returnedAddress.getThoroughfare();

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