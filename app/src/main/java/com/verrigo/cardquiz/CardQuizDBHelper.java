package com.verrigo.cardquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class CardQuizDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 3;
    private static final String DB_NAME = "cardQuizDB";

    public static final String CARD_COLUMN_ID = "card_id";
    public static final String CARD_TABLE_NAME = "cardTable";
    public static final String CARD_COLUMN_PACK_NAME = "cardPackName";
    public static final String CARD_COLUMN_QUESTION = "cardQuestion";
    public static final String CARD_COLUMN_ANSWER = "cardAnswer";
    public static final String CARD_COLUMN_IS_SHOWN = "cardIsShown";

    public static final String PACK_TABLE_NAME = "packTable";
    public static final String PACK_COLUMN_ID = "pack_id";
    public static final String PACK_COLUMN_PACK_NAME = "packPackName";

    private final String createCardTableCommand = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT)", CARD_TABLE_NAME, CARD_COLUMN_ID, CARD_COLUMN_PACK_NAME, CARD_COLUMN_QUESTION, CARD_COLUMN_ANSWER);
    private final String createPackTableCommand = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT)", PACK_TABLE_NAME, PACK_COLUMN_ID, PACK_COLUMN_PACK_NAME);

    private final String upgradeCardTableCommandToV2 = String.format("ALTER TABLE %s ADD %s BOOLEAN", CARD_TABLE_NAME, CARD_COLUMN_IS_SHOWN);
    private final String upgradeCardTableCommandToV3 = String.format("ALTER TABLE %s DROP %s ADD %s NUMERIC", CARD_TABLE_NAME, CARD_COLUMN_IS_SHOWN, CARD_COLUMN_IS_SHOWN);


    public CardQuizDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        onUpgrade(sqLiteDatabase, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i < 1) {
            sqLiteDatabase.execSQL(createPackTableCommand);
            sqLiteDatabase.execSQL(createCardTableCommand);
        }
        if (i < 2) {
            sqLiteDatabase.execSQL(upgradeCardTableCommandToV2);
        }
    }

    public void addNewPack(Pack pack) {
        ContentValues packValues = new ContentValues();
        packValues.put(PACK_COLUMN_PACK_NAME, pack.getPackName());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(PACK_TABLE_NAME, null, packValues);
        db.close();
    }

    public boolean doesPackExistByPackName(String packName) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s = ?", PACK_COLUMN_ID, PACK_TABLE_NAME, PACK_COLUMN_PACK_NAME), new String[]{packName});
        boolean check = cursor.moveToFirst();
        db.close();
        cursor.close();
        return check;
    }

    public void deletePackByPackName(String packName) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(PACK_TABLE_NAME, PACK_COLUMN_PACK_NAME + " = ?", new String[]{packName});
        db.delete(CARD_TABLE_NAME, CARD_COLUMN_PACK_NAME + " = ?", new String[]{packName});
        db.close();
    }

    public List<Pack> getPackList() {
        List<Pack> packs = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(PACK_TABLE_NAME, new String[]{PACK_COLUMN_ID, PACK_COLUMN_PACK_NAME}, null, null, null, null, null);
        int _id;
        String packName;
        if (cursor.moveToFirst()) {
            do {
                _id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(PACK_COLUMN_ID)));
                packName = cursor.getString(cursor.getColumnIndex(PACK_COLUMN_PACK_NAME));
                packs.add(new Pack(_id, packName));
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return packs;
    }



    public void addNewCard(Card card) {
        ContentValues cardValues = new ContentValues();
        cardValues.put(CARD_COLUMN_PACK_NAME, card.getPackName());
        cardValues.put(CARD_COLUMN_QUESTION, card.getQuestion());
        cardValues.put(CARD_COLUMN_ANSWER, card.getAnswer());
        cardValues.put(CARD_COLUMN_IS_SHOWN, true);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(CARD_TABLE_NAME, null, cardValues);
    }

    public void deleteCardById(int _id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(CARD_TABLE_NAME, CARD_COLUMN_ID + " = ?", new String[]{Integer.toString(_id)});
        db.close();
    }

    public void changeCardWithId(Card card) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues forUpdate = new ContentValues();
        forUpdate.put(CARD_COLUMN_QUESTION, card.getQuestion());
        forUpdate.put(CARD_COLUMN_ANSWER, card.getAnswer());
        db.update(CARD_TABLE_NAME, forUpdate, "card_id = ?", new String[]{Integer.toString(card.get_id())});
        db.close();
    }

    public void changeCardIsShownStatementById(int _id, boolean b) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues forUpdate = new ContentValues();
        int numToSet = b ? 1 : 0;
        forUpdate.put(CARD_COLUMN_IS_SHOWN, numToSet);
        db.update(CARD_TABLE_NAME, forUpdate, "card_id = ?", new String[]{Integer.toString(_id)});
        db.close();
    }

    public void changeCardsIsShownStatement(List<Card> cards) {
        int i = 0;
        if (!cards.isEmpty()) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues forUpdate;
            Cursor cursor = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s = ?", CARD_COLUMN_ID, CARD_TABLE_NAME, CARD_COLUMN_PACK_NAME), new String[]{cards.get(i).getPackName()});
            if (cursor.moveToFirst()) {
                do {
                    forUpdate = new ContentValues();
                    forUpdate.put(CARD_COLUMN_IS_SHOWN, cards.get(i).isShown() ? 1 : 0);
                    db.update(CARD_TABLE_NAME, forUpdate, "card_id = ?", new String[]{Integer.toString(cursor.getInt(cursor.getColumnIndex(CARD_COLUMN_ID)))});
                    i++;
                } while (cursor.moveToNext());
            }
            db.close();
            cursor.close();
        }
    }

    public List<Card> getCardListByPackName(String packName) {
        List<Card> cards = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT %s, %s, %s, %s FROM %s WHERE %s = ?", CARD_COLUMN_ID, CARD_COLUMN_QUESTION, CARD_COLUMN_ANSWER, CARD_COLUMN_IS_SHOWN, CARD_TABLE_NAME, CARD_COLUMN_PACK_NAME), new String[]{packName});
        int _id;
        String question;
        String answer;
        boolean isShown;
        if (cursor.moveToFirst()) {
            do {
                _id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CARD_COLUMN_ID)));
                question = cursor.getString(cursor.getColumnIndex(CARD_COLUMN_QUESTION));
                answer = cursor.getString(cursor.getColumnIndex(CARD_COLUMN_ANSWER));
                isShown = cursor.getInt(cursor.getColumnIndex(CARD_COLUMN_IS_SHOWN)) == 1;
                cards.add(new Card(_id, packName, question, answer, isShown));
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return cards;
    }

    public void addPackFromList(Pack pack, String packNameToAdd, boolean ignoreRepeats){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cardValues;
        String packName = packNameToAdd == null ? pack.getPackName() : packNameToAdd;
        ContentValues packValues = new ContentValues();
        Cursor c = db.rawQuery((String.format("SELECT %s FROM %s WHERE %s = ?", PACK_COLUMN_PACK_NAME, PACK_TABLE_NAME ,PACK_COLUMN_PACK_NAME)), new String[]{packName});
        if (!c.moveToFirst()) {
            packValues.put(PACK_COLUMN_PACK_NAME, pack.getPackName());
            db.insert(PACK_TABLE_NAME, null, packValues);
        }
        if (!ignoreRepeats) {
            for (Card card : pack.getCards()) {
                cardValues = new ContentValues();
                cardValues.put(CARD_COLUMN_PACK_NAME, packName);
                cardValues.put(CARD_COLUMN_QUESTION, card.getQuestion());
                cardValues.put(CARD_COLUMN_ANSWER, card.getAnswer());
                cardValues.put(CARD_COLUMN_IS_SHOWN, true);
                db.insert(CARD_TABLE_NAME, null, cardValues);
            }
        } else {
            for (Card card : pack.getCards()) {
                c = db.rawQuery((String.format("SELECT %s FROM %s WHERE %s = ?", CARD_COLUMN_ANSWER, CARD_TABLE_NAME, CARD_COLUMN_ANSWER)), new String[]{card.getAnswer()});
                if (!c.moveToFirst()) {
                    cardValues = new ContentValues();
                    cardValues.put(CARD_COLUMN_PACK_NAME, packName);
                    cardValues.put(CARD_COLUMN_QUESTION, card.getQuestion());
                    cardValues.put(CARD_COLUMN_ANSWER, card.getAnswer());
                    cardValues.put(CARD_COLUMN_IS_SHOWN, true);
                    db.insert(CARD_TABLE_NAME, null, cardValues);
                }
            }
        }

        
        c.close();
        db.close();
    }


}
