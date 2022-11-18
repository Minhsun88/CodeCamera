package com.example.gp;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.gp.databinding.FragmentUpdatePostBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.HashMap;

public class UpdatePostFragment extends Fragment {

    FragmentUpdatePostBinding B;
    private StorageReference StorageRef = FirebaseStorage.getInstance().getReference();
    private FirebaseAuth Auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        B = FragmentUpdatePostBinding.inflate(inflater,container,false);
        View v = B.getRoot();

        Bundle bundle = getArguments();

        StorageRef.child("RegisterImg").child(Auth.getCurrentUser().getEmail())
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getContext())
                                .load(uri)
                                .into(B.PostImg);
                    }
                });
        B.textViewName.setText(bundle.getString("name"));
        B.PostEdText.setText(bundle.getString("text"));

        for (int i = 0 ; i < bundle.getLong("count") ; i++){
            StorageReference ref = StorageRef.child("PostImg").child(bundle.getString("id") + "_" + i);
            File file;
            try {
                file = File.createTempFile("images","png");
                ref.getFile(file)
                        .addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                ImageView imageView = new ImageView(getContext());
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(500, 500);
                                imageView.setLayoutParams(params);

                                Glide.with(getContext())
                                        .load(Uri.fromFile(file))
                                        .into(imageView);

                                B.Linear.addView(imageView);
                            }
                        });
            }catch (Exception e){
                Log.d("AAAAA",e.getMessage());
            }
        }

        B.btnSavePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Posts")
                        .document(bundle.getString("id"))
                        .update("PostTexts",B.PostEdText.getText().toString());

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout,new PostFragment());
                fragmentTransaction.commit();
            }
        });

        return v;
    }
}