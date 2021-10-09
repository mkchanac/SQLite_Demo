package com.example.sqlitedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner txtRecords;
    private TextView txtName;
    private TextView txtStudentId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtRecords = findViewById(R.id.txtId);
        txtName = findViewById(R.id.txtName);
        txtStudentId = findViewById(R.id.txtStudentId);

        DatabaseHandler db = new DatabaseHandler(this);

        db.deleteAllStudents();

        Log.v("Insert: ", "Inserting ...");
        db.addStudent(new Student("Simon", "912345"));
        db.addStudent(new Student("Raymond", "923456"));
        db.addStudent(new Student("Ivan", "934567"));
        db.addStudent(new Student("Mary", "94567"));

        Log.v("Read: ", "Reading all students...");
        List<Student> students = db.getAllStudents();

        for (Student st : students) {
            Log.v("Record: ", st.getId() + ", " + st.getName() + ", " + st.getStudentId());
        }


        String[] spinnerItems = new String[students.size()];
        for (int i = 0; i < students.size(); i++) {
            spinnerItems[i] = String.valueOf(students.get(i).getId());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                spinnerItems);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtRecords.setAdapter(adapter);

        txtRecords.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int recordId = Integer.parseInt(txtRecords.getSelectedItem().toString());
                Student student = db.getStudent(recordId);
                txtName.setText(student.getName());
                txtStudentId.setText(student.getStudentId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}