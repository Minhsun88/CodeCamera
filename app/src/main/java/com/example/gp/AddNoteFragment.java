package com.example.gp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gp.databinding.FragmentAddNoteBinding;
import com.example.gp.databinding.FragmentRecordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AddNoteFragment extends Fragment {

    private FragmentAddNoteBinding B;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth Auth = FirebaseAuth.getInstance();

    private ArrayList TitleList = new ArrayList();
    private ArrayList TextList = new ArrayList();
    private ArrayList TimeList = new ArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        B = FragmentAddNoteBinding.inflate(inflater,container,false);
        View view = B.getRoot();

        db.collection("MemberData").document(Auth.getCurrentUser().getEmail()).
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if(document.get("Titles") != null && document.get("Texts") != null && document.get("Times") != null) {
                            TitleList = (ArrayList) document.get("Titles");
                            TextList = (ArrayList) document.get("Texts");
                            TimeList = (ArrayList) document.get("Times");
                        }
                    } else {
                        Log.d("AAAAA","沒資料");
                    }
                } else {
                    Log.d("AAAAA",task.getException().toString());
                }
            }
        });

        B.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Title = B.edTitle.getText().toString();
                String Text = B.edText.getText().toString();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
                String Time =  format.format(new Date());

                TitleList.add(Title);
                TextList.add(Text);
                TimeList.add(Time);

                HashMap<String,Object> Notes = new HashMap<>();
                Notes.put("Titles",TitleList);
                Notes.put("Texts",TextList);
                Notes.put("Times",TimeList);

                db.collection("MemberData")
                        .document(Auth.getCurrentUser().getEmail())
                        .update(Notes);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout,new RecordFragment());
                fragmentTransaction.commit();
            }
        });

        return view;
    }

}