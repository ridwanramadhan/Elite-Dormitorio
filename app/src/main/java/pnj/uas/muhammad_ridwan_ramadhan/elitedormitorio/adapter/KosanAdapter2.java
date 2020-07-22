package pnj.uas.muhammad_ridwan_ramadhan.elitedormitorio.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pnj.uas.muhammad_ridwan_ramadhan.elitedormitorio.AddList;
import pnj.uas.muhammad_ridwan_ramadhan.elitedormitorio.MainActivity;
import pnj.uas.muhammad_ridwan_ramadhan.elitedormitorio.R;
import pnj.uas.muhammad_ridwan_ramadhan.elitedormitorio.UpdateAdd;
import pnj.uas.muhammad_ridwan_ramadhan.elitedormitorio.model.Kosan2;

public class KosanAdapter2 extends RecyclerView.Adapter<KosanAdapter2.MyViewHolder> {
    Context context2;
    ArrayList<Kosan2> models2;

    public KosanAdapter2(Context c, ArrayList<Kosan2> m){
        context2 = c;
        models2 = m;
    }

    @NonNull
    @Override
    public KosanAdapter2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new KosanAdapter2.MyViewHolder(LayoutInflater.from(context2).inflate(R.layout.item2,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull KosanAdapter2.MyViewHolder holder, int position) {
        holder.residenceNamel.setText(models2.get(position).getResidenceName());
        holder.costl.setText("Rp"+models2.get(position).getCost());
        holder.categoryl.setText(models2.get(position).getCategory());
        Picasso.get().load(models2.get(position).getResidenceImageURL()).into(holder.imgResidencel);
        holder.availRooml.setText(models2.get(position).getRoomAvailable()+" Room(s)");
        holder.useidTv.setText(models2.get(position).getUserID());
        holder.kosidTv.setText(models2.get(position).getKosanID());
    }

    @Override
    public int getItemCount() {
        return models2.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView residenceNamel, categoryl, costl, availRooml, useidTv, kosidTv ;
        ImageView imgResidencel;
        Button updatekosanBtnl,deletekosanBtn1;
        public  MyViewHolder(View itemView){
            super(itemView);
            residenceNamel = itemView.findViewById(R.id.residenceNamel);
            categoryl = itemView.findViewById(R.id.categoryl);
            imgResidencel = itemView.findViewById(R.id.imgResidencel);
            costl = itemView.findViewById(R.id.costl);
            useidTv = itemView.findViewById(R.id.useidTv);
            kosidTv = itemView.findViewById(R.id.kosidTv);
            availRooml = itemView.findViewById(R.id.availRooml);
            updatekosanBtnl = itemView.findViewById(R.id.updatekosanBtnl);
            deletekosanBtn1 = itemView.findViewById(R.id.deletekosanBtn1);

            deletekosanBtn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deletekosan();
                    Intent myactivity = new Intent(context2.getApplicationContext(), MainActivity.class);
                    myactivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context2.getApplicationContext().startActivity(myactivity);
                }
            });

            updatekosanBtnl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myactivity = new Intent(context2.getApplicationContext(), UpdateAdd.class);
                    myactivity.putExtra("kosanID", kosidTv.getText().toString());
                    myactivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context2.getApplicationContext().startActivity(myactivity);
                    ((AddList)context2).finish();
                }
            });

        }

        public void deletekosan(){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            Query deleteQuery = ref.child(useidTv.getText().toString()+"/kosanList/"+kosidTv.getText().toString());
            Query deleteQuery2 = FirebaseDatabase.getInstance().getReference("kosan").orderByChild(kosidTv.getText().toString()).equalTo(kosidTv.getText().toString());

            DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference()
                    .child("kosan").child(kosidTv.getText().toString());
            mPostReference.removeValue();

            DatabaseReference mPostReference2 = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(useidTv.getText().toString()).child("kosanList").child(kosidTv.getText().toString());
            mPostReference2.removeValue();


        }
    }
}
