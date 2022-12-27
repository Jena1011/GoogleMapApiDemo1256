package com.demo.android.googlemapapidemo1256;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.demo.android.googlemapapidemo1256.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                LatLng latLng = marker.getPosition();
                Intent intent = new Intent(MapsActivity.this, WeatherActivity.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("lat", latLng.latitude);
                bundle.putDouble("lng", latLng.longitude);
                startActivity(intent);
            }
        });

        // Add a marker in Sydney and move the camera
        LatLng Taoyuan = new LatLng(24.93119985316976, 121.17200995685634);
        LatLng Taipei = new LatLng(25.047612345268107, 121.51701141894509);
        LatLng center = new LatLng(23.781478186856063, 120.90782310486458);
        Marker marker = mMap.addMarker(new MarkerOptions().position(Taoyuan).title("Marker in Taoyuan").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker2)));
        Marker marker2 = mMap.addMarker(new MarkerOptions().position(Taipei).title("Marker in Taipei").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker2)));
        marker2.showInfoWindow(); // 一次只能顯示一個，其他要點擊才會顯示(目前預設是台北)
        mMap.setInfoWindowAdapter(new MyAdapter());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center,8)); // newLatLng() → newLatLngZoom 才可預設縮放大小
    }

    class  MyAdapter implements GoogleMap.InfoWindowAdapter{

        @Nullable
        @Override
        public View getInfoContents(@NonNull Marker marker) {


            return null;
        }

        @Nullable
        @Override
        public View getInfoWindow(@NonNull Marker marker) {
//            ImageView iv = new ImageView(MapsActivity.this);
//            iv.setImageResource(R.drawable.rain_day);
////            View view = getLayoutInflater().inflate(R.layout.myLayout, null);
            return null;
        }
    }
}