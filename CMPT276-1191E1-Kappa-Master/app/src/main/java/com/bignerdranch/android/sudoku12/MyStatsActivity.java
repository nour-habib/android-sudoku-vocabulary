package com.bignerdranch.android.sudoku12;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//public class MyStatsActivity extends MainActivity {
//    TextView output;
//    public String wordM = new String();
//    public Map<String, int[]> myWords_mywords = new HashMap<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_mywords);
//
//        TextView text_word = (TextView) findViewById(R.id.wordlist);
//        //text_word.setTypeface(null, Typeface.BOLD);
//        myWords_mywords = gethashmap();
//        if(myWords_mywords.isEmpty())
//            text_word.setText("Word  correct ratio\n" + "No data");
//        else {
//            String[] myWordString = new String[myWords_mywords.size()];
////        for(int i = 0; i < myWords_mywords.size(); i++){
////            myWordString[i] = myWords_mywords[]
////        }
//            int i = 0;
//            myWordString[0] = " ";
//            for (Map.Entry<String, int[]> entry : myWords_mywords.entrySet()) {
//                myWordString[i] = entry.getKey() + "\n";
//                i++;
//                // Do things with the list
//            }
//            text_word.setText("Word  correct ratio\n" + Arrays.toString(myWordString));
//        }
//      //  Log.d("myworrddds", Arrays.toString(myWordString));
//    }
//}

public class MyStatsActivity extends Activity{
    TextView output;
    public String wordM = new String();
    public Map<String, int[]> myWords_mywords = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywords);

        TextView text_word = (TextView) findViewById(R.id.high_score);
        TextView text_word2 = (TextView) findViewById(R.id.words_text);
        //text_word.setTypeface(null, Typeface.BOLD);
        //myWords_mywords = gethashmap();
       // if(myWords_mywords.isEmpty())
           // text_word.setText("Word  correct ratio\n" + "No data");
       // else {
           // String[] myWordString = new String[myWords_mywords.size()];
//        for(int i = 0; i < myWords_mywords.size(); i++){
//            myWordString[i] = myWords_mywords[]
//        }
//            int i = 0;
//            myWordString[0] = " ";
//            for (Map.Entry<String, int[]> entry : myWords_mywords.entrySet()) {
//                myWordString[i] = entry.getKey() + "\n";
//                i++;
//                // Do things with the list
//            }
//            text_word.setText("Word  correct ratio\n" + Arrays.toString(myWordString));
//        }
        //  Log.d("myworrddds", Arrays.toString(myWordString));


//        SharedPreferences sharedPreferences2 = getSharedPreferences("Words", Context.MODE_PRIVATE);
//        String wordlist = sharedPreferences2.getString("words", "");
//        text_word2.setText(wordlist);


        SharedPreferences sharedPreferences = getSharedPreferences("GameScore", Context.MODE_PRIVATE);
        int gameScore = sharedPreferences.getInt("gameScore", 0);
        text_word.setText(Integer.toString(gameScore));
    }
}