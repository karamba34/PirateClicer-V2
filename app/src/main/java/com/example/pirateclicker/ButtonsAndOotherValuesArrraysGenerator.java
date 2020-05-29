package com.example.pirateclicker;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class ButtonsAndOotherValuesArrraysGenerator {

    private MainActivity instance;
    ImageButton gun1;
    ImageButton gun2;
    ImageButton gun3;
    ImageButton gun4;
    List <ImageButton> imageGunButtonArray =new ArrayList<>();
    Drawable gunDrawable1;
    Drawable gunDrawable2;
    Drawable gunDrawable3;
    Drawable gunDrawable4;
    List <Drawable> imageGunButtonDrawable =new ArrayList<>();
    TextView gun1NumberTextView;
    TextView gun2NumberTextView;
    TextView gun3NumberTextView;
    TextView gun4NumberTextView;
    List <TextView> gunNumberTextViewList=new ArrayList<>();
    TextView gun1CostTextView;
    TextView gun2CostTextView;
    TextView gun3CostTextView;
    TextView gun4CostTextView;
    List <TextView> gunCostTextViewList=new ArrayList<>();
    List <Integer>gunNumberList=new ArrayList<>();
    List <Integer>gunCostList=new ArrayList<>();
    List <Integer>gunInitialCostList=new ArrayList<>();
    List <Integer>gunBaseDamageList=new ArrayList<>();
    List <Boolean>damageDealingThreadIsStartredList=new ArrayList<>();
    List<Integer> damageList=new ArrayList<>();
    List<Integer> currentDamageDealingList=new ArrayList<>();





    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ButtonsAndOotherValuesArrraysGenerator(MainActivity instance) {
       this.instance = instance;
       gun1= this.instance.findViewById(R.id.normalGunButton);
       gun2= this.instance.findViewById(R.id.betterGunButton);
       gun3= this.instance.findViewById(R.id.imageButton2);
       gun4= this.instance.findViewById(R.id.imageButton);
       imageGunButtonArray.add(gun1);
       imageGunButtonArray.add(gun2);
       imageGunButtonArray.add(gun3);
       imageGunButtonArray.add(gun4);

       gunDrawable1=this.instance.getResources().getDrawable(R.drawable.normall_gun);
       gunDrawable2=this.instance.getResources().getDrawable(R.drawable.better_gun);
       gunDrawable3=this.instance.getResources().getDrawable(R.drawable.even_better_gun);
       gunDrawable4=this.instance.getResources().getDrawable(R.drawable.super_gun);
       imageGunButtonDrawable.add(gunDrawable1);
       imageGunButtonDrawable.add(gunDrawable2);
       imageGunButtonDrawable.add(gunDrawable3);
       imageGunButtonDrawable.add(gunDrawable4);

       gun1NumberTextView= this.instance.findViewById(R.id.numberOfNormalGunsTextViev);
       gun2NumberTextView= this.instance.findViewById(R.id.betterGunNumberTextViev);
       gun3NumberTextView= this.instance.findViewById(R.id.Gun3NumberTextView);
       gun4NumberTextView= this.instance.findViewById(R.id.Gun4NumberTextView2);
       gunNumberTextViewList.add(gun1NumberTextView);
       gunNumberTextViewList.add(gun2NumberTextView);
       gunNumberTextViewList.add(gun3NumberTextView);
       gunNumberTextViewList.add(gun4NumberTextView);

       gun1CostTextView=this.instance.findViewById(R.id.costOfNormallGunTextViev);
       gun2CostTextView=this.instance.findViewById(R.id.betterGunCostTextViev);
       gun3CostTextView=this.instance.findViewById(R.id.Gun3CostTextView2);
       gun4CostTextView=this.instance.findViewById(R.id.Gun4CostTextView);
       gunCostTextViewList.add(gun1CostTextView);
       gunCostTextViewList.add(gun2CostTextView);
       gunCostTextViewList.add(gun3CostTextView);
       gunCostTextViewList.add(gun4CostTextView);

     // setting initial cost and damage of guns
        int initialCost=1000;
        int initialDamage=20;

       for(int i=0;i<=imageGunButtonArray.size();i++){

           damageDealingThreadIsStartredList.add(false);
           gunNumberList.add(0);
           gunCostList.add(initialCost);
           gunInitialCostList.add(initialCost);
           initialCost*=10;
           gunBaseDamageList.add(initialDamage);
           damageList.add(initialDamage);
           currentDamageDealingList.add(initialDamage);
           initialDamage*=3;


       }

    }


}
