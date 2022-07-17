package com.wingy.moltakana.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wingy.moltakana.Objects.MajorChatRoom;
import com.wingy.moltakana.Objects.MinorChatRoom;
import com.wingy.moltakana.R;

import java.util.ArrayList;
import java.util.List;


public class ChatRoomAdapter extends RecyclerView.Adapter <ChatRoomAdapter.ChatRoomViewHolder>{
    private Context mContext;
    private List<MajorChatRoom> mMajorChatRooms;
    private OnItemClickListener mListener;

    public ChatRoomAdapter(Context context, List<MajorChatRoom> majorChatRooms){

        mContext= context;
        mMajorChatRooms = majorChatRooms;
    }

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.chat_room_item_layout, parent, false);
        return new ChatRoomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position) {
            holder.tvName.setText(mMajorChatRooms.get(position).getName());
            ArrayList<MinorChatRoom> minorChatRoom= mMajorChatRooms.get(position).getRoomMinors();
            holder.tvRoomCount.setText(String.valueOf(minorChatRoom.size()));

        int count=0;
        for(int i=0; i<mMajorChatRooms.get(position).getRoomMinors().size(); i++){
            count+= mMajorChatRooms.get(position).getRoomMinors().get(i).getRoomMembers().size();
        }

        holder.tvUserCount.setText(String.valueOf(count));
        if(! mMajorChatRooms.get(position).getImage().isEmpty())
            Picasso.get().load(mMajorChatRooms.get(position).getImage()).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.imageView);
        else
            Picasso.get().load(R.drawable.ic_account_circle_black_24dp).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mMajorChatRooms.size();
    }

    public class ChatRoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvName, tvUserCount, tvRoomCount;
        public ImageView imageView;

        public ChatRoomViewHolder(View itemView){
            super(itemView);
            tvName= itemView.findViewById(R.id.item_chatroom_name);
            imageView= itemView.findViewById(R.id.item_chatroom_image);
            tvRoomCount= itemView.findViewById(R.id.tvRoomCount);
            tvUserCount= itemView.findViewById(R.id.tvUserCount);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mListener!=null){
                int position= getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnClickListener(OnItemClickListener listener){
        mListener= listener;
    }
}
