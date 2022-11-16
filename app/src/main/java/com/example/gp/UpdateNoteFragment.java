package com.example.gp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gp.databinding.FragmentUpdateNoteBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class UpdateNoteFragment extends Fragment {

    private FragmentUpdateNoteBinding B;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        B = FragmentUpdateNoteBinding.inflate(inflater,container,false);
        View v = B.getRoot();

        Bundle bundle = getArguments();

        B.edTitle.setText(bundle.getString("title"));
        B.edText.setText(bundle.getString("text"));

        B.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> note = new HashMap();
                note.put("NoteTitles",B.edTitle.getText().toString());
                note.put("NoteTexts",B.edText.getText().toString());

                db.collection("Notes")
                        .document(bundle.getString("id"))
                        .update(note);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, new RecordFragment());
                fragmentTransaction.commit();
            }
        });
        return v;
    }
}