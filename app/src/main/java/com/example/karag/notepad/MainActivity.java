package com.example.karag.notepad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_list_view_notes)
    public ListView mListViewNotes;
    private GestureDetectorCompat gd;
    public NoteActivity noteActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.float_Button)
    public void onFabClicked(View v) {
        startActivity(new Intent(MainActivity.this, NoteActivity.class));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    protected void onResume() {

        super.onResume();
        //load saved notes into the listview
        //first, reset the listview
        mListViewNotes.setAdapter(null);
        ArrayList<Note> notes = Utilited.getAllSavedNotes(getApplicationContext());
        //sort notes from new to old
        Collections.sort(notes, new Comparator<Note>() {

            @Override

            public int compare(Note lhs, Note rhs) {
                if (lhs.getDateTime() > rhs.getDateTime()) {
                    return -1;
                } else {
                    return 1;
                }
            }

        });
        if (notes != null && notes.size() > 0) { //check if we have any notes!
            final NoteAdapter na = new NoteAdapter(this, R.layout.item_note, notes);
            mListViewNotes.setAdapter(na);

            //set click listener for items in the list, by clicking each item the note should be loaded into NoteActivity

            mListViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //run the NoteActivity in view/edit mode
                    String fileName = ((Note) mListViewNotes.getItemAtPosition(position)).getDateTime()
                            + Utilited.FILE_EXTENSION;
                    Intent viewNoteIntent = new Intent(getApplicationContext(), NoteActivity.class);
                    viewNoteIntent.putExtra(Utilited.EXTRAS_NOTE_FILENAME, fileName);
                    startActivity(viewNoteIntent);

                }
            });
        } else { //remind user that we have no notes!
            Toast.makeText(getApplicationContext(), "У вас нет сохранённых заметок\nсоздайте же их! :)"
                    , Toast.LENGTH_SHORT).show();
        }
    }

}

