package pnj.uas.muhammad_ridwan_ramadhan.elitedormitorio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import pnj.uas.muhammad_ridwan_ramadhan.elitedormitorio.model.User;

public class Register extends AppCompatActivity {
    EditText name, email, password, repassword, phnumber;
    Button signup;

    FirebaseDatabase database;
    DatabaseReference ref;
    User user;
    private FirebaseAuth mAuth;
    String fullnamePattern = "^[\\p{L} .'-]+$";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String phnumberPattern = "^\\d{10,13}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.inputName);
        email = findViewById(R.id.inputEmail);
        password = findViewById(R.id.inputPassword);
        repassword = findViewById(R.id.inputRepass);
        phnumber = findViewById(R.id.inputPhnum);
        signup = findViewById(R.id.signupBtn);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Users");
        user = new User();
        mAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().equals("")||email.getText().toString().equals("")|| password.getText().toString().equals("")||repassword.getText().toString().equals("")
                        || phnumber.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Don't leave a field in blank.", Toast.LENGTH_LONG).show();
                }else{
                    if(name.getText().toString().matches(fullnamePattern)){
                        if(email.getText().toString().matches(emailPattern)) {
                            if(repassword.getText().toString().equals(password.getText().toString())){
                                if(password.getText().toString().length() > 6 && repassword.getText().toString().length()>6){
                                    if(phnumber.getText().toString().matches(phnumberPattern)){
                                        registerNewUser();
                                    }else {
                                        Toast.makeText(getApplicationContext(), "Invalid phone number.", Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(), "Password must more 6 digits.", Toast.LENGTH_LONG).show();
                                }
                            }else {
                                Toast.makeText(getApplicationContext(), "Between password and password confirmation are not same.", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Email format not valid.", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Can't input number in full name field.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void getValues(){
        user.setName(name.getText().toString());
        user.setDescription("null");
        user.setImageURL("null");
        user.setInstagram("null");
        user.setPhoneNumber(phnumber.getText().toString());
    }

    private void registerNewUser(){
        String emailAuth , passwordAuth ;
        emailAuth = email.getText().toString();
        passwordAuth = password.getText().toString();

        mAuth.createUserWithEmailAndPassword(emailAuth, passwordAuth).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Unable to create user, "+ e,
                        Toast.LENGTH_LONG).show();
            }}).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, String> userData = new HashMap<>();

                            userData.put("Description", "null");
                            userData.put("ImageURL", "null");
                            userData.put("Instagram", "null");
                            userData.put("Name", name.getText().toString());
                            userData.put("phoneNumber", phnumber.getText().toString());

                            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                            String postId = currentFirebaseUser.getUid();
                            ref.child(postId+"/Profile").setValue(userData);
                            Toast.makeText(Register.this,"Success register.",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Register.this, Login.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(Register.this,databaseError.getMessage().toString(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}
