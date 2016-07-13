package com.peterlzhou.pokedex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by peterlzhou on 7/12/16.
 */
//TODO: Change this into a dialog maybe?

public class Pop extends Activity{
    Calendar c;
    Capture mCapture;
    long mCaptureMilliseconds;
    private static final String[] POKEMON = new String[]{
            "Bulbasaur",
            "Ivysaur",
            "Venusaur",
            "Charmander",
            "Charmeleon",
            "Charizard",
            "Squirtle",
            "Wartortle",
            "Blastoise",
            "Caterpie",
            "Metapod",
            "Butterfree",
            "Weedle",
            "Kakuna",
            "Beedrill",
            "Pidgey",
            "Pidgeotto",
            "Pidgeot",
            "Rattata",
            "Raticate",
            "Spearow",
            "Fearow",
            "Ekans",
            "Arbok",
            "Pikachu",
            "Raichu",
            "Sandshrew",
            "Sandslash",
            "Nidoran♀",
            "Nidorina",
            "Nidoqueen",
            "Nidoran♂",
            "Nidorino",
            "Nidoking",
            "Clefairy",
            "Clefable",
            "Vulpix",
            "Ninetales",
            "Jigglypuff",
            "Wigglytuff",
            "Zubat",
            "Golbat",
            "Oddish",
            "Gloom",
            "Vileplume",
            "Paras",
            "Parasect",
            "Venonat",
            "Venomoth",
            "Diglett",
            "Dugtrio",
            "Meowth",
            "Persian",
            "Psyduck",
            "Golduck",
            "Mankey",
            "Primeape",
            "Growlithe",
            "Arcanine",
            "Poliwag",
            "Poliwhirl",
            "Poliwrath",
            "Abra",
            "Kadabra",
            "Alakazam",
            "Machop",
            "Machoke",
            "Machamp",
            "Bellsprout",
            "Weepinbell",
            "Victreebel",
            "Tentacool",
            "Tentacruel",
            "Geodude",
            "Graveler",
            "Golem",
            "Ponyta",
            "Rapidash",
            "Slowpoke",
            "Slowbro",
            "Magnemite",
            "Magneton",
            "Farfetch'd",
            "Doduo",
            "Dodrio",
            "Seel",
            "Dewgong",
            "Grimer",
            "Muk",
            "Shellder",
            "Cloyster",
            "Gastly",
            "Haunter",
            "Gengar",
            "Onix",
            "Drowzee",
            "Hypno",
            "Krabby",
            "Kingler",
            "Voltorb",
            "Electrode",
            "Exeggcute",
            "Exeggutor",
            "Cubone",
            "Marowak",
            "Hitmonlee",
            "Hitmonchan",
            "Lickitung",
            "Koffing",
            "Weezing",
            "Rhyhorn",
            "Rhydon",
            "Chansey",
            "Tangela",
            "Kangaskhan",
            "Horsea",
            "Seadra",
            "Goldeen",
            "Seaking",
            "Staryu",
            "Starmie",
            "Mr. Mime",
            "Scyther",
            "Jynx",
            "Electabuzz",
            "Magmar",
            "Pinsir",
            "Tauros",
            "Magikarp",
            "Gyarados",
            "Lapras",
            "Ditto",
            "Eevee",
            "Vaporeon",
            "Jolteon",
            "Flareon",
            "Porygon",
            "Omanyte",
            "Omastar",
            "Kabuto",
            "Kabutops",
            "Aerodactyl",
            "Snorlax",
            "Articuno",
            "Zapdos",
            "Moltres",
            "Dratini",
            "Dragonair",
            "Dragonite",
            "Mewtwo",
            "Mew"
    };
    AutoCompleteTextView pokemon_name;
    ImageButton submit_button;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        getWindow().setLayout((int)(width * .8),(int)(height * .2));

        //Loads up the autocomplete text box
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, POKEMON);
        pokemon_name = (AutoCompleteTextView)  findViewById(R.id.pokemon_name);
        pokemon_name.setAdapter(adapter);
        pokemon_name.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if(actionId== EditorInfo.IME_ACTION_DONE){
                            sendMarker(v);
                        }
                        //Return true prevents the soft keyboard from going away, which we will handle elsewhere
                        return true;
                    }
        });
        //Doesn't work
        pokemon_name.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if (!hasFocus) {
                    hideSoftKeyboard(v);
                }
            }
        });
        submit_button = (ImageButton) findViewById(R.id.submit_button);
        submit_button.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        sendMarker(v);
                    }
                }
        );
    }


    public void sendMarker(View v){
        pokemon_name = (AutoCompleteTextView) findViewById(R.id.pokemon_name);
        String pokemonstring = pokemon_name.getText().toString();
        //Correct Pokemon, send the POST request, unfocus the keyboard, and give a toast or some popup message
        if (validPokemon(pokemonstring)){
            Toast.makeText(this,"Valid Pokemon!",Toast.LENGTH_SHORT).show();
            c = Calendar.getInstance();
            mCaptureMilliseconds = c.getTimeInMillis();


        }
        //Incorrect pokemon, give a toast
        else{
            Toast.makeText(this,"Invalid Pokemon!",Toast.LENGTH_SHORT).show();
        }

    }

    protected boolean validPokemon(String a){
        //Case insensitive check
        for (String s : POKEMON){
            if  (a.equalsIgnoreCase(s)){
                return true;
            }
        }
        return false;
    }
    //Use this method to hide the keyboard
    //Doesn't work
    //TODO: Hide the keyboard when Pop Activity exits, turn Pop into a dialog?
    public void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        //Toast.makeText(this,"Invalid Pokemon!",Toast.LENGTH_SHORT).show();
    }

}


