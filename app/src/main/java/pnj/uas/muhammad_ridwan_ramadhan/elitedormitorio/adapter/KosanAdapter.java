package pnj.uas.muhammad_ridwan_ramadhan.elitedormitorio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pnj.uas.muhammad_ridwan_ramadhan.elitedormitorio.R;
import pnj.uas.muhammad_ridwan_ramadhan.elitedormitorio.model.Kosan;

public class KosanAdapter extends RecyclerView.Adapter<KosanAdapter.MyViewHolder> {

    Context context;
    ArrayList<Kosan> models;

    public KosanAdapter(Context c, ArrayList<Kosan> m){
        context = c;
        models = m;
    }

    @NonNull
    @Override
    public KosanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull KosanAdapter.MyViewHolder holder, int position) {
        holder.residenceName.setText(models.get(position).getResidenceName());
        holder.cost.setText("Rp"+models.get(position).getCost());
        holder.category.setText(models.get(position).getCategory());
        holder.availRoom.setText(models.get(position).getRoomAvailable()+" Room(s)");
        Picasso.get().load(models.get(position).getResidenceImageURL()).into(holder.ImageURL);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    static class  MyViewHolder extends  RecyclerView.ViewHolder{
        TextView residenceName, category, cost, availRoom;
        ImageView ImageURL;
        public  MyViewHolder(View itemView){
            super(itemView);
            residenceName = itemView.findViewById(R.id.residenceName);
            category = itemView.findViewById(R.id.category);
            ImageURL = itemView.findViewById(R.id.imgResidence);
            cost = itemView.findViewById(R.id.cost);
            availRoom = itemView.findViewById(R.id.availRoom);
        }
    }
}
