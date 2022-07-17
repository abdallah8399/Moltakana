package com.wingy.moltakana.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wingy.moltakana.Objects.Member;
import com.wingy.moltakana.Objects.MemberPermission;
import com.wingy.moltakana.R;

import yuku.ambilwarna.AmbilWarnaDialog;

public class SettingsActivity extends AppCompatActivity {
    private Button btnActivatePrivate, btnSave, btnColorPicker;
    private TextView sizeExample;
    private RadioGroup radioLangGroup;
    private SeekBar sizeBar;
    private CheckBox stopMic;
    private int size=20, defaultColor, MemberOrVisitor;
    private ProgressDialog progressDialog;
    private DatabaseReference chatRef, visitorRef;
    private String position_major, position_minor, position_user;
    private Member currentMember;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        InitializeIDs();
        position_major= getIntent().getStringExtra("position_major");
        position_minor= getIntent().getStringExtra("position_minor");
        position_user= getIntent().getStringExtra("position_user");
        MemberOrVisitor=getIntent().getIntExtra("MemberOrVisitor",0);
        visitorRef= FirebaseDatabase.getInstance().getReference("Visitors");

        chatRef= FirebaseDatabase.getInstance().getReference("ChatRooms").child(position_major)
                .child("roomMinors").child(position_minor).child("roomMembers").child(position_user);
        if(MemberOrVisitor==MainActivity.MEMBER)
            SetPrimSettings();
        else
            visitorRef.child(position_major).child(position_minor).child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentMember=dataSnapshot.getValue(Member.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        btnColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenColorPicker();
            }
        });

        sizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                size=i;
                currentMember.setTextSize(size);
                sizeExample.setTextSize(size);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        stopMic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    currentMember.setStopMic(b);
            }
        });

        btnActivatePrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentMember.getPrivateMessage()){
                    currentMember.setPrivateMessage(false);
                    btnActivatePrivate.setText("تفعيل");
                }else {
                    currentMember.setPrivateMessage(true);
                    btnActivatePrivate.setText("تعطيل");
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                if(MemberOrVisitor==MainActivity.MEMBER)
                     chatRef.setValue(currentMember).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        SettingsActivity.this.finish();
                         }
                });
                else  visitorRef.child(position_major).child(position_minor).child(FirebaseAuth.getInstance().getUid())
                        .setValue(currentMember).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                SettingsActivity.this.finish();
                            }
                        });
            }
        });


    }

    private void InitializeIDs(){
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("جاري الحفظ");
        btnActivatePrivate= findViewById(R.id.room_settings_activate_private_messages);
        sizeExample= findViewById(R.id.room_settings_size_example);
        sizeBar= findViewById(R.id.seekBar);
        stopMic= findViewById(R.id.cb_stop_mic);
        radioLangGroup= findViewById(R.id.room_settings_radio_group);
        btnSave= findViewById(R.id.btn_room_settings_save);
        btnColorPicker= findViewById(R.id.room_settings_color_picker);
        relativeLayout= findViewById(R.id.settings_layout);
    }

    private void SetPrimSettings(){
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentMember= dataSnapshot.getValue(Member.class);
                sizeExample.setTextSize(currentMember.getTextSize());
                sizeBar.setProgress(currentMember.getTextColor());
                if(currentMember.getArabic())
                    radioLangGroup.check(R.id.radioButton16);
                else
                    radioLangGroup.check(R.id.radioButton17);
                sizeExample.setTextColor(currentMember.getTextColor());
                if(currentMember.getStopMic())
                    stopMic.setChecked(true);
                else stopMic.setChecked(false);
                if(currentMember.getPrivateMessage())
                    btnActivatePrivate.setText("تعطيل");
                else btnActivatePrivate.setText("تفعيل");
                btnColorPicker.setBackgroundColor(currentMember.getTextColor());
                defaultColor= currentMember.getTextColor();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void OpenColorPicker(){
        AmbilWarnaDialog colorPicker= new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor= color;
                currentMember.setTextColor(color);
                btnColorPicker.setBackgroundColor(color);
            }

        });

        colorPicker.show();
    }
}
