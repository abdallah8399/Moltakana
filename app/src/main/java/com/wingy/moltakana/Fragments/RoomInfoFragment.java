package com.wingy.moltakana.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wingy.moltakana.Activities.ManageRoomActivity;
import com.wingy.moltakana.R;

public class RoomInfoFragment extends Fragment {
private TextView tvRoomName, tvOwner, tvCapacity,tvExpire, tvS, tvA, tvSA, tvM;
private DatabaseReference userRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.fragment_room_info, container, false);
        tvRoomName= view.findViewById(R.id.textView3);
        tvOwner= view.findViewById(R.id.textView5);
        tvCapacity= view.findViewById(R.id.textView7);
        tvExpire= view.findViewById(R.id.textView9);
        tvS= view.findViewById(R.id.textView11);
        tvA= view.findViewById(R.id.textView12);
        tvSA= view.findViewById(R.id.textView13);
        tvM= view.findViewById(R.id.textView14);
        userRef= FirebaseDatabase.getInstance().getReference("ChatRooms");

        setRoomName(tvRoomName);
        setCapacity(tvCapacity);
        setExpire(tvExpire);
       // setOwner(tvOwner);
        setUserType(tvS, tvA, tvSA, tvM);



        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setRoomName(final TextView textView){
        userRef.child(ManageRoomActivity.position_major).child("roomMinors").child(ManageRoomActivity.position_minor).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               String roomName= dataSnapshot.getValue().toString();
               textView.setText(roomName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setOwner(final TextView textView){
        userRef.child(ManageRoomActivity.position_major).child("roomMinors").child(ManageRoomActivity.position_minor).child("owner").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String owner = dataSnapshot.getValue().toString();
                textView.setText(owner);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setCapacity(final TextView textView){
        userRef.child(ManageRoomActivity.position_major).child("roomMinors").child(ManageRoomActivity.position_minor).child("roomMembers").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        textView.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void setExpire(TextView textView){


    }

    private void setUserType(final TextView textView1, final TextView textView2, final TextView textView3, final TextView textView4){
        userRef.child(ManageRoomActivity.position_major).child("roomMinors").child(ManageRoomActivity.position_minor).child("roomMembers").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String type = String.valueOf(dataSnapshot.getChildrenCount());
                    textView1.append(type);
                    textView2.append(type);
                    textView3.append(type);
                    textView4.append(type);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }
}
