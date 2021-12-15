package sample;

import dialogs.CustomDialog;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import static sample.EnemyShots.*;
import static sample.Main.*;
import static sample.PlayerShots.player_points;

public class CreateLayout {

    // Create the grid that shows the enemy's ships
    // It consists of buttons which the user clicks in order to shoot to a cell with specific coordinates
    private static void playerGrid(){
        for (int i = 0; i < 11; i++) {
            playerGrid.getColumnConstraints().add(new ColumnConstraints(30));
            playerGrid.getRowConstraints().add(new RowConstraints(30));
        }

        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (i == 0 && j != 0) {
                    Label num = new Label(String.valueOf(j-1));
                    GridPane.setConstraints(num, j, i);
                    playerGrid.getChildren().add(num);
                }
                else if (j == 0 && i != 0) {
                    Label num = new Label(String.valueOf(i-1));
                    GridPane.setConstraints(num, j, i);
                    playerGrid.getChildren().add(num);
                }
                else if (i != 0 && j != 0){
                    Button button = new Button();
                    button.setId("button");
                    button.onMouseClickedProperty();
                    button.setPrefHeight(30);
                    button.setPrefWidth(30);

                    GridPane.setConstraints(button, j, i);
                    playerGrid.getChildren().add(button);

                    button.setDisable(true);

                    buttons[i][j] = button;

                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            if (b_enemy.shot && b_player.number_of_shots < 40 && !game_over) {
                                PlayerShots s = new PlayerShots(b_player, b_enemy, GridPane.getRowIndex(button) - 1, GridPane.getColumnIndex(button) - 1);
                                String hit_cell = s.Shoot();

                                b_enemy.shot = false;
                                b_player.shot = true;
                                Pair<String, String> p_info;
                                if (hit_cell != "-") {
                                    p_info = new Pair<>("successful", hit_cell);
                                    // If the player manages to hit one of the enemy's ships the color of the corresponding cell changes
                                    button.setStyle("-fx-background-color: rgb(255, 77, 77);");

                                    // The button is disabled so that the user does not shoot the same cell twice
                                    button.setDisable(true);
                                    if(b_enemy.new_cruiser.getSunkState()){
                                        for(int ii = 0; ii < 10; ii++) {
                                            for (int jj = 0; jj < 10; jj++) {
                                                int a = ii+1;
                                                int b = jj+1;
                                                if (b_enemy.layout_initial[ii][jj] == "cruiser") {
                                                    buttons[a][b].setStyle("-fx-background-color: linear-gradient(to bottom, " +
                                                            "rgb(230, 0, 0), rgb(204, 0, 0), rgb(128, 0, 0), rgb(77, 0, 0));");                                 }

                                            }
                                        }
                                    }
                                    // If one of the enemy's ships gets sunk all of its cells turn to a dark red color
                                    // to indicate to the user that there are no more not hit cells of the specific ship
                                    if(b_enemy.new_carrier.getSunkState()){
                                        for(int ii = 0; ii < 10; ii++) {
                                            for (int jj = 0; jj < 10; jj++) {
                                                int a = ii+1;
                                                int b = jj+1;
                                                if (b_enemy.layout_initial[ii][jj] == "carrier") {
                                                    buttons[a][b].setStyle("-fx-background-color: linear-gradient(to bottom, " +
                                                            "rgb(230, 0, 0), rgb(204, 0, 0), rgb(128, 0, 0), rgb(77, 0, 0));");
                                                }

                                            }
                                        }
                                    }
                                    if(b_enemy.new_battleship.getSunkState()){
                                        for(int ii = 0; ii < 10; ii++) {
                                            for (int jj = 0; jj < 10; jj++) {
                                                int a = ii+1;
                                                int b = jj+1;
                                                if (b_enemy.layout_initial[ii][jj] == "battleship") {
                                                    buttons[a][b].setStyle("-fx-background-color: linear-gradient(to bottom, " +
                                                            "rgb(230, 0, 0), rgb(204, 0, 0), rgb(128, 0, 0), rgb(77, 0, 0));");
                                                }

                                            }
                                        }
                                    }
                                    if(b_enemy.new_destroyer.getSunkState()){
                                        for(int ii = 0; ii < 10; ii++) {
                                            for (int jj = 0; jj < 10; jj++) {
                                                int a = ii+1;
                                                int b = jj+1;
                                                if (b_enemy.layout_initial[ii][jj] == "destroyer") {
                                                    buttons[a][b].setStyle("-fx-background-color: linear-gradient(to bottom, " +
                                                            "rgb(230, 0, 0), rgb(204, 0, 0), rgb(128, 0, 0), rgb(77, 0, 0));");
                                                }

                                            }
                                        }
                                    }
                                    if(b_enemy.new_submarine.getSunkState()){
                                        for(int ii = 0; ii < 10; ii++) {
                                            for (int jj = 0; jj < 10; jj++) {
                                                int a = ii+1;
                                                int b = jj+1;
                                                if (b_enemy.layout_initial[ii][jj] == "submarine") {
                                                    buttons[a][b].setStyle("-fx-background-color: linear-gradient(to bottom, " +
                                                            "rgb(230, 0, 0), rgb(204, 0, 0), rgb(128, 0, 0), rgb(77, 0, 0));");
                                                }
                                            }

                                        }
                                    }

                                }
                                // If there is not a ship at the cell that is hit, the cell's color changes to a blue color to indicate that
                                // the shot hit the sea
                                else {
                                    p_info = new Pair<>("unsuccessful", hit_cell);
                                    button.setStyle("-fx-background-color: linear-gradient(to bottom, rgb(0, 102, 204), rgb(0, 128, 255), rgb(102, 179, 255));");
                                    button.setDisable(true);
                                }
                                player_shots_num.setText(String.valueOf(100*b_player.number_of_successful_shots/b_player.number_of_shots) +"%");
                                player_points_num.setText(String.valueOf(player_points));
                                player_remaining_ships_num.setText(String.valueOf(b_player.num_of_remaining_ships));
                                enemy_remaining_ships_num.setText(String.valueOf(b_enemy.num_of_remaining_ships));
                                Pair<Integer, Integer> p_coords = new Pair<>(GridPane.getRowIndex(button)-1,GridPane.getColumnIndex(button)-1);
                                Pair<Pair<Integer, Integer>, Pair<String, String>> pr = new Pair<>(p_coords, p_info);
                                player_five_shots.add(pr);

                                if(p==1){
                                    enemyStartsCheck();
                                    checkWinner();
                                }
                                else{
                                    checkWinner();
                                    if(!game_over)
                                        enemyStartsCheck();
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    // Create the grid that shows the player's ships
    // It consists of buttons which are disabled and cannot be clicked by the the user
    // The colors change as described above, everytime the enemy shoots
    private static Button[][] enemyGrid(){
        for (int i = 0; i < 11; i++) {
            enemyGrid.getColumnConstraints().add(new ColumnConstraints(30));
            enemyGrid.getRowConstraints().add(new RowConstraints(30));
        }
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (j == 0 && i != 0 ){
                    Label num = new Label(String.valueOf(i-1));
                    GridPane.setConstraints(num, j, i);
                    enemyGrid.getChildren().add(num);
                }
                else if (i == 0 && j != 0){
                    Label num = new Label(String.valueOf(j-1));
                    GridPane.setConstraints(num, j, i);
                    enemyGrid.getChildren().add(num);
                }
                else if(i != 0 && j != 0) {
                    Button button = new Button();
                    button.setId("dis_button");
                    button.setDisable(true);
                    button.setPrefHeight(30);
                    button.setPrefWidth(30);

                    GridPane.setConstraints(button, j, i);

                    enemyGrid.getChildren().add(button);
                    disButtons[i][j] = button;
                }
            }
        }
        return disButtons;
    }

    // Places the labels showing the game's information (such as success percentage) to the right positions
    private static void addRest(){
        enemy_p.getChildren().addAll(enemy_points_l,enemy_points_num);
        enemy_p.setSpacing(5);
        enemy_s.getChildren().addAll(enemy_shots_l, enemy_shots_num);
        enemy_s.setSpacing(5);
        enemy_r_s.getChildren().addAll(enemy_remaining_ships,enemy_remaining_ships_num);
        enemy_r_s.setSpacing(5);

        player_p.getChildren().addAll(player_points_l,player_points_num);
        player_p.setSpacing(5);
        player_s.getChildren().addAll(player_shots_l, player_shots_num);
        player_s.setSpacing(5);
        player_r_s.getChildren().addAll(player_remaining_ships,player_remaining_ships_num);
        player_r_s.setSpacing(5);

        enemyResults.getChildren().add(enemy_l);
        enemyResults.getChildren().add(enemy_r_s);
        enemyResults.getChildren().add(enemy_p);
        enemyResults.getChildren().add(enemy_s);

        playerResults.getChildren().add(player_l);
        playerResults.getChildren().add(player_r_s);
        playerResults.getChildren().add(player_p);
        playerResults.getChildren().add(player_s);

    }

    // Creates the menu bar
    static void createMenuBar(){
        Menu app = new Menu("Application");
        Menu details = new Menu("Details");

        // create menu items
        MenuItem app1 = new MenuItem("Start");
        MenuItem app2 = new MenuItem("Load");
        MenuItem app3 = new MenuItem("Exit");

        MenuItem det1 = new MenuItem("Enemy Ships");
        MenuItem det2 = new MenuItem("Player Shots");
        MenuItem det3 = new MenuItem("Enemy Shots");

        // add menu items to menu
        app.getItems().add(app1);
        app.getItems().add(app2);
        app.getItems().add(app3);

        details.getItems().add(det1);
        details.getItems().add(det2);
        details.getItems().add(det3);

        // create events for menu items
        // When the "Start" button is clicked
        EventHandler<ActionEvent> event_start = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                if(load_done)
                    restart();
                else{
                    CustomDialog dialog = new CustomDialog("", "Please load the game before trying to start it!");
                    dialog.openDialog();
                }
            }
        };

        // When the "Load" button is clicked
        EventHandler<ActionEvent> event_load = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                id = loadPopUp();
            }
        };

        // When the "Exit" button is clicked
        EventHandler<ActionEvent> event_exit = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                Platform.exit();
            }
        };

        // When the "Enemy Ships" button is clicked
        EventHandler<ActionEvent> event_enemy_ships = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                if(restart_done)
                    enemy_shots.enemyShipsCondition();
                else{
                    CustomDialog dialog = new CustomDialog("", "The game has not started yet!");
                    dialog.openDialog();
                }
            }
        };

        // When the "Player Shots" button is clicked
        EventHandler<ActionEvent> event_player_shots = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                if(restart_done)
                    fiveShots(player_five_shots, "player");
                else{
                    CustomDialog dialog = new CustomDialog("", "The game has not started yet!");
                    dialog.openDialog();
                }
            }
        };

        // When the "Enemy Shots" button is clicked
        EventHandler<ActionEvent> event_enemy_shots = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                if (restart_done)
                    fiveShots(enemy_five_shots, "enemy");
                else{
                    CustomDialog dialog = new CustomDialog("", "The game has not started yet!");
                    dialog.openDialog();
                }
            }
        };

        // add event
        app1.setOnAction(event_start);
        app2.setOnAction(event_load);
        app3.setOnAction(event_exit);

        det1.setOnAction(event_enemy_ships);
        det2.setOnAction(event_player_shots);
        det3.setOnAction(event_enemy_shots);

        // create a menubar
        MenuBar mb = new MenuBar();

        // add menu to menubar
        mb.getMenus().addAll(app, details);
        vb.getChildren().add(mb);

    }

    // Creates the final layout of all the above items e.g the two grids, the labels, the menu bar
    static void createLayout(){

        // create the legend
        rectangle1 = new Rectangle(25, 25);
        rectangle1.setStyle("-fx-fill: rgb(204, 0, 68)");
        rectangle2 = new Rectangle(25, 25);
        rectangle2.setStyle("-fx-fill: linear-gradient(to bottom, rgb(0, 102, 204), rgb(0, 128, 255), rgb(102, 179, 255));");
        rectangle3 = new Rectangle(25, 25);
        rectangle3.setStyle("-fx-fill: rgb(255, 77, 77);");
        rectangle4 = new Rectangle(25, 25);
        rectangle4.setStyle("-fx-fill: linear-gradient(to bottom, rgb(230, 0, 0), rgb(204, 0, 0), rgb(128, 0, 0), rgb(77, 0, 0));");
        legend.add(rectangle1, 1, 1);
        legend.add(rectangle2, 1, 2);
        legend.add(rectangle3, 1, 3);
        legend.add(rectangle4, 1, 4);
        legend.add(legend1, 2, 1);
        legend.add(legend2, 2, 2);
        legend.add(legend3, 2, 3);
        legend.add(legend4, 2, 4);
        legend.setVgap(10);
        legend.setHgap(30);
        legend.setAlignment(Pos.BASELINE_LEFT);

        playerGrid();
        enemyGrid();
        addRest();

        bothResults.getChildren().addAll(enemyResults, playerResults);

        gridContainer.getChildren().add(enemyGrid);
        gridContainer.getChildren().add(playerGrid);
        gridContainer.setSpacing(200);
        gridContainer.setAlignment(Pos.CENTER);

        bothResults.setAlignment(Pos.CENTER);
        bothResults.setSpacing(390);
        bothResults.setFillHeight(false);

        info.setSpacing(20);

        createMenuBar();
        String background = Main.class.getResource("../images/background.png").toExternalForm();
        ImageView c = new ImageView(background);
        f.setId("background");
        info.getChildren().addAll(vb, bothResults, gridContainer, legend );
        f.getStylesheets().add("stylesheets/background.css");
        f.getChildren().addAll(info);

        if(p==-1){
            enemyShoots();
        }
    }
}
