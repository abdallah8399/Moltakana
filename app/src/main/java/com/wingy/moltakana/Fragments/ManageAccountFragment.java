package com.wingy.moltakana.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.wingy.moltakana.Activities.AddMemberActivity;
import com.wingy.moltakana.Activities.MainActivity;
import com.wingy.moltakana.Activities.ManageRoomActivity;
import com.wingy.moltakana.Objects.Member;
import com.wingy.moltakana.Adapters.MemberAdapter;
import com.wingy.moltakana.R;

import java.util.ArrayList;
import java.util.List;

public class ManageAccountFragment extends Fragment implements  MemberAdapter.OnItemClickListener{

    private RecyclerView mRecyclerView;
    private MemberAdapter mAdapter;
    private List<Member> mMemberList, filteredMembersList;
    private DatabaseReference memberRef;
    private CheckBox admin, member, master, sAdmin;
    private Boolean adminB=true, memberB=true, masterB=true, sAdminB= true;
    private ImageView addAccount;
    private TextView tvTotalUsers;
    private int userCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_manage_accounts, container, false);

        admin= view.findViewById(R.id.cbAdmin);
        master= view.findViewById(R.id.cbMaster);
        sAdmin= view.findViewById(R.id.cbSAdmin);
        member= view.findViewById(R.id.cbMember);
        addAccount= view.findViewById(R.id.ivAddMember);
        tvTotalUsers= view.findViewById(R.id.tvTotal);

        mRecyclerView = view.findViewById(R.id.manage_account_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMemberList = new ArrayList<>();
        filteredMembersList = new ArrayList<>();
        memberRef= FirebaseDatabase.getInstance().getReference("ChatRooms").child(ManageRoomActivity.position_major).child("roomMinors").child(ManageRoomActivity.position_minor).child("roomMembers");
        setRecyclerView();

        addAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getContext(), AddMemberActivity.class);
                startActivity(intent);
            }
        });

        admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true){
                    adminB=true;
                    setView();

                }else{
                    adminB=false;
                    setView();
                }
            }
        });

        member.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true){
                    memberB=true;
                    setView();
                }else{
                    memberB=false;
                    setView();
                }
            }
        });

        sAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true){
                    sAdminB=true;
                    setView();
                }else{
                    sAdminB=false;
                    setView();
                }
            }
        });

        master.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true){
                    masterB=true;
                    setView();
                }else{
                    masterB=false;
                    setView();
                }
            }
        });



        return view;
    }

    @Override
    public void onItemClick(int position) {

    }

    private void setRecyclerView(){
        memberRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userCount= (int) dataSnapshot.getChildrenCount();
                mMemberList.clear();
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    Member member= d.getValue(Member.class);
                    mMemberList.add(member);
                }
                tvTotalUsers.setText("Total: " + String.valueOf(userCount));
                setView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setView(){
        filteredMembersList.clear();

        if(memberB){
            for(int i=0; i<mMemberList.size(); i++){
                Member mMember= mMemberList.get(i);
                if(mMember.getMemberType()== MainActivity.MEMBER)
                    filteredMembersList.add(mMember);

            }
        }

        if(adminB){
            for(int i=0; i<mMemberList.size(); i++){
                Member mMember= mMemberList.get(i);
                if(mMember.getMemberType()== MainActivity.ADMIN)
                    filteredMembersList.add(mMember);

            }
        }

        if(masterB){
            for(int i=0; i<mMemberList.size(); i++){
                Member mMember= mMemberList.get(i);
                if(mMember.getMemberType()== MainActivity.MASTER || mMember.getMemberType()== MainActivity.MASTER_GIRL)
                    filteredMembersList.add(mMember);

            }
        }

        if(sAdminB){
            for(int i=0; i<mMemberList.size(); i++){
                Member mMember= mMemberList.get(i);
                if(mMember.getMemberType()== MainActivity.SUPER_ADMIN)
                    filteredMembersList.add(mMember);

            }
        }

        mAdapter= new MemberAdapter(getContext(), filteredMembersList);
        mAdapter.setOnClickListener(ManageAccountFragment.this);
        mRecyclerView.setAdapter(mAdapter);
    }
}
