package boaviagem.casadocodigo.com.br.boaviagem.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import boaviagem.casadocodigo.com.br.boaviagem.domain.Travel;

/**
 * Created by adriano on 29/07/15.
 */
public class DAOTravel extends DAO {

    public DAOTravel(Context context) {
        super(context);
    }

    public List<Travel> listTravels() {
        Cursor cursor = getDb().query(
                DatabaseHelper.Travel.TABLE,
                DatabaseHelper.Travel.COLUNAS,
                null, null, null, null, null);
        List<Travel> travels = new ArrayList<Travel>();

        while(cursor.moveToNext()) {
            Travel travel = bindTravel(cursor);
            travels.add(travel);
        }
        cursor.close();
        return travels;
    }

    public Travel getTravelById(String id) {
        try {
            return getTravelById(Long.parseLong(id));
        } catch (Exception e) {
            return null;
        }
    }

    public Travel getTravelById(Long id) {
        Cursor cursor =
                getDb().query(DatabaseHelper.Travel.TABLE,
                        DatabaseHelper.Travel.COLUNAS,
                        "where " + DatabaseHelper.Travel._ID + " = ?" ,
                        new String[] {id.toString()}, null, null, null);

        Travel travel = bindTravel(cursor);
        close();
        return travel;
    }

    public int saveOrUpdate(Travel travel) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Travel.DESTINY, travel.getDestiny());
        values.put(DatabaseHelper.Travel.DATE_ARRIVE, travel.getDateArrive().getTime());
        values.put(DatabaseHelper.Travel.DATE_OUT, travel.getDateOut().getTime());
        values.put(DatabaseHelper.Travel.BUDGET, travel.getBudget());
        values.put(DatabaseHelper.Travel.QUANTITY_PERSONS, travel.getQuantityPersons());
        values.put(DatabaseHelper.Travel.TYPE_TRAVEL, travel.getTypeTravel());

        if (travel.getId() == null) {
            return (int) getDb().insert(DatabaseHelper.Travel.TABLE, null, values);
        } else {
            return getDb().update(DatabaseHelper.Travel.TABLE, values, DatabaseHelper.Travel._ID + " = ?", new String[]{travel.getId().toString()});
        }
    }

    public void delete(String id) {
        String where [] = new String[]{ id };

        getDb().delete("gasto", "viagem_id = ?", where);
        getDb().delete("viagem", "_id = ?", where);
    }

    public double getSumSpentById(Long id) {
        Cursor cursor =
                getDb().rawQuery(
                "select sum(valor) from gasto where viagem_id = ?",
                new String[] {id.toString()});
        cursor.moveToFirst();
        double total = cursor.getDouble(0);
        close();
        return total;
    }


    private Travel bindTravel(Cursor cursor) {
        Travel travel = new Travel();
        travel.setId(cursor.getLong(0));
        travel.setDestiny(cursor.getString(1));
        travel.setTypeTravel(cursor.getInt(2));
        travel.setDateArrive(new Date(cursor.getLong(3)));
        travel.setDateOut(new Date(cursor.getLong(4)));
        travel.setBudget(cursor.getDouble(5));
        travel.setQuantityPersons(cursor.getInt(6));

        return travel;
    }
}
