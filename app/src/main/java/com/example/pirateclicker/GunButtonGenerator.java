package com.example.pirateclicker;

import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;


public class GunButtonGenerator {
    //creating instance of shared preferences
    SharedPreferences mPreferences;
    private boolean damageDealingThreadIsStarted = false;

    private TextView wynik;
    private ProgressBar zamekHpBar;
    private MainActivity instance;
    private ButtonsAndOotherValuesArrraysGenerator valueListGenerator;
    List<Integer> currentDamageDealingList=new ArrayList<>();




    // keys using for saving information about guns

    //View view;
    private static final String TAG = MainActivity.class.getSimpleName();

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GunButtonGenerator(MainActivity instance, SharedPreferences mPreferences) {

        this.instance = instance;
        this.mPreferences = mPreferences;
        this.wynik = this.instance.findViewById(R.id.wynik);
        this.zamekHpBar=this.instance.findViewById(R.id.hpBar);

        valueListGenerator = new ButtonsAndOotherValuesArrraysGenerator(this.instance);

        for (int i = 0; i <valueListGenerator.imageGunButtonArray.size() ; i++) {

            revivingGuns(i);
            currentDamageDealingList.add(0);
        }

    }

      public void buttonClick(int buttonID) {
        double danaPosrednia = Double.parseDouble(MainActivity.wartosc_wyniku);

        ImageButton button = this.instance.findViewById(buttonID);
        for (int i = 0; i < valueListGenerator.imageGunButtonArray.size(); i++) {
            if (button == valueListGenerator.imageGunButtonArray.get(i)) {

                if (Integer.parseInt(MainActivity.wartosc_wyniku) >= valueListGenerator.gunCostList.get(i)) {
                    Integer numberOfguns = valueListGenerator.gunNumberList.get(i);
                    Integer costOfGun= valueListGenerator.gunCostList.get(i);
                    TextView numberOfGunsTextView = valueListGenerator.gunNumberTextViewList.get(i);
                    TextView costOfGunsTextView = valueListGenerator.gunCostTextViewList.get(i);
                    Integer damageForSelectedGun = valueListGenerator.gunBaseDamageList.get(i);

                    numberOfguns ++;
                    valueListGenerator.gunNumberList.set(i,numberOfguns);
                    valueListGenerator.damageList.set(i,damage(numberOfguns,damageForSelectedGun)) ;

                    // taking cost of gun from main score

                    MainActivity.wartosc_wyniku = String.valueOf((int) danaPosrednia - costOfGun);

                    // increasing costa of guns
                    double doubleCostofGun = costOfGun;
                    doubleCostofGun *= 1.15;
                    Log.d(TAG, "costOfGun in GUN BUTTON GENERATOR " + costOfGun);
                    costOfGun = Integer.parseInt(String.valueOf(Math.round(doubleCostofGun)));
                    Log.d(TAG, "costOfGun in GUN BUTTON GENERATOR PO Zaokrągleniu " + costOfGun);
                    valueListGenerator.gunCostList.set(i,costOfGun);

                    // settin calkulated numbers in Text Vievs
                    wynik.setText(MainActivity.wartosc_wyniku);

                    numberOfGunsTextView.setText(String.valueOf(valueListGenerator.gunNumberList.get(i)));

                    costOfGunsTextView.setText(String.valueOf(valueListGenerator.gunCostList.get(i)));

                    if (!valueListGenerator.damageDealingThreadIsStartredList.get(i)) {

                        damageDealingThread(i);
                    }

                    // creating key for gun witch was clicked
                    String GUN_NUMBER = "gun_%d_Number";
                    String gunKey=String.format(GUN_NUMBER,i);


                    // saving data to preferences

                    SharedPreferences.Editor preferencesEditor = mPreferences.edit();

                    preferencesEditor.putInt(gunKey,numberOfguns);

                    preferencesEditor.apply();

                }

            }

           Log.d(TAG, "buttonID in GUN BUTTON GENERATOR  " + buttonID);

        }

      }


        void reset () {


            for (int i = 0; i < valueListGenerator.imageGunButtonArray.size(); i++) {
                TextView numberOfGunsTextView = valueListGenerator.gunNumberTextViewList.get(i);
                TextView costOfGunsTextView = valueListGenerator.gunCostTextViewList.get(i);

                valueListGenerator.gunNumberList.add(i,0);
                valueListGenerator.gunCostList.add(i,valueListGenerator.gunInitialCostList.get(i));
                valueListGenerator.damageDealingThreadIsStartredList.add(i,false);
                valueListGenerator.damageList.add(i,valueListGenerator.gunBaseDamageList.get(i));
                currentDamageDealingList.set(i,0);
                numberOfGunsTextView.setText(String.valueOf(valueListGenerator.gunNumberList.get(i)));
                costOfGunsTextView.setText(String.valueOf(valueListGenerator.gunCostList.get(i)));
            }




        }
        void damageDealingThread (final int i) {

            new Thread(new Runnable() {

                @Override
                public void run() {


                    Log.d(TAG, "weszło do petli =  " + damageDealingThreadIsStarted);
                    Log.d(TAG, "valueListGenerator.gunNumberList.get(i) =  " + valueListGenerator.gunNumberList.get(i));
                    while ( valueListGenerator.gunNumberList.get(i)>0 & MainActivity.zamekHpAmount > 0) {
                        Integer numberOfguns = valueListGenerator.gunNumberList.get(i);
                        Integer damageForSelectedGun = valueListGenerator.gunBaseDamageList.get(i);
                        int localAmountOfDamage = damage(numberOfguns,damageForSelectedGun);
                        currentDamageDealingList.set(i,localAmountOfDamage);
                        Log.d(TAG, "localAmountOfDamage = " + localAmountOfDamage);
                        MainActivity.zamekHpAmount -= localAmountOfDamage;
                        zamekHpBar.setProgress(MainActivity.zamekHpAmount);
                        updateWynik(localAmountOfDamage);

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }


                }

            }).start();
            valueListGenerator.damageDealingThreadIsStartredList.set(i,true);

        }
        void revivingGuns (int i) {

            Integer numberOfguns;
            Integer costOfGun = valueListGenerator.gunInitialCostList.get(i);

            TextView numberOfGunsTextView = valueListGenerator.gunNumberTextViewList.get(i);
            TextView costOfGunTextView = valueListGenerator.gunCostTextViewList.get(i);

            // Restore preferences

                // creating key for gun witch was clicked
                String GUN_NUMBER = "gun_%d_Number";
                String gunKey=String.format(GUN_NUMBER,i);

                numberOfguns = mPreferences.getInt(gunKey, 0);
                if (numberOfguns >= 1) {

                    double doubleCostOfGun=costOfGun;

                    doubleCostOfGun *= Math.pow(1.15, numberOfguns);
                    Log.d(TAG, "costOfGun in reviving gun method przed  = " + costOfGun);
                    costOfGun=Integer.parseInt(String.valueOf( Math.round(doubleCostOfGun)));
                    Log.d(TAG, "costOfGun in reviving gun method PO = " + costOfGun);
                    valueListGenerator.gunCostList.add(i,costOfGun);
                    valueListGenerator.gunNumberList.add(i,numberOfguns);



                } else {

                   // costOfGun = valueListGenerator.gunInitialCostList.get(i);
                    valueListGenerator.gunCostList.add(i,costOfGun);

                }
                costOfGunTextView.setText(String.valueOf(costOfGun));
                numberOfGunsTextView.setText(String.valueOf(numberOfguns));

                Log.d(TAG, "numberOfBetterGuns = " + numberOfguns);
                if (numberOfguns > 0) {

                    damageDealingThread(i);
                    Log.d(TAG, "numberOfBetterGuns if thread is started = " + numberOfguns);
                }



        }

        public void updateWynik( int localAmountOfDamage){

            MainActivity.wartosc_wyniku = String.valueOf(Integer.parseInt(MainActivity.wartosc_wyniku) + localAmountOfDamage);

            instance.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    wynik.setText(String.valueOf(MainActivity.wartosc_wyniku));

                    if (MainActivity.zamekHpAmount < 0) {

                        SetNewZamek.builtNewZamek();

                    }
                }
            });


        }

        int damage( Integer numberOfBetterGuns,Integer gunDamage){

            return numberOfBetterGuns * gunDamage;

        }



}



