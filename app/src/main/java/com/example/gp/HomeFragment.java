package com.example.gp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.gp.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding B;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth Auth = FirebaseAuth.getInstance();

    ArrayList<Note> arrayListNote = new ArrayList<Note>();
    ArrayList<Post> arrayListPost = new ArrayList<Post>();
    ArrayList<Integer> PositionList = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        B = FragmentHomeBinding.inflate(inflater, container, false);
        View view = B.getRoot();


        if (!B.Modeswitch.isChecked()){
            readPost(new PostCallback() {
                @Override
                public void onCallback(ArrayList<Post> list) {
                    PostCalendarSetting(list);
                }
            });
        }

        B.Modeswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    readPost(new PostCallback() {
                        @Override
                        public void onCallback(ArrayList<Post> list) {
                            PostCalendarSetting(list);
                        }
                    });
                } else {
                    readNote(new NoteCallback() {
                        @Override
                        public void onCallback(ArrayList<Note> list) {
                            NoteCalendarSetting(list);
                        }
                    });
                }
            }
        });

        return view;
    }

    private void PostCalendarSetting(ArrayList<Post> list) {
        List<EventDay> event = new ArrayList<>();
        list.forEach(
                (post -> ListGetting(post.PostTimes,event,R.drawable.ic_baseline_photo_library_24)
                ));

        B.calendarView.setEvents(event);
        B.calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                db.collection("MemberData")
                        .document(Auth.getCurrentUser().getEmail())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(!task.getResult().get("group").toString().equals("")){
                            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/dd/MM");
                            String selectedDay = sdf.format(eventDay.getCalendar().getTime());

                            for(int i = 0; i < list.size(); i++){
                                if(selectedDay.equals(sdf.format(list.get(i).PostTimes))){
                                    if(!list.get(i).PostAuthor.equals("")){
                                        db.collection("MemberData")
                                                .document(list.get(i).PostAuthor)
                                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                arrayAdapter.add(task.getResult().get("name") + "的貼文");
                                            }
                                        });

                                        PositionList.add(i);
                                    }else {
                                        arrayAdapter.add("查無此人(已踢出)的貼文");
                                        PositionList.add(i);
                                    }
                                }
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                                    .setTitle("本日貼文")
                                    .setIcon(R.drawable.ic_baseline_photo_library_24)
                                    .setPositiveButton("新增貼文", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("Date", selectedDay);
                                            PostAddFragment postAddFragment = new PostAddFragment();
                                            postAddFragment.setArguments(bundle);

                                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                            fragmentTransaction.replace(R.id.frame_layout, postAddFragment);
                                            fragmentTransaction.commit();
                                        }
                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).setAdapter(arrayAdapter, null);

                            builder.show();
                        }else {
                            new AlertDialog.Builder(getContext())
                                    .setTitle(Auth.getCurrentUser().getEmail())
                                    .setMessage("尚未設定群組，請至群組區設置")
                                    .setPositiveButton("返回", null).show();
                        }
                    }
                });
            }
        });
    }

    private void NoteCalendarSetting(ArrayList<Note> list) {
        List<EventDay> event = new ArrayList<>();
        list.forEach(
                (note -> ListGetting(note.NoteTimes, event, R.drawable.ic_baseline_list_alt_24)
                ));

        B.calendarView.setEvents(event);

        B.calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String selectedDay = sdf.format(eventDay.getCalendar().getTime());

                for(int i = 0; i < list.size(); i++){
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

                    if(selectedDay.equals(sdf.format(list.get(i).NoteTimes))){
                        if(list.get(i).NoteTitles.equals("")){
                            arrayAdapter.add(sdf.format(list.get(i).NoteTimes)+"  無標題");
                        }else {
                            arrayAdapter.add(sdf.format(list.get(i).NoteTimes)+"  "+list.get(i).NoteTitles);
                        }
                        PositionList.add(i);
                    }
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle("本日紀錄")
                        .setIcon(R.drawable.ic_baseline_list_alt_24)
                        .setPositiveButton("新增紀錄", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Bundle bundle = new Bundle();
                                bundle.putString("Date", selectedDay);
                                NoteAddFragment noteAddFragment = new NoteAddFragment();
                                noteAddFragment.setArguments(bundle);

                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame_layout, noteAddFragment);
                                fragmentTransaction.commit();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setAdapter(arrayAdapter, null);

                builder.show();
            }
        });
    }

    public interface NoteCallback {
        void onCallback(ArrayList<Note> list);
    }

    public void readNote(NoteCallback noteCallback) {
        db.collection("Notes")
                .whereEqualTo("NoteAuthor", Auth.getCurrentUser().getEmail())
                .orderBy("NoteTimes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot doc : task.getResult()){
                            arrayListNote.add(doc.toObject(Note.class));
                        }
                        noteCallback.onCallback(arrayListNote);
                    }
                });
    }

    public interface PostCallback {
        void onCallback(ArrayList<Post> list);
    }

    public void readPost(PostCallback postCallback) {
        db.collection("MemberData")
                .document(Auth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        db.collection("Posts")
                                .whereEqualTo("PostGroup", task.getResult().get("group").toString())
                                .orderBy("PostTimes")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for(DocumentSnapshot doc : task.getResult()){
                                            arrayListPost.add(doc.toObject(Post.class));
                                        }
                                        postCallback.onCallback(arrayListPost);
                                    }
                                });
                    }
                });
    }

    private void ListGetting(Date date,List<EventDay> list,Integer icon) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            String dateToStr = sdf.format(date);
            date = sdf.parse(dateToStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            list.add(new EventDay(calendar, icon));
        } catch (Exception e) {
            Log.d("AAAAA", e.getMessage());
        }
    }
}