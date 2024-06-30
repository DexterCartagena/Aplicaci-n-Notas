package com.example.aplicacionnotas.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicacionnotas.Modelos.Notas;
import com.example.aplicacionnotas.NotesClickListener;
import com.example.aplicacionnotas.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class NotesListAdapter extends RecyclerView.Adapter<NotesViewHolder>{

    Context context;
    List<Notas> List;
    NotesClickListener Listener;

    public NotesListAdapter(Context context, List<Notas> list, NotesClickListener listener) {
        this.context = context;
        this.List = list;
        this.Listener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.note_list, parent, false));

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
    holder.textView_title.setText(List.get(position).getTitle());
    holder.textView_title.setSelected(true);

    holder.textView_notes.setText(List.get(position).getNotes());

    holder.textView_date.setText(List.get(position).getDate());
    holder.textView_date.setSelected(true);

    if(List.get(position).isPinned()){
        holder.imageView_pin.setImageResource(R.drawable.ic_ping);
    }else{
        holder.imageView_pin.setImageResource(0);
        }

    int color_code = getRandomColor();
    holder.notes_container.setCardBackgroundColor(holder.itemView.getResources().getColor(color_code, null));

    holder.notes_container.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Listener.onClick(List.get(holder.getAdapterPosition()));
        }
    });

    holder.notes_container.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            Listener.onLongClick(List.get(holder.getAdapterPosition()), holder.notes_container);
            return true;
        }
    });
    }



    private int getRandomColor(){
        List<Integer> colorCode = new ArrayList<>();

        colorCode.add(R.color.color1);
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);
        colorCode.add(R.color.color4);
        colorCode.add(R.color.color5);

        Random random = new Random();
        int random_color = random.nextInt(colorCode.size());
        return colorCode.get(random_color);

    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    public void filterList(List<Notas> filteredList){
        List = filteredList;
        notifyDataSetChanged();
    }
}

class NotesViewHolder extends RecyclerView.ViewHolder{

    CardView notes_container;
    TextView textView_title, textView_notes, textView_date;
    ImageView imageView_pin;
    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        notes_container = itemView.findViewById(R.id.notes_container);
        textView_title = itemView.findViewById(R.id.textView_title);
        textView_notes = itemView.findViewById(R.id.textView_notes);
        textView_date = itemView.findViewById(R.id.textView_date);
        imageView_pin = itemView.findViewById(R.id.imageView_pin);
    }
}