package edu.rosehulman.rosspa.exam1byphilipross;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ShoppingList currentShoppingList;
    private ShoppingList clearedShoppingList;
    private TextView shoppingListTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        shoppingListTV = (TextView) findViewById(R.id.shopping_list);
        currentShoppingList = new ShoppingList();

        currentShoppingList.addGiftForPerson(new GiftForPerson("Philip", "Money"));
        currentShoppingList.addGiftForPerson(new GiftForPerson("Rob", "Stuff"));
        currentShoppingList.getAtIndex(1).toggleImportance();
        updateShoppingListTextView();

        Button button = (Button) findViewById(R.id.enter_button);
        button.setOnClickListener(this);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.enter_button) {
            EditText giftEdit = (EditText) findViewById(R.id.edit_gift);
            EditText recipientEdit = (EditText) findViewById(R.id.edit_recipient);
            if (giftEdit.getText().toString().equals("") || recipientEdit.getText().toString().equals("")) {
                hideKeyboard(this);
                return;
            }
            GiftForPerson giftForPerson = new GiftForPerson(giftEdit.getText().toString(), recipientEdit.getText().toString());
            currentShoppingList.addGiftForPerson(giftForPerson);
            hideKeyboard(this);
        }
        updateShoppingListTextView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_reset:
                clearedShoppingList = currentShoppingList;
                currentShoppingList = new ShoppingList();
                Snackbar.make(findViewById(R.id.coordinator_layout), "Shopping List Cleared", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                currentShoppingList = clearedShoppingList;
                                clearedShoppingList = null;
                                updateShoppingListTextView();
                                Snackbar.make(findViewById(R.id.coordinator_layout), "Item restored", Snackbar.LENGTH_LONG).show();
                            }
                        }).show();
                updateShoppingListTextView();
                return true;
            case R.id.action_edit:
                MainActivity that = this;
                DialogFragment df = new DialogFragment() {
                    @NonNull
                    @Override
                    public Dialog onCreateDialog(Bundle savedInstanceState) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        CharSequence[] names = new CharSequence[currentShoppingList.getList().size()];
                        for (int i=0; i<currentShoppingList.getList().size(); i++) {
                            GiftForPerson g = currentShoppingList.getAtIndex(i);
                            names[i] = g.getPerson();
                        }
                        builder.setTitle(getString(R.string.dialog_title));
                        builder.setMultiChoiceItems(names, currentShoppingList.getImportance(),
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                        currentShoppingList.getAtIndex(i).toggleImportance();
                                        updateShoppingListTextView();
                                    }
                                });
                        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_importance, null);

                        builder.setView(view);

                        return builder.create();
                    }

                };
                df.show(getSupportFragmentManager(), "add");
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateShoppingListTextView() {
        // deprecated but this is much better, ideal solution is to use a list view and bold each element as needed
        shoppingListTV.setText(Html.fromHtml(currentShoppingList.toHtmlString()));
//        shoppingListTV.setText(currentShoppingList.toString());
    }


    // http://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
