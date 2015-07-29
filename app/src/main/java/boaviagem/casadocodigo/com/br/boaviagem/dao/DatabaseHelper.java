package boaviagem.casadocodigo.com.br.boaviagem.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by adriano on 26/07/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE = "GoodTravel";
    private static int VERSION = 1;

    public static class Travel {
        public static final String TABLE = "viagem";
        public static final String _ID = "_id";
        public static final String DESTINY = "destino";
        public static final String DATE_ARRIVE = "data_chegada";
        public static final String DATE_OUT = "data_saida";
        public static final String BUDGET = "orcamento";
        public static final String QUANTITY_PERSONS = "quantidade_pessoas";
        public static final String TYPE_TRAVEL = "tipo_viagem";
        public static final String[] COLUNAS = new String[]{
                _ID, DESTINY, DATE_ARRIVE, DATE_OUT, TYPE_TRAVEL, BUDGET, QUANTITY_PERSONS};

    }
    public static class Spent {
        public static final String
                TABLE = "spent",
                _ID = "_id",
                TRAVEL_ID = "viagem_id",
                CATEGORY = "categoria",
                DATE = "data",
                DESCRIPTION = "descricao",
                VALUE = "valor",
                PLACE = "local";
        public static final String[] COLUNAS = new String[] {
                _ID, TRAVEL_ID, CATEGORY, DATE, DESCRIPTION, VALUE, PLACE};
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE viagem (_id INTEGER PRIMARY KEY," +
                " destino TEXT, tipo_viagem INTEGER, data_chegada DATE," +
                " data_saida DATE, orcamento DOUBLE," +
                " quantidade_pessoas INTEGER);");
        db.execSQL("CREATE TABLE gasto (_id INTEGER PRIMARY KEY," +
                " categoria TEXT, data DATE, valor DOUBLE," +
                " descricao TEXT, local TEXT, viagem_id INTEGER," +
                " FOREIGN KEY(viagem_id) REFERENCES viagem(_id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("ALTER TABLE gasto ADD COLUMN pessoa TEXT");
    }
}
