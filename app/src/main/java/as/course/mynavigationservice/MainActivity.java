package as.course.mynavigationservice;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    double altitude;
    double latitude;
    double longitude;
    float speed ;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult ( new ActivityResultContracts
                                .RequestMultiplePermissions ( ) , result -> {
                            Boolean fineLocationGranted = result.getOrDefault (
                                    Manifest.permission.ACCESS_FINE_LOCATION , false );
                            Boolean coarseLocationGranted = result.getOrDefault (
                                    Manifest.permission.ACCESS_COARSE_LOCATION , false );
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                            } else {
                                // No location access granted.
                            }
                        }
                );

        locationPermissionRequest.launch ( new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION ,
                Manifest.permission.ACCESS_COARSE_LOCATION
        } );

        fusedLocationClient = LocationServices.getFusedLocationProviderClient ( this );


        if (ActivityCompat.checkSelfPermission ( this , Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission ( this , Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation ( )
                .addOnSuccessListener ( this , new OnSuccessListener<Location> ( ) {
                    @Override
                    public void onSuccess ( Location location ) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            altitude = location.getAltitude ();
                            latitude = location.getLatitude ();
                            longitude = location.getLongitude ();
                            speed = location.getSpeed ();
                        }
                    }
                } );
        Toast.makeText(this,
                "latitude="+String.valueOf (latitude),
                Toast.LENGTH_LONG).show();
        TextView latitudeView = findViewById(R.id.latitude);
        latitudeView.setText(String.valueOf (latitude));

        TextView longitudeView = findViewById(R.id.longitude);
        longitudeView.setText(String.valueOf (longitude));

        TextView altitudeView = findViewById(R.id.altitude);
        altitudeView.setText(String.valueOf (altitude));

        TextView speedView = findViewById(R.id.speed);
        speedView.setText(String.valueOf (speed));

    }
}