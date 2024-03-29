package sample;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

	@FXML private ListView listView;
	@FXML private ImageView p;
	private Alert alert = new Alert(AlertType.ERROR);
	private Alert quitAlert = new Alert(AlertType.CONFIRMATION);
	private Alert deleteAlert = new Alert(AlertType.CONFIRMATION);
	private ArrayList<String> _quiz = new ArrayList<String>();
	/* Method that runs commands using string bash commands*/
	public static void runCommand(String com) {
		try {

			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", com);

			Process process = builder.start();

			InputStream out = process.getInputStream();
			BufferedReader stdout = new BufferedReader(new InputStreamReader(out));
			int exitStatus = process.waitFor();

			if(exitStatus ==0) {

			}

		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	/* Method that starts the creation process and moves to the next scene of searching wikipedia*/
	public void buttonCreate(ActionEvent event) throws IOException {
		File temp = new File("Audio");
		if(temp.exists()) {

		}else {
			runCommand("mkdir Audio");
		}
		Parent createParent = FXMLLoader.load(getClass().getResource("/FXML/search.fxml"));
		Scene createScene = new Scene(createParent, 500, 500);

		//This gets the stage info
		Stage createWindow = (Stage)((Node)event.getSource()).getScene().getWindow();

		createWindow.setScene(createScene);
		createWindow.show();

	}

	/* Method that deletes selected creations*/
	public void buttonDelete(ActionEvent event) throws IOException{
		deleteAlert.setContentText("Confirm if you would like to delete " + listView.getSelectionModel().getSelectedItem().toString());
		deleteAlert.setTitle("Delete File");
		deleteAlert.setHeaderText("Are you sure?");

		Optional<ButtonType> result = deleteAlert.showAndWait();
		if (result.get() == ButtonType.OK) {
			int i = listView.getSelectionModel().getSelectedIndex();
			if (listView.getSelectionModel().getSelectedItem() != null) {
				String playString = listView.getSelectionModel().getSelectedItem().toString();
				String URL = "./Creations/" + playString + ".mp4";
				if (i != -1) {
					listView.getItems().remove(i);
					try {
						String cmd = "rm -f " + URL;


						ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);

						Process process = pb.start();


						ArrayList<String> viewList = new ArrayList<String>();

						BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));


					}catch (IOException e) {

					}
				}
			}
		} else {
			//chose to cancel therefore stay on menu
		}


	}
	/* Method that moves on to the next scene of something*/
	public void buttonPlay(ActionEvent event) throws IOException{
		if (listView.getSelectionModel().getSelectedItem() != null) {
			String playString = listView.getSelectionModel().getSelectedItem().toString();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/FXML/something.fxml"));
			Parent createParent = loader.load();
			Scene createScene = new Scene(createParent, 500, 500);

			MediaPlayerController controller = loader.getController();
			controller.initData(playString);

			//This gets the stage info
			Stage createWindow = (Stage)((Node)event.getSource()).getScene().getWindow();

			createWindow.setScene(createScene);
			createWindow.show();
		}

	}
	/* This method grabs creations and puts them in a list for the list view*/
	public String[]  getCreations() {

		try {
			String cmd = "ls -1 ./Creations | egrep '\\.mp4$' | sed -e 's/\\..*$//'";

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

		} catch (IOException | InterruptedException e) {
			return null;
		}
		return null;
	}
	/* Button that changes to new scene(quiz scene) using past created creations*/
	public void buttonQuiz(ActionEvent event) throws IOException{
		File temp = new File("Quiz");
		if (temp.listFiles().length==0) {
			alert.setContentText("No Pre-Created Creations");
			alert.setTitle("No Pre-Created Creations");
			alert.setHeaderText("No Pre-Created Creations");
			alert.show();
		}else {
			for(File file: temp.listFiles()) {
				_quiz.add(file.getName().replace(".mp4", ""));

			}

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/FXML/quiz.fxml"));
			Parent createParent = loader.load();
			Scene createScene = new Scene(createParent, 500, 500);

			QuizController controller = loader.getController();
			controller.initData(_quiz);

			//This gets the stage info
			Stage createWindow = (Stage)((Node)event.getSource()).getScene().getWindow();

			createWindow.setScene(createScene);
			createWindow.show();
		}

	}

	public void buttonQuit(ActionEvent event) throws IOException {
		//Show user an alert confirming if they want to quit
		quitAlert.setContentText("Confirm if you would like to quit?");
		quitAlert.setTitle("Quit Application");
		quitAlert.setHeaderText("Are you sure?");

		Optional<ButtonType> result = quitAlert.showAndWait();
		if (result.get() == ButtonType.OK) {
			Platform.exit();
		} else {
			//chose to cancel therefore stay on menu
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String[] strings = getCreations();
		listView.getItems().addAll(strings);
	}



}
