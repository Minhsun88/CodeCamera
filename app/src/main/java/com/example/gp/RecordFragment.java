package com.example.gp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gp.databinding.FragmentRecordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RecordFragment extends Fragment {

    private FragmentRecordBinding B;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String RcTitle;
    ArrayList<String> arrayList = new ArrayList<>();
    NoteAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        B = FragmentRecordBinding.inflate(inflater,container,false);
        View view = B.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db.collection("Notes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot doc: task.getResult())
                            {
                                String Title = doc.getString("Title");
                                RcTitle = Title;
                                Log.d("Demo",""+Title);
                                arrayList.add(RcTitle);

                            }
                            Log.d("Demo",""+arrayList.size());
                            adapter =new NoteAdapter(getContext(),arrayList);
                            B.RecyclerView.setAdapter(adapter);
                            B.RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    }
                });

        B.btnAddNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout,new AddNoteFragment());
                fragmentTransaction.commit();
            }
        });
    }

}