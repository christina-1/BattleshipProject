package sample;

import dialogs.CustomDialog;
import dialogs.CustomDialogShipsCondition;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

import static sample.Main.*;
import static sample.PlayerShots.player_points;

public class EnemyShots extends Boat {

    // create instance of Random class
    Random rand = new Random();

    private String hit_cell, orientation = "";
    public static int enemy_points = 0;
    boolean shot, success = false, found_rest_of_ship = false, first_try = true, ship_down = false;
    private ArrayList<Pair> coords = new ArrayList<Pair>(), left_choices = new ArrayList<>(), right_choices = new ArrayList<>(), up_choices = new ArrayList<>(),
            down_choices = new ArrayList<>();
    private Boat enemy_ships, player_ships;
    private Pair<Integer, Integer> parent_shot;
    private int actual_x, actual_y;

    EnemyShots(Boat b_enemy, Boat b_player) {
        this.enemy_ships = b_enemy;
        this.player_ships = b_player;
    }


    public Pair<String, Pair<Integer, Integer>> Shoot() {
        // Variables successful_shot_x and successful_shot_y keep the coordinates of the last successful shot of the player
        // If they equal -1 it means that there has not been any successful shot either since the beginning of the game
        // or since a ship was sunk by the enemy

        if (enemy_ships.successful_shot_x == -1 && enemy_ships.successful_shot_y == -1) {
            ShootingWithoutPreviousSuccess();
        }
        else {
            if (first_try) {
                // Put in coords the 4 neighbors of the hit cell in order to search and find the rest of the ship
                PossibleShots(enemy_ships.successful_shot_x, enemy_ships.successful_shot_y);
                ChoosingCell();
            }

            // orientation variable keeps the direction at which the shots must be made (relatively to the first cell of the ship that was found)
            // in order to sunk the ship
            // The possible values are:
            // "horizontal-left", "horizontal-right", "vertical-up", "vertical-down"
            // If orientation has a value, it means that the enemy managed to make a second shot on the same ship in one of
            // the 4 possible directions and now he will continue shooting in this direction

            else if (orientation != "" && !ship_down) {
                if (orientation.equals("horizontal-left") && !left_choices.isEmpty() && !found_rest_of_ship && !right_choices.isEmpty()) {
                    Pair<Integer, Integer> new_coords = right_choices.get(0);
                    right_choices.remove(0);
                    ShootSpecifically(new_coords, parent_shot);
                } else if (orientation.equals("horizontal-left") && !left_choices.isEmpty()) {
                    Pair<Integer, Integer> new_coords = left_choices.get(0);
                    left_choices.remove(0);
                    ShootSpecifically(new_coords, parent_shot);
                }

                // If the list containing the shots that can be made to the left of the previous cell (left_choices list) is not empty
                // but the enemy couldn't hit the ship with his previous shot
                // then he will start looking to the possible shots towards the right (right_choices list) to check if there
                // are cells of the ship still not shot.
                // The same checks are made for all 4 possible directions depending on the orientation of the ship each time
                // left_choices --> possible shots to the left
                // right_choices --> possible shots to the right
                // up_choices --> possible shots upwards
                // down_choices --> possible shots downwards
                else if (orientation.equals("horizontal-left") && left_choices.isEmpty() && !right_choices.isEmpty()) {
                    Pair<Integer, Integer> new_coords = right_choices.get(0);
                    right_choices.remove(0);
                    ShootSpecifically(new_coords, parent_shot);
                } else if (orientation.equals("horizontal-right") && !right_choices.isEmpty() && !found_rest_of_ship && !left_choices.isEmpty()) {
                    Pair<Integer, Integer> new_coords = left_choices.get(0);
                    left_choices.remove(0);
                    ShootSpecifically(new_coords, parent_shot);
                } else if (orientation.equals("horizontal-right") && !right_choices.isEmpty()) {
                    Pair<Integer, Integer> new_coords = right_choices.get(0);
                    right_choices.remove(0);
                    ShootSpecifically(new_coords, parent_shot);
                } else if (orientation.equals("horizontal-right") && right_choices.isEmpty() && !left_choices.isEmpty()) {
                    Pair<Integer, Integer> new_coords = left_choices.get(0);
                    left_choices.remove(0);
                    ShootSpecifically(new_coords, parent_shot);
                } else if (orientation.equals("vertical-up") && !up_choices.isEmpty() && !found_rest_of_ship && !down_choices.isEmpty()) {
                    Pair<Integer, Integer> new_coords = down_choices.get(0);
                    down_choices.remove(0);
                    ShootSpecifically(new_coords, parent_shot);
                } else if (orientation.equals("vertical-up") && !up_choices.isEmpty()) {
                    Pair<Integer, Integer> new_coords = up_choices.get(0);
                    up_choices.remove(0);
                    ShootSpecifically(new_coords, parent_shot);
                } else if (orientation.equals("vertical-up") && up_choices.isEmpty() && !down_choices.isEmpty()) {
                    Pair<Integer, Integer> new_coords = down_choices.get(0);
                    down_choices.remove(0);
                    ShootSpecifically(new_coords, parent_shot);
                } else if (orientation.equals("vertical-down") && !down_choices.isEmpty() && !found_rest_of_ship && !up_choices.isEmpty()) {
                    Pair<Integer, Integer> new_coords = up_choices.get(0);
                    up_choices.remove(0);
                    ShootSpecifically(new_coords, parent_shot);
                } else if (orientation.equals("vertical-down") && !down_choices.isEmpty()) {
                    Pair<Integer, Integer> new_coords = down_choices.get(0);
                    down_choices.remove(0);
                    ShootSpecifically(new_coords, parent_shot);
                } else if (orientation.equals("vertical-down") && down_choices.isEmpty() && !up_choices.isEmpty()) {
                    Pair<Integer, Integer> new_coords = up_choices.get(0);
                    up_choices.remove(0);
                    ShootSpecifically(new_coords, parent_shot);
                }
                if (ship_down) {
                    // Re-initialize variables for the next shots
                    orientation = "";
                    enemy_ships.successful_shot_x = -1;
                    enemy_ships.successful_shot_y = -1;
                    success = false;
                    found_rest_of_ship = false;
                    first_try = true;
                    ship_down = false;

                    left_choices.clear();
                    right_choices.clear();
                    up_choices.clear();
                    down_choices.clear();
                    coords.clear();
                }
            }

            // When the enemy is trying to sink a ship but he did not manage to hit a ship's cell with his previous shot
            else if (!success) {

                // Search again for the rest of the ship
                ChoosingCell();
                PossibleShots(enemy_ships.successful_shot_x, enemy_ships.successful_shot_y);

            }
            // The ship is down
            else if (ship_down) {
                // Re-initialize variables for the next shots
                orientation = "";
                enemy_ships.successful_shot_x = -1;
                enemy_ships.successful_shot_y = -1;
                success = false;
                found_rest_of_ship = false;
                first_try = true;

                left_choices.clear();
                right_choices.clear();
                up_choices.clear();
                down_choices.clear();
                coords.clear();

                Shoot();
            }
        }
        enemy_ships.number_of_shots++;

        Pair<String, Pair<Integer, Integer>> res = new Pair<>(hit_cell, new Pair<>(actual_x, actual_y));
        return res;
    }

    // When the previous enemy shot was not successful and he has not found a ship
    // so he randomly chooses for the next cell
    public void ShootingWithoutPreviousSuccess() {
        int x, y;
        // Choose randomly
        x = rand.nextInt(10);
        y = rand.nextInt(10);

        // Keeping track of whether the enemy shot for this round or not
        this.shot = enemy_ships.shot;

        first_try = true;

        // If layout value for these coords equals "X" or "Y" the cell has already been hit so search randomly for another cell
        // that has not been previously hit
        while (player_ships.layout[x][y] == "X" || player_ships.layout[x][y] == "Y") {
            x = rand.nextInt(10);
            y = rand.nextInt(10);
        }
        // Make a shot to x,y and change the points and the ships conditions if needed
        Pair<String, Boolean> shot_result = player_ships.changeLayout(x, y);
        enemy_points = enemy_ships.Count(shot_result.getKey(), player_ships.num_of_remaining_cells);
        hit_cell = shot_result.getKey();
        enemy_ships.shot = true; // The enemy shot for this round is complete
        actual_x = x;
        actual_y = y;

        // If an opponent's ship was hit
        if (shot_result.getKey() != "-") { // We have made sure that we wouldn't shoot a cell that has already been shot
            // The enemy managed to hit a player's ship
            // Keep the first successful shot
            enemy_ships.successful_shot_x = x;
            enemy_ships.successful_shot_y = y;

            // Declares that the enemy hit the opponent with his shot
            success = true;
            enemy_ships.number_of_successful_shots++;
        }
    }

    // When the previous shot was successful the enemy stops shooting randomly and he shoots specifically
    // trying to sink the opponent's ship

    public void ShootSpecifically(Pair<Integer, Integer> new_coords, Pair<Integer, Integer> old_coords) {
        int x = new_coords.getKey();
        int y = new_coords.getValue();
        // Make the shot
        Pair<String, Boolean> shot_result = player_ships.changeLayout(x, y);
        enemy_points = enemy_ships.Count(shot_result.getKey(), player_ships.num_of_remaining_cells);
        enemy_ships.shot = true;
        hit_cell = shot_result.getKey();
        actual_x = x;
        actual_y = y;


        // The enemy found one more cell of the ship
        if (shot_result.getKey() != "-" && shot_result.getKey() != "Y" && shot_result.getKey() != "X") {
            first_try = false;
            found_rest_of_ship = true;

            // Check if the ship has sunk, find the direction of the shot and create the lists with the possible next shots
            ship_down = player_ships.sunk_state;
            enemy_ships.number_of_successful_shots++;
            FindShotDirection(actual_x, actual_y, enemy_ships.successful_shot_x, enemy_ships.successful_shot_y);
            listChoices(enemy_ships.successful_shot_x, enemy_ships.successful_shot_y);

            // If the ship has sunk
            // Re-initialize all the boolean variables in case we find another one of the opponents ships
            if (ship_down) {
                success = false;
                found_rest_of_ship = false;
                ship_down = false;
                orientation = "";
                first_try = true;
                enemy_ships.successful_shot_x = -1;
                enemy_ships.successful_shot_y = -1;
                left_choices.clear();
                right_choices.clear();
                up_choices.clear();
                down_choices.clear();
                coords.clear();

            } else {
                //Keep the first successful shot in parent_shot variable
                parent_shot = new Pair(enemy_ships.successful_shot_x, enemy_ships.successful_shot_y);
                enemy_ships.successful_shot_x = x;
                enemy_ships.successful_shot_y = y;
                first_try = false;

                // Find the shot direction
                String direction = FindShotDirection(x, y, old_coords.getKey(), old_coords.getValue());
                orientation = direction;
                success = true;
            }
        } else {
            // The enemy shot specifically but he did not manage to find the rest of the ship
            found_rest_of_ship = false;
            success = false;
        }
    }

    // Creates the coords arraylist
    // Coords stores all the neighbors of the first cell of the ship that the enemy found
    // so that he tries shooting in these cells until he finds the rest of the ship
    // Maximum 4 values will be kept in coords, depending on the position of the cell that was hit

    public ArrayList<Pair> PossibleShots(int x, int y) {
        parent_shot = new Pair(x, y);
        if (x >= 0 && y >= 0 && x <= 9 && y <= 9) {
            if (x - 1 >= 0 ){
                Pair<Integer, Integer> pos1 = new Pair(x - 1, y);
                coords.add(pos1);
            }
            if (x + 1 <= 9 ){
                Pair<Integer, Integer> pos2 = new Pair(x + 1, y);
                coords.add(pos2);
            }
            if (y - 1 >= 0 ){
                Pair<Integer, Integer> pos3 = new Pair(x, y - 1);
                coords.add(pos3);
            }
            if (y + 1 <= 9 ){
                Pair<Integer, Integer> pos4 = new Pair(x, y + 1);
                coords.add(pos4);
            }
        }

        return coords;
    }

    // Creates the left_choices, right_choices, up_choices, down_choices lists
    // depending on the orientation of the list
    // only adds the cells in the lists if they are not the first shot made on the ship and
    // generally if they have not been previously shot
    public void listChoices(int x, int y) {
        if(orientation.equals("horizontal-left")||orientation.equals("horizontal-right")) {
            for (int i = enemy_ships.successful_shot_y - 1; i >= 0; i--) {
                if (i != parent_shot.getValue() && player_ships.layout[x][i] != "X" && player_ships.layout[x][i] != "Y") {
                    Pair<Integer, Integer> c_1 = new Pair(x, i);
                    if(!left_choices.contains(c_1))
                        left_choices.add(c_1);
                }
            }

            for (int i = enemy_ships.successful_shot_y + 1; i <= 9; i++) {
                if (i != parent_shot.getValue() && player_ships.layout[x][i] != "X" && player_ships.layout[x][i] != "Y") {
                    Pair<Integer, Integer> c_1 = new Pair(x, i);
                    if(!right_choices.contains(c_1))
                        right_choices.add(c_1);
                }
            }
        }

        else if(orientation.equals("vertical-up")||orientation.equals("vertical-down")) {
            for (int i = enemy_ships.successful_shot_x - 1; i >= 0; i--) {
                if (i != parent_shot.getKey() && player_ships.layout[i][y] != "X" && player_ships.layout[i][y] != "Y") {
                    Pair<Integer, Integer> c_1 = new Pair(i, y);
                    if(!up_choices.contains(c_1))
                        up_choices.add(c_1);
                }
            }

            for (int i = enemy_ships.successful_shot_x + 1; i <= 9; i++) {
                if (i != parent_shot.getKey() && player_ships.layout[i][y] != "X" && player_ships.layout[i][y] != "Y") {
                    Pair<Integer, Integer> c_1 = new Pair(i, y);
                    if(!down_choices.contains(c_1))
                        down_choices.add(c_1);
                }
            }
        }
    }

    public void ChoosingCell() {
        //Search for a neighbor of the initial shot that hasn't been tried out
        int x = 0, y = 0;

        if (ship_down) {
            orientation = "";
            enemy_ships.successful_shot_x = -1;
            enemy_ships.successful_shot_y = -1;
            success = false;
            found_rest_of_ship = false;
            first_try = true;
            ship_down = false;
            left_choices.clear();
            right_choices.clear();
            up_choices.clear();
            down_choices.clear();
            coords.clear();
            Shoot();
        }
        else {
            while (!coords.isEmpty()) {
                x = (int) coords.get(0).getKey();
                y = (int) coords.get(0).getValue();

                coords.remove(0);

                // So that an already hit cell is not hit again
                if (player_ships.layout[x][y] != "Y") {
                    break;
                }
            }
            Pair<Integer, Integer> new_shot = new Pair(x, y);
            ShootSpecifically(new_shot, parent_shot);
        }
    }

    // Find the direction that the shots must have

    private String FindShotDirection(int x, int y, int x_old, int y_old) {
        //
        if (y - y_old == 0 && x - x_old < 0) {
            // Empty coords list to be ready for the next time we need them
            coords.clear();
            orientation = "vertical-up";

        }
        // Down from the initial cell
        else if (y - y_old== 0 && x - x_old > 0) {
            coords.clear();
            orientation = "vertical-down";

        }
        // To the left of the initial cell
        else if (x - x_old == 0 && y - y_old < 0) {
            coords.clear();
            orientation = "horizontal-left";

        }
        // To the right of the initial cell
        else if (x - x_old == 0 && y - y_old > 0) {
            coords.clear();
            orientation = "horizontal-right";
        }
        return orientation;
    }

    // The enemy shoots
    public static void enemyShoots(){
        String hit_cell;
        int x , y;

        if (b_player.shot && b_enemy.number_of_shots < 40 && !game_over) {
            Pair<String, Pair<Integer, Integer>> res = enemy_shots.Shoot();
            hit_cell = res.getKey();
            x = res.getValue().getKey();
            y = res.getValue().getValue();
            b_enemy.shot = true;
            b_player.shot = false;
            Pair<String, String> p_info;

            // If the enemy hits the player then the color of the cell changes to red
            // A delay is added using the PauseTransition so that it looks like the enemy is waiting for the player to finish his shot
            if (hit_cell != "-") {
                PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
                pause.setOnFinished(event ->
                        disButtons[x+1][y+1].setStyle("-fx-background-color: rgb(255, 77, 77);")
                );
                pause.play();
                p_info = new Pair<>("successful", hit_cell);
            }
            // If the enemy hits the sea instead of the player's ships then the color of the cell changes to blue
            else {
                PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
                pause.setOnFinished(event ->
                        //blue
                        disButtons[x+1][y+1].setStyle("-fx-background-color: linear-gradient(to bottom, rgb(0, 102, 204), rgb(0, 128, 255), rgb(102, 179, 255));")
                );
                pause.play();
                p_info = new Pair<>("unsuccessful", hit_cell);
            }
            // If p equals 1 the player starts shooting
            // if p equals -1 the enemy starts shooting
            if(p==1) {
                PauseTransition pause_enemy = new PauseTransition(Duration.seconds(0.5));
                pause_enemy.setOnFinished(event ->
                        {
                            enemy_shots_num.setText(String.valueOf(100*b_enemy.number_of_successful_shots/b_enemy.number_of_shots) +"%");
                            enemy_points_num.setText(String.valueOf(enemy_points));
                            enemy_remaining_ships_num.setText(String.valueOf(b_enemy.num_of_remaining_ships));
                        }
                );
                pause_enemy.play();
            }
            else{
                 PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
                 pause.setOnFinished(event ->
                        {
                            enemy_shots_num.setText(String.valueOf(100*b_enemy.number_of_successful_shots/b_enemy.number_of_shots) +"%");
                            enemy_points_num.setText(String.valueOf(enemy_points));
                            player_remaining_ships_num.setText(String.valueOf(b_player.num_of_remaining_ships));
                            enemy_remaining_ships_num.setText(String.valueOf(b_enemy.num_of_remaining_ships));
                        }
                );
                pause.play();

            }
            // Check if one of the player's ships sunk after the shot was made and change the color of all
            // of the ship's cells to dark red
            if(b_player.new_cruiser.getSunkState()){
                for(int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        int a = i+1;
                        int b = j+1;
                        if (b_player.layout_initial[i][j] == "cruiser") {
                            PauseTransition pause = new PauseTransition(Duration.seconds(0.7));
                            pause.setOnFinished(event ->
                                    disButtons[a][b].setStyle("-fx-background-color: linear-gradient(to bottom, " +
                                            "rgb(230, 0, 0), rgb(204, 0, 0), rgb(128, 0, 0), rgb(77, 0, 0));")
                            );
                            pause.play();
                        }
                    }
                }
            }
            if(b_player.new_carrier.getSunkState()){
                for(int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        int a = i+1;
                        int b = j+1;
                        if (b_player.layout_initial[i][j] == "carrier") {
                            PauseTransition pause = new PauseTransition(Duration.seconds(0.7));
                            pause.setOnFinished(event ->
                                    disButtons[a][b].setStyle("-fx-background-color: linear-gradient(to bottom, " +
                                            "rgb(230, 0, 0), rgb(204, 0, 0), rgb(128, 0, 0), rgb(77, 0, 0));")
                            );
                            pause.play();
                        }
                    }
                }
            }
            if(b_player.new_battleship.getSunkState()){
                for(int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        int a = i+1;
                        int b = j+1;
                        if (b_player.layout_initial[i][j] == "battleship") {
                            PauseTransition pause = new PauseTransition(Duration.seconds(0.7));
                            pause.setOnFinished(event ->
                                    disButtons[a][b].setStyle("-fx-background-color: linear-gradient(to bottom, " +
                                            "rgb(230, 0, 0), rgb(204, 0, 0), rgb(128, 0, 0), rgb(77, 0, 0));")
                            );
                            pause.play();
                        }
                    }
                }
            }
            if(b_player.new_destroyer.getSunkState()){
                for(int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        int a = i+1;
                        int b = j+1;
                        if (b_player.layout_initial[i][j] == "destroyer") {
                            PauseTransition pause = new PauseTransition(Duration.seconds(0.7));
                            pause.setOnFinished(event ->
                                    disButtons[a][b].setStyle("-fx-background-color: linear-gradient(to bottom, " +
                                            "rgb(230, 0, 0), rgb(204, 0, 0), rgb(128, 0, 0), rgb(77, 0, 0));")
                            );
                            pause.play();
                        }
                    }
                }
            }
            if(b_player.new_submarine.getSunkState()){
                for(int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        int a = i+1;
                        int b = j+1;
                        if (b_player.layout_initial[i][j] == "submarine") {
                            PauseTransition pause = new PauseTransition(Duration.seconds(0.7));
                            pause.setOnFinished(event ->
                                    disButtons[a][b].setStyle("-fx-background-color: linear-gradient(to bottom, " +
                                            "rgb(230, 0, 0), rgb(204, 0, 0), rgb(128, 0, 0), rgb(77, 0, 0));")
                            );
                            pause.play();
                        }
                    }
                }
            }
            Pair<Integer, Integer> p_coords = new Pair<>(x,y);
            Pair<Pair<Integer, Integer>, Pair<String, String>> pr = new Pair<>(p_coords, p_info);
            enemy_five_shots.add(pr);

        }
    }

    // Used for displaying the info for the condition of the enemy's ships when the button "Enemy Ships" from the menu "Details" is clicked
    static void enemyShipsCondition(){
        String content;

        try{
            //Carrier
            String carrier_state = "";
            if (b_enemy.new_carrier.getUntouchedState()) {
                carrier_state = "untouched";
            } else if (b_enemy.new_carrier.getHitState() && !b_enemy.new_carrier.getSunkState()) {
                carrier_state = "hit";
            } else if (b_enemy.new_carrier.getSunkState()) {
                carrier_state = "sunk";
            }

            //Battleship
            String battleship_state = "";
            if (b_enemy.new_battleship.getUntouchedState()) {
                battleship_state = "untouched";
            } else if (b_enemy.new_battleship.getHitState() && !b_enemy.new_battleship.getSunkState()) {
                battleship_state = "hit";
            } else if (b_enemy.new_battleship.getSunkState()) {
                battleship_state = "sunk";
            }

            //Cruiser
            String cruiser_state = "";
            if (b_enemy.new_cruiser.getUntouchedState()) {
                cruiser_state = "untouched";
            } else if (b_enemy.new_cruiser.getHitState() && !b_enemy.new_cruiser.getSunkState()) {
                cruiser_state = "hit";
            } else if (b_enemy.new_cruiser.getSunkState()) {
                cruiser_state = "sunk";
            }

            //Submarine
            String submarine_state = "";
            if (b_enemy.new_submarine.getUntouchedState()) {
                submarine_state = "untouched";
            } else if (b_enemy.new_submarine.getHitState() && !b_enemy.new_submarine.getSunkState()) {
                submarine_state = "hit";
            } else if (b_enemy.new_submarine.getSunkState()) {
                submarine_state = "sunk";
            }

            //Destroyer
            String destroyer_state = "";
            if (b_enemy.new_destroyer.getUntouchedState()) {
                destroyer_state = "untouched";
            } else if (b_enemy.new_destroyer.getHitState() && !b_enemy.new_destroyer.getSunkState()) {
                destroyer_state = "hit";
            } else if (b_enemy.new_destroyer.getSunkState()) {
                destroyer_state = "sunk";
            }
            content = "The carrier is " + carrier_state + "\n"
                    + "The battleship is " + battleship_state + "\n"
                    + "The cruiser is " + cruiser_state + "\n"
                    + "The submarine is " + submarine_state + "\n"
                    + "The destroyer is " + destroyer_state;
            CustomDialogShipsCondition dialog = new CustomDialogShipsCondition("Information about enemy's ships", content);
            dialog.openDialog();
        }catch (Exception e){
            String txt = "The game has not started yet!";
            CustomDialog dialog = new CustomDialog("Information about enemy's ships", txt);
            dialog.openDialog();
        }

    }
    // Check if it is enemy's turn to shoot
    static void enemyStartsCheck(){
        if(b_player.shot && !b_enemy.shot){
            enemyShoots();
        }
    }

    // Check whether there is a winner or not
    // Either 40 shots are made from both players so we check who is the winner by checking who has gained more points
    // or one of the players has sunk all of his enemy's ships and so the game finishes with him as the winner

    public static void checkWinner() {

        boolean enemy_sunk = b_enemy.new_battleship.getSunkState() && b_enemy.new_submarine.getSunkState() && b_enemy.new_destroyer.getSunkState()
                && b_enemy.new_cruiser.getSunkState() && b_enemy.new_carrier.getSunkState(),
                player_sunk = b_player.new_battleship.getSunkState() && b_player.new_submarine.getSunkState() && b_player.new_destroyer.getSunkState()
                        && b_player.new_cruiser.getSunkState() && b_player.new_carrier.getSunkState();


        if (enemy_sunk) {
            game_over = true;
            CustomDialog dialog = new CustomDialog("THE GAME FINISHED", "The winner is player! You sank all enemy's ships!");
            dialog.openDialog();
            imagePopupWindowShow();

        } else if (player_sunk) {
            game_over = true;
            CustomDialog dialog = new CustomDialog("THE GAME FINISHED", "The winner is enemy! All player's ships sank!");
            dialog.openDialog();
            imagePopupWindowShow();

        }
        else if (b_player.number_of_shots == 40 && b_enemy.number_of_shots == 40) {
            game_over = true;
            if (player_points > enemy_points) {
                winner = "player";
            } else if (player_points < enemy_points) {
                winner = "enemy";
            } else {
                winner = "tight";
            }
            if (!winner.equals("tight")) {
                CustomDialog dialog = new CustomDialog("THE GAME FINISHED", "The winner is " + winner +" (all 40 shots were made)");
                dialog.openDialog();

            } else {
                CustomDialog dialog = new CustomDialog("THE GAME FINISHED", "The game is a tight (all 40 shots were made)");
                dialog.openDialog();
            }
            imagePopupWindowShow();
        }
    }

}

