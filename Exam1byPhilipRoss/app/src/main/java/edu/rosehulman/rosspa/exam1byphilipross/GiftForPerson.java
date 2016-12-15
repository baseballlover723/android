package edu.rosehulman.rosspa.exam1byphilipross;

/**
 * Created by rosspa on 12/13/2016.
 */
public class GiftForPerson {
    private String person; // TODO refactor to own class
    private String gift; // TODO refactor to own class
    private boolean important;

    public GiftForPerson(String person, String gift) {
        this.person = person;
        this.gift = gift;
        this.important = false;
    }

    public void toggleImportance() {
        this.important = !this.important;
    }

    public String getGift() {
        return gift;
    }

    public String getPerson() {
        return person;
    }

    public boolean isImportant() {
        return this.important;
    }

    public String toString() {
        String str = this.gift + " for " + this.person;
        return this.important ? str.toUpperCase() : str;
    }
    public String toHtmlString() {
        String str = this.gift + " for " + this.person;
        return this.important ? "<b>" + str.toUpperCase() +"</b>" : str;
    }
}
