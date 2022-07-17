package com.wingy.moltakana.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.wingy.moltakana.Objects.Member;
import com.wingy.moltakana.Objects.MinorChatRoom;
import com.wingy.moltakana.R;
import com.wingy.moltakana.Objects.RoomSettings;

import java.util.ArrayList;

import static android.graphics.Color.rgb;

public class AddMinorChatRoomActivity extends AppCompatActivity {
    private ImageView  imageViewMinor;
    private Button  btnAddMinor;
    private EditText etMinorChat, etOwnerName, etOwnerPassword;
    private Uri uri,UriMinor;
    private StorageReference mStorageRef;
    private String minorImageURL="";
    private Boolean imageUploaded= true;
    private DatabaseReference roomsRef;
    private ArrayList<Member> roomMembers;
    private int roomsCount= -1;
    private ProgressDialog progressDialog;
    private ArrayList<MinorChatRoom> minorChatRoomArrayList;
    private String position_major;
    private CheckBox cbVIP;
    private Boolean setVIP= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_minor_chat_room);


        minorChatRoomArrayList= new ArrayList<>();
        roomMembers= new ArrayList<>();
        position_major= getIntent().getStringExtra("position_major");

        imageViewMinor= findViewById(R.id.ivAddMinorChatImage);
        btnAddMinor= findViewById(R.id.btnAddMinorChatRoom);
        etMinorChat= findViewById(R.id.etAddMinorChatName);
        etOwnerName= findViewById(R.id.etAddOwnerrMinorChatName);
        etOwnerPassword= findViewById(R.id.etAddOwnerMinorChatPassword);
        cbVIP= findViewById(R.id.setVIP);

        cbVIP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setVIP=b;
            }
        });

        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("adding room...");

        mStorageRef= FirebaseStorage.getInstance().getReference("Images");
        roomsRef= FirebaseDatabase.getInstance().getReference("ChatRooms");
        getRoomsCount();


        btnAddMinor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etMinorChat.getText().toString().isEmpty() || etOwnerName.getText().toString().isEmpty())
                    Toast.makeText(AddMinorChatRoomActivity.this, "fill all data", Toast.LENGTH_SHORT).show();
                else if(etOwnerPassword.getText().toString().trim().isEmpty() || etOwnerPassword.getText().toString().trim().length() <= 8){
                    Toast.makeText(AddMinorChatRoomActivity.this, "choose password more than 8 characters", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.show();
                    RoomSettings roomSettings= new RoomSettings();

                    roomSettings.setRoomTitle(etMinorChat.getText().toString());
                    roomSettings.setWelcomeMessage("Welcome Message");
                    roomSettings.setVisitorTime(310);
                    roomSettings.setMemberTime(310);
                    roomSettings.setMasterTime(310);
                    roomSettings.setSAdminTime(310);
                    roomSettings.setAdminTime(310);
                    roomSettings.setCloseReason("");
                    roomSettings.setDesignURL(minorImageURL);


                    roomSettings.setTalk(ManageRoomActivity.ALL);
                    roomSettings.setCamera(ManageRoomActivity.ALL);
                    roomSettings.setChat(ManageRoomActivity.ALL);
                    roomSettings.setRoomClose(ManageRoomActivity.OPEN);


                    roomMembers.add(new Member(etOwnerName.getText().toString().trim(), "", "", MainActivity.MASTER, FirebaseAuth.getInstance().getUid()
                            ,rgb(0,0,0) , 20, true, false, false));
                    final MinorChatRoom minorChatRoom = new MinorChatRoom(minorImageURL, etMinorChat.getText().toString()
                            , etOwnerName.getText().toString(), roomMembers, roomSettings, setVIP);

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(etOwnerName.getText().toString().trim()+"@Moltkana.com"
                    , etOwnerPassword.getText().toString().trim());

                            roomsRef.child(position_major).child("roomMinors").child(String.valueOf(roomsCount)).setValue(minorChatRoom)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            etMinorChat.setText("");
                                            Toast.makeText(AddMinorChatRoomActivity.this, "minor chat added", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                }

            }
        });


        imageViewMinor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 11);

            }
        });


    }

    private String getExtension(Uri u){
        ContentResolver cr= getContentResolver();
        MimeTypeMap m= MimeTypeMap.getSingleton();
        return m.getExtensionFromMimeType(cr.getType(u));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 11 && resultCode == RESULT_OK && data != null && data.getData() != null){
            UriMinor= data.getData();

            Picasso.get().load(UriMinor).fit().centerCrop().into(imageViewMinor);

            Toast.makeText(AddMinorChatRoomActivity.this, "Uploading...", Toast.LENGTH_SHORT).show();
            imageUploaded= false;
            final StorageReference storageReference= mStorageRef.child(System.currentTimeMillis()+"."+ getExtension(UriMinor));
            storageReference.putFile(UriMinor).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddMinorChatRoomActivity.this, "Image Uploaded!", Toast.LENGTH_SHORT).show();
                    imageUploaded= true;

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            minorImageURL= uri.toString();

                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddMinorChatRoomActivity.this, "connection errors", Toast.LENGTH_SHORT).show();
                    minorImageURL="";
                    imageUploaded= true;
                }
            });

        }


    }

    private void getRoomsCount(){

        roomsRef.child(position_major).child("roomMinors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomsCount= (int) dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
