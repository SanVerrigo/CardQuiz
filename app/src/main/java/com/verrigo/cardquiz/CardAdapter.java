package com.verrigo.cardquiz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private List<Card> cardList;
    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.card_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Card card = cardList.get(i);
        viewHolder.questionText.setText(card.getQuestion());
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView questionText;

        public ViewHolder(View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.card_item_question_text);
        }
    }
}
