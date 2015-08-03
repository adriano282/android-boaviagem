package boaviagem.casadocodigo.com.br.boaviagem.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import boaviagem.casadocodigo.com.br.boaviagem.domain.Spent;

/**
 * Created by adriano on 29/07/15.
 */
public class DAOSpent extends DAO {
    public DAOSpent(Context context) {
        super(context);
    }

    public int saveOrUpdate(Spent spent) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Spent.CATEGORY, spent.getCategory());
        values.put(DatabaseHelper.Spent.DESCRIPTION, spent.getDescription());
        values.put(DatabaseHelper.Spent.DATE, spent.getDate().getTime());
        values.put(DatabaseHelper.Spent.PLACE, spent.getLocal());
        values.put(DatabaseHelper.Spent.VALUE, spent.getValor());
        values.put(DatabaseHelper.Spent.TRAVEL_ID, spent.getTravel_id());

        if (spent.getId() == null) {
            return (int) getDb().insert(DatabaseHelper.Spent.TABLE, null, values);
        } else {
            return getDb().update(DatabaseHelper.Spent.TABLE, values, DatabaseHelper.Spent._ID + " = ?", new String[]{spent.getId().toString()});
        }
    }

    public void delete(String id) {
        String[] where = new String[]{id};

        getDb().delete(DatabaseHelper.Spent.TABLE, " where id = ?", where);
    }

    public List<Spent> listSpentByTravel(String id) {
        Cursor cursor = getDb().query(
                DatabaseHelper.Spent.TABLE,
                DatabaseHelper.Spent.COLUNAS,
                DatabaseHelper.Spent.TRAVEL_ID + " = ?",
                new String[] {id}, null, null, null);
        List<Spent> spents = new ArrayList<Spent>();

        while(cursor.moveToNext()) {
            Spent spent = bindSpent(cursor);
            spents.add(spent);
        }
        cursor.close();
        return spents;
    }

    private Spent bindSpent(Cursor cursor) {
        Spent spent = new Spent();
        spent.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.Spent._ID)));
        spent.setCategory(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Spent.CATEGORY)));
        spent.setDate(new Date(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.Spent.DATE))));
        spent.setValor(cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.Spent.VALUE)));
        spent.setDescription(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Spent.DESCRIPTION)));
        spent.setLocal(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Spent.PLACE)));
        spent.setTravel_id(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Spent.TRAVEL_ID)));

        return spent;
    }


}
