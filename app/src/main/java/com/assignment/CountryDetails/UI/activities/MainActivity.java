package com.assignment.CountryDetails.UI.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.assignment.CountryDetails.R;
import com.assignment.CountryDetails.data.DB.CountryDatabase;

public class MainActivity extends AppCompatActivity {

    CountryDatabase mCountryDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        mCountryDatabase = CountryDatabase.getAppDatabase(MainActivity.this);
    }

    /**
     * Setting Actionbar/toolbar title
     *
     * @param title
     */
    public void setActionbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                //Display confirmation dialog for deleting the records
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("");
                builder.setMessage(getString(R.string.msg_delete_record_confirmation))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.str_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                mCountryDatabase.countryDao().deleteAllRows();

                            }
                        })
                        .setNegativeButton(getString(R.string.str_no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));


                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}