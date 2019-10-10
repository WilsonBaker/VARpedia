package sample;

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
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML private ListView listView;
    private Alert alert = new Alert(AlertType.ERROR);
    private ArrayList<String> _quiz = new ArrayList<String>();
    

    public void buttonCreate(ActionEvent event) throws IOException {
        Parent createParent = FXMLLoader.load(getClass().getResource("search.fxml"));
        Scene createScene = new Scene(createParent, 500, 500);

        //This gets the stage info
        Stage createWindow = (Stage)((Node)event.getSource()).getScene().getWindow();

        createWindow.setScene(createScene);
        createWindow.show();

    }
    
    
    public void buttonDelete(ActionEvent event) throws IOException{
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
    	
    }
    public void buttonPlay(ActionEvent event) throws IOException{
    	if (listView.getSelectionModel().getSelectedItem() != null) {
	    	String playString = listView.getSelectionModel().getSelectedItem().toString();
	
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("something.fxml"));
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
    		System.out.println(_quiz.get(0));
    	}
		
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] strings = getCreations();
        listView.getItems().addAll(strings);
    }


    
}
