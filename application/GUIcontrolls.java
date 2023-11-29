/**
 * GUIcontrolls class
 * 1. contains methods on Home Scene and Setting Scene of Graphic User Interfaces
 * 2. Connect methods with scene objects
 * 3. Necessary methods
 * 4. Necessary variables
 */
package application;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUIcontrolls implements Initializable {

	public static Stage stage;		//Primary Stage variable
	public static Scene scene;		//Scene variable
	public static Parent root;		//Root variable for the scene
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();		//screenSize variable for getting screen sizes when full screen
	
	//home scene using FXML
	@FXML
	private AnchorPane home;	//home scene anchor pane contains a vertical box, that vertical box contains five horizontal boxes that contains everything on home scene using FXML
		@FXML
		private AnchorPane HelpPane;	//help pane appears when about menu item is being clicked on menu bar
			@FXML
			private Button CloseHelpButton;	//close button on the HelpPane
		@FXML
		private Label music_label;	//music label within a horizontal box with music check box using FXML
		@FXML
		private CheckBox music_check_box;	//music check box within a horizontal box with music label using FXML
		
		@FXML
		private Button startButton;	//start button within a horizontal box using FXML

		@FXML
		private Button loadButton;	//load/continue button within a horizontal box using FXML
		
		@FXML
		private Button settingButton;	//setting button within a horizontal box using FXMl
			//switch to setting scene when clicking settingButton from the home scene
			@FXML 
			private AnchorPane setting;	//setting scene anchor pane contains a vertical box using FXML
			@FXML
			private VBox setting_vbox;	//setting vertical box contains four horizontal boxes using FXML
				@FXML
				private HBox window_size_hbox;	//window size horizontal box contains a resolution label, a resolution toggle group and four radio buttons using FXML
					@FXML
					private Label window_size_label;	//window size label using FXML
					@FXML
					private ToggleGroup window_size_toggle_group;	//window size toggle group contains four radio buttons using FXML
						@FXML
						private RadioButton eight_by_six_radio;		//800 * 600 size radio button using FXML
						@FXML
						private RadioButton eleven_by_six_radio;	//1067 * 600 size radio button using FXML
						@FXML
						private RadioButton thirteen_by_nine_radio;	//1280 * 900 size radio button using FXML
						@FXML
						private RadioButton full_screen_radio;	//full screen radio button using FXML
						private final Toggle DEFAULT_TOGGLE = eight_by_six_radio;	//default toggle variable
						private Toggle window_size_toggle = DEFAULT_TOGGLE;	//current window size toggle set to default toggle
				@FXML
				private HBox volume_hbox;	//volume horizontal box using FXML
					@FXML
					private Label volume_label;	//volume label using FXML
					@FXML
					public Slider volume_slider = new Slider(0, 100, 100); //volume slider with minimum, maximum and current value using FXML
					public final static double DEFAULT_VOLUME = 1.00;	//default volume level variable
					public double current_volume = DEFAULT_VOLUME;	//current volume variable set to default volume
				@FXML
				private HBox sensitivity_hbox;	//sensitivity horizontal box using FXML
					@FXML
					Label sensitivity_label;	//sensitivity label using FXML
				@FXML
				private HBox DSR_hbox;		//Default, Save, Return horizontal box using FXML contains three buttons
					@FXML
					private Button default_setting;		//default setting button using FXML
					@FXML
					private Button save_setting;	//save setting button using FXML
					@FXML
					private Button return_home;		//return to home scene button using FXML
					
		@FXML
		private Button exitButton;	//exit button within a horizontal box using FXMl

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		//initializes a volume slider when the root has been processed
		volume_slider.valueProperty().addListener(new ChangeListener<Number>() {	//add value property listener onto volume slider
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				current_volume = volume_slider.getValue() * 0.01;	//set the value from volume slider onto current_volume
			}
        });
	}
		
	public void startGame(ActionEvent event) throws IOException {	//called when the start button is clicked and the game scene will be loaded
		GamePlay.setPreviousGame(false);	//check if there is previous game saved
		GamePlay.setLoadGame(false);
		//load the game scene from "GameScene.fxml"
		root = FXMLLoader.load(getClass().getResource("GameScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        //check if full screen is being chosen after previous settings
        if (GamePlay.getFullScreen()) {
			stage.setFullScreen(false);
			stage.setScene(scene);
			stage.setFullScreen(true);
		} else {
			stage.setScene(scene);
		}
        stage.show();
	}

	public void load(ActionEvent event) throws FileNotFoundException, IOException {	//called when the load/continue button in the home scene is clicked and the previous game scene will be loaded
		//load the game scene from "GameScene.fxml"
		GamePlay.setLoadGame(true);
		//if there is a previous game saved
		if (GamePlay.getPreviousGame() == true) {
			root = FXMLLoader.load(getClass().getResource("GameScene.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			//check if full screen is being chosen after previous settings
			if (GamePlay.getFullScreen()) {
				stage.setFullScreen(false);
				stage.setScene(scene);
				stage.setFullScreen(true);
			} else {
				stage.setScene(scene);
			}
			stage.show();
		} else {	//else a alert will shown
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Message");
			alert.setHeaderText("No Previous Game Saved!");
			alert.setContentText("Loading Game Failed!!!");
			alert.show();
		}
	}
	
	public void setting(ActionEvent event) throws IOException {	//called when setting button is clicked and the setting scene will be loaded
		//load the setting scene from "SettingScene.fxml"
		root = FXMLLoader.load(getClass().getResource("SettingScene.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		//check if full screen is being chosen after previous settings
		if (GamePlay.getFullScreen()) {
			stage.setFullScreen(false);
			stage.setScene(scene);
			stage.setFullScreen(true);
		} else {
			stage.setScene(scene);
		}
		stage.show();
	}
	
	
	public void exit(Stage stage) {		//exit method with parameter stage will be called when exiting the stage, a alert will show up
		Alert alert = new Alert(AlertType.CONFIRMATION);	//instantiate alert type
		alert.setTitle("Exit");		//alert title
		alert.setHeaderText("You're about to exit the game!");	//alert header
		alert.setContentText("You're about to exit the game!!!");	//alert text
		if(alert.showAndWait().get() == ButtonType.OK) {	//if OK is clicked
			stage.close(); 	//close the stage
		}
	}
	
	public void getResolution(ActionEvent event) throws IOException {	//sets current resolution height and width with chosen resolution radio button
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		if (eight_by_six_radio.isSelected()) {	//if 800*600 is chosen
			eight_by_six_radio.setSelected(true);
			eleven_by_six_radio.setSelected(false);
			thirteen_by_nine_radio.setSelected(false);
			full_screen_radio.setSelected(false);
			GamePlay.setPrevious_window_height(GamePlay.getCurrent_window_height());	//previous window height
			GamePlay.setPrevious_window_width(GamePlay.getCurrent_window_width());	//previous window width
			GamePlay.setCurrent_window_height(GamePlay.getSix());	//current window height = 600
			GamePlay.setCurrent_window_width(GamePlay.getEight());	//current window width = 800
			window_size_toggle = eight_by_six_radio;
		} else if (eleven_by_six_radio.isSelected()) {	//if 1067*600 is chosen
			eight_by_six_radio.setSelected(false);
			eleven_by_six_radio.setSelected(true);
			thirteen_by_nine_radio.setSelected(false);
			full_screen_radio.setSelected(false);
			GamePlay.setPrevious_window_height(GamePlay.getCurrent_window_height());	//previous window height
			GamePlay.setPrevious_window_width(GamePlay.getCurrent_window_width());	//previous window width	
			GamePlay.setCurrent_window_height(GamePlay.getSix());	//current window height = 600
			GamePlay.setCurrent_window_width(GamePlay.getEleven());	//current window width = 1067
			window_size_toggle = eleven_by_six_radio;
		} else if (thirteen_by_nine_radio.isSelected()) {	//if 1280*900 is chosen
			eight_by_six_radio.setSelected(false);
			eleven_by_six_radio.setSelected(false);
			thirteen_by_nine_radio.setSelected(true);
			full_screen_radio.setSelected(false);
			GamePlay.setPrevious_window_height(GamePlay.getCurrent_window_height());	//previous window height
			GamePlay.setPrevious_window_width(GamePlay.getCurrent_window_width());	//previous window width	
			GamePlay.setCurrent_window_height(GamePlay.getNine());	//current window height = 600
			GamePlay.setCurrent_window_width(GamePlay.getThirteen());	//current window width = 800
			window_size_toggle = thirteen_by_nine_radio;
		} else if (full_screen_radio.isSelected()) {	//if full screen is chosen
			eight_by_six_radio.setSelected(false);
			eleven_by_six_radio.setSelected(false);
			thirteen_by_nine_radio.setSelected(false);
			full_screen_radio.setSelected(true);
			GamePlay.setPrevious_window_height(GamePlay.getCurrent_window_height());	//previous window height
			GamePlay.setPrevious_window_width(GamePlay.getCurrent_window_width());	//previous window width
			GamePlay.setCurrent_window_height((int)screenSize.getHeight());	//current window height = current screen's maximum height
			GamePlay.setCurrent_window_width((int)screenSize.getWidth());	//current window width = current screen's maximum width
			window_size_toggle = full_screen_radio;
		}
	}
	
	public void default_setting(ActionEvent event) {	//called when default setting button is clicked
		Alert alert = new Alert(AlertType.CONFIRMATION);	//instantiate alert type
		alert.setTitle("Setting");	//alert title
		alert.setHeaderText("Default setting is about to apply!");	//alert header
		alert.setContentText("Do you want default setting to apply?: ");	//alert text
		if (alert.showAndWait().get() == ButtonType.OK) {	//if OK is clicked
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			//DEFAULT SETTINGS being applied
			stage.setFullScreen(false);
			eight_by_six_radio.setSelected(false);
			eleven_by_six_radio.setSelected(false);
			thirteen_by_nine_radio.setSelected(false);
			full_screen_radio.setSelected(false);
			volume_slider.setValue(DEFAULT_VOLUME * 100);
			stage.setWidth(GamePlay.getDefaultWidth());
			stage.setHeight(GamePlay.getDefaultHeight());
			Main.mediaplayer.setVolume(DEFAULT_VOLUME);
		}
	}
	
	public void save_setting(ActionEvent event) {	//called when save setting button is clicked
		Alert alert = new Alert(AlertType.CONFIRMATION);	//instantiate alert type
		Alert noChange = new Alert(AlertType.INFORMATION);	//instantiate noChange type
		alert.setTitle("Setting");	//alert title
		noChange.setTitle("Message");	//noChange title
		alert.setHeaderText("Confirm");	//alert header
		noChange.setHeaderText("Information");	//noChange header
		alert.setContentText("New settings will be apply!");	//alert text
		noChange.setContentText("No change has been made!");	//noChange text
		if (alert.showAndWait().get() == ButtonType.OK) {	//if OK is clicked
			//set window size
			if(window_size_toggle != DEFAULT_TOGGLE) {	//change window size if different window size is being selected
				if (eight_by_six_radio.isSelected()) {	//if 800*600 window size being selected
					stage.setFullScreen(false);
					window_size_toggle = eight_by_six_radio;
					stage.setWidth(GamePlay.getEight());
					stage.setHeight(GamePlay.getSix());
					setting.setPrefWidth(GamePlay.getEight());
					setting.setPrefHeight(GamePlay.getSix());
					GamePlay.setFullScreen(false);
				} else if (eleven_by_six_radio.isSelected()) {	//if 1067*600 window size being selected
					stage.setFullScreen(false);
					window_size_toggle = eleven_by_six_radio;
					stage.setWidth(GamePlay.getEleven());
					stage.setHeight(GamePlay.getSix());
					setting.setPrefWidth(GamePlay.getEleven());
					setting.setPrefHeight(GamePlay.getSix());
					GamePlay.setFullScreen(false);
				} else if (thirteen_by_nine_radio.isSelected()) {	//if 1280*900 window size being selected
					stage.setFullScreen(false);
					window_size_toggle = thirteen_by_nine_radio;
					stage.setWidth(GamePlay.getThirteen());
					stage.setHeight(GamePlay.getNine());
					setting.setPrefWidth(GamePlay.getThirteen());
					setting.setPrefHeight(GamePlay.getNine());
					GamePlay.setFullScreen(false);
				} else if (full_screen_radio.isSelected()) {	//if full screen being selected
					window_size_toggle = full_screen_radio;
					stage.setFullScreenExitHint("");
					stage.setFullScreen(true);
					GamePlay.setFullScreen(true);
				}
				GamePlay.setBrickResize(true);	//brick resizing required
			}
			//set volume level
			Main.mediaplayer.setVolume(current_volume);
		} else {	//if other button besides OK is clicked, settings will not be saved
			noChange.show();
		}
	}
	
	public void returnToHome(ActionEvent event) throws IOException {	//called when return to home button is clicked
		root = FXMLLoader.load(getClass().getResource("HomeScene.fxml"));	//load home scene from "HomeScene.fxml"
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		if (GamePlay.getFullScreen()) {	//retains full screen if full screen radio button is selected
			stage.setFullScreen(false);
			stage.setScene(scene);
			stage.setFullScreen(true);
		} else {
			stage.setScene(scene);
		}
		stage.show();
	}
	
	public void exit(ActionEvent event) {	//called when exit button being clicked
		Alert alert = new Alert(AlertType.CONFIRMATION);	//instantiate alert
		alert.setTitle("Exit");	//alert title
		alert.setHeaderText("You're about to exit the game!");	//alert header
		alert.setContentText("You're about to exit the game!!!");	//alert text
		if (alert.showAndWait().get() == ButtonType.OK) {	//if OK is clicked
			stage = (Stage) home.getScene().getWindow();
			stage.close();	//close the stage
		} else {
			event.consume();
		}
	}
	
	public void help(ActionEvent event) {
		HelpPane.setVisible(true);
	}
	
	public void closeHelp(ActionEvent event) {
		HelpPane.setVisible(false);
	}

	public void on_off(ActionEvent event) {	//when music ON/OFF check box is being checked or unchecked
		if (music_check_box.isSelected()) {	//if checked
			Main.mediaplayer.setMute(false);
		} else {	//if unchecked
			Main.mediaplayer.setMute(true);
		}
	}
	
}
