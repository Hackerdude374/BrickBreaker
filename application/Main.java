/**
 * Main class
 * 1. Necessary variables for background music
 * 2. start the stage
 */
package application;

import java.io.File;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Main extends Application {
	
	public static MediaPlayer mediaplayer;		//media player variable
	private Media media;		//media variable
	private File directory;		//file directory variable for loading the media
	private File[] files;		//file array variable to hold files from file directory
	private ArrayList<File> songs;		//songs array list variable will load file
	private int songnumber;		//song number to keep track of the songs
	
	@Override
	public void start(Stage stage) {	//start method of the application
		try {	//try the following
			//load the home scene from HomeScene.fxml
			Parent root = FXMLLoader.load(getClass().getResource("HomeScene.fxml"));	
			Scene scene = new Scene(root);
			songs = new ArrayList<File>();	//instantiate songs array list
			directory = new File("C:\\Users\\bobby\\eclipse-workspace\\BreakOut (7).zip_expanded\\BreakOutTest\\application\\Music");	//lead a file path as directory
			files = directory.listFiles();		//files array adds all file in that directory
			if (files != null) {
				for (File file : files) {	//for each loop adds every file in the files array into songs array list
					songs.add(file);
				}
			}
			media = new Media(songs.get(songnumber).toURI().toString());	//instantiate media with songs array list
			mediaplayer = new MediaPlayer(media);	//instantiate media player with media
			mediaplayer.play();		//play the media player
			mediaplayer.setCycleCount(MediaPlayer.INDEFINITE); //loop the media player infinite times
			stage.setScene(scene);
			stage.setHeight(GamePlay.getCurrent_window_height());
			stage.setWidth(GamePlay.getCurrent_window_width());
			stage.setResizable(false);
			stage.setTitle("Break Out!");
			Image icon = new Image("C:\\Users\\bobby\\eclipse-workspace\\BreakOut (7).zip_expanded\\BreakOutTest\\application\\Image&CSS\\Icon.png");
			stage.getIcons().add(icon);
			stage.show();
			stage.setOnCloseRequest(event -> {	//a alert will pop up when close request being processing
				event.consume();	//consume the event of closing the window
				exit(stage);	//pop up the alert
			});
		} catch(Exception e) {		//catch any errors
			e.printStackTrace();	//print stack traces
		}
	}
	
	public void exit(Stage stage) {		//exit method with parameter stage will be called when exiting the stage, a alert will show up
		Alert alert = new Alert(AlertType.CONFIRMATION);	//instantiate alert type
		alert.setTitle("Exit");		//alert title
		alert.setHeaderText("You're about to exit the game!");	//alert header
		alert.setContentText("Do you want to exit before saving?: ");	//alert text
		if(alert.showAndWait().get() == ButtonType.OK) {	//if OK is clicked
			stage.close(); 	//close the stage
		}
	}
	
	public static void main(String[] args) {	//main
		launch(args);	//launch
	}
	
	
	
}
