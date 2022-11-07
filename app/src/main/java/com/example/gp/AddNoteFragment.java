package com.example.gp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.gp.databinding.FragmentAddNoteBinding;
import com.example.gp.databinding.FragmentRecordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class AddNoteFragment extends Fragment {

    private FragmentAddNoteBinding B;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth Auth = FirebaseAuth.getInstance();

    DatePickerDialog.OnDateSetListener datePicker;
    Calendar calendar = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        B = FragmentAddNoteBinding.inflate(inflater,container,false);
        View view = B.getRoot();

        B.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SimpleDateFormat formatDT = new SimpleDateFormat("yyyy/MM/ddHH:mm");
                    formatDT.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));

                    String Title = B.edTitle.getText().toString();
                    String Text = B.edText.getText().toString();
                    String Date,Time;


                    if(B.TextViewDate.getText().toString() == ""){
                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                        format.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));

                        Date = format.format(new Date());
                    }else {
                        Date = B.TextViewDate.getText().toString().substring(5);
                    }
                    if(B.TextViewTime.getText().toString() == ""){
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                        format.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));

                        Time = format.format(new Date());
                    }else {
                        Time = B.TextViewTime.getText().toString().substring(5);
                    }

                    Date date = formatDT.parse(Date+Time);

                    HashMap<String, Object> Notes = new HashMap<>();
                    Notes.put("NoteTitles", Title);
                    Notes.put("NoteTexts", Text);
                    Notes.put("NoteTimes", date);
                    Notes.put("NoteAuthor",Auth.getCurrentUser().getEmail());

                    db.collection("Notes")
                            .add(Notes);
                }catch (Exception e){
                    Log.d("AAAAA",e.getMessage());
                }
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout,new RecordFragment());
                fragmentTransaction.commit();
            }
        });

        B.btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int HH, int mm) {
                        String timeS;
                        if(HH >= 0 && HH < 10){
                            timeS = "時間 : " + "0" + HH;
                        }else {
                            timeS = "時間 : " + HH;
                        }
                        if(mm >= 0 && mm < 10){
                            timeS += ":0"+mm;
                        }else{
                            timeS += ":"+mm;
                        }
                        B.TextViewTime.setText(timeS);
                    }
                },hour,minute,true).show();
            }
        });
        //日期
        datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                String myFormat = "yyyy/MM/dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.TAIWAN);
                B.TextViewDate.setText("日期 : " + sdf.format(calendar.getTime()));
            }
        };
        //日期
        B.btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(),datePicker,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        return view;
    }

}