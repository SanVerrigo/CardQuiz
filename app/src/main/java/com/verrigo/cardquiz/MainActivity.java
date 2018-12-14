package com.verrigo.cardquiz;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements PackNameDialogFragment.PackNameDialogListener {

    private static final String CHECK_STATE = "checkState";
    CardQuizDBHelper dbHelper;
    PackAdapter packAdapter;
    Switch aSwitch;
    public final static String CHAOTIC_MODE = "chaoticMode";
    public final static String NORMAL_MODE = "normalMode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.activity_main_recycler_view);
        packAdapter = new PackAdapter();
        aSwitch = findViewById(R.id.activity_main_switch);
        if (savedInstanceState != null) {
            aSwitch.setChecked(savedInstanceState.getBoolean(CHECK_STATE));
            packAdapter.setMode(savedInstanceState.getBoolean(CHECK_STATE) ? CHAOTIC_MODE : NORMAL_MODE);
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                packAdapter.setMode(b ? CHAOTIC_MODE : NORMAL_MODE);
            }
        });

        recyclerView.setAdapter(packAdapter);
        recyclerView.addItemDecoration(new VerticalDividerDecoration(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbHelper = new CardQuizDBHelper(this);
        packAdapter.setMode(aSwitch.isChecked() ? CHAOTIC_MODE : NORMAL_MODE);
        packAdapter.setPackList(dbHelper.getPackList());
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
                PackNameDialogFragment dialogFragment = new PackNameDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "packName");
                return true;
            default:
                return true;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(CHECK_STATE, aSwitch.isChecked());
        super.onSaveInstanceState(outState);
    }

    //    private List<Pack> testList() {
//        List<Pack> list = new ArrayList<>();
//        for (int i = 0; i < 12; i++) {
//            list.add(new Pack(String.format("Pack for us %d", i)));
//        }
//        return list;
//    }

    @Override
    public void onDialogPositiveButtonClick(DialogFragment dialogFragment, EditText editTextPackName) {
        String packName = editTextPackName.getText().toString();
        if (!dbHelper.doesPackExistByPackName(packName)) {
            dbHelper.addNewPack(new Pack(packName));
            startActivity(new PackActivity().createIntent(this, packName));
        } else {
            Toast.makeText(this, String.format("Пакет с именем '%s' уже существует", packName), Toast.LENGTH_SHORT).show();
        }
    }
}
