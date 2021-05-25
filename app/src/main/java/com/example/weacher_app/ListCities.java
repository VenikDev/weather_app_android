package com.example.weacher_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class ListCities extends AppCompatActivity implements DialogDelegate{

    // Ссылка на приложение
    MyApplication mApp;

    // Элемент интерфейса
    ListView mList;
    Button mAddCity;

    ArrayAdapter<String> mAdapter;

    InputDialog mInputDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cities);
        setTitle(R.string.activeBarTitle);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mApp = (MyApplication) getApplicationContext();

        mAddCity = findViewById(R.id.buttonAddCity);
        mList = findViewById(R.id.ListViewCity);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.titleInputDialog);

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton(R.string.btnAddInputDialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String city = input.getText().toString();

                if (!city.isEmpty()){
                    if(!mApp.getListCities().contains(city)){
                        mApp.getListCities().add(city);

                        mAdapter.notifyDataSetChanged();
                    }
                    else
                        input.setText("Город уже есть!");
                }
                else
                    input.setText("Название не может быть пустым!");
            }
        });
        builder.setNegativeButton(R.string.btnCancelInputDialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        mInputDialog = new InputDialog(this, "Добавить город.");
        mInputDialog.setDelegate(this);

        View.OnClickListener addListener = new View.OnClickListener() {
            @Override
            public void onClick(View v){
//                mInputDialog.show();
                builder.show();
            }
        };

        mAddCity.setOnClickListener(addListener);

        mAdapter = new ArrayAdapter<String>(mApp, R.layout.my_list_template, android.R.id.text1, mApp.getListCities()) {
            /**
             * Получить вид
             * @param position - позиция
             * @param convertView - вид ранее созданный
             * @param parent - родитель
             * @return - вид
             */
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view;

                if(convertView == null){
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    view = inflater.inflate(R.layout.my_list_template, parent, false);
                }
                else{
                    view = convertView;
                }

                TextView textView = view.findViewById(R.id.itemListNameCity);

                String city = mApp.getListCities().get(position);

                textView.setText(city);

                return view;
            }
        };

        mList.setAdapter(mAdapter);

        mList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                TextView textView = itemClicked.findViewById(R.id.itemListNameCity);

                String selectCity = textView.getText().toString();

                mApp.getWeather().setCity(selectCity);
                mApp.setSelectedCityFromTheList(selectCity);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    @Override
    public void onConfirm() {
        String city = mInputDialog.getText();

        if (!city.isEmpty()){
            if(!mApp.getListCities().contains(city)){
                mApp.getListCities().add(city);

                mAdapter.notifyDataSetChanged();
            }
            else
                Toast.makeText(this, "Город уже есть!", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Название не может быть пустым!", Toast.LENGTH_LONG).show();
        }
    }
}