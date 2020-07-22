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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.compat.AutocompleteFilter;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.ui.PlaceAutocomplete;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddAds extends AppCompatActivity {
    private static final int PICK_UP = 0;
    private EditText inputResidenceName, inputAvailable,inputCost,inputfullAddress, residenceDesc;
    private Spinner inputCategory;
    private CheckBox AirConditioner, Wifi, freelaundry, freeelectricity, privatebathroom;
    private Button uploadaddBtn, updateaddBtn;
    private TextView statusaddUpload;
    private ImageView imgaddResidence;
    private int REQUEST_CODE = 0;
    private static  final int IMAGE = 1;
    private Uri imguri;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef, myRef2;
    private Query residenceRef;
    private String userID;
    private StorageTask uploadTask;
    StorageReference mStorageReference;
    FirebaseDatabase database;
    Uri downloadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ads);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        statusaddUpload = findViewById(R.id.statusaddUpload);
        updateaddBtn = findViewById(R.id.updateaddBtn);
        uploadaddBtn = findViewById(R.id.uploadaddBtn);
        imgaddResidence = findViewById(R.id.imgaddResidence);
        inputfullAddress = findViewById(R.id.inputfullAddress);
        inputResidenceName = findViewById(R.id.inputResidenceName);
        inputAvailable = findViewById(R.id.inputAvailable);
        inputCost = findViewById(R.id.inputCost);
        inputCategory = findViewById(R.id.inputCatagory);
        AirConditioner = findViewById(R.id.AirConditioner);
        Wifi = findViewById(R.id.Wifi);
        freelaundry = findViewById(R.id.freelaundry);
        freeelectricity = findViewById(R.id.freeelectricity);
        privatebathroom = findViewById(R.id.privatebathroom);
        residenceDesc = findViewById(R.id.residenceDesc);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mStorageReference = FirebaseStorage.getInstance().getReference("kosan_img");
        myRef = mFirebaseDatabase.getReference("Users");
        myRef2 = mFirebaseDatabase.getReference("kosan");

        Spinner spinner = findViewById(R.id.inputCatagory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categoryArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
        inputfullAddress.setEnabled(true);

        uploadaddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });

        updateaddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask != null && uploadTask.isInProgress()) {

                    Toast.makeText(getApplicationContext(),
                            "UPLOAD IN PROGRESS",
                            Toast.LENGTH_LONG).show();
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

    private  String getExtension(Uri uri){
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

                    Map<String, String> kosanData = new HashMap<>();

                    kosanData.put("address", inputfullAddress.getText().toString());
                    if(AirConditioner.isChecked()){
                        kosanData.put("airConditioner", "true");
                    }else {
                        kosanData.put("airConditioner", "false");
                    }
                    if(privatebathroom.isChecked()){
                        kosanData.put("bathRoom", "true");
                    }else {
                        kosanData.put("bathRoom", "false");
                    }
                    kosanData.put("category", inputCategory.getSelectedItem().toString());
                    kosanData.put("cost", inputCost.getText().toString());
                    if(freeelectricity.isChecked()){
                        kosanData.put("electricity", "true");
                    }else {
                        kosanData.put("electricity", "false");
                    }
                    if(freelaundry.isChecked()){
                        kosanData.put("laundry", "true");
                    }else {
                        kosanData.put("laundry", "false");
                    }
                    kosanData.put("residenceDesc", residenceDesc.getText().toString());
                    kosanData.put("residenceImageURL", downloadUri.toString());
                    kosanData.put("residenceName", inputResidenceName.getText().toString());
                    kosanData.put("roomAvailable", inputAvailable.getText().toString());
                    kosanData.put("seen", "0");
                    kosanData.put("userID", userID);
                    if(freelaundry.isChecked()){
                        kosanData.put("wifi", "true");
                    }else {
                        kosanData.put("wifi", "false");
                    }

                    DatabaseReference pushedPostRef = myRef2.push();
                    String kosanId = pushedPostRef.getKey();
                    myRef.child(userID+"/kosanList/"+kosanId).setValue(kosanData);
                    myRef2.child(kosanId).setValue(kosanData);
                    Toast.makeText(AddAds.this,"Success.",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddAds.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();


                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case PICK_UP:
                if (resultCode == RESULT_OK) {
                    // Tampung Data tempat ke variable
                    Place placeData = PlaceAutocomplete.getPlace(this, data);
                    if (placeData.isDataValid()) {
                        // Show in Log Cat
                        Log.d("autoCompletePlace Data", placeData.toString());
                        // Dapatkan Detail
                        String placeAddress = placeData.getAddress().toString();
                        // Cek user milih titik jemput atau titik tujuan
                        switch (REQUEST_CODE) {
                            case PICK_UP:
                                // Set ke widget lokasi asal
                                inputfullAddress.setText(placeAddress);
                                break;
                        }
                    } else {
                        // Data tempat tidak valid
                        Toast.makeText(this, "Invalid Place !", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case IMAGE:
                if(requestCode==1&&resultCode==RESULT_OK && data !=null && data.getData()!=null){
                    imguri=data.getData();
                    imgaddResidence.setImageURI(imguri);
                    statusaddUpload.setText("picture was added.");
                }
                break;
        }
    }
}
