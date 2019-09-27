package sample;

import java.io.BufferedReader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

public class ChooseAudioController implements Initializable {
	 private String _wikitSearch;
	 private Alert alert = new Alert(AlertType.INFORMATION);
	 @FXML private ListView listAvailable;
	 @FXML private ListView listCreation;
	 @FXML private TextField creationName;
	 
	 @FXML private ChoiceBox picturesNo;
	 public void setWikitName(String name) {
		 _wikitSearch=name;
		 
	 }
	 
	 public String[]  getCreations() {

	        try {
	            String cmd = "ls -1 ~/test | egrep '\\.mp3$' | sed -e 's/\\..*$//'";

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
	                System.out.println("Success!");
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
	 
	 public void addButton() {
		 
		 ObservableList listOfFiles = listAvailable.getSelectionModel().getSelectedItems();
		 
		 for(Object item: listOfFiles) {
			 listCreation.getItems().add(item.toString());
		 }
	 }
	 

	 public void removeButton() {
		 
		 int i = listCreation.getSelectionModel().getSelectedIndex();
		 
		 if (i != -1) {
			 listCreation.getItems().remove(i);
		 }
		 
	 }
	 public void flickr(ActionEvent event) throws IOException {
		if(creationName.getText().equals("")) {
			alert.setContentText("Creation name empty");
			alert.setTitle("Empty Fields Required");
			alert.setHeaderText("Empty Fields Required");
			alert.show();
		}else {
			/*run another task to combine audio files here*/
			/* combined mp3 needs to be in the creations folder*/
			CreateFlickrTask createtask = new CreateFlickrTask(_wikitSearch ,creationName.getText(),(String)picturesNo.getSelectionModel().getSelectedItem());
	 		Thread thread = new Thread(createtask);
	 		thread.start();
		}
		
	 }
	 
	 @Override
	    public void initialize(URL location, ResourceBundle resources) {
	        String[] strings = getCreations();
	        listAvailable.getItems().addAll(strings);
	        listAvailable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	        
	        String[] pictures = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	        picturesNo.getItems().addAll(pictures);
	        picturesNo.getSelectionModel().selectFirst();
	        
	    }

}
