package boaviagem.casadocodigo.com.br.boaviagem.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import boaviagem.casadocodigo.com.br.boaviagem.R;
import boaviagem.casadocodigo.com.br.boaviagem.domain.Note;

/**
 * Created by adriano on 07/09/15.
 */
public class NoteFragment extends Fragment implements OnClickListener {
    private EditText day, title, description;
    private Button saveButton;
    private Note note;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_note, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        day = (EditText) getActivity().findViewById(R.id.day);
        title = (EditText) getActivity().findViewById(R.id.title);
        description =
                (EditText) getActivity().findViewById(R.id.description);
        saveButton = (Button) getActivity().findViewById(R.id.save);
        saveButton.setOnClickListener(this);

        if (note != null) {
            prepareForEdition(note);
        }
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public void prepareForEdition(Note note) {
        setNote(note);
        day.setText(note.getDay().toString());
        title.setText(note.getTitle());
        description.setText(note.getDescription());
    }

    @Override
    public void onClick(View view) {

    }

    public void createNewNote() {
        note = new Note();
        day.setText("");
        title.setText("");
        description.setText("");
    }

}
