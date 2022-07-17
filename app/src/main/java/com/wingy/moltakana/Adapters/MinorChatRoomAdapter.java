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
import com.wingy.moltakana.Objects.MinorChatRoom;
import com.wingy.moltakana.R;

import java.util.List;

import static android.graphics.Color.rgb;


public class MinorChatRoomAdapter extends RecyclerView.Adapter <MinorChatRoomAdapter.MinorChatRoomViewHolder>{
    private Context mContext;
    private List<MinorChatRoom> mMinorChatRooms;
    private MinorChatRoomAdapter.OnItemClickListener mListener;

    public MinorChatRoomAdapter(Context context, List<MinorChatRoom> minorChatRooms){

        mContext= context;
        mMinorChatRooms = minorChatRooms;
    }

    @NonNull
    @Override
    public MinorChatRoomAdapter.MinorChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.minor_chat_item_layout, parent, false);
        return new MinorChatRoomAdapter.MinorChatRoomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MinorChatRoomAdapter.MinorChatRoomViewHolder holder, int position) {
        holder.tvName.setText(mMinorChatRooms.get(position).getName());
        holder.tvUserCount.setText(String.valueOf(mMinorChatRooms.get(position).getRoomMembers().size()));

        if(mMinorChatRooms.get(position).getVIP())
            holder.itemView.setBackgroundColor(rgb(255,170,183));
        else
            holder.itemView.setBackgroundColor(rgb(255,255,255));

        if(! mMinorChatRooms.get(position).getImage().isEmpty())
            Picasso.get().load(mMinorChatRooms.get(position).getImage()).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mMinorChatRooms.size();
    }


    public class MinorChatRoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvName, tvUserCount;
        public ImageView imageView;

        public MinorChatRoomViewHolder(View itemView){
            super(itemView);
            tvName= itemView.findViewById(R.id.item_chatroom_name_minor);
            imageView= itemView.findViewById(R.id.item_chatroom_image_minor);
            tvUserCount= itemView.findViewById(R.id.tvUserCount_minor);
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

    public void setOnClickListener(MinorChatRoomAdapter.OnItemClickListener listener){
        mListener= listener;
    }
}
