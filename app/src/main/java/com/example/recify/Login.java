package com.example.recify;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    // vars
    Button register, submit;
    ImageView logo;
    TextView logotext, tagline;
    TextInputLayout username, password;

    // google vars
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    Button googleBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       // overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //google sign in
        mAuth = FirebaseAuth.getInstance();
        createRequest();

        //google hook
        findViewById(R.id.googleButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


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

    //google request
    private void createRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    // on google sign in button press
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //handled sign in result when user clicks an email
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            }
            catch (ApiException e) {
                //Google Sign In failed, update UI appropriately
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    //google authentication with firebase
    private void firebaseAuthWithGoogle(GoogleSignInAccount idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("google firebase auth", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("google firebase auth", "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "auth failure", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private boolean usernameValidate() {
        String input = username.getEditText().getText().toString();
        if (input.isEmpty()) {
            username.setError("Please enter a username");
            return false;
        }
        else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    };

    private boolean passwordValidate() {
        String input = password.getEditText().getText().toString();

        if (input.isEmpty()) {
            password.setError("Please enter a password");
            return false;
        }
        else {
            password.setError(null);
            return true;
        }
    };


    public void login(View v){
        //check validation
        if (!usernameValidate() | !passwordValidate()){
            return;
        }
        else {
            userExists();
        }
    }

    private void userExists(){
        final String usernameInput = username.getEditText().getText().toString().trim();
        final String passwordInput = password.getEditText().getText().toString().trim();
        //loader
        final Loader loader = new Loader(Login.this);
        //db ref
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/users");
        //query to check pass for username entered
        Query userCheck = ref.orderByChild("username").equalTo(usernameInput);
        // if user there is val in snapshot, then check if exists, fetch password, match password from do to password input
        if (userCheck != null) {
            Log.d("query", "query " + userCheck);
        }
        userCheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if(snapshot.exists()){
                    for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                        // fetch vals from snapshot pls work
                        Log.v("1",""+ childDataSnapshot.getKey()); //displays the key for the node
                        Log.v("2",""+ childDataSnapshot.child("password").getValue());   //gives the value for given keyname
                        Log.v("3",""+ childDataSnapshot.child("name").getValue());   //gives the value for given keyname
                        Log.v("4",""+ childDataSnapshot.child("email").getValue());   //gives the value for given keyname
                        Log.v("5",""+ childDataSnapshot.child("username").getValue());   //gives the value for given keyname
                        String dbPassword = childDataSnapshot.child("password").getValue().toString();
                        String dbName = childDataSnapshot.child("name").getValue().toString();
                        String dbEmail = childDataSnapshot.child("email").getValue().toString();
                        String dbUsername = childDataSnapshot.child("username").getValue().toString();
                        Log.d("passworddb", "db " + dbPassword);
                        //remove errors if user exists
                        username.setError(null);
                        username.setErrorEnabled(false);

                        Log.d("check", "dbpass " + dbPassword + snapshot.hasChild("users") + snapshot.hasChild(usernameInput));

                        if(dbPassword.equals(passwordInput)){
                            loader.loadStart();
                            //remove errors is equals
                            username.setError(null);
                            username.setErrorEnabled(false);
                            // pass vals to next activity
                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                            intent.putExtra("name", dbName);
                            intent.putExtra("username", dbUsername);
                            intent.putExtra("password", dbPassword);
                            intent.putExtra("email", dbEmail);
                            startActivity(intent);
                        }
                        else{
                            password.setError("Incorrect Password");
                            password.requestFocus();
                        }

                    }

                }
                else{
                    username.setError("No user exists");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
