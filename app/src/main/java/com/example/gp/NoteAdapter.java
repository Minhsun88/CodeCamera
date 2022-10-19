package com.example.gp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    Context mContext;
    ArrayList<String> arrayList;

    private ArrayList TitleList = new ArrayList();
    private ArrayList TextList = new ArrayList();
    private ArrayList TimeList = new ArrayList();

    public NoteAdapter(Context mContext, ArrayList<String> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View itemView = layoutInflater.inflate(R.layout.item_view,parent,false);

        ViewHolder holder =  new ViewHolder(itemView);
        holder.textViewTitle = itemView.findViewById(R.id.MyTitle);
        holder.textViewText = itemView.findViewById(R.id.MyText);
        holder.textViewTime = itemView.findViewById(R.id.Time);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseAuth Auth = FirebaseAuth.getInstance();

        db.collection("MemberData")
                .document(Auth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                if(document.get("Titles") != null && document.get("Texts") != null && document.get("Times") != null) {
                                    TitleList = (ArrayList) document.get("Titles");
                                    TextList = (ArrayList) document.get("Texts");
                                    TimeList = (ArrayList) document.get("Times");

                                    holder.textViewTitle.setText(TitleList.get(position).toString());
                                    holder.textViewText.setText(TextList.get(position).toString());
                                    holder.textViewTime.setText(TimeList.get(position).toString());
                                }
                            } else {
                                Log.d("AAAAA",task.getException().toString());
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
                        .setMessage("確認要刪除這筆紀錄?")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.collection("MemberData")
                                        .document(Auth.getCurrentUser().getEmail())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if(document.exists()){
                                                        TitleList = (ArrayList) document.get("Titles");
                                                        TextList = (ArrayList) document.get("Texts");
                                                        TimeList = (ArrayList) document.get("Times");

                                                        TitleList.remove(id);
                                                        TimeList.remove(id);
                                                        TextList.remove(id);

                                                        HashMap<String,Object> Notes = new HashMap<>();
                                                        Notes.put("Titles",TitleList);
                                                        Notes.put("Texts",TextList);
                                                        Notes.put("Times",TimeList);

                                                        db.collection("MemberData")
                                                                .document(Auth.getCurrentUser().getEmail())
                                                                .update(Notes);

                                                        arrayList.remove(id); //從products中移除該項目
                                                        notifyItemRemoved(id); //通知移除item
                                                        notifyItemRangeChanged(0,arrayList.size()); //刷新項目
                                                    }
                                                }
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
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewTime,textViewText,textViewTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
