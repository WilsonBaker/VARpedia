package sample;

import java.io.BufferedReader;
import java.io.File;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/* This is the class that controls the scene that is determined using the ChooseAudio fxml file*/
public class ChooseAudioController implements Initializable {

	private String _wikitSearch;
	private Alert alert = new Alert(AlertType.ERROR);
	private ArrayList<String> _rimages = new ArrayList<String>();

	@FXML private ListView listAvailable;
	@FXML private ListView listCreation;
	@FXML private TextField creationName;
	@FXML private Text text;
	@FXML private Button createButton;
	@FXML private ChoiceBox music;
	/* This method is to get the search term from another scene*/
	public void setWikitName(String name) {
		_wikitSearch=name;

	}
	/* The get Creations method looks for audio chunks in the wav file and adds them to a list for a list view*/
	public String[]  getCreations() {
		String path = System.getProperty("user.dir");

		try {
			String cmd = "ls -1 " + path + "/Audio | egrep '\\.wav$' | sed -e 's/\\..*$//'";

			ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);

			Process process = pb.start();


			ArrayList<String> viewList = new ArrayList<String>();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;

			while ((line = reader.readLine()) != null) {
				viewList.add(line);
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {

				String[] strings = viewList.toArray(new String[viewList.size()]);
				return strings;
			} else {
				//abnormal..
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	/* This method adds chunks to a list called listCreation that contains the chunks in the creation*/
	public void addButton() {

		ObservableList listOfFiles = listAvailable.getSelectionModel().getSelectedItems();

		for(Object item: listOfFiles) {
			listCreation.getItems().add(item.toString());
		}
	}

	/* This method removes the selected chunk from the list listCreation hence from the creation*/
	public void removeButton() {

		int i = listCreation.getSelectionModel().getSelectedIndex();

		if (i != -1) {
			listCreation.getItems().remove(i);
		}

	}
	/* This method goes to the scene of selecting images when button pressed*/
	public void flickr(ActionEvent event) throws IOException {
		File temp = new File("Creations/"+creationName.getText()+".mp4");
		if(listCreation.getItems().size()==0) {
			alert.setContentText("No Chunks Selected");
			alert.setTitle("No Chunks Selected");
			alert.setHeaderText("No Chunks Selected");
			alert.show();

		}else if(creationName.getText().equals("")) {
			alert.setContentText("Creation name empty");
			alert.setTitle("Empty Fields Required");
			alert.setHeaderText("Empty Fields Required");
			alert.show();
		} else if(creationName.getText().contains(" ")|| creationName.getText().contains(".")) {
			alert.setContentText("Field Contains Space or Full Stop");
			alert.setTitle("Field Contains Space or Full Stop");
			alert.setHeaderText("Field Contains Space or Full Stop");
			alert.show();
		}else if(temp.exists()){
			alert.setContentText("Creation " + creationName.getText()+ " exists");
			alert.setTitle("Creation Exists");
			alert.setHeaderText("Creation Exists");
			alert.show();


		}else {

			text.setText("Loading . . .");
			CreateAudioTask audiotask = new CreateAudioTask(listCreation.getItems(), music.getSelectionModel().getSelectedItem().toString());



			Thread thread = new Thread(audiotask);
			thread.start();

			audiotask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event3) {
					CreateFlickrTask createtask = new CreateFlickrTask(_wikitSearch ,creationName.getText(),(String)music.getSelectionModel().getSelectedItem(),_rimages);
					Thread thread = new Thread(createtask);
					thread.start();


					createtask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
						@Override
						public void handle(WorkerStateEvent event3) {
							text.setText("");

							/*once images are found go to next scene*/
							try {



								FXMLLoader loader = new FXMLLoader();
								loader.setLocation(getClass().getResource("images.fxml"));
								Parent createParent = loader.load();
								Scene createScene = new Scene(createParent, 500, 500);

								ChooseImageController controller = loader.getController();

								controller.setImages(_rimages,_wikitSearch,creationName.getText());

								//This gets the stage info
								Stage createWindow = (Stage)((Node)event.getSource()).getScene().getWindow();

								createWindow.setScene(createScene);
								createWindow.show();
							}catch(IOException e) {

							}

						}
					});

				}
			});





		}

	}
	/* This method is used to preview the music for the creations*/
	public void preview(ActionEvent event) throws IOException {
		PreviewMusicTask previewMsc = new PreviewMusicTask(music.getSelectionModel().getSelectedItem().toString());
		Thread thread = new Thread(previewMsc);
		thread.start();
	}

	/* This method is used to clear files when trying to return to main menu*/
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
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String[] musiclist = {"Happy", "Sad", "Scary", "Calm", "Action"};
		String[] strings = getCreations();
		listAvailable.getItems().addAll(strings);
		listAvailable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		music.getItems().addAll(musiclist);
		music.getSelectionModel().selectFirst();

		createButton.disableProperty().bind(
				Bindings.isEmpty(creationName.textProperty())


				);

	}


}
