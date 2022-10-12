package com.example.gp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.gp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth Auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //註冊
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this,register.class);
                startActivity(it);
            }
        });
        //登入
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String UserEmail = binding.edTAccount.getText().toString();
                    String UserPassword = binding.edTPasswd.getText().toString();

                    Intent it = new Intent(MainActivity.this, index.class);
                    it.putExtra("user", UserEmail);  // for sign out

                    /* 登入認證 */
                    Auth.signInWithEmailAndPassword(UserEmail, UserPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(it);  // go to Index and send "user"
                                        Toast.makeText(MainActivity.this, "登入成功 " +
                                                task.getResult().getUser().getEmail(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this,
                                            "登入失敗 帳號或密碼錯誤", Toast.LENGTH_SHORT).show();
                                }
                            });
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "請輸入資料~", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //忘記密碼
        binding.textViewForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String UserEmail = binding.edTAccount.getText().toString();

                    Auth.sendPasswordResetEmail(UserEmail)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MainActivity.this, "已送出", Toast.LENGTH_SHORT).show();
                                }
                            });
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "請輸入信箱~", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}