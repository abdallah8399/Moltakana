package com.wingy.moltakana.Fragments;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import com.wingy.moltakana.Activities.ManageRoomActivity;
import com.wingy.moltakana.R;
import com.wingy.moltakana.Objects.RoomSettings;

import static android.app.Activity.RESULT_OK;


public class RoomSettingsFragment extends Fragment {
    private EditText roomTitle, welcomeMessage, visitorTime, memberTime,  adminTime, superAdminTime, masterTime, reasonToClose;
    private RadioGroup talkGroup, cameraGroup, messageGroup, closeRoomGroup;
    private ImageView changeDesign;
    private Button btnSave;
    private RoomSettings roomSettings;
    private DatabaseReference settingsRef;
    private String designURL="";
    private ProgressDialog progressDialog;
    private Uri uri;
    private StorageReference mStorageRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_room_settings, container, false);
        initializeIDs(view);
        settingsRef= FirebaseDatabase.getInstance().getReference("ChatRooms");
        roomSettings= new RoomSettings();
        setPrimarySettings();
        progressDialog= new ProgressDialog(getContext());
        progressDialog.setMessage("جاري الحفظ");
        mStorageRef= FirebaseStorage.getInstance().getReference("Designs");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!roomTitle.getText().toString().isEmpty()) {
                    progressDialog.show();
                    saveSettings();
                } else Toast.makeText(getContext(), "ادخل عنوان الغرفة", Toast.LENGTH_SHORT).show();
            }
        });

        changeDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 12);
            }
        });

        return view;
    }

    private String getExtension(Uri u){
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap m= MimeTypeMap.getSingleton();
        return m.getExtensionFromMimeType(cr.getType(u));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12 && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri= data.getData();

            Picasso.get().load(uri).fit().into(changeDesign);

            final StorageReference storageReference= mStorageRef.child(System.currentTimeMillis()+"."+ getExtension(uri));
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "تم رفع التصميم", Toast.LENGTH_SHORT).show();

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            designURL= uri.toString();
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "خطأ في رفع الصورة", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initializeIDs(View view){
        roomTitle= view.findViewById(R.id.editText);
        welcomeMessage= view.findViewById(R.id.editText2);
        visitorTime= view.findViewById(R.id.editText3);
        memberTime= view.findViewById(R.id.editText4);
        adminTime= view.findViewById(R.id.editText5);
        superAdminTime= view.findViewById(R.id.editText6);
        masterTime= view.findViewById(R.id.editText7);
        reasonToClose= view.findViewById(R.id.editText8);
        talkGroup= view.findViewById(R.id.rg_reasonToClose);
        cameraGroup= view.findViewById(R.id.rg_cameraGroup);
        messageGroup= view.findViewById(R.id.rg_messageGroup);
        closeRoomGroup= view.findViewById(R.id.rg_closeRoomGroup);
        changeDesign= view.findViewById(R.id.imageView3);
        reasonToClose= view.findViewById(R.id.editText8);
        btnSave= view.findViewById(R.id.button);
    }

    private void setPrimarySettings(){
        settingsRef.child(ManageRoomActivity.position_major).child("roomMinors").child(ManageRoomActivity.position_minor)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        roomSettings= dataSnapshot.child("roomSettings").getValue(RoomSettings.class);
                        roomTitle.setText(roomSettings.getRoomTitle());
                        welcomeMessage.setText(roomSettings.getWelcomeMessage());
                        visitorTime.setText(String.valueOf(roomSettings.getVisitorTime()));
                        memberTime.setText(String.valueOf(roomSettings.getMemberTime()));
                        masterTime.setText(String.valueOf(roomSettings.getMasterTime()));
                        superAdminTime.setText(String.valueOf(roomSettings.getSAdminTime()));
                        adminTime.setText(String.valueOf(roomSettings.getAdminTime()));
                        reasonToClose.setText(roomSettings.getCloseReason());

                        switch (roomSettings.getTalk()){
                            case ManageRoomActivity.ALL: talkGroup.check(R.id.radioButton);
                                break;
                            case ManageRoomActivity.MEM_SUPERVISOR: talkGroup.check(R.id.radioButton2);
                                break;
                            case ManageRoomActivity.SUPERVISOR: talkGroup.check(R.id.radioButton3);
                                break;
                            case ManageRoomActivity.NO_ONE: talkGroup.check(R.id.radioButton4);
                                break;
                        }
                        switch (roomSettings.getCamera()){
                            case ManageRoomActivity.ALL: cameraGroup.check(R.id.radioButton5);
                                break;
                            case ManageRoomActivity.MEM_SUPERVISOR: cameraGroup.check(R.id.radioButton6);
                                break;
                            case ManageRoomActivity.SUPERVISOR: cameraGroup.check(R.id.radioButton7);
                                break;
                            case ManageRoomActivity.NO_ONE: cameraGroup.check(R.id.radioButton8);
                                break;
                        }
                        switch (roomSettings.getChat()){
                                case ManageRoomActivity.ALL: messageGroup.check(R.id.radioButton9);
                                    break;
                                case ManageRoomActivity.MEM_SUPERVISOR: messageGroup.check(R.id.radioButton10);
                                    break;
                                case ManageRoomActivity.SUPERVISOR: messageGroup.check(R.id.radioButton11);
                                    break;
                                case ManageRoomActivity.NO_ONE: messageGroup.check(R.id.radioButton12);
                                    break;
                        }
                        switch (roomSettings.getRoomClose()){
                            case ManageRoomActivity.OPEN: messageGroup.check(R.id.radioButton13);
                                break;
                            case ManageRoomActivity.MEM_SUPERVISOR: messageGroup.check(R.id.radioButton14);
                                break;
                            case ManageRoomActivity.ENTRANCE_GATE: messageGroup.check(R.id.radioButton15);
                                break;

                        }
                        if(! roomSettings.getDesignURL().isEmpty())
                        Picasso.get().load(roomSettings.getDesignURL()).placeholder(R.drawable.ic_do_not_disturb_black_24dp).fit().into(changeDesign);



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void saveSettings(){

            roomSettings.setRoomTitle(roomTitle.getText().toString());
            roomSettings.setWelcomeMessage(welcomeMessage.getText().toString());
            roomSettings.setVisitorTime(Integer.parseInt(visitorTime.getText().toString()));
            roomSettings.setMemberTime(Integer.parseInt(memberTime.getText().toString()));
            roomSettings.setMasterTime(Integer.parseInt(masterTime.getText().toString()));
            roomSettings.setSAdminTime(Integer.parseInt(superAdminTime.getText().toString()));
            roomSettings.setAdminTime(Integer.parseInt(adminTime.getText().toString()));
            roomSettings.setCloseReason(reasonToClose.getText().toString());
            roomSettings.setDesignURL(designURL);



        switch (talkGroup.getCheckedRadioButtonId()){
            case R.id.radioButton: roomSettings.setTalk(ManageRoomActivity.ALL);
                break;
            case R.id.radioButton2: roomSettings.setTalk(ManageRoomActivity.MEM_SUPERVISOR);
                break;
            case R.id.radioButton3: roomSettings.setTalk(ManageRoomActivity.SUPERVISOR);
                break;
            case R.id.radioButton4: roomSettings.setTalk(ManageRoomActivity.NO_ONE);
                break;
        }
        switch (cameraGroup.getCheckedRadioButtonId()){
            case R.id.radioButton5: roomSettings.setCamera(ManageRoomActivity.ALL);
                break;
            case R.id.radioButton6: roomSettings.setCamera(ManageRoomActivity.MEM_SUPERVISOR);
                break;
            case R.id.radioButton7: roomSettings.setCamera(ManageRoomActivity.SUPERVISOR);
                break;
            case R.id.radioButton8: roomSettings.setCamera(ManageRoomActivity.NO_ONE);
                break;
        }
        switch (messageGroup.getCheckedRadioButtonId()){
            case R.id.radioButton9: roomSettings.setChat(ManageRoomActivity.ALL);
                break;
            case R.id.radioButton10: roomSettings.setChat(ManageRoomActivity.MEM_SUPERVISOR);
                break;
            case R.id.radioButton11: roomSettings.setChat(ManageRoomActivity.SUPERVISOR);
                break;
            case R.id.radioButton12: roomSettings.setChat(ManageRoomActivity.NO_ONE);
                break;
        }
        switch (closeRoomGroup.getCheckedRadioButtonId()){
            case R.id.radioButton13: roomSettings.setRoomClose(ManageRoomActivity.OPEN);
                break;
            case R.id.radioButton14: roomSettings.setRoomClose(ManageRoomActivity.MEM_SUPERVISOR);
                break;
            case R.id.radioButton15: roomSettings.setRoomClose(ManageRoomActivity.ENTRANCE_GATE);
                break;

        }

        settingsRef.child(ManageRoomActivity.position_major).child("roomMinors").child(ManageRoomActivity.position_minor).child("roomSettings")
                .setValue(roomSettings).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "تم الحفظ", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

}