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

    private ArrayList TitleList = new ArrayList();
    private ArrayList TextList = new ArrayList();
    private ArrayList TimeList = new ArrayList();
    private ArrayList DateList = new ArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        B = FragmentAddNoteBinding.inflate(inflater,container,false);
        View view = B.getRoot();

        db.collection("Notes").document(Auth.getCurrentUser().getEmail()).
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if(     document.get("NoteTitles") != null &&
                                document.get("NoteTexts") != null &&
                                document.get("NoteDates") != null &&
                                document.get("NoteTimes") != null ) {
                            TitleList = (ArrayList) document.get("NoteTitles");
                            TextList = (ArrayList) document.get("NoteTexts");
                            TimeList = (ArrayList) document.get("NoteTimes");
                            DateList = (ArrayList) document.get("NoteDates");
                        }
                    } else {
                        Log.d("AAAAA","沒資料");
                    }
                } else {
                    Log.d("AAAAA",task.getException().toString());
                }
            }
        });

        B.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String Title = B.edTitle.getText().toString();
                    String Text = B.edText.getText().toString();

                    TitleList.add(Title);
                    TextList.add(Text);
                    if(B.TextViewDate.getText().toString() == ""){
                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                        format.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));

                        DateList.add(format.format(new Date()));
                    }else {
                        DateList.add(B.TextViewTime.getText().toString().substring(5));
                    }
                    if(B.TextViewTime.getText().toString() == ""){
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                        format.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));

                        TimeList.add(format.format(new Date()));
                    }else {
                        TimeList.add(B.TextViewTime.getText().toString().substring(5));
                    }
                    HashMap<String, Object> Notes = new HashMap<>();
                    Notes.put("NoteTitles", TitleList);
                    Notes.put("NoteTexts", TextList);
                    Notes.put("NoteDates", DateList);
                    Notes.put("NoteTimes", TimeList);
                    db.collection("Notes")
                            .document(Auth.getCurrentUser().getEmail())
                            .update(Notes);
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
                        if(mm >= 0 && mm < 10){
                            B.TextViewTime.setText("時間 : "+HH + ":0"+mm);
                        }else{
                            B.TextViewTime.setText("時間 : "+HH + ":"+mm);
                        }
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