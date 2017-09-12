package com.example.peacock.wheatherapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by peacock on 26/4/17.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomMyAdapter> {

    Context context;
    ArrayList<User> list = null;

    public CustomAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public CustomMyAdapter onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new CustomMyAdapter(view);
    }

    @Override
    public void onBindViewHolder(CustomMyAdapter holder, int position) {

        User obj = list.get(position); // return  Bean class..

        // set text
        holder.time.setText(String.valueOf(obj.getTime()));
        holder.temperture.setText(String.valueOf(obj.getTemperture()));

//        Glide.with(context)
//                .load(obj.getIcon())
//                .transform(new GlideCircleTransform(context))
//                .into(holder.img);

        Glide.with(context)
                .load(obj.getIcon())
                .transform(new GlideCircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_userimg)
                .crossFade(1000)
                .thumbnail(0.2f)
                // .fitCenter()
                .into(holder.img);

    /*Glide.with(context).load(list.get(position).getIcon())
            .thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img);
*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomMyAdapter extends RecyclerView.ViewHolder {

        TextView time, temperture;
        ImageView img;

        public CustomMyAdapter(View itemView) {
            super(itemView);

            time = (TextView) itemView.findViewById(R.id.textView_time);
            temperture = (TextView) itemView.findViewById(R.id.textView_temperture);
            img = (ImageView) itemView.findViewById(R.id.List_icon);
        }
    }
}
