package com.peterlzhou.pokedex;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationListener;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    //This is considered bad practice, I will work on fixing it later
    public static LatLng mlatLng;
    GoogleMap mGoogleMap;
    SupportMapFragment mFragment;
    Marker currLocationMarker;
    FrameLayout mapTouchLayer;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    public static final String[] POKEMON = new String[]{
            //NOTE: All Pokemon taking the first position means that the other pokemon will start indexed at 1
            "All Pokemon",
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
            "Nidoran♀",
            "Nidoran♂",
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
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.main_drawer);

        //Loads up the maps fragment
        mFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mFragment.getMapAsync(this);
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.draw_item, POKEMON));
        //set up drawerlistview with items and click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
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

        //Loads up the autocomplete text box
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, POKEMON);
//        pokemon_name = (AutoCompleteTextView)  findViewById(R.id.pokemon_name);
//        pokemon_name.setAdapter(adapter);

        //Handles Button Clicks, Sends POST request, unfocuses keyboard, and gives Toast message if valid, Gives Toast message if invalid
        ImageButton PingButton = (ImageButton) findViewById(R.id.ping_button);
        PingButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        //Zooms the Camera in, then starts the intent
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(mlatLng).zoom(20).build();
                        mGoogleMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition));
                        startActivity(new Intent(MapsActivity.this, Pop.class));
                    }
                }
        );
        //Zoom into your current location when you're ready to enter your pokemon name
//        pokemon_name.setOnClickListener(
//                new EditText.OnClickListener(){
//                    public void onClick(View v){
//                        CameraPosition cameraPosition = new CameraPosition.Builder()
//                                .target(mlatLng).zoom(20).build();
//                        mGoogleMap.animateCamera(CameraUpdateFactory
//                                .newCameraPosition(cameraPosition));
//                    }
//                }
//        );
//        //Set return to respond the same way as button click
//        //TODO: BugTest This
//        pokemon_name.setOnEditorActionListener(
//                new TextView.OnEditorActionListener() {
//                    @Override
//                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                        if(actionId== EditorInfo.IME_ACTION_DONE){
//                            sendMarker(v);
//                        }
//                        //Return true prevents the soft keyboard from going away, which we will handle elsewhere
//                        return true;
//                    }
//        });
//        /*Doesn't work
//        pokemon_name.setOnFocusChangeListener(new View.OnFocusChangeListener(){
//            @Override
//            public void onFocusChange(View v, boolean hasFocus){
//                //TODO: Move the input box and button downward to the bottom of the screen
//                if (!hasFocus) {
//                    hideSoftKeyboard(v);
//                }
//            }
//        });*/

        mapTouchLayer = (FrameLayout)findViewById(R.id.map_touch_layer);
        mapTouchLayer.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                hideSoftKeyboard(v);
                return false;
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        mGoogleMap = gMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);

        buildGoogleApiClient();

        mGoogleApiClient.connect();


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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear(); # Use This if you want to refresh the map upon no last location
            mlatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            //Specify qualities of the marker we are creating
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(mlatLng);
            markerOptions.title("You");
            //TODO: Set custom bitmap to trainer at your location
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            //Add the marker to the map
            currLocationMarker = mGoogleMap.addMarker(markerOptions);
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        //Focus on current location
        //TODO: Update default zoom on current location
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mlatLng));
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

        //place marker at current position
        //mGoogleMap.clear();
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        mlatLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(mlatLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        currLocationMarker = mGoogleMap.addMarker(markerOptions);

        //Toast.makeText(this,"Location Changed",Toast.LENGTH_SHORT).show();

        //Disabled zoom to current position
        //zoom to current position:
        /*CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(mlatLng).zoom(14).build();

        mGoogleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));*/

        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }
    @Override
    protected void onResume(){
        super.onResume();
        //Remove focus on soft keyboard
        findViewById(R.id.map).requestFocus();
        if (mlatLng != null){
            //Focus on current location
            //TODO: Update default zoom on current location
            //TODO: Lock screen or make it so that it zooms on something else, basically we can't zoom if mLatLng is null
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(mlatLng).zoom(20).build();
            mGoogleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
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