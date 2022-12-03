package com.example.gp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import com.example.gp.databinding.FragmentGroupSetBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GroupSetFragment extends Fragment {

    private FragmentGroupSetBinding B;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth Auth = FirebaseAuth.getInstance();
    private StorageReference StorageRef = FirebaseStorage.getInstance().getReference();
    ArrayList<String> memberList = new ArrayList<>();
    String[] permission = {"Master","Leader","Member"};

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

        B.member.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String userPermission = memberList.get(i).substring(0,6);
                if(userPermission.equals("Master")){
                    SetPermissionDialog(i,0);
                }else if(userPermission.equals("Leader")){
                    SetPermissionDialog(i,1);
                }else {
                    SetPermissionDialog(i,2);
                }
            }
        });

        db.collection("Groups")
                .document(userGroup)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                B.GroupName.setText(task.getResult().get("GroupName").toString());

                String master = task.getResult().get("master").toString();
                ArrayList<String> leader = (ArrayList<String>) task.getResult().get("leader");
                ArrayList<String> member = (ArrayList<String>) task.getResult().get("member");

                memberList.add("Master."+master);
                for (int i = 0; i < leader.size(); i++) {
                    memberList.add("Leader. " + leader.get(i));
                }
                for (int i = 0; i < member.size(); i++) {
                    memberList.add("Member. " + member.get(i));
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

                                        memberList.add("Member. "+inputStr);
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
                new AlertDialog.Builder(getContext())
                        .setTitle("刪除")
                        .setMessage("確定要刪除群組？")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.collection("Groups")
                                        .document(userGroup)
                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                ArrayList<String> member = (ArrayList<String>) task.getResult().get("member");

                                                for(int i = 0; i < member.size(); i++){
                                                    db.collection("MemberData")
                                                            .document(member.get(i))
                                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    if(task.getResult().get("group").toString().equals(userGroup)){
                                                                        db.collection("MemberData")
                                                                                .document(task.getResult().getId())
                                                                                .update("group","");
                                                                    }
                                                                }
                                                            });
                                                }

                                                db.collection("Groups")
                                                        .document(userGroup)
                                                        .delete();
                                            }
                                        });
                                db.collection("Posts")
                                        .whereEqualTo("PostGroup", userGroup)
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                for(DocumentSnapshot doc : task.getResult()){
                                                    for (int j = 0 ; j < (long) doc.get("PostPicCount") ; j++){
                                                        StorageReference ref = StorageRef.child("PostImg").child(doc.getId() + "_" + j);

                                                        ref.delete()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        Log.d("AAAAA","刪除照片成功");
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Log.d("AAAAA","刪除照片失敗");
                                                                    }
                                                                });
                                                    }

                                                    db.collection("Post")
                                                            .document(doc.getId())
                                                            .delete();
                                                }
                                            }
                                        });

                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame_layout, new GroupFragment());
                                fragmentTransaction.commit();
                            }
                        })
                        .setNegativeButton("取消返回",null).show();
            }
        });

        return v;
    }

    private void SetPermissionDialog(int position, int permissionId) {
        new AlertDialog.Builder(getContext())
                .setTitle(memberList.get(position).substring(7))
                .setSingleChoiceItems(permission, permissionId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("取消返回",null).show();
    }

}