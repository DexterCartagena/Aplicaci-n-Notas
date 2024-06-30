package com.example.aplicacionnotas;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.aplicacionnotas.Adapters.NotesListAdapter;
import com.example.aplicacionnotas.Database.RoomDB;
import com.example.aplicacionnotas.Modelos.Notas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter;
    List<Notas> notes = new ArrayList<>();
    RoomDB database;
    FloatingActionButton fab_add;
    SearchView searView_home;
    Notas selectedNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         recyclerView = findViewById(R.id.recycler_home);
         fab_add = findViewById(R.id.fab_add);
        searView_home = findViewById(R.id.searView_home);

         database = RoomDB.getInstance(this);
         notes = database.mainDAO().getAll();

         updateRecycler(notes);

         fab_add.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
                 startActivityForResult(intent, 101);
             }
         });

         searView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
             @Override
             public boolean onQueryTextSubmit(String query) {
                 return false;
             }

             @Override
             public boolean onQueryTextChange(String newText) {
                 filter(newText);
                 return true;
             }
         });
    }

    private void filter(String newText) {
        List <Notas> FilteredList = new ArrayList<>();
        for(Notas singleNote : notes){
            if(singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
                    || singleNote.getNotes().toLowerCase().contains(newText.toLowerCase())){
                FilteredList.add(singleNote);
            }

        }
        notesListAdapter.filterList(FilteredList);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101){
            if(resultCode == Activity.RESULT_OK){
                Notas new_notes = (Notas) data.getSerializableExtra("note");
                database.mainDAO().insert(new_notes);
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
        }
        else if(requestCode==102){
            if(requestCode==Activity.RESULT_OK){
                Notas new_notes = (Notas) data.getSerializableExtra("note");
                database.mainDAO().update(new_notes.getID(), new_notes.getTitle(), new_notes.getNotes());
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycler(List<Notas> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapter = new NotesListAdapter(MainActivity.this, notes, notesClickListener);
        recyclerView.setAdapter(notesListAdapter);
    }

    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onClick(Notas notas) {
            Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
            intent.putExtra("old_note", notas);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Notas notas, CardView cardView) {
            selectedNotes = new Notas();
            selectedNotes = notas;
            showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.pin:
                if(selectedNotes.isPinned()){
                    database.mainDAO().pin(selectedNotes.getID(), false);
                    Toast.makeText(MainActivity.this, "Desanclar", Toast.LENGTH_SHORT).show();
                }else{
                    database.mainDAO().pin(selectedNotes.getID(), true);
                    Toast.makeText(MainActivity.this, "Anclar", Toast.LENGTH_SHORT).show();
                }

                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
                return true;

            case R.id.delete:
                database.mainDAO().delete(selectedNotes);
                notes.remove(selectedNotes);
                notesListAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Nota Eliminada!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }
}