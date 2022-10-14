package com.example.gp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    Context mContext;
    ArrayList<String> arrayListPost;

    public PostAdapter(Context mContext, ArrayList<String> arrayListPost) {
        this.mContext = mContext;
        this.arrayListPost = arrayListPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View itemView = layoutInflater.inflate(R.layout.post_view,parent,false);

        ViewHolder holder =  new ViewHolder(itemView);
        holder.textViewName = itemView.findViewById(R.id.PostName);
        holder.textViewTime = itemView.findViewById(R.id.PostTime);
        holder.imageView = itemView.findViewById(R.id.PostImg);
        holder.textViewText = itemView.findViewById(R.id.PostText);
        holder.horizontalScrollView = itemView.findViewById(R.id.PostViewScrollView);
        holder.linear = itemView.findViewById(R.id.PostViewLinear);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        StorageReference StorageRef = FirebaseStorage.getInstance().getReference();

        db.collection("Post")
                .whereEqualTo("postId",arrayListPost.get(position))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot doc: task.getResult())
                            {
                                String Name = doc.getString("Name");
                                String Text = doc.getString("Text");
                                String Time = doc.getString("Time");

                                holder.textViewName.setText(Name);
                                holder.textViewText.setText(Text);
                                holder.textViewTime.setText(Time);

                            }
                        }
                    }
                });

//        StorageRef.child("PostImg").child("Postimage:59")
//                .getDownloadUrl()
//                .addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        Glide.with(mContext)
//                                .load(uri)
//                                .into(holder.imageView);
//                    }
//                });

        StorageRef.child("PostImg").listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()){
                            item.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.d("Demo1",uri.toString());

                                            ImageView imageView = new ImageView(mContext);
                                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(500, 500);
                                            imageView.setLayoutParams(params);

                                            Glide.with(mContext)
                                                    .load(uri)
                                                    .into(imageView);

                                            holder.linear.addView(imageView);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("Demo12",e.getMessage());
                                        }
                                    });
                        }
                    }
                });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int id = holder.getAdapterPosition();
                new AlertDialog.Builder(mContext)
                        .setTitle("刪除確認")
                        .setMessage("確認要刪除這筆紀錄?")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.collection("Post").document(arrayListPost.get(id)).delete();
                                arrayListPost.remove(id); //從products中移除該項目
                                notifyItemRemoved(id); //通知移除item
                                notifyItemRangeChanged(0,arrayListPost.size()); //刷新項目
                            }
                        })
                        .setNegativeButton("取消返回",null).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListPost.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewName,textViewTime,textViewText;
        ImageView imageView;
        HorizontalScrollView horizontalScrollView;
        LinearLayout linear;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
