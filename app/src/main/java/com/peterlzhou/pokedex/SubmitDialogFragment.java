package com.peterlzhou.pokedex;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by peterlzhou on 7/15/16.
 */
public class SubmitDialogFragment extends DialogFragment{
    Calendar c;
    Capture mCapture;
    long mCaptureMilliseconds;
    AutoCompleteTextView pokemon_name;
    ImageButton submit_button;
    @Override
    public Dialog onCreateDialog(Bundle savedInstance){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_submit, null);
        AutoCompleteTextView actv = (AutoCompleteTextView) view.findViewById(R.id.pokemon_name);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, MapsActivity.POKEMON);
        actv.setAdapter(adapter);
        actv.setOnEditorActionListener(
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
        builder.setView(view)
                .setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.gotcha, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id){
                        //TODO: Thank you message
                        sendMarker(view);
                        SubmitDialogFragment.this.getDialog().cancel();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id){
                        SubmitDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();

    }
    public boolean sendMarker(View v){
        pokemon_name = (AutoCompleteTextView) v.findViewById(R.id.pokemon_name);
        String pokemonstring = pokemon_name.getText().toString();
        //Correct Pokemon, send the POST request, unfocus the keyboard, and give a toast or some popup message
        if (validPokemon(pokemonstring)){
            Toast.makeText(getActivity(),"Thanks for submitting!",Toast.LENGTH_LONG).show();
            c = Calendar.getInstance();
            mCaptureMilliseconds = c.getTimeInMillis();
            mCapture = new Capture();
            mCapture.latitude = MapsActivity.mlatLng.latitude;
            mCapture.longitude = MapsActivity.mlatLng.longitude;
            mCapture.time = mCaptureMilliseconds;
            //Dummy variable
            mCapture.pokemon_name = pokemonstring;
            PostServer makePost = new PostServer();
            makePost.execute(mCapture);
            return true;

        }
        //Incorrect pokemon, give a toast
        else{
            Toast.makeText(getActivity(),"Invalid Pokemon!",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    protected boolean validPokemon(String a){
        //Case insensitive check
        for (String s : MapsActivity.POKEMON){
            if  (a.equalsIgnoreCase(s)){
                return true;
            }
        }
        return false;
    }
}
