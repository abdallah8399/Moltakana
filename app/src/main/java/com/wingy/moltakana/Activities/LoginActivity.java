package com.wingy.moltakana.Activities;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.wingy.moltakana.R;

public class LoginActivity extends Activity {

    private Button btnLogin, btnMember, btnVisitor, btnMosgal;
    private EditText etUsername, etPassword;
    private String username,password;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    public static int VISITOR=1;
    public static int MEMBER=2;
    private int MemberOrVisitor = VISITOR;
    private String position, position1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DisplayMetrics displayMetrics= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width= displayMetrics.widthPixels;
        int height= 700;
        getWindow().setLayout((int)(width*.8),(int)(height));

        position = getIntent().getStringExtra("position_minor");
        position1 = getIntent().getStringExtra("position_major");

        btnLogin= (Button) findViewById(R.id.btnLogin);
        etUsername= (EditText) findViewById(R.id.etUsername);
        etPassword= (EditText) findViewById(R.id.etPassword);
        btnMember= findViewById(R.id.btnMember);
        btnMosgal= findViewById(R.id.btnMosgal);
        btnVisitor= findViewById(R.id.btnVisitor);


        etPassword.setVisibility(View.INVISIBLE);

        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("تسجيل الدخول...");
        mAuth= FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = etUsername.getText().toString().trim();

                if(MemberOrVisitor==MEMBER) {
                    password = etPassword.getText().toString();
                    if(!(username.isEmpty() || password.isEmpty())){
                        username += "@gmail.com";
                        SignIn(username, password);
                        progressDialog.show();
                    }else
                        Toast.makeText(LoginActivity.this, "enter a username and a password", Toast.LENGTH_SHORT).show();

                } else {
                    if(!username.isEmpty()) {
                        Intent intent = new Intent(LoginActivity.this, ChatRoomActivity.class);
                        intent.putExtra("MemberOrVisitor", VISITOR);
                        intent.putExtra("visitorName", username);
                        intent.putExtra("position_minor_loggedIn", position);
                        intent.putExtra("position_major_loggedIn", position1);
                        startActivity(intent);
                    }else
                        Toast.makeText(LoginActivity.this, "ادخل اسم الزائر", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnMosgal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLogin.setVisibility(View.INVISIBLE);
                etUsername.setVisibility(View.INVISIBLE);
                etPassword.setVisibility(View.INVISIBLE);
            }
        });

        btnVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MemberOrVisitor = VISITOR;
                etUsername.setHint("اسم الزائر");
                etPassword.setVisibility(View.INVISIBLE);
                btnLogin.setVisibility(View.VISIBLE);
                etUsername.setVisibility(View.VISIBLE);
            }
        });

        btnMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MemberOrVisitor = MEMBER;
                etPassword.setVisibility(View.VISIBLE);
                etUsername.setHint("اسم المستخدم");
                btnLogin.setVisibility(View.VISIBLE);
                etUsername.setVisibility(View.VISIBLE);
            }
        });

    }

    private void SignIn(final String mUsername, String mPassword){
        mAuth.signInWithEmailAndPassword(mUsername, mPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Intent intent= new Intent(LoginActivity.this, ChatRoomActivity.class);
                            intent.putExtra("MemberOrVisitor", MemberOrVisitor);
                            intent.putExtra("position_minor_loggedIn", position);
                            intent.putExtra("position_major_loggedIn", position1);
                            intent.putExtra("visitorName", mUsername);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "لم يتم تسجيل الدخول", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

}
