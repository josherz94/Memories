package com.example.josh.memories;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import java.util.ArrayList;


public class MemoryList extends AppCompatActivity {

    public static final String DELETE="delete";
    public static final String UPDATE="update";
    public static final String TITLE="title";
    public static final String DESCRIPTION="Description";
    public static final String DATETIME= "datetime";
    public static final String DEBUG_TAG="MainActivity";
    public static final String TRANSITION_TITLE ="transition_title";
    public static final String TRANSITION_DATE = "transition_date";
    public static final String TRANSITION_DESCRIPTION ="transition_description";
    public static final int RESULT_ADD=2;
    public static final int RESULT_DELETE=3;
    public static final int RESULT_SAVE=4;


    private ArrayList<Memory> memoryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MemoryAdapter mAdapter;
    private String[] titles, descriptions, dates;
    private long[] ids;
    public MemoryData db =new MemoryData(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_list);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new MemoryAdapter(MemoryList.this, memoryList, db);
        recyclerView.setAdapter(mAdapter);


        new GetOrCreatememoryListTask().execute();
        Log.d(DEBUG_TAG, "IN CREATE");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity act = MemoryList.this;

                Intent i = new Intent(act, AddMemory.class);
                act.startActivityForResult(i, mAdapter.getItemCount());
            }
        });
    }

    private void removeItem(long id) {
        memoryList.remove(id);
        recyclerView.removeViewAt((int) id);
        mAdapter.notifyItemRemoved((int) id);
        mAdapter.notifyItemRangeChanged((int) id, memoryList.size());

    }


    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
        db.open();
        Log.d(DEBUG_TAG, "cardsData opened in onResume()");
    }


    @Override
    protected void onPause() {
        super.onPause();
        db.close();
        Log.d(DEBUG_TAG, "cardsData closed in onPause()");
    }

    public void doSmoothScroll(int position) {
        recyclerView.smoothScrollToPosition(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        db.open();
        Log.d(DEBUG_TAG, "requestCode is " + requestCode);
        // if adapter.getItemCount() is request code, that means we are adding a new position
        // anything less than adapter.getItemCount() means we are editing a particular position
            if (resultCode == RESULT_ADD) {
                // Make sure the Add request was successful
                // if add name, update name list
                String title = data.getStringExtra("add_title");
                String date = data.getStringExtra("add_date");
                String description = data.getStringExtra("add_description");
                byte[] byteArray = data.getByteArrayExtra("add_picture");
                int newPosition = mAdapter.getItemCount();
                Log.d(DEBUG_TAG, "newPosition = " + newPosition);
                mAdapter.addMemory(title, date, description, byteArray);
                new GetOrCreatememoryListTask().execute();
            }
            if(resultCode == RESULT_DELETE) {
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(requestCode);
                mAdapter.deleteMemory(viewHolder.itemView, requestCode);
            }
            if(resultCode == RESULT_SAVE) {
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(requestCode);
                String title = data.getStringExtra(TITLE);
                String date = data.getStringExtra(DATETIME);
                String description = data.getStringExtra(DESCRIPTION);
                viewHolder.itemView.setVisibility(View.INVISIBLE);
                mAdapter.updateMemory(title, date, description, requestCode);
            }
        }



    public class GetOrCreatememoryListTask extends AsyncTask<Void, Void, ArrayList<Memory>> {
        @Override
        protected ArrayList<Memory> doInBackground(Void... params) {
            db.open();
            memoryList = db.getAll();
            if (memoryList.size() != 0) {
                for (int i = 0; i < 50; i++) {
                    Memory memory = new Memory();
                    memory.setId(ids[i]);
                    memory.setTitle(titles[i]);
                    memory.setDate(dates[i]);
                    memory.setDescription(descriptions[i]);
                    memoryList.add(memory);
                    db.create(memory);
                    Log.d(DEBUG_TAG, "Card created with id " + memory.getId() + ", name " + memory.getTitle() + ", date " + memory.getDate() + ", description " + memory.getDescription());
                }
            }
            return memoryList;
        }

        @Override
        protected void onPostExecute(ArrayList<Memory> memory) {
            super.onPostExecute(memory);
            mAdapter = new MemoryAdapter(MemoryList.this, memoryList, db);
            recyclerView.setAdapter(mAdapter);
        }

    }
}
