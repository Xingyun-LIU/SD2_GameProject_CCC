package object;

import java.util.ArrayList;
import java.util.List;


public class Inventory {

    public ArrayList<Item> items = new ArrayList<>();

    public void addItem(Item newItem){

        for(Item item : items){
            if(item.id == newItem.id){
                item.amount += newItem.amount;
                return;
            }
        }

        if(items.size() < 5){
            items.add(newItem);
        }
    }

    public boolean isFull(){
        return items.size() >= 5;
    }
}
