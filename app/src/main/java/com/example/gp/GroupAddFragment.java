package com.example.gp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.gp.databinding.FragmentGroupAddBinding;
import com.example.gp.databinding.FragmentGroupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupAddFragment extends Fragment {

    private FragmentGroupAddBinding B;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth Auth = FirebaseAuth.getInstance();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        B = FragmentGroupAddBinding.inflate(inflater,container,false);
        View v = B.getRoot();

        B.Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(B.GroupName.getText().toString().equals("")){
                    Log.d("AAAAA","名稱不能空值");
                }else {
                    HashMap<String,Object> group = new HashMap<>();
                    ArrayList<String> member = new ArrayList<>();

                    group.put("GroupName",B.GroupName.getText().toString());
                    group.put("master", Auth.getCurrentUser().getEmail());
                    group.put("member", member);
                    group.put("leader", member);
                    db.collection("Groups")
                            .add(group);

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout,new GroupFragment());
                    fragmentTransaction.commit();
                }
            }
        });

        return v;
    }
}