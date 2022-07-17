package com.wingy.moltakana.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wingy.moltakana.Objects.LogoutHistoryObject;
import com.wingy.moltakana.R;
import java.util.List;

public class LogoutHistoryAdapter extends RecyclerView.Adapter <LogoutHistoryAdapter.LogoutHistoryViewHolder>{
    private Context mContext;
    private List<LogoutHistoryObject> mLogoutHistoryObjects;

    public LogoutHistoryAdapter(Context context, List<LogoutHistoryObject> logoutHistoryObjects){

        mContext= context;
        mLogoutHistoryObjects = logoutHistoryObjects;
    }

    @NonNull
    @Override
    public LogoutHistoryAdapter.LogoutHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_logout_history_layout, parent, false);
        return new LogoutHistoryAdapter.LogoutHistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LogoutHistoryAdapter.LogoutHistoryViewHolder holder, int position) {
        holder.tvName.setText(mLogoutHistoryObjects.get(position).getName());
        holder.tvIn.setText(mLogoutHistoryObjects.get(position).getIn());
        holder.tvOut.setText(mLogoutHistoryObjects.get(position).getOut());
        holder.tvID.setText(mLogoutHistoryObjects.get(position).getId());
        holder.tvLocation.setText(mLogoutHistoryObjects.get(position).getLocation());
        holder.tvTime.setText(mLogoutHistoryObjects.get(position).getTime()+" sec");

    }

    @Override
    public int getItemCount() {
        return mLogoutHistoryObjects.size();
    }

    public class LogoutHistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvIn, tvOut, tvTime, tvID, tvLocation;

        public LogoutHistoryViewHolder(View itemView){
            super(itemView);
            tvName= itemView.findViewById(R.id.item_logoutHistory_name);
            tvIn= itemView.findViewById(R.id.item_logoutHistory_in);
            tvOut= itemView.findViewById(R.id.item_logoutHistory_out);
            tvTime= itemView.findViewById(R.id.item_logoutHistory_time);
            tvID= itemView.findViewById(R.id.item_logoutHistory_id);
            tvLocation= itemView.findViewById(R.id.item_logoutHistory_location);

        }
    }


}
