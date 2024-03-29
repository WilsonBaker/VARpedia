package sample;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.media.*;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
/* This class is the controller for the scene that is implemented by the quiz FXML file*/
public class QuizController implements Initializable{

	private File _fileURL;
	private Media _video;
	private MediaPlayer _player;
	@FXML private MediaView _view;
	private ArrayList<String> _quiz;
	@FXML private Button next;
	@FXML private Button submit;
	@FXML private TextField guess;
	private int _index;
	@FXML private Text answer;
	/* This methods gets data for the quiz that is necessary*/
	public void initData(ArrayList<String> quiz) {
		_quiz=quiz;
		_index=0;
		_fileURL = new File("./Quiz/" + _quiz.get(_index) + ".mp4");
		_video = new Media(_fileURL.toURI().toString());
		_player = new MediaPlayer(_video);
		_player.setAutoPlay(true);
		_view.setMediaPlayer(_player);
		next.setDisable(true);
	}
	/* This methods checks the submitted answer and compares it to the actual*/
	public void submitAnswer(ActionEvent event) throws IOException {
		if(guess.getText().toLowerCase().equals(_quiz.get(_index).toLowerCase())) {
			answer.setText("Correct");
			if(!(_quiz.size()<_index+2)) {
				next.setDisable(false);
			}
		}else {
			answer.setText("Incorrect");
		}



	}
	/* This method gives the user the ability to see the answer*/
	public void giveUp(ActionEvent event) throws IOException {
		answer.setText(_quiz.get(_index));
		if(!(_quiz.size()<_index+2)) {
			next.setDisable(false);
		}
	}
	/*This method gives the user the ability to move on to the next creation when they get the answer right or gives up*/
	public void nextButton(ActionEvent event) throws IOException {
		answer.setText("");
		guess.setText("");
		_index+=1;
		next.setDisable(true);
		_fileURL = new File("./Quiz/" + _quiz.get(_index) + ".mp4");
		_video = new Media(_fileURL.toURI().toString());
		_player = new MediaPlayer(_video);
		_player.setAutoPlay(true);
		_view.setMediaPlayer(_player);

	}
	/* This method pauses or plays the player for the quiz*/
	public void buttonPlayPause() {
		if (_player.getStatus() == Status.PLAYING) {
			_player.pause();
		} else {
			_player.play();
		}
	}
	/*This method returns the user to the main menu scene*/
	public void buttonMenu(ActionEvent event) throws IOException {
		_player.dispose();
		Parent createParent = FXMLLoader.load(getClass().getResource("/FXML/menu.fxml"));
		Scene createScene = new Scene(createParent, 500, 500);

		//This gets the stage info
		Stage createWindow = (Stage)((Node)event.getSource()).getScene().getWindow();

		createWindow.setScene(createScene);
		createWindow.show();

	}
	/* This method fast fowards the player*/
	public void buttonForward() {
		_player.seek(_player.getCurrentTime().add(Duration.seconds(5)));
	}
	/* This method goes back on the player*/
	public void buttonBack() {
		_player.seek(_player.getCurrentTime().subtract(Duration.seconds(5)));
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		submit.disableProperty().bind(
				Bindings.isEmpty(guess.textProperty())

				);

	}


}

