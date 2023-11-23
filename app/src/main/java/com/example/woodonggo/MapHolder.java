package com.example.woodonggo;

import android.content.Context;

import net.daum.android.map.MapView;
import net.daum.mf.map.api.MapLayout;

import java.lang.reflect.Field;

public class MapHolder {
    private static MapView mapView;

    public static MapView getMapView(Context context) {
        if (mapView == null) {
            mapView = new MapView(context);
            // 필요한 초기화 코드 추가
        }
        return mapView;
    }

    public static void releaseMapView() {
        if (mapView != null) {
            // MapView의 내부 MapLayout을 가져와서 removeAllViews 호출
            try {
                Field mapLayoutField = MapView.class.getDeclaredField("mMapLayout");
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
    }
}
