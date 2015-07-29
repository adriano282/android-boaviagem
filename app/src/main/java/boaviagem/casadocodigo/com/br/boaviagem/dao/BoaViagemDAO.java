package boaviagem.casadocodigo.com.br.boaviagem.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by adriano on 29/07/15.
 */
public class BoaViagemDAO {
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public BoaViagemDAO(Context context) {
        helper = new DatabaseHelper(context);
    }

    private SQLiteDatabase getDb() {
        if (db == null) {
            db = helper.getWritableDatabase();
        }
        return db;
    }

    public void close() {
        helper.close();
    }
}
