package com.wingy.moltakana.Activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.wingy.moltakana.Objects.MajorChatRoom;
import com.wingy.moltakana.Objects.Member;
import com.wingy.moltakana.Objects.MinorChatRoom;
import com.wingy.moltakana.Objects.RoomSettings;
import com.wingy.moltakana.R;

import java.util.ArrayList;

import static android.graphics.Color.rgb;

public class AddChatRoomActivity extends AppCompatActivity {
    private ImageView imageView;
    private Button btnAdd;
    private EditText etName;
    private Uri uri;
    private StorageReference mStorageRef;
    private String imageURL="";
    private Boolean imageUploaded= true;
    private DatabaseReference roomsRef;
    private int roomsCount= -1;
    private ArrayList<MinorChatRoom> minorChatRoomArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chat_room);

        minorChatRoomArrayList= new ArrayList<>();

        imageView= findViewById(R.id.ivAddChatImage);
        btnAdd= findViewById(R.id.btnAddChatRoom);
        etName= findViewById(R.id.etAddChatName);

        mStorageRef= FirebaseStorage.getInstance().getReference("Images");
        roomsRef= FirebaseDatabase.getInstance().getReference("ChatRooms");
        getRoomsCount();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(! etName.getText().toString().isEmpty()){
                    if(imageUploaded) {
                        Member member= new Member("admin", "", "", MainActivity.MASTER, FirebaseAuth.getInstance().getUid()
                        , rgb(0,0,0) , 20, true, false, false);
                        ArrayList<Member> roomMembers= new ArrayList<>();
                        roomMembers.add(member);

                        RoomSettings roomSettings= new RoomSettings();
                        roomSettings.setRoomTitle(etName.getText().toString());
                        roomSettings.setWelcomeMessage("Welcome Message");
                        roomSettings.setVisitorTime(310);
                        roomSettings.setMemberTime(310);
                        roomSettings.setMasterTime(310);
                        roomSettings.setSAdminTime(310);
                        roomSettings.setAdminTime(310);
                        roomSettings.setCloseReason("");
                        roomSettings.setDesignURL(imageURL);


                        roomSettings.setTalk(ManageRoomActivity.ALL);
                        roomSettings.setCamera(ManageRoomActivity.ALL);
                        roomSettings.setChat(ManageRoomActivity.ALL);
                        roomSettings.setRoomClose(ManageRoomActivity.OPEN);


                        MinorChatRoom minorChatRoom= new MinorChatRoom(imageURL, etName.getText().toString()
                                , "admin", roomMembers,roomSettings , true);
                        minorChatRoomArrayList.add(minorChatRoom);
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword("admin@gmail.com", "123456789");

                        MajorChatRoom chatRoom = new MajorChatRoom(imageURL, etName.getText().toString(),
                                minorChatRoomArrayList);
                        addRoom(chatRoom);

                    }
                    else
                        Toast.makeText(AddChatRoomActivity.this, "wait for image to get uploaded", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(AddChatRoomActivity.this, "write a name for the chat room", Toast.LENGTH_SHORT).show();

            }
        });




        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 10);
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
        if (requestCode == 10 && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri= data.getData();

            Picasso.get().load(uri).fit().centerCrop().into(imageView);

            Toast.makeText(AddChatRoomActivity.this, "Uploading...", Toast.LENGTH_SHORT).show();
            imageUploaded= false;
            final StorageReference storageReference= mStorageRef.child(System.currentTimeMillis()+"."+ getExtension(uri));
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddChatRoomActivity.this, "Image Uploaded!", Toast.LENGTH_SHORT).show();
                    imageUploaded= true;



                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageURL= uri.toString();

                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddChatRoomActivity.this, "connection errors", Toast.LENGTH_SHORT).show();
                    imageUploaded= true;
                }
            });
        }


    }

    private void getRoomsCount(){

        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomsCount= (int) dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void addRoom(MajorChatRoom chatRoom){
        if (roomsCount >= 0){
            roomsRef.child(String.valueOf(roomsCount)).setValue(chatRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(AddChatRoomActivity.this, "Chat Room was added successfully", Toast.LENGTH_SHORT).show();
                    AddChatRoomActivity.this.finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddChatRoomActivity.this, "connection error", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            Toast.makeText(this, "try again...", Toast.LENGTH_SHORT).show();
            getRoomsCount();
        }
    }



}
