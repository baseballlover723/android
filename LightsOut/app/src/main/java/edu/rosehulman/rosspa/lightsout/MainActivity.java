package edu.rosehulman.rosspa.lightsout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final int NUMBER_OF_LIGHTS = 7;
    private LightsOutGame game;
    private TextView gameStateTV;
    private Button[] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game = new LightsOutGame(NUMBER_OF_LIGHTS);
        gameStateTV = (TextView) findViewById(R.id.message_text);

        if (savedInstanceState != null && savedInstanceState.getIntArray("game_state") != null) {
            game.setAllValues(savedInstanceState.getIntArray("game_state"));
            gameStateTV.setText(savedInstanceState.getCharSequence("state_message"));
            game.setNumPresses(savedInstanceState.getInt("turns"));
        }

        Button newGameButton = (Button) findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(this);

        buttons = new Button[NUMBER_OF_LIGHTS];
        for (int x = 0; x < NUMBER_OF_LIGHTS; x++) {
            int id = getResources().getIdentifier("button" + x, "id", getPackageName());
            buttons[x] = (Button) findViewById(id);
            buttons[x].setOnClickListener(this);
            String zero = getString(R.string.zero);
            String one = getString(R.string.one);
            buttons[x].setText(game.getValueAtIndex(x) == 0 ? zero : one);
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.new_game_button) {
            game = new LightsOutGame(NUMBER_OF_LIGHTS);
        }
        for (int x = 0; x < NUMBER_OF_LIGHTS; x++) {
            if (view.getId() == buttons[x].getId()) {
                Log.d("Lights Out", "Button pressed at " + x);
                game.pressedButtonAtIndex(x);
            }
        }
        for (int x = 0; x < NUMBER_OF_LIGHTS; x++) {
            String zero = getString(R.string.zero);
            String one = getString(R.string.one);
            buttons[x].setText(game.getValueAtIndex(x) == 0 ? zero : one);
        }
        if (game.checkForWin()) {
            gameStateTV.setText(getString(R.string.win));
        } else {
            gameStateTV.setText(getResources().getQuantityString(R.plurals.message_format, game.getNumPresses(), game.getNumPresses()));
        }
        if (view.getId() == R.id.new_game_button) {
            gameStateTV.setText(getString(R.string.start));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("game_state", game.getAllValues());
        outState.putCharSequence("state_message", gameStateTV.getText());
        outState.putInt("turns", game.getNumPresses());
    }
}
