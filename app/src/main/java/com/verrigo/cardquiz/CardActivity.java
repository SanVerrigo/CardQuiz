package com.verrigo.cardquiz;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CardActivity extends AppCompatActivity {

    public static final String PACK_NAME = "packName";
    public static final String QUESTION = "question";
    public static final String ANSWER = "answer";
    public static final String ID = "_id";
    public static final String INTENTION = "intention";
    public static final String INTENTION_EDIT = "intentionEdit";
    public static final String INTENTION_CREATE = "intentionCreate";

    private String currentIntention;
    private String question;
    private String answer;
    private CardQuizDBHelper dbHelper;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int _id;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        dbHelper = new CardQuizDBHelper(this);
        final EditText questionEditText = findViewById(R.id.activity_card_question);
        final EditText answerEditText = findViewById(R.id.activity_card_answer);
        intent = getIntent();
        final String packName = intent.getStringExtra(PACK_NAME);
        currentIntention = intent.getStringExtra(INTENTION);
        View view = findViewById(R.id.activity_card_foot_container);
        switch (currentIntention) {
            case INTENTION_EDIT:
                _id = intent.getExtras().getInt(ID);
                question = intent.getStringExtra(QUESTION);
                answer = intent.getStringExtra(ANSWER);
                questionEditText.setText(question);
                answerEditText.setText(answer);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!(questionEditText.getText().toString().isEmpty() && answerEditText.getText().toString().isEmpty())) {
                            dbHelper.changeCardWithId(new Card(_id, packName, questionEditText.getText().toString(), answerEditText.getText().toString()));
                            finish();
                        } else {
                            Toast.makeText(CardActivity.this, "Пожалуйста, заполните поля", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;

            case INTENTION_CREATE:
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!(questionEditText.getText().toString().isEmpty() && answerEditText.getText().toString().isEmpty())) {
                            try {
                                dbHelper.addNewCard(new Card(packName, questionEditText.getText().toString(), answerEditText.getText().toString()));
                                finish();
                            } catch (Exception ex) {
                            }
                        } else {
                            Toast.makeText(CardActivity.this, "Пожалуйста, заполните поля", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_card, menu);
        MenuItem item = menu.findItem(R.id.menu_activity_card_delete);
        switch (currentIntention) {
            case INTENTION_EDIT:
                item.setVisible(true);
                getSupportActionBar().setTitle("Редактирование");
                break;
            case INTENTION_CREATE:
                item.setVisible(false);
                getSupportActionBar().setTitle("Создание");
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_activity_card_delete:
                dbHelper.deleteCardById(intent.getExtras().getInt(ID));
                finish();
                return true;
            default:
                return true;
        }
    }

    public Intent createIntentForCreation(Context context, String packName, String intention) {
        return new Intent(context, this.getClass())
                .putExtra(PACK_NAME, packName)
                .putExtra(INTENTION, intention);
    }
    public Intent createIntentForEditing(Context context, int _id, String packName, String question, String answer, String intention) {
        return new Intent(context, this.getClass())
                .putExtra(ID, _id)
                .putExtra(PACK_NAME, packName)
                .putExtra(QUESTION, question)
                .putExtra(ANSWER, answer)
                .putExtra(INTENTION, intention);
    }

}
