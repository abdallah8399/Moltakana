package com.wingy.moltakana.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wingy.moltakana.Activities.ChatRoomActivity;
import com.wingy.moltakana.Activities.ManageRoomActivity;
import com.wingy.moltakana.Adapters.MemberAdapter;
import com.wingy.moltakana.Objects.Member;
import com.wingy.moltakana.R;

import java.util.ArrayList;
import java.util.List;

public class RoomUsersFragment extends Fragment implements  MemberAdapter.OnItemClickListener{

    private List<Member> mMemberList;
    private DatabaseReference memberRef;
    private RecyclerView mRecyclerView;
    private MemberAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_room_users, container, false);
        mMemberList= new ArrayList<>();

        memberRef= FirebaseDatabase.getInstance().getReference("ChatRooms").child(ChatRoomActivity.position_major).child("roomMinors").child(ChatRoomActivity.position_minor).child("roomMembers");
        mRecyclerView = view.findViewById(R.id.room_users_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        memberRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMemberList.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Member member= dataSnapshot1.getValue(Member.class);
                    mMemberList.add(member);
                }
                mAdapter= new MemberAdapter(getContext(), mMemberList);
                mAdapter.setOnClickListener(RoomUsersFragment.this);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onItemClick(int position) {

    }
}
