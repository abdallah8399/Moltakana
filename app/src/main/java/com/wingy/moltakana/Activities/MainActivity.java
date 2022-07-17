package com.wingy.moltakana.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wingy.moltakana.Adapters.ChatRoomAdapter;
import com.wingy.moltakana.Objects.MajorChatRoom;
import com.wingy.moltakana.Objects.MinorChatRoom;
import com.wingy.moltakana.R;

import java.util.ArrayList;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ChatRoomAdapter.OnItemClickListener {

    public static final int MEMBER=0, ADMIN=1, SUPER_ADMIN=2, MASTER=3, MASTER_GIRL=4;

    private RecyclerView mRecyclerView;
    private ChatRoomAdapter mAdapter;
    private List<MajorChatRoom> mMajorChatRoomsList;
    private DatabaseReference chatRoomsRef;
    private FloatingActionButton fabAddRooms;
    private TextView mainInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabAddRooms= findViewById(R.id.fabNewChatRoom);
        fabAddRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, AddChatRoomActivity.class);
                startActivity(intent);
            }
        });

        mainInfo= findViewById(R.id.tvMainInfo);

        mRecyclerView = findViewById(R.id.majorChatRoom_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMajorChatRoomsList = new ArrayList<>();

        chatRoomsRef = FirebaseDatabase.getInstance().getReference("ChatRooms");

        chatRoomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMajorChatRoomsList.clear();
                for (DataSnapshot p : dataSnapshot.getChildren()) {
                    MajorChatRoom majorChatRoom= p.getValue(MajorChatRoom.class);
                    mMajorChatRoomsList.add(majorChatRoom);
                }

                mAdapter= new ChatRoomAdapter(MainActivity.this, mMajorChatRoomsList);
                mAdapter.setOnClickListener(MainActivity.this);
                mRecyclerView.setAdapter(mAdapter);

                int countRooms=0, countMembers=0;
                for(int i=1; i<mMajorChatRoomsList.size(); i++){
                        countRooms+=mMajorChatRoomsList.get(i).getRoomMinors().size();
                    for(int j=0; j<mMajorChatRoomsList.get(i).getRoomMinors().size(); j++) {
                        MinorChatRoom m = mMajorChatRoomsList.get(i).getRoomMinors().get(j);
                        countMembers+= m.getRoomMembers().size();
                    }
                }

                mainInfo.setText( "   غرفة: "+ String.valueOf(countRooms)+"       مستخدم: "+String.valueOf(countMembers));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "connection error...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onItemClick(int position) {
        Intent intent= new Intent(MainActivity.this, MinorChatRoomActivity.class);
        intent.putExtra("position", String.valueOf(position));
        startActivity(intent);

    }

}
