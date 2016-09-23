package com.victor_xiao.sqlitedatabasetest;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private WordsDBHelper mDbHelper;

    private final static String words = "words";
    private final static String means = "means";
    private final static String samples = "samples";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView list = (ListView) findViewById(R.id.lstWords);
        registerForContextMenu(list);


        mDbHelper = new WordsDBHelper(this);

        mDbHelper.getWritableDatabase();

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

//        values.put(Words.Word.COLUMN_NAME_WORD, "apple");
//        values.put(Words.Word.COLUMN_NAME_MEANING, "苹果");
//        values.put(Words.Word.COLUMN_NAME_SAMPLE, "I ate an apple.");
//        db.insert(Words.Word.TABLE_NAME, null, values);
//        values.clear();
//
//        values.put(Words.Word.COLUMN_NAME_WORD, "red");
//        values.put(Words.Word.COLUMN_NAME_MEANING, "红色的");
//        values.put(Words.Word.COLUMN_NAME_SAMPLE, "Tom has a red pants.");
//        db.insert(Words.Word.TABLE_NAME, null, values);

        Button ins = (Button) findViewById(R.id.ins);
        ins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //新增单词
                InsertDialog();
            }
        });

        Button que = (Button) findViewById(R.id.qurey);
        que.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAll();
            }
        });

        Button Sea = (Button) findViewById(R.id.search);
        Sea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchDialog();
            }
        });


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.word_selected, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        TextView textWord = null;
        TextView textMeaning = null;
        TextView textSample = null;

        AdapterView.AdapterContextMenuInfo info = null;
        View itemView = null;

        switch (item.getItemId()) {
            case R.id.action_delete:
                Log.d("db", "deleting");
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                itemView = info.targetView;
                textWord = (TextView) itemView.findViewById(R.id.listword);
                if (textWord != null) {
                    String strWord = textWord.getText().toString();
                    deletedialog(strWord);
                }
                getAll();
                break;
            case R.id.action_edit:
                Log.d("db", "deleting");
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                itemView = info.targetView;
                textWord = (TextView) itemView.findViewById(R.id.listword);
                textMeaning = (TextView) itemView.findViewById(R.id.listmeaning);
                textSample = (TextView) itemView.findViewById(R.id.listsample);
                if (textWord != null) {
                    String strWord = textWord.getText().toString();
                    String strMeaning = textMeaning.getText().toString();
                    String strSample = textSample.getText().toString();
                    delete(strWord);

                    Log.d("db", "editing");
                    InsertDialog(strWord,strMeaning,strSample);
                }
                getAll();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void InsertDialog() {


        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final ContentValues values = new ContentValues();

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View v = inflater.inflate(R.layout.insert_dialog, null);

        builder.setView(v);

        final EditText word;
        final EditText meaning;
        final EditText sample;

        word = (EditText) v.findViewById(R.id.insword);
        meaning = (EditText) v.findViewById(R.id.insmean);
        sample = (EditText) v.findViewById(R.id.inssam);

        builder.setTitle("插入");


        builder.setPositiveButton("插入", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //login
                String tag = "db";

                String TextWord;
                String TextMeaning;
                String TextSam;


                TextWord = word.getText().toString();
                TextMeaning = meaning.getText().toString();
                TextSam = sample.getText().toString();

                Log.d(tag, TextWord);
                Log.d(tag, TextMeaning);
                Log.d(tag, TextSam);

                if (TextWord.equals("") || TextMeaning.equals("") || TextSam.equals("")) {
                    Log.d(tag, "something is empty");
                    if (TextWord.equals("")) {
                        Log.e(tag, "wrong,no word");
                        Toast.makeText(MainActivity.this, "错误，请输入单词",
                                Toast.LENGTH_LONG).show();
                        InsertDialog();
                    } else if (TextMeaning.equals("")) {
                        Log.e(tag, "wrong,no meaning");
                        Toast.makeText(MainActivity.this, "错误，请输入单词释义",
                                Toast.LENGTH_LONG).show();
                        InsertDialog();
                    } else {
                        Log.e(tag, "wrong,no Sample");
                        Toast.makeText(MainActivity.this, "错误，请输入单词例句",
                                Toast.LENGTH_LONG).show();
                        InsertDialog();
                    }
                } else {
                    Log.d(tag, "right");

                    values.put(Words.Word.COLUMN_NAME_WORD, TextWord);
                    values.put(Words.Word.COLUMN_NAME_MEANING, TextMeaning);
                    values.put(Words.Word.COLUMN_NAME_SAMPLE, TextSam);
                    db.insert(Words.Word.TABLE_NAME, null, values);
                    values.clear();

                    Toast.makeText(MainActivity.this, "成功插入单词", Toast.LENGTH_LONG).show();
                }
            }
        })

                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //cancel
                    }
                });
        builder.show();

    }

    private void InsertDialog(final String OldWord,final String OldMeaning,final String OldSample) {


        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final ContentValues values = new ContentValues();

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View v = inflater.inflate(R.layout.insert_dialog, null);

        builder.setView(v);

        final EditText word;
        final EditText meaning;
        final EditText sample;

        word = (EditText) v.findViewById(R.id.insword);
        meaning = (EditText) v.findViewById(R.id.insmean);
        sample = (EditText) v.findViewById(R.id.inssam);

        word.setText(OldWord);
        meaning.setText(OldMeaning);
        sample.setText(OldSample);

        builder.setTitle("插入");


        builder.setPositiveButton("插入", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //login
                String tag = "test1";

                String TextWord;
                String TextMeaning;
                String TextSam;


                TextWord = word.getText().toString();
                TextMeaning = meaning.getText().toString();
                TextSam = sample.getText().toString();

                Log.d(tag, TextWord);
                Log.d(tag, TextMeaning);
                Log.d(tag, TextSam);

                if (TextWord.equals("") || TextMeaning.equals("") || TextSam.equals("")) {
                    Log.d(tag, "something is empty");
                    if (TextWord.equals("")) {
                        Log.e(tag, "wrong,no word");
                        Toast.makeText(MainActivity.this, "错误，请输入单词",
                                Toast.LENGTH_LONG).show();
                        InsertDialog();
                    } else if (TextMeaning.equals("")) {
                        Log.e(tag, "wrong,no meaning");
                        Toast.makeText(MainActivity.this, "错误，请输入单词释义",
                                Toast.LENGTH_LONG).show();
                        InsertDialog();
                    } else {
                        Log.e(tag, "wrong,no Sample");
                        Toast.makeText(MainActivity.this, "错误，请输入单词例句",
                                Toast.LENGTH_LONG).show();
                        InsertDialog();
                    }
                } else {
                    Log.d(tag, "right");

                    values.put(Words.Word.COLUMN_NAME_WORD, TextWord);
                    values.put(Words.Word.COLUMN_NAME_MEANING, TextMeaning);
                    values.put(Words.Word.COLUMN_NAME_SAMPLE, TextSam);
                    db.insert(Words.Word.TABLE_NAME, null, values);
                    values.clear();

                    Toast.makeText(MainActivity.this, "成功插入单词", Toast.LENGTH_LONG).show();
                }
            }
        })

                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //cancel
                    }
                });
        builder.show();

    }


    private void getAll() {


        ListView list = (ListView) findViewById(R.id.lstWords);
        List<Map<String, Object>> items = new ArrayList<>();

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.query(Words.Word.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String word = cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_WORD));
                String mean = cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_MEANING));
                String sample = cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_SAMPLE));


                Map<String, Object> item = new HashMap<>();
                item.put(words, word);
                item.put(means, mean);
                item.put(samples, sample);
                items.add(item);

//                Log.d("db", "word is " + word);
//                Log.d("db", "meaning " + mean);
//                Log.d("db", "here is a sample: " + sample);
            } while (cursor.moveToNext());
        }
        cursor.close();

        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.word_list, new String[]{words, means, samples}, new int[]{R.id.listword, R.id.listmeaning, R.id.listsample});

        list.setAdapter(adapter);
    }

    private void deletedialog(final String strId) {
        new AlertDialog.Builder(this).setTitle("删除单词").setMessage("是否真的删除单词?").
                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete(strId);
                        getAll();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }

    private void delete(String strWord) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // 定义where子句
        String selection = Words.Word.COLUMN_NAME_WORD + " = ?";

        // 指定占位符对应的实际参数
        String[] selectionArgs = {strWord};

        // Issue SQL statement.
        db.delete(Words.Word.TABLE_NAME, selection, selectionArgs);
    }

    private void SearchDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View v = inflater.inflate(R.layout.search_dialog, null);

        builder.setView(v);

        final EditText SearchWord;

        SearchWord=(EditText)v.findViewById(R.id.txt_search_word);

        builder.setTitle("查找单词")//标题
                //确定按钮及其动作
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        Log.d("db",SearchWord.getText().toString());
                        String txtSearchWord=SearchWord.getText().toString();
                        Log.d("db",txtSearchWord);


                        ArrayList<Map<String, String>> items=null;

                         items=Search(txtSearchWord);

                        if(items.size()>0) {
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("result",items);
                            Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }else
                            Toast.makeText(MainActivity.this,"没有找到", Toast.LENGTH_LONG).show();


                    }


                })
                //取消按钮及其动作
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()//创建对话框
                .show();//显示对话框

    }


    private ArrayList<Map<String, String>> Search(String strWordSearch) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                Words.Word.COLUMN_NAME_WORD,
                Words.Word.COLUMN_NAME_MEANING,
                Words.Word.COLUMN_NAME_SAMPLE
        };

        String sortOrder =
                Words.Word.COLUMN_NAME_WORD + " DESC";

        String selection = Words.Word.COLUMN_NAME_WORD + " LIKE ?";
        String[] selectionArgs = {"%"+strWordSearch+"%"};

        Cursor c = db.query(
                Words.Word.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return ConvertCursor2List(c);
    }

    private ArrayList<Map<String, String>> ConvertCursor2List(Cursor cursor) {
        ArrayList<Map<String, String>> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            map.put(Words.Word.COLUMN_NAME_WORD, cursor.getString(0));
            map.put(Words.Word.COLUMN_NAME_MEANING, cursor.getString(1));
            map.put(Words.Word.COLUMN_NAME_SAMPLE, cursor.getString(2));
            result.add(map);
        }
        return result;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDbHelper.close();
    }

}
