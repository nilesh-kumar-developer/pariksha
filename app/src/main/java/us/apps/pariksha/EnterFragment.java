package us.apps.pariksha;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class EnterFragment extends Fragment {
    View view;
//    private static final String TAG = "EnterFragment";
    private ImageButton enterBtn,reportBtn,signOutBtn;
    private TextView enterBtnTxt,reportBtnTxt,signOutBtnTxt,noticeTExt;
    private FirebaseAuth mAuth;
    private FirebaseFirestore EStore;
    private int counter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_enter, container, false);

        mAuth = FirebaseAuth.getInstance();
        enterBtn=(ImageButton)view.findViewById(R.id.enterButton);
        reportBtn=(ImageButton) view.findViewById(R.id.reportBtn);
        signOutBtn=(ImageButton) view.findViewById(R.id.signOut);
        enterBtnTxt=(TextView) view.findViewById(R.id.enterButtonTxt);
        reportBtnTxt=(TextView) view.findViewById(R.id.reportBtnTxt);
        signOutBtnTxt=(TextView) view.findViewById(R.id.signOutTxt);
        noticeTExt=(TextView)view.findViewById(R.id.noticeTXt);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseUser currentUser = mAuth.getCurrentUser();

//        DocumentReference noticeREf=EStore.collection("Quiz Id").document("Notice");
//        noticeREf.addSnapshotListener(this.requireActivity(), (documentSnapshot, e) -> {
//            assert documentSnapshot != null;
////            for (int counter=1;counter<=5;counter++){
////                int finalCounter = counter;
////                new CountDownTimer(30000, 1000) {
////                    public void onTick(long millisUntilFinished) {
////                        Log.d(TAG, "onViewCreated: "+finalCounter);
////                        noticeTExt.setText(documentSnapshot.getString(String.valueOf(finalCounter)));
//            noticeTExt.setText(documentSnapshot.getString(String.valueOf(1)));
////                    }
////                    public void onFinish() {
////                        Log.d(TAG, "onViewCreated: "+finalCounter);
////                    }
////                }.start();
////            }
//        });
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(EnterFragment.this)
                        .navigate(R.id.action_enterFragment_to_reportFragment);
            }
        });

        if (currentUser!=null){
            signOutBtnTxt.setVisibility(View.VISIBLE);
            signOutBtn.setVisibility(View.VISIBLE);
        }
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        if (currentUser==null){
            enterBtnTxt.setText(R.string.enterFragBtnTxt);
        }else {
            reportBtnTxt.setVisibility(View.VISIBLE);
            reportBtn.setVisibility(View.VISIBLE);
        }

        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser==null){
                    NavHostFragment.findNavController(EnterFragment.this)
                            .navigate(R.id.action_enterFragment_to_loginFragment);
                }
                else {
                    if (currentUser.isEmailVerified()){
                        NavHostFragment.findNavController(EnterFragment.this)
                                .navigate(R.id.action_enterFragment_to_homeFragment);
                    }
                    else {
                        NavHostFragment.findNavController(EnterFragment.this)
                                .navigate(R.id.action_enterFragment_to_loginFragment);
                    }
                }
            }
        });
    }

    private void logout(){
        AlertDialog alertBox = new AlertDialog.Builder(this.getActivity())
                .setMessage(R.string.logoutText)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        NavHostFragment.findNavController(EnterFragment.this)
                                .navigate(R.id.action_enterFragment_to_loginFragment);
                        mAuth.signOut();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
    }
}

//    // Reference to an image file in Cloud Storage
//    StorageReference storageReference = = FirebaseStorage.getInstance().getReference().child("myimage");
//
//
//        ImageView image = (ImageView)findViewById(R.id.imageView);
//
//// Load the image using Glide
//        Glide.with(this /* context */)
//        .using(new FirebaseImageLoader())
//        .load(storageReference)
//        .into(image );
