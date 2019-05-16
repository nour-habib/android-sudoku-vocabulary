package com.bignerdranch.android.sudoku12;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import android.app.Dialog;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class MainMenuActivity extends AppCompatActivity {

    String wordpairs;

    final Context context = this;
    private Button button;
    private Button myWordsButton;
    private RadioGroup gridRadio;
    private RadioGroup difficultyRadio;
    private RadioButton gridButton;
    private RadioButton difficultyButton;
    private Button startGame;


    private int gridSize = 0;
    private int difficultyLevel = 8;


    Button newGameButton;
    Button uploadFileButton;
    Button instructionsButton;
    Button modifyWordsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            wordpairs = extras.getString("wordList");
            showToast(MainMenuActivity.this, "Words successfully loaded");
        }

        newGameButton = (Button) findViewById(R.id.new_game);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int gridSize = 9;
                initializeGame(gridSize, difficultyLevel);
//                Intent intent = new Intent(MainMenuActivity.this,MainActivity.class);
//                startActivity(intent);
            }
        });

        //
        uploadFileButton = (Button) findViewById(R.id.upload_file);
        uploadFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainMenuActivity.this, UploadFileActivity.class);
                startActivity(intent2);


            }
        });

        //My Words
        myWordsButton = (Button) findViewById(R.id.wordbutton);
        myWordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(MainMenuActivity.this, MyStatsActivity.class);
                startActivity(intent3);
            }
        });

        //Game options
        button = (Button) findViewById(R.id.popupbutton1);
        // add button listener
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.gridsizepopup);
                gridRadio = (RadioGroup) dialog.findViewById(R.id.gridSizes);
                difficultyRadio = (RadioGroup) dialog.findViewById(R.id.difficulty);
                startGame = (Button) dialog.findViewById(R.id.startGameButton);
                startGame.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int selectedgrid = gridRadio.getCheckedRadioButtonId();
                        int selecteddiff = difficultyRadio.getCheckedRadioButtonId();
                        // find the radiobutton by returned id
                        gridButton = (RadioButton) dialog.findViewById(selectedgrid);
                        difficultyButton = (RadioButton) dialog.findViewById(selecteddiff);
                        if((gridButton == null) || (difficultyButton == null)){
                            Toast toast = Toast.makeText(MainMenuActivity.this, "Please choose both game options before start the game", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 0);
                            toast.show();
                        }
                        else
                        {
                            if (gridButton.getText().equals("Grid Size 4x4")) {
                                gridSize = 4;
                            }
                            else if (gridButton.getText().equals("Grid Size 6x6")) {
                                gridSize = 6;
                            }
                            else if (gridButton.getText().equals("Grid Size 12x12")) {
                                gridSize = 12;
                            }
                            else {
                                gridSize = 9;
                            }
                            Log.i("gridSize",Integer.toString(gridSize));

                            if (difficultyButton.getText().equals("Difficulty: Easy")) {
                                difficultyLevel = 24;
                            }
                            else if (difficultyButton.getText().equals("Difficulty: Medium")) {
                                difficultyLevel = 48;
                            }
                            else if (difficultyButton.getText().equals("Difficulty: Hard")) {
                                difficultyLevel = 72;
                            }
                            else {
                                difficultyLevel = 96;
                            }
//                            int diff = difficultyLevel;
//                            int grid = gridSize;
//                            int result = diff/grid;
                            difficultyLevel = difficultyLevel/(15 - gridSize);
                            Log.i("result",Integer.toString(difficultyLevel));
                            initializeGame(gridSize,difficultyLevel);
                        }
                    }
                });

                dialog.show();
            }
        });



        instructionsButton = (Button) findViewById(R.id.instructions);
        instructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg1) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.instructionspopup);

                Button dialogBtn = (Button) dialog.findViewById(R.id.donebutton);
                dialogBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }

        });
    }

    private void initializeGame(int gridsize, int difficultyLevel)
    {
        Intent i = new Intent(MainMenuActivity.this, MainActivity.class);
        i.putExtra("gridsize",gridsize);
        if(wordpairs!=null)
        {
//            if(wordpairs.length()+1 < gridsize)
//            {
//                showToast(MainMenuActivity.this, "Error: not enough words!.");
//            }
            //Log.d("wordpairsnull: ", "null");
            i.putExtra("wordList",wordpairs);
            saveInfo(wordpairs);


        }


        i.putExtra("difficulty", difficultyLevel);



        MainMenuActivity.this.startActivity(i);
    }

    public void showToast(Context mContext, String text)
    {
        Toast toast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }
//
//    public void clearRadioChecked() {
//        dialogButton4.setChecked(false);
//        rdopb.setChecked(false);
//        rdopc.setChecked(false);
//        rdopd.setChecked(false);
//    }

    public void saveInfo(String words)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("Words", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("words", words);
        editor.apply();

    }
}
