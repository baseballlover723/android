package edu.rosehulman.rosspa.exam1byphilipross;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rosspa on 12/13/2016.
 */

public class ShoppingList {
    private List<GiftForPerson> list;

    public ShoppingList() {
        this.list = new ArrayList<GiftForPerson>();
    }

    public void addGiftForPerson(GiftForPerson newGiftForPerson) {
        this.list.add(newGiftForPerson);
    }

    public List<GiftForPerson> getList() {
        return this.list;
    }

    public GiftForPerson getAtIndex(int i) {
        return this.list.get(i);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (GiftForPerson giftForPerson : this.list) {
            sb.append(giftForPerson);
            sb.append("\n");
        }
        return sb.toString();
    }

    public String toHtmlString() {
        StringBuilder sb = new StringBuilder();
        for (GiftForPerson giftForPerson : this.list) {
            sb.append(giftForPerson.toHtmlString());
            sb.append("<br/>");
        }
        return sb.toString();
    }

    public boolean[] getImportance() {
        boolean[] arr = new boolean[this.list.size()];
        for (int i = 0; i < this.list.size(); i++) {
            arr[i] = this.list.get(i).isImportant();
        }
        return arr;
    }
}
