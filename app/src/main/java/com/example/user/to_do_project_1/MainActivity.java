package com.example.user.to_do_project_1;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DataBaseHelper mDataBaseHelper;

    Toolbar mToolbar;

    ImageView addButton,completeButton;
    EditText m_title,m_description;
    DatePicker mDatePicker;
    Button save,cancel;
    ListView mlistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDataBaseHelper = new DataBaseHelper(MainActivity.this,Constants.DATABASE_NAME,null,Constants.DATABASE_VERSION);

        //Setting references with their IDs.
        addButton=findViewById(R.id.add_task);
        completeButton=findViewById(R.id.completed_task);
        mlistview=findViewById(R.id.list);

        //Creating object of EntryRepo and listing Entries by Setting adapter of CustomAdapter to listView object.
        ArrayList<Task> arrayList = mDataBaseHelper.getEntryList();
        CustomAdapter customAdapter = new CustomAdapter(MainActivity.this,arrayList);
        mlistview.setAdapter(customAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Add task",Toast.LENGTH_SHORT).show();

                //Creating object of Dialog.
                final Dialog dialog=new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.custom_dialog);   //Setting ContentView.

                m_title=dialog.findViewById(R.id.title_et);
                m_description=dialog.findViewById(R.id.description_et);
                mDatePicker=dialog.findViewById(R.id.datePicker);
                save=dialog.findViewById(R.id.save_btn);
                cancel=dialog.findViewById(R.id.cancel_btn);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!m_title.getText().toString().isEmpty()&&!m_description.getText().toString().isEmpty())
                        {
                            DataBaseHelper dataBaseHelper= new DataBaseHelper(MainActivity.this,Constants.DATABASE_NAME,null,Constants.DATABASE_VERSION);
                            Task task = new Task();
                            task.mtitle=m_title.getText().toString();
                            task.mdescription=m_description.getText().toString();
                            task.mdate=String.valueOf(mDatePicker.getDayOfMonth()+"/")+String.valueOf(mDatePicker.getMonth()+"/")+String.valueOf(mDatePicker.getYear());
                            task.mstatus=0;
                            task.mid=dataBaseHelper.insert(task);

                            ArrayList<Task> arrayList1 = dataBaseHelper.getEntryList();

                            CustomAdapter customAdapter1 = new CustomAdapter(MainActivity.this,arrayList1);

                            mlistview.setAdapter(customAdapter1);

                            Toast.makeText(MainActivity.this,"Item Saved",Toast.LENGTH_SHORT).show();

                            dialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"please fill the information correctly",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"List of completed task",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this,CompletedTask.class);
                startActivity(intent);
            }
        });

        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.custom_dialog);

                m_title=findViewById(R.id.title_et);
                m_description=findViewById(R.id.description_et);
                mDatePicker=findViewById(R.id.datePicker);
                save=findViewById(R.id.save_btn);
                cancel=findViewById(R.id.cancel_btn);

                final DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this,Constants.DATABASE_NAME,null,Constants.DATABASE_VERSION);
                ArrayList<Task> arrayList1 = dataBaseHelper.getEntryList();
                final Task task = arrayList1.get(i);

                m_title.setText(task.mtitle);
                m_description.setText(task.mdescription);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(!m_title.getText().toString().isEmpty() && !m_description.getText().toString().isEmpty())
                        {

                            //Updating information in DB just like insering.
                            Task updatedTask=new Task();
                            updatedTask.mid=task.mid;
                            updatedTask.mtitle=m_title.getText().toString();
                            updatedTask.mdescription=m_description.getText().toString();
                            updatedTask.mdate=String.valueOf(mDatePicker.getDayOfMonth())+"/"+
                                    String.valueOf(mDatePicker.getMonth())+"/"+
                                    String.valueOf(mDatePicker.getYear());
                            updatedTask.mstatus=task.mstatus;

                            dataBaseHelper.update(updatedTask);

                            ArrayList<Task> updatedArrayList = dataBaseHelper.getEntryList();

                            CustomAdapter adapter = new CustomAdapter(MainActivity.this,updatedArrayList);

                            mlistview.setAdapter(adapter);

                            Toast.makeText(MainActivity.this,"List Updated",Toast.LENGTH_SHORT).show();

                            dialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Please fill the information correctly",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        mlistview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Creating object of EntryRepo.
                DataBaseHelper dataBaseHelper  = new DataBaseHelper(MainActivity.this,Constants.DATABASE_NAME,null,Constants.DATABASE_VERSION);
                ArrayList<Task> entries = dataBaseHelper.getEntryList();

                //creating reference of Entry.
                Task completedTask = entries.get(i);

                completedTask.mstatus=1;
                dataBaseHelper.update(completedTask);

                //Creating object of EntryRepo and listing Entries by Setting adapter of CustomAdapter to listView object.
                ArrayList<Task> updatedEntries = dataBaseHelper.getEntryList();

                CustomAdapter adapter = new CustomAdapter(MainActivity.this,updatedEntries);

                mlistview.setAdapter(adapter);

                //Displaying Toast.
                Toast.makeText(MainActivity.this,"Item Updated",Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }
}
