package sample;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class SelectionController implements Initializable {
	@FXML
	private TextArea lines;
	
	@FXML
	private TextField creation_name;
	@FXML
	private TextArea selected_lines;
	
	@FXML
	private ComboBox voice;
	private String _wLines;
	private Alert alert = new Alert(AlertType.INFORMATION);
	private String[] words;
	
	public void setLines(String largeLines){
		_wLines=largeLines;
		lines.setText(largeLines);
		
	}
	
	public void preview(ActionEvent event) throws IOException {
		words= selected_lines.getText().split("\\s+");
		if(words.length>30) {
			alert.setContentText("Text has more than 30 words");
			alert.setTitle("Too Many Words");
			alert.setHeaderText("Too Many Words");
			alert.show();
			
		}else if(voice.getValue().equals("festival")) {
			PreviewFestivalTask festivalTask = new PreviewFestivalTask(selected_lines.getText());
    		Thread thread = new Thread(festivalTask);
    		thread.start();
		}else if (voice.getValue().equals("espeak")) {
			PreviewESpeakTask espeaktask = new PreviewESpeakTask(selected_lines.getText());
    		Thread thread = new Thread(espeaktask);
    		thread.start();
		}
		
	}
	
	public void create(ActionEvent event) throws IOException {
		words= selected_lines.getText().split("\\s+");
		if(words.length>30) {
			alert.setContentText("Text has more than 30 words");
			alert.setTitle("Too Many Words");
			alert.setHeaderText("Too Many Words");
			alert.show();
			
		}else if(creation_name.getText().equals("") || selected_lines.getText().equals("")) {
			alert.setContentText("Either creation name or selected text is empty");
			alert.setTitle("Empty Fields Required");
			alert.setHeaderText("Empty Fields Required");
			alert.show();
		}else if(voice.getValue().equals("espeak")) {
			CreateESpeakTask espeaktask = new CreateESpeakTask(selected_lines.getText(),creation_name.getText());
    		Thread thread = new Thread(espeaktask);
    		thread.start();
    		/*You need to add the change in scene over here to the movie creator*/
			
		}else if(voice.getValue().equals("festival")) {
			CreateFestivalTask festivaltask = new CreateFestivalTask(selected_lines.getText(),creation_name.getText());
    		Thread thread = new Thread(festivaltask);
    		thread.start();
    		
    		/*You need to add the change in scene over here as well to the movie creator*/
		}
	}
	
	public void buttonMenu(ActionEvent event) throws IOException {
        Parent createParent = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Scene createScene = new Scene(createParent, 500, 500);

        //This gets the stage info
        Stage createWindow = (Stage)((Node)event.getSource()).getScene().getWindow();

        createWindow.setScene(createScene);
        createWindow.show();

    }
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		lines.setWrapText(true);
		selected_lines.setWrapText(true);
		
		voice.getItems().addAll("festival","espeak");
		voice.getSelectionModel().selectFirst();
		
    }

}
