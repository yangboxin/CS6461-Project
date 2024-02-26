import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Random;

public class panel extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox vbox = new VBox(10); // Main frame
        vbox.setAlignment(Pos.CENTER);

        // 4 GPRs
        for (int i = 0; i < 4; i++) {
            HBox gprWithLoad = createGPRWithLD("GPR " + i, 16);
            vbox.getChildren().add(gprWithLoad);
        }
        
        // 3 IDXs
        for (int i = 0; i < 3; i++) {
            HBox idxWithLoad = createGPRWithLD("IDX " + i, 16);
            vbox.getChildren().add(idxWithLoad);
        }

        // PC
        HBox pcWithLd=createRecs("PC", 12, 1);
        vbox.getChildren().add(pcWithLd);
        // MAR
        HBox marWithLd=createRecs("MAR", 12, 1);
        vbox.getChildren().add(marWithLd);
        // MBR
        HBox mbrWithLd=createRecs("MBR", 16, 1);
        vbox.getChildren().add(mbrWithLd);
        // IR
        HBox irWithoutLd=createRecs("MAR", 16, 0);
        vbox.getChildren().add(irWithoutLd);
        
        // set stages and scenes
        Scene scene = new Scene(vbox, 1100, 900);
        primaryStage.setTitle("GPRs with Individual LD Buttons");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createRecs(String label, int bitsCount, int ldButFlag){
        HBox hbox = new HBox(5); 
        hbox.setAlignment(Pos.CENTER_RIGHT);

        Label recLabel = new Label(label);
        hbox.getChildren().add(recLabel);

        // GPR's rectangles
        for (int i = 0; i < bitsCount; i++) {
            Rectangle rect = new Rectangle(10, 20, Color.LIGHTGRAY); 
            rect.setStroke(Color.BLACK); 
            hbox.getChildren().add(rect);
        }
        
        if(ldButFlag==0){
            return hbox;
        }
        // Add LD button
        Button ldButton = new Button("LD");
        ldButton.setOnAction(event -> loadBinaryBits(hbox));
        hbox.getChildren().add(ldButton);

        return hbox;
    }
    
    private HBox createGPRWithLD(String label, int bitsCount) {
        //for every line of GPR and the LD button
        HBox hbox = new HBox(5); 
        hbox.setAlignment(Pos.CENTER_LEFT);

        Label gprLabel = new Label(label);
        hbox.getChildren().add(gprLabel);

        // GPR's rectangles
        for (int i = 0; i < bitsCount; i++) {
            Rectangle rect = new Rectangle(10, 20, Color.LIGHTGRAY); 
            rect.setStroke(Color.BLACK); 
            hbox.getChildren().add(rect);
        }
        
        // Add LD button
        Button ldButton = new Button("LD");
        ldButton.setOnAction(event -> loadBinaryBits(hbox));
        hbox.getChildren().add(ldButton);

        return hbox;
    }

    private void loadBinaryBits(HBox gprBox) {
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}
