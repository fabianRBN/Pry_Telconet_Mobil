package com.example.telconet;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    TextView txtWelcome;
    ArrayAdapter adapter;
    ArrayList<String> arrayList;
    ListView listView;
    Button btn_notificacion;
    InputMethodManager imm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String message = intent.getStringExtra("USERNAME");
        txtWelcome = findViewById(R.id.txt_welcome);
        txtWelcome.setText("Bienvenido "+message+" - Admin");

        btn_notificacion = findViewById(R.id.btn_notificacion);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Envio de Correo", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        listView = findViewById(R.id.listView);


        myDb = new DatabaseHelper(this);
        arrayList= new ArrayList<>();
        getAllData();
        if (arrayList != null) {
            adapter = new ArrayAdapter<String>(WelcomeActivity.this, R.layout.activity_listview, arrayList);
        }
        listView.setAdapter(adapter);


    }

    private void getAllData(){
        arrayList.clear();
        Cursor cursor = myDb.getAllData();
        if (cursor.getCount() == 0){
            Toast.makeText(this, "No Value", Toast.LENGTH_SHORT).show();
        }

        while (cursor.moveToNext()){
            arrayList.add(cursor.getString(0));
            listView.invalidateViews();
            Log.d("Main", "onClick: " + cursor.getString(0) + ", " + cursor.getString(1));
        }
    }

}
