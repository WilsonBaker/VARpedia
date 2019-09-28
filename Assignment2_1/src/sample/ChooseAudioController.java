package sample;

import java.io.BufferedReader;
import java.io.File;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

public class ChooseAudioController implements Initializable {
	 private String _wikitSearch;
	 private Alert alert = new Alert(AlertType.INFORMATION);
	 @FXML private ListView listAvailable;
	 @FXML private ListView listCreation;
	 @FXML private TextField creationName;
	 @FXML private Text text;
	 
	 @FXML private ChoiceBox picturesNo;
	 public void setWikitName(String name) {
		 _wikitSearch=name;
		 
	 }
	 
	 public String[]  getCreations() {
		 String path = System.getProperty("user.dir");

	        try {
	            String cmd = "ls -1 " + path + "/Audio | egrep '\\.mp3$' | sed -e 's/\\..*$//'";

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

	        } catch (Exception e) {
	        	e.printStackTrace();
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
		File temp = new File("Creations/"+creationName.getText()+".mp4");
		if(creationName.getText().equals("")) {
			alert.setContentText("Creation name empty");
			alert.setTitle("Empty Fields Required");
			alert.setHeaderText("Empty Fields Required");
			alert.show();
		}else if(temp.exists()){
			alert.setContentText("Creation " + creationName.getText()+ " exists");
			alert.setTitle("Creation Exists");
			alert.setHeaderText("Creation Exists");
			alert.show();
			try {
	            String del = "rm Audio/*.mp3";
	            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", del);

	            Process process = builder.start();

	            InputStream out = process.getInputStream();
	            BufferedReader stdout = new BufferedReader(new InputStreamReader(out));
	            int exitStatus = process.waitFor();
	            
	            if(exitStatus ==0) {
	            	
	            }
	            
			}catch(Exception ex) {
	            ex.printStackTrace();
	        }
			
		}else {
			/*run another task to combine audio files here*/
			/* combined mp3 needs to be in the creations folder*/
			text.setText("Combining Audio...");
			CombineAudioTask combineTask = new CombineAudioTask(listCreation.getItems());
			Thread combineThread = new Thread(combineTask);
			combineThread.start();
			combineTask.setOnSucceeded(null);
			combineTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
    			@Override
    			public void handle(WorkerStateEvent event2) {
					text.setText("Creating...");
					CreateFlickrTask createtask = new CreateFlickrTask(_wikitSearch ,creationName.getText(),(String)picturesNo.getSelectionModel().getSelectedItem());
			 		Thread thread = new Thread(createtask);
			 		thread.start();
	 		
	 		
			 		createtask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
		    			@Override
		    			public void handle(WorkerStateEvent event2) {
		    				text.setText("");
		    				alert.setContentText("Creation" + creationName.getText() + " Created");
		    				alert.setTitle("Creation Created");
		    				alert.setHeaderText("Creation Created");
		    				alert.show();
		    				
		    				try {
	    						toMenu(event);
	    					} catch(IOException e) {
	    					
	    					}
		    				/* Over here you need to play the video*/
		    			}
    		});
    			}
			});
	 		
	 		
		}
		
	 }
	 
	 public void toMenu(ActionEvent event) throws IOException {
			
			
	        Parent createParent = FXMLLoader.load(getClass().getResource("menu.fxml"));
	        Scene createScene = new Scene(createParent, 500, 500);

	        //This gets the stage info
	        Stage createWindow = (Stage)((Node)event.getSource()).getScene().getWindow();

	        createWindow.setScene(createScene);
	        createWindow.show();
	        
	    }
	 public void buttonMenu(ActionEvent event) throws IOException {
			File temp = new File("Audio");
			for(File file: temp.listFiles()) {
				file.delete();
			}
			
	        Parent createParent = FXMLLoader.load(getClass().getResource("menu.fxml"));
	        Scene createScene = new Scene(createParent, 500, 500);

	        //This gets the stage info
	        Stage createWindow = (Stage)((Node)event.getSource()).getScene().getWindow();

	        createWindow.setScene(createScene);
	        createWindow.show();
	        
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
