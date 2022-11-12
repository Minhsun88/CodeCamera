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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RecordFragment extends Fragment {

    private FragmentRecordBinding B;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth Auth = FirebaseAuth.getInstance();

    ArrayList<Note> arrayListNote = new ArrayList<Note>();
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

        B.RecyclerView.setHasFixedSize(true);
        B.RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter =new NoteAdapter(getContext(),arrayListNote);
        B.RecyclerView.setAdapter(adapter);
        EventChangeListener();

        return view;
    }

    private void EventChangeListener() {
        db.collection("Notes")
                .whereEqualTo("NoteAuthor", Auth.getCurrentUser().getEmail())
                .orderBy("NoteTimes")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null){
                            Log.d("FireStore Error",error.getMessage());

                            return;
                        }
                        for (DocumentChange doc: value.getDocumentChanges())
                        {
                            if(doc.getType() == DocumentChange.Type.ADDED){
                                arrayListNote.add(doc.getDocument().toObject(Note.class));
                            }
                            adapter.notifyDataSetChanged();
                        }

                    }
                });
    }
}