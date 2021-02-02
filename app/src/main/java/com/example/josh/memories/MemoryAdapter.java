package com.example.josh.memories;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder> {

    private List<Memory> memoryList;
    private Context context;
    public MemoryData db;

    private static final String DEBUG_TAG = "MemoryAdapter";

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date, description;
        public ImageView image;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            date = (TextView) view.findViewById(R.id.date);
            description = (TextView) view.findViewById(R.id.description);
            image = (ImageView) view.findViewById(R.id.image_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pair<View, String> p1 = Pair.create((View) title, MemoryList.TRANSITION_TITLE);
                    Pair<View, String> p2 = Pair.create((View) date, MemoryList.TRANSITION_DATE);
                    Pair<View, String> p3 = Pair.create((View) description, MemoryList.TRANSITION_DESCRIPTION);

                    ActivityOptionsCompat options;
                    AppCompatActivity act = (AppCompatActivity) context;
                    options = ActivityOptionsCompat.makeSceneTransitionAnimation(act, p1, p2, p3);

                    int requestCode = getAdapterPosition();
                    String title = memoryList.get(requestCode).getTitle();
                    String date = memoryList.get(requestCode).getDate();
                    String description = memoryList.get(requestCode).getDescription();
                    Log.d(DEBUG_TAG, "send to details title=" + title);
                    Log.d(DEBUG_TAG, "send to details date=" + date);
                    Log.d(DEBUG_TAG, "send to details description=" + description);


                    Log.d(DEBUG_TAG, "SampleMaterialAdapter itemView listener for Edit adapter position " + requestCode);

                    Intent transitionIntent = new Intent(context, MemoryDetails.class);
                    transitionIntent.putExtra(MemoryList.TITLE, title);
                    transitionIntent.putExtra(MemoryList.DATETIME, date);
                    transitionIntent.putExtra(MemoryList.DESCRIPTION, description);
                    transitionIntent.putExtra("update", MemoryList.RESULT_SAVE);
                    ((AppCompatActivity) context).startActivityForResult(transitionIntent, requestCode, options.toBundle());
                }
            });

        }
    }


    public MemoryAdapter(Context context, ArrayList<Memory> memoryList, MemoryData db) {
        this.memoryList = memoryList;
        this.context = context;
        this.db = db;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.memory_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = memoryList.get(position).getTitle();
        String date = memoryList.get(position).getDate();
        String description = memoryList.get(position).getDescription();
        TextView titleTextView = holder.title;
        TextView dateTextView = holder.date;
        TextView descriptionTextView = holder.description;
        titleTextView.setText(title);
        dateTextView.setText(date);
        descriptionTextView.setText(description);
    }

    public void addMemory(String title, String date, String description, byte[] byteArray) {
        Memory memory = new Memory();
        memory.setTitle(title);
        memory.setDate(date);
        memory.setDescription(description);
        memory.setImage(byteArray);
        new CreateMemoryTask().execute(memory);
    }

    public void updateMemory(String title, String date, String description, int list_position) {
        Memory memory = new Memory();
        memory.setTitle(title);
        memory.setDescription(description);
        memory.setDate(date);
        memory.setId(getItemId(list_position));
        memory.setListPosition(list_position);
        new UpdateMemoryTask().execute(memory);
    }

    public void deleteMemory(View view, int list_position) {
        Memory memory = new Memory();
        memory.setId(getItemId(list_position));
        memory.setListPosition(list_position);
        new DeleteMemoryTask().execute(memory);
    }

    @Override
    public int getItemCount() {
        if (memoryList.isEmpty()) {
            return 0;
        } else {
            return memoryList.size();
        }
    }

    @Override
    public long getItemId(int position) {
        return memoryList.get(position).getId();
    }

    private class CreateMemoryTask extends AsyncTask<Memory, Void, Memory> {
        @Override
        protected Memory doInBackground(Memory... memory) {
            db.create(memory[0]);
            memoryList.add(memory[0]);
            return memory[0];
        }

        @Override
        protected void onPostExecute(Memory memory) {
            super.onPostExecute(memory);
            ((MemoryList) context).doSmoothScroll(getItemCount() - 1);
            notifyItemInserted(getItemCount());
            Log.d(DEBUG_TAG, "Card created with id " + memory.getId() + ", title " + memory.getTitle() + ", date " + memory.getDate() + ", description " + memory.getDescription());
        }
    }

    private class UpdateMemoryTask extends AsyncTask<Memory, Void, Memory> {
        @Override
        protected Memory doInBackground(Memory... memory) {
            db.update(memory[0].getId(), memory[0].getTitle(), memory[0].getDate(), memory[0].getDescription(), memory[0].getImage());
            memoryList.get(memory[0].getListPosition()).setTitle(memory[0].getTitle());
            memoryList.get(memory[0].getListPosition()).setDate(memory[0].getDate());
            memoryList.get(memory[0].getListPosition()).setDescription(memory[0].getDescription());
            Log.d(DEBUG_TAG, "IN UpdateMemoryTask memory[0] =" + memory[0]);
            Log.d(DEBUG_TAG, "IN UpdateMemoryTask memory[0].getDescription =" + memory[0].getDescription());
            Log.d(DEBUG_TAG, "IN UpdateMemoryTask memory[0].getDate =" + memory[0].getDate());

            return memory[0];
        }

        @Override
        protected void onPostExecute(Memory memory) {
            super.onPostExecute(memory);
            Log.d(DEBUG_TAG, "list_position is " + memory.getListPosition());
            notifyItemChanged(memory.getListPosition());
        }
    }

    private class DeleteMemoryTask extends AsyncTask<Memory, Void, Memory> {
        @Override
        protected Memory doInBackground(Memory... memory) {
            db.delete(memory[0].getId());
            memoryList.remove(memory[0].getListPosition());
            return memory[0];
        }

        @Override
        protected void onPostExecute(Memory memory) {
            super.onPostExecute(memory);
            notifyItemRemoved(memory.getListPosition());
        }
    }
}

