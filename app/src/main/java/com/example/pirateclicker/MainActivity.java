package com.example.pirateclicker;



import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity{


    TextView wynik;
    TextView amountOFDamageTextViev;
    ProgressBar zamekHpBar;
    ImageView zamek;

    public  static String wartosc_wyniku = "0";
    private static final String TAG = MainActivity.class.getSimpleName();
    //creating instance of shared preferences
     SharedPreferences mPreferences;
    private String sharedPrefFile ="com.example.pirateclicker";
    // Key for current count
    private final String COUNT_KEY = "count";
    private final String COUNT_KEY_ZAMEK_HP_AMOUNT = "zamekHpAmount";
    private final String COUNT_KEY_AMOUNT_OF_DAMAGE = "amountOfDamage";
    private final String WITCH_CASTEL_IS_ACTIVE="witchCastleIsActive";

    // dane początkowe
    static int milions= 1000000;
    static int zamekHpAmount= milions;
    int amountOfDamage=20;
    int witchCastleIsActive;

    // declaring explosion objects
    AnimationDrawable explosionAnimation;
    AnimationDrawable explosionAnimation2;
    AnimationDrawable explosionAnimation3;
    // declaring button object
    GunButtonGenerator gunButtonGenerator;


    static SetNewZamek setNewZamek;


    private Handler mHandler = new Handler();
    Context context;

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        wynik = findViewById(R.id.wynik);

        zamekHpBar = findViewById(R.id.hpBar);
        zamek=findViewById(R.id.zamekView);


        // setting drawable for castle image;
        Drawable zamek1=this.getResources().getDrawable(R.drawable.zamek);
        Drawable zamek2=this.getResources().getDrawable(R.drawable.zamek2);
        zamek.setImageDrawable(zamek1);

        mPreferences = getSharedPreferences(
                sharedPrefFile, MODE_PRIVATE);

        // setting zamekHpprogress bar max value at 1 milion
        zamekHpBar.setMax(zamekHpAmount);

        // Restore preferences
        wartosc_wyniku = String.valueOf(mPreferences.getInt(COUNT_KEY, 0));

        zamekHpAmount = mPreferences.getInt(COUNT_KEY_ZAMEK_HP_AMOUNT, milions);

        witchCastleIsActive = mPreferences.getInt(WITCH_CASTEL_IS_ACTIVE, 1);

        if (witchCastleIsActive==1){
            zamek.setImageDrawable(zamek1);
        }else{
            zamek.setImageDrawable(zamek2);
        }

        setNewZamek=new SetNewZamek (this,mPreferences);

        // sending information about numer, cost, text vievs to better gun class and sending instance of main activity tu betterGunButtonClic classs

        gunButtonGenerator=new GunButtonGenerator(this,mPreferences);


        // setting initial value after revaving the aplication
        wynik.setText(wartosc_wyniku);
        zamekHpBar.setProgress(zamekHpAmount);

        // creating instance for view that hendels animation
        //setting xml file that contains animation
        //setting view that handels animation to invisible

        ImageView fortExplosion= findViewById(R.id.explosionViev);
        ImageView fortExplosion2= findViewById(R.id.explosionViev2);
        ImageView fortExplosion3= findViewById(R.id.explosionView3);
        amountOFDamageTextViev= findViewById(R.id.damageAmount);

        fortExplosion.setBackgroundResource(R.drawable.explosion);
        fortExplosion2.setBackgroundResource(R.drawable.explosion);
        fortExplosion3.setBackgroundResource(R.drawable.explosion);

        fortExplosion.setVisibility(View.INVISIBLE);
        fortExplosion2.setVisibility(View.INVISIBLE);
        fortExplosion3.setVisibility(View.INVISIBLE);
        amountOFDamageTextViev.setVisibility(View.INVISIBLE);

        explosionAnimation = (AnimationDrawable) fortExplosion.getBackground();
        explosionAnimation2 = (AnimationDrawable) fortExplosion2.getBackground();
        explosionAnimation3 = (AnimationDrawable) fortExplosion3.getBackground();
    }
    public void onButtonClick(View v) {

        ImageView fortExplosion= findViewById(R.id.explosionViev);
        ImageView fortExplosion2= findViewById(R.id.explosionViev2);
        ImageView fortExplosion3= findViewById(R.id.explosionView3);
        amountOFDamageTextViev= findViewById(R.id.damageAmount);
        zamek=findViewById(R.id.zamekView);
        zamekHpBar = findViewById(R.id.hpBar);


        Log.d(TAG, " wartosc_wyniku in onButtonClick=  " + wartosc_wyniku);
        double  danaPosrednia=Double.parseDouble(wartosc_wyniku);
        int dana = (int)danaPosrednia;
        amountOfDamage=20;
        for(int i=0;i<gunButtonGenerator.currentDamageDealingList.size();i++){

        amountOfDamage += gunButtonGenerator.currentDamageDealingList.get(i)*15;
        }
        dana += amountOfDamage;

        wartosc_wyniku = String.valueOf(dana);

        wynik.setText(wartosc_wyniku);

        fortExplosion.setVisibility(View.VISIBLE);
        fortExplosion2.setVisibility(View.VISIBLE);
        fortExplosion3.setVisibility(View.VISIBLE);
        // starting explosion animation in 3 diferent instances
        explosionAnimation.start();
        explosionAnimation.setOneShot(true);
        explosionAnimation.setVisible(true, true);

        explosionAnimation2.start();
        explosionAnimation2.setOneShot(true);
        explosionAnimation2.setVisible(true, true);

        explosionAnimation3.start();
        explosionAnimation3.setOneShot(true);
        explosionAnimation3.setVisible(true, true);

        // creating new tread for handling zamek hp destroy animation

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (zamekHpAmount > 0) {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            zamekHpAmount -= amountOfDamage;
                            zamekHpBar.setProgress(zamekHpAmount);

                        }
                    });

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            amountOFDamageTextViev.setText(String.valueOf(amountOfDamage));
                            amountOFDamageTextViev.setVisibility(View.VISIBLE);

                        }
                    });
                }
                else {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            SetNewZamek.builtNewZamek();

                        }
                    });
                    //SetNewZamek.builtNewZamek();
                }
            }
        }).start();
        // saving data to preferences
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putInt(COUNT_KEY, Integer.parseInt(wartosc_wyniku));
        preferencesEditor.putInt(COUNT_KEY_ZAMEK_HP_AMOUNT, zamekHpAmount);
        preferencesEditor.putInt(COUNT_KEY_AMOUNT_OF_DAMAGE, amountOfDamage);
        preferencesEditor.apply();

        // sending new walu of wartośc wyniku to BetterGunButtonClic class

        BetterGunButtonClick.localinThreadWartoscWyniku=wartosc_wyniku;

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onResetButtonClick(View v) {

        int localAmountOfdamage = 20;
        amountOfDamage=20;

        // set new value to wynik TextView
        wartosc_wyniku = String.valueOf(0);
        wynik.setText(wartosc_wyniku);
        amountOFDamageTextViev.setText(String.valueOf(localAmountOfdamage));
        setNewZamek.reset();
        gunButtonGenerator.reset();

        // Clear preferences
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.clear();
        preferencesEditor.apply();

    }

    public void onNormalGunButtonClick(View v) {


         int buttonID=v.getId();
         gunButtonGenerator.buttonClick(buttonID);


    }

    public void onBetterGunButtonClick(View v) {




        gunButtonGenerator.buttonClick(v.getId());


    }
    public void onEvenBetterGunButtonClick(View v) {




        gunButtonGenerator.buttonClick(v.getId());


    }
    public void onGun4ButtonClick(View v) {




        gunButtonGenerator.buttonClick(v.getId());


    }

    @Override
    protected void onPause(){
        super.onPause();

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putInt(COUNT_KEY, Integer.parseInt(wartosc_wyniku));
        preferencesEditor.apply();

        explosionAnimation.stop();

    }

}
