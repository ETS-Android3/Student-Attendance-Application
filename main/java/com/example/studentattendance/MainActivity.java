package com.example.studentattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ClassAdapter classAdapter;
    FloatingActionButton floatingActionButton;
    ArrayList<Item> classItemArrayList = new ArrayList<>();

    //now the work on database using sqllite
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();
        setNavigationDrawerMenu();
        setRecyclerView();
        setFloatingActionButton();

        //database work
        database = new Database(this);
        loadData();
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbarid);
        setSupportActionBar(toolbar);
        TextView title = toolbar.findViewById(R.id.titletvid);
        TextView subtitle = toolbar.findViewById(R.id.subtitletvid);
        ImageButton save = toolbar.findViewById(R.id.savebtnid);
        ImageButton back = toolbar.findViewById(R.id.backbtnid);

        back.setVisibility(View.GONE);
        title.setText("Student Attendance");
        title.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        subtitle.setVisibility(View.GONE);
        save.setVisibility(View.INVISIBLE);
    }

    private void setNavigationDrawerMenu() {
        drawerLayout = findViewById(R.id.drawer_layout_id);
        navigationView = findViewById(R.id.nav_view_id);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,
                toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.normal));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            onDestroy();
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.recyclerviewid);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        classAdapter = new ClassAdapter(this, classItemArrayList);
        recyclerView.setAdapter(classAdapter);
        classAdapter.setOnItemClickListener(new ClassAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this,StudentActivity.class);
                intent.putExtra("className", classItemArrayList.get(position).getClassName());
                intent.putExtra("subjectName", classItemArrayList.get(position).getSubjectName());
                intent.putExtra("position",position);
                intent.putExtra("cid",classItemArrayList.get(position).getCid());

                startActivity(intent);
            }
        });
    }

    private void setFloatingActionButton() {
        floatingActionButton = findViewById(R.id.fabid);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addClass();
            }
        });
    }

    private void addClass() {
        DialogBox dialog = new DialogBox();
        dialog.show(getSupportFragmentManager(), DialogBox.CLASS_ADD_DIALOG);
        dialog.setListener(new DialogBox.OnClickListener(){
            @Override
            public void onClick(String className,String subjectName) {
                long cid = database.addClass(className,subjectName);
                Item item = new Item(cid,className,subjectName);
                classItemArrayList.add(item);
                classAdapter.notifyDataSetChanged();
            }
        });
    }

    private void loadData() {
        Cursor cursor = database.getClassTable();

        classItemArrayList.clear();
        while (cursor.moveToNext()){
            long cid = cursor.getLong(cursor.getColumnIndex(database.C_ID));
            String className = cursor.getString(cursor.getColumnIndex(database.CLASS_NAME_KEY));
            String subjectName = cursor.getString(cursor.getColumnIndex(database.SUBJECT_NAME_KEY));

            classItemArrayList.add(new Item(cid,className,subjectName));
        }
        cursor.close();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home_id:
                onBackPressed();
                break;
            case R.id.nav_add_class_id:
                onBackPressed();
                addClass();
                break;
            case R.id.nav_profile_id:
                onBackPressed();
                Dialog dialog = new Dialog(this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.header);
                dialog.show();
                break;
            case R.id.nav_logout_id:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_rate_id:
                onBackPressed();
                Dialog dialog2 = new Dialog(this);
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog2.setContentView(R.layout.rating_layout);
                dialog2.show();

                TextView rating = dialog2.findViewById(R.id.text_rating);
                Button submit = dialog2.findViewById(R.id.submit_btn);
                RatingBar ratingBar = dialog2.findViewById(R.id.rating_id);

                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        rating.setText(String.format("(%s)",v));
                    }
                });

                submit.setOnClickListener((new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this,"Thanks for the rating",Toast.LENGTH_SHORT).show();
                        dialog2.dismiss();
                    }
                }));
                break;
            case R.id.nav_share_id:
                onBackPressed();
                try {
                    Intent intent1 = new Intent(Intent.ACTION_SEND);
                    intent1.setType("text/plain");
                    String body = "Download this Application";
                    String subject = "You can download the app now by give a request on my number 8178675901 "
                            + "or you can download it by click on the download link: " +
                            "https://play.google.com/store/apps/details?=" + BuildConfig.APPLICATION_ID + "\n" +
                            "As of now this app is available to you only by me. So you can request for process. Thanks";
                    intent1.putExtra(Intent.EXTRA_SUBJECT, body);
                    intent1.putExtra(Intent.EXTRA_TEXT, subject);
                    startActivity(Intent.createChooser(intent1, "ShareVia"));
                }catch (Exception e){
                    Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 0:
                showUpdateDialog(item.getGroupId());
                break;
            case 1:
                deleteClass(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(final int position) {
        DialogBox dialog = new DialogBox();
        dialog.show(getSupportFragmentManager(),DialogBox.CLASS_UPDATE_DIALOG);
        dialog.setListener(new DialogBox.OnClickListener(){
            @Override
            public void onClick(String className,String subjectName) {
                updateClass(position,className,subjectName);
            }
        });
    }

    private void updateClass(int position, String className, String subjectName) {
        database.updateClass(classItemArrayList.get(position).getCid(),className,subjectName);
        classItemArrayList.get(position).setClassName(className);
        classItemArrayList.get(position).setSubjectName(subjectName);
        classAdapter.notifyItemChanged(position);

    }

    private void deleteClass(int position) {
        database.deleteClass(classItemArrayList.get(position).getCid());
        classAdapter.notifyItemRemoved(position);
        classItemArrayList.remove(position);
    }
}