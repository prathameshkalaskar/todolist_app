package com.example.todolist20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.todolist20.adapter.ToDoAdapter;
import com.example.todolist20.model.ToDoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  DialogCloseListener {

    private RecyclerView recyclerView;

    ToDoAdapter taskAdapter;
    List<ToDoModel> taskList;

    DatabaseHandler db;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        db.OpenDatabase();

        recyclerView = findViewById(R.id.taskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskAdapter = new ToDoAdapter(db,this);
        recyclerView.setAdapter(taskAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeHelper(taskAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        floatingActionButton = findViewById(R.id.floating);
        taskList = db.getAllTask();
        Collections.reverse(taskList);

        taskAdapter.setTasks(taskList);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });

    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        taskList = db.getAllTask();
        Collections.reverse(taskList);
        taskAdapter.setTasks(taskList);
        taskAdapter.notifyDataSetChanged();

    }
}