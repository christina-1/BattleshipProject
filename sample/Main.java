package sample;

import dialogs.CustomDialog;
import dialogs.CustomDialogShots;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import static sample.CreateLayout.createLayout;
import static sample.EnemyShots.enemyShoots;

public class Main extends Application implements EventHandler<ActionEvent> {
    static int p;
    static Boat b_player, b_enemy;
    static EnemyShots enemy_shots;
    static String winner = "";

    public static String id;
    private static SearchMedialabFolder files = new SearchMedialabFolder();
    public static boolean exception_caught = false, load_done = false, restart_done = false, game_over = false;
    public static Stage stage;
    static Scene scene2;
    public static StackPane f = new StackPane();
    public static GridPane playerGrid = new GridPane(), enemyGrid = new GridPane(), legend = new GridPane();
    public static HBox gridContainer = new HBox(); // so that the two grids are placed one next to the other
    public static VBox info = new VBox(), enemyResults = new VBox(), playerResults = new VBox(), vb = new VBox();
    public static HBox bothResults = new HBox(), enemy_p = new HBox(), enemy_s = new HBox(), enemy_r_s = new HBox(),
                    player_p = new HBox(), player_s = new HBox(), player_r_s = new HBox();
    public  static Rectangle rectangle1, rectangle2, rectangle3, rectangle4;
    static ArrayList<Pair<Pair<Integer, Integer>, Pair<String, String>>> enemy_five_shots = new ArrayList<>(), player_five_shots = new ArrayList<>();

    static Label enemy_l = new Label("Enemy:"), enemy_points_l = new Label("Points: "), enemy_points_num = new Label(),
                 enemy_shots_l = new Label("Percentage of successful shots: "), enemy_shots_num = new Label(),
                 enemy_remaining_ships = new Label("Remaining ships: "), enemy_remaining_ships_num = new Label(),
                 player_l = new Label("Player:"), player_points_l = new Label("Points: "), player_points_num = new Label(),
                 player_shots_l = new Label("Percentage of successful shots: "), player_shots_num = new Label(),
                 player_remaining_ships = new Label("Remaining ships: "), player_remaining_ships_num = new Label(),
                 legend1 = new Label("Player's ship"), legend2 = new Label("Sea"), legend3 = new Label("Ship"), legend4 = new Label("Ship sunk");

    public static Button[][] buttons = new Button[11][11], disButtons = new Button[11][11];

    // Initialize player and enemy grid
    private static void callInitiazer(){
        Initializer initial;

        initial = new Initializer("player_SCENARIO-" + id + ".txt", "enemy_SCENARIO-" + id + ".txt");
        enemy_five_shots.clear();
        player_five_shots.clear();

        if(exception_caught){
            // Re-initialize
            exception_caught = false;

            for(int i = 0; i < 10; i++){
                for(int j = 0; j < 10; j++){
                    disButtons[i+1][j+1].setStyle("");
                    buttons[i+1][j+1].setStyle("");
                    buttons[i+1][j+1].onMouseClickedProperty();
                    buttons[i+1][j+1].setDisable(true);
                }
            }
            load_done = false;
            b_player = null;
            b_enemy = null;
        }
        else{
            b_player = initial.b_player;
            b_enemy = initial.b_enemy;
            for(int i = 0; i < 10; i++){
                for(int j = 0; j < 10; j++){
                    disButtons[i+1][j+1].setStyle("");
                    buttons[i+1][j+1].setStyle("");
                    buttons[i+1][j+1].onMouseClickedProperty();
                    buttons[i+1][j+1].setDisable(false);

                    if(b_player.layout_initial[i][j] != "-"){
                        // Show player's ships on the board before the game starts
                        disButtons[i+1][j+1].setStyle("-fx-background-color: rgb(204, 0, 68)");
                    }
                }
            }
            // Randomly choose who will shoot first
            // if p = 1 player shoots first
            // if p = -1 enemy shoots first
            Random random = new Random();
            p = random.nextBoolean() ? 1 : -1;
            load_done = true;
        }
    }

    // Create a "game-over" explosion animation everytime we have a winner and the game finishes
    public static void imagePopupWindowShow() {
        String exp1 = Main.class.getResource("../images/e1.png").toExternalForm();
        String exp2 = Main.class.getResource("../images/e2.png").toExternalForm();
        String exp3 = Main.class.getResource("../images/e3.png").toExternalForm();
        String exp7 = Main.class.getResource("../images/e7.png").toExternalForm();
        String game_over = Main.class.getResource("../images/game_over.png").toExternalForm();
        String sea = Main.class.getResource("../images/background.png").toExternalForm();


        final ImageView exp_1 = new ImageView(exp1);
        final ImageView exp_2 = new ImageView(exp2);
        final ImageView exp_3 = new ImageView(exp3);
        final ImageView exp_7 = new ImageView(exp7);
        final ImageView game_over_view = new ImageView(game_over);
        final ImageView sea_view = new ImageView(sea);


        exp_1.setFitHeight(500);
        exp_1.setFitWidth(500);
        exp_1.setPreserveRatio(true);

        exp_2.setFitHeight(500);
        exp_2.setFitWidth(500);
        exp_2.setPreserveRatio(true);


        exp_3.setFitHeight(500);
        exp_3.setFitWidth(500);
        exp_3.setPreserveRatio(true);

        exp_7.setFitHeight(500);
        exp_7.setFitWidth(500);
        exp_7.setPreserveRatio(true);

        game_over_view.setFitHeight(500);
        game_over_view.setFitWidth(500);
        game_over_view.setPreserveRatio(true);

        sea_view.setTranslateX(-200);
        sea_view.setTranslateY(-150);
        sea_view.setFitHeight(800);
        sea_view.setFitWidth(1000);

        Group explosion = new Group(sea_view, exp_1);
        explosion.setTranslateX(200);
        explosion.setTranslateY(120);
        Timeline t = new Timeline();
        t.getKeyFrames().add(new KeyFrame(
                Duration.millis(500),
                (ActionEvent event) -> {
                    explosion.getChildren().set(1, exp_2);
                }
        ));
        t.getKeyFrames().add(new KeyFrame(
                Duration.millis(650),
                (ActionEvent event) -> {
                    explosion.getChildren().set(1, exp_3);
                }
        ));
        t.getKeyFrames().add(new KeyFrame(
                Duration.millis(2200),
                (ActionEvent event) -> {
                    explosion.getChildren().set(1, exp_7);
                }
        ));
        t.getKeyFrames().add(new KeyFrame(
                Duration.millis(2800),
                (ActionEvent event) -> {
                    explosion.getChildren().set(1, game_over_view);
                }
        ));
        t.play();

        getPrimaryStage().setScene(new Scene(explosion,1000, 700));


        final StackPane i = new StackPane();
        i.setId("background");
        i.getStylesheets().add("stylesheets/background.css");
        i.getChildren().addAll(getInfo());
        String explosion_sound = "./src/sounds/explosion.wav";

        try {
            File explosion_musicPath = new File(explosion_sound);
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(explosion_musicPath);
            Clip clip_exp = AudioSystem.getClip();
            clip_exp.open(audioInput);

            PauseTransition pause_exp = new PauseTransition(Duration.seconds(0.3));
            pause_exp.setOnFinished(event ->
                clip_exp.start()
            );
            pause_exp.play();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        getPrimaryStage().getIcons().add(new Image("/images/jetFighter1.png"));
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event ->
                getPrimaryStage().setScene(new Scene(i, 1000, 700))
        );
        pause.play();
    }

    // Used to initialize the game ( clear the labels showing the game information )
    private static void Initialize(){
        player_points_num.setText("");
        player_shots_num.setText("");
        enemy_points_num.setText("");
        player_remaining_ships_num.setText("");
        enemy_remaining_ships_num.setText("");
        enemy_shots_num.setText("");
        player_five_shots.clear();
        enemy_five_shots.clear();

        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 11; j++) {
                buttons[i][j].setStyle ("");
                buttons[i][j].onMouseClickedProperty();
                buttons[i][j].setDisable(false);
                disButtons[i][j].setStyle("");
            }
        }
    }

    // Called when the Start button is clicked
    static void restart() {
        game_over = false;
        Initialize();
        callInitiazer();
        player_five_shots.clear();
        enemy_five_shots.clear();
        if(load_done) {
            if (p == 1) {
                b_player.shot = false;
                b_enemy.shot = true;
                CustomDialog dialog = new CustomDialog("THE GAME STARTS", "Player starts shooting");
                dialog.openDialog();
                enemy_shots = new EnemyShots(b_enemy, b_player);

            } else {
                b_player.shot = true;
                b_enemy.shot = false;
                CustomDialog dialog = new CustomDialog("THE GAME STARTS", "Enemy starts shooting");
                dialog.openDialog();
                enemy_shots = new EnemyShots(b_enemy, b_player);
                enemyShoots();

            }
            restart_done = true;
        }
    }


    // Called when the "Player shots" and "Enemy shots" buttons from the "Details" menu are clicked
    protected static void fiveShots(ArrayList<Pair<Pair<Integer, Integer>, Pair<String, String>>> arr, String player){
        int[] x = new int[5];
        int[] y = new int[5];
        String[] shot_result = new String[5];
        String[] shot_type = new String[5];
        int counter = 0;
        int counter_reverse = arr.size()-1;
        int i = 0;
        String message = "";
        if(arr.size()-1 >= 5){
            i = 5;
        }
        else{
            i = arr.size();
        }

        while(counter < i){
            Pair<Pair<Integer, Integer>, Pair<String, String>> tmp = arr.get(counter_reverse);
            x[counter] = tmp.getKey().getKey();
            y[counter] = tmp.getKey().getValue();
            shot_result[counter] = tmp.getValue().getKey();
            shot_type[counter] = tmp.getValue().getValue();
            counter++;
            counter_reverse--;

        }
        for(int j = 0; j <= counter-1; j++){
            message = message +"Coordinates are: x: " +x[j] +" y: " +y[j] +"\n"
                    +"Shot result is: " +shot_result[j] +"\n"
                    +"Type of cell hit: " +shot_type[j] +"\n"
                    +"----------------------------------------" +"\n";
        }

        String header = "Information about "+player +"'s shots";
        CustomDialogShots dialog = new CustomDialogShots(header, message);
        dialog.openDialog();

    }

    // Called when the "Load" button is clicked from the "Application" menu
    // A pop-up is created asking the user to give the scenario ID
    static String loadPopUp(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Medialab Battleship");
        dialog.setHeaderText("Please give a scenario ID");

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField input = new TextField();
        input.setText("1");

        Label label = new Label("Scenario ID:");
        label.setFont(Font.font(14));

        grid.add(label, 0, 0);
        grid.add(input, 1, 0);

        // Enable/Disable OK button depending on whether an ID was entered.
        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(false);
        okButton.setStyle("-fx-background-color: #dbc8e8,\n" +
                "        linear-gradient(#c2abd1 50%, white 100%),\n" +
                "        radial-gradient(center 10% -35%, radius 200%, #e6e6e6 45%, #d4bfe3 50%)");


        input.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Get the Stage
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();

        // Add a custom icon
        stage.getIcons().add(new Image("/images/jetFighter1.png"));

        Platform.runLater(() -> input.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return input.getText();
            }
            return null;
        });

        // Get the response value
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()){
            id = result.get();

            boolean checking_existence = true;

            files.FileNames();
            for (String element_player : files.players) {
                if (!element_player.equals(id)) {
                    checking_existence = false;
                } else {
                    checking_existence = true;
                    break;
                }
            }
            if (checking_existence) {
                for (String element_enemy : files.enemies) {
                    if (element_enemy.equals(id)) {
                        checking_existence = true;
                        break;
                    } else if (!element_enemy.equals(id)) {
                        checking_existence = false;
                    }
                }
            }
            callInitiazer();
        }
        return id;
    }

    public void startGame(Stage stage) throws IOException {
        // initialisation from start method goes here
        this.stage = stage;
        StackPane root = new StackPane();
        stage.setTitle("Medialab Battleship");
        StackPane s1 = new StackPane();

        String image = Main.class.getResource("../images/jetFighter.png").toExternalForm();
        String image1 = Main.class.getResource("../images/jetFighter1.png").toExternalForm();
        String background = Main.class.getResource("../images/background.png").toExternalForm();
        ImageView a = new ImageView(image);
        ImageView b = new ImageView(image1);
        ImageView c = new ImageView(background);

        c.setFitHeight(700);
        c.setFitWidth(1000);

        a.setFitHeight(200);
        a.setFitWidth(200);

        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(8));

        transition.setToX(1000);
        transition.setToY(1000);
        transition.setNode(a);
        transition.play();

        b.setFitHeight(200);
        b.setFitWidth(200);

        TranslateTransition transition1 = new TranslateTransition();
        transition1.setDuration(Duration.seconds(8));

        transition1.setToX(-1000);
        transition1.setToY(-1000);
        transition1.setNode(b);
        transition1.play();

        s1.getChildren().addAll(c,a,b);
        Scene scene1 = new Scene(s1, 1000, 700);
        String musicFile = "./src/sounds/start.wav";

        // Create the "start game" animation
        scene2 = new Scene(f, 1000, 700);
        try{
            stage.setScene(scene1);
            File musicPath = new File(musicFile);
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
            pause.setOnFinished(event ->
                    clip.start()
            );
            pause.play();
            PauseTransition pause1 = new PauseTransition(Duration.seconds(4));
            pause1.setOnFinished(event ->
                    stage.setScene(scene2)
            );
            pause1.play();


        }catch (Exception e){
            e.printStackTrace();
        }

        createLayout();
        info.setAlignment(Pos.TOP_CENTER);
        f.setAlignment(Pos.TOP_CENTER);
        stage.show();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.getIcons().add(new Image("/images/jetFighter1.png"));
        startGame(primaryStage);
    }

    @Override
    public void handle(ActionEvent actionEvent) {

    }
    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getPrimaryStage() {
        return stage;
    }
    public static VBox getInfo() {
        return info;
    }
}
