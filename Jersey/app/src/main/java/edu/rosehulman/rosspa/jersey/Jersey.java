package edu.rosehulman.rosspa.jersey;

/**
 * Created by Philip Ross on 12/15/2016.
 */

public class Jersey {
    private String name;
    private int playerNumber;
    private boolean red;

    public Jersey() {
        this("ANDROID", 17, true);
    }

    public Jersey(String name, int playerNumber, boolean red) {
        this.name = name;
        this.playerNumber = playerNumber;
        this.red = red;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public String getPlayerNumberString() {
        return String.valueOf(playerNumber);
    }

    public void setRed(boolean red) {
        this.red = red;
    }

    public boolean isRed() {
        return red;
    }
}
