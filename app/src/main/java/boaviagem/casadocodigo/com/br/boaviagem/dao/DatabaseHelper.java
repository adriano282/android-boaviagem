package boaviagem.casadocodigo.com.br.boaviagem.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by adriano on 26/07/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE = "GoodTravel";
    private static int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table travel (_id integer primary key, " +
        " destiny text, type_travel integer, date_arrive date, " +
        " date_out date, budget double, " +
        " quantity_people integer);");

        db.execSQL("create table spent (_id integer primary key, " +
        " category text, data date, value double, " +
        " description text, place text, travel_id integer, " +
        " foreign key(travel_id) references travel(_id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("alter table spent add column user text");
    }
}
