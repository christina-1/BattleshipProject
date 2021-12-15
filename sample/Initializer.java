package sample;

import dialogs.CustomDialog;
import exceptions.InvalidCountException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import static sample.Main.exception_caught;

public class Initializer extends Boat{
    public Boat b_player, b_enemy;
    boolean player_flag, enemy_flag, boat_player_flag, boat_enemy_flag;


    public Initializer(String player_fileName, String enemy_fileName) {

        // Reading enemy and player input
        // If the number of columns of some line of the input are not exactly 4 then exceptions are raised

        try {
            int[][] player = SavingInput(player_fileName);
            int[][] enemy = SavingInput(enemy_fileName);

            // Call Boat constructor to create the ships
            // If the number of the given ships is correct
            // and if there are exactly 5 ships, one of each kind
            // the boat_player_flag and boat_enemy_flag will have been set to true
            // and the placingTheShips method can be called in order to check if all the other constraints are met
            // and create the layout

            if (!exception_caught) {
                this.b_player = new Boat(player);
                this.b_enemy = new Boat(enemy);
                boat_player_flag = b_player.num_ships_flag;
                boat_enemy_flag = b_enemy.num_ships_flag;

                if (boat_player_flag && boat_enemy_flag) {

                    //Create enemy and player layout
                    this.b_player.placingTheShips();
                    this.b_enemy.placingTheShips();

                    player_flag = b_player.layout_flag;
                    enemy_flag = b_enemy.layout_flag;
                }
            }

        } catch (ArrayIndexOutOfBoundsException | NumberFormatException | NullPointerException | InvalidCountException e) {
            exception_caught = true;
            //Null pointer exception in case the id the user gives is invalid
            CustomDialog dialog = new CustomDialog("Error!", e.getMessage());
            dialog.openDialog();
        }
    }

    // If the given scenario id does not correspond to an enemy_SCENARIO-ID.txt and a player_SCENARIO-ID.txt file
    // an exception is raised and the user is prompted to give a valid scenario ID
    public int[][] SavingInput(String fileName) throws InvalidCountException {
        int[][] myArray = new int[0][];
        try {
            Scanner sc = new Scanner(new BufferedReader(new FileReader("./src/medialab/" + fileName)));

            int lineCount = (int) Files.lines(Paths.get("./src/medialab/" + fileName)).count();
            myArray = new int[lineCount][4];

            while (sc.hasNextLine()) {
                for (int i = 0; i < myArray.length; i++) {
                    String[] line = sc.nextLine().trim().split(",");
                    checkNumberOfShips(line.length, myArray.length);
                    for (int j = 0; j < line.length; j++) {
                        boolean ship_num = checkNumberOfShips(line.length, myArray.length);
                        if(ship_num)
                            myArray[i][j] = Integer.parseInt(line[j]);
                    }
                }
            }
        } catch (IOException e) {
            Main.exception_caught = true;
            CustomDialog dialog = new CustomDialog("Error!", "Please enter valid scenario ID");
            dialog.openDialog();
        }
        return myArray;
    }

    // Checks if exactly 5 ships are given in the txt files
    boolean checkNumberOfShips(int line_len, int array_len) throws InvalidCountException {
        boolean ship_num = true;
        if(array_len !=5)
            throw new InvalidCountException("Exactly 5 ships must be used!");
        if(line_len != 4) {
            ship_num = false;
            throw new InvalidCountException("Exactly 5 ships must be used!");
        }
        return ship_num;
    }
}
