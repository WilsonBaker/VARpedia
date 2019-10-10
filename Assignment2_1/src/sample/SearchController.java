package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SearchController implements Initializable {
	private String _wikitSearch;
	private ArrayList<String> wList;
	@FXML
	private TextField searchBar;
	
	@FXML
	private Text response;
	
	@FXML
	private Button searchButton;
	
    public void buttonMenu(ActionEvent event) throws IOException {
        Parent createParent = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Scene createScene = new Scene(createParent, 500, 500);

        //This gets the stage info
        Stage createWindow = (Stage)((Node)event.getSource()).getScene().getWindow();

        createWindow.setScene(createScene);
        createWindow.show();

    }
    public void runCommand(String com) {
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
    public void buttonSearch(ActionEvent event) throws IOException {
    	runCommand("rm Audio/*.txt");
    	runCommand("rm Audio/*.mp3");
    	runCommand("rm Audio/*.wav");
    	runCommand("rm Creations/*.mp3");
    	runCommand("rm Creations/*.wav");
    	runCommand("rm Audio");
    	response.setText("");
    	if (searchBar.getText().equals("")) {
    		
    		response.setText("Empty Search");
    	} else {
    		_wikitSearch=searchBar.getText();
    		WikitSearchTask wikitSearchTask = new WikitSearchTask(searchBar.getText());
    		Thread thread = new Thread(wikitSearchTask);
    		thread.start();
    		response.setText("Loading . . .");
    		wikitSearchTask.messageProperty().addListener(new ChangeListener<String>() {
    			@Override
    			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
    					
    			}
    			});
    		wikitSearchTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
    			@Override
    			public void handle(WorkerStateEvent event2) {
    					
    					wList= wikitSearchTask.getline();
    					
    					try {
    						toSelect(event);
    					} catch(IOException e) {
    					
    					}
    					
    					
    					
    					
    			}
    			
    		});

    		wikitSearchTask.setOnCancelled(new EventHandler<WorkerStateEvent>() {
    			@Override
    			public void handle(WorkerStateEvent event2) {
    					
    					
    					response.setText("No Search Found");
    					
    			}
    		});
    	}
    	

    }
    public void toSelect(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(getClass().getResource("selection.fxml"));
    	Parent createParent= loader.load();
        Scene createScene = new Scene(createParent, 500, 500);
        
        SelectionController controller = loader.getController();
        String largeLines = "";
        for(int i=0;i<wList.size();i++) {
        	largeLines= largeLines + wList.get(i) + "\n";
        }
        controller.setLines(largeLines, _wikitSearch);
        
        
        //This gets the stage info
        Stage createWindow = (Stage)((Node)event.getSource()).getScene().getWindow();

        createWindow.setScene(createScene);
        createWindow.show();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	searchButton.disableProperty().bind(
    		    Bindings.isEmpty(searchBar.textProperty())
    		    
    		);
    }
}
