package com.example.SQLiteBike;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication1.R;

import java.util.ArrayList;
import java.util.Objects;


public class BikeListAdapter extends BaseAdapter {

    private Context context;
    private  int layout;
    private ArrayList<Bike> bikeList;

    public BikeListAdapter(Context context, int layout, ArrayList<Bike> bikeList) {
        this.context = context;
        this.layout = layout;
        this.bikeList = bikeList;
    }

    @Override
    public int getCount() {
        return bikeList.size();
    }

    @Override
    public Object getItem(int position) {
        return bikeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtName, txtType, txtChainset, txtCassette,txtBracket, txtChainlink;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = Objects.requireNonNull(inflater).inflate(layout, null);
            //row = inflater.inflate(layout, null);

            holder.txtName = row.findViewById(R.id.txtBikeName);
            holder.txtType = row.findViewById(R.id.txtBikeType);
            holder.txtChainset = row.findViewById(R.id.txtSqlChainset);
            holder.txtCassette = row.findViewById(R.id.txtSqlCassette);
            holder.txtBracket = row.findViewById(R.id.txtSqlBracket);
            holder.txtChainlink = row.findViewById(R.id.txtSqlChainLinkNumber);
            holder.imageView = row.findViewById(R.id.imgBike);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        Bike bike = bikeList.get(position);

        holder.txtName.setText(bike.getName());
        holder.txtType.setText(bike.getType());
        holder.txtChainset.setText(bike.getChainset());
        holder.txtCassette.setText(bike.getCassette());
        holder.txtBracket.setText(bike.getBracket());
        holder.txtChainlink.setText(bike.getChainlink());

        byte[] bikeImage = bike.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bikeImage, 0, bikeImage.length);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }
}
