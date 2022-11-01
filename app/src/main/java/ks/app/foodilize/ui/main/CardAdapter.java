package ks.app.foodilize.ui.main;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import ks.app.foodilize.ObjectNC;
import ks.app.foodilize.R;
import ks.app.foodilize.Utils;
import ks.app.foodilize.ViewNGO;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>{

    ArrayList<ObjectNC> arrayList;
    Context context;
    private LayoutInflater mInflater;

    CardAdapter(Context context, ArrayList<ObjectNC> arrayList){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.arrayList = arrayList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.object_nc_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        ObjectNC obj = arrayList.get(position);
        holder.tV_rank.setText("#"+String.valueOf(position+1));
        holder.tV_name.setText(obj.getName());
        holder.iV_profile.setImageBitmap(Utils.StringToBitMap(obj.getImgUrl()));
        holder.container.setOnClickListener(v->{
            Intent i = new Intent(context, ViewNGO.class);
            i.putExtra("USER", obj);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tV_rank;
        TextView tV_name;
        ImageView iV_profile;
        ConstraintLayout container;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tV_name = itemView.findViewById(R.id.tV_ngo_card_name);
            tV_rank = itemView.findViewById(R.id.tV_ngo_card_number);
            iV_profile = itemView.findViewById(R.id.iV_ngo_card_img);
            container = itemView.findViewById(R.id.container_ngo_card);
            container.setOnClickListener(v->{
                //TODO: Open the Details Page
            });
        }
    }
}
