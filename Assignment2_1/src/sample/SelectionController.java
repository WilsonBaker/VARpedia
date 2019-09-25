package sample;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SelectionController implements Initializable {
	@FXML
	private TextArea lines;
	
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
			System.out.println("yo");
			
		}else if(voice.getValue().equals("festival")) {
			System.out.println("ho");
		}else if (voice.getValue().equals("espeak")) {
			PreviewESpeakTask espeaktask = new PreviewESpeakTask(selected_lines.getText());
    		Thread thread = new Thread(espeaktask);
    		thread.start();
		}
		
	}
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		lines.setWrapText(true);
		selected_lines.setWrapText(true);
		
		voice.getItems().addAll("festival","espeak");
		voice.getSelectionModel().selectFirst();
		
    }

}
