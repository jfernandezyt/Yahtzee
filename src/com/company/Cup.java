package com.company;

import java.util.ArrayList;
import java.util.List;

public class Cup {
    public List<Die> dice = new ArrayList<>();

    public Cup() {
        while(dice.size() < 5){
            dice.add(new Die());
        }
    }
    public Cup(List<Die> dice) {
        this.dice = dice;
    }
    public void  roll(){
        for(Die die : dice)
            die.roll();

    }
    public void  roll(int unlockedSelection){
        dice.get(unlockedSelection).roll();

    }
    public void roll(List<Integer> unlockedSelections){
        for (int index: unlockedSelections)
            roll(index);

    }
    public String displayDice(){
        String output = "";
        int[] array = new int[5];
        int counter = 0;
        for(Die die : dice) {
            array[counter] = die.faceUpValue;
            counter++;
        }
        output = stringRepresentingDice(array);
        return output;
    }

    private String stringRepresentingDice(int[] values){
        String temp = "┌───┐   ┌───┐   ┌───┐   ┌───┐   ┌───┐\n";
        temp += "│ %s │   │ %s │   │ %s │   │ %s │   │ %s │\n";
        temp += "└───┘   └───┘   └───┘   └───┘   └───┘";
        temp = String.format(temp, values[0], values[1], values[2], values[3], values[4]);
        return temp;
    }
}
