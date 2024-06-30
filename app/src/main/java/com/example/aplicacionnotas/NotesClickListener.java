package com.example.aplicacionnotas;

import androidx.cardview.widget.CardView;

import com.example.aplicacionnotas.Modelos.Notas;

public interface NotesClickListener {
    void onClick(Notas notas);
    void onLongClick(Notas notas, CardView cardView);
}
