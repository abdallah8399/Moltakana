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
import com.wingy.moltakana.Adapters.LogoutHistoryAdapter;
import com.wingy.moltakana.Adapters.MemberAdapter;
import com.wingy.moltakana.Objects.LogoutHistoryObject;
import com.wingy.moltakana.Activities.ManageRoomActivity;
import com.wingy.moltakana.R;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {
    private DatabaseReference historyRef;
    private RecyclerView mRecyclerView;
    private LogoutHistoryAdapter mAdapter;
    private List<LogoutHistoryObject> mLogoutHistoryObjects;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_history, container, false);
        historyRef= FirebaseDatabase.getInstance().getReference("Logout_History");

        mRecyclerView = view.findViewById(R.id.history_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mLogoutHistoryObjects= new ArrayList<>();

        historyRef.child(ManageRoomActivity.position_major).child(ManageRoomActivity.position_minor)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            LogoutHistoryObject logoutHistoryObject= d.getValue(LogoutHistoryObject.class);
                            mLogoutHistoryObjects.add(logoutHistoryObject);
                        }
                        mAdapter= new LogoutHistoryAdapter(getContext(), mLogoutHistoryObjects);
                        mRecyclerView.setAdapter(mAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        return view;
    }

}
