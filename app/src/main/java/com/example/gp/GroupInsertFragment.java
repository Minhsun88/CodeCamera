package com.example.gp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gp.databinding.FragmentGroupInsertBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class GroupInsertFragment extends Fragment {

    private FragmentGroupInsertBinding B;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        B = FragmentGroupInsertBinding.inflate(inflater,container,false);
        View view = B.getRoot();

        B.InsertGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                db.collection("Groups")
//                        .whereEqualTo("GroupName", B.GroupName.getText().toString())
//                        .get()
//                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if(task.isSuccessful()){
//                                    if(task.getResult().isEmpty()){
//
//                                        String GroupName = B.GroupName.getText().toString();
//
//                                        HashMap<String,Object> Group = new HashMap<>();
//                                        Group.put("GroupName",GroupName);
//
//                                        db.collection("Groups")
//                                                .add(Group);
//
//                                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                                        fragmentTransaction.replace(R.id.frame_layout,new GroupSelectFragment());
//                                        fragmentTransaction.commit();
//                                    }else{
//                                        Toast.makeText(getActivity(),"重複組名",Toast.LENGTH_SHORT).show();
//                                        Log.d("AAAAA","重複");
//                                    }
//                                }
//                            }
//                        });
                Toast toast = Toast.makeText(
                        getActivity().getApplicationContext(), "Custom Toast From Fragment", Toast.LENGTH_LONG
                );
                // Set the Toast display position layout center
                toast.setGravity(Gravity.CENTER,0,0);
                // Finally, show the toast
                toast.show();
                Log.d("AAAAA",getActivity().toString());
            }
        });
        return view;
    }

}