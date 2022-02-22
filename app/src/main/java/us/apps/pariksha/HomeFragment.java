package us.apps.pariksha;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.grpc.okhttp.internal.Platform;

public class HomeFragment extends Fragment {
    View view;
    private static final String TAG = "HomeFragment";
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore fStore;
    private TextView name, phoneNum, emailDis,rollNum, eTitle1, eTitle2, eTitle3, eTitle4,
            eTitle5, eTitle6, eTitle7, eTitle8, eTitle9, eTitle10, eOrg1, eOrg2, eOrg3, eOrg4,
            eOrg5, eOrg6, eOrg7, eOrg8, eOrg9, eOrg10,date1,date2,date3,date4,date5,date6,date7,
            date8,date9,date10,time1,time2,time3,time4,time5,time6,time7,time8,time9,time10,timeAll1,
            timeAll2,timeAll3,timeAll4,timeAll5,timeAll6,timeAll7,timeAll8,timeAll9,timeAll10;
    private String userId,quizIdCheck,userProfile,quizIdQues,atpdVAl,quizPass;
    private CardView card1,card2,card3,card4,card5,card6,card7,card8,card9,card10;
    private TextView headingQuiz;
    private SwipeRefreshLayout swipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        fStore=FirebaseFirestore.getInstance();

        headingQuiz=(TextView)view.findViewById(R.id.quizListHEad);
        phoneNum=(TextView)view.findViewById(R.id.phoneTextV);
        name=(TextView)view.findViewById(R.id.nameTextV);
        emailDis=(TextView)view.findViewById(R.id.emailTextV);
        rollNum=(TextView)view.findViewById(R.id.rollnoTextV);
        card1=view.findViewById(R.id.cardOne);
        card2=view.findViewById(R.id.cardTwo);
        card3=view.findViewById(R.id.cardThree);
        card4=view.findViewById(R.id.cardFour);
        card5=view.findViewById(R.id.cardFive);
        card6=view.findViewById(R.id.cardSix);
        card7=view.findViewById(R.id.cardSeven);
        card8=view.findViewById(R.id.cardEight);
        card9=view.findViewById(R.id.cardNine);
        card10=view.findViewById(R.id.cardTen);
        eTitle1=view.findViewById(R.id.text1Event);
        eTitle2=view.findViewById(R.id.text2Event);
        eTitle3=view.findViewById(R.id.text3Event);
        eTitle4=view.findViewById(R.id.text4Event);
        eTitle5=view.findViewById(R.id.text5Event);
        eTitle6=view.findViewById(R.id.text6Event);
        eTitle7=view.findViewById(R.id.text7Event);
        eTitle8=view.findViewById(R.id.text8Event);
        eTitle9=view.findViewById(R.id.text9Event);
        eTitle10=view.findViewById(R.id.text10Event);
        eOrg1=view.findViewById(R.id.text1Org);
        eOrg2=view.findViewById(R.id.text2Org);
        eOrg3=view.findViewById(R.id.text3Org);
        eOrg4=view.findViewById(R.id.text4Org);
        eOrg5=view.findViewById(R.id.text5Org);
        eOrg6=view.findViewById(R.id.text6Org);
        eOrg7=view.findViewById(R.id.text7Org);
        eOrg8=view.findViewById(R.id.text8Org);
        eOrg9=view.findViewById(R.id.text9Org);
        eOrg10=view.findViewById(R.id.text10Org);
        date1=view.findViewById(R.id.dateOne);
        date2=view.findViewById(R.id.dateTwo);
        date3=view.findViewById(R.id.dateThree);
        date4=view.findViewById(R.id.dateFour);
        date5=view.findViewById(R.id.dateFive);
        date6=view.findViewById(R.id.dateSix);
        date7=view.findViewById(R.id.dateSeven);
        date8=view.findViewById(R.id.dateEight);
        date9=view.findViewById(R.id.dateNine);
        date10=view.findViewById(R.id.dateTen);
        time1=view.findViewById(R.id.timeOne);
        time2=view.findViewById(R.id.timeTwo);
        time3=view.findViewById(R.id.timeThree);
        time4=view.findViewById(R.id.timeFour);
        time5=view.findViewById(R.id.timeFive);
        time6=view.findViewById(R.id.timeSix);
        time7=view.findViewById(R.id.timeSeven);
        time8=view.findViewById(R.id.timeEight);
        time9=view.findViewById(R.id.timeNine);
        time10=view.findViewById(R.id.timeTen);
        timeAll1=view.findViewById(R.id.timeAllotedOne);
        timeAll2=view.findViewById(R.id.timeAllotedTwo);
        timeAll3=view.findViewById(R.id.timeAllotedThree);
        timeAll4=view.findViewById(R.id.timeAllotedFour);
        timeAll5=view.findViewById(R.id.timeAllotedFive);
        timeAll6=view.findViewById(R.id.timeAllotedSix);
        timeAll7=view.findViewById(R.id.timeAllotedSeven);
        timeAll8=view.findViewById(R.id.timeAllotedEight);
        timeAll9=view.findViewById(R.id.timeAllotedNine);
        timeAll10=view.findViewById(R.id.timeAllotedTen);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userId=currentUser.getUid();
        userDetails();

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizSelector("1");
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizSelector("2");
            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizSelector("3");
            }
        });
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizSelector("4");
            }
        });
        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizSelector("5");
            }
        });
        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizSelector("6");
            }
        });
        card7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizSelector("7");
            }
        });
        card8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizSelector("8");
            }
        });
        card9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizSelector("9");
            }
        });
        card10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizSelector("10");
            }
        });

        swipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeContainer);
        // Adding Listener
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Reload current fragment
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_homeFragment_self);
                // To keep animation for 4 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeLayout.setRefreshing(false);
                    }
                }, 4000); // Delay in millis
            }
        });

        // Scheme colors for animation
        swipeLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );

        DocumentReference timerRef=fStore.collection("Quiz Id").document("quizidis69");
        timerRef.addSnapshotListener(this.requireActivity(), (EventListener<DocumentSnapshot>) (documentSnapshot, e) -> {
            for (int i=1;i<=10;i++){
                assert documentSnapshot != null;
                quizIdCheck=documentSnapshot.getString(String.valueOf(i));
                assert quizIdCheck != null;
                if (!(quizIdCheck.equals(""))){
                    headingQuiz.setText(R.string.quizList);
                    switch (i){
                        case 1:
                            card1.setVisibility(View.VISIBLE);
                            detailDis(eTitle1,eOrg1,i,date1,time1,timeAll1);
                            break;
                        case 2:
                            card2.setVisibility(View.VISIBLE);
                            detailDis(eTitle2,eOrg2,i,date2,time2,timeAll2);
                            break;
                        case 3:
                            card3.setVisibility(View.VISIBLE);
                            detailDis(eTitle3,eOrg3,i,date3,time3,timeAll3);
                            break;
                        case 4:
                            card4.setVisibility(View.VISIBLE);
                            detailDis(eTitle4,eOrg4,i,date4,time4,timeAll4);
                            break;
                        case 5:
                            card5.setVisibility(View.VISIBLE);
                            detailDis(eTitle5,eOrg5,i,date5,time5,timeAll5);
                            break;
                        case 6:
                            card6.setVisibility(View.VISIBLE);
                            detailDis(eTitle6,eOrg6,i,date6,time6,timeAll6);
                            break;
                        case 7:
                            card7.setVisibility(View.VISIBLE);
                            detailDis(eTitle7,eOrg7,i,date7,time7,timeAll7);
                            break;
                        case 8:
                            card8.setVisibility(View.VISIBLE);
                            detailDis(eTitle8,eOrg8,i,date8,time8,timeAll8);
                            break;
                        case 9:
                            card9.setVisibility(View.VISIBLE);
                            detailDis(eTitle9,eOrg9,i,date9,time9,timeAll9);
                            break;
                        case 10:
                            card10.setVisibility(View.VISIBLE);
                            detailDis(eTitle10,eOrg10,i,date10,time10,timeAll10);
                            break;
                    }
                }
            }
        });
    }

    private void detailDis(TextView viewTit, TextView viewOrg,int cardValue,TextView dateDis,TextView timeShow,TextView timeAllotted){
        DocumentReference documentReference=fStore.collection("Quiz Id").document("quizidis69");
        documentReference.addSnapshotListener(this.requireActivity(), (EventListener<DocumentSnapshot>) (documentSnapshot, e) -> {
            assert documentSnapshot != null;
            viewTit.setText(documentSnapshot.getString(cardValue+"event"));
            viewOrg.setText(documentSnapshot.getString(cardValue+"org"));
            timeShow.setText(documentSnapshot.getString(cardValue+"time"));
            dateDis.setText(documentSnapshot.getString(cardValue+"date"));
            timeAllotted.setText(String.format("Allotted time: %s%s", documentSnapshot.getString(cardValue + "tim"),"minutes"));
        });
    }

    private void userDetails(){
        DocumentReference documentReference=fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this.requireActivity(), (EventListener<DocumentSnapshot>) (documentSnapshot, e) -> {
            assert documentSnapshot != null;
            emailDis.setText(documentSnapshot.getString("email"));
            name.setText(documentSnapshot.getString("mName"));
            rollNum.setText(documentSnapshot.getString("rollno"));
            phoneNum.setText(documentSnapshot.getString("pNumber"));
            userProfile = name.getText() + "|" + rollNum.getText() + "|" + emailDis.getText() + "|" + phoneNum.getText();
//            Log.d(TAG, "userDetails: "+userProfile);
        });
    }

    private void quizSelector(String quizId){
        DocumentReference quizDataReference=fStore.collection("metadata").document(userId);
        Map<String,Object> data=new HashMap<>();
        data.put("ID",quizId);
        quizDataReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                quizStarter(quizId);
//                Log.d(TAG, "onSuccess: "+quizId);
            }
        });
    }

    private void quizStarter(String essenID){
        DocumentReference timer_IdRef=fStore.collection("Quiz Id").document("quizidis69");
        timer_IdRef.addSnapshotListener(this.requireActivity(), (documentSnapshot, e) -> {
            assert documentSnapshot != null;
            quizIdQues=documentSnapshot.getString(essenID);
            quizPass=documentSnapshot.getString(essenID+"pass");
            showDialog();
//            Log.d(TAG, "quizStarter: "+quizIdQues);
        });
    }

    public void showDialog() {
        LayoutInflater li = LayoutInflater.from(HomeFragment.this.getActivity());
        View promptsView = li.inflate(R.layout.fragment_pass, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.requireActivity());
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.passForAuth);
        // set dialog message
        alertDialogBuilder.setCancelable(false).setNegativeButton("Enter",
                new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String user_text = (userInput.getText()).toString();
                                if (user_text.equals(quizPass)) {
                                    attemptORNOT();
                                } else{
                                    String message = "The password you have entered is incorrect." + " \n \n" + "Please try again!";
                                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeFragment.this.getActivity());
                                    builder.setTitle("Error");
                                    builder.setMessage(message);
                                    builder.setPositiveButton("Cancel", null);
                                    builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            showDialog();
                                        }
                                    });
                                    builder.create().show();
                                }
                            }
                        }).setPositiveButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.dismiss();
                            }
                        }
                );
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void attemptORNOT(){
        DocumentReference quizGetForAttempt=fStore.collection("metadata").document(quizIdQues).collection(userProfile).document("attempt or not");
        quizGetForAttempt.addSnapshotListener(this.requireActivity(), (documentSnapshot, e) -> {
            assert documentSnapshot != null;
            atpdVAl=documentSnapshot.getString("Value");
            new CountDownTimer(500, 1000) {
                public void onTick(long millisUntilFinished) {
                    Log.d(TAG, "onTick: "+millisUntilFinished);
                }
                public void onFinish() {
                    if (!Objects.equals(atpdVAl, "atpd")){
//                Log.d(TAG, "attemptORNOT: Atpd"+atpdVAl);
                        NavHostFragment.findNavController(HomeFragment.this)
                                .navigate(R.id.action_homeFragment_to_quizFragment);
                    }else {
//                Log.d(TAG, "Else attemptORNOT: Atpd"+atpdVAl);
                        AlertDialog attemptLimitAlert = new AlertDialog.Builder(HomeFragment.this.getActivity())
                                .setMessage(R.string.limitAlert).setTitle(R.string.alert).show();
                    }
                }
            }.start();

        });
    }

    //    @Override
//    public void onClick(View v) {
//        Log.d(TAG, "onClick: clicked");
//        switch (view.getId()){
//            case R.id.cardOne:
//                quizSelector("1");
////                quizStarter();
//                break;
//            case R.id.cardTwo:
//                quizSelector("2");
////                quizStarter();
//                break;
//            case R.id.cardThree:
//                quizSelector("3");
////                quizStarter();
//                break;
//            case R.id.cardFour:
//                quizSelector("4");
////                quizStarter();
//                break;
//            case R.id.cardFive:
//                quizSelector("5");
////                quizStarter();
//                break;
//            case R.id.cardSix:
//                quizSelector("6");
////                quizStarter();
//                break;
//            case R.id.cardSeven:
//                quizSelector("7");
////                quizStarter();
//                break;
//            case R.id.cardEight:
//                quizSelector("8");
////                quizStarter();
//                break;
//            case R.id.cardNine:
//                quizSelector("9");
////                quizStarter();
//                break;
//            case R.id.cardTen:
//                quizSelector("10");
////                quizStarter();
//                break;
//        }
//    }
}