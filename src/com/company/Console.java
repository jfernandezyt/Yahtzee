package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Console {
    private static final Scanner scanner = new Scanner(System.in);

    public static List<Integer> parseUserSelections(String input) {
        String[] inputArr = input.split(" ");
        List<Integer> selections = new ArrayList<>();
        for (String temp : inputArr) {
            if (temp.equals("0")) {
                return new ArrayList<>();
            }
            selections.add(Integer.parseInt(temp) - 1);
        }
        return selections;
    }
    public static String getNumbersToReRoll(String playerName) {
        displayMessage("What Numbers would you like to re-roll (Player: " + playerName + ")  (1-5) ? ");
        String temp = scanner.nextLine();
        if(temp.equals("")) return null;
        return temp;
    }
    public static int getNumberInput(String message) {
        displayMessage(message);
        return scanner.nextInt();
    }
    public static void nextLine(){
        String consumer = scanner.nextLine();
    }
    public static String getStringInput(String message) {
        displayMessage(message);
        return scanner.nextLine();
    }
    public static void displayMessage(String message) {
        System.out.print(message);
    }
}
