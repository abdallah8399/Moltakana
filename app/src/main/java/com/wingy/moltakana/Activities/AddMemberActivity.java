package com.wingy.moltakana.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.rgb;

public class AddMemberActivity extends AppCompatActivity {
    private Button cancel, add;
    private CheckBox block_device, stop, fire, mic, delete_text, general_message,
    cancel_block, logout_history, account_management, manage_member,
    manage_admin, manage_super_admin, manage_master, manage_room,
            supervisors_report, close_device;

    private Boolean block_deviceB, stopB, fireB, micB, delete_textB, general_messageB,
            cancel_blockB, logout_historyB, account_managementB, manage_memberB,
            manage_adminB, manage_super_adminB, manage_masterB, manage_roomB,
            supervisors_reportB, close_deviceB=false;
    private Spinner userTypeSpinner;
    private ArrayAdapter<String> userTypeAdapter;
    private List<String> types;
    private DatabaseReference permRef, userRef;
    private ProgressDialog progressDialog;
    private int memberType=0;
    private int count= -1;

    private EditText etUsername, etPassword;
    private int color=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        initializeIDs();
        clickListener();
        color= rgb(0,0,0);

        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("جاري اضافة الحساب");

        permRef= FirebaseDatabase.getInstance().getReference("Members_Permissions");
        userRef= FirebaseDatabase.getInstance().getReference("ChatRooms");
        getCount();

        types= new ArrayList<>();
        types.add("Member");
        types.add("Admin");
        types.add("Super Admin");
        types.add("Master");
        types.add("Master Girl");

        userTypeAdapter= new ArrayAdapter<String>(AddMemberActivity.this, android.R.layout.simple_spinner_dropdown_item, types);
        userTypeSpinner.setAdapter(userTypeAdapter);

        userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 1: adminSelected();
                    break;
                    case 2: superAdminSelected();
                    break;
                    case 3: masterSelected();
                    break;
                    case 4: masterGirlSelected();
                    break;
                    default: memberSelected();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        userTypeSpinner.setSelection(0);

    }

    private void adminSelected(){
        color= rgb(0,0,255);
        memberType= MainActivity.ADMIN;
        block_deviceB= true;
        stopB=true;
        fireB=true;
        micB=true;
        delete_textB=true;
        general_messageB=true;
        cancel_blockB=false;
        logout_historyB=false;
        account_managementB=false;
        manage_memberB=false;
        manage_adminB=false;
        manage_super_adminB=false;
        manage_masterB=false;
        manage_roomB=false;
        supervisors_reportB=false;

        block_device.setChecked(true);
        stop.setChecked(true);
        fire.setChecked(true);
        mic.setChecked(true);
        delete_text.setChecked(true);
        general_message.setChecked(true);
        cancel_block.setChecked(false);
        logout_history.setChecked(false);
        account_management.setChecked(false);
        manage_member.setChecked(false);
        manage_admin.setChecked(false);
        manage_super_admin.setChecked(false);
        manage_master.setChecked(false);
        manage_room.setChecked(false);
        supervisors_report.setChecked(false);
    }

    private void superAdminSelected(){

        color=rgb(0,255,0);

        memberType= MainActivity.SUPER_ADMIN;
        block_deviceB= true;
        stopB=true;
        fireB=true;
        micB=true;
        delete_textB=true;
        general_messageB=true;
        cancel_blockB=true;
        logout_historyB=true;
        account_managementB=false;
        manage_memberB=false;
        manage_adminB=false;
        manage_super_adminB=false;
        manage_masterB=false;
        manage_roomB=false;
        supervisors_reportB=false;

        block_device.setChecked(true);
        stop.setChecked(true);
        fire.setChecked(true);
        mic.setChecked(true);
        delete_text.setChecked(true);
        general_message.setChecked(true);
        cancel_block.setChecked(true);
        logout_history.setChecked(true);
        account_management.setChecked(false);
        manage_member.setChecked(false);
        manage_admin.setChecked(false);
        manage_super_admin.setChecked(false);
        manage_master.setChecked(false);
        manage_room.setChecked(false);
        supervisors_report.setChecked(false);
    }

    private void masterSelected(){
        color=rgb(255,0,0);


        memberType= MainActivity.MASTER;
        block_deviceB= true;
        stopB=true;
        fireB=true;
        micB=true;
        delete_textB=true;
        general_messageB=true;
        cancel_blockB=true;
        logout_historyB=true;
        account_managementB=true;
        manage_memberB=true;
        manage_adminB=true;
        manage_super_adminB=true;
        manage_masterB=true;
        manage_roomB=true;
        supervisors_reportB=true;

        block_device.setChecked(true);
        stop.setChecked(true);
        fire.setChecked(true);
        mic.setChecked(true);
        delete_text.setChecked(true);
        general_message.setChecked(true);
        cancel_block.setChecked(true);
        logout_history.setChecked(true);
        account_management.setChecked(true);
        manage_member.setChecked(true);
        manage_admin.setChecked(true);
        manage_super_admin.setChecked(true);
        manage_master.setChecked(true);
        manage_room.setChecked(true);
        supervisors_report.setChecked(true);
    }

    private void masterGirlSelected(){
        color=rgb(255,0,255);

        memberType= MainActivity.MASTER_GIRL;
        block_deviceB= true;
        stopB=true;
        fireB=true;
        micB=true;
        delete_textB=true;
        general_messageB=true;
        cancel_blockB=true;
        logout_historyB=true;
        account_managementB=true;
        manage_memberB=true;
        manage_adminB=true;
        manage_super_adminB=true;
        manage_masterB=true;
        manage_roomB=true;
        supervisors_reportB=true;

        block_device.setChecked(true);
        stop.setChecked(true);
        fire.setChecked(true);
        mic.setChecked(true);
        delete_text.setChecked(true);
        general_message.setChecked(true);
        cancel_block.setChecked(true);
        logout_history.setChecked(true);
        account_management.setChecked(true);
        manage_member.setChecked(true);
        manage_admin.setChecked(true);
        manage_super_admin.setChecked(true);
        manage_master.setChecked(true);
        manage_room.setChecked(true);
        supervisors_report.setChecked(true);
    }


    private void memberSelected(){
        color=rgb(128,0,128);


        memberType= MainActivity.MEMBER;
        block_deviceB= false;
        stopB=false;
        fireB=false;
        micB=false;
        delete_textB=false;
        general_messageB=false;
        cancel_blockB=false;
        logout_historyB=false;
        account_managementB=false;
        manage_memberB=false;
        manage_adminB=false;
        manage_super_adminB=false;
        manage_masterB=false;
        manage_roomB=false;
        supervisors_reportB=false;

        block_device.setChecked(false);
        stop.setChecked(false);
        fire.setChecked(false);
        mic.setChecked(false);
        delete_text.setChecked(false);
        general_message.setChecked(false);
        cancel_block.setChecked(false);
        logout_history.setChecked(false);
        account_management.setChecked(false);
        manage_member.setChecked(false);
        manage_admin.setChecked(false);
        manage_super_admin.setChecked(false);
        manage_master.setChecked(false);
        manage_room.setChecked(false);
        supervisors_report.setChecked(false);
    }

    private void getCount(){
        userRef.child(ManageRoomActivity.position_major).child("roomMinors").child(ManageRoomActivity.position_minor)
                .child("roomMembers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count= (int) dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void clickListener(){
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddMemberActivity.this.finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (count!= -1){
                    progressDialog.show();

                final MemberPermission memberPermission = new MemberPermission(FirebaseAuth.getInstance().getUid(), block_deviceB, stopB, fireB, micB, delete_textB, general_messageB,
                        cancel_blockB, logout_historyB, account_managementB, manage_memberB,
                        manage_adminB, manage_super_adminB, manage_masterB, manage_roomB,
                        supervisors_reportB, close_deviceB);


                FirebaseAuth.getInstance().createUserWithEmailAndPassword(etUsername.getText().toString() + "@gmail.com", etPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                userRef.child(ManageRoomActivity.position_major).child("roomMinors").child(ManageRoomActivity.position_minor)
                                        .child("roomMembers").child(String.valueOf(count)).setValue(new Member(etUsername.getText().toString(),
                                        "", "", memberType, authResult.getUser().getUid(), color , 20, true, false, false));

                                permRef.child(authResult.getUser().getUid()).setValue(memberPermission).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Toast.makeText(AddMemberActivity.this, "تم اضافة الحساب", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        });
            }else
                    Toast.makeText(AddMemberActivity.this, "حاول مرة اخرى", Toast.LENGTH_SHORT).show();
        }
        });
        stop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    stopB=true;

                }else{
                    stopB=false;

                }
            }
        });

        fire.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    fireB= true;
                }else{
                    fireB=false;
                }
            }
        });

        mic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    micB=true;
                }else{
                    micB=false;
                }
            }
        });

        block_device.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    block_deviceB=true;
                }else{
                    block_deviceB=false;
                }
            }
        });
        delete_text.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    delete_textB=true;
                }else{
                    delete_textB=false;
                }
            }
        });
        general_message.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    general_messageB=true;
                }else{
                    general_messageB=false;
                }
            }
        });
        cancel_block.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    cancel_blockB=true;
                }else{
                    cancel_blockB=false;
                }
            }
        });
        logout_history.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    logout_historyB=true;
                }else{
                    logout_historyB=false;
                }
            }
        });
        account_management.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    account_managementB=true;
                }else{
                    account_managementB=false;
                }
            }
        });
        manage_member.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    manage_memberB=true;
                }else{
                    manage_memberB=false;
                }
            }
        });
        manage_admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    manage_adminB=true;
                }else{
                    manage_adminB=false;
                }
            }
        });
        manage_super_admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    manage_super_adminB=true;
                }else{
                    manage_super_adminB=false;
                }
            }
        });
        manage_master.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    manage_masterB=true;
                }else{
                    manage_masterB=false;
                }
            }
        });
        manage_room.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    manage_roomB= true;
                }else{
                    manage_roomB= false;
                }
            }
        });
        supervisors_report.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    supervisors_reportB=true;
                }else{
                    supervisors_reportB=false;
                }
            }
        });
        close_device.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    close_deviceB=true;
                }else{
                    close_deviceB=false;
                }
            }
        });


    }

    private void initializeIDs(){
        cancel= findViewById(R.id.btn_add_member_cancel);
        add= findViewById(R.id.btn_add_member_add);
        block_device= findViewById(R.id.cb_add_member_block_device);
        stop= findViewById(R.id.cb_add_member_stop);
        fire= findViewById(R.id.cb_add_member_fire);
        mic= findViewById(R.id.cb_add_member_mic);
        delete_text= findViewById(R.id.cb_add_member_delete_text);
        general_message= findViewById(R.id.cb_add_member_general_message);
        cancel_block= findViewById(R.id.cb_add_member_cancel_block);
        logout_history= findViewById(R.id.cb_add_member_logout_history);
        account_management= findViewById(R.id.cb_add_member_account_management);
        manage_member= findViewById(R.id.cb_add_member_manage_member);
        manage_admin= findViewById(R.id.cb_add_member_manage_admin);
        manage_super_admin= findViewById(R.id.cb_add_member_manage_super_admin);
        manage_master= findViewById(R.id.cb_add_member_manage_master);
        manage_room= findViewById(R.id.cb_add_member_manage_room);
        supervisors_report= findViewById(R.id.cb_add_member_supervisors_report);
        close_device= findViewById(R.id.cb_add_member_close_device);
        etPassword= findViewById(R.id.et_add_member_password);
        etUsername= findViewById(R.id.et_add_member_username);
        userTypeSpinner= findViewById(R.id.sp_add_member_userType);
    }
}
