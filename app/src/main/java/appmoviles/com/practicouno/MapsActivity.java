package appmoviles.com.practicouno;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng position;
    private Marker myLocation;
    private static final int REQUEST_CODE = 11;
    private LocationManager manager;

    private int opcion = 0;

    Polyline polyBiblioteca;
    Polyline polyEdificioC;
    Polyline polyCafeteria;

    private int points = 0;
    private FloatingActionButton boton;
    private FloatingActionButton boton_canje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        if (intent != null) {
            points = intent.getIntExtra("Points", 0);
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boton = findViewById(R.id.boton_accion);
        boton_canje = findViewById(R.id.boton_accion_2);
        boton_canje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchNewActivityCanje();
            }
        });
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchNewActivity();
            }
        });
        boton.hide();
        boton_canje.hide();
    }

    private void launchNewActivity() {
        if (opcion == 1) {
            Intent intent = new Intent(this, Canje.class);
            intent.putExtra("Points", points);
            startActivity(intent);
        } else if (opcion == 2) {
            Intent intent = new Intent(this, Preguntas.class);
            intent.putExtra("Points", points);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void launchNewActivityCanje() {
        Intent intent = new Intent(this, Canje.class);
        intent.putExtra("Points", points);
        startActivity(intent);
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
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, REQUEST_CODE);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(3.341477, -76.529987);
        myLocation = mMap.addMarker(new MarkerOptions().position(sydney).title("Yo"));
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(20));

        polyBiblioteca = mMap.addPolyline(new PolylineOptions().add(
                new LatLng(3.341934, -76.530068),
                new LatLng(3.341928, -76.529800),
                new LatLng(3.341666, -76.529800),
                new LatLng(3.341671, -76.530089),
                new LatLng(3.341934, -76.530068)
        ));

        polyEdificioC = mMap.addPolyline(new PolylineOptions().add(
                new LatLng(3.341254, -76.530411),
                new LatLng(3.341243, -76.529859),
                new LatLng(3.341061, -76.529875),
                new LatLng(3.341066, -76.530443),
                new LatLng(3.341254, -76.530411)
        ));

        polyCafeteria = mMap.addPolyline(new PolylineOptions().add(
                new LatLng(3.342255, -76.529703),
                new LatLng(3.342260, -76.529478),
                new LatLng(3.341886, -76.529500),
                new LatLng(3.341896, -76.529709),
                new LatLng(3.342255, -76.529703)
        ));


        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 1, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
                myLocation.setPosition(pos);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation.getPosition()));

                boolean biblioteca = comprobarBiblioteca(pos);
                boolean cafeteria = comprobarCafeteria(pos);
                boolean edificioC = comprobarEdificioC(pos);


                if (biblioteca) {
                    // Abrir Canje
                    opcion = 1;
                    boton.show();

                } else if (cafeteria) {
                    // Abrir Preguntas
                    boton.show();
                    opcion = 2;
                } else if (edificioC) {
                    // Abrir Preguntas
                    boton.show();
                    opcion = 2;

                } else {
                    boton.hide();
                    opcion = 0;
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });

    }


    public boolean comprobarBiblioteca(LatLng actual) {
        boolean estoy = PolyUtil.containsLocation(actual, polyBiblioteca.getPoints(), true);
        return estoy;
    }

    public boolean comprobarEdificioC(LatLng actual) {
        boolean estoy = PolyUtil.containsLocation(actual, polyEdificioC.getPoints(), true);
        return estoy;
    }

    public boolean comprobarCafeteria(LatLng actual) {
        boolean estoy = PolyUtil.containsLocation(actual, polyCafeteria.getPoints(), true);
        return estoy;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
