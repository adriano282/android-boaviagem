package boaviagem.casadocodigo.com.br.boaviagem.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import boaviagem.casadocodigo.com.br.boaviagem.dao.DatabaseHelper;

import static boaviagem.casadocodigo.com.br.boaviagem.provider.BoaViagemContract.*;

/**
 * Created by adriano on 04/08/15.
 */
public class BoaViagemProvider extends ContentProvider {
    private static final int VIAGENS = 1;
    private static final int VIAGEM_ID = 2;
    private static final int GASTOS = 3;
    private static final int GASTO_ID = 4;
    private static final int GASTOS_VIAGEM_ID = 5;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, VIAGEM_PATH, VIAGENS);
        uriMatcher.addURI(AUTHORITY, VIAGEM_PATH + "/#", VIAGEM_ID);
        uriMatcher.addURI(AUTHORITY, GASTO_PATH, GASTOS);
        uriMatcher.addURI(AUTHORITY, GASTO_PATH + "/#", GASTO_ID);
        uriMatcher.addURI(AUTHORITY, GASTO_PATH + "/" + VIAGEM_PATH + "/#", GASTOS_VIAGEM_ID);
    }

    private DatabaseHelper helper;

    @Override
    public boolean onCreate() {
        helper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = helper.getReadableDatabase();

        switch (uriMatcher.match(uri)) {
            case VIAGENS:
                return database.query(VIAGEM_PATH, projection,
                        selection, selectionArgs, null, null, sortOrder);
            case VIAGEM_ID:
                selection = Travel._ID + " = ?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                return database.query(VIAGEM_PATH, projection, selection,
                        selectionArgs, null, null, sortOrder);
            case GASTOS:
                return database.query(GASTO_PATH, projection, selection,
                        selectionArgs, null, null, sortOrder );
            case GASTO_ID:
                selection = Spent._ID + " = ?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                return database.query(GASTO_PATH, projection, selection,
                        selectionArgs, null, null, sortOrder );
            case GASTOS_VIAGEM_ID:
                selection = Spent.TRAVEL_ID + " = ?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                return database.query(GASTO_PATH, projection, selection,
                        selectionArgs, null, null, sortOrder );
            default:
                throw new IllegalArgumentException("Unknow URI");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection,
                      String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}
