package com.example.a201005;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db = null;

    private final static String CREATE_TABLE = "CREATE TABLE table01(_id INTEGER PRIMARY KEY," +
            "num INTEGER,data TEXT)";

    ListView LV01;
    Button BtD;
    EditText EdSQL;
    String str,ItDa;
    int n = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LV01 = (ListView)findViewById(R.id.LV01);
        BtD = (Button)findViewById(R.id.BtD);
        EdSQL = (EditText)findViewById(R.id.edtSQL);
        BtD.setOnClickListener(BDC);


        //預設SQL指令為新增資料
        ItDa = "資料項目" + n;
        str = "INSERT INTO table01(num,data)\n"+
                " values (" + n + ",'" + ItDa + "')";
        EdSQL.setText(str);

        //建立database
        db = openOrCreateDatabase("db1.db",MODE_PRIVATE,null);
        try {
            db.execSQL(CREATE_TABLE);
        }catch (Exception e){
            UpdataAdapter();
        }
    }

    //課本範例沒有能引導至此的程式
    protected void onDestroy() {
        super.onDestroy();
        //刪除原有的資料表，使每次執行都是空的
        db.execSQL("DROP TABLE table01");
        db.close();
    }

    private Button.OnClickListener BDC = new Button.OnClickListener() {
        public void onClick(View v){
            try {
                db.execSQL(EdSQL.getText().toString());
                UpdataAdapter();
                n++;
                ItDa = "資料項目" + n;
                str = "INSERT INTO table01(num,data) values(" + n + ",'" + ItDa + "')";
                EdSQL.setText(str);
                setTitle("資料新增完畢");
            }catch (Exception er) {
                setTitle("SQL語法錯誤!");
            }
            }
        };

    public void UpdataAdapter()
    {
        Cursor cursor = db.rawQuery("SELECT*FROM table01",null);
        if(cursor != null && cursor.getCount() >= 0){
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_2,
                    cursor,new String[]{"num","data"},
                    new int[]{android.R.id.text1,android.R.id.text2},0);
            LV01.setAdapter(adapter);
        }
    }
}