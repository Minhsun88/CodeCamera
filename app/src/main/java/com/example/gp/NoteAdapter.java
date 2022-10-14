package com.example.gp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    Context mContext;
    ArrayList<String> arrayList;

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

        db.collection("Notes")
                .whereEqualTo("Title",arrayList.get(position))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot doc: task.getResult())
                            {
                                String Title = doc.getString("Title");
                                String Text = doc.getString("Text");
                                String Time = doc.getString("Time");

                                holder.textViewTitle.setText(Title);
                                holder.textViewText.setText(Text);
                                holder.textViewTime.setText(Time);

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
                                db.collection("Notes").document(arrayList.get(id)).delete();
                                arrayList.remove(id); //從products中移除該項目
                                notifyItemRemoved(id); //通知移除item
                                notifyItemRangeChanged(0,arrayList.size()); //刷新項目
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
