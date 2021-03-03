package com.example.pettrackerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase sqLiteDatabase;
    PetDatabaseHelper petDatabaseHelper;
    RecyclerView recyclerView;
    PetAdapter petAdapter;
    ArrayList<PetEntry> petEntryArrayList = new ArrayList<PetEntry>();
    int petNumber = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PetEntry pet1 = new PetEntry("hello", "cat");
        PetEntry pet2 = new PetEntry("whatup", "Dog");
        recyclerView = findViewById(R.id.recyclerView);
        petAdapter = new PetAdapter();
        recyclerView.setAdapter(petAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        petDatabaseHelper = new PetDatabaseHelper(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = petDatabaseHelper.getReadableDatabase();
        //petDatabaseHelper.insertData(pet1);
        //petDatabaseHelper.insertData(pet2);
        setup();
    }

    public void setup(){
        SQLiteDatabase sqLiteDatabase = petDatabaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("pets", new String[] {"_id", "name", "type", "drawable", "homeLat", "homeLong", "petLat", "petLong"}
        , null, null, null,null,null);

        if(cursor.moveToLast()){
            int id = cursor.getInt(0);
            petNumber = id + 1;
        }

        if(cursor.moveToFirst()){
            PetEntry petEntry = createPet(cursor);
            petEntryArrayList.add(petEntry);

        }

        boolean keepgoing = true;

        while(keepgoing == true){
            if(cursor.moveToNext()){
                PetEntry petEntry = createPet(cursor);
                petEntryArrayList.add(petEntry);
            }
            else{
                keepgoing = false;
            }
        }
    }

    public PetEntry createPet(Cursor cursor){
        String name = cursor.getString(1);
        String type = cursor.getString(2);
        PetEntry petEntry = new PetEntry(name, type);
        return petEntry;
    }

    public class PetAdapter extends RecyclerView.Adapter{

        class PetViewHolder extends RecyclerView.ViewHolder{
            TextView nameTextView;
            TextView ageTextView;

            public PetViewHolder(@NonNull View itemView) {
                super(itemView);
                this.nameTextView = itemView.findViewById(R.id.nameTextView);
                this.ageTextView = itemView.findViewById(R.id.ageTextView);
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout, parent, false);
            //view.setOnClickListener(myOnClickListener);
            return new PetViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            PetViewHolder petViewHolder = (PetViewHolder) holder;
            PetEntry petEntry = petEntryArrayList.get(position);

            petViewHolder.ageTextView.setText(petEntry.type);
            petViewHolder.nameTextView.setText(petEntry.name);
        }

        @Override
        public int getItemCount() {
            return petEntryArrayList.size();
        }
    }
}