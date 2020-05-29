package com.example.pirateclicker;

import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;



public class NormalGunButtonClick {
    //creating instance of shared preferences
    SharedPreferences mPreferences;
    private int milions= 1000;
    private int numberOfBetterGuns;
    private int costOfBetterGuns=milions;
    static int damage=0;

    private boolean betterGunIsInPosesion ;
    private boolean damageDealingThreadIsStarted=false;

    private ProgressBar zamekHpBar;
    private ImageView zamek;
    private TextView wynik;
    private TextView costOfGunTextViev;
    private TextView numberOfGunsTextViev;
    private MainActivity instance;
    private ButtonsAndOotherValuesArrraysGenerator valueListGenerator;




    // keys using for saving information about guns
    String NORMAL_GUN_NUMBER = "normalGunsNumber";
    //View view;
    private static final String TAG = MainActivity.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NormalGunButtonClick(MainActivity instance, SharedPreferences mPreferences ) {

        this.instance=instance;
        this.mPreferences =mPreferences;
        this.zamekHpBar=this.instance.findViewById(R.id.hpBar);
        this.wynik=this.instance.findViewById(R.id.wynik);

        //Log.d(TAG, "=instance in bettergunClass =  " + instance);
        costOfGunTextViev=this.instance.findViewById(R.id.costOfNormallGunTextViev);
        numberOfGunsTextViev=this.instance.findViewById(R.id.numberOfNormalGunsTextViev);
        this.zamek=this.instance.findViewById(R.id.zamekView);
        revivingGuns();
        valueListGenerator=new ButtonsAndOotherValuesArrraysGenerator(this.instance);

    }

    public void buttonClick() {
        double danaPosrednia= Double.parseDouble(MainActivity.wartosc_wyniku);

        if (Integer.parseInt(MainActivity.wartosc_wyniku) >= costOfBetterGuns) {
            betterGunIsInPosesion=true;
            numberOfBetterGuns++;
            damage = damage(numberOfBetterGuns);

            // taking cost of gun from main score
            MainActivity.wartosc_wyniku=String.valueOf((int)danaPosrednia-costOfBetterGuns);


            // increasing costa of guns
            costOfBetterGuns *= 1.15;

            // settin calkulated numbers in Text Vievs
            wynik.setText(MainActivity.wartosc_wyniku);

            numberOfGunsTextViev.setText(String.valueOf(numberOfBetterGuns));

            costOfGunTextViev.setText(String.valueOf(costOfBetterGuns));
            Log.d(TAG, "damageDealingThreadIsStarted =  " + damageDealingThreadIsStarted);
            if (!damageDealingThreadIsStarted){
                damageDealingThread();
            }


            // saving data to preferences

            SharedPreferences.Editor preferencesEditor = mPreferences.edit();

            preferencesEditor.putInt(NORMAL_GUN_NUMBER, numberOfBetterGuns);

            preferencesEditor.apply();

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
        damage=numberOfBetterGuns;
        numberOfGunsTextViev.setText(String.valueOf(numberOfBetterGuns));
        costOfGunTextViev.setText(String.valueOf(costOfBetterGuns));

    }
    void damageDealingThread(){

        new Thread(new Runnable() {

            @Override
            public void run() {

                while (betterGunIsInPosesion & MainActivity.zamekHpAmount>0) {
                    int localAmountOfDamage = damage(numberOfBetterGuns);

                    Log.d(TAG, "localAmountOfDamage = "+ localAmountOfDamage );
                    MainActivity.zamekHpAmount -= localAmountOfDamage;
                    zamekHpBar.setProgress(MainActivity.zamekHpAmount);
                    updateWynikForBetterGun(localAmountOfDamage);

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
        numberOfBetterGuns = mPreferences.getInt(NORMAL_GUN_NUMBER, 0);
        if (numberOfBetterGuns>= 1) {

            costOfBetterGuns *= Math.pow(1.15,numberOfBetterGuns);

        }else{

            costOfBetterGuns =  milions;

        }
        costOfGunTextViev.setText(String.valueOf(costOfBetterGuns));
        numberOfGunsTextViev.setText(String.valueOf(numberOfBetterGuns));
        damage=damage(numberOfBetterGuns);
        Log.d(TAG, "numberOfBetterGuns = "+ numberOfBetterGuns  );
        if(numberOfBetterGuns>0){
            betterGunIsInPosesion=true;
            damageDealingThread();
            Log.d(TAG, "numberOfBetterGuns if thread is started = "+ numberOfBetterGuns  );
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

        return numberOfBetterGuns * 20;

    }



}

