package us.apps.pariksha;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReportFragment extends Fragment {
//    private final static String TAG="ReportFragment";

    View view;
    private FirebaseAuth rAuth;
    private FirebaseFirestore rStore;

    private Button sendRepoBtn;
    private EditText reportDes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_report, container, false);

        rAuth = FirebaseAuth.getInstance();
        rStore= FirebaseFirestore.getInstance();

        sendRepoBtn=(Button)view.findViewById(R.id.sendBugRep);
        reportDes=(EditText) view.findViewById(R.id.bugEditTxt);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser currentUser = rAuth.getCurrentUser();
        Date currentTime = Calendar.getInstance().getTime();
        String userId= currentUser.getUid();

        sendRepoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataToSend=String.valueOf(reportDes.getText());
//                Log.d(TAG, "onClick: "+dataToSend);
                DocumentReference submitBugReport=rStore.collection("BugReport").document(currentTime+"\n"+userId);
                Map<String,Object> bugRef=new HashMap<>();
                bugRef.put("Details",dataToSend);
                submitBugReport.set(bugRef).addOnSuccessListener(aVoid -> {
                    Snackbar snackbar = Snackbar
                            .make(view, "Report have been sent! thanks for stepping out to report a bug.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                });
            }
        });
    }
}