package com.bignerdranch.android.sudoku12;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ModifyTextActivity extends AppCompatActivity {

    String wordpairs;
    TextView output;
    public String[] separate;
    public String[] eWords;
    public String[] cWords;
    public int englishCount = 0;
    public int chineseCount = 0;
    private Button finishButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_text);

        Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
        Bundle extras = getIntent().getExtras();
        wordpairs = extras.getString("wordList");


//        Toast toast = Toast.makeText(ModifyText.this, wordpairs, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.TOP, 0, 0);
//        toast.show();
        separate = splitString(wordpairs,"\n");
        final int size = separate.length;

        final EditText[] ete = new EditText[size];
        final EditText[] etc = new EditText[size];
        eWords = new String[size];
        cWords = new String[size];
        for(int i = 0;i<size;i++)
        {
            String s = separate[i];
            Log.d("sep: ", s);
            String word[] = s.split(",");
            eWords[i] = word[0];
            cWords[i] = word[1];
            Log.d("words: ", eWords[i] + "    " + cWords[i]);
        }

        LinearLayout ll = (LinearLayout)findViewById(R.id.modifyTextLinear);

        for(int i = 0; i < size; i++){

//            LinearLayout layout = new LinearLayout(this);
//            layout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            //layout.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
//            layout.setLayoutParams(p);
            TextView titleView = new TextView(this);
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            titleView.setLayoutParams(lparams);
            titleView.setText("Word" + " " + Integer.toString(englishCount+1));
            titleView.setTextColor(Color.parseColor("#66bbff"));
            if(titleView.getParent() != null) {
                ((ViewGroup)titleView.getParent()).removeView(titleView); // <- fix
            }
            // setContentView(ll);
            ll.addView(titleView);

//
//            // add edittext
            ete[i] = new EditText(this);
            ete[i].setLayoutParams(p);
            if (eWords[englishCount] == null)
                ete[i].setHint("English Word");
            else
                ete[i].setText(eWords[englishCount]);
            ete[i].setId(englishCount + 1);
            ll.addView(ete[i]);
            etc[i] = new EditText(this);
            etc[i].setLayoutParams(p);
            if (cWords[englishCount] == null)
                etc[i].setHint("Chinese Word");
            else
                etc[i].setText(cWords[englishCount]);
            etc[i].setId(chineseCount - 1);
            ll.addView(etc[i]);
            englishCount++;
            chineseCount--;
            String test = ete[i].getText().toString();
            //Log.d("edittext: ", test);
        }

        finishButton = (Button) findViewById(R.id.finishbutton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < size; i++){
                    eWords[i] = ete[i].getText().toString();
                    cWords[i] = etc[i].getText().toString();
                    // Log.d("finish: ", eWords[i] + " " + cWords[i]);
                }
                String wordlist = "";
                for(int i = 0; i < size; i++){
                    wordlist = wordlist.concat(eWords[i] + ',' + cWords[i] + '\n');
                    Log.d("finish: ", wordlist);
                }
                Intent i = new Intent(ModifyTextActivity.this, MainMenuActivity.class);
                i.putExtra("wordList", wordlist);
                startActivity(i);
            }
        });


    }

    private String[] splitString(String string, String splitChar) {
        String[] separatedArray = string.split(splitChar);
        Log.d("modify separate: ", Integer.toString(separatedArray.length));

        return separatedArray;
    }
}
