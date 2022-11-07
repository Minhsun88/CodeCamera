package com.example.gp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.gp.databinding.ActivityIndexBinding;
import com.example.gp.databinding.FragmentAddNoteBinding;
import com.example.gp.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding B;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth Auth = FirebaseAuth.getInstance();
    List<String> TimeList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        B = FragmentHomeBinding.inflate(inflater,container,false);
        View view = B.getRoot();

        if(!B.Modeswitch.isChecked()){
            try {
                List<EventDay> event = new ArrayList<>();

                db.collection("Posts")
                        .whereEqualTo("PostAuthor",Auth.getCurrentUser().getEmail())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (QueryDocumentSnapshot doc : task.getResult()){
                                    Timestamp ts = (Timestamp) doc.get("PostTimes");

                                    try {
                                        Date date = ts.toDate();
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTime(date);
                                        event.add(new EventDay(calendar, R.drawable.ic_baseline_photo_library_24));
                                    } catch (Exception e){
                                        Log.d("AAAAA",e.getMessage());
                                    }
                                }
                                B.calendarView.setEvents(event);
                            }
                        });
            } catch (Exception e){
                Log.d("AAAAA",e.getMessage());
            }

        }

        B.Modeswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    try {
                        List<EventDay> event = new ArrayList<>();

                        db.collection("Notes")
                                .whereEqualTo("NoteAuthor",Auth.getCurrentUser().getEmail())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (QueryDocumentSnapshot doc : task.getResult()){
                                            Timestamp ts = (Timestamp) doc.get("NoteTimes");

                                            try {
                                                Date date = ts.toDate();
                                                Calendar calendar = Calendar.getInstance();
                                                calendar.setTime(date);
                                                event.add(new EventDay(calendar, R.drawable.ic_baseline_list_alt_24));
                                            } catch (Exception e){
                                                Log.d("AAAAA",e.getMessage());
                                            }
                                        }
                                        B.calendarView.setEvents(event);
                                    }
                                });
                    } catch (Exception e){
                        Log.d("AAAAA",e.getMessage());
                    }
                } else {
                    try {
                        List<EventDay> event = new ArrayList<>();

                        db.collection("Posts")
                                .whereEqualTo("PostAuthor",Auth.getCurrentUser().getEmail())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (QueryDocumentSnapshot doc : task.getResult()){
                                            Timestamp ts = (Timestamp) doc.get("PostTimes");

                                            try {
                                                Date date = ts.toDate();
                                                Calendar calendar = Calendar.getInstance();
                                                calendar.setTime(date);
                                                event.add(new EventDay(calendar, R.drawable.ic_baseline_photo_library_24));
                                            } catch (Exception e){
                                                Log.d("AAAAA",e.getMessage());
                                            }
                                        }
                                        B.calendarView.setEvents(event);
                                    }
                                });
                    } catch (Exception e){
                        Log.d("AAAAA",e.getMessage());
                    }
                }
            }
        });
        return view;
    }

}