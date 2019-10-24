package sample;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.*;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MediaPlayerController implements Initializable{
	
	private File _fileURL;
	private Media _video;
	private MediaPlayer _player;
	@FXML private MediaView _view;
	
	/* Constructor with the selected file to play as string*/
	public void initData(String filename) {
		_fileURL = new File("./Creations/" + filename + ".mp4");
		_video = new Media(_fileURL.toURI().toString());
		_player = new MediaPlayer(_video);
		_player.setAutoPlay(true);
		_view.setMediaPlayer(_player);
	}
	/* Method that mutes player*/
	public void buttonMute() {
		
		if(_player.isMute()) {
			_player.setMute(false);
		} else {
			_player.setMute(true);
		}
		
	}
	/*Method that pauses or plays the player*/
	public void buttonPlayPause() {
		if (_player.getStatus() == Status.PLAYING) {
			_player.pause();
		} else {
			_player.play();
		}
	}
	
	public void buttonMenu(ActionEvent event) throws IOException {
		_player.dispose();
        Parent createParent = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Scene createScene = new Scene(createParent, 500, 500);

        //This gets the stage info
        Stage createWindow = (Stage)((Node)event.getSource()).getScene().getWindow();

        createWindow.setScene(createScene);
        createWindow.show();

    }
	/*Fast forward the player*/
	public void buttonForward() {
		_player.seek(_player.getCurrentTime().add(Duration.seconds(5)));
	}
	/* Go back in the player*/
	public void buttonBack() {
		_player.seek(_player.getCurrentTime().subtract(Duration.seconds(5)));
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	

}
