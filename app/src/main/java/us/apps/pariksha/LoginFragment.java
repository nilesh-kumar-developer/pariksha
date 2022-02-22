package us.apps.pariksha;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginFragment extends Fragment {
    View view;
//    private static final String TAG = "LoginFragment";
    private ExtendedFloatingActionButton registerPageBtn, signInBtn;
    private EditText emailEditTextS,passEditTextS;
    private FirebaseAuth mAuth;
    private TextView forgotPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();
        registerPageBtn=(ExtendedFloatingActionButton)view.findViewById(R.id.registerPageButton);
        signInBtn=(ExtendedFloatingActionButton)view.findViewById(R.id.signInButton);
        emailEditTextS=(EditText)view.findViewById(R.id.editTextEmailS);
        passEditTextS=(EditText)view.findViewById(R.id.editTextPasswordS);
        forgotPassword=(TextView)view.findViewById(R.id.forgotPass);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInProfile();
            }
        });

        registerPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_loginFragment_to_signUpFragment);
            }
        });
    }
    public void showDialog() {
        LayoutInflater li = LayoutInflater.from(LoginFragment.this.getActivity());
        View promptsView = li.inflate(R.layout.fragment_reset, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginFragment.this.getActivity());
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.emailEntry);
        // set dialog message
        alertDialogBuilder.setCancelable(false).setNegativeButton("Send",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        String user_text = (userInput.getText()).toString();
                        if (user_text.contains(".com")) {
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            auth.sendPasswordResetEmail(user_text)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // do something when mail was sent successfully.
                                                Snackbar snackbar = Snackbar
                                                        .make(view, "Check your E-mail inbox, we've sent you an email.", Snackbar.LENGTH_LONG);
                                                snackbar.show();
                                            }
                                        }
                                    });
                        } else{
                            String message = "Enter an valid E-mail address." + " \n \n" + "Please try again!";
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginFragment.this.getActivity());
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

    private void updateUI(FirebaseUser getUser){
        if (getUser==null){
            Toast.makeText(this.getActivity(), "E-mail ID / Password not Correct Or E-mail ID not registered yet",
                    Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this.getActivity(), "Signing in...!", Toast.LENGTH_LONG).show();
            if (!getUser.isEmailVerified()){
                Toast.makeText(this.getActivity(), "Verify your E-mail Id, check inbox.", Toast.LENGTH_LONG).show();
            }
            NavHostFragment.findNavController(LoginFragment.this)
                    .navigate(R.id.action_loginFragment_to_verifyFragment);
        }
    }

    private void signInProfile(){
        String email, password;
        email = emailEditTextS.getText().toString();
        password = passEditTextS.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(requireContext().getApplicationContext(), "Please enter email!!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(requireContext().getApplicationContext(), "Please enter password!!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this.requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            updateUI(null);
                        }
                    }
                });
    }
}