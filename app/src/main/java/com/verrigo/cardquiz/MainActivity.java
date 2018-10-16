package com.verrigo.cardquiz;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PackNameDialogFragment.PackNameDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.activity_main_recycler_view);
        PackAdapter packAdapter = new PackAdapter();
        packAdapter.setPackList(testList());
        recyclerView.setAdapter(packAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_activity_main_create_pack:
                Toast.makeText(this, "dsa", Toast.LENGTH_SHORT).show();
                PackNameDialogFragment dialogFragment = new PackNameDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "packName");
                return true;
            default:
                return true;
        }
    }

    private List<Pack> testList() {
        List<Pack> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            list.add(new Pack(String.format("Pack for us %d", i)));
        }
        return list;
    }

    @Override
    public void onDialogPositiveButtonClick(DialogFragment dialogFragment, EditText editTextPackName) {
        String packName = editTextPackName.getText().toString();
        startActivity(new PackActivity().createIntent(this, packName));
    }
}
