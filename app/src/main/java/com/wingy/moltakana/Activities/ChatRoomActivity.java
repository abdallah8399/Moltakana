package com.wingy.moltakana.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.wingy.moltakana.Fragments.RoomUsersFragment;
import com.wingy.moltakana.Objects.LogoutHistoryObject;
import com.wingy.moltakana.Objects.Member;
import com.wingy.moltakana.Objects.Message;
import com.wingy.moltakana.Adapters.MessageAdapter;
import com.wingy.moltakana.Objects.RoomSettings;
import com.wingy.moltakana.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

import static android.graphics.Color.rgb;

public class ChatRoomActivity  extends AppCompatActivity implements MessageAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private MessageAdapter mAdapter;
    private List<Message> mMinorChatList;
    private DatabaseReference chatRef, userRef, logoutRef, visitorsRef;
    private ImageView ivSend, ivEmoji, showMembers, ivMic, ivVideo, ivSound, ivMessage, ivOutSide_click;
    private int count= -1;
    private int prevCount=0, MemberOrVisitor;
    private String senderName = "";
    public static String position_major, position_minor;
    private View rootView;
    private EmojiconEditText etMessage;
    private EmojIconActions emojIcon;
    public static int VISITOR=1;
    public static int MEMBER=2;
    private String position_user="", timeIn, logoutCount;
    private Member sender;
    private FrameLayout userFrame;
    private  Toolbar mToolbar;
    private TextView toolbar_counter, toolbar_title;
    private int timerCounter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date dateIn = new Date();
        timeIn = dateFormat.format(dateIn);

        mToolbar = findViewById(R.id.main_app_bar);
        setSupportActionBar(mToolbar);
        showMembers= findViewById(R.id.iv_show_room_users);
        rootView= (RelativeLayout)findViewById(R.id.layout_chat_room);
        etMessage= findViewById(R.id.etMessage);
        ivSend= findViewById(R.id.ivSendMessage);
        userFrame= findViewById(R.id.room_users_fragment);
        userFrame.setVisibility(View.INVISIBLE);
        ivEmoji= findViewById(R.id.btnEmoji);
        ivMic= findViewById(R.id.iv_chat_room_mic);
        ivVideo= findViewById(R.id.iv_chat_room_video);
        ivSound= findViewById(R.id.iv_chat_room_sound);
        ivMessage= findViewById(R.id.iv_chat_room_message);
        ivOutSide_click= findViewById(R.id.outSide_click);
        toolbar_counter= findViewById(R.id.tv_counter);
        toolbar_title= findViewById(R.id.tv_toolbar_title);
        ivOutSide_click.setVisibility(View.INVISIBLE);
        final Handler handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {

                timerCounter++;
                toolbar_counter.setText(Integer.toString(timerCounter));
                handler.postDelayed(this, 1000);

            }
        }; handler.post(run);


        mRecyclerView = findViewById(R.id.ChatRoom_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMinorChatList = new ArrayList<>();

        position_minor = getIntent().getStringExtra("position_minor_loggedIn");
        position_major = getIntent().getStringExtra("position_major_loggedIn");

        senderName= getIntent().getStringExtra("visitorName");
        MemberOrVisitor=getIntent().getIntExtra("MemberOrVisitor",0);
        visitorsRef= FirebaseDatabase.getInstance().getReference("Visitors");
        chatRef= FirebaseDatabase.getInstance().getReference("Chats").child(String.valueOf(position_major)).child(String.valueOf(position_minor));
        userRef= FirebaseDatabase.getInstance().getReference("ChatRooms");
        logoutRef= FirebaseDatabase.getInstance().getReference("Logout_History");
        if(MemberOrVisitor==LoginActivity.MEMBER){
            getLogoutCount();
            getCurrentMember();
            ApplySettings();
        }else {
            sender = new Member(senderName, "", "", MainActivity.MEMBER, FirebaseAuth.getInstance().getUid(), rgb(0, 0, 0)
                    , 20, true, false, false);
            visitorsRef.child(position_major).child(position_minor).child(FirebaseAuth.getInstance().getUid()).setValue(sender).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    visitorsRef.child(position_major).child(position_minor).child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            sender= dataSnapshot.getValue(Member.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        }
        showMessages();
        sendMessage();

        emojIcon= new EmojIconActions(getApplicationContext(), rootView, etMessage,  ivEmoji);
        emojIcon.ShowEmojIcon();

        getSupportFragmentManager().beginTransaction().replace(R.id.room_users_fragment, new RoomUsersFragment()).commit();
        showMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userFrame.getVisibility()==View.INVISIBLE) {
                    userFrame.setVisibility(View.VISIBLE);
                    ivOutSide_click.setVisibility(View.VISIBLE);
                }
                else {
                    userFrame.setVisibility(View.INVISIBLE);
                    ivOutSide_click.setVisibility(View.INVISIBLE);

                }
            }
        });
        ivOutSide_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userFrame.setVisibility(View.INVISIBLE);
                ivOutSide_click.setVisibility(View.INVISIBLE);
            }
        });

        ivMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ivMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ivSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ivVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    @Override
    public void onItemClick(int position) {

    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        if(MemberOrVisitor==MEMBER)
            getMenuInflater().inflate(R.menu.member_options_menu, menu);
        else if(MemberOrVisitor==VISITOR)
            getMenuInflater().inflate(R.menu.visitor_options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.menu_status:
            case R.id.menu_status_visitor:
                break;
            case R.id.menu_settings: goToSettings();
                break;
            case R.id.menu_about:   aboutProgram();
                break;
            case R.id.menu_delete:  deleteChat();
                break;
            case R.id.menu_manage_room: roomManager();
                break;
            case R.id.menu_change_password: changePassword();
                break;
            case R.id.menu_logout:  logOut();
                break;
            case R.id.menu_settings_visitor:  goToSettings();
                break;
            case R.id.menu_about_visitor:   aboutProgram();
                break;
            case R.id.menu_logout_visitor:  ChatRoomActivity.this.finish();
                break;
            case R.id.menu_status_1: setStatus("متاح");
                break;
            case R.id.menu_status_2: setStatus("مشغول");
                break;
            case R.id.menu_status_3: setStatus("في الخارج");
                break;
            case R.id.menu_status_4: setStatus("هاتف");
                break;
            case R.id.menu_status_5: setStatus("طعام");
                break;
            case R.id.menu_status_6: setStatus("نائم");
                break;

        }



        return true;
    }

    private void setStatus(final String status){
        if(MemberOrVisitor==MainActivity.MEMBER)
        userRef.child(position_major).child("roomMinors").child(String.valueOf(position_minor))
                .child("roomMembers").child(position_user).child("status").setValue(status);
        else visitorsRef.child(position_major).child(position_minor).child(FirebaseAuth.getInstance().getUid())
        .child("status").setValue(status);
    }


    private void goToSettings(){
        Intent intent= new Intent(ChatRoomActivity.this, SettingsActivity.class);
        intent.putExtra("position_major", position_major);
        intent.putExtra("position_minor",  position_minor);
        intent.putExtra("position_user", position_user);
        intent.putExtra("MemberOrVisitor", MemberOrVisitor);
        intent.putExtra("userName", senderName);
        startActivity(intent);
    }

    private void aboutProgram(){


    }

    private void roomManager(){
        Intent intent= new Intent(ChatRoomActivity.this, ManageRoomActivity.class);
        intent.putExtra("position_major", position_major);
        intent.putExtra("position_minor",  position_minor);
        intent.putExtra("position_user", position_user);
        intent.putExtra("userName", senderName);
        startActivity(intent);

    }

    private void deleteChat(){
        chatRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ChatRoomActivity.this, "Chat Deleted Successfully", Toast.LENGTH_SHORT).show();
                prevCount=-1;
            }
        });

    }

    private void changePassword(){
        Intent intent= new Intent(ChatRoomActivity.this, ChangePasswordActivity.class);
        intent.putExtra("userName", senderName);
        startActivity(intent);

    }


    private void showMessages(){

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMinorChatList.clear();
                for (DataSnapshot d:dataSnapshot.getChildren()){
                    Message message= d.getValue(Message.class);
                    mMinorChatList.add(message);
                }

                mAdapter= new MessageAdapter(ChatRoomActivity.this, mMinorChatList);
                mAdapter.setOnClickListener(ChatRoomActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.scrollToPosition(mMinorChatList.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatRoomActivity.this, "connection error...", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void sendMessage(){
        getMessagesCount();
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMessagesCount();
                String message = etMessage.getText().toString();
                if(message.isEmpty()){
                    Toast.makeText(ChatRoomActivity.this, "Enter a message", Toast.LENGTH_SHORT).show();
                }else {
                    if(count>=prevCount){
                        send(message);
                        etMessage.setText("");
                    }else {
                        Toast.makeText(ChatRoomActivity.this, "please wait", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
    private void getMessagesCount(){
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count= (int) dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getCurrentMember(){
        userRef.child(position_major).child("roomMinors").child(String.valueOf(position_minor))
                .child("roomMembers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot p: dataSnapshot.getChildren()) {
                    Member m = p.getValue(Member.class);
                    if (m.getId().equals(FirebaseAuth.getInstance().getUid())) {
                       sender= m;
                       position_user=p.getKey();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void send(final String message){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String time = dateFormat.format(date);

        Message mMessage= new Message(message, sender, sender.getTextColor(), time);
        chatRef.child(String.valueOf(count)).setValue(mMessage);
        prevCount=count+1;

    }

    @Override
    public void onBackPressed() {
        if(userFrame.getVisibility()==View.VISIBLE){
            userFrame.setVisibility(View.INVISIBLE);
        }
        else{
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("تأكيد");
        builder.setMessage("هل تريد تسجيل الخروج؟");

        builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                dialog.dismiss();
                logOut();

            }
        });

        builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    }

    private void logOut(){
        FirebaseAuth.getInstance().signOut();

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String timeOut = dateFormat.format(date);

        int outH= (Integer.parseInt(Character.toString(timeOut.charAt(0)))*10+ Integer.parseInt(Character.toString(timeOut.charAt(1))))*60*60
                , outM= (Integer.parseInt(Character.toString(timeOut.charAt(3)))*10+ Integer.parseInt(Character.toString(timeOut.charAt(4))))*60
                , outS= (Integer.parseInt(Character.toString(timeOut.charAt(6)))*10+ Integer.parseInt(Character.toString(timeOut.charAt(7))));

        int inH= (Integer.parseInt(Character.toString(timeIn.charAt(0)))*10+ Integer.parseInt(Character.toString(timeIn.charAt(1))))*60*60
                , inM= (Integer.parseInt(Character.toString(timeIn.charAt(3)))*10+ Integer.parseInt(Character.toString(timeIn.charAt(4))))*60
                , inS= (Integer.parseInt(Character.toString(timeIn.charAt(6)))*10+ Integer.parseInt(Character.toString(timeIn.charAt(7))));

        String time= String.valueOf((outH+outM+outS)-(inH+inM+inS));
        String country = this.getResources().getConfiguration().locale.getDisplayCountry();
         LogoutHistoryObject logoutHistoryObject = new LogoutHistoryObject(sender.getName(), sender.getId()
                , country, timeIn, timeOut, time);

         logoutRef.child(position_major).child(position_minor).child(logoutCount).setValue(logoutHistoryObject).addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {
                 ChatRoomActivity.this.finish();

             }
         });

    }

    private void getLogoutCount(){
        logoutRef.child(position_major).child(position_minor).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                logoutCount= String.valueOf(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ApplySettings(){
        userRef.child(position_major).child("roomMinors").child(String.valueOf(position_minor))
                .child("roomSettings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RoomSettings roomSettings= dataSnapshot.getValue(RoomSettings.class);
                if(dataSnapshot.child("roomTitle").exists())
                    toolbar_title.setText(roomSettings.getRoomTitle());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
