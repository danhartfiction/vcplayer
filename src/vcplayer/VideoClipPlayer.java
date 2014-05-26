/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vcplayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedSet;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dhart
 */
public class VideoClipPlayer extends Application {
    
    protected StackPane root;
    protected Scene scene;
    protected VBox rootVbox;
    protected HBox topHbox;
    protected VBox categoriesVbox;
    protected VBox categoryListVbox;
    protected VBox settingsVbox;
    protected HBox lowerHbox;
    protected VBox controlsVbox;
    protected HBox lowerButtonsHboxRow1;
    protected HBox lowerButtonsHboxRow2;
    protected HBox lowerStatusHboxRow;
    protected Label categoriesLabel;
    protected Label statusLabel;
    protected Label statusText;
    
    protected Button nextVideoButton;
    protected Button previousVideoButton;
    protected Button trashVideoButton;
    protected Button nextImageButton;
    protected Button previousImageButton;
    protected Button trashImageButton;
    protected Button resetWallpaperButton;
    //protected Button rescanFilesButton;
    
    protected FileManager myMedia;
    
    protected Process videoProcess;
    
    @Override
    public void start(final Stage primaryStage) {
        
        this.root = new StackPane();
        this.scene = new Scene(root, 800, 500);
        
        this.rootVbox = new VBox();
        this.topHbox = new HBox();
        this.categoriesVbox = new VBox();
        this.settingsVbox = new VBox();
        this.lowerHbox = new HBox();
        this.controlsVbox = new VBox();
        
        this.categoriesVbox.setPrefSize(400, 250);
        this.settingsVbox.setPrefSize(400, 250);
        this.controlsVbox.setPrefSize(800, 250);
        
        this.lowerButtonsHboxRow1 = new HBox();
        this.lowerButtonsHboxRow2 = new HBox();
        this.lowerStatusHboxRow = new HBox();
        
        this.categoriesLabel = new Label();
        this.categoriesLabel.setText("Include Categories:");
        this.categoriesLabel.setStyle("-fx-font-weight: bold");
        
        this.statusLabel = new Label();
        this.statusLabel.setText("Current Status: ");
        this.statusLabel.setStyle("-fx-font-weight: bold");
        
        this.statusText = new Label();
        
        this.lowerStatusHboxRow.setPadding(new Insets(10, 10, 10, 10));
                
        this.categoryListVbox = new VBox();
        this.categoriesVbox.getChildren().addAll(this.categoriesLabel, this.categoryListVbox);
        
        this.lowerHbox.setPadding(new Insets(10, 10, 10, 10));  // top, ?, bottom, ?
        this.lowerButtonsHboxRow1.setPadding(new Insets(0, 0, 10, 0));  // top, ?, bottom, right
        
        this.categoriesVbox.setPadding(new Insets(5, 5, 5, 5));
        this.categoriesVbox.setSpacing(5);
        this.lowerButtonsHboxRow1.setSpacing(5);
        this.lowerButtonsHboxRow2.setSpacing(5);

        this.nextVideoButton = new Button("Next Video");
        this.nextVideoButton.setPrefSize(150, 20);
        this.nextVideoButton.setStyle("-fx-font-weight: bold");
        this.previousVideoButton = new Button("Previous Video");
        this.previousVideoButton.setPrefSize(150, 20);
        this.trashVideoButton = new Button("Trash Video");
        this.trashVideoButton.setPrefSize(150, 20);
        this.nextImageButton = new Button("Next Image");
        this.nextImageButton.setPrefSize(150, 20);
        this.nextImageButton.setStyle("-fx-font-weight: bold");
        this.previousImageButton = new Button("Previous Image");
        this.previousImageButton.setPrefSize(150, 20);
        this.trashImageButton = new Button("Trash Image");
        this.trashImageButton.setPrefSize(150, 20);
        this.resetWallpaperButton = new Button("Reset Wallpaper");
        this.resetWallpaperButton.setPrefSize(150, 20);
               
        this.lowerButtonsHboxRow1.getChildren().addAll(this.trashVideoButton, this.previousVideoButton, this.nextVideoButton);
        this.lowerButtonsHboxRow2.getChildren().addAll(this.trashImageButton, this.previousImageButton, this.nextImageButton, this.resetWallpaperButton);
        this.lowerStatusHboxRow.getChildren().addAll(this.statusLabel, this.statusText);
        
        this.controlsVbox.getChildren().addAll(this.lowerButtonsHboxRow1, this.lowerButtonsHboxRow2, this.lowerStatusHboxRow);
        this.lowerHbox.getChildren().add(this.controlsVbox);
        this.topHbox.getChildren().addAll(this.categoriesVbox, this.settingsVbox);
        this.rootVbox.getChildren().addAll(this.topHbox, this.lowerHbox);
        
        this.root.getChildren().add(this.rootVbox);
            
        this.myMedia = new FileManager();
        this.myMedia.crawlVideos();
        this.updateCategories(this.myMedia.getCategories());
        this.myMedia.crawlImages(); // Are not included in categories
        
        this.nextVideoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println(getActiveCategories());
                String myVideo = myMedia.getNextVideo(getActiveCategories());
                if (myVideo.contentEquals("No such files exist.")) {
                    statusText.setText("Error: " + myVideo);
                } else {
                    statusText.setText("Now Playing: " + myVideo);
                }
                System.out.println("DEBUG: nextVideo: " + myVideo);
                try {
                    String [] cmd = { "/Applications/QuickTime Player.app/Contents/MacOS/QuickTime Player", myMedia.getCurrentVideo() };
                    videoProcess = Runtime.getRuntime().exec(cmd);
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        }); 
        
        this.previousVideoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String myVideo = myMedia.getPreviousVideo();
                if (myVideo.contentEquals("No such files exist.")) {
                    statusText.setText("Error: " + myVideo);
                } else {
                    statusText.setText("Now Previous: " + myVideo);
                }
                System.out.println("DEBUG: previousVideo: " + myVideo);
                // TODO : Play Video
            }
        }); 
        
        this.trashVideoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                videoProcess.destroy();
                if (myMedia.trashCurrentVideo()) {
                    statusText.setText("Deleted current video file");
                } else {
                    statusText.setText("Error: No current video file selected");
                }
            }
        });         
        
        this.nextImageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println(getActiveCategories());
                String myImage = myMedia.getNextImage(getActiveCategories());
                if (myImage.contentEquals("No such files exist.")) {
                    statusText.setText("Error: " + myImage);
                } else {
                    statusText.setText("Now Displaying: " + myImage);
                }
                System.out.println("DEBUG: nextImage: " + myImage);
                // TODO: Display Image
            }
        }); 
        
        this.previousImageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String myImage = myMedia.getPreviousImage();
                if (myImage.contentEquals("No such files exist.")) {
                    statusText.setText("Error: " + myImage);
                } else {
                    statusText.setText("Now Previous: " + myImage);
                }
                System.out.println("DEBUG: previousVideo: " + myImage);
                // TODO : Play Video
            }
        }); 
                
        this.trashImageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // TODO: First need to kill image viewer, then trash.  Maybe sleep in between?
                if (myMedia.trashCurrentImage()) {
                    statusText.setText("Deleted current image file");
                } else {
                    statusText.setText("Error: No current image file selected"); 
               }
            }
        });        
                
        // quick and dirty test DHTEST
        Label secondLabel = new Label("Hello");
                 
        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(secondLabel);

        Scene secondScene = new Scene(secondaryLayout, 200, 100);

        final Stage secondStage = new Stage();
        secondStage.setTitle("Second Stage");
        secondStage.setScene(secondScene);

        //Set position of second window, related to primary window.
        //secondStage.setX(primaryStage.getX() + 250);
        //secondStage.setY(primaryStage.getY() + 100);

        secondStage.hide();
                
        this.resetWallpaperButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (secondStage.isShowing()) { 
                    secondStage.hide();
                } else {
                    secondStage.show();                
                }
            }
        });
        // end quick and dirty test DHTEST
        
        primaryStage.setTitle("Video Clip Remote");
        primaryStage.setScene(this.scene);
        primaryStage.show();
        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
    }
    

    
    protected ArrayList<String> getActiveCategories() {
        ArrayList<String> activeCategories = new ArrayList<>();
        Iterator it = this.categoryListVbox.getChildren().iterator();
        while (it.hasNext()) {
            CheckBox cb = (CheckBox) it.next();
            if (cb.isSelected()) {
                activeCategories.add(cb.getText());
            }
        }
        return activeCategories;
    }
    
    public void updateCategories(SortedSet<String> newCategories) {
        Iterator it = newCategories.iterator();
        while (it.hasNext()) {
            String category = (String) it.next();
            CheckBox cb = new CheckBox(category);
            cb.setIndeterminate(false);
            this.categoryListVbox.getChildren().add(cb);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
