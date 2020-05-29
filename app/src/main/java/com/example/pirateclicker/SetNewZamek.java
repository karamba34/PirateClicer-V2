package com.example.pirateclicker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;


public class SetNewZamek {
    MainActivity instance;
    static SharedPreferences mPreferences;
    Context context;
    static Drawable zamek1;
    static Drawable zamek2;
    private static ImageView zamekImageViev;
    private static ProgressBar zamekHpBar;
    private static String COUNT_KEY_WITCH_CASTLE_IS_ACTIVE="witchCastleIsActive";
    private static int milions= 1000000;
    private static int witchCastleIsActive;
    private static final String TAG = MainActivity.class.getSimpleName();

   // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SetNewZamek(MainActivity instance, SharedPreferences mPreferences) {

        this.instance=instance;
        zamek1=this.instance.getResources().getDrawable(R.drawable.zamek);
        zamek2=this.instance.getResources().getDrawable(R.drawable.zamek2);
        zamekImageViev=this.instance.findViewById(R.id.zamekView);
        zamekHpBar=this.instance.findViewById(R.id.hpBar);
        SetNewZamek.mPreferences =mPreferences;

        Log.d(TAG, "przed warunkami zamekImageViev.getDrawable() = " +zamekImageViev.getDrawable() );
        Log.d(TAG, "zamek1 z klasy setZamek przed warunkami = " +zamek1 );

    }
    public static void builtNewZamek(){
        if(zamekImageViev.getDrawable()==zamek1) {
            Log.d(TAG, "zamekImageViev.getDrawable() z klasy setZamek else  IN IF = " +zamekImageViev.getDrawable() );
            zamekImageViev.setImageDrawable(zamek2);
            witchCastleIsActive=2;
        }else{
            Log.d(TAG, "zamekImageViev.getDrawable() z klasy setZamek else = " +zamekImageViev.getDrawable() );
            Log.d(TAG, "zamek1 z klasy setZamek else = " +zamek1 );
            zamekImageViev.setImageDrawable(zamek1);
            witchCastleIsActive=1;

        }
        milions*=2;
        Log.d(TAG, "milions z klasy setZamek  = " +milions );
        MainActivity.zamekHpAmount = milions;
        zamekHpBar.setMax(milions);
        zamekHpBar.setProgress(MainActivity.zamekHpAmount);

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putInt(COUNT_KEY_WITCH_CASTLE_IS_ACTIVE, witchCastleIsActive);
        preferencesEditor.apply();

    }

    void reset(){

        zamekImageViev.setImageDrawable(zamek1);
        witchCastleIsActive=1;
        milions=1000000;
        MainActivity.zamekHpAmount=milions;
        zamekHpBar.setMax(milions);
        zamekHpBar.setProgress(milions);
    }

}
