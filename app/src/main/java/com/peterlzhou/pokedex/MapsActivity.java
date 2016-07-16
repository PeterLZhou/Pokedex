package com.peterlzhou.pokedex;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.LocationListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String TAG = MapsActivity.class.getSimpleName();
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Context context = this;
    //This is considered bad practice, I will work on fixing it later
    public static LatLng mlatLng;
    public static GoogleMap mGoogleMap;
    TouchableMapFragment mFragment;
    FrameLayout mapTouchLayer;
    GetServer makeGet;
    Double viewlatitude, viewlongitude = 0.0;
    double range = 0.0;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    public static ListView mDrawerList;
    private View mLayout;
    private static final int REQUEST_LOCATIONS = 0;
    public static ArrayList<String> markerArrayList = new ArrayList<String>();
    public static final String[] POKEMON = new String[]{
            //NOTE: All Pokemon taking the first position means that the other pokemon will start indexed at 1
            "All Pokémon",
            "Rare Pokémon",
            "Abra",
            "Aerodactyl",
            "Alakazam",
            "Arbok",
            "Arcanine",
            "Articuno",
            "Beedrill",
            "Bellsprout",
            "Blastoise",
            "Bulbasaur",
            "Butterfree",
            "Caterpie",
            "Chansey",
            "Charizard",
            "Charmander",
            "Charmeleon",
            "Clefable",
            "Clefairy",
            "Cloyster",
            "Cubone",
            "Dewgong",
            "Diglett",
            "Ditto",
            "Dodrio",
            "Doduo",
            "Dragonair",
            "Dragonite",
            "Dratini",
            "Drowzee",
            "Dugtrio",
            "Eevee",
            "Ekans",
            "Electabuzz",
            "Electrode",
            "Exeggcute",
            "Exeggutor",
            "Farfetch'd",
            "Fearow",
            "Flareon",
            "Gastly",
            "Gengar",
            "Geodude",
            "Gloom",
            "Golbat",
            "Goldeen",
            "Golduck",
            "Golem",
            "Graveler",
            "Grimer",
            "Growlithe",
            "Gyarados",
            "Haunter",
            "Hitmonchan",
            "Hitmonlee",
            "Horsea",
            "Hypno",
            "Ivysaur",
            "Jigglypuff",
            "Jolteon",
            "Jynx",
            "Kabuto",
            "Kabutops",
            "Kadabra",
            "Kakuna",
            "Kangaskhan",
            "Kingler",
            "Koffing",
            "Krabby",
            "Lapras",
            "Lickitung",
            "Machamp",
            "Machoke",
            "Machop",
            "Magikarp",
            "Magmar",
            "Magnemite",
            "Magneton",
            "Mankey",
            "Marowak",
            "Meowth",
            "Metapod",
            "Mew",
            "Mewtwo",
            "Moltres",
            "Mr. Mime",
            "Muk",
            "Nidoking",
            "Nidoqueen",
            "Nidoran F",
            "Nidoran M",
            "Nidorina",
            "Nidorino",
            "Ninetales",
            "Oddish",
            "Omanyte",
            "Omastar",
            "Onix",
            "Paras",
            "Parasect",
            "Persian",
            "Pidgeot",
            "Pidgeotto",
            "Pidgey",
            "Pikachu",
            "Pinsir",
            "Poliwag",
            "Poliwhirl",
            "Poliwrath",
            "Ponyta",
            "Porygon",
            "Primeape",
            "Psyduck",
            "Raichu",
            "Rapidash",
            "Raticate",
            "Rattata",
            "Rhydon",
            "Rhyhorn",
            "Sandshrew",
            "Sandslash",
            "Scyther",
            "Seadra",
            "Seaking",
            "Seel",
            "Shellder",
            "Slowbro",
            "Slowpoke",
            "Snorlax",
            "Spearow",
            "Squirtle",
            "Starmie",
            "Staryu",
            "Tangela",
            "Tauros",
            "Tentacool",
            "Tentacruel",
            "Vaporeon",
            "Venomoth",
            "Venonat",
            "Venusaur",
            "Victreebel",
            "Vileplume",
            "Voltorb",
            "Vulpix",
            "Wartortle",
            "Weedle",
            "Weepinbell",
            "Weezing",
            "Wigglytuff",
            "Zapdos",
            "Zubat"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Request permission at the very start so that we don't have to go through a loop?
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.main_drawer);
        mLayout = findViewById(R.id.main_drawer);
        //Loads up the maps fragment
        mFragment = (TouchableMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mFragment.getMapAsync(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.draw_item, POKEMON));
        //set up drawerlistview with items and click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object *//* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
        //Handles Button Clicks, Sends POST request, unfocuses keyboard, and gives Toast message if valid, Gives Toast message if invalid
        ImageButton PingButton = (ImageButton) findViewById(R.id.ping_button);
        PingButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        //Zooms the Camera in, then starts the intent # The zoom doesn't work
                        if (mlatLng !=null) {
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(mlatLng).zoom(20).build();
                            mGoogleMap.animateCamera(CameraUpdateFactory
                                    .newCameraPosition(cameraPosition));
                            SubmitDialogFragment myFragment = new SubmitDialogFragment();
                            myFragment.show(getFragmentManager(), "fragment");
                        }
                        //TODO: Comment this out for now
                        //startActivity(new Intent(MapsActivity.this, Pop.class));
                        //This is for the GET request. TODO: Move this
                        //GetServer makeGet = new GetServer();
                        //makeGet.execute();
                        //Show up dialog box
                        else{
                            Toast.makeText(context, "Make sure you have location services enabled.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
   }

    @Override
    public void onMapReady(GoogleMap gMap) {
        //System.out.println("Map is ready!");
        mGoogleMap = gMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        }
        else {
            mGoogleMap.setMyLocationEnabled(true);
            buildGoogleApiClient();
            mGoogleApiClient.connect();
            //Map on settled listener
            new MapStateListener(mGoogleMap, mFragment, this) {
                @Override
                public void onMapTouched() {
                    //System.out.println("I touched the map");

                }

                @Override
                public void onMapReleased() {
                    //System.out.println("I released the map");
                }

                @Override
                public void onMapUnsettled() {
                    //System.out.println("I unsettled the map");
                }

                @Override
                public void onMapSettled() {
                    //System.out.println("I settled the map");
                    viewlatitude = (mGoogleMap.getProjection().getVisibleRegion().latLngBounds.northeast.latitude + mGoogleMap.getProjection().getVisibleRegion().latLngBounds.southwest.latitude) / 2;
                    viewlongitude = (mGoogleMap.getProjection().getVisibleRegion().latLngBounds.northeast.longitude + mGoogleMap.getProjection().getVisibleRegion().latLngBounds.southwest.longitude) / 2;
                    range = Math.abs(mGoogleMap.getProjection().getVisibleRegion().latLngBounds.northeast.latitude - viewlatitude);
                    //System.out.println("The map changed and the viewlatitude is" + viewlatitude + "and the view longitude is" + viewlongitude);
                    new GetServer(mGoogleMap, context, viewlatitude, viewlongitude, range).execute();
                }
            };
            System.out.println("I should have made a mapstate listener by now");
        }
    }

    protected synchronized void buildGoogleApiClient() {
        //Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        //Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        }
        else{
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null && mGoogleMap != null) {
                //place marker at current position
                //mGoogleMap.clear(); # Use This if you want to refresh the map upon no last location
                mlatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                Location location = mGoogleMap.getMyLocation();
                CameraPosition myPosition = new CameraPosition.Builder()
                        .target(mlatLng).zoom(18).build();
                mGoogleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(myPosition));
            }
            System.out.println("I set a locationrequest");
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(5000); //5 seconds
            mLocationRequest.setFastestInterval(3000); //3 seconds
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            //Focus on current location
            //TODO: Update default zoom on current location
            if (mlatLng != null) {
                CameraPosition setPosition = new CameraPosition.Builder()
                        .target(mlatLng).zoom(18).build();
                mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(setPosition));
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        //Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        //TODO: Remove markers when viewport leaves
        mlatLng = new LatLng(location.getLatitude(), location.getLongitude());
    }
    @Override
    protected void onResume(){
        super.onResume();
        //Remove focus on soft keyboard
        findViewById(R.id.map).requestFocus();
        if (mlatLng != null && mGoogleMap != null){
            //Focus on current location
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(mlatLng).zoom(20).build();
            mGoogleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }

    }

    private void requestLocationPermission() {
        Log.i(TAG, "LOCATION permission has NOT been granted. Requesting permission.");

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Log.i(TAG, "Displaying location access rationale to provide additional context.");
            Snackbar.make(mLayout, R.string.permission_location_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MapsActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_LOCATIONS);
                        }
                    })
                    .show();
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATIONS);
        }
        // END_INCLUDE(camera_permission_request)
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_LOCATIONS) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for camera permission.
            Log.i(TAG, "Received response for Camera permission request.");

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
                Log.i(TAG, "CAMERA permission has now been granted. Showing preview.");
                Snackbar.make(mLayout, R.string.permision_available_location,
                        Snackbar.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "CAMERA permission was NOT granted.");
                Snackbar.make(mLayout, R.string.permissions_not_granted,
                        Snackbar.LENGTH_SHORT).show();

            }
            onMapReady(mGoogleMap);
        }else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
            //This is for the GET request. TODO: Move this
            mGoogleMap.clear();
            markerArrayList.clear();
            makeGet = new GetServer(mGoogleMap, context, viewlatitude, viewlongitude, range);
            makeGet.execute();
        }
    }

    //Use this method to hide the keyboard
    //Doesn't work
    public void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        //Toast.makeText(this,"Invalid Pokemon!",Toast.LENGTH_SHORT).show();
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

}