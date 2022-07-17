package com.wingy.moltakana.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wingy.moltakana.Objects.MinorChatRoom;
import com.wingy.moltakana.Adapters.MinorChatRoomAdapter;
import com.wingy.moltakana.R;

import java.util.ArrayList;
import java.util.List;

public class MinorChatRoomActivity extends AppCompatActivity implements MinorChatRoomAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private MinorChatRoomAdapter mAdapter;
    private List<MinorChatRoom> mMinorChatRoomsList;
    private DatabaseReference chatRoomsRef;
    private FloatingActionButton fabAddMinor;
    private String position1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minor_chat_room);


        fabAddMinor= findViewById(R.id.fabNewMinorChatRoom);
        mRecyclerView = findViewById(R.id.minorChatRoom_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMinorChatRoomsList = new ArrayList<>();

        position1 = getIntent().getStringExtra("position");
        chatRoomsRef= FirebaseDatabase.getInstance().getReference("ChatRooms");

        fabAddMinor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MinorChatRoomActivity.this, AddMinorChatRoomActivity.class);
                intent.putExtra("position_major", position1);
                startActivity(intent);
            }
        });

        chatRoomsRef.child(position1).child("roomMinors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMinorChatRoomsList.clear();
                    for(DataSnapshot p:dataSnapshot.getChildren()){
                        MinorChatRoom minorChatRoom= p.getValue(MinorChatRoom.class);
                        mMinorChatRoomsList.add(minorChatRoom);
                    }

                mAdapter= new MinorChatRoomAdapter(MinorChatRoomActivity.this, mMinorChatRoomsList);
                mAdapter.setOnClickListener(MinorChatRoomActivity.this);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MinorChatRoomActivity.this, "connection error...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onItemClick(int position) {
        Intent intent= new Intent(MinorChatRoomActivity.this, LoginActivity.class);
        intent.putExtra("position_minor", String.valueOf(position));
        intent.putExtra("position_major", position1);

        startActivity(intent);

    }

}
