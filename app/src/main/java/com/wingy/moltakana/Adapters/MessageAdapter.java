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
import com.wingy.moltakana.Objects.Member;
import com.wingy.moltakana.Objects.Message;
import com.wingy.moltakana.R;

import java.util.List;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;


public class MessageAdapter extends RecyclerView.Adapter <MessageAdapter.MessageViewHolder>{
private Context mContext;
private List<Message> mMessages;
private OnItemClickListener mListener;

public MessageAdapter(Context context, List<Message> messages){

        mContext= context;
        mMessages = messages;
        }

@NonNull
@Override
public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.message_item_layout, parent, false);
        return new MessageViewHolder(v);
        }

@Override
public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.tvSenderName.setText(mMessages.get(position).getSender().getName());
        holder.tvSenderName.setTextColor(mMessages.get(position).getColor());
        holder.tvMessage.setText(mMessages.get(position).getMessage());
        holder.tvMessage.setTextColor(mMessages.get(position).getColor());
        holder.sideMessageColor.setBackgroundColor(mMessages.get(position).getColor());
        holder.timeView.setText(mMessages.get(position).getTime());
        Member member= mMessages.get(position).getSender();
        holder.tvMessage.setTextSize(member.getTextSize());
        holder.tvMessage.setEmojiconSize(3*member.getTextSize());

        holder.sideMessageColor.setHeight(holder.tvMessage.getHeight());
        if(! mMessages.get(position).getSender().getImage().isEmpty())
            Picasso.get().load(mMessages.get(position).getSender().getImage()).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.imageView);

        }

@Override
public int getItemCount() {
        return mMessages.size();
        }


public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView tvSenderName, sideMessageColor, timeView;
    public EmojiconTextView tvMessage;
    public ImageView imageView;

    public MessageViewHolder(View itemView){
        super(itemView);
        imageView= itemView.findViewById(R.id.item_sender_image);
        tvMessage= itemView.findViewById(R.id.item_tv_message);
        tvSenderName= itemView.findViewById(R.id.item_tv_sender);
        sideMessageColor= itemView.findViewById(R.id.sideMessageColor);
        timeView= itemView.findViewById(R.id.item_tv_time);

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

    public void setOnClickListener(MessageAdapter.OnItemClickListener listener){
        mListener= listener;
    }
}
