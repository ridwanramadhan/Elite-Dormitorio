package pnj.uas.muhammad_ridwan_ramadhan.elitedormitorio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import pnj.uas.muhammad_ridwan_ramadhan.elitedormitorio.model.User;

public class ProfileUser extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private Query residenceRef;
    private String userID;
    private Uri imguri;
    private StorageTask uploadTask;
    StorageReference mStorageReference;
    FirebaseDatabase database;
    Uri downloadUri;

    User users;

    ImageView imgUserEd,imgUserEd2;
    TextView tv_name, tv_address2, nameImage;
    EditText edName, edInstagram, edPhone, edDesc;
    Button uploadBtn, updateproBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        imgUserEd = findViewById(R.id.imgUserEd);
        tv_name = findViewById(R.id.tv_name);
        tv_address2 = findViewById(R.id.tv_address2);
        edName = findViewById(R.id.edName);
        edInstagram = findViewById(R.id.edInstagram);
        edDesc = findViewById(R.id.edDesc);
        edPhone = findViewById(R.id.edPhone);
        uploadBtn = findViewById(R.id.uploadBtn);
        imgUserEd2 = findViewById(R.id.imgUserEd2);
        nameImage = findViewById(R.id.nameImg);
        updateproBtn = findViewById(R.id.updateprBtn);

        mStorageReference = FirebaseStorage.getInstance().getReference("profile_img");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        myRef = mFirebaseDatabase.getReference("Users");
        residenceRef = mFirebaseDatabase.getReference("Users").child(userID).child("kosanList");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        final FirebaseUser user2 = mAuth.getCurrentUser();
        userID = user2.getUid();
        myRef = mFirebaseDatabase.getReference("Users");
        users = new User();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String N = snapshot.child(userID).child("Profile").child("Name").getValue(String.class);
                String D = snapshot.child(userID).child("Profile").child("Description").getValue(String.class);
                String I = snapshot.child(userID).child("Profile").child("ImageURL").getValue(String.class);
                String Ig = snapshot.child(userID).child("Profile").child("Instagram").getValue(String.class);
                String P = snapshot.child(userID).child("Profile").child("phoneNumber").getValue(String.class);
                tv_name.setText(N);
                if(D.equals("null")){
                    tv_address2.setText("No Description User.");
                }else{
                    tv_address2.setText(D);
                }
                Picasso.get().load(I).into(imgUserEd);
                edName.setText(N);
                edInstagram.setText(Ig);
                edPhone.setText(P);
                edDesc.setText(D);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });

        updateproBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask != null && uploadTask.isInProgress()) {

                    Toast.makeText(getApplicationContext(),
                            "UPLOAD IN PROGRESS",
                            Toast.LENGTH_LONG).show();
                }else {
                    dataprUpdate();
                }
            }
        });
    }

    private void FileChooser() {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void getValues(){
        users.setName(edName.getText().toString());
        users.setDescription(edDesc.getText().toString());
        users.setImageURL(downloadUri.toString());
        users.setInstagram(edInstagram.getText().toString());
        users.setPhoneNumber(edPhone.getText().toString());
    }

    private void dataprUpdate() {
        final StorageReference ref = mStorageReference.child(System.currentTimeMillis()+"."+ getExtension(imguri));

        uploadTask = ref.putFile(imguri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUri = task.getResult();
                    dataprUpdate2();

                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    private void dataprUpdate2() {
        Map<String, String> userData = new HashMap<>();

        userData.put("Description", edDesc.getText().toString());
        userData.put("ImageURL", downloadUri.toString());
        userData.put("Instagram", edInstagram.getText().toString());
        userData.put("Name", edName.getText().toString());
        userData.put("phoneNumber", edPhone.getText().toString());

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String postId = currentFirebaseUser.getUid();
        myRef.child(postId+"/Profile").setValue(userData);
        Toast.makeText(ProfileUser.this,"Success update.",Toast.LENGTH_LONG).show();
        finish();
        Intent intent = new Intent(ProfileUser.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK && data !=null && data.getData()!=null) {
            imguri = data.getData();
            imgUserEd2.setImageURI(imguri);
            nameImage.setText("picture was added.");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
