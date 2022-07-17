package com.wingy.moltakana.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.wingy.moltakana.R;

public class ChangePasswordActivity extends AppCompatActivity {
        private EditText etPassword;
        private Button btnChangePassword;
        private FirebaseAuth auth;
        private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar mToolbar = findViewById(R.id.change_pass_toolbar);
        mToolbar.setTitle("تغيير كلمة المرور");
        setSupportActionBar(mToolbar);

        btnChangePassword= findViewById(R.id.btnChangePass);
        etPassword= findViewById(R.id.etChangePass);

        auth= FirebaseAuth.getInstance();

        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("يتم تغيير كلمة السر");

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etPassword.getText().toString().length()<8)
                    Toast.makeText(ChangePasswordActivity.this, "ادخل كلمة سر اكبر من 8 احرف", Toast.LENGTH_SHORT).show();
                else {
                    progressDialog.show();
                    auth.getCurrentUser().updatePassword(etPassword.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(ChangePasswordActivity.this, "تم تغيير كلمة السر بنجاح", Toast.LENGTH_SHORT).show();
                            ChangePasswordActivity.this.finish();
                        }
                    });
                }
            }
        });
    }
}
