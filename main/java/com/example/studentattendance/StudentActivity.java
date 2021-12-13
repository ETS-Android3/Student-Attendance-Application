package com.example.studentattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {

    Toolbar toolbar;
    private String className;
    private String subjectName;
    private int position;
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<StudentItem> studentItems = new ArrayList<>();
    private Database database;
    private long cid;
    private CalendarActivity calendar;
    private TextView subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        calendar = new CalendarActivity();
        Intent intent = getIntent();
        className = intent.getStringExtra("className");
        subjectName = intent.getStringExtra("subjectName");
        position = intent.getIntExtra("position",-1);
        cid = intent.getLongExtra("cid",-1);

        setToolbar();

        database = new Database(this);
        loadData();

        recyclerView = findViewById(R.id.studentrvid);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StudentAdapter(this,studentItems);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new StudentAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                changeStatus(position);
            }
        });

        loadStatusData();
    }

    private void loadData() {
        Cursor cursor = database.getStudentTable(cid);
        studentItems.clear();

        while (cursor.moveToNext()){
            long sid = cursor.getLong(cursor.getColumnIndex(database.S_ID));
            int roll = cursor.getInt(cursor.getColumnIndex(database.STUDENT_ROLL_KEY));
            String name = cursor.getString(cursor.getColumnIndex(database.STUDENT_NAME_KEY));

            studentItems.add(new StudentItem(sid,roll,name));
        }
        cursor.close();
    }

    private void changeStatus(int position) {
        String status = studentItems.get(position).getStatus();
        if (status.equals("P"))
            status="A";
        else
            status ="P";

        studentItems.get(position).setStatus(status);
        adapter.notifyItemChanged(position);
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbarid);
        TextView title = toolbar.findViewById(R.id.titletvid);
        subtitle = toolbar.findViewById(R.id.subtitletvid);
        ImageButton back = toolbar.findViewById(R.id.backbtnid);
        ImageButton save = toolbar.findViewById(R.id.savebtnid);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(StudentItem studentItem : studentItems){
                    String status = studentItem.getStatus();
                    if(status!="P")
                        status = "A";
                    long value = database.addStatus(studentItem.getSid(),cid,calendar.getDate(),status);

                    if(value == -1)
                        database.updateStatus(studentItem.getSid(),calendar.getDate(),status);
                }
            }
        });

        title.setText(className);
        subtitle.setText(subjectName+" | "+calendar.getDate());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        toolbar.inflateMenu(R.menu.student_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.add_student_id){
                    showAddStudentDialog();
                }
                else if(menuItem.getItemId()==R.id.show_calendar_id){
                    showCalendar();
                }
                else if(menuItem.getItemId()==R.id.show_attendance_sheet_id){
                    openSheetList();
                }
                return true;
            }
        });
    }

    private void openSheetList() {
        long[] idArray = new long[studentItems.size()];
        String[] nameArray = new String[studentItems.size()];
        int[] rollArray = new int[studentItems.size()];

        for(int i=0; i<idArray.length; i++)
            idArray[i] = studentItems.get(i).getSid();
        for(int i=0; i<rollArray.length; i++)
            rollArray[i] = studentItems.get(i).getRollno();
        for(int i=0; i<nameArray.length; i++)
            nameArray[i] = studentItems.get(i).getName();

        Intent intent = new Intent(this, SheetListActivity.class);
        intent.putExtra("cid",cid);
        intent.putExtra("idArray",idArray);
        intent.putExtra("rollArray",rollArray);
        intent.putExtra("nameArray",nameArray);
        intent.putExtra("classname",className);
        intent.putExtra("subjectname",subjectName);

        startActivity(intent);
    }

    private void loadStatusData(){
        for(StudentItem studentItem : studentItems){
            String status = database.getStatus(studentItem.getSid(), calendar.getDate());
            if(status != null)
                studentItem.setStatus(status);
            else
                studentItem.setStatus("");
        }
        adapter.notifyDataSetChanged();
    }

    private void showCalendar() {
        calendar.show(getSupportFragmentManager(),"");
        calendar.setOnCalendarOkClickListener(this::onCalendarOkClicked);
    }

    private void onCalendarOkClicked(int year, int month, int day) {
        calendar.setDate(year, month, day);
        subtitle.setText(subjectName+ " | "+calendar.getDate());
        loadStatusData();
    }

    private void showAddStudentDialog() {
        DialogBox dialog = new DialogBox();
        dialog.show(getSupportFragmentManager(),DialogBox.STUDENT_ADD_DIALOG);
        dialog.setListener(new DialogBox.OnClickListener() {
            @Override
            public void onClick(String roll_string, String name) {
                int rollno = Integer.parseInt(roll_string);
                long sid = database.addStudent(cid, rollno, name);
                StudentItem studentItem = new StudentItem(sid,rollno,name);
                studentItems.add(studentItem);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 0:
                showUpdateStudentDialog(item.getGroupId());
                break;
            case 1:
                deleteStudent(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateStudentDialog(final int position) {
        DialogBox dialog = new DialogBox(studentItems.get(position).getRollno(),studentItems.get(position).getName());
        dialog.show(getSupportFragmentManager(),DialogBox.STUDENT_UPDATE_DIALOG);
        dialog.setListener(new DialogBox.OnClickListener(){
            @Override
            public void onClick(String roll_string,String name) {
                updateStudent(position,name);
            }
        });
    }

    private void updateStudent(int position, String name) {
        database.updateStudent(studentItems.get(position).getSid(), name);
        studentItems.get(position).setName(name);
        adapter.notifyItemChanged(position);
    }

    private void deleteStudent(int position) {
        database.deleteStudent(studentItems.get(position).getSid());
        adapter.notifyItemRemoved(position);
        studentItems.remove(position);
    }
}