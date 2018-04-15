package com.example.karag.notepad;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class NoteActivity extends AppCompatActivity {


    private EditText mEdTitle;
    private EditText mEdContent;
    private boolean mIsViewingOrUpdating;
    private String mNoteFileName;


    private Note mLoadedNote = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mEdTitle = (EditText)findViewById(R.id.note_et_title);
        mEdContent = (EditText)findViewById(R.id.edContent);

        mNoteFileName = getIntent().getStringExtra(Utilited.EXTRAS_NOTE_FILENAME);
        if(mNoteFileName != null && !mNoteFileName.isEmpty()){
            mLoadedNote = Utilited.getNoteByName(getApplicationContext(),mNoteFileName);

            if (mLoadedNote!=null){
                mEdTitle.setText(mLoadedNote.getTitle());
                mEdContent.setText(mLoadedNote.getContent());
                mIsViewingOrUpdating = true;
            }
            else{
                mIsViewingOrUpdating = true;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_new,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_main_new_note:
                saveNote();
                break;
            case R.id.action_main_new_delete:
                actionDelete();
                break;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        actionCancel();
    }

    //Saving note
    private void saveNote() {
        Note note;
        //if is empty
        if(mEdTitle.getText().toString().trim().isEmpty() || mEdContent.getText().toString().trim().isEmpty()){
            Toast.makeText(this,"Ну не... Так дела не ведут",Toast.LENGTH_LONG).show();
            return;
        }
        if(mLoadedNote == null){
            note = new Note(System.currentTimeMillis(), mEdTitle.getText().toString(),
                    mEdContent.getText().toString());
        }
        else{
            note = new Note(mLoadedNote.getDateTime(), mEdTitle.getText().toString(),
                    mEdContent.getText().toString());
        }
        //if all allright
        if(Utilited.saveNote(this, note)){
            Toast.makeText(NoteActivity.this,"Hello darkness me old friend", Toast.LENGTH_LONG).show();
            finish();
        }
        else{
            Toast.makeText(this,"hello lightness my old friend",Toast.LENGTH_LONG).show();
        }
        finish();
    }
    public void actionDelete() {
        //ask user if he really wants to delete the note!
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(this)
                .setTitle("Удалить?")
                .setMessage("Ты рили хочешь удалить свою запись??")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mLoadedNote != null && Utilited.deleteFile(getApplicationContext(),mNoteFileName)) {
                            Toast.makeText(NoteActivity.this, mLoadedNote.getTitle() + " Удалено"
                                    , Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(NoteActivity.this, "Невозможно удалить" + mLoadedNote.getTitle() + "'"
                                    , Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                })
                .setNegativeButton("NO", null); //do nothing on clicking NO button :P

        dialogDelete.show();
    }
    private void actionCancel() {
        if(!checkNoteAltred()) { //if note is not altered by user (user only viewed the note/or did not write anything)
            finish(); //just exit the activity and go back to MainActivity
        } else { //we want to remind user to decide about saving the changes or not, by showing a dialog
            AlertDialog.Builder dialogCancel = new AlertDialog.Builder(this)
                    .setTitle("discard changes...")
                    .setMessage("are you sure you do not want to save changes to this note?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveNote();
                            finish();
                        }
                    })
                    .setNegativeButton("NO", null); //null = stay in the activity!
            dialogCancel.show();
        }
    }
    private boolean checkNoteAltred() {
        if(mIsViewingOrUpdating) { //if in view/update mode
            return mLoadedNote != null && (!mEdTitle.getText().toString().equalsIgnoreCase(mLoadedNote.getTitle())
                    || !mEdContent.getText().toString().equalsIgnoreCase(mLoadedNote.getContent()));
        } else { //if in new note mode
            return !mEdTitle.getText().toString().isEmpty() || !mEdContent.getText().toString().isEmpty();
        }
    }
}
