package com.bignerdranch.android.sudoku12;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.StringPrepParseException;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.RequiresApi;
import android.text.method.ScrollingMovementMethod;
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
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.EditText;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.content.Intent;
import java.util.Timer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import android.os.Handler;

import android.content.res.Resources;
import android.widget.Toast;
import java.util.TimerTask;




public class MainActivity extends Activity implements TextToSpeech.OnInitListener {
    //Counter
    public int counter = 0;
    public int counterButton = 0; //refactored from counter_button
    private int prevSelectedPosition = -1;
    public int counterPosition = 0; //refactored from counter_Position
    public int counterSpeakClick = 0; //refactored from counter_speak_click
    public int counterListening = 0; //refactored from counter_listening
    public int counterListeningLang = 0; //refactored from counter_listening_lang
    public int mode = 0; //Default setting
    public int gridSize = 9;  //refactored from gridsize
    public int totalSize;
    public int gameCount = 0;
    //achievements
    public int rotate = 0;
    public int swap = 0;
    public int list = 0;


    //Buttons
    private Button swapButton;
    private Button listeningButton;
    private Button optionButton;
    private Button currentButton;
    private Button gameInstructionsButton; //refactored from gameinstructionsButton
    private Button achievementButton;
    private Button[] insertButton = new Button[12];  //refactored from insert

    // Speak
    private TextToSpeech myTTS;
    private int MY_DATA_CHECK_CODE = 0;

    //Import user defined word list
    public String[][] table; //[0] English, [1] Chinese, [2] number
    String[]englishWords;
    String[]chineseWords;

    //Grid
    public ArrayAdapter<String> adapter;
    public java.lang.String x = "x";
    //public List<String> gridList;
    private String[] intSolution;
    private String[] solution;
    private String[] gameArray;
    String[] intGameArray;
    private SudokuPuzzleType mSudokuPuzzleType;
    private SudokuGenerator mSudokuGenerator;
    private SudokuPuzzle mSudokuPuzzle;
    private Chronometer chronometer;
    private StatsTracker mStatsTracker;
    private TextView mScoreTextView;



    //my words
    public Map<String, int[]> wordsHashMap = new HashMap<>(); //refactored from mywords
    public int[] counterWordsCorrectOrWrong = new int[2]; //counterWordsCorrectOrWrong[0] = correct times, counterWordsCorrectOrWrong[1] = wrong times //refactored from newwords
    public  String[] seperate;

    //public String[] gridv;
    public ArrayList<String> gridList;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //XML call
        final GridView gridView = (GridView) findViewById(R.id.gridView1); //refactored from gv

        mStatsTracker = new StatsTracker();

        chronometer = new Chronometer(this);
        startTimer(chronometer);

        mScoreTextView = (TextView)findViewById(R.id.score);
        mScoreTextView.setText(Integer.toString(mStatsTracker.getScore()));


        String wordPairs = new String();

        Bundle extras = getIntent().getExtras();
        int nval;
        if(extras.isEmpty()){
            nval = 9;
        }
        else
        nval = extras.getInt("gridsize");
        int difficultyLevel = extras.getInt("difficulty");
        if (extras.getString("wordList") != null) {
            wordPairs = extras.getString("wordList");
            mode = 1;

        }
        gridSize = nval;

        totalSize = gridSize*gridSize;

        Log.d("nGridsize: ", Integer.toString(nval));

        //initialize game
        int n = gridSize;
        Log.d("n_gs: ", Integer.toString(n));

        Log.d("difficultyLevel: ", Integer.toString(difficultyLevel));
        gameCount = (n*n)-difficultyLevel;

        if(gridSize==4)
        {
            mSudokuPuzzleType = SudokuPuzzleType.FOURBYFOUR;
        }

        if(gridSize==6)
        {
            mSudokuPuzzleType = SudokuPuzzleType.SIXBYSIX;
        }
        if(gridSize==9)
        {
            mSudokuPuzzleType = SudokuPuzzleType.NINEBYNINE;
        }
        if(gridSize==12)
        {
            mSudokuPuzzleType = SudokuPuzzleType.TWELVEBYTWELVE;
        }

        mSudokuGenerator = new SudokuGenerator();
        mSudokuPuzzle = mSudokuGenerator.generateRandomSudoku(mSudokuPuzzleType);
        intSolution = mSudokuPuzzle.getBoard();
        Log.d("intSolution: ", Arrays.deepToString(intSolution));
        intGameArray = mSudokuPuzzle.getGame(difficultyLevel);
        Log.d(" intGameArray: ", Arrays.deepToString( intGameArray));




        englishWords = new String[gridSize];
        chineseWords = new String[gridSize];

        //default values
        if (mode == 0) {
            chineseWords = getResources().getStringArray(R.array.chineseNumbersArray);
            englishWords = getApplicationContext().getResources().getStringArray(R.array.englishNumbersArray);

            table = generateMapping(englishWords, chineseWords);
            Log.d("chineseWords: ", Arrays.deepToString(chineseWords));
            Log.d("englishWords: ", Arrays.deepToString(englishWords));


        }
            //custom word list
        else if (mode==1)
        {
            seperate = splitString(wordPairs,"\n");
            Log.d("seperate: ", Arrays.deepToString(seperate));
            Log.d("seperatelenth: ", Integer.toString(seperate.length));

            String[] randomSelectedPairsString; //refactored from randomSelectedPairs
            randomSelectedPairsString = randomSelectedPairs(seperate);

            for(int i = 0;i<randomSelectedPairsString.length;i++)
            {
                String s = randomSelectedPairsString[i];
                Log.d("sep: ", s);
                String word[] = s.split(",");
                englishWords[i] = word[0];
                chineseWords[i] = word[1];

            }
            table = new String[gridSize][3];

            table = generateMapping(englishWords, chineseWords);


            Log.d("tablecust: ", Arrays.deepToString(table));
        }


        Log.d("table: ", Arrays.deepToString(table));

        solution = translate(table, intSolution);
        Log.d("msolution: ", Arrays.deepToString(solution));

        gameArray = translate(table, intGameArray);
        Log.d("mgamesolutin: ", Arrays.deepToString(gameArray));


        gridList = new ArrayList<String>(Arrays.asList(gameArray));


        //Log.d("gridlist: ", Arrays.deepToString(gridList));


        if (savedInstanceState != null) {

            gridSize = savedInstanceState.getInt("gridsize");
            gameArray = savedInstanceState.getStringArray("GRIDV");
            gridList = savedInstanceState.getStringArrayList("GRIDLIST");
            counterListeningLang = savedInstanceState.getInt("COUNTER_LANG");
            counter = savedInstanceState.getInt("COUNTER");
            counterListening = savedInstanceState.getInt("COUNTER_LIST");
            intGameArray = savedInstanceState.getStringArray("intGameArray");
            solution = savedInstanceState.getStringArray("solution");
            intSolution = savedInstanceState.getStringArray("intSolution");
            mStatsTracker = savedInstanceState.getParcelable("statsTracker");
            gameCount = savedInstanceState.getInt("gameCount");
            rotate = 1;

        }


        //Text to speech;
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);


        //initialize dictionary
        //table[0][0] = new ArrayList(); // add another ArrayList object to [0,0]

        insertButton[0] = (Button) findViewById(R.id.insert1);
        insertButton[1] = (Button) findViewById(R.id.insert2);
        insertButton[2] = (Button) findViewById(R.id.insert3);
        insertButton[3] = (Button) findViewById(R.id.insert4);
        insertButton[4] = (Button) findViewById(R.id.insert5);
        insertButton[5] = (Button) findViewById(R.id.insert6);
        insertButton[6] = (Button) findViewById(R.id.insert7);
        insertButton[7] = (Button) findViewById(R.id.insert8);
        insertButton[8] = (Button) findViewById(R.id.insert9);
        insertButton[9] = (Button) findViewById(R.id.insert10);
        insertButton[10] = (Button) findViewById(R.id.insert11);
        insertButton[11] = (Button) findViewById(R.id.insert12);


        if (gridSize == 9){
            insertButton[11].setVisibility (View.GONE);
            insertButton[10].setVisibility (View.GONE);
            insertButton[9].setVisibility (View.GONE);
        }
        else if (gridSize == 4){
            insertButton[11].setVisibility (View.GONE);
            insertButton[10].setVisibility (View.GONE);
            insertButton[9].setVisibility (View.GONE);
            insertButton[8].setVisibility (View.GONE);
            insertButton[7].setVisibility (View.GONE);
            insertButton[6].setVisibility (View.GONE);
            insertButton[5].setVisibility (View.GONE);
            insertButton[4].setVisibility (View.GONE);
        }
        else if (gridSize == 6){
            insertButton[11].setVisibility (View.GONE);
            insertButton[10].setVisibility (View.GONE);
            insertButton[9].setVisibility (View.GONE);
            insertButton[8].setVisibility (View.GONE);
            insertButton[7].setVisibility (View.GONE);
            insertButton[6].setVisibility (View.GONE);
        }

        for (int i = 0; i < gridSize; i++) {
            insertButton[i].setText(table[i][counterListeningLang]);
           // insertButton[i].getAutoSizeMaxTextSize();
        }

        optionButton = (Button) findViewById(R.id.option_button);
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//            final Dialog dialog = new Dialog(v.getContext());
//                dialog.setContentView(R.layout.optionpopup);

                LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View popupView = inflater.inflate(R.layout.optionpopup, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);

                swapButton = (Button)popupView.findViewById(R.id.swap_lang);
                swapButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        swap = 1;
                        if (counter % 2 == 0) {
                            for (int i = 0; i < totalSize; i++) {
                                for (int j = 0; j < table.length; j++) {
                                    if (adapter.getItem(i) == table[j][0]) {
                                        adapter.insert(table[j][1], i);
                                        adapter.remove(table[j][0]);
                                    }
                                }
                            }
                            for(int i =0 ; i<gridSize;i++)
                            {
                                insertButton[i].setText(table[i][1]);
                            }
                            //gridList = new ArrayList<String>(Arrays.asList(gridv));
                            adapter.notifyDataSetChanged();
                            counter++;
                            myTTS.setLanguage(Locale.CHINESE);
                            counterListeningLang = 1;
//                    Toast toast = Toast.makeText(MainActivity.this, R.string.swap_lang_1, Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.TOP, 0, 0);
//                    toast.show();
                        } else {
                            for (int i = 0; i < totalSize; i++) {
                                for (int j = 0; j < table.length; j++) {
                                    if (adapter.getItem(i) == table[j][1]) {
                                        adapter.insert(table[j][0], i);
                                        adapter.remove(table[j][1]);
                                    }
                                }
                            }
                            for(int i =0 ; i<gridSize;i++)
                            {
                                insertButton[i].setText(table[i][0]);
                            }
                            adapter.notifyDataSetChanged();
                            counter++;
                            myTTS.setLanguage(Locale.US);
                            counterListeningLang = 0;
//                    Toast toast = Toast.makeText(MainActivity.this, R.string.swap_lang_2, Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.TOP, 0, 0);
//                    toast.show();
                        }
                    }

                });

                listeningButton = (Button) popupView.findViewById(R.id.listening_button);
                listeningButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list = 1;
                        if (counterListening % 2 == 0) {   //words to number
                            for (int i = 0; i < totalSize; i++) {
                                for (int j = 0; j < table.length; j++) {
                                    if (adapter.getItem(i) == table[j][counterListeningLang]) {
                                        adapter.insert(table[j][2], i);
                                        adapter.remove(table[j][counterListeningLang]);
                                    }
                                }
                            }
                            //change the order of buttons after choose listening mode
                            List<String>shufflebuttons = new ArrayList<>(Arrays.asList(englishWords));
                            Collections.shuffle(shufflebuttons);
                            for(int i =0 ; i<gridSize;i++)
                            {
                                insertButton[i].setText(shufflebuttons.get(i));
                            }
                            adapter.notifyDataSetChanged();
                            counterListening++;
//                    Toast toast = Toast.makeText(MainActivity.this, R.string.listening_1, Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.TOP, 0, 0);
                            //toast.show();
                        } else {
                            for (int i = 0; i < totalSize; i++) {
                                for (int j = 0; j < table.length; j++) {
                                    if (adapter.getItem(i) == table[j][2]) {
                                        adapter.insert(table[j][counterListeningLang], i);
                                        adapter.remove(table[j][2]);
                                    }
                                }
                            }
                            List<String>originbuttons = new ArrayList<>(Arrays.asList(englishWords));
                            for(int i =0 ; i<gridSize;i++)
                            {
                                insertButton[i].setText(originbuttons.get(i));
                            }
                            adapter.notifyDataSetChanged();
                            counterListening++;
//                    Toast toast = Toast.makeText(MainActivity.this, R.string.listening_2, Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.TOP, 0, 0);
//                    toast.show();
                        }
                    }

                });

                ImageButton btnDismiss = (ImageButton) popupView.findViewById(R.id.dismiss);
                btnDismiss.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();
                    }});

                // Current words
                currentButton = (Button) popupView.findViewById(R.id.current_Words);
                currentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //getcurrentwords(v,seperate);
                        popupWindow.dismiss();
                        String[] currentWordsString = new String[gridSize]; //refactored from currentwords
                        String[] english = new String[gridSize];
                        String[] chinese = new String[gridSize];
                        for(int i = 0; i<gridSize;i++){
                            english[i] = table[i][0];
                            chinese[i] = table[i][1];
                            if(i == 0)
                                currentWordsString[i] = String.valueOf(i+1) + ". " + english[i] + "    " + chinese[i];
                            else
                                currentWordsString[i] = "\n" + String.valueOf(i+1) + ". " + english[i] + "    " + chinese[i];
                        }
                        String currentWordsRaw = Arrays.toString(currentWordsString);
                        String currentWords = currentWordsRaw.substring(1, currentWordsRaw.length()-1);
                        //cw.substring(1,cw.length()-1);
                        getcurrentwords(v,currentWords);
                        //onButtonShowPopupWindowClick(v, Arrays.toString(currentwords));
                    }
                });

                gameInstructionsButton = (Button) popupView.findViewById(R.id.instructions_pop);
                gameInstructionsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        final Dialog gameInstructionPopup = new Dialog(v.getContext()); //refactored from inspop
                        gameInstructionPopup.setContentView(R.layout.instructionspopup);

                        Button dialogBtn = (Button) gameInstructionPopup.findViewById(R.id.donebutton);
                        dialogBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                gameInstructionPopup.dismiss();
                            }
                        });

                        gameInstructionPopup.show();

                    }
                });

                achievementButton = (Button) popupView.findViewById(R.id.Achievement_pop);
                achievementButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        final Dialog achievePopup = new Dialog(v.getContext()); //refactored from inspop
                        achievePopup.setContentView(R.layout.achievementpopup);
                        LinearLayout all = (LinearLayout)achievePopup.findViewById(R.id.achievementLinear);
                        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        if (swap == 1) {
                            TextView tv = new TextView(getApplicationContext());
                            tv.setLayoutParams(lparams);
                            tv.setText("Congrats! Swap Language");
                            tv.setId(swap);
                            //tv.setTextColor(Color.parseColor("#FFF"));
                            if (tv.getParent() != null) {
                                ((ViewGroup) tv.getParent()).removeView(tv); // <- fix
                            }
                            all.addView(tv);
                        }
                            if (list == 1) {
                                TextView tv1 = new TextView(getApplicationContext());
                                tv1.setLayoutParams(lparams);
                                tv1.setText("Congrats! Listening Comprehension");
                                tv1.setId(list);
                                //tv.setTextColor(Color.parseColor("#FFF"));
                                if (tv1.getParent() != null) {
                                    ((ViewGroup) tv1.getParent()).removeView(tv1); // <- fix
                                }

                                all.addView(tv1);
                            }
                            if (rotate == 1){
                                    TextView tv2= new TextView(getApplicationContext());
                                    tv2.setLayoutParams(lparams);
                                    tv2.setText("Congrats! Screen Rotation");
                                    tv2.setId(rotate);
                                    //tv.setTextColor(Color.parseColor("#FFF"));
                                    if(tv2.getParent() != null) {
                                        ((ViewGroup)tv2.getParent()).removeView(tv2); // <- fix
                                    }
                                    all.addView(tv2);
                        }
                        Button dialogBtn = (Button) achievePopup.findViewById(R.id.backbutton);
                        dialogBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                achievePopup.dismiss();
                            }
                        });

                        achievePopup.show();

                    }
                });


                popupWindow.showAtLocation(v, Gravity.CENTER,0,0);
            }
        });

        //List<String> word1 = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, gridList) {

            @Override
            public View getView(int position, View v, ViewGroup parent) {

                View view = super.getView(position, v, parent); // Generate items

                TextView textView = (TextView) view;  //refactored from text1

                //textView.setTypeface(null, Typeface.BOLD);

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
                );
                textView.setLayoutParams(lp);

                RelativeLayout.LayoutParams parameters = (RelativeLayout.LayoutParams) textView.getLayoutParams();

                //411x731dp at 41 for Nexus 5X in portraitM
                // 600x960dp at 60 for Nexus 7
                // 410 dp x 730 dp at 41 for Nexus S
                // Pattern is smaller number (width) over 10
                //Pixeldp/10

                gridView.setColumnWidth(GridView.AUTO_FIT);
                gridView.setNumColumns(gridSize);
                gridView.setHorizontalSpacing(5);

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

                if (gridSize == 4){
                    y = y * 2;
                }

                //float dpHeight = outMetrics.heightPixels / density; //for height
                //entering gridview width parameters (y)
                parameters.width = getPixelsFromDPs(MainActivity.this, y);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setLayoutParams(parameters);


                //creating a size of text based on screen dpWidth/10
                int s;

                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    //landscape
                    if (dpWidth / 10 > 55) {
                        s = 15;
                    } else {
                        s = 12;
                    }
                } else {
                    // In portrait
                    if (dpWidth / 10 > 55) {
                        s = 11;
                    } else {
                        s = 10;
                    }
                }

                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, s); //text size 9 for Nexus 5X
                textView.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
                //textView.setText(gridList.get(position));
                Log.d("textView.len",textView.getText().toString());
                //textView.setTextColor(Color.parseColor("#66bbff"));
                //textView.setMovementMethod(new ScrollingMovementMethod());

//                if(position!=0) //an unsatisfying way to fix the bug where the first word disappears
//                    textView.setHorizontallyScrolling(true);
//                else
//                    textView.setText(gridList.get(position).substring(0,2));

                textView.setHorizontallyScrolling(true);
                tvsetbackcolor(textView);
                return textView;
            }

        };
        //gridList.remove(0);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, final View v,
                                    final int position, long id) {
//                Toast.makeText(MainActivity.this, "Clicked item at index:" + parent.getItemAtPosition(position).toString(),
//                        Toast.LENGTH_SHORT).show();
                //String selectedItem = parent.getItemAtPosition(position).toString();
                //Click on grid

                TextView textView = (TextView) v;
                textView.setBackgroundColor(Color.parseColor("#83C1FC"));
                //textView.setTextColor(Color.parseColor("#66bbff"));
                TextView prevSelectedView = (TextView) gridView.getChildAt(prevSelectedPosition);
                String words = parent.getItemAtPosition(position).toString();

                for (int i = 0; i < gridSize; i++) {
                    if (words == table[i][2])
                        words = table[i][counterListeningLang];
                }
                if ((words != " ") && (counterListening % 2 == 0))
                    onButtonShowPopupWindowClick(textView, words);
//                if ((counter%2 == 0) && (words == " "))
//                    words = "Input an answer first!";
//                else if ((counter%2 == 1) && (words == " "))
//                    words = "请先输入个单词";
                if (prevSelectedPosition == -1) {   //first click
                    // Text to Speech
                    counterPosition = position;
                    speakWords(words);
                    counterSpeakClick = 1;
                } else if (prevSelectedPosition == position) {
                    //prevSelectedView.setSelected(true);
                    speakWords(words);
                    if ((counterSpeakClick >= 5) && (counterSpeakClick != 10)) {
                        if (counter % 2 == 0)
                            words = "Stop clicking me！";
                        else if (counter % 2 == 1)
                            words = "别点我了！";
                        speakWords(words);
                    } else if (counterSpeakClick == 10) {
                        if (counter % 2 == 0)
                            words = "I said stop!";
                        else if (counter % 2 == 1)
                            words = "我都说了别点了！";
                        speakWords(words);
                        Toast toast = Toast.makeText(MainActivity.this, R.string.accomplishment_speech_1, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        //accomplishment[3] = 1;
                    }
                    counterSpeakClick++;
                } else if (prevSelectedPosition != position) {
                    prevSelectedView.setSelected(false);
                    tvsetbackcolor(prevSelectedView);
                    speakWords(words);
                    counterPosition = position;
                    counterSpeakClick = 1;
                }
                prevSelectedPosition = position;


                insertButton[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int p = position;
                        counterButton = 0;
//                        String words = table[counterButton][0];
//                        speakWords(words);
                        inputanswer(p, counterButton, counterListeningLang);
                    }
                });
                insertButton[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int p = position;
                        counterButton = 1;
                        inputanswer(p, counterButton, counterListeningLang);
                    }
                });
                insertButton[2].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int p = position;
                        counterButton = 2;
                        inputanswer(p, counterButton, counterListeningLang);
                    }
                });
                insertButton[3].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int p = position;
                        counterButton = 3;
                        inputanswer(p, counterButton, counterListeningLang);
                    }
                });
                insertButton[4].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int p = position;
                        counterButton = 4;
                        inputanswer(p, counterButton, counterListeningLang);
                    }
                });
                insertButton[5].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int p = position;
                        counterButton = 5;
                        inputanswer(p, counterButton, counterListeningLang);
                    }
                });
                insertButton[6].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int p = position;
                        counterButton = 6;
                        inputanswer(p, counterButton, counterListeningLang);
                    }
                });
                insertButton[7].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int p = position;
                        counterButton = 7;
                        inputanswer(p, counterButton, counterListeningLang);
                    }
                });
                insertButton[8].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int p = position;
                        counterButton = 8;
                        inputanswer(p, counterButton, counterListeningLang);
                    }
                });
            }

        });
    }


    public static int getPixelsFromDPs(Activity activity, int dps) {
        Resources r = activity.getResources();
        int pixel1 = (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps, r.getDisplayMetrics()));
        return pixel1;
    }

    private void inputanswer(int p, int cb, int lang) {
        Log.d("inputanswer_sol", solution[p]);
        Log.d("inputanswer_table", table[cb][0]);
        if (table[cb][0].equals(solution[p])) {

            Log.d("inputanswer_score: ", Integer.toString(mStatsTracker.getScore()));
            Log.d("inputanswer_true: ", "True");
            gameCount++;
            Log.d("inputanswer_gameCount: ", Integer.toString(gameCount));
            if(gameCount == totalSize)
            {
                endGame();

            }
            else
            {
                Toast toast = Toast.makeText(MainActivity.this, "Correct", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();
            }
            mStatsTracker.setScore();
            mScoreTextView.setText(Integer.toString(mStatsTracker.getScore()));
            String[] newview = new String[totalSize];
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
                adapter.insert(newview[k], k);
            }
//            gridv[p] = table[cb][lang];
//            gridList = new ArrayList<String>(Arrays.asList(gridv));
//            adapter.notifyDataSetChanged();

            speakWords(table[cb][counterListeningLang]);
        } else {
            Toast toast = Toast.makeText(MainActivity.this, "Incorrect Answer", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }
    }


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
            } else {
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
            if (counterListeningLang==0){//(myTTS.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE) {
                myTTS.setLanguage(Locale.US);
            }
            else
            myTTS.setLanguage(Locale.CHINESE);
        } else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }



    private String[] splitString(String string, String splitChar) {
        String[] separatedArray = string.split(splitChar);
        Log.d("seperatearrlen: ", Integer.toString(separatedArray.length));

        return separatedArray;
    }

    private String[] randomSelectedPairs(String[] wordsArr)
    {
        String[] newWordList = new String[gridSize];

        List<String>stringList = new ArrayList<String>(Arrays.asList(wordsArr));
        counterWordsCorrectOrWrong[0] = 0;
        counterWordsCorrectOrWrong[1] = 0;
        for(int i = 0; i < wordsArr.length; i++){
            if(wordsHashMap.containsKey(wordsArr[i]) == false){
                wordsHashMap.put(wordsArr[i], counterWordsCorrectOrWrong);
                Log.d("wordsHashMap", String.valueOf(wordsHashMap.size()));
            }
        }

        Collections.shuffle(stringList);
        for(int i=0;i<newWordList.length;i++)
        {
            newWordList[i] = stringList.get(i);
        }
        Log.d("newWordList: ", Arrays.toString(newWordList));
        return newWordList;
    }

    private String[][] generateMapping(String[]englishArray,String[]chineseArray)
    {
        String[][] map = new String[gridSize+1][3];
        for(int i=0;i<gridSize;i++)
        {

            map[i][0] = englishArray[i];
            map[i][1] = chineseArray[i];
            int k = i+1;
            map[i][2] = Integer.toString(k);

        }

        Log.d("genermapping: ", Arrays.deepToString(map));
        Log.d("chineseArray: ", Arrays.toString(chineseArray));


      return map;
    }

    private String[] translate(String[][]map,String[]solution)
    {
        String[]translatedSolution = new String[totalSize];

        for(int i=0;i<translatedSolution.length;i++)
        {

            for(int k=0;k<map.length;k++)
            {
                if (solution[i].equals(map[k][2]))
                {
                    translatedSolution[i] = map[k][0];
                }
                if(solution[i].equals(" "))
                {
                    translatedSolution[i] = " ";
                }
            }

        }
        Log.d("prevsolution: ", Arrays.toString(solution));
        Log.d("translatedsolution: ", Arrays.toString(translatedSolution));

        return translatedSolution;
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
        TextView text_word = (TextView) popupView.findViewById(R.id.word_view);
        text_word.setTypeface(null, Typeface.BOLD);
        text_word.setText(word_display);
        // show the popup window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(20);
        }
        popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);
//        try {
//            TimeUnit.SECONDS.sleep(1);
//            //popupWindow.dismiss();
//
//        } catch(InterruptedException e) {
//            System.out.println("got interrupted!");
//        }
        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void getcurrentwords(View view, String word_display) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.current_words, null);
        //popupView.setText("sdf");
        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //((TextView)popupWindow.getContentView().findViewById(R.id.word_view).setText("Hi"));
        TextView text_word = (TextView) popupView.findViewById(R.id.current_words_textview);
        text_word.setTypeface(null, Typeface.BOLD);
        text_word.setText(word_display);
        // show the popup window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(20);
        }
        popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);
//        try {
//            TimeUnit.SECONDS.sleep(1);
//            //popupWindow.dismiss();
//
//        } catch(InterruptedException e) {
//            System.out.println("got interrupted!");
//        }
        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }


    public void tvsetbackcolor(TextView tv){
        if(tv.getText() != " ")
            tv.setBackgroundColor(Color.parseColor("#e8e9ed"));
        else
            tv.setBackgroundColor(Color.TRANSPARENT);//parseColor("#83F0FC"));
    }

    //Save state for rotation
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //
        savedInstanceState.putInt("gridsize",gridSize);
        savedInstanceState.putStringArray("GRIDV", gameArray);
        savedInstanceState.putStringArrayList("GRIDLIST", gridList);
        savedInstanceState.putInt("COUNTER_LANG", counterListeningLang);
        savedInstanceState.putInt("COUNTER", counter);
        savedInstanceState.putInt("COUNTER_LIST", counterListening);
        savedInstanceState.putStringArray("intGameArray", intGameArray);
        savedInstanceState.putStringArray("solution", solution);
        savedInstanceState.putStringArray("intSolution", intSolution);
        savedInstanceState.putParcelable("statsTracker", mStatsTracker);
        savedInstanceState.putInt("gameCount", gameCount);

    }

    public Map<String, int[]> gethashmap(){
        return wordsHashMap;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        gridSize = savedInstanceState.getInt("gridsize");
        gameArray = savedInstanceState.getStringArray("GRIDV");
        gridList = savedInstanceState.getStringArrayList("GRIDLIST");
        counterListeningLang = savedInstanceState.getInt("COUNTER_LANG");
        counter = savedInstanceState.getInt("COUNTER");
        counterListening = savedInstanceState.getInt("COUNTER_LIST");
        intGameArray = savedInstanceState.getStringArray("intGameArray");
        solution = savedInstanceState.getStringArray("solution");
        intSolution = savedInstanceState.getStringArray("intSolution");
        mStatsTracker = savedInstanceState.getParcelable("statsTracker");
        gameCount = savedInstanceState.getInt("gameCount");

    }

    private void startTimer(Chronometer ch)
    {
        ch = findViewById(R.id.chronometer);
        ch.start();
    }

    private void endGame()
    {
        chronometer.stop();
        Toast toast = Toast.makeText(MainActivity.this, "Finished! You got "+ Integer.toString(mStatsTracker.getScore()) + " points!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();

        saveInfo(mStatsTracker.getScore());


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(MainActivity.this, MainMenuActivity.class);
                startActivity(i);
            }
        }, 3000);
    }

    public void saveInfo(int intGameScore)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("GameScore", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("gameScore", mStatsTracker.getScore());
        editor.apply();

    }

}
