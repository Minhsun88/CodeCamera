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
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                                    String dateInString = doc.get("PostTimes").toString();
                                    try {
                                        Date date = sdf.parse(dateInString);
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
                        B.calendarView.setEvents(event);

                        db.collection("Notes")
                                .document(Auth.getCurrentUser().getEmail())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot doc = task.getResult();
                                        try {
                                            TimeList = (ArrayList) doc.get("NoteDates");
                                            for(int i = 0 ; i < TimeList.size() ; i++){
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                                                Date date = sdf.parse(TimeList.get(i));
                                                Calendar calendar = Calendar.getInstance();

                                                calendar.setTime(date);
                                                event.add(new EventDay(calendar, R.drawable.ic_baseline_list_alt_24));
                                            }

                                            B.calendarView.setEvents(event);
                                        } catch (Exception e){
                                            Log.d("AAAAA",e.getMessage());
                                        }
                                    }
                                });
                    } catch (Exception e){
                        Log.d("AAAAA",e.getMessage());
                    }
                } else {
                    try {
                        List<EventDay> event = new ArrayList<>();
                        B.calendarView.setEvents(event);

                        db.collection("Posts")
                                .whereEqualTo("PostAuthor",Auth.getCurrentUser().getEmail())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (QueryDocumentSnapshot doc : task.getResult()){
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                                            String dateInString = doc.get("PostTimes").toString();
                                            try {
                                                Date date = sdf.parse(dateInString);
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
//        B.calendarView.setOnDayClickListener(new OnDayClickListener() {
//            @Override
//            public void onDayClick(EventDay eventDay) {
//                event.add(new EventDay(eventDay.getCalendar(), R.drawable.ic_baseline_message_24));
//                try {
//                    Log.d("AAAAA",event.toString());
//                    B.calendarView.setEvents(event);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        Date date = new Date();
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat sdfY = new SimpleDateFormat("yyyy");
//        SimpleDateFormat sdfM = new SimpleDateFormat("MM");
//        SimpleDateFormat sdfD = new SimpleDateFormat("dd");
//        /**calender設置為今日*/
//        calendar.set(Integer.parseInt(sdfY.format(date))
//                , Integer.parseInt(sdfM.format(date)) - 1
//                , Integer.parseInt(sdfD.format(date)));
//        try {
//                    B.calendarView.setEvents(event);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return view;
    }

}