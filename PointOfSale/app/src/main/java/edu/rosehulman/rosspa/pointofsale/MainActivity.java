package edu.rosehulman.rosspa.pointofsale;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {
    private Item currentItem;
    private Item clearedItem;
    private TextView mNameText;
    private TextView mQuantityText;
    private TextView mDateText;

    private ArrayList<Item> items = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNameText = (TextView) findViewById(R.id.name_text);
        mQuantityText = (TextView) findViewById(R.id.quantity_text);
        mDateText = (TextView) findViewById(R.id.date_text);

        registerForContextMenu(mNameText);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEditItem(false);
//                currentItem = Item.getDefaultItem();
//                showCurrentItem();

            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_main, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_context_edit:
                addEditItem(true);
                return true;

            case R.id.menu_context_remove:
                items.remove(currentItem);
                currentItem = new Item();
                showCurrentItem();
                return true;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_reset:
                clearedItem = currentItem;
                currentItem = new Item();
                showCurrentItem();
                Snackbar.make(findViewById(R.id.coordinator_layout), "Item cleared", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                currentItem = clearedItem;
                                clearedItem = null;
                                showCurrentItem();
                                Snackbar.make(findViewById(R.id.coordinator_layout), "Item restored", Snackbar.LENGTH_LONG).show();
                            }
                        }).show();
                return true;
            case R.id.action_search:
                showSearchDialog();
                return true;

            case R.id.action_clear_all:
                showClearAllDialog();
                return true;

            case R.id.action_settings:
                startActivity(new Intent(Settings.ACTION_SETTINGS));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void showCurrentItem() {
        mNameText.setText(currentItem.getName());
        mQuantityText.setText(getString(R.string.quantity_format, currentItem.getQuantity()));
        mDateText.setText(getString(R.string.date_format, currentItem.getDeliveryDateString()));
    }

    private void addEditItem(final boolean isEditing) {
        DialogFragment df = new DialogFragment() {
            @NonNull
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add, null);
                builder.setView(view);

                final EditText nameEditText = (EditText) view.findViewById(R.id.edit_name);
                final EditText quantityEditText = (EditText) view.findViewById(R.id.edit_quantity);
                final CalendarView deliveryDateView = (CalendarView) view.findViewById(R.id.calendar_view);
                final GregorianCalendar calendar = new GregorianCalendar();
                deliveryDateView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                    }
                });

                if (isEditing) {
                    nameEditText.setText(currentItem.getName());
                    quantityEditText.setText(String.valueOf(currentItem.getQuantity()));
                    deliveryDateView.setDate(currentItem.getDeliveryDateTime());
                }

                builder.setNegativeButton(android.R.string.cancel, null);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // TODO
                        String name = nameEditText.getText().toString();
                        int quantity = Integer.parseInt(quantityEditText.getText().toString());

                        if (isEditing) {
                            currentItem.setName(name);
                            currentItem.setQuantity(quantity);
                            currentItem.setDeliveryDate(calendar);
                        } else {
                            currentItem = new Item(name, quantity, calendar);
                            items.add(currentItem);
                        }
                        showCurrentItem();


                    }
                });

                return builder.create();
            }

        };
        df.show(getSupportFragmentManager(), "add");
    }

    private void showSearchDialog() {
        DialogFragment df = new DialogFragment() {
            @NonNull
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.search_dialog_title));
                builder.setItems(getNames(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentItem = items.get(i);
                        showCurrentItem();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, null);

                return builder.create();
            }

        };
        df.show(getSupportFragmentManager(), "search");
    }

    private String[] getNames() {
        String[] names = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            names[i] = items.get(i).getName();
        }
        return names;
    }

    private void showClearAllDialog() {
        DialogFragment df = new DialogFragment() {
            @NonNull
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.remove));
                builder.setMessage(getString(R.string.confirmation_dialog_message));
                builder.setNegativeButton(android.R.string.cancel, null);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        items.clear();
                        currentItem = new Item();
                        showCurrentItem();
                    }
                });
                return builder.create();
            }

        };
        df.show(getSupportFragmentManager(), "search");

    }

}
