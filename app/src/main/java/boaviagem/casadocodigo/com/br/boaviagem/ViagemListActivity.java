package boaviagem.casadocodigo.com.br.boaviagem;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViagemListActivity extends ListActivity implements OnItemClickListener, DialogInterface.OnClickListener {
    private List<Map<String, Object>> viagens;
    private AlertDialog alertDialog;
    private int viagemSelecionada;

    @Override
    public void onClick(DialogInterface dialog, int item) {
        switch (item) {
            case 0:
                startActivity(new Intent(this, ViagemActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, GastoActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, GastoListActivity.class));
                break;
            case 3:
                viagens.remove(this.viagemSelecionada);
                getListView().invalidateViews();
                break;
        }
    }

    private AlertDialog criarAlertDialog() {
        final CharSequence[] items = {
                getString(R.string.edit),
                getString(R.string.new_spent),
                getString(R.string.list_spent),
                getString(R.string.remove),
                };

        AlertDialog.Builder builder =  new AlertDialog.Builder(this);
        builder.setTitle(R.string.options);
        builder.setItems(items, this);

        return builder.create();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] de = {"imagem", "destino", "data", "total"};
        int[] para = {R.id.tipoViagem, R.id.destino, R.id.data, R.id.valor};

        SimpleAdapter adapter =
                new SimpleAdapter(this, listarViagens(),
                        R.layout.lista_viagem, de, para);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        this.alertDialog = criarAlertDialog();
    }

    private List<Map<String, Object>> listarViagens() {
        viagens = new ArrayList<Map<String, Object>>();

        Map<String, Object> item = new HashMap<String, Object>();
        item.put("imagem", R.drawable.negocios);
        item.put("destino", "Sao Paulo");
        item.put("data", "02/02/2012 a 04/02/2012");
        item.put("total", "Gasto total R$ 314,98");
        viagens.add(item);

        item = new HashMap<String, Object>();
        item.put("imagem", R.drawable.lazer);
        item.put("destino", "Maceio");
        item.put("data", "14/05/2012 a 22/05/2012");
        item.put("total", "Gasto total R$ 25834,67");
        viagens.add(item);

        return viagens;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        this.viagemSelecionada = position;
        alertDialog.show();
    }
}
