package pnj.uas.muhammad_ridwan_ramadhan.elitedormitorio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class UpdateAdd extends AppCompatActivity {
    private EditText inputResidenceNameu, inputAvailableu,inputCostu,inputLongitudeu,inputLatitudeu,inputfullAddressu, residenceDescu;
    private Spinner inputCategoryu;
    private CheckBox AirConditioneru, Wifiu, freelaundryu, freeelectricityu, privatebathroomu;
    private Button uploadaddBtnu, updateaddBtnu;
    private TextView tvPlaceAPILatu,tvPlaceAPILongu,tvPlaceAPIAddu, statusaddUploadu;
    private ImageView imgaddResidenceu, imgKosanu;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef, myRef2;
    private Query residenceRef;
    private String userID, kosanID;
    private Uri imguri;
    private StorageTask uploadTask;
    StorageReference mStorageReference;
    FirebaseDatabase database;
    Uri downloadUri;
    String a,ac,b,c,co,e,lat,lau,lo,rd,riu,rn,ra,s,st,ui,w;

    private static final int PICK_UP = 0;
    private int REQUEST_CODE = 0;
    private static  final int IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_add);

        kosanID = getIntent().getExtras().getString("kosanID");

        statusaddUploadu = findViewById(R.id.statusaddUploadu);
        updateaddBtnu = findViewById(R.id.updateaddBtnu);
        uploadaddBtnu = findViewById(R.id.uploadaddBtnu);
        imgaddResidenceu = findViewById(R.id.imgaddResidenceu);
        inputfullAddressu = findViewById(R.id.inputfullAddressu);
        inputResidenceNameu = findViewById(R.id.inputResidenceNameu);
        inputAvailableu = findViewById(R.id.inputAvailableu);
        inputCostu = findViewById(R.id.inputCostu);
        inputCategoryu = findViewById(R.id.inputCatagoryu);
        AirConditioneru = findViewById(R.id.AirConditioneru);
        Wifiu = findViewById(R.id.Wifiu);
        freelaundryu = findViewById(R.id.freelaundryu);
        freeelectricityu = findViewById(R.id.freeelectricityu);
        privatebathroomu = findViewById(R.id.privatebathroomu);
        residenceDescu = findViewById(R.id.residenceDescu);
        imgKosanu = findViewById(R.id.imgKosanu);
        mStorageReference = FirebaseStorage.getInstance().getReference("profile_img");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        myRef = mFirebaseDatabase.getReference("kosan");
        myRef2 = mFirebaseDatabase.getReference("Users");
        residenceRef = mFirebaseDatabase.getReference("Users");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        final FirebaseUser user2 = mAuth.getCurrentUser();
        userID = user2.getUid();

        inputfullAddressu.setEnabled(true);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                }
            }
        };

        residenceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                a = snapshot.child(userID).child("kosanList").child(kosanID).child("address").getValue(String.class);
                ac = snapshot.child(userID).child("kosanList").child(kosanID).child("airConditioner").getValue(String.class);
                b = snapshot.child(userID).child("kosanList").child(kosanID).child("bathRoom").getValue(String.class);
                c = snapshot.child(userID).child("kosanList").child(kosanID).child("category").getValue(String.class);
                co = snapshot.child(userID).child("kosanList").child(kosanID).child("cost").getValue(String.class);
                e = snapshot.child(userID).child("kosanList").child(kosanID).child("electricity").getValue(String.class);;
                lau = snapshot.child(userID).child("kosanList").child(kosanID).child("laundry").getValue(String.class);
                rd = snapshot.child(userID).child("kosanList").child(kosanID).child("residenceDesc").getValue(String.class);
                riu = snapshot.child(userID).child("kosanList").child(kosanID).child("residenceImageURL").getValue(String.class);
                rn = snapshot.child(userID).child("kosanList").child(kosanID).child("residenceName").getValue(String.class);
                ra = snapshot.child(userID).child("kosanList").child(kosanID).child("roomAvailable").getValue(String.class);
                s = snapshot.child(userID).child("kosanList").child(kosanID).child("seen").getValue(String.class);
                st = snapshot.child(userID).child("kosanList").child(kosanID).child("statusResidence").getValue(String.class);
                ui = snapshot.child(userID).child("kosanList").child(kosanID).child("userID").getValue(String.class);
                w = snapshot.child(userID).child("kosanList").child(kosanID).child("wifi").getValue(String.class);
                inputfullAddressu.setText(a);
                Picasso.get().load(riu).into(imgKosanu);;
                inputResidenceNameu.setText(rn);
                inputAvailableu.setText(ra);
                inputCostu.setText(co);
                String compareValue;
                if(c.equals("Men")){
                    compareValue = "Men";
                }else if(c.equals("Women")){
                    compareValue = "Women";
                }else{
                    compareValue = "Men & Women";
                }
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(UpdateAdd.this, R.array.categoryArray, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                inputCategoryu.setAdapter(adapter);

                if (compareValue != null) {
                    int spinnerPosition = adapter.getPosition(compareValue);
                    inputCategoryu.setSelection(spinnerPosition);
                }

                if(!ac.equals("false")){
                    AirConditioneru.setChecked(true);
                }
                if(!w.equals("false")){
                    Wifiu.setChecked(true);
                }
                if(!lau.equals("false")){
                    freeelectricityu.setChecked(true);
                }
                if(!e.equals("false")){
                    freeelectricityu.setChecked(true);
                }
                if(!b.equals("false")){
                    privatebathroomu.setChecked(true);
                }
                residenceDescu.setText(rd);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        uploadaddBtnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });

        updateaddBtnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(getApplicationContext(), "UPLOAD IN PROGRESS", Toast.LENGTH_LONG).show();
                }else {
                    dataaddUpdate();
                }
            }
        });
    }

    private void FileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void dataaddUpdate() {
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



                } else {
                    Toast.makeText(UpdateAdd.this, "gagal", Toast.LENGTH_LONG).show();
                }
                Map<String, String> kosanData = new HashMap<>();

                kosanData.put("address", inputfullAddressu.getText().toString());
                if(AirConditioneru.isChecked()){
                    kosanData.put("airConditioner", "true");
                }else {
                    kosanData.put("airConditioner", "false");
                }
                if(privatebathroomu.isChecked()){
                    kosanData.put("bathRoom", "true");
                }else {
                    kosanData.put("bathRoom", "false");
                }
                kosanData.put("category", inputCategoryu.getSelectedItem().toString());
                kosanData.put("cost", inputCostu.getText().toString());
                if(freeelectricityu.isChecked()){
                    kosanData.put("electricity", "true");
                }else {
                    kosanData.put("electricity", "false");
                }
                kosanData.put("latitude", inputLatitudeu.getText().toString());
                if(freelaundryu.isChecked()){
                    kosanData.put("laundry", "true");
                }else {
                    kosanData.put("laundry", "false");
                }
                kosanData.put("longitude", inputLongitudeu.getText().toString());
                kosanData.put("residenceDesc", residenceDescu.getText().toString());
                if(statusaddUploadu.equals("No image chosen")){
                    kosanData.put("residenceImageURL", riu);
                }else{
                    kosanData.put("residenceImageURL", downloadUri.toString());
                }

                kosanData.put("residenceName", inputResidenceNameu.getText().toString());
                kosanData.put("roomAvailable", inputAvailableu.getText().toString());
                kosanData.put("seen", s);
                kosanData.put("statusResidence", st);
                kosanData.put("userID", userID);
                if(freelaundryu.isChecked()){
                    kosanData.put("wifi", "true");
                }else {
                    kosanData.put("wifi", "false");
                }

                myRef2.child(userID+"/kosanList/"+kosanID).setValue(kosanData);
                myRef.child(kosanID).setValue(kosanData);
                Toast.makeText(UpdateAdd.this,"Success.",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(UpdateAdd.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(this, "Sini Gaes", Toast.LENGTH_SHORT).show();
        // Pastikan Resultnya OK
        switch (requestCode){
            case PICK_UP:
                if (resultCode == RESULT_OK) {
                    //Toast.makeText(this, "Sini Gaes2", Toast.LENGTH_SHORT).show();
                    // Tampung Data tempat ke variable

                    // Dapatkan Detail
                    String placeAddress = inputfullAddressu.getText().toString();
                    // Cek user milih titik jemput atau titik tujuan
                    switch (REQUEST_CODE) {
                        case PICK_UP:
                            // Set ke widget lokasi asal
                            inputfullAddressu.setText(placeAddress);
                            break;
                    }
                }
                break;
            case IMAGE:
                if(requestCode==1&&resultCode==RESULT_OK && data !=null && data.getData()!=null){
                    imguri=data.getData();
                    imgaddResidenceu.setImageURI(imguri);
                    statusaddUploadu.setText("picture was added.");
                }
                break;
        }
    }
}
