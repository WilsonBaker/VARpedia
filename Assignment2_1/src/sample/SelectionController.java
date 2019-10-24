package sample;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.control.Button;
import javafx.stage.Stage;
/* This class is the controller for the line selection scene that is implemented by the selection FXML file*/
public class SelectionController implements Initializable {
	@FXML
	private TextArea lines;

	@FXML
	private TextField creation_name;
	@FXML
	private TextArea selected_lines;
	@FXML
	private Button previewButton;
	@FXML
	private Button createButton;
	@FXML
	private Button copyButton;

	@FXML
	private ComboBox voice;
	private String _wLines;
	private Alert alert = new Alert(AlertType.ERROR);
	private Alert alert2 = new Alert(AlertType.INFORMATION);
	private String[] words;
	private String _wikitSearch;
	/* THis method is used to collect the lines from the search scene*/
	public void setLines(String largeLines, String wikitSearch){
		_wLines=largeLines;
		lines.setText(largeLines);
		_wikitSearch=wikitSearch;

	}
	/* This method previews the selected lines with the selected voice(runs the PreviewTask*/
	public void preview(ActionEvent event) throws IOException {
		words= selected_lines.getText().split("\\s+");
		if(words.length>30) {
			alert.setContentText("Text has more than 30 words");
			alert.setTitle("Too Many Words");
			alert.setHeaderText("Too Many Words");
			alert.show();

		}else {
			PreviewTask preview = new PreviewTask(selected_lines.getText(),voice.getValue());
			Thread thread = new Thread(preview);
			thread.start();




		}

	}
	/* This method checks if the creation name is correct and lines are fine(runs PreviewTask)  */
	public void create(ActionEvent event) throws IOException {

		words= selected_lines.getText().split("\\s+");
		File temp = new File("Audio/"+creation_name.getText()+".wav");

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
		} else if(creation_name.getText().contains(" ") || creation_name.getText().contains(".") ) {
			alert.setContentText("Field Contains Space or Full Stop");
			alert.setTitle("Field Contains Space or Full Stop");
			alert.setHeaderText("Field Contains Space or Full Stop");
			alert.show();
		} else if(temp.exists()){
			alert.setContentText("Chunk " + creation_name.getText()+ " exists");
			alert.setTitle("Chunk Exists");
			alert.setHeaderText("Chunk Exists");
			alert.show();
		}else {
			CreateChunkTask createtask = new CreateChunkTask(selected_lines.getText(),creation_name.getText(),voice.getValue());
			Thread thread = new Thread(createtask);
			thread.start();

			createtask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event2) {
					alert2.setContentText("Chunk " + creation_name.getText() + " Created");
					alert2.setTitle("Chunk Created");
					alert2.setHeaderText("Chunk Created");
					alert2.show();
				}
			});
			/*You need to add the change in scene over here to the movie creator*/
		}

	}
	/*This method copys created lines to selected lines*/
	public void copyButton(ActionEvent event) throws IOException {
		selected_lines.setText(lines.getSelectedText());
	}


	/* This method changes the scene to the menu scene*/
	public void buttonMenu(ActionEvent event) throws IOException {
		File temp = new File("Audio");
		for(File file: temp.listFiles()) {
			file.delete();
		}
		temp.delete();

		Parent createParent = FXMLLoader.load(getClass().getResource("menu.fxml"));
		Scene createScene = new Scene(createParent, 500, 500);

		//This gets the stage info
		Stage createWindow = (Stage)((Node)event.getSource()).getScene().getWindow();

		createWindow.setScene(createScene);
		createWindow.show();

	}
	/*This method moves the scene to the ChooseAudio scene which is implemented by ChooseAudio FXML file*/
	public void AudioScene (ActionEvent event) throws IOException {
		File temp = new File("Audio");
		if(temp.listFiles().length == 0) {
			alert.setContentText("No Audio Chunks for creation");
			alert.setTitle("No Audio Chunks for creation");
			alert.setHeaderText("No Audio Chunks for creation");
			alert.show();
		}else {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("ChooseAudio.fxml"));
			Parent createParent= loader.load();
			ChooseAudioController controller = loader.getController();
			controller.setWikitName( _wikitSearch);
			Scene createScene = new Scene(createParent, 500, 500);

			Stage createWindow = (Stage)((Node)event.getSource()).getScene().getWindow();

			createWindow.setScene(createScene);
			createWindow.show();

		}


	}

	@Override

	public void initialize(URL location, ResourceBundle resources) {
		lines.setWrapText(true);
		selected_lines.setWrapText(true);

		voice.getItems().addAll("festival","espeak");
		voice.getSelectionModel().selectFirst();
		/*Set bind the empty property of certain text fields to the buttons*/
		createButton.disableProperty().bind(
				Bindings.isEmpty(selected_lines.textProperty())
				.or(Bindings.isEmpty(creation_name.textProperty()))

				);
		previewButton.disableProperty().bind(
				Bindings.isEmpty(selected_lines.textProperty())


				);
		copyButton.disableProperty().bind(
				Bindings.isEmpty(lines.textProperty())


				);
	}



}
