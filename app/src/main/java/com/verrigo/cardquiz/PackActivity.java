package com.verrigo.cardquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PackActivity extends AppCompatActivity {

    private static final String PACK_NAME = "packName";

    private String packName;
    private CardAdapter cardAdapter;
    private CardQuizDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack);
        Intent intent = getIntent();
        packName = intent.getStringExtra(PACK_NAME);
        getSupportActionBar().setTitle(packName);
        cardAdapter = new CardAdapter();
        dbHelper = new CardQuizDBHelper(this);
        RecyclerView recyclerView = findViewById(R.id.activity_pack_recycler_view);
        recyclerView.setAdapter(cardAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            cardAdapter.setCardList(dbHelper.getCardListByPackName(packName));
        } catch (Exception ex) {
            cardAdapter.setCardList(testList());
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_pack, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_activity_pack_create_item:
                startActivity(new CardActivity().createIntentForCreation(this, packName, CardActivity.INTENTION_CREATE));
                return true;
            case R.id.menu_activity_pack_delete_pack:
                // TODO: 17.10.2018 change this to the alert dialog
                dbHelper.deletePackByPackName(packName);
                finish();
                return true;
            default:
                return true;
        }
    }

    public List<Card> testList(){
        List<Card> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(new Card("", String.format("here i come %d", i), ""));
        }
        return list;
    }

    public Intent createIntent(Context context, String packName) {
        return new Intent(context, this.getClass())
                .putExtra(PACK_NAME, packName);
    }
}
