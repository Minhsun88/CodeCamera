package com.example.gp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.gp.databinding.FragmentGroupSetBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GroupSetFragment extends Fragment {

    private FragmentGroupSetBinding B;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth Auth = FirebaseAuth.getInstance();
    ArrayList<String> memberList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        B = FragmentGroupSetBinding.inflate(inflater,container,false);
        View v = B.getRoot();

        Bundle bundle = getArguments();
        String userGroup = bundle.getString("userGroup");

        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                memberList);
        B.member.setAdapter(adapter);

        db.collection("Groups")
                .document(userGroup)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                B.GroupName.setText(task.getResult().get("GroupName").toString());

                ArrayList<String> list = (ArrayList<String>) task.getResult().get("member");

                for (int i = 0; i < list.size(); i++) {
                    memberList.add(String.valueOf(i+1) + ". " + list.get(i));
                }
                adapter.notifyDataSetChanged();
            }
        });

        B.addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Groups")
                        .document(userGroup)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        ArrayList<String> list = (ArrayList<String>) task.getResult().get("member");

                        db.collection("MemberData")
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                String inputStr = B.memberId.getText().toString();

                                for (DocumentSnapshot doc : task.getResult()){
                                    if(doc.getId().equals(inputStr)){
                                        list.add(inputStr);
                                        db.collection("Groups")
                                                .document(userGroup)
                                                .update("member",list);

                                        memberList.clear();
                                        for (int i = 0; i < list.size(); i++) {
                                            memberList.add(String.valueOf(i+1) + ". " + list.get(i));
                                        }
                                        adapter.notifyDataSetChanged();
                                        B.memberId.setText("");
                                    }
                                }

                            }
                        });
                    }
                });
            }
        });

        B.deleteGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Groups")
                        .document(userGroup)
                        .delete();
                db.collection("MemberData")
                        .document(Auth.getCurrentUser().getEmail())
                        .update("group","");

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, new GroupFragment());
                fragmentTransaction.commit();
            }
        });

        return v;
    }
}