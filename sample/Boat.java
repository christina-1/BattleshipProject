package sample;

import dialogs.CustomDialog;
import exceptions.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

public class Boat extends CountPoints {
    Carrier new_carrier;
    Battleship new_battleship;
    Cruiser new_cruiser;
    Submarine new_submarine;
    Destroyer new_destroyer;
    ArrayList<String> ship_array = new ArrayList<String>(Arrays.asList("carrier", "cruiser", "battleship", "submarine", "destroyer"));
    int[] carrier, battleship, cruiser, submarine, destroyer;
    String layout[][], layout_initial[][];
    boolean layout_flag, num_ships_flag, shot, sunk_state;
    int num_of_remaining_cells, number_of_shots = 0, number_of_successful_shots = 0, num_of_remaining_ships = 5,
            successful_shot_x = -1, successful_shot_y = -1;

    public Boat() {}
    public Boat(int[][] input) {
        num_ships_flag = true;

        // If there are not given exactly five ships, one of each  kind, from the enemy and player txts
        // an exception is raised and the user is prompted to give another scenario ID with valid number of ships
        // If the number of ships is valid, the five ships are created
        try {
            checkKindOfShips(input);
            for (int i = 0; i < 5; i++) {
                switch (input[i][0]) {
                    case 1:
                        carrier = input[i];
                        break;
                    case 2:
                        battleship = input[i];
                        break;
                    case 3:
                        cruiser = input[i];
                        break;
                    case 4:
                        submarine = input[i];
                        break;
                    case 5:
                        destroyer = input[i];
                        break;
                }
            }

            new_carrier = new Carrier(carrier);
            new_battleship = new Battleship(battleship);
            new_cruiser = new Cruiser(cruiser);
            new_submarine = new Submarine(submarine);
            new_destroyer = new Destroyer(destroyer);

        }catch(InvalidCountException | NullPointerException e){
            num_ships_flag = false;
            Main.exception_caught = true;
            CustomDialog dialog = new CustomDialog("Error!", e.getMessage());
            dialog.openDialog();

        }
    }


    // Called from the Initializer
    public String[][] placingTheShips () {
        layout_flag = true;
        String[][] layout = new String[10][10];
        this.layout = layout;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++){
                layout[i][j] = "-"; //initialize layout
            }
        }

        // If an exception is raised when creating the layout, by "inserting" the ships
        // e.g. a ship gets out of the grid's bounds, the user is prompted to give another scenario ID
        // if no exception is raised the layout for the player or the enemy is created (depending on the usage)
        try {
            new_carrier.Carrier_layout(new_carrier, layout);
            new_battleship.Battleship_layout(new_battleship, layout);
            new_cruiser.Cruiser_layout(new_cruiser, layout);
            new_submarine.Submarine_layout(new_submarine, layout);
            new_destroyer.Destroyer_layout(new_destroyer, layout);
        }
        catch (OversizeException | OverlapTilesException | AdjacentTilesException | OrientationException e){
            CustomDialog dialog = new CustomDialog("Error!", e.getMessage());
            dialog.openDialog();
            Main.exception_caught = true;
            layout_flag = false;
        }

        layout_initial = new String[layout.length][layout[0].length];
        for (int i = 0; i < layout_initial.length; i++)
            layout_initial[i] = Arrays.copyOf(layout[i], layout[i].length);
        return layout;
    }

    // Called after each shot is made
    // When creating the layout all the cells containing a ship take a string value equal to the type of the ship
    // e.g "battleship". All the other cells take the value of "-", denoting that no ship is placed on these cells
    // When a ship is hit the corresponding cell takes the value of "X"
    // When a shot is made on a cell that does not contain any ship, the value given is "Y"
    public Pair<String, Boolean> changeLayout(int x, int y)  {

        String type_of_cell = "-";
        try {
            //sunk_state = false;
            if (layout[x][y] == "carrier") {
                new_carrier.setUntouchedState(false);
                new_carrier.setHitState(true);
                layout[x][y] = "X";
                new_carrier.number_of_remaining_cells--;
                num_of_remaining_cells = new_carrier.number_of_remaining_cells;
                if (new_carrier.number_of_remaining_cells == 0) {

                    new_carrier.setSunkState(true);
                    num_of_remaining_ships--;
                }
                type_of_cell = "carrier";
                sunk_state = new_carrier.getSunkState();
            }
            else if (layout[x][y] == "battleship") {
                new_battleship.setUntouchedState(false);
                new_battleship.setHitState(true);
                layout[x][y] = "X";
                new_battleship.number_of_remaining_cells--;
                num_of_remaining_cells = new_battleship.number_of_remaining_cells;
                if (new_battleship.number_of_remaining_cells == 0) {

                    new_battleship.setSunkState(true);
                    num_of_remaining_ships--;
                }
                type_of_cell = "battleship";
                sunk_state = new_battleship.getSunkState();
            }
            else if (layout[x][y] == "cruiser") {
                new_cruiser.setUntouchedState(false);
                new_cruiser.setHitState(true);
                layout[x][y] = "X";

                new_cruiser.number_of_remaining_cells--;
                num_of_remaining_cells = new_cruiser.number_of_remaining_cells;

                if (new_cruiser.number_of_remaining_cells == 0) {

                    new_cruiser.setSunkState(true);
                    num_of_remaining_ships--;
                }
                type_of_cell = "cruiser";
                sunk_state = new_cruiser.getSunkState();
            }
            else if (layout[x][y] == "submarine") {
                new_submarine.setUntouchedState(false);
                new_submarine.setHitState(true);
                layout[x][y] = "X";
                new_submarine.number_of_remaining_cells--;
                num_of_remaining_cells = new_submarine.number_of_remaining_cells;
                if (new_submarine.number_of_remaining_cells == 0) {

                    new_submarine.setSunkState(true);
                    num_of_remaining_ships--;
                }
                type_of_cell = "submarine";
                sunk_state = new_submarine.getSunkState();
            }
            else if (layout[x][y] == "destroyer") {
                new_destroyer.setUntouchedState(false);
                new_destroyer.setHitState(true);
                layout[x][y] = "X";
                new_destroyer.number_of_remaining_cells--;
                num_of_remaining_cells = new_destroyer.number_of_remaining_cells;
                if (new_destroyer.number_of_remaining_cells == 0) {

                    new_destroyer.setSunkState(true);
                    num_of_remaining_ships--;
                }
                type_of_cell = "destroyer";
                sunk_state = new_destroyer.getSunkState();
            }
            else{
                layout[x][y] = "Y"; //indicates that a shot has been made to that cell but it wasn't successful
            }
            shot = true;

        }
        catch(ArrayIndexOutOfBoundsException e){

            Main.exception_caught = true;
            CustomDialog dialog = new CustomDialog("Error!", e.getMessage());
            dialog.openDialog();
        }
        Pair<String, Boolean> results = new Pair(type_of_cell, sunk_state);
        return results;
    }

    // Throws an exception if there is not exactly one ship of each of the five kinds of ships
    void checkKindOfShips(int[][] input) throws InvalidCountException {
        int[] ship_counter = new int[6];

        // Initialize ship_counter to zero
        Arrays.fill(ship_counter, 0);

        for (int i = 0; i < 5; i++) {

            // Checking if there is exactly one ship of each of the five kinds of ships
            if (ship_counter[input[i][0]] == 0)
                ship_counter[input[i][0]]++;
            else {
                Main.exception_caught = true;
                throw new InvalidCountException(("There can only be one ship of each kind"));
            }
        }
    }

    // Checks if the given cell is in the bounds of the board and either nothing or
    // another cell of the same ship is placed in it
    public boolean CheckingBounds(int x, int y, String[][] layout, String ship_type){
        int max_size = 9, min_size = 0;
        boolean in_bounds = false;
        if((x <= max_size && x >= min_size && y <= max_size && y >= min_size
                && (layout[x][y] == ship_type || layout[x][y] == "-"))
                || (x > max_size || x < min_size || y > max_size || y < min_size)){
            in_bounds = true;
        }
        return in_bounds;
    }
}
