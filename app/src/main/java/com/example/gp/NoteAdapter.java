package com.example.gp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Note> arrayListNote;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth Auth = FirebaseAuth.getInstance();

    public NoteAdapter(Context mContext, ArrayList<Note> arrayListNote) {
        this.mContext = mContext;
        this.arrayListNote = arrayListNote;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_view,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = arrayListNote.get(position);
        String Title = note.NoteTitles;
        String Text = note.NoteTexts;
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String Time = format.format(note.NoteTimes);
        String Id = note.docId;

        holder.textViewTitle.setText(Title);
        holder.textViewText.setText(Text);
        holder.textViewTime.setText(Time);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                UpdateNoteFragment updateNoteFragment = new UpdateNoteFragment();
                bundle.putString("id",Id);
                bundle.putString("title",Title);
                bundle.putString("text",Text);

                updateNoteFragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout,updateNoteFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int id = holder.getAdapterPosition();
                Note note = arrayListNote.get(id);

                new AlertDialog.Builder(mContext)
                        .setTitle("刪除確認")
                        .setMessage("確認要刪除這筆紀錄?")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.collection("Notes")
                                        .document(note.docId)
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                arrayListNote.remove(id); //從products中移除該項目
                                                notifyItemRemoved(id); //通知移除item
                                                notifyItemRangeChanged(0,arrayListNote.size()); //刷新項目
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
        return arrayListNote.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewTime,textViewText,textViewTitle;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            textViewTitle = itemView.findViewById(R.id.MyTitle);
            textViewText = itemView.findViewById(R.id.MyText);
            textViewTime = itemView.findViewById(R.id.Time);
        }
    }
}
