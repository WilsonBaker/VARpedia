package sample;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.media.*;

public class MediaPlayerController implements Initializable{
	
	private File _fileURL;
	private Media _video;
	private MediaPlayer _player;
	@FXML private MediaView _view;
	
	
	public void initData(String filename) {
		_fileURL = new File("./Creations/" + filename + ".mp4");
		_video = new Media(_fileURL.toURI().toString());
		_player = new MediaPlayer(_video);
		_player.setAutoPlay(true);
		_view.setMediaPlayer(_player);
	}
	
	public void buttonPlay() {
		_player.play();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	

}
