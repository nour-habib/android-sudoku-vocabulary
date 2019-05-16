package com.bignerdranch.android.sudoku12;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.EditText;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.content.Intent;
import java.util.Locale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.res.Resources;
import android.widget.Toast;


/*
gridView1.setOnItemClickListener(new OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            first case:
                break;
            second case:
                break;
        }
    }
});

*/

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {
    //Counter
    public int counter = 0;
    public int counterButton = 0;
    private int prevSelectedPosition = -1;
    public int counterPosition = 0;
    public int counterSpeakClick = 0;
    public int counterListening = 0;
    public int counterListeningLang = 0;

    //Buttons
    private Button swapButton;
    private Button listeningButton;
    private Button[] insertButton = new Button[9];

    // Speak
    private TextToSpeech myTTS;
    private int MY_DATA_CHECK_CODE = 0;

    //Import user defined word list
    public String[][] table = new String[10][3];
    List<List<String>> dictionary = new ArrayList<>();

    private String[]game = new String[]
                    {"jab", "big", "sky", "jar", "job", "mix", "zoo", "one", "two",
                    "jar", "job", "two", "one", "zoo", "jab", "big", "sky", "mix",
                    "one", "zoo", "mix", "big", "sky", "two", "jab", "jar", "job",
                    "mix", "jab", "zoo", "job", "jar", "one", "sky", "two", "big",
                    "sky", "two", "jar", "mix", "jab", "big", "job", "zoo", "one",
                    "job", "one", "big", "zoo", "two", "sky", "mix", "jab", "jar",
                    "zoo", "jar", "one", "jab", "big", "job", "two", "mix", "sky",
                    "two", "mix", "job", "sky", "one", "zoo", "jar", "big", "jab",
                    "big", "sky", "jab", "two", "mix", "jar", "one", "job", "zoo"};

    //Grid
    public ArrayAdapter<String> adapter;
    public java.lang.String x = "x";
    public String[] gridv = new String[]{
            "jab",
            " ",
            " ",
            "jar",
            "job",
            " ",
            " ",
            " ",
            "two",
        //endline
            " ",
            " ",
            "two",
            " ",
            " ",
            " ",
            " ",
            "sky",
            "mix",
        //endline
            "one",
            " ",
            "mix",
            "big",
            " ",
            " ",
            "jab",
            "jar",
            " ",
        //endline
            "mix",
            " ",
            " ",
            " ",
            " ",
            " ",
            " ",
            "two",
            " ",
        //endline
            " ",
            "two",
            " ",
            " ",
            " ",
            " ",
            " ",
            "zoo",
            " ",
            //endline
            " ",
            "one",
            " ",
            " ",
            " ",
            "sky",
            " ",
            " ",
            "jar",
        //endline
            " ",
            "jar",
            "one",
            " ",
            " ",
            "job",
            "two",
            " ",
            "sky",
        //endline
            " ",
            "mix",
            " ",
            " ",
            " ",
            " ",
            "jar",
            " ",
            " ",
        //endline
            "bigwwwwwwwwwwwww",
            " ",
            " ",
            "two",
            "mix",
            " ",
            " ",
            " ",
            "zoo"
    };
    public List<String> gridList = new ArrayList<String>(Arrays.asList(gridv));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //XML call
        final GridView gridView = (GridView) findViewById(R.id.gridView1);

        //java.lang.String z1="WWWW";

        //Text to speech;
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

        //adapter = new adapter(); // Create empty adapter.

        ///code for saving during rotation
        /*
        if(savedInstanceState != null) {
            ArrayList<MyItem> items = savedInstanceState.getParcelableArrayList("adapter");
            adapter.setItems(items); // Load saved data if any.
        }
        */

        //initialize dictionary
        //table[0][0] = new ArrayList(); // add another ArrayList object to [0,0]
        for(int i = 0; i<9;i++)
        {
            int k = i+1;
            table[i][2] = Integer.toString(k);  //"0" for English, "1" for Chinese
        }
        table[0][0] = "one";
        table[0][1] = "一";
        table[1][0] = "two";
        table[1][1] = "二";
        table[2][0] = "big";
        table[2][1] = "大";
        table[3][0] = "sky";
        table[3][1] = "天";
        table[4][0] = "jab";
        table[4][1] = "戳";
        table[5][0] = "jar";
        table[5][1] = "罐";
        table[6][0] = "job";
        table[6][1] = "工作";
        table[7][0] = "mix";
        table[7][1] = "混合";
        table[8][0] = "zoo";
        table[8][1] = "动物园";
        table[9][0] = "Han";
        table[9][1] = "瀚";

        insertButton[0] = (Button) findViewById(R.id.insert1);
        insertButton[1] = (Button) findViewById(R.id.insert2);
        insertButton[2] = (Button) findViewById(R.id.insert3);
        insertButton[3] = (Button) findViewById(R.id.insert4);
        insertButton[4] = (Button) findViewById(R.id.insert5);
        insertButton[5] = (Button) findViewById(R.id.insert6);
        insertButton[6] = (Button) findViewById(R.id.insert7);
        insertButton[7] = (Button) findViewById(R.id.insert8);
        insertButton[8] = (Button) findViewById(R.id.insert9);

        for (int i=0; i<9; i++) {
            insertButton[i].setText(table[i][0]);
        }
        //List<String> word1 = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, gridList) {
            public View getView(int position, View v, ViewGroup parent) {

                View view = super.getView(position, v, parent);
                TextView textView = (TextView) view;

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
                );
                textView.setLayoutParams(lp);

                RelativeLayout.LayoutParams parameters = (RelativeLayout.LayoutParams) textView.getLayoutParams();

                //411x731dp at 41 for Nexus 5X in portraitM
                // 600x960dp at 60 for Nexus 7
                // 410 dp x 730 dp at 41 for Nexus S
                // Pattern is smaller number (width) over 10
                //Pixeldp/10


                //get the display metrics of the screen
                Display display = getWindowManager().getDefaultDisplay();
                DisplayMetrics outMetrics = new DisplayMetrics();
                display.getMetrics(outMetrics);

                //calculating the width and height as a float
                float density = getResources().getDisplayMetrics().density;
                float dpWidth = outMetrics.widthPixels / density;

                float z = dpWidth / 10;

                //rounding the width of the float value to an int
                int x = Math.round(dpWidth);

                //dividing by 10 with the analyzed pattern
                int y = x / 10;

                //float dpHeight = outMetrics.heightPixels / density; //for height
                //entering gridview width parameters (y)
                parameters.width = getPixelsFromDPs(MainActivity.this, y);
                textView.setGravity(Gravity.CENTER);
                textView.setLayoutParams(parameters);


                //creating a size of text based on screen dpWidth/10
                int s;

                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    //landscape
                    if (dpWidth / 10 > 55) {
                        s = 12;
                    } else {
                        s = 9;
                    }
                } else {
                    // In portrait
                    if (dpWidth / 10 > 55) {
                        s = 9;
                    } else {
                        s = 7;
                    }
                }

                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, s); //text size 9 for Nexus 5X
                textView.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
                textView.setText(gridList.get(position));

                //grid colour
                textView.setBackgroundColor(Color.parseColor("#83F0FC"));
                return textView;
            }

        };
        gridView.setAdapter(adapter);
        
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, final View v,
                                    final int position, long id) {
//                Toast.makeText(MainActivity.this, "Clicked item at index:" + parent.getItemAtPosition(position).toString(),
//                        Toast.LENGTH_SHORT).show();
                //String selectedItem = parent.getItemAtPosition(position).toString();
                TextView textView = (TextView) v;
                textView.setBackgroundColor(Color.parseColor("#83C1FC"));
                TextView prevSelectedView = (TextView) gridView.getChildAt(prevSelectedPosition);
                String words = parent.getItemAtPosition(position).toString();
                for(int i=0; i<9; i++)
                {
                    if(words == table[i][2])
                        words = table[i][counterListeningLang];
                }
                if(words != " ")
                    onButtonShowPopupWindowClick(textView,words);
//                if ((counter%2 == 0) && (words == " "))
//                    words = "Input an answer first!";
//                else if ((counter%2 == 1) && (words == " "))
//                    words = "请先输入个单词";
                if (prevSelectedPosition == -1) {   //first click
                    // Text to Speech
                    counterPosition = position;
                    speakWords(words);
                    counterSpeakClick = 1;
                }
                else if (prevSelectedPosition == position ) {
                    //prevSelectedView.setSelected(true);
                    speakWords(words);
                    if ((counterSpeakClick >= 5) && (counterSpeakClick != 10)) {
                        if (counter%2 == 0)
                            words = "Stop clicking me！";
                        else if (counter%2 == 1)
                            words = "别点我了！";
                        speakWords(words);
                    } else if (counterSpeakClick == 10) {
                        if (counter%2 == 0)
                            words = "I said stop!";
                        else if (counter%2 == 1)
                            words = "我都说了别点了！";
                        speakWords(words);
                        Toast toast = Toast.makeText(MainActivity.this, R.string.accomplishment_speech_1, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        //accomplishment[3] = 1;
                    }
                    counterSpeakClick++;
                }
                else if (prevSelectedPosition != position) {
                        prevSelectedView.setSelected(false);
                        prevSelectedView.setBackgroundColor(Color.parseColor("#83F0FC"));
                        speakWords(words);
                        counterPosition = position;
                        counterSpeakClick=1;
                    }
                prevSelectedPosition = position;



                insertButton[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("position", "position: " + position);
                        int p = position;
                        counterButton = 0;
//                        String words = table[counterButton][0];
//                        speakWords(words);
                        inputanswer(p,counterButton,counterListeningLang);
                    }
                });
                insertButton[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("position", "position: " + position);
                        int p = position;
                        counterButton = 1;
                        inputanswer(p,counterButton,counterListeningLang);
                    }
                });
                insertButton[2].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("position", "position: " + position);
                        int p = position;
                        counterButton = 2;
                        inputanswer(p,counterButton,counterListeningLang);
                    }
                });
                insertButton[3].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("position", "position: " + position);
                        int p = position;
                        counterButton = 3;
                        inputanswer(p,counterButton,counterListeningLang);
                    }
                });
                insertButton[4].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("position", "position: " + position);
                        int p = position;
                        counterButton = 4;
                        inputanswer(p,counterButton,counterListeningLang);
                    }
                });
                insertButton[5].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("position", "position: " + position);
                        int p = position;
                        counterButton = 5;
                        inputanswer(p,counterButton,counterListeningLang);
                    }
                });
                insertButton[6].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("position", "position: " + position);
                        int p = position;
                        counterButton = 6;
                        inputanswer(p,counterButton,counterListeningLang);
                    }
                });
                insertButton[7].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("position", "position: " + position);
                        int p = position;
                        counterButton = 7;
                        inputanswer(p,counterButton,counterListeningLang);
                    }
                });
                insertButton[8].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("position", "position: " + position);
                        int p = position;
                        counterButton = 8;
                        inputanswer(p,counterButton,counterListeningLang);
                    }
                });

                swapButton = (Button) findViewById(R.id.swap_button);
                swapButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //adapter.clear();
                        //I'll try to use arraylist later
                        //swap(counter, 1, "swap_lang_1");
                        if (counter % 2 == 0) {
                            for (int i = 0; i < 81; i++) {
                                for (int j = 0; j < table.length; j++) {
                                    if (adapter.getItem(i) == table[j][0]) {
                                        adapter.insertButton(table[j][1], i);
                                        adapter.remove(table[j][0]);
                                        insertButton[j].setText(table[j][1]); //Change button text
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                            counter++;
                            myTTS.setLanguage(Locale.CHINESE);
                            counterListeningLang = 1;
                            Toast toast = Toast.makeText(MainActivity.this, R.string.swap_lang_1, Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 0);
                            toast.show();
                        } else {
                            for (int i = 0; i < 81; i++) {
                                for (int j = 0; j < table.length; j++) {
                                    if (adapter.getItem(i) == table[j][1]) {
                                        adapter.insertButton(table[j][0], i);
                                        adapter.remove(table[j][1]);
                                        insertButton[j].setText(table[j][0]);
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                            counter++;
                            myTTS.setLanguage(Locale.US);
                            counterListeningLang = 0;
                            Toast toast = Toast.makeText(MainActivity.this, R.string.swap_lang_2, Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 0);
                            toast.show();
                        }
                    }
                });

                listeningButton = (Button) findViewById(R.id.listening_button);
                listeningButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            if (counterListening % 2 == 0) {   //words to number
                                for (int i = 0; i < 81; i++) {
                                    for (int j = 0; j < table.length; j++) {
                                        if (adapter.getItem(i) == table[j][counterListeningLang]) {
                                            adapter.insertButton(table[j][2], i);
                                            adapter.remove(table[j][counterListeningLang]);
                                        }
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                counterListening++;
                                Toast toast = Toast.makeText(MainActivity.this, R.string.listening_1, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 0);
                                //toast.show();
                            } else {
                                for (int i = 0; i < 81; i++) {
                                    for (int j = 0; j < table.length; j++) {
                                        if (adapter.getItem(i) == table[j][2]) {
                                            adapter.insertButton(table[j][counterListeningLang], i);
                                            adapter.remove(table[j][2]);
                                        }
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                counterListening++;
                                Toast toast = Toast.makeText(MainActivity.this, R.string.listening_2, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 0);
                                toast.show();
                            }
                        }

                });

            }

        });

    }

    //Rotation save call
    /*

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putParcelableArrayList("adapter", adapter.getItems());
    }
    */

    public static int getPixelsFromDPs(Activity activity, int dps){
        Resources r = activity.getResources();
        int  pixel1 = (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps, r.getDisplayMetrics()));
        return pixel1;
    }

    private void inputanswer(int p,int cb,int lang) {
        if (table[cb][0] == game[p]) {
            String[] newview = new String[90];
            for (int y = 0; y <= p; y++) {
                if (y == p)
                    newview[y] = table[cb][lang];
                else
                    newview[y] = adapter.getItem(y);
            }
            for (int j = 0; j <= p; j++) {
                if (j == p)
                    adapter.remove(" ");
                else
                    adapter.remove(newview[j]);
            }
            for (int k = 0; k <= p; k++) {
                adapter.insertButton(newview[k], k);
            }
            Toast toast = Toast.makeText(MainActivity.this, "correct", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        } else {
            Toast toast = Toast.makeText(MainActivity.this, "Incorrect", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }
    }


//    public void swap(int counter, int swap_index, String a){
//        if (counter % 2 == 0) {
//            for (int i = 0; i < 81; i++) {
//                for (int j = 0; j < table.length; j++) {
//                    if (adapter.getItem(i) == table[j][0]) {
//                        adapter.insertButton(table[j][swap_index], i);
//                        adapter.remove(table[j][0]);
//                    }
//                }
//            }
//            adapter.notifyDataSetChanged();
//            counter++;
//            myTTS.setLanguage(Locale.CHINESE);
//            counterListeningLang = 1;
//            //Trying to use identifier
////            String packageName = this.getPackageName();
////            int resID = getResources().getIdentifier(a, "string", packageName);
//            if (a == "swap_lang_1")
//            {
//                Toast toast = Toast.makeText(MainActivity.this, R.string.swap_lang_1, Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.TOP, 0, 0);
//                toast.show();
//            }
//            else
//            {
//                Toast toast = Toast.makeText(MainActivity.this, R.string.listening_1, Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.TOP, 0, 0);
//                toast.show();
//            }
//        } else {
//            for (int i = 0; i < 81; i++) {
//                for (int j = 0; j < table.length; j++) {
//                    if (adapter.getItem(i) == table[j][swap_index]) {
//                        adapter.insertButton(table[j][0], i);
//                        adapter.remove(table[j][swap_index]);
//                    }
//                }
//            }
//            adapter.notifyDataSetChanged();
//            counter++;
//            myTTS.setLanguage(Locale.US);
//            counterListeningLang = 0;
//            if (a == "swap_lang_1")
//            {
//                Toast toast = Toast.makeText(MainActivity.this, R.string.swap_lang_2, Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.TOP, 0, 0);
//                toast.show();
//            }
//            else
//            {
//                Toast toast = Toast.makeText(MainActivity.this, R.string.listening_2, Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.TOP, 0, 0);
//                toast.show();
//            }
//        }
//    }

    //Reference https://code.tutsplus.com/tutorials/android-sdk-using-the-text-to-speech-engine--mobile-8540
    private void speakWords(String speech) {
        //speak straight away
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    //act on result of TTS data check
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS
                myTTS = new TextToSpeech(getApplicationContext(), this);
            }
            else {
                //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    //setup TTS
    public void onInit(int initStatus) {
        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if(myTTS.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE) {
                myTTS.setLanguage(Locale.US);
            }
        }
        else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    //Reference: https://stackoverflow.com/questions/5944987/how-to-create-a-popup-window-popupwindow-in-android
    //popup word window
    public void onButtonShowPopupWindowClick(View view, String word_display) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.word_window, null);
        //popupView.setText("sdf");
        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //((TextView)popupWindow.getContentView().findViewById(R.id.word_view).setText("Hi"));
        TextView text_word = (TextView)popupView.findViewById(R.id.word_view);
        text_word.setTypeface(null,Typeface.BOLD);
        text_word.setText(word_display);
        // show the popup window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(20);
        }
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
}

