package com.example.gp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gp.databinding.FragmentAddPostBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class AddPostFragment extends Fragment {

    private FragmentAddPostBinding B;

    private StorageReference StorageRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String postId ;

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


        db.collection("MemberData")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot doc : task.getResult()){
                                String name = doc.getString("name");
                                B.textViewName.setText(name);
                            }
                        }
                    }
                });

        B.btnSavePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Text = B.PostEdText.getText().toString();
                String Name = B.textViewName.getText().toString();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                String Time =  format.format(new Date());
                postId = "Post" + "_" +Time;

                HashMap<String,Object> Post = new HashMap<>();
                Post.put("Text",Text);
                Post.put("Time",Time);
                Post.put("Name",Name);
                Post.put("postId",postId);

                db.collection("Post")
                        .document(postId)
                        .set(Post);


                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout,new PostFragment());
                fragmentTransaction.commit();
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
}