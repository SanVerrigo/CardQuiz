package com.verrigo.cardquiz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class PackAdapter extends RecyclerView.Adapter<PackAdapter.ViewHolder> {

    private List<Pack> packList;
    private Context context;
    private CardQuizDBHelper dbHelper;
    private String mode;
    List<Card> cardList;



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        dbHelper = new CardQuizDBHelper(context);
        View view = LayoutInflater.from(context).inflate(R.layout.pack_item, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Pack pack = packList.get(i);
        boolean isAvailable = false;
        cardList = dbHelper.getCardListByPackName(pack.getPackName());
        for (Card each : cardList) {
            if (each.isShown()) {
                isAvailable = true;
                break;
            }
        }
        viewHolder.packNameTextView.setText(pack.getPackName());
        if (!cardList.isEmpty() && isAvailable) {
            viewHolder.startQuizButton.setClickable(true);
            viewHolder.startQuizButton.setImageResource(R.drawable.ic_play_arrow_green_48dp);
            viewHolder.startQuizButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new QuizActivity().createIntent(context, pack.getPackName(), getMode()));
                }
            });
        } else {
            viewHolder.startQuizButton.setClickable(false);
            viewHolder.startQuizButton.setImageResource(R.drawable.ic_play_arrow_gray_48dp);
        }
        viewHolder.mainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new PackActivity().createIntent(context, pack.getPackName()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return packList.size();
    }

    public void setPackList(List<Pack> packList) {
        this.packList = packList;

        notifyDataSetChanged();
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView packNameTextView;
        private ImageButton startQuizButton;
        private ConstraintLayout mainContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            startQuizButton = itemView.findViewById(R.id.pack_item_start_quiz_button);
            mainContainer = itemView.findViewById(R.id.pack_item_main_container);
            packNameTextView = itemView.findViewById(R.id.pack_item_pack_name);
        }
    }
}
