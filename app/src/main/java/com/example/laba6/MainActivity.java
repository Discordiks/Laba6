package com.example.laba6;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper=new DBHelper(getApplicationContext());
        try {
            database=dbHelper.getWritableDatabase(); //и читать, и писать
        } catch (Exception e){
            e.printStackTrace();
        }

        listView=findViewById(R.id.ListView);

        ArrayList<HashMap<String,String>>animals =new ArrayList<>(); //общий список
        HashMap <String,String> animal; //отдельная строка
        Cursor cursor = database.rawQuery("SELECT animals.id, animals.name AS \"Имя\", animal_types.name AS \"Тип\" FROM animals JOIN animal_types ON animal_types.id = animals.animal_type_id", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){ //сохраняем каждую строчку в массив
            animal=new HashMap<>();
            animal.put("name", cursor.getString(1));
            animal.put("type", cursor.getString(2));
            animals.add(animal);
            cursor.moveToNext();
        }
        cursor.close();

        SimpleAdapter adapter = new SimpleAdapter(
                getApplicationContext(),
                animals, android.R.layout.simple_list_item_2,
                new String[]{"name","type"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );
        listView.setAdapter(adapter);
    }
}