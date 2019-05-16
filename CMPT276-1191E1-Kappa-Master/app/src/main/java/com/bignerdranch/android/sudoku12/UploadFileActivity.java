package com.bignerdranch.android.sudoku12;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

public class UploadFileActivity extends AppCompatActivity {
    private static final int READ_REQUEST_CODE = 42;
    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private Button mUploadButton;
    TextView output;
    String nullobject = "nullfile";
    String s = "test";
    public String wordM = new String();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadfile);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //        SharedPreferences sharedPreferences = getSharedPreferences("Words", Context.MODE_PRIVATE);
//        String wordlist = sharedPreferences.getString("words", "");
//        text_word.setText(wordlist);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
        }

        mUploadButton = (Button)findViewById(R.id.uploadFileButton);
        output = (TextView)  findViewById(R.id.output);
        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performFileSearch();
            }
        });

        //Log.d("wordsize: ", Integer.toString(wordListPairs.size()));
        String wordsHashMap = getWords();
        Log.d("wordsHashMap", wordsHashMap);
    }

    private String readText(String input)
    {
        Log.d("func: ", s);

        String words = new String();
        File file_internal = new File(input); // /storage/emulated/0/storage/emulated/0/Download/app.txt NO NEED TO GET EXTERNAL STORAGE SINCE WE ARE USING INTERNAL
        File file_external = new File(Environment.getExternalStorageDirectory(),input);
        StringBuilder text = new StringBuilder();
        Log.d("func1: ", s);

        try
        {
            //BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            Reader reader = new InputStreamReader(new FileInputStream(file_internal), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;

//            String tmp = null;
//            line = new String(tmp.getBytes(), "UTF-8");
            if(bufferedReader.readLine() == null)
            {
                Log.d("nullfile: ", nullobject);
            }

            while((line = bufferedReader.readLine())!= null)
            {
                //String newString = new String(line.getBytes("Unicode"), "UTF-8");
                text.append(line);
                words += line+"\n";
                Log.d("line: ", line);
                text.append("\n");
            }

            bufferedReader.close();
        }
        catch(IOException e)
        {
            Log.d("func3: ", s);
            e.printStackTrace();
            try
            {

                //BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

                Reader reader = new InputStreamReader(new FileInputStream(file_external), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line;

//            String tmp = null;
//            line = new String(tmp.getBytes(), "UTF-8");
                if(bufferedReader.readLine() == null)
                {
                    Log.d("nullfile: ", nullobject);
                }

                while((line = bufferedReader.readLine())!= null)
                {
                    //String newString = new String(line.getBytes("Unicode"), "UTF-8");
                    text.append(line);
                    words += line+"\n";
                    Log.d("line: ", line);
                    text.append("\n");
                }

                bufferedReader.close();
            }
            catch(IOException e_1)
            {
                Log.d("func3: ", s);
                Toast toast = Toast.makeText(UploadFileActivity.this, "Invalid input! Please check instructions", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();
                e.printStackTrace();
            }
        }
        wordM = words;
        Log.d("wordM", wordM);
        Log.d("func4: ", s);
        return wordM;
        //return text.toString();
    }

    private String getWords()
    {
        return wordM;
    }

    private void performFileSearch()
    {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, READ_REQUEST_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data)
    {
        String m = new String();
        //Log.d("test: ", s);
        if(requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            if(data == null)
            {
                Log.d("null: ", s);
            }

            if(data != null)
            {

                Uri uri = data.getData();
                String path = uri.getPath();
                Log.d("path: ", path);
                path = path.substring(path.indexOf(":") + 1);
                Toast.makeText(this,""+path,Toast.LENGTH_SHORT );
                m = readText((path));
                //output.setText(m); //Gonna set text in main menu or a new pop up
                Intent i = new Intent(this, ModifyTextActivity.class);
                i.putExtra("wordList",m);
                startActivity(i);

            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode == PERMISSION_REQUEST_STORAGE)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
