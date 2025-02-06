package com.example.healthlocator;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationActivity extends AppCompatActivity {

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_location);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        client.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null && supportMapFragment != null) {
                        supportMapFragment.getMapAsync(googleMap -> {
                            LatLng latLng = new LatLng(5.756998, 102.273499);

                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title("You are here!");

                            googleMap.addMarker(options).showInfoWindow();
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                            addHospitalMarkers(googleMap);
                        });
                    } else {
                        System.out.println("Location is null. Ensure GPS is enabled.");
                    }
                })
                .addOnFailureListener(e -> System.out.println("Failed to retrieve location: " + e.getMessage()));
    }

    private void addHospitalMarkers(GoogleMap googleMap) {
        LatLng[] hospitals = {
                new LatLng(5.7633, 102.2257), // Hospital Machang
                new LatLng(5.8100, 102.1523), // Hospital Tanah Merah
                new LatLng(6.0057, 102.3592), // Hospital Bachok
                new LatLng(6.1253, 102.1457), // Hospital Kota Bharu
                new LatLng(6.1897, 102.1573), // Hospital Tumpat
                new LatLng(5.5359, 102.1991), // Hospital Kuala Krai
                new LatLng(5.7808, 102.2131), // Klinik Kota Harmoni
                new LatLng(5.7643, 102.2188), // Klinik Bharu Machang
                new LatLng(5.8087, 102.2274), // Klinik Primer Impian
                new LatLng(6.0753, 102.2384)  // Poliklinik Al-Farhain
        };

        String[] hospitalNames = {
                "Hospital Machang",
                "Hospital Tanah Merah",
                "Hospital Bachok",
                "Hospital Raja Perempuan Zainab II",
                "Hospital Tumpat",
                "Hospital Kuala Krai",
                "Klinik Kota Harmoni Machang",
                "Klinik Baru Machang",
                "Klinik Primer Impian",
                "Poliklinik Al-Farhain 24 Jam"
        };

        float[] markerColors = {
                BitmapDescriptorFactory.HUE_AZURE,
                BitmapDescriptorFactory.HUE_BLUE,
                BitmapDescriptorFactory.HUE_GREEN,
                BitmapDescriptorFactory.HUE_YELLOW,
                BitmapDescriptorFactory.HUE_ORANGE,
                BitmapDescriptorFactory.HUE_CYAN,
                BitmapDescriptorFactory.HUE_AZURE,
                BitmapDescriptorFactory.HUE_AZURE,
                BitmapDescriptorFactory.HUE_AZURE,
                BitmapDescriptorFactory.HUE_VIOLET
        };

        for (int i = 0; i < hospitals.length; i++) {
            googleMap.addMarker(new MarkerOptions()
                    .position(hospitals[i])
                    .title(hospitalNames[i])
                    .icon(BitmapDescriptorFactory.defaultMarker(markerColors[i % markerColors.length])));
        }
    }
}
