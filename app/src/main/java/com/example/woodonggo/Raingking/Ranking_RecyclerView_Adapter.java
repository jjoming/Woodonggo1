package com.example.woodonggo.Raingking;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.woodonggo.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Ranking_RecyclerView_Adapter extends RecyclerView.Adapter<Ranking_RecyclerView_Adapter.MyViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<DataModelRank> dataModels;
    private Context context;

    public Ranking_RecyclerView_Adapter(FragmentActivity context, ArrayList<DataModelRank> dataModels) {
        this.dataModels = dataModels;
        this.context = context;
    }

    public void setDataModels(ArrayList<DataModelRank> dataModels) {
        if (dataModels == null) {
            this.dataModels = new ArrayList<>();
        } else {
            this.dataModels = dataModels;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_recyclerview_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");

        MyViewHolder myViewHolder = (MyViewHolder) holder;

        DataModelRank dataModel = dataModels.get(position);

        myViewHolder.rank.setText(String.valueOf(dataModel.getRanking()));
        myViewHolder.rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 랭킹을 눌렀을 때 나오는 화면
            }
        });
        String rank = getUserIdBasedOnRank(dataModel.getRanking());
        String userId = dataModel.getUserId();
        fetchUserProfileImageForRank(rank, myViewHolder.imgView_rank, userId);
        myViewHolder.nick_rank.setText(dataModel.getNick());
    }

    @Override
    public int getItemCount() {
        return dataModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rank;
        ImageView imgView_rank;
        TextView nick_rank;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.rank);
            imgView_rank = itemView.findViewById(R.id.imgView_rank);
            nick_rank = itemView.findViewById(R.id.nick_rank);
        }
    }

    private String getUserIdBasedOnRank(int rank) {
        return String.valueOf(rank);
    }

    private void fetchUserProfileImageForRank(String rank, ImageView imageView, String userId) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference profileImageRef = storageReference.child("user_profiles/" + userId + ".jpg");

        profileImageRef.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    Glide.with(context)
                            .load(uri)
                            .into(imageView);
                })
                .addOnFailureListener(exception -> {
                    Glide.with(context)
                            .load(R.drawable.noprofile)
                            .into(imageView);
                });
    }
}