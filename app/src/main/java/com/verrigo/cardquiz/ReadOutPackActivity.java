package com.verrigo.cardquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.verrigo.cardquiz.utils.DiskUtil;

public class ReadOutPackActivity extends AppCompatActivity {

    private Pack pack;
    private CardQuizDBHelper dbHelper;

    private Switch addRepeatsSwitch;
    private EditText packNameEditText;
    private TextView warningTextView;
    private TextView numberOfCardsTextView;
    private View addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_out_pack);

        final Gson gson = new GsonBuilder().create();
        final String jsonDeck = DiskUtil.readStringFromUri(this, getIntent().getData());
        if (jsonDeck == null) {
            finish();
            return;
        }
        try {
            pack = gson.fromJson(jsonDeck, Pack.class);
        } catch (JsonSyntaxException e) {
            Toast.makeText(this, "da", Toast.LENGTH_SHORT).show();
            finish();
        }
        dbHelper = new CardQuizDBHelper(this);

        warningTextView = findViewById(R.id.activity_read_out_warning_string);
        packNameEditText = findViewById(R.id.activity_read_out_pack_name);
        numberOfCardsTextView = findViewById(R.id.activity_read_out_number_of_cards);
        addButton = findViewById(R.id.activity_read_out_add_button);
        addRepeatsSwitch = findViewById(R.id.activity_read_out_switch_add_repeats);

        if (dbHelper.doesPackExistByPackName(pack.getPackName())) {
            warningTextView.setText(String.format("!Предупреждение\nПакет \"%s\" уже существует", pack.getPackName()));
        }
        packNameEditText.setText(pack.getPackName());
        numberOfCardsTextView.setText(Integer.toString(pack.getCards().size()));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dbHelper.addPackFromList(pack, packNameEditText.getText().toString(), addRepeatsSwitch.isChecked());
                    startActivity(new Intent(ReadOutPackActivity.this, MainActivity.class));
                    finish();
                } catch (Exception ex) {
                    dbHelper.deletePackByPackName(pack.getPackName());
                    Toast.makeText(ReadOutPackActivity.this, "An exception was made", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




}
