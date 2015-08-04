package boaviagem.casadocodigo.com.br.boaviagem.provider;

import android.net.Uri;

/**
 * Created by adriano on 04/08/15.
 */
public class BoaViagemContract {
    public static final String AUTHORITY = "br.com.casadocodigo.boaviagem.provider";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
    public static final String VIAGEM_PATH = "viagem";
    public static final String GASTO_PATH = "gasto";

    public static final class Travel {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, VIAGEM_PATH);
        public static final String _ID = "_id";
        public static final String DESTINY = "destino";
        public static final String DATE_ARRIVE = "data_chegada";
        public static final String DATE_OUT = "data_saida";
        public static final String BUDGET = "orcamento";
        public static final String QUANTITY_PERSONS = "quantidade_pessoas";
        public static final String TYPE_TRAVEL = "tipo_viagem";
    }

    public static final class Spent {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, GASTO_PATH);
        public static final String _ID = "_id";
        public static final String TRAVEL_ID = "viagem_id";
        public static final String CATEGORY = "categoria";
        public static final String DATE = "data";
        public static final String DESCRIPTION = "descricao";
        public static final String VALUE = "valor";
        public static final String PLACE = "local";
    }

}
