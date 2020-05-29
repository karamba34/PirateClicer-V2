package com.example.pirateclicker;

import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

public class BetterGunButtonClick  {

    //creating instance of shared preferences
    SharedPreferences mPreferences;
    private int numberOfBetterGuns;
    private int costOfBetterGuns;
    static int damage=0;
    int GunIsInPosesionIntegerValue;
    static String localinThreadWartoscWyniku;
    boolean betterGunIsInPosesion ;
    boolean damageDealingThreadIsStarted=false;
    int milions= 10000;
    double doubleCostOfNormalGun=milions;
    ProgressBar zamekHpBar;
    private ImageView zamek;
    private TextView wynik;
    TextView costOfGunTextViev;
    TextView numberOfGunsTextViev;
    MainActivity instance;
    SetNewZamek setNewZamek;


   // keys using for saving information about guns
    String COUNT_KEY_BETTER_GUN_NUMBER = "betterGunsNumber";
    String COUNT_KEY_BETTER_GUN_COST = "betterGunCost";
    String IF_GUN_WAS_BOUGHT="ifGunsWasBought";
    //View view;
    private static final String TAG = MainActivity.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BetterGunButtonClick(MainActivity instance, SharedPreferences mPreferences ) {

        this.instance=instance;
        this.mPreferences =mPreferences;
        this.zamekHpBar=this.instance.findViewById(R.id.hpBar);
        this.wynik=this.instance.findViewById(R.id.wynik);

       // Log.d(TAG, "=instance in bettergunClass =  " + instance);
        costOfGunTextViev=this.instance.findViewById(R.id.betterGunCostTextViev);
        numberOfGunsTextViev=this.instance.findViewById(R.id.betterGunNumberTextViev);
        this.zamek=this.instance.findViewById(R.id.zamekView);
        revivingGuns();
        setNewZamek=new SetNewZamek(instance,mPreferences);

    }

    public void buttonClick() {
        double danaPosrednia= Double.parseDouble(MainActivity.wartosc_wyniku);

        if (Integer.parseInt(MainActivity.wartosc_wyniku) >= costOfBetterGuns) {
            betterGunIsInPosesion=true;
            int betterGunIsInPosesionNotBollean=1;
            numberOfBetterGuns++;
            damage = damage(numberOfBetterGuns);
            // taking cost of gun from main score
            MainActivity.wartosc_wyniku=String.valueOf((int)danaPosrednia-costOfBetterGuns);

            doubleCostOfNormalGun = costOfBetterGuns*1.15;
            costOfBetterGuns= (int)doubleCostOfNormalGun;

            // settin calkulated numbers in Text Vievs
            wynik.setText(MainActivity.wartosc_wyniku);

            //number.setText(String.valueOf(numberOfBetterGuns));
            numberOfGunsTextViev.setText(String.valueOf(numberOfBetterGuns));
            //cost.setText(String.valueOf(costOfBetterGuns));

            costOfGunTextViev.setText(String.valueOf(costOfBetterGuns));

            // saving data to preferences

            SharedPreferences.Editor preferencesEditor = mPreferences.edit();

            preferencesEditor.putInt(COUNT_KEY_BETTER_GUN_NUMBER, numberOfBetterGuns);

            preferencesEditor.putInt(COUNT_KEY_BETTER_GUN_COST, costOfBetterGuns);

            preferencesEditor.putInt(IF_GUN_WAS_BOUGHT, betterGunIsInPosesionNotBollean);

            preferencesEditor.apply();

            if (!damageDealingThreadIsStarted){
                damageDealingThread();
            }
        }
        else{

            //Log.d(TAG, "przystanek gdy wyskoczy warunek else " );
        }

    }

    void reset(){
        betterGunIsInPosesion = false;
        damageDealingThreadIsStarted=false;
        costOfBetterGuns=milions;
        numberOfBetterGuns=0;
        damage = numberOfBetterGuns;
        numberOfGunsTextViev.setText(String.valueOf(numberOfBetterGuns));
        costOfGunTextViev.setText(String.valueOf(costOfBetterGuns));

    }
    void damageDealingThread(){
        new Thread(new Runnable() {

            @Override
            public void run() {

                while (betterGunIsInPosesion & MainActivity.zamekHpAmount>0) {

                        int localAmountOfDamage = damage(numberOfBetterGuns);
                        Log.d(TAG, "localAmountOfDamage BetterGuN = "+ localAmountOfDamage );
                        MainActivity.zamekHpAmount -= localAmountOfDamage;
                        zamekHpBar.setProgress(MainActivity.zamekHpAmount);
                        updateWynikForBetterGun( localAmountOfDamage);

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                }
            }

        }).start();
        damageDealingThreadIsStarted=true;

    }
    void revivingGuns(){

        // Restore preferences
        numberOfBetterGuns = mPreferences.getInt(COUNT_KEY_BETTER_GUN_NUMBER, 0);
        costOfBetterGuns = mPreferences.getInt(COUNT_KEY_BETTER_GUN_COST, milions);
        GunIsInPosesionIntegerValue = mPreferences.getInt(IF_GUN_WAS_BOUGHT, 0);
        costOfGunTextViev.setText(String.valueOf(costOfBetterGuns));
        numberOfGunsTextViev.setText(String.valueOf(numberOfBetterGuns));
        damage=damage(numberOfBetterGuns);
        if(GunIsInPosesionIntegerValue==1){
            betterGunIsInPosesion=true;
            damageDealingThread();
        }else {
            betterGunIsInPosesion=false;
        }

    }

    public void updateWynikForBetterGun( int localAmountOfDamage){

        MainActivity.wartosc_wyniku=String.valueOf(Integer.parseInt(MainActivity.wartosc_wyniku) + localAmountOfDamage);

        instance.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                wynik.setText(String.valueOf( MainActivity.wartosc_wyniku));

                if (MainActivity.zamekHpAmount<0){

                    SetNewZamek.builtNewZamek();

                }
            }
        });

    }
    int damage (int numberOfBetterGuns){

        return numberOfBetterGuns * 60;

    }



}
