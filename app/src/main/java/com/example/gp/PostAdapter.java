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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    Context mContext;
    ArrayList<String> arrayListPost;
    private FirebaseAuth Auth = FirebaseAuth.getInstance();

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

        holder.linear.removeAllViews();

        db.collection("Posts")
                .whereEqualTo("PostAuthor", Auth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            db.collection("MemberData")
                                    .document(Auth.getCurrentUser().getEmail())
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    holder.textViewName.setText(task.getResult().get("name").toString());
                                }
                            });

                            String Text = task.getResult().getDocuments().get(position).getString("PostTexts");
                            String Time = task.getResult().getDocuments().get(position).getString("PostTimes");

                            Log.d("AAAAA",task.getResult().getDocuments().get(position).get("PostPicCount").toString());
                            holder.textViewText.setText(Text);
                            holder.textViewTime.setText(Time);

                            for (int i = 0 ; i < Integer.parseInt(task.getResult().getDocuments().get(position).get("PostPicCount").toString()) ; i++){
                                StorageReference ref = StorageRef.child("PostImg").child(task.getResult().getDocuments().get(position).getId() + "_" + i);
                                File file;
                                try {
                                    file = File.createTempFile("images","png");
                                    ref.getFile(file)
                                            .addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                            ImageView imageView = new ImageView(mContext);
                                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(500, 500);
                                            imageView.setLayoutParams(params);

                                            Glide.with(mContext)
                                                    .load(Uri.fromFile(file))
                                                    .into(imageView);

                                            holder.linear.addView(imageView);
                                        }
                                    });
                                }catch (Exception e){
                                    Log.d("AAAAA",e.getMessage());
                                }
                            }
                        }
                    }
                });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int id = holder.getAdapterPosition();

                new AlertDialog.Builder(mContext)
                        .setTitle("刪除確認")
                        .setMessage("確認要刪除這筆貼文?")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.collection("Posts")
                                        .document(arrayListPost.get(id))
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                for (int i = 0 ; i < Integer.parseInt(task.getResult().get("PostPicCount").toString()) ; i++){
                                                    StorageReference ref = StorageRef.child("PostImg").child(arrayListPost.get(id) + "_" + i);

                                                    ref.delete()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    Log.d("AAAAA","刪除照片成功");
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.d("AAAAA","刪除照片失敗");
                                                                }
                                                            });
                                                }

                                                db.collection("Posts")
                                                        .document(arrayListPost.get(id))
                                                        .delete()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                arrayListPost.remove(id); //從products中移除該項目
                                                                notifyItemRemoved(id); //通知移除item
                                                                notifyItemRangeChanged(0,arrayListPost.size()); //刷新項目
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d("AAAAA","刪除貼文失敗");
                                                            }
                                                        });
                                            }
                                        });
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
