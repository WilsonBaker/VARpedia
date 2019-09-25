package sample;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SelectionController implements Initializable {
	@FXML
	private TextArea lines;
	
	@FXML
	private TextArea selected_lines;
	
	@FXML
	private ComboBox voice;
	private String _wLines;
	
	public void setLines(String largeLines){
		_wLines=largeLines;
		lines.setText(largeLines);
	}
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		lines.setWrapText(true);
		selected_lines.setWrapText(true);
    }

}
