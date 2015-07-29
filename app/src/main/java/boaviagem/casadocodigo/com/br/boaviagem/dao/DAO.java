package boaviagem.casadocodigo.com.br.boaviagem.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by adriano on 29/07/15.
 */
public abstract  class DAO {
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public DAO(Context context) {
        helper = new DatabaseHelper(context);
    }

    protected SQLiteDatabase getDb() {
        if (db == null) {
            db = helper.getWritableDatabase();
        }
        return db;
    }

    public void close() {
        helper.close();
    }

}
