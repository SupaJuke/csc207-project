package controller;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleInputUI {
    /**
     * A helper for the UI
     * @return returns the int input from the user
     */
    Integer getIntInput(){
        Scanner reader = new Scanner(System.in);
        int input = -1;
        try{
            input = reader.nextInt();
        }catch(InputMismatchException e){
            System.out.println("Invalid ID detected.");
        }
        //reader.close();
        return input;
    }

    /**
     * A helper for the UI
     * @return returns the string input from the user
     */
    String getStringInput(){
        Scanner reader = new Scanner(System.in);
        return reader.nextLine();
    }
}
