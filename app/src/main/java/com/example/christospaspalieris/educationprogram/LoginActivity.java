package com.example.christospaspalieris.educationprogram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAyth;
    private FirebaseAuth.AuthStateListener mfirebaseAyth;
    private DatabaseReference dbReferenceLogin;

    TextView textView_SignUP;
    EditText username,email,password;
    Button btn;
    private ProgressDialog progressLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAyth = FirebaseAuth.getInstance();
        mfirebaseAyth = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null)
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        };
        dbReferenceLogin= FirebaseDatabase.getInstance().getReference("USERS");

        textView_SignUP=(TextView)findViewById(R.id.textView_SignUp);

        username = (EditText) findViewById(R.id.UserName);
        email = (EditText) findViewById(R.id.EmailAddress);
        password = (EditText) findViewById(R.id.TextPassword);

        btn=(Button)findViewById(R.id.buttonSiginIn);

        progressLogin = new ProgressDialog(this);
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onClick(View view) {
        if(view==textView_SignUP) {
            firebaseAyth.addAuthStateListener(mfirebaseAyth);
            firebaseAyth.signOut();
        }
        if(view==btn){
            progressLogin.setMessage("Sign in User!!!");
            progressLogin.show();
            checkLogin();
        }
    }

    private void checkLogin() {
        String getusername = username.getText().toString().trim();
        String getemail = email.getText().toString().trim();
        String getpassword = password.getText().toString().trim();



        if(!TextUtils.isEmpty(getemail)&&!TextUtils.isEmpty(getpassword)&&!TextUtils.isEmpty(getusername)){
            firebaseAyth.signInWithEmailAndPassword(getemail,getpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        checkUserExist();
                        progressLogin.dismiss();
                    }else{
                        Toast.makeText(LoginActivity.this,"Error while Login",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void checkUserExist() {
        final String user_id = firebaseAyth.getCurrentUser().getUid();
        dbReferenceLogin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id)){
                    startActivity(new Intent(LoginActivity.this,EducationalProgramActivity.class));
                   // Toast.makeText(getApplicationContext(),"Hello !!!"+ user_id,Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(LoginActivity.this,"Error !!!",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
