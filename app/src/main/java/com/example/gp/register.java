package com.example.gp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gp.databinding.ActivityMainBinding;
import com.example.gp.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class register extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference StorageRef = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //返回登入畫面
        binding.textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edTAccount.getText().toString().isEmpty() == false) {
                    Intent it = new Intent(Intent.ACTION_GET_CONTENT);
                    it.setType("image/*");
                    startActivityForResult(it, 101);
                }else {
                    Toast.makeText(register.this,"請輸入帳號",Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String email = binding.edTAccount.getText().toString();
                    String pwd = binding.edTPassword.getText().toString();
                    String name = binding.edTName.getText().toString();

                    /* check if user exist */
                    mAuth.createUserWithEmailAndPassword(email,pwd)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // user registered successfully
                                    if(task.isSuccessful()){

                                        /* create account */
                                        mAuth.createUserWithEmailAndPassword(email, pwd)
                                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                    @Override
                                                    public void onSuccess(AuthResult authResult) {
                                                        String str = authResult.getUser().getEmail();
                                                        Toast.makeText(register.this, str + "已建立 ", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                        /* send the email */
                                        mAuth.getCurrentUser().sendEmailVerification()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(register.this, "已傳送認證信件", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                        /* 跳出提示視窗 */
                                        new AlertDialog.Builder(register.this)
                                                .setTitle("認證信箱")
                                                .setMessage("請到信箱中，接收認證信件\n並點選連結完成認證")
                                                .setPositiveButton("完成認證", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Toast.makeText(register.this, "好der,88", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                })
                                                .setNeutralButton("重新傳送認證信件", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        mAuth.getCurrentUser().sendEmailVerification()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        Toast.makeText(register.this, "已重新發送", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    }
                                                })
                                                .show();

                                        HashMap<String,Object> member =new HashMap<>();
                                        HashMap<String,Object> np =new HashMap<>();

//                                        member.put("account",email);
                                        member.put("password",pwd);
                                        member.put("name",name);
                                        db.collection("MemberData")
                                                .document(email)
                                                .set(member);
                                        db.collection("Posts")
                                                .document(email)
                                                .set(np);
                                        db.collection("Notes")
                                                .document(email)
                                                .set(np);
                                    }else{
                                        Toast.makeText(register.this, "帳號建立失敗", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }catch (Exception e){
                    Toast.makeText(register.this, "請輸入完整資料~", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String Email = binding.edTAccount.getText().toString();
        if ( requestCode == 101 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            Glide.with(register.this)
                    .load(uri)
                    .into(binding.imageView);

            //大頭貼存入DB
            StorageRef.child("RegisterImg").child(Email)
                    .putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("TAG","上傳成功");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG",e.getMessage());
                        }
                    });
        }
    }
}