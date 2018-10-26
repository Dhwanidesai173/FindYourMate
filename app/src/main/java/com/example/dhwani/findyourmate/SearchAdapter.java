package com.example.dhwani.findyourmate;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

class SearchViewHolder extends RecyclerView.ViewHolder{

    public TextView nameSearch,emailSearch;
    public CircleImageView profileSearch;

    public SearchViewHolder(View itemView) {
        super(itemView);
        nameSearch = (TextView) itemView.findViewById(R.id.mainName);
        emailSearch = (TextView) itemView.findViewById(R.id.mainEmail);
        profileSearch = (CircleImageView) itemView.findViewById(R.id.mainIcon);

    }
}

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder>{

    private Context context;
    private List<User> modellist;

    public SearchAdapter(Context context,List<User> users){
        this.context = context;
        this.modellist = users;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new SearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.nameSearch.setText(modellist.get(position).getUser_name());
        holder.emailSearch.setText(modellist.get(position).getEmail());
        Bitmap bitmap = Constants.decodeBase64(modellist.get(position).getProfileImage());
        holder.profileSearch.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return modellist.size();
    }
}
