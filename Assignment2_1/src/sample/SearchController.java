package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SearchController implements Initializable {
	private ArrayList<String> wList;
	@FXML
	private TextField searchBar;
	
	@FXML
	private Text response;
	
    public void buttonMenu(ActionEvent event) throws IOException {
        Parent createParent = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Scene createScene = new Scene(createParent, 500, 500);

        //This gets the stage info
        Stage createWindow = (Stage)((Node)event.getSource()).getScene().getWindow();

        createWindow.setScene(createScene);
        createWindow.show();

    }
    
    public void buttonSearch(ActionEvent event) throws IOException {
    	response.setText("");
    	if (searchBar.getText().equals("")) {
    		
    		response.setText("Empty Search");
    	} else {
    		
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
        controller.setLines(largeLines);
        //This gets the stage info
        Stage createWindow = (Stage)((Node)event.getSource()).getScene().getWindow();

        createWindow.setScene(createScene);
        createWindow.show();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
