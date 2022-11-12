package com.example.gp;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gp.databinding.FragmentAddPostBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddPostFragment extends Fragment {
    private FragmentAddPostBinding B;
    private StorageReference StorageRef;
    private FirebaseAuth Auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList UriList = new ArrayList();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        B = FragmentAddPostBinding.inflate(inflater,container,false);
        View view = B.getRoot();
        StorageRef = FirebaseStorage.getInstance().getReference();

        Bundle bundle=getArguments();
        String selectedDate=bundle.getString("Date");
        db.collection("MemberData")
                .document(Auth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc = task.getResult();
                        if(doc.exists()){B.textViewName.setText(doc.get("name").toString());}
                    }
                });

        B.btnSavePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String Text = B.PostEdText.getText().toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/dd/MM");

                    HashMap<String,Object> Posts = new HashMap<>();
                    Posts.put("PostTexts",Text);
                    Posts.put("PostTimes",sdf.parse(selectedDate));
                    Posts.put("PostAuthor",Auth.getCurrentUser().getEmail());
                    Posts.put("PostPicCount",UriList.size());
                    db.collection("Posts")
                            .add(Posts).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            for (int i = 0 ; i < UriList.size() ; i++ ){
                                Uri uri = (Uri) UriList.get(i);

                                StorageReference ref = StorageRef.child("PostImg").child( task.getResult().getId() + "_" + i);
                                ref.putFile(uri)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                Log.d("AAAAA","上傳成功");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("AAAAA",e.getMessage());
                                            }
                                        });

                            }
                        }
                    });

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, new HomeFragment());
                    fragmentTransaction.commit();
                }catch (Exception e){
                    Log.d("addpost wrong",e.getMessage());
                }

            }
        });


        B.addPostImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Intent.ACTION_GET_CONTENT);
                it.setType("image/*");
                startActivityForResult(it, 101);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == 101 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            ImageView imageView = new ImageView(getContext());//新增ImageView
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(400, 400);
            imageView.setLayoutParams(params);//調整ImageView大小
            Glide.with(getContext())
                    .load(uri)
                    .into(imageView);
            B.addLinear.addView(imageView);

            UriList.add(uri);

        }
    }
}