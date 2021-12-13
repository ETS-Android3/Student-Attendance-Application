package com.example.studentattendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SheetListActivity extends AppCompatActivity {
    Toolbar toolbar;
    private ListView sheetList;
    private ArrayAdapter adapter;
    private ArrayList<String> listItems = new ArrayList();
    private long cid;
    private String className, subjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_list);

        className = getIntent().getStringExtra("classname");
        subjectName = getIntent().getStringExtra("subjectname");
        setToolbar();

        cid = getIntent().getLongExtra("cid",-1);
        loadListItems();
        sheetList = findViewById(R.id.sheet_list_id);
        adapter = new ArrayAdapter(this, R.layout.sheet_list_layout,R.id.date_list_item_id, listItems);
        sheetList.setAdapter(adapter);


        sheetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openSheetActivity(position);
            }
        });
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbarid);
        TextView title = toolbar.findViewById(R.id.titletvid);
        TextView subtitle = toolbar.findViewById(R.id.subtitletvid);
        ImageButton back = toolbar.findViewById(R.id.backbtnid);
        ImageButton save = toolbar.findViewById(R.id.savebtnid);

        title.setText(className);
        subtitle.setText(subjectName);
        save.setVisibility(View.INVISIBLE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SheetListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void openSheetActivity(int position) {
        long[] idArray = getIntent().getLongArrayExtra("idArray");
        int[] rollArray = getIntent().getIntArrayExtra("rollArray");
        String[] nameArray = getIntent().getStringArrayExtra("nameArray");

        Intent intent = new Intent(this, SheetActivity.class);
        intent.putExtra("idArray",idArray);
        intent.putExtra("rollArray",rollArray);
        intent.putExtra("nameArray",nameArray);
        intent.putExtra("month",listItems.get(position));
        intent.putExtra("classname",className);
        intent.putExtra("subjectname",subjectName);

        startActivity(intent);
    }

    private void loadListItems() {
        Cursor cursor = new Database(this).getDistinctMonths(cid);

        while (cursor.moveToNext()){
            String date = cursor.getString(cursor.getColumnIndex(Database.DATE_KEY)); //25.11.2021
            listItems.add(date.substring(3));
        }
    }
}