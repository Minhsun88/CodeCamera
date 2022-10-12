package com.example.gp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gp.databinding.FragmentAddNoteBinding;
import com.example.gp.databinding.FragmentRecordBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AddNoteFragment extends Fragment {

    private FragmentAddNoteBinding B;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        B = FragmentAddNoteBinding.inflate(inflater,container,false);
        View view = B.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        B.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Title = B.edTitle.getText().toString();
                String Text = B.edText.getText().toString();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
                String Time =  format.format(new Date());

                HashMap<String,Object> Notes = new HashMap<>();
                Notes.put("Title",Title);
                Notes.put("Text",Text);
                Notes.put("Time",Time);

                db.collection("Notes")
                        .document(Title)
                        .set(Notes);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout,new RecordFragment());
                fragmentTransaction.commit();
            }
        });
    }
}