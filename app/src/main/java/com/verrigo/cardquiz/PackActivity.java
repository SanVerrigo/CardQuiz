package com.verrigo.cardquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class PackActivity extends AppCompatActivity {

    private static final String PACK_NAME = "packName";

    private String packName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_pack);

        packName = intent.getStringExtra(PACK_NAME);
        getSupportActionBar().setTitle(packName);
        CardAdapter cardAdapter = new CardAdapter();
        RecyclerView recyclerView = findViewById(R.id.activity_pack_recycler_view);
        cardAdapter.setCardList(testList());
        recyclerView.setAdapter(cardAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                startActivity(new Intent(PackActivity.this, CardActivity.class));
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
