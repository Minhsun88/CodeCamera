package com.example.gp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.gp.databinding.FragmentNoteAddBinding;
import com.example.gp.databinding.FragmentGroupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GroupFragment extends Fragment {

    private FragmentGroupBinding B;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth Auth = FirebaseAuth.getInstance();
    ArrayList<String> GroupList = new ArrayList<>();
    ArrayList<String> NameList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        B = FragmentGroupBinding.inflate(inflater,container,false);
        View view= B.getRoot();

        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_text,
                NameList);
        B.Spinner.setAdapter(adapter);

        db.collection("Groups")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot doc : task.getResult()){
                            String master = doc.get("master").toString();
                            ArrayList<String> leaderList = (ArrayList<String>) doc.get("leader");
                            ArrayList<String> memberList = (ArrayList<String>) doc.get("member");

                            if(master.equals(Auth.getCurrentUser().getEmail())){
                                NameList.add(doc.get("GroupName").toString());
                                GroupList.add(doc.getId());
                            }
                            for (int i = 0; i < leaderList.size(); i++){
                                if(leaderList.get(i).equals(Auth.getCurrentUser().getEmail())){
                                    NameList.add(doc.get("GroupName").toString());
                                    GroupList.add(doc.getId());
                                }
                            }
                            for (int i = 0; i < memberList.size(); i++){
                                if(memberList.get(i).equals(Auth.getCurrentUser().getEmail())){
                                    NameList.add(doc.get("GroupName").toString());
                                    GroupList.add(doc.getId());
                                }
                            }

                            adapter.notifyDataSetChanged();
                        }

                    }
                });

        B.Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                db.collection("MemberData")
                        .document(Auth.getCurrentUser().getEmail())
                        .update("group", GroupList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        B.GroupAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout,new GroupAddFragment());
                fragmentTransaction.commit();
            }
        });

        B.GroupSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("MemberData")
                        .document(Auth.getCurrentUser().getEmail())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                String userGroup = task.getResult().get("group").toString();
                                Bundle bundle = new Bundle();
                                GroupSetFragment groupSetFragment = new GroupSetFragment();
                                if(!userGroup.equals("")){
                                bundle.putString("userGroup", task.getResult().get("group").toString());
                                groupSetFragment.setArguments(bundle);

                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame_layout, groupSetFragment);
                                fragmentTransaction.commit();
                                }else {
                                    Log.d("AAAAA","請選擇群組");
                                }
                            }
                        });
            }
        });

        return view;
    }
}