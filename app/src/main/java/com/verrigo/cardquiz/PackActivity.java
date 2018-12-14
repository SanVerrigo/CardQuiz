package com.verrigo.cardquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.verrigo.cardquiz.utils.DiskUtil;
import com.verrigo.cardquiz.utils.IntentUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PackActivity extends AppCompatActivity {

    private static final String PACK_NAME = "packName";
    private static final int VERTICAL_ITEM_SPACE = 48;

    private Pack pack;
    private CardAdapter cardAdapter;
    String packName;
    private CardQuizDBHelper dbHelper;
//    private JsonPackShareManager jsonPackShareManager = new JsonPackShareManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack);
        Intent intent = getIntent();
        packName = intent.getStringExtra(PACK_NAME);
        getSupportActionBar().setTitle(packName);
        cardAdapter = new CardAdapter();
        dbHelper = new CardQuizDBHelper(this);
        ArrayList<Card> cards = (ArrayList<Card>) dbHelper.getCardListByPackName(packName);
        pack = new Pack(0, packName, cards);
        RecyclerView recyclerView = findViewById(R.id.activity_pack_recycler_view);
        recyclerView.addItemDecoration(new VerticalDividerDecoration(this));
        recyclerView.setAdapter(cardAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            cardAdapter.setCardList(dbHelper.getCardListByPackName(packName));
        } catch (Exception ex) {
//            cardAdapter.setCardList(testList());
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        dbHelper.changeCardsIsShownStatement(cardAdapter.getCardList());
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
            case R.id.menu_activity_pack_share_pack:
                sharePackApp(pack);
            default:
                return true;
        }
    }

//    public List<Card> testList(){
//        List<Card> list = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            list.add(new Card("", String.format("here i come %d", i), ""));
//        }
//        return list;
//    }

    public JSONObject writeJson() {
        JSONObject mainJsonObject = new JSONObject();
        JSONObject subJsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        List<Card> cardList = dbHelper.getCardListByPackName(packName);
        try {
            mainJsonObject.put("packName", packName);
            for (Card card : cardList) {
                subJsonObject = new JSONObject();
//                subJsonObject.put("id", card.get_id());
                subJsonObject.put("question", card.getQuestion());
                subJsonObject.put("answer", card.getAnswer());
                jsonArray.put(subJsonObject);
            }
            mainJsonObject.put("cards", subJsonObject);
            return mainJsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void sharePackApp(Pack pack) {
        final Gson gson = new GsonBuilder().create();
        final String jsonStr = gson.toJson(pack);
        final String fileName = pack.getPackName() ;
        final File outputFile = DiskUtil.saveJsonOnDisk(this, jsonStr, fileName);
        final Intent intent = IntentUtils.createShareCardset(this,
                outputFile,
                getString(R.string.share_cardset_subject));
        startActivity(Intent.createChooser(intent, getString(R.string.send_cardset_title)));
    }

    public Intent createIntent(Context context, String packName) {
        return new Intent(context, this.getClass())
                .putExtra(PACK_NAME, packName);
    }
}
