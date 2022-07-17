package com.wingy.moltakana.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.wingy.moltakana.Activities.ManageRoomActivity;
import com.wingy.moltakana.Objects.Member;
import com.wingy.moltakana.Adapters.MemberAdapter;
import com.wingy.moltakana.R;

import java.util.ArrayList;
import java.util.List;


public class BlockAccountFragment extends Fragment implements  MemberAdapter.OnItemClickListener{

    private RecyclerView mRecyclerView;
    private MemberAdapter mAdapter;
    private List<Member> mMemberList, filteredMembersList;
    private DatabaseReference memberRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_blocked, container, false);

        mRecyclerView = view.findViewById(R.id.blocked_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMemberList = new ArrayList<>();
        filteredMembersList = new ArrayList<>();
        memberRef= FirebaseDatabase.getInstance().getReference("ChatRooms").child(ManageRoomActivity.position_major).child("roomMinors").child(ManageRoomActivity.position_minor).child("roomMembers");
        setRecyclerView();


        return view;
    }

    @Override
    public void onItemClick(int position) {

    }

    private void setRecyclerView(){
        memberRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    Member member= d.getValue(Member.class);
                    if(member.getBlocked())
                    mMemberList.add(member);
                }
                mAdapter= new MemberAdapter(getContext(), filteredMembersList);
                mAdapter.setOnClickListener(BlockAccountFragment.this);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
