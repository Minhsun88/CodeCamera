package com.example.gp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gp.databinding.FragmentPostBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PostFragment extends Fragment {

    private FragmentPostBinding B;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth Auth = FirebaseAuth.getInstance();
    ArrayList<Post> arrayListPost = new ArrayList<Post>();
    PostAdapter adapter;
    Integer position = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        B = FragmentPostBinding.inflate(inflater,container,false);
        View view = B.getRoot();

        B.RecyclerView.setHasFixedSize(true);
        B.RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PostAdapter(getContext(),arrayListPost);
        B.RecyclerView.setAdapter(adapter);
        EventChangeListener();

        return view;
    }

    private void EventChangeListener() {
                db.collection("MemberData")
                        .document(Auth.getCurrentUser().getEmail())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        db.collection("Posts")
                                .whereEqualTo("PostGroup", task.getResult().get("group").toString())
                                .orderBy("PostTimes")
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                        if(error != null){
                                            Log.d("FireStore Error",error.getMessage());

                                            return;
                                        }
                                        for (DocumentChange doc: value.getDocumentChanges())
                                        {
                                            if(doc.getType() == DocumentChange.Type.ADDED){
                                                arrayListPost.add(doc.getDocument().toObject(Post.class));
                                            }
                                            adapter.notifyDataSetChanged();
                                        }

                                    }
                                });
                    }
                });
    }
}