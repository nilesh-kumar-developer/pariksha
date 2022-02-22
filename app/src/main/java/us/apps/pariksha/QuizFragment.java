package us.apps.pariksha;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;
import static java.lang.String.valueOf;

public class QuizFragment extends Fragment implements View.OnClickListener {
    View view;
//    private static final String TAG = "QuizFragment";
    private FirebaseAuth mAuth;
    private FirebaseFirestore QStore;

    private TextView question,timerDis,ansDis,eventName, eventOrganiser, eventDescription,qNum;
    private ImageView questionImg;
    private CardView nextBtn,prevBtn,ansCardDis;
//    private CardView confirmBtn;
    private Button startBtn,submitBtn;
    private RadioGroup radioGroupOpt;
    private RadioButton optA, optB,optC, optD;
    private ExtendedFloatingActionButton quitBtn,backToHome;
    private int quesIncr=0, quesValue=-1;
    private String idQuiz;
    private String quizIdQues;
    private String timerTime;
    private String userProfile;
    private String userName;
    private String userPhNum;
    private String userEmail;
    private String userRollNum;
    private String questionCount;
    private final String selectedAns="Selected option: ";
    private String optionValue;
    private String eventNameID;
    private final String[] answerGetter=new String[51];
    private final String[] array = new String[51];
    private int score=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_quiz, container, false);

        mAuth = FirebaseAuth.getInstance();
        QStore=FirebaseFirestore.getInstance();

        question= view.findViewById(R.id.questionView);
        questionImg=view.findViewById(R.id.questionViewImg);
        backToHome=view.findViewById(R.id.backToHomeFrag);
        qNum=view.findViewById(R.id.QSign);
        optA= view.findViewById(R.id.optionA);
        optB= view.findViewById(R.id.optionB);
        optC= view.findViewById(R.id.optionC);
        optD= view.findViewById(R.id.optionD);
        quitBtn= view.findViewById(R.id.quitQuiz);
        nextBtn= view.findViewById(R.id.nextButton);
        radioGroupOpt= view.findViewById(R.id.radioGroupOpt);
        timerDis= view.findViewById(R.id.timeDis);
        ansDis= view.findViewById(R.id.answerDisplay);
        prevBtn= view.findViewById(R.id.prevButton);
        startBtn= view.findViewById(R.id.startButton);
        submitBtn= view.findViewById(R.id.submitBtn);
        ansCardDis= view.findViewById(R.id.answerDisCard);
        eventOrganiser = view.findViewById(R.id.organizationName);
        eventDescription= view.findViewById(R.id.eventDescription);
        eventName= view.findViewById(R.id.eventName);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        DocumentReference quizDataReference=QStore.collection("metadata").document(userId);
        quizDataReference.addSnapshotListener(this.requireActivity(), (documentSnapshot, e) -> {
            assert documentSnapshot != null;
            idQuiz=documentSnapshot.getString("ID");
        });

        startBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        quitBtn.setOnClickListener(this);
        prevBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        optA.setOnClickListener(this);
        optB.setOnClickListener(this);
        optC.setOnClickListener(this);
        optD.setOnClickListener(this);
        backToHome.setOnClickListener(this);

        DocumentReference profileRef=QStore.collection("users").document(userId);
        profileRef.addSnapshotListener(this.requireActivity(), (documentSnapshot, e) -> {
            assert documentSnapshot != null;
            userEmail=documentSnapshot.getString("email");
            userName=documentSnapshot.getString("mName");
            userRollNum=documentSnapshot.getString("rollno");
            userPhNum=documentSnapshot.getString("pNumber");
            userProfile = userName + "|" + userRollNum + "|" + userEmail + "|" + userPhNum;
        });

        quizGetter();
    }

    private void update(int ques) {
        ansDis.setText(selectedAns);
        DocumentReference documentReference = QStore.collection("Questions").document(quizIdQues);
        documentReference.addSnapshotListener(this.requireActivity(), (documentSnapshot, e) -> {
            qNum.setText("Q"+ques+". ");
            assert documentSnapshot != null;
            String questionVAl =documentSnapshot.getString(String.valueOf(ques));
            assert questionVAl != null;
            if(questionVAl.contains("https://")){
                Glide.with(this)
                        .load(questionVAl)
                        .into(questionImg);
                question.setText("");
//                Log.d(TAG, "update: Glide is working");
            }else {
                question.setText(questionVAl);
                questionImg.setImageResource(0);
//                Log.d(TAG, "update: Ques is working");
            }
            optA.setText(documentSnapshot.getString(ques + "a"));
            optB.setText(documentSnapshot.getString(ques + "b"));
            optC.setText(documentSnapshot.getString(ques + "c"));
            optD.setText(documentSnapshot.getString(ques + "d"));
        });
        int selectedId = radioGroupOpt.getCheckedRadioButtonId();
        RadioButton radioButtonE = view.findViewById(selectedId);
//        Log.d(TAG, "update: Incr"+ quesIncr+"| ques"+ques);
//        String oA,oB,oC,oD,sid;
//        oA=String.valueOf(optA.getText());
//        oB=String.valueOf(optB.getText());
//        oC=String.valueOf(optC.getText());
//        oD=String.valueOf(optD.getText());
//        if ()
//        sid=String.valueOf(radioButton.getText());
        if (optA.equals(radioButtonE)) {
            array[quesValue] = "A";
            radioGroupOpt.clearCheck();
//            Log.d(TAG, "update: A | "+array[quesValue]+" | ->"+Arrays.toString(array));
//            Log.d(TAG, "update: "+quesValue);
        } else if (optB.equals(radioButtonE)) {
            array[quesValue] = "B";
            radioGroupOpt.clearCheck();
//            Log.d(TAG, "update: D | "+array[quesValue]+" | ->"+Arrays.toString(array));
//            Log.d(TAG, "update: "+quesValue);
        } else if (optC.equals(radioButtonE)) {
            array[quesValue] = "C";
            radioGroupOpt.clearCheck();
//            Log.d(TAG, "update: C | "+array[quesValue]+" | ->"+Arrays.toString(array));
//            Log.d(TAG, "update: "+quesValue);
        } else if (optD.equals(radioButtonE)) {
            array[quesValue] = "D";
            radioGroupOpt.clearCheck();
//            Log.d(TAG, "update: D | "+array[quesValue]+" | ->"+Arrays.toString(array));
//            Log.d(TAG, "update: "+quesValue);
        } else {
            Toast.makeText(QuizFragment.this.getActivity(),"No option selected for Q"+quesValue+".",Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "update: "+quesValue);
        }
    }

    private void quizGetter(){
        DocumentReference timer_IdRef=QStore.collection("Quiz Id").document("quizidis69");
        timer_IdRef.addSnapshotListener(this.requireActivity(), (documentSnapshot, e) -> {
            assert documentSnapshot != null;
            quizIdQues=documentSnapshot.getString(idQuiz);
            timerTime=documentSnapshot.getString(idQuiz+"tim");
            eventName.setText(documentSnapshot.getString(String.format("%sevent", idQuiz)));
            eventOrganiser.setText(documentSnapshot.getString(idQuiz+"org"));
            eventDescription.setText(documentSnapshot.getString(idQuiz+"descrip"));
//            Log.d(TAG, "quizGetter: "+eventDescription.getText());
        });
    }

    private void timer(){
//        Log.d(TAG, "timer: "+timerTime);
        new CountDownTimer(Long.parseLong(timerTime)*60*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerDis.setText(""+String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }
            public void onFinish() {
                timerDis.setText(R.string.timeOut);
                Snackbar snackbar = Snackbar
                        .make(view, "TIME OVER!! Your quiz is Submitted Successfully!", Snackbar.LENGTH_LONG);
                snackbar.show();
                submitInternalQuiz();
            }
        }.start();
    }

    private void startQuiz(){
        backToHome.setVisibility(View.INVISIBLE);
        DocumentReference ansRef=QStore.collection("Answers").document(quizIdQues);
        ansRef.addSnapshotListener(this.requireActivity(), (documentSnapshot, e) -> {
            assert documentSnapshot != null;
            questionCount=documentSnapshot.getString("question count");
            if (questionCount != null) {
                for (int quesNum = 1; quesNum<=Integer.parseInt(questionCount); quesNum++){
                    answerGetter[quesNum]=documentSnapshot.getString(String.valueOf(quesNum));
                }
            }
        });
        DocumentReference attemptRef=QStore.collection("metadata").document(quizIdQues).collection(userProfile).document("attempt or not");
        Map<String,Object> atREF=new HashMap<>();
        atREF.put("Value","atpd");
        attemptRef.set(atREF).addOnSuccessListener(aVoid -> {
        });

        startBtn.setVisibility(View.INVISIBLE);

        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                eventDescription.setText(MessageFormat.format("Starting in: {0}", millisUntilFinished / 1000));
            }
            public void onFinish() {
                eventDescription.setText(R.string.bol);
                eventDescription.setGravity(Gravity.CENTER);
                ansCardDis.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.VISIBLE);
                submitBtn.setVisibility(View.VISIBLE);
                quitBtn.setVisibility(View.VISIBLE);
                timer();
                nextQuestion();
            }
        }.start();
    }

    private void nextQuestion(){
        quesValue++;
        quesIncr++;
        if (quesIncr>1){
            prevBtn.setVisibility(View.VISIBLE);
        }
        if (quesIncr==Integer.parseInt(questionCount)-1){
            nextBtn.setVisibility(View.INVISIBLE);
        }
        if (quesIncr<=Integer.parseInt(questionCount)){
            update(quesIncr);
//            Log.d(TAG, "nextQuestion: "+quesIncr);
        }
//        Log.d(TAG, "nextQuestion: outer"+quesIncr);
    }

    private void prevQuestion(){
        quesIncr--;
        quesValue--;
        if (quesIncr<=1){
            prevBtn.setVisibility(View.INVISIBLE);
        }
        if (quesIncr<Integer.parseInt(questionCount)){
            nextBtn.setVisibility(View.VISIBLE);
            ansCardDis.setVisibility(View.VISIBLE);
        }
        update(quesIncr);
    }

    private void quitQuiz(){
        AlertDialog quitAlert = new AlertDialog.Builder(this.getActivity())
                .setMessage(R.string.quitQuizTxt).setTitle(R.string.quit)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        submitInternalQuiz();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
        quitAlert.setTitle(R.string.quit);
    }

    private void submitQuiz(){
        AlertDialog submitQuizAlert = new AlertDialog.Builder(this.getActivity())
                .setMessage(R.string.submitQuizTxt).setTitle(R.string.submit)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        nextQuestion();
                        Snackbar snackbar = Snackbar
                                .make(view, "Your quiz is Submitted Successfully!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        submitInternalQuiz();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
    }

    private void submitInternalQuiz(){
        DocumentReference ansRef=QStore.collection("Answers").document(quizIdQues);
        ansRef.addSnapshotListener(this.requireActivity(), (documentSnapshot, e) -> {
            assert documentSnapshot != null;
            if (questionCount != null) {
                for (int quesNum = 1; quesNum<=Integer.parseInt(questionCount); quesNum++){
                    answerGetter[quesNum]=documentSnapshot.getString(String.valueOf(quesNum));
                }
            }
        });
//        Log.d(TAG, "submitInternalQuiz: "+Arrays.toString(answerGetter)+questionCount+quizIdQues);

        for (int quesNum3=1;quesNum3<=Integer.parseInt(questionCount);quesNum3++){
            if (Objects.equals(answerGetter[quesNum3], array[quesNum3])){
                score = score + 1;
//                Log.d(TAG, "submitInternalQuiz: VAl:"+Arrays.toString(array)+" | SERVER:"+Arrays.toString(answerGetter));
            }
        }

        DocumentReference submitQuizRef=QStore.collection("result").document(quizIdQues).collection(userProfile).document("marks");
        Map<String,Object> quizRef=new HashMap<>();
        quizRef.put("Score",score-1);
//        Log.d(TAG, "submitInternalQuiz: | Score:"+score);
        quizRef.put("Answers",Arrays.toString(array));
//        Log.d(TAG, "submitInternalQuiz: Ans:"+Arrays.toString(answerGetter)+" | Input: "+Arrays.toString(array));
        submitQuizRef.set(quizRef).addOnSuccessListener(aVoid -> {
        });

        NavHostFragment.findNavController(QuizFragment.this)
                .navigate(R.id.action_quizFragment_to_homeFragment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backToHomeFrag:
                NavHostFragment.findNavController(QuizFragment.this)
                        .navigate(R.id.action_quizFragment_to_homeFragment);
                break;
            case R.id.startButton:
                AlertDialog startQuizAlert = new AlertDialog.Builder(this.getActivity())
                        .setMessage(R.string.startQuizState).setTitle(R.string.startQuiz)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                startQuiz();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        })
                        .show();
                break;
            case R.id.nextButton:
                nextQuestion();
                break;
            case R.id.quitQuiz:
                quitQuiz();
                break;
            case R.id.prevButton:
                prevQuestion();
                break;
            case R.id.submitBtn:
                submitQuiz();
                break;
            case R.id.optionA:
                ansDis.setText(format("%s%s", selectedAns, optA.getText()));
                break;
            case R.id.optionB:
                ansDis.setText(format("%s%s", selectedAns, optB.getText()));
                break;
            case R.id.optionC:
                ansDis.setText(format("%s%s", selectedAns, optC.getText()));
                break;
            case R.id.optionD:
                ansDis.setText(format("%s%s", selectedAns, optD.getText()));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
    }
}
