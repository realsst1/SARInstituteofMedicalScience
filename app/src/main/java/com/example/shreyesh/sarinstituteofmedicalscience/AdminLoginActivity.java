package com.example.shreyesh.sarinstituteofmedicalscience;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLoginActivity extends AppCompatActivity {

    private Toolbar adminToolbar;
    private TextInputLayout adminPass, adminEmail;
    private Button adminLogin;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private RadioButton doctor, consultant, admin;
    private RadioGroup sarims;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        //Initialize views
        adminToolbar = (Toolbar) findViewById(R.id.AdminToolbar);
        adminEmail = (TextInputLayout) findViewById(R.id.adminEmail);
        adminPass = (TextInputLayout) findViewById(R.id.adminPassword);
        adminLogin = (Button) findViewById(R.id.signInAdmin);
        admin = (RadioButton) findViewById(R.id.adminLogin);
        doctor = (RadioButton) findViewById(R.id.doctorLogin);
        consultant = (RadioButton) findViewById(R.id.consultantLogin);
        progressDialog = new ProgressDialog(this);

        sarims = (RadioGroup) findViewById(R.id.sarimsStaffType);
        progressDialog.setTitle("Logging In");
        progressDialog.setMessage("Please while we verify your credentials");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();

        //Set Toolbar
        setSupportActionBar(adminToolbar);
        getSupportActionBar().setTitle("Admin Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Login Button action
        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get Information from the input box in string format
                String email = adminEmail.getEditText().getText().toString();
                String password = adminPass.getEditText().getText().toString();


                RadioButton rb = (RadioButton) sarims.findViewById(sarims.getCheckedRadioButtonId());
                final String type = rb.getText().toString();
                //Check for validity of email,password
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(AdminLoginActivity.this, "Please fill details", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(AdminLoginActivity.this, "Invalid Email", Toast.LENGTH_LONG).show();
                    return;
                }
                if (type.equals("Admin")) {
                    if (!email.equals("sarimsadmin2018@gmail.com") || !password.equals("sarims2018")) {
                        Toast.makeText(AdminLoginActivity.this, "Incorrect Credentials", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if (!admin.isChecked() && !doctor.isChecked() && !consultant.isChecked()) {
                    Toast.makeText(AdminLoginActivity.this, "Please select Admin or Doctor or Consultant", Toast.LENGTH_SHORT).show();
                    return;
                }


                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            switch (type) {
                                case "Admin":
                                    startActivity(new Intent(AdminLoginActivity.this, AdminHomeActivity.class));
                                    finish();
                                    break;
                                case "Doctors":
                                    break;
                                case "Consultant":
                                    break;
                            }

                        } else {
                            progressDialog.hide();
                            Toast.makeText(AdminLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        });

    }
}
