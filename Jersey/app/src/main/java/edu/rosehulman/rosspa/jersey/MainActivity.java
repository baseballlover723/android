package edu.rosehulman.rosspa.jersey;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import static edu.rosehulman.rosspa.jersey.R.id.fab;

public class MainActivity extends AppCompatActivity {
    private final static String PREFS = "PREFS";
    private static final String KEY_JERSEY_NAME = "KEY_JERSEY_NAME";
    private static final String KEY_JERSEY_NUMBER = "KEY_JERSEY_NUMBER";
    private static final String KEY_JERSEY_COLOR_RED = "KEY_JERSEY_COLOR_RED";
    private TextView nameTV;
    private TextView numberTV;
    private Jersey currentJersey;
    private Jersey clearedJersey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nameTV = (TextView) findViewById(R.id.jersey_name);
        numberTV = (TextView) findViewById(R.id.jersey_number);

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String name = prefs.getString(KEY_JERSEY_NAME, getString(R.string.default_jersey_name));
        int number = prefs.getInt(KEY_JERSEY_NUMBER, Integer.parseInt(getString(R.string.default_jersey_number)));
        boolean red = prefs.getBoolean(KEY_JERSEY_COLOR_RED, true);

        currentJersey = new Jersey(name, number, red);
        displayJersey();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editJersey();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getString(R.string.confirmation_dialog_message));
            builder.setNegativeButton(android.R.string.cancel, null);
            builder.setPositiveButton(getString(R.string.reset), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    clearedJersey = currentJersey;
                    currentJersey = new Jersey();
                    displayJersey();
                    Snackbar.make(findViewById(R.id.coordinator_layout), "Item cleared", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    currentJersey = clearedJersey;
                                    clearedJersey = null;
                                    displayJersey();
                                    Snackbar.make(findViewById(R.id.coordinator_layout), "Item restored", Snackbar.LENGTH_LONG).show();
                                }
                            }).show();
                }
            });
            builder.create().show();
            return true;
        }  else if (id == R.id.action_settings) {
            startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
        }

        return super.onOptionsItemSelected(item);
    }

    public void editJersey() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = MainActivity.this.getLayoutInflater().inflate(R.layout.dialog_edit, null);

        final EditText editName = (EditText) view.findViewById(R.id.edit_name);
        final EditText editNumber = (EditText) view.findViewById(R.id.edit_number);
        final Switch redSwitch = (Switch) view.findViewById(R.id.red_switch);

        editName.setText(currentJersey.getName());
        editNumber.setText(currentJersey.getPlayerNumberString());
        redSwitch.setChecked(currentJersey.isRed());

        builder.setView(view);
        builder.setTitle(getString(R.string.edit_dialog_title));
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                currentJersey.setName(editName.getText().toString());
                if (editNumber.getText().toString().equals("")) {
                    currentJersey.setPlayerNumber(0);
                } else {
                    currentJersey.setPlayerNumber(Integer.parseInt(editNumber.getText().toString()));
                }
                currentJersey.setRed(redSwitch.isChecked());
                displayJersey();
            }
        });

        builder.create().show();

    }

    public void displayJersey() {
        nameTV.setText(currentJersey.getName());
        numberTV.setText(currentJersey.getPlayerNumberString());
        ImageView image = (ImageView) findViewById(R.id.jersey_image);
        image.setImageResource(currentJersey.isRed() ? R.drawable.red_jersey : R.drawable.blue_jersey);
    }

    // Add this method:
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_JERSEY_NAME, currentJersey.getName());
        editor.putInt(KEY_JERSEY_NUMBER, currentJersey.getPlayerNumber());
        editor.putBoolean(KEY_JERSEY_COLOR_RED, currentJersey.isRed());
// Put the other fields into the editor
        editor.commit();
    }
}
