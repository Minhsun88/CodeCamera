package com.example.gp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;

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
    String[] permission = {"Leader","Member"};
    String alterPermission ;

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

                      String master = task.getResult().get("master").toString();
                      ArrayList<String> leader = (ArrayList<String>) task.getResult().get("leader");
                      ArrayList<String> member = (ArrayList<String>) task.getResult().get("member");

                      inputList(master, leader, member, adapter);
                  }
              });

        B.member.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                db.collection("Groups")
                        .document(userGroup)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String GM = task.getResult().get("master").toString();
                        String userPermission = memberList.get(i).substring(0,6);

                        if(GM.equals(Auth.getCurrentUser().getEmail())){
                            if(userPermission.equals("Master")){
                                new AlertDialog.Builder(getContext())
                                        .setTitle(GM)
                                        .setMessage("您已經是GM了")
                                        .setNegativeButton("返回",null).show();
                            }else if(userPermission.equals("Leader")){
                                SetPermissionDialog(i, 0, userGroup, adapter);
                            }else {
                                SetPermissionDialog(i, 1, userGroup, adapter);
                            }
                        }else {
                            new AlertDialog.Builder(getContext())
                                    .setTitle(Auth.getCurrentUser().getEmail())
                                    .setMessage("權限不足，無法設置權限")
                                    .setNegativeButton("返回",null).show();
                        }
                    }
                });
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
                        String master = task.getResult().get("master").toString();
                        ArrayList<String> leader = (ArrayList<String>) task.getResult().get("leader");
                        ArrayList<String> member = (ArrayList<String>) task.getResult().get("member");
                        Boolean CheckPermission = false;

                        if (master.equals(Auth.getCurrentUser().getEmail())){
                            CheckPermission = true;
                        }
                        for (int i = 0; i < leader.size(); i++){
                            if (leader.get(i).equals(Auth.getCurrentUser().getEmail())){
                                CheckPermission = true;
                            }
                        }
                        if(CheckPermission){
                            db.collection("MemberData")
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    String inputStr = B.memberId.getText().toString();

                                    for (DocumentSnapshot doc : task.getResult()){
                                        if(doc.getId().equals(inputStr)){
                                            member.add(inputStr);
                                            db.collection("Groups")
                                                    .document(userGroup)
                                                    .update("member",member);

                                            memberList.add("Member. "+inputStr);
                                            adapter.notifyDataSetChanged();
                                            B.memberId.setText("");
                                        }
                                    }
                                }
                            });
                        } else {
                            new AlertDialog.Builder(getContext())
                                    .setTitle(Auth.getCurrentUser().getEmail())
                                    .setMessage("權限不足，無法新增人員")
                                    .setNegativeButton("返回", null).show();
                            B.memberId.setText("");
                        }
                    }
                });
            }
        });

        B.deleteGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Groups")
                        .document(userGroup)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String master = task.getResult().get("master").toString();

                        if(master.equals(Auth.getCurrentUser().getEmail())){
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
                        }else {
                            new AlertDialog.Builder(getContext())
                                    .setTitle(Auth.getCurrentUser().getEmail())
                                    .setMessage("權限不足，無法刪除群組")
                                    .setPositiveButton("返回", null).show();
                        }
                    }
                });

            }
        });

        return v;
    }

    private void SetPermissionDialog(int position, int permissionId, String userGroup, ArrayAdapter adapter) {
        alterPermission = permission[permissionId];
        String AlterTarget = memberList.get(position).substring(8);

        new AlertDialog.Builder(getContext())
                .setTitle(AlterTarget)
                .setSingleChoiceItems(permission, permissionId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alterPermission = permission[which];
                    }
                })
                .setPositiveButton("修改權限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(alterPermission.equals(permission[permissionId])){
                            Log.d("AAAAA",AlterTarget + " 已經是 " + alterPermission + " 囉!");
                        }else {
                            db.collection("Groups")
                                    .document(userGroup)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    String master = task.getResult().get("master").toString();
                                    ArrayList<String> member = (ArrayList<String>) task.getResult().get("member");
                                    ArrayList<String> leader = (ArrayList<String>) task.getResult().get("leader");

                                    if(alterPermission.equals("Leader")){
                                        for (int i = 0; i < member.size(); i++){
                                            if(member.get(i).equals(AlterTarget)){
                                                member.remove(i);
                                                leader.add(AlterTarget);
                                                db.collection("Groups")
                                                        .document(userGroup)
                                                        .update("member", member,
                                                                "leader",leader);

                                                inputList(master, leader, member, adapter);
                                            }
                                        }
                                    }else if(alterPermission.equals("Member")){
                                        for (int i = 0; i < leader.size(); i++){
                                            if(leader.get(i).equals(AlterTarget)){
                                                leader.remove(i);
                                                member.add(AlterTarget);
                                                db.collection("Groups")
                                                        .document(userGroup)
                                                        .update("member", member,
                                                                "leader",leader);

                                                inputList(master, leader, member, adapter);
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                })
                .setNegativeButton("踢出群組", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new AlertDialog.Builder(getContext())
                                .setTitle(AlterTarget)
                                .setMessage("確認踢出?")
                                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        db.collection("Groups")
                                                .document(userGroup)
                                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                String master = task.getResult().get("master").toString();
                                                ArrayList<String> member = (ArrayList<String>) task.getResult().get("member");
                                                ArrayList<String> leader = (ArrayList<String>) task.getResult().get("leader");
                                                for (int i = 0; i < member.size(); i++){
                                                    if(member.get(i).equals(AlterTarget)){
                                                        member.remove(i);
                                                    }
                                                }
                                                for (int i = 0; i < leader.size(); i++){
                                                    if(leader.get(i).equals(AlterTarget)){
                                                        leader.remove(i);
                                                    }
                                                }
                                                db.collection("Groups")
                                                        .document(userGroup)
                                                        .update("member", member
                                                                , "leader", leader);
                                                resetPostAuthor(AlterTarget);
                                                resetKickGroup(AlterTarget, userGroup);

                                                inputList(master, leader, member, adapter);
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                    }
                })
                .setNeutralButton("取消返回",null)
                .show();
    }

    private void resetKickGroup(String alterTarget,String userGroup) {
        db.collection("MemberData")
                .document(alterTarget)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().get("group").toString().equals(userGroup)){
                    db.collection("MemberData")
                            .document(alterTarget)
                            .update("group", "");
                }
            }
        });
    }

    private void resetPostAuthor(String AlterTarget) {
        db.collection("Posts")
                .whereEqualTo("PostAuthor", AlterTarget)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc: task.getResult()){
                    db.collection("Posts")
                            .document(doc.getId())
                            .update("PostAuthor", "");
                }
            }
        });
    }

    private void inputList(String master, ArrayList<String> leader, ArrayList<String> member, ArrayAdapter adapter) {
        memberList.clear();
        memberList.add("Master. "+master);
        for (int j = 0; j < leader.size(); j++) {
            memberList.add("Leader. " + leader.get(j));
        }
        for (int j = 0; j < member.size(); j++) {
            memberList.add("Member. " + member.get(j));
        }
        adapter.notifyDataSetChanged();
    }

}