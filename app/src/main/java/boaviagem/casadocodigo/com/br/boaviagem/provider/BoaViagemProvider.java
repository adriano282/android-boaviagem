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
    private static final int VIAGENS = 1;           // Insere ou pesquisa viagens
    private static final int VIAGEM_ID = 2;         // Atualizar ou remover viagem
    private static final int GASTOS = 3;            // Pesquisa gasto
    private static final int GASTO_ID = 4;          // Atualiza ou remove gasto
    private static final int GASTOS_VIAGEM_ID = 5;  // Pesquisar gastos de uma viagem

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
        SQLiteDatabase database = helper.getWritableDatabase();
        long id;

        switch (uriMatcher.match(uri)) {
            case GASTOS:
                id = database.insert(GASTO_PATH, null, values);
                return uri.withAppendedPath(Spent.CONTENT_URI,
                        String.valueOf(id));
            case VIAGENS:
                id = database.insert(VIAGEM_PATH, null, values);
                return uri.withAppendedPath(Travel.CONTENT_URI,
                        String.valueOf(id));
            default:
                throw new IllegalArgumentException("Unknow URI");
        }
    }

    @Override
    public int delete(Uri uri, String selection,
                      String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase database = helper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case VIAGEM_ID:
                selection = Travel._ID + " = ?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                return database.update(VIAGEM_PATH, values, selection, selectionArgs);
            case GASTOS:
                selection = Spent._ID + " = ?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                return database.update(GASTO_PATH, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Unknow URI");
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}
