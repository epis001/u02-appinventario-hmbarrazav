package com.example.appinventario;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private List<Productos> imageUrls; private Context context;
    public DataAdapter(Context context, List<Productos> imageUrls) {
        this.context = context; this.imageUrls = imageUrls;
    }
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_layout, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Glide.with(context).load(imageUrls.get(i).getImg()).into(viewHolder.img);
        viewHolder.nombre.setText(imageUrls.get(i).getName());
        viewHolder.precio.setText(imageUrls.get(i).getPrice()+"");
    }
    @Override
    public int getItemCount() {
        return imageUrls.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img; TextView nombre; TextView precio;
        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.imageView);
            nombre = view.findViewById(R.id.tvnombre);
            precio = view.findViewById(R.id.tvprecio);
        }
    }
    public void update(List<Productos> datas){
        this.imageUrls = datas; notifyDataSetChanged();
    }
}
