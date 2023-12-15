package com.example.mutilnotes.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mutilnotes.R;
import com.example.mutilnotes.activities.AddNoteActivity;
import com.example.mutilnotes.activities.DetailNoteActivity;
import com.example.mutilnotes.activities.MainActivity;
import com.example.mutilnotes.dao.NoteRepo;
import com.example.mutilnotes.domains.Note;

import java.util.List;
import java.util.Objects;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.viewHolder> {

    List<Note> items;

    Context context;

    public NoteAdapter(List<Note> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public NoteAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item_view, parent, false);
        this.context = parent.getContext();
        return new viewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.viewHolder holder, int position) {
        holder.date.setText(items.get(position).getCreate());
        holder.title.setText(items.get(position).getTitle());
        holder.content.setText(items.get(position).getContent());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailNoteActivity.class);
                intent.putExtra("note_id", items.get(position).getId());
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu menu = new PopupMenu(context, v);
                menu.getMenu().add("Delete");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(Objects.equals(item.getTitle(), "Delete")) {
                            NoteRepo noteRepo = NoteRepo.getInstance();
                            noteRepo.delete(context, items.get(position).getId());
                            Toast.makeText(context,"Note deleted",Toast.LENGTH_SHORT).show();
                            items.remove(position);
                            notifyItemRemoved(position);
                        }
                        return true;
                    }
                });
                menu.show();
                return true;
            }
        });
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView date, title, content;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.itemNoteDate);
            title = itemView.findViewById(R.id.itemNoteTitle);
            content = itemView.findViewById(R.id.itemNoteContent);
        }
    }
}
