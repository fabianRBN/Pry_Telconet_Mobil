package com.example.telconet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edt_username, edt_password;
    Button btn_submit, btn_delteData,btn_Ingresar, btn_loginData;
    DatabaseHelper myDb;

    ArrayAdapter adapter;
    ArrayList<String> arrayList;
    ListView listView;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        btn_submit = findViewById(R.id.btn_submit);
        btn_delteData = findViewById(R.id.btn_deleteData);
        btn_Ingresar = findViewById(R.id.btn_Ingresar);
        edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        listView = findViewById(R.id.listView);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);



        myDb = new DatabaseHelper(this);
        btn_submit.setOnClickListener(this);
        btn_delteData.setOnClickListener(this);
        arrayList= new ArrayList<>();
        if (arrayList != null) {
            adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.activity_listview, arrayList);
        }
        listView.setAdapter(adapter);

        //getAllData();

        //this is for realtime fetching of data
        Runnable runnable = new CountdownRunner();
        Thread thread = new Thread(runnable);
        //thread.start();

        btn_Ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean login = myDb.login(edt_username.getText().toString(), edt_password.getText().toString());
                if(login){
                    Intent intent = new Intent( view.getContext(), WelcomeActivity.class);
                    String message = edt_username.getText().toString();
                    intent.putExtra("USERNAME", message);
                    startActivity(intent);
                }else{
                    // Create the object of
                    // AlertDialog Builder class
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    // Set the message show for the Alert time
                    builder.setMessage("Datos incorrectos");

                    // Set Alert Title
                    builder.setTitle("Alerta !");

                    // Set Cancelable false
                    // for when the user clicks on the outside
                    // the Dialog Box then it will remain show
                    builder.setCancelable(false);

                    // Set the positive button with yes name
                    // OnClickListener method is use of
                    // DialogInterface interface.

                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                            {
                                // When the user click yes button
                                // then app will close
                                dialog.cancel();
                            }
                        });

                    // Set the Negative button with No name
                    // OnClickListener method is use
                    // of DialogInterface interface.
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {// If user click no
                            // then dialog box is canceled.
                             dialog.cancel();
                        }
                    });

                    // Create the Alert dialog
                    AlertDialog alertDialog = builder.create();

                    // Show the Alert Dialog box
                    alertDialog.show();
                }


            }
        });
    }

    //this is for realtime fetching of data
    class CountdownRunner implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()){
                try{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getAllData();
                        }
                    });
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btn_submit:
                boolean isInserted = myDb.insertData(edt_username.getText().toString(), edt_password.getText().toString());
                if (edt_username.getText().toString().trim().length() == 0 || edt_password.getText().toString().trim().length() == 0) {
                    Toast.makeText(this, "Please fill the fields", Toast.LENGTH_SHORT).show();
                }else{
                    if (isInserted == true) {
                        Toast.makeText(this, "Sucess", Toast.LENGTH_SHORT).show();
                        edt_password.requestFocus();
                        imm.hideSoftInputFromWindow(edt_password.getWindowToken(),0);

                    } else {
                        Toast.makeText(this, "Failed to insert", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.btn_deleteData:
                boolean isDeleted = myDb.deleteData(edt_username.getText().toString());
                if (isDeleted){
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                    edt_password.requestFocus();
                    imm.hideSoftInputFromWindow(edt_password.getWindowToken(),0);
                }else{
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                }

                break;



        }
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
