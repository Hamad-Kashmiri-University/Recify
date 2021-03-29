package com.example.recify;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {

    // vars
    Button register, submit;
    ImageView logo;
    TextView logotext, tagline;
    TextInputLayout username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // hooks
        register = findViewById(R.id.registerButton);
        logo = findViewById(R.id.logo);
        logotext = findViewById(R.id.logotext);
        username = findViewById(R.id.user);
        password = findViewById(R.id.password);
        tagline = findViewById(R.id.tagline);
        submit = findViewById(R.id.submit);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                //transition inspired by coding with tea
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View,String>(logo,"logotop");
                pairs[1] = new Pair<View,String>(logotext,"logobottom");
                pairs[2] = new Pair<View,String>(username,"user");
                pairs[3] = new Pair<View,String>(password,"pass");
                pairs[4] = new Pair<View,String>(tagline,"tagline");
                pairs[5] = new Pair<View,String>(submit,"submit");
                pairs[6] = new Pair<View,String>(register,"bottomtext");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);
                    startActivity(intent, options.toBundle());
                }

            }
        });
    }
}
