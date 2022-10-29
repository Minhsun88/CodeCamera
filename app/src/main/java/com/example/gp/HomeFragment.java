package com.example.gp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.gp.databinding.ActivityIndexBinding;
import com.example.gp.databinding.FragmentAddNoteBinding;
import com.example.gp.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding B;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth Auth = FirebaseAuth.getInstance();
    List<EventDay> event = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        B = FragmentHomeBinding.inflate(inflater,container,false);
        View view = B.getRoot();

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