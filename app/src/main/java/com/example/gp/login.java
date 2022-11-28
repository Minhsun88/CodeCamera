package com.example.gp;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.gp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends Activity {
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
                Intent it = new Intent(login.this,register.class);
                startActivity(it);
            }
        });
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String UserEmail = binding.InputEmail.getText().toString();
                String UserPassword = binding.InputPassword.getText().toString();

                Intent it = new Intent(login.this, index.class);
                it.putExtra("user", UserEmail);  // for sign out

                if (TextUtils.isEmpty(UserEmail) == false) {           //是否有輸入帳號
                    if (TextUtils.isEmpty(UserPassword) == false) {
                        /* 登入認證 */
                        Auth.signInWithEmailAndPassword(UserEmail, UserPassword)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            startActivity(it);  // go to Index and send "user"
                                            Toast.makeText(login.this, "登入成功 " +
                                                    task.getResult().getUser().getEmail(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(login.this,
                                                "登入失敗 帳號或密碼錯誤", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
                //判斷是否有輸入帳密
                if(TextUtils.isEmpty(UserPassword)&&TextUtils.isEmpty(UserEmail))
                {
                    binding.errorText1.setText("輸入帳號");
                    binding.errorText2.setText("輸入密碼");
                    return;
                }
                if (TextUtils.isEmpty(UserPassword)) {
                    binding.errorText1.setText(" ");
                    binding.errorText2.setText("輸入密碼");
                    return;
                }
                if (TextUtils.isEmpty(UserEmail)) {
                    binding.errorText1.setText("輸入帳號");
                    binding.errorText2.setText(" ");
                    return;
                }
                if(TextUtils.isEmpty(UserPassword)==false&&TextUtils.isEmpty(UserEmail)==false)
                {
                    binding.errorText1.setText("");
                    binding.errorText2.setText("");
                    return;
                }
            }
        });
        //忘記密碼
        binding.textViewForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String UserEmail = binding.InputEmail.getText().toString();

                    Auth.sendPasswordResetEmail(UserEmail)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(login.this, "已送出", Toast.LENGTH_SHORT).show();
                                }
                            });
                }catch (Exception e){
                    Toast.makeText(login.this, "請輸入信箱~", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}