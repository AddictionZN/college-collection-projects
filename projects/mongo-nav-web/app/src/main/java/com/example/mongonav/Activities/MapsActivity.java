package com.example.mongonav.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.camera2.TotalCaptureResult;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

// classes needed to initialize map
import com.example.mongonav.BaseActivity;
import com.example.mongonav.Load.MainActivity;
import com.example.mongonav.Models.ObjectSerializer;
import com.example.mongonav.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.Maps;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geocoder.MapboxGeocoder;
import com.mapbox.geocoder.service.models.GeocoderResponse;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

// classes needed to add the location component
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;

// classes needed to add a marker
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

// Geo Locator

// classes to calculate a route
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;
import com.mapbox.services.android.navigation.ui.v5.map.NavigationMapboxMap;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigationOptions;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;

// classes needed to launch navigation UI
import android.view.View;
import android.widget.Button;

import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.ui.geocoder.GeocoderAutoCompleteView;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.commons.models.Position;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener, PermissionsListener {

    // Firebase Elements
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference users;
    private FirebaseFirestore dataUsers;
    private FirebaseUser user;

    // variables for adding location layer
    private MapView mapView;
    private MapboxMap mapboxMap;

    // variables for adding location layer
    LocationManager locationManager;
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    private LocationEngine locationEngine;
    private long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    private static final int PLACE_SELECTION_REQUEST_CODE = 56789;
    private static final int ONE_HUNDRED_MILLISECONDS = 100;

    // variables for calculating and drawing a route
    private DirectionsRoute currentRoute;
    private static final String TAG = "MapsActivity";
    private NavigationMapRoute navigationMapRoute;
    private Marker destinationMarker;
    private String geojsonSourceLayerId = "geojsonSourceLayerId";
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;

    // Locations
    Location location;
    private LatLng originCoord;
    private LatLng destinationCoord;

    // Buttons, ImageViews
    private Button button;
    private ImageView imgHome;
    private ImageView imgNav;

    // Getting the email throughout the application
    private String email;
    private String transport;
    private String system;
    private String mode;

    // Getting the modes and themes
    private String selectTransport;
    private String selectSystem;
    private String selectMode;

    private Point startPosition;
    private Point endPosition;
    private LatLng savePoint;
    private Marker marker;
    private LatLng intialLocation;
    private boolean isNavi = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_maps);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Getting the state of the email through the application
        Intent i = getIntent();
        email = i.getStringExtra("email");

        // FireBase initialisation
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("users");

        // Getting the firestore database to the instance
        dataUsers = FirebaseFirestore.getInstance();

        imgHome = findViewById(R.id.imgHome);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        imgHome.setOnClickListener(v -> {
            Intent intent = new Intent(MapsActivity.this, BaseActivity.class);
            intent.putExtra("email", email.trim());
            startActivity(intent);
            MapsActivity.this.finish();
        });

        // Getting the users data from firebase
        DocumentReference docRefUser = dataUsers.collection("users").document(email);
        docRefUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    transport = documentSnapshot.getString("transport");
                    system = documentSnapshot.getString("system");
                    mode = documentSnapshot.getString("mode");

                    // Determining the different modes
                    if (mode.equals("Light")) {
                        mapboxMap.setStyle(Style.LIGHT);
                    } else if (mode.equals("Dark")) {
                        mapboxMap.setStyle(Style.DARK);
                    } else if (mode.equals("Satellite")) {
                        mapboxMap.setStyle(Style.SATELLITE);
                    } else if (mode.equals("Streets")) {
                        mapboxMap.setStyle(Style.MAPBOX_STREETS);
                    } else if (mode.equals("Traffic")) {
                        mapboxMap.setStyle(Style.TRAFFIC_DAY);
                    }

                } else {
                    Toast.makeText(MapsActivity.this, "Unable to get the users data", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        // This code is the Geocoder for the auto complete and the search function of the application
        GeocoderAutoCompleteView autocomplete = (GeocoderAutoCompleteView) findViewById(R.id.query);
        // Setting the autocomplete to South Africa
        autocomplete.setCountry("ZA");
        autocomplete.setAccessToken(Mapbox.getAccessToken());
        String types[] = {GeocodingCriteria.TYPE_PLACE, GeocodingCriteria.TYPE_ADDRESS, GeocodingCriteria.TYPE_DISTRICT, GeocodingCriteria.TYPE_REGION};
        autocomplete.setTypes(types);
        autocomplete.setOnFeatureListener(new GeocoderAutoCompleteView.OnFeatureListener() {
            @Override
            @SuppressWarnings({"MissingPermission"})
            public void onFeatureClick(com.mapbox.services.api.geocoding.v5.models.CarmenFeature feature) {
                hideOnScreenKeyboard();
                Position position = feature.asPosition();
                mapboxMap.addOnMapClickListener(MapsActivity.this);

                // Getting the start point and end point and computing it into the system
                startPosition = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                        locationComponent.getLastKnownLocation().getLatitude());

                endPosition = Point.fromLngLat(position.getLongitude(), position.getLatitude());

                // Enabling the route of the start to end point
                getRoute(startPosition, endPosition);

                imgNav.setEnabled(true);
                imgNav.setBackgroundResource(R.color.colorPrimary);

                //****************************************************************************************************************
                // SAVING THE LOCATION HISTORY
                //****************************************************************************************************************
                savePoint = new LatLng(position.getLongitude(), position.getLatitude());

                // Saving the locations to the share preferences
                saveLocationData(savePoint);

            }
        });
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.addOnMapClickListener(MapsActivity.this);

        Intent intent = getIntent();
        if (intent.getIntExtra("place", 0) == 0) {
            mapboxMap.setStyle(getString(R.string.navigation_guidance_day), new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);

                    addDestinationIconSymbolLayer(style);

                    imgNav = findViewById(R.id.imgNavigate);
                    imgNav.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                boolean simulateRoute = true;

                                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                        .directionsRoute(currentRoute)
                                        .shouldSimulateRoute(simulateRoute)
                                        .build();

                                // Call this method with Context from within an Activity
                                NavigationLauncher.startNavigation(MapsActivity.this, options);
                            } catch (Exception e) {
                                Toast.makeText(MapsActivity.this, "Please select a location", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        } else {
            Location placeLocation = new Location(LocationManager.GPS_PROVIDER);
            placeLocation.setLatitude(TripsActivity.locations.get(intent.getIntExtra("place", 0)).getLatitude());
            placeLocation.setLongitude(TripsActivity.locations.get(intent.getIntExtra("place", 0)).getLongitude());

            addMarker(placeLocation, TripsActivity.places.get(intent.getIntExtra("place", 0)));
        }
    }

    private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage("destination-icon-id",
                BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));
        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        loadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
        destinationSymbolLayer.withProperties(
                iconImage("destination-icon-id"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        );
        loadedMapStyle.addLayer(destinationSymbolLayer);
    }

    @SuppressWarnings({"MissingPermission"})
    @Override
    public boolean onMapClick(@NonNull LatLng point) {

        if (destinationMarker != null) {
            mapboxMap.removeMarker(destinationMarker);
        }
        destinationCoord = point;
        destinationMarker = mapboxMap.addMarker(new MarkerOptions()
                .position(destinationCoord)
        );
        marker = mapboxMap.addMarker(new MarkerOptions().position(point));
        endPosition = Point.fromLngLat(point.getLongitude(), point.getLatitude());
        startPosition = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                locationComponent.getLastKnownLocation().getLatitude());

        GeoJsonSource source = mapboxMap.getStyle().getSourceAs("destination-source-id");
        if (source != null) {
            source.setGeoJson(Feature.fromGeometry(endPosition));
        }

        getRoute(startPosition, endPosition);

        //****************************************************************************************************************
        // GETTING THE SHARE PREFERENCES TO STORE TO HISTORY
        //****************************************************************************************************************

        saveLocationData(point);

        imgNav.setEnabled(true);
        imgNav.setBackgroundResource(R.color.colorPrimary);
        return true;
    }

    private void getRoute(Point origin, Point destination) {

        try {
            NavigationRoute.builder(this)
                    .accessToken(Mapbox.getAccessToken())
                    .origin(origin)
                    .destination(destination)
                    .build()
                    .getRoute(new Callback<DirectionsResponse>() {
                        @Override
                        public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                            // You can get the generic HTTP info about the response
                            Log.d(TAG, "Response code: " + response.code());
                            if (response.body() == null) {
                                Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                                return;
                            } else if (response.body().routes().size() < 1) {
                                Log.e(TAG, "No routes found");
                                return;
                            }

                            currentRoute = response.body().routes().get(0);

                            // Draw the route on the map
                            if (navigationMapRoute != null) {
                                navigationMapRoute.removeRoute();
                            } else {
                                navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap);
                            }
                            navigationMapRoute.addRoute(currentRoute);
                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                            Log.e(TAG, "Error: " + throwable.getMessage());
                        }
                    });


        } catch (Exception e) {

            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Activate the MapboxMap LocationComponent to show user location
            // Adding in LocationComponentOptions is also an optional parameter
            locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(this, loadedMapStyle);
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent(mapboxMap.getStyle());
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

            // Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

            // Create a new FeatureCollection and add a new Feature to it using selectedCarmenFeature above.
            // Then retrieve and update the source designated for showing a selected location's symbol layer icon

            if (mapboxMap != null) {
                Style style = mapboxMap.getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs(geojsonSourceLayerId);
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[]{Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }

                    // Move map camera to the selected location
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                                            ((Point) selectedCarmenFeature.geometry()).longitude()))
                                    .zoom(14)
                                    .build()), 4000);
                }
            }
        }
    }

    // *********************************************************************************************
    // Custom Methods
    // *********************************************************************************************
    private void hideOnScreenKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

    }

    public void addMarker(Location location, String title) {
        if (location != null) {
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mapboxMap.clear();
            mapboxMap.addMarker(new MarkerOptions().position(userLocation).title(title));
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12));
        }
    }

    public void saveLocationData(LatLng point) {

        // Getting googles api to obtain the place
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        // Creating a string address to be stored in the shared preferences
        String address = "";

        try {

            // 1. Get the list of the lat and long then convert it to the location point getting the name of that place
            List<Address> listAdddresses = geocoder.getFromLocation(point.getLatitude(), point.getLongitude(), 1);

            // 2. Adding the list of addresses to the 1 address in the share preferences
            if (listAdddresses != null && listAdddresses.size() > 0) {
                if (listAdddresses.get(0).getThoroughfare() != null) {
                    if (listAdddresses.get(0).getSubThoroughfare() != null) {
                        address += listAdddresses.get(0).getSubThoroughfare() + " ";
                    }
                    address += listAdddresses.get(0).getThoroughfare();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // If the address is null just add the date of the place where the user went to.
        if (address.equals("")) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy-MM-dd");
            address += sdf.format(new Date());
        }

        mapboxMap.addMarker(new MarkerOptions().position(point).title(address));

        // Add the place and location to the shared preferences in the Trips Activity
        TripsActivity.places.add(address);
        TripsActivity.locations.add(point);

        //TripsActivity.arrayAdapter.notifyDataSetChanged();
        // Getting the shared preferences
        SharedPreferences sharedPreferences = this.getSharedPreferences(email, Context.MODE_PRIVATE);

        try {

            // Create arrays for the latitude and longitude
            ArrayList<String> latitudes = new ArrayList<>();
            ArrayList<String> longitudes = new ArrayList<>();

            // Run a for loop to create the and convert the lat and longs a point/coridanate
            for (LatLng coord : TripsActivity.locations) {
                latitudes.add(Double.toString(coord.getLatitude()));
                longitudes.add(Double.toString(coord.getLongitude()));
            }

            //1. Getting the shared preferences to save the key and pairs of the data
            //2. Use the object serialize to protect the locations for the user
            //3. Then apply the shared preference to the storage in the phone
            sharedPreferences.edit().putString("places", ObjectSerializer.serialize(TripsActivity.places)).apply();
            sharedPreferences.edit().putString("lats", ObjectSerializer.serialize(latitudes)).apply();
            sharedPreferences.edit().putString("lons", ObjectSerializer.serialize(longitudes)).apply();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "Location Saved!", Toast.LENGTH_SHORT).show();
    }
}
