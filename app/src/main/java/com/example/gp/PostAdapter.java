package com.example.gp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
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
import java.util.Date;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Post> arrayListPost;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference StorageRef = FirebaseStorage.getInstance().getReference();
    private FirebaseAuth Auth = FirebaseAuth.getInstance();

    public PostAdapter(Context mContext, ArrayList<Post> arrayListPost) {
        this.mContext = mContext;
        this.arrayListPost = arrayListPost;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.post_view,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.linear.removeAllViews();

        Post post = arrayListPost.get(position);
        String Text = post.PostTexts;
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String Time = format.format(post.PostTimes);
        String Id = post.docId;
        long Count = post.PostPicCount;

        db.collection("MemberData")
            .document(Auth.getCurrentUser().getEmail())
            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                holder.textViewName.setText(task.getResult().get("name").toString());
                }
            });


        holder.textViewText.setText(Text);
        holder.textViewTime.setText(Time);

        for (int i = 0 ; i < Count ; i++){
            StorageReference ref = StorageRef.child("PostImg").child(Id + "_" + i);
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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int id = holder.getAdapterPosition();
                Post post = arrayListPost.get(id);

                new AlertDialog.Builder(mContext)
                        .setTitle("刪除確認")
                        .setMessage("確認要刪除這筆貼文?")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                for (int j = 0 ; j < Count ; j++){
                                    StorageReference ref = StorageRef.child("PostImg").child(post.docId + "_" + j);

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
                                        .document(post.docId)
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
            textViewName = itemView.findViewById(R.id.PostName);
            textViewTime = itemView.findViewById(R.id.PostTime);
            imageView = itemView.findViewById(R.id.PostImg);
            textViewText = itemView.findViewById(R.id.PostText);
            horizontalScrollView = itemView.findViewById(R.id.PostViewScrollView);
            linear = itemView.findViewById(R.id.PostViewLinear);
        }
    }
}
