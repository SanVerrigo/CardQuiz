package com.verrigo.cardquiz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PackAdapter extends RecyclerView.Adapter<PackAdapter.ViewHolder> {

    private List<Pack> packList;
    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.pack_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Pack pack = packList.get(i);
        viewHolder.packNameTextView.setText(pack.getPackName());
    }

    @Override
    public int getItemCount() {
        return packList.size();
    }

    public void setPackList(List<Pack> packList) {
        this.packList = packList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView packNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            packNameTextView = itemView.findViewById(R.id.pack_item_pack_name);
        }
    }
}
