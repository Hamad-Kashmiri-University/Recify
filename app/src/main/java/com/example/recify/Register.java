package com.example.recify;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

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
        formPassword = findViewById(R.id.password);
        formEmail = findViewById(R.id.email);
        formSubmit = findViewById(R.id.formSubmit);
        formLogin = findViewById(R.id.formLogin);

        formLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                //transition inspired by coding with tea
                Pair[] pairs = new Pair[4];
                pairs[0] = new Pair<View,String>(formUsername,"user");
                pairs[1] = new Pair<View,String>(formPassword,"pass");
                pairs[2] = new Pair<View,String>(formSubmit,"submit");
                pairs[3] = new Pair<View,String>(formLogin,"bottomtext");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Register.this,pairs);
                    startActivity(intent, options.toBundle());
                }

            }
        });


    }

    //validate name
    public boolean nameValidate() {
        String input = formName.getEditText().getText().toString();

        if (input.isEmpty()) {
            formName.setError("Please enter a name");
            return false;
        }
        else {
            formName.setError(null);
            formName.setErrorEnabled(false);
            return true;
        }
    };

    public boolean usernameValidate() {
        String input = formUsername.getEditText().getText().toString();
        if (input.isEmpty()) {
            formUsername.setError("Please enter a username");
            return false;
        }
        else if(input.length() >= 12) {
            formUsername.setError("Username must be 12 or fewer characters");
            return false;
        }
        else {
            formUsername.setError(null);
            formUsername.setErrorEnabled(false);
            return true;
        }
    };

    public boolean usernameValidateTest(String input) {
        if (input.isEmpty()) {
            return false;
        }
        else if(input.length() >= 12) {
            return false;
        }
        else {
            return true;
        }
    };

    public boolean emailValidate() {
        String input = formEmail.getEditText().getText().toString();
        String emailRegEx = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (input.isEmpty()) {
            formEmail.setError("Please enter an email");
            return false;
        }
        else if (!input.matches(emailRegEx)){
            formEmail.setError("Email is Invalid");
            return false;
        }
        else {
            formEmail.setError(null);
            return true;
        }
    };

    public boolean emailValidateTest(String input) {
        String emailRegEx = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (input.isEmpty()) {
            return false;
        }
        else if (!input.matches(emailRegEx)){
            return false;
        }
        else {
            return true;
        }
    };

    public boolean passwordValidate() {
        String input = formPassword.getEditText().getText().toString();

        if (input.isEmpty()) {
            formPassword.setError("Please enter a password");
            return false;
        }
        else {
            formPassword.setError(null);
            return true;
        }
    };

    public boolean passwordValidateTest(String input) {

        if (input.isEmpty()) {
            return false;
        }
        else {
            return true;
        }
    };



    public void register (View v){
            //check validation
            if(!nameValidate() | !emailValidate() | !usernameValidate() | !passwordValidate()){
                return;
            }
            db = FirebaseDatabase.getInstance();
            ref = db.getReference("/users");

            // retrieve form values
            String name = formName.getEditText().getText().toString();
            String username = formUsername.getEditText().getText().toString();
            String email = formEmail.getEditText().getText().toString();
            String password = formPassword.getEditText().getText().toString();

            UserHelper User = new UserHelper(name, username, email, password);
        Toast.makeText(Register.this, "congrats a user has been added", Toast.LENGTH_LONG).show();

            //unique identifier for new users
            ref.child(name).setValue(User);
        }

}