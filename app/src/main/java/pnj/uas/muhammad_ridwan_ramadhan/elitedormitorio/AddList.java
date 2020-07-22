package pnj.uas.muhammad_ridwan_ramadhan.elitedormitorio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pnj.uas.muhammad_ridwan_ramadhan.elitedormitorio.adapter.KosanAdapter2;
import pnj.uas.muhammad_ridwan_ramadhan.elitedormitorio.model.Kosan2;

public class AddList extends AppCompatActivity {
    RecyclerView recyclerView2;
    KosanAdapter2 adapter2;
    List<Kosan2> models2;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private Query residenceRef;
    private String userID;

    Animation atg, atg2, atg3;
    TextView listTv, useidTv, kosidTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        atg = AnimationUtils.loadAnimation(this, R.anim.atg);
        atg2 = AnimationUtils.loadAnimation(this, R.anim.atg2);
        atg3 = AnimationUtils.loadAnimation(this, R.anim.atg3);

        recyclerView2 = findViewById(R.id.recyclerView);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        models2 = new ArrayList<Kosan2>();

        listTv = findViewById(R.id.listTv);

        useidTv = findViewById(R.id.useidTv);
        kosidTv = findViewById(R.id.kosidTv);

        recyclerView2.setAnimation(atg);
        listTv.setAnimation(atg2);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        myRef = mFirebaseDatabase.getReference("Users");
        residenceRef = mFirebaseDatabase.getReference("Users").child(userID).child("kosanList");

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
                for (DataSnapshot dsp:snapshot.getChildren()){
                    Kosan2 m = dsp.getValue(Kosan2.class);
                    m.setKosanID(dsp.getKey());
                    models2.add(m);

                }
                adapter2 = new KosanAdapter2(AddList.this, (ArrayList<Kosan2>) models2);
                recyclerView2.setAdapter(adapter2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "ERROR!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void deletekosan(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        Query deleteQuery = ref.child(useidTv.getText().toString()+"/kosanList/").orderByChild(kosidTv.getText().toString()).equalTo(kosidTv.getText().toString());

        deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot desp: dataSnapshot.getChildren()) {
                    desp.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddList.this, "Error, "+databaseError, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
