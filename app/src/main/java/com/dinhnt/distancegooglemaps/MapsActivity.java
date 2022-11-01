package com.dinhnt.distancegooglemaps;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dinhnt.distancegooglemaps.models.Location;
import com.dinhnt.distancegooglemaps.others.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.dinhnt.distancegooglemaps.databinding.ActivityMapsBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText edtDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        edtDistance = findViewById(R.id.edtDistance);
        Button btnShow = findViewById(R.id.btnShow);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMapReady(mMap);
            }
        });
    }

    private ArrayList<Location> getData(double distance) {
        //data mẫu
        ArrayList<Location> list = new ArrayList<>();
        list.add(new Location(1, "Địa điểm A", "10.8063811,106.68655869999999"));
        list.add(new Location(2, "Địa điểm B", "10.7918127,106.71276329999999"));
        list.add(new Location(3, "Địa điểm C", "10.807865999999999,106.71064199999999"));
        list.add(new Location(4, "Địa điểm D", "10.808539999999999,106.694276"));
        list.add(new Location(5, "Địa điểm E", "10.8123062,106.6953832"));


        //lọc data
        ArrayList<Location> listResult = new ArrayList<>();
        for (Location location : list) {
            String vitri = location.getLglt();
            int vt = vitri.indexOf(",");
            double lat = Double.parseDouble(vitri.substring(0, vt));
            double lng = Double.parseDouble(vitri.substring(vt + 1));
            LatLng latLng = new LatLng(lat, lng);

            if (CalculationByDistance(currentLoaction(), latLng) <= distance) {
                listResult.add(location);
            }
        }

        return listResult;
    }

    private void showLocation(ArrayList<Location> listLocationShow) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        int i = 0;
        mMap.clear();
        for (Location location : listLocationShow) {
            String vitri = location.getLglt();
            int vt = vitri.indexOf(",");
            double lat = Double.parseDouble(vitri.substring(0, vt));
            double lng = Double.parseDouble(vitri.substring(vt + 1));
            LatLng latLng = new LatLng(lat, lng);
            mMap.setMyLocationEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .title(location.getName())
                    .position(latLng));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.place));
            marker.setTag(i);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        showLocation(getData(Double.parseDouble(edtDistance.getText().toString())));
    }

    private double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    private LatLng currentLoaction() {
        GPSTracker gpsTracker = new GPSTracker(MapsActivity.this);
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            return new LatLng(latitude, longitude);
        } else {
            // gpsTracker.showSettingsAlert();
            return new LatLng(0, 0);
        }
    }
}