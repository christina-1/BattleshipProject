package dialogs;

import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;


// Creates and styles the window that shows the condition of the enemy ships
// when the "Enemy Ships" button is clicked from the "Details" menu
public class CustomDialogShipsCondition extends Stage {
    private ScaleTransition scale1 = new ScaleTransition();
    private ScaleTransition scale2 = new ScaleTransition();
    private Stage window;

    private SequentialTransition anim = new SequentialTransition(scale1, scale2);
    public CustomDialogShipsCondition(String header, String content){
        Pane root = new Pane();

        scale1.setFromX(0.01);
        scale1.setFromY(0.01);
        scale1.setToY(1.0);
        scale1.setDuration(Duration.seconds(0.33));
        scale1.setNode(root);

        scale2.setFromX(0.01);
        scale2.setToX(1.0);
        scale2.setDuration(Duration.seconds(0.33));
        scale2.setNode(root);

        window = new Stage();
        window.getIcons().add(new Image("/images/jetFighter1.png"));
        window.setTitle("Medialab Battleship");


        initStyle(StageStyle.UNDECORATED);
        initModality(Modality.APPLICATION_MODAL);

        Rectangle bg = new Rectangle(root.getWidth()+350, root.getHeight()+200, Color.LAVENDER);

        Text headerText = new Text(header);
        headerText.setFont(Font.font(20));

        Text contentText = new Text(content);
        contentText.setFont(Font.font(14));

        VBox box = new VBox(10,
                headerText,
                new Separator(Orientation.HORIZONTAL),
                contentText
        );
        box.setPadding(new Insets(15));

        Button btn = new Button("OK");
        btn.setId("ok_btn");
        btn.getStylesheets().add("stylesheets/okButton.css");
        btn.setTranslateX(bg.getWidth() - 50);
        btn.setTranslateY(bg.getHeight() - 50);
        btn.setOnAction(e -> closeDialog());

        window.setOnCloseRequest((WindowEvent we) -> {
            closeDialog();
        });

        root.getChildren().addAll(bg, box, btn);
        Scene dialog = new Scene(root, null);
        dialog.setFill(Color.LAVENDER);
        window.setScene(dialog);
        window.showAndWait();

    }
    public void openDialog(){
        show();
        anim.play();
    }

    public void closeDialog(){
        anim.setOnFinished(e -> close());
        anim.setAutoReverse(true);
        anim.setCycleCount(2);
        anim.playFrom(Duration.seconds(0.66));
        window.close();
    }
}

