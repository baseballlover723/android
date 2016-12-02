package edu.rosehulman.rosspa.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TicTacToeGame myGame = new TicTacToeGame(this);
    private TextView myGameStateTV;
    private Button[][] myTicTacToeButtons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myGame.resetGame();
        myGameStateTV = (TextView) findViewById(R.id.message_text);

        Button newGameButton = (Button) findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(this);

        myTicTacToeButtons = new Button[TicTacToeGame.NUM_ROWS][TicTacToeGame.NUM_COLUMNS];
        for (int row = 0; row < TicTacToeGame.NUM_ROWS; row++) {
            for (int col = 0; col < TicTacToeGame.NUM_COLUMNS; col++) {
                int id = getResources().getIdentifier("button" + row + col, "id", getPackageName());
                myTicTacToeButtons[row][col] = (Button) findViewById(id);
                myTicTacToeButtons[row][col].setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.new_game_button) {
            myGame.resetGame();
        }
        myGameStateTV.setText(myGame.stringForGameState());
        for (int row = 0; row < TicTacToeGame.NUM_ROWS; row++) {
            for (int col = 0; col < TicTacToeGame.NUM_COLUMNS; col++) {
                if (view.getId() == myTicTacToeButtons[row][col].getId()) {
                    Log.d("TTT", "Button pressed at " + row + " " + col);
                    myGame.pressedButtonAtLocation(row, col);
                }
                myTicTacToeButtons[row][col].setText(myGame.stringForButtonAtLocation(row, col));
            }
        }
        myGameStateTV.setText(myGame.stringForGameState());
    }
}
