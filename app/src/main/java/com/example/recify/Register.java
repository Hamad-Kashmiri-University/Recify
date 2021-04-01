package com.example.recify;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    //vars
    TextInputLayout formName, formUsername, formPassword, formEmail;
    Button formSubmit, formLogin;

    //firebase
    FirebaseDatabase db;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        //hooks
        formName = findViewById(R.id.name);
        formUsername = findViewById(R.id.user);
        formPassword = findViewById(R.id.email);
        formEmail = findViewById(R.id.password);
        formSubmit = findViewById(R.id.formSubmit);
        formLogin = findViewById(R.id.formLogin);

        //store data to firebase when submit  is pressed
        formSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = FirebaseDatabase.getInstance();
                ref = db.getReference("/users");

                ref.setValue("test");


            }
        });

    }
}