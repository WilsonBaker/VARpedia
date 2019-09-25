package sample;

import java.io.BufferedReader;
import javafx.collections.ObservableList;
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

public class ChooseAudioController implements Initializable {
	
	 @FXML private ListView listAvailable;
	 @FXML private ListView listCreation;
	 
	 @FXML private ChoiceBox picturesNo;
	 
	 
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
	 
	 @Override
	    public void initialize(URL location, ResourceBundle resources) {
	        String[] strings = getCreations();
	        listAvailable.getItems().addAll(strings);
	        listAvailable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	        
	        String[] pictures = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	        picturesNo.getItems().addAll(pictures);
	    }

}
