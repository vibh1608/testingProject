package com.example.user.to_do_project_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CompletedTask extends AppCompatActivity {

    ImageView add,complete;
    ListView listView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completed_task);

        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        listView=findViewById(R.id.list2);
        add=findViewById(R.id.add_tool_btn2);
        complete=findViewById(R.id.complete_tool_btn2);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CompletedTask.this,"Press back and go to previous page",Toast.LENGTH_LONG).show();
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CompletedTask.this,"You are watching completed task",Toast.LENGTH_LONG).show();
            }
        });

        DataBaseHelper dataBaseHelper = new DataBaseHelper(CompletedTask.this,Constants.DATABASE_NAME,null,Constants.DATABASE_VERSION);

        final ArrayList<Task> arrayList = dataBaseHelper.getCompletedEntryList();

        if(arrayList.isEmpty())
        {
            Toast.makeText(CompletedTask.this,"There is no completed task",Toast.LENGTH_SHORT).show();
            finish();
        }

        final CustomAdapter customAdapter = new CustomAdapter(CompletedTask.this,arrayList);

        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(CompletedTask.this,"Please long press to delete task",Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                DataBaseHelper newRepo = new DataBaseHelper(CompletedTask.this,Constants.DATABASE_NAME,null,Constants.DATABASE_VERSION);

                //Deleting the long clicked Entry.
                newRepo.delete(arrayList.get(i).mid);

                Toast.makeText(CompletedTask.this,"Item Deleted",Toast.LENGTH_SHORT).show();

                //Creating object of EntryRepo and listing Entries by Setting adapter of CustomAdapter to listView object.
                ArrayList<Task> newCompletedEntries = newRepo.getCompletedEntryList();

                CustomAdapter newAdapter = new CustomAdapter(CompletedTask.this,newCompletedEntries);

                listView.setAdapter(newAdapter);

                //If there are no completed entries, then we will finish the activity.
                if(newCompletedEntries.isEmpty())
                {
                    Toast.makeText(CompletedTask.this,"There are no completed Items",Toast.LENGTH_SHORT).show();
                    Intent changeActivity_1=new Intent(CompletedTask.this,MainActivity.class);
                    startActivity(changeActivity_1);

                    finish();
                }

                Intent changeActivity_2=new Intent(CompletedTask.this,MainActivity.class);
                startActivity(changeActivity_2);

                return true;
            }
        });
    }
}
