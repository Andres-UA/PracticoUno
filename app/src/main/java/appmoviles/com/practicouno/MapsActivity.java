package appmoviles.com.practicouno;

import android.Manifest;
import android.annotation.SuppressLint;
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

    Polyline Biblioteca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
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

        Biblioteca = mMap.addPolyline(new PolylineOptions().add(
                new LatLng(3.341934, -76.530068),
                new LatLng(3.341928, -76.529800),
                new LatLng(3.341666, -76.529800),
                new LatLng(3.341671, -76.530089),
                new LatLng(3.341934, -76.530068)
        ));

        Polyline EdificioC = mMap.addPolyline(new PolylineOptions().add(
                new LatLng(3.341254, -76.530411),
                new LatLng(3.341243, -76.529859),
                new LatLng(3.341061, -76.529875),
                new LatLng(3.341066, -76.530443),
                new LatLng(3.341254, -76.530411)
        ));

        Polyline Cafeteria = mMap.addPolyline(new PolylineOptions().add(
                new LatLng(3.342255, -76.529703),
                new LatLng(3.342260, -76.529478),
                new LatLng(3.341886, -76.529500),
                new LatLng(3.341896, -76.529709),
                new LatLng(3.342255, -76.529703)
        ));

        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
                myLocation.setPosition(pos);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation.getPosition()));
                int opcion = -1;
                boolean biblioteca = comprobarBiblioteca(pos);
                if (biblioteca) {
                    // Abrir Canje
                    // boton.setVisibility(View.VISIBLE);
    Toast.makeText(getApplication(),biblioteca+"",Toast.LENGTH_LONG).show();
                    opcion = 1;
                } else {
                    opcion = 2;
                    boolean cafeteria = true;
                    if (cafeteria) {
                        // Abrir Preguntas

                    } else {
                        boolean edificioC = false;
                        // Abrir Preguntas
                    }
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
        boolean estoy = PolyUtil.containsLocation(actual, Biblioteca.getPoints(), true);
        return estoy;
    }

    public boolean comprobarEdificioC(LatLng actual) {
        boolean estoy = false;
        LatLng[] c = new LatLng[4];
        c[0] = new LatLng(3.341254, -76.530411);
        c[0] = new LatLng(3.341243, -76.529859);
        c[0] = new LatLng(3.341061, -76.529875);
        c[0] = new LatLng(3.341066, -76.530443);
        estoy = dentroPoligono(c, actual);
        return estoy;
    }

    public boolean comprobarCafeteria(LatLng actual) {
        boolean estoy = false;
        LatLng[] bilioteca = new LatLng[4];

        bilioteca[0] = new LatLng(3.342255, -76.529703);
        bilioteca[0] = new LatLng(3.341886, -76.529500);
        bilioteca[0] = new LatLng(3.341886, -76.529500);
        bilioteca[0] = new LatLng(3.341896, -76.529709);
        estoy = dentroPoligono(bilioteca, actual);
        return estoy;
    }

    public boolean dentroPoligono(LatLng[] puntos, LatLng actual) {
        int counter = 0;
        int i;
        double xinters;
        LatLng p1 = new LatLng(0, 0);
        LatLng p2 = new LatLng(0, 0);
        int n = puntos.length;

        p1 = puntos[0];
        for (i = 1; i <= n; i++) {
            p2 = puntos[i % n];
            if (actual.latitude > Math.min(p1.latitude, p2.latitude)) {
                if (actual.latitude <= Math.max(p1.latitude, p2.latitude)) {
                    if (actual.longitude <= Math.max(p1.longitude, p2.longitude)) {
                        if (p1.latitude != p2.latitude) {
                            xinters = (actual.latitude - p1.latitude) * (p2.longitude - p1.longitude) / (p2.latitude - p1.latitude) + p1.longitude;
                            if (p1.longitude == p2.longitude || actual.longitude <= xinters)
                                counter++;
                        }
                    }
                }
            }
            p1 = p2;
        }
        if (counter % 2 == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
