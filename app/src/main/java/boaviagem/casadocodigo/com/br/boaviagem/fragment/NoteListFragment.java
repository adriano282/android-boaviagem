package boaviagem.casadocodigo.com.br.boaviagem.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import boaviagem.casadocodigo.com.br.boaviagem.NoteListener;
import boaviagem.casadocodigo.com.br.boaviagem.R;
import boaviagem.casadocodigo.com.br.boaviagem.domain.Constantes;
import boaviagem.casadocodigo.com.br.boaviagem.domain.Note;

/**
 * Created by adriano on 07/09/15.
 */
public class NoteListFragment extends ListFragment
        implements OnItemClickListener, OnClickListener {
    private NoteListener callback;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notes_list, container, false);
    }

    /**
     * Called when view already to show off
     */
    @Override
    public void onStart() {
        super.onStart();

        /* Get notes */
        List<Note> notes = listNotes();

        ArrayAdapter<Note> adapter =
                new ArrayAdapter<Note> (getActivity(),
                        android.R.layout.simple_list_item_1,
                        notes);

        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        Button button = (Button) getActivity().findViewById(R.id.new_note);
        button.setOnClickListener(this);
        listNotesByTravel(getArguments());

    }

    @Override
    public void onItemClick(AdapterView<?> parent,
                            View view, int position,
                            long id) {
        Note note = (Note) getListAdapter().getItem(position);
        callback.noteSelected(note);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (NoteListener) activity;
    }

    @Override
    public void onClick(View v) {
        callback.newNote();
    }

    /**
     * Method for simulating a database of notes
     * @return
     */
    private List<Note> listNotes() {
        List<Note> notes = new ArrayList<Note>();

        for (int i = 1; i < 20; i++) {
            Note note = new Note();
            note.setDay(i);
            note.setTitle("Note " + i);
            note.setDescription("Description " + i);
            notes.add(note);
        }
        return notes;
    }

    public void listNotesByTravel(Bundle bundle) {
        if (bundle != null &&
                bundle.containsKey(Constantes.VIAGEM_SELECIONADA)) {
            // An arraylist just to populate de listView
            List<Note> notes = listNotes();

            ArrayAdapter<Note> adapter =
                    new ArrayAdapter<Note> (getActivity(),
                            android.R.layout.simple_list_item_1,
                            notes);
            setListAdapter(adapter);
        }
    }
}
