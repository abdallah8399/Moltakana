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
import com.wingy.moltakana.Activities.MainActivity;
import com.wingy.moltakana.Objects.Member;
import com.wingy.moltakana.R;

import java.util.List;

import static android.graphics.Color.rgb;


public class MemberAdapter extends RecyclerView.Adapter <MemberAdapter.MemberViewHolder>{
private Context mContext;
private List<Member> mMembers;
private OnItemClickListener mListener;


public MemberAdapter(Context context, List<Member> members){

        mContext= context;
        mMembers = members;
        }

@NonNull
@Override
public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.member_item_layout, parent, false);
        return new MemberViewHolder(v);
        }

@Override
public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {

        if(mMembers.get(position).getMemberType() == MainActivity.ADMIN)
            holder.tvMemberName.setTextColor(rgb(0,0,255));
        else if(mMembers.get(position).getMemberType() == MainActivity.MASTER)
            holder.tvMemberName.setTextColor(rgb(255,0,0));
        else if(mMembers.get(position).getMemberType() == MainActivity.MASTER_GIRL)
            holder.tvMemberName.setTextColor(rgb(255,0,255));
        else if(mMembers.get(position).getMemberType() == MainActivity.MEMBER)
            holder.tvMemberName.setTextColor(rgb(128,0,128));
        else if(mMembers.get(position).getMemberType() == MainActivity.SUPER_ADMIN)
            holder.tvMemberName.setTextColor(rgb(0,255,0));

        holder.tvMemberName.setText(mMembers.get(position).getName());

        if(! mMembers.get(position).getImage().isEmpty())
        Picasso.get().load(mMembers.get(position).getImage()).placeholder(R.drawable.ic_people_black_24dp).fit().centerCrop().into(holder.memberImage);

      /*  String status="";
        if(! mMembers.get(position).getStatus().isEmpty())
        Picasso.get().load(status).placeholder(R.drawable.ic_do_not_disturb_black_24dp).fit().centerCrop().into(holder.memberImage);
*/
}

@Override
public int getItemCount() {
        return mMembers.size();
        }


public class MemberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView tvMemberName;
    public ImageView memberImage, statusImage;

    public MemberViewHolder(View itemView){
        super(itemView);
        tvMemberName= itemView.findViewById(R.id.member_item_name);
        memberImage= itemView.findViewById(R.id.member_item_image);
        statusImage= itemView.findViewById(R.id.member_item_status);

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

    public void setOnClickListener(MemberAdapter.OnItemClickListener listener){
        mListener= listener;
    }
}
