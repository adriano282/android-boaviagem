package boaviagem.casadocodigo.com.br.boaviagem.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import java.util.Arrays;
import java.util.List;

import boaviagem.casadocodigo.com.br.boaviagem.NoteListener;
import boaviagem.casadocodigo.com.br.boaviagem.domain.Constantes;

/**
 * Created by adriano on 07/09/15.
 */
public class TravelListFragment extends ListFragment implements OnItemClickListener {
    private NoteListener callBack;

    /**
     * Called before the view to be created, so we can't
     * access the components of screen
     */
    @Override
    public void onCreate(Bundle saved ) {
        super.onCreate(saved);
    }

    /**
     * As we can't use the onCreate method for
     * manipulating components, we'll use the
     * onStart method, because it is called when
     * the fragment already to show off, therefore
     * we can handle the screen's components
     */
    @Override
    public void onStart() {
        super.onStart();
        List<String> travels = Arrays.asList(
                "Pale Alto", "San Francisco", "Silicon Valley");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String> (getActivity(),
                        android.R.layout.simple_list_item_1, travels);

        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent,
                            View view, int position,
                            long id) {
        String travel = (String) getListAdapter().getItem(position);
        Bundle bundle = new Bundle();
        bundle.putString(Constantes.VIAGEM_SELECIONADA, travel);
        callBack.travelSelected(bundle);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callBack = (NoteListener) activity;
    }
}
