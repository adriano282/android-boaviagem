package boaviagem.casadocodigo.com.br.boaviagem.view;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boaviagem.casadocodigo.com.br.boaviagem.R;
import boaviagem.casadocodigo.com.br.boaviagem.dao.DAOSpent;
import boaviagem.casadocodigo.com.br.boaviagem.domain.Constantes;
import boaviagem.casadocodigo.com.br.boaviagem.domain.Spent;

/**
 * Created by adriano on 24/07/15.
 */
public class GastoListActivity extends ListActivity implements AdapterView.OnItemClickListener {
    private List<Map<String, Object>> gastos;
    private String travelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] de = {"data", "descricao", "valor", "categoria"};
        int[] para = {R.id.data, R.id.description, R.id.valor, R.id.categoria };
        travelId = getIntent().getExtras().getString(Constantes.VIAGEM_ID);

        SimpleAdapter adapter = new SimpleAdapter(this,
                listarGastos(travelId), R.layout.lista_gasto, de, para);

        adapter.setViewBinder(new GastoViewBinder());
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        //Registramos aqui o novo menu de contexto
        registerForContextMenu(getListView());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_gasto, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.remover) {
            DAOSpent dao = new DAOSpent(this);
            AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                    .getMenuInfo();
            gastos.remove(info.position);
            getListView().invalidateViews();
            dataAnterior = "";
            //
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        Map<String, Object> map = gastos.get(position);
        String descricao = (String) map.get("descricao");
        String mensagem = "Gasto selecionado: " + descricao;
        Toast.makeText(this, mensagem,
                Toast.LENGTH_SHORT).show();
    }


    private List<Map<String, Object>> listarGastos(String id) {

        DAOSpent dao = new DAOSpent(this);

        List<Spent> spents = dao.listSpentByTravel(travelId);

        gastos = new ArrayList<Map<String, Object>>();
        Map<String, Object> item = new HashMap<String, Object>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (int i = 0; i < spents.size(); i++) {
            item = new HashMap<String, Object>();
            Spent current = spents.get(i);

            item.put("data", dateFormat.format(current.getDate().getTime()));
            item.put("descricao", current.getDescription());
            item.put("valor", "R$ " + current.getValor());
            item.put("categoria", current.getCategory());
            gastos.add(item);
        }
        return gastos;
    }

    private String dataAnterior = "";

    private class GastoViewBinder implements ViewBinder {

        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {

            if (view.getId() == R.id.data) {
                if (!dataAnterior.equals(data)) {
                    TextView textView = (TextView) view;
                    textView.setText(textRepresentation);
                    dataAnterior = textRepresentation;
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
                return true;
            }

            if (view.getId() == R.id.categoria) {

                if (data.equals("Combustível")) {
                    view.setBackgroundColor(getResources().getColor(R.color.categoria_outros));
                } else if (data.equals("Alimentação")) {
                    view.setBackgroundColor(getResources().getColor(R.color.categoria_alimentacao));
                } else if (data.equals("Transporte")) {
                    view.setBackgroundColor(getResources().getColor(R.color.categoria_outros));
                } else if (data.equals("Hospedagem")) {
                    view.setBackgroundColor(getResources().getColor(R.color.categoria_hospedagem));
                } else if (data.equals("Outros")) {
                    view.setBackgroundColor(getResources().getColor(R.color.categoria_outros));
                }
                return true;
            }
            return false;
        }
    }
}
