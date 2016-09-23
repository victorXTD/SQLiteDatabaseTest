package com.victor_xiao.sqlitedatabasetest;

/**
 * Created by Victor_Xiao on 16/09/09.
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        ArrayList<Map<String, String>> items= (ArrayList<Map<String, String>>) bundle.getSerializable("result");

        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.word_list,
                new String[]{Words.Word.COLUMN_NAME_WORD, Words.Word.COLUMN_NAME_MEANING, Words.Word.COLUMN_NAME_SAMPLE},
                new int[]{R.id.listword, R.id.listmeaning, R.id.listsample});

        ListView list = (ListView) findViewById(R.id.lastSearchResultWords);

        list.setAdapter(adapter);
    }
}