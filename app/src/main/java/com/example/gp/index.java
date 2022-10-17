package com.example.gp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.example.gp.databinding.ActivityIndexBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class index extends AppCompatActivity {
    private ActivityIndexBinding B;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        B = ActivityIndexBinding.inflate(getLayoutInflater());
        setContentView(B.getRoot());
        replaceFragment(new HomeFragment());

        B.navigation.setSelectedItemId(R.id.home);

        B.navigation.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
//                case R.id.alert:
//                    replaceFragment(new AlertFragment());
//                    break;
                case R.id.record:
                    replaceFragment(new RecordFragment());
                    break;
                case R.id.post:
                    replaceFragment(new PostFragment());
                    break;
                case R.id.setting:
                    replaceFragment(new SettingFragment());
                    break;
//                case R.id.groups:
//                    replaceFragment(new GroupFragment());
//                    break;
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}