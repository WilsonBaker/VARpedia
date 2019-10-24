package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;



import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/* The purpose of this class is to have a controller that runs the scene with the images fxml file*/
public class ChooseImageController implements Initializable{
	private ArrayList<String> _orig;
	private ArrayList<String> _chosen = new ArrayList<String>();
	private String _search;
	private String _name;
	
	@FXML
	private ImageView img1;
	
	@FXML
	private ImageView img2;
	@FXML
	private ImageView img3;
	@FXML
	private ImageView img4;
	@FXML
	private ImageView img5;
	@FXML
	private ImageView img6;
	@FXML
	private ImageView img7;
	@FXML
	private ImageView img8;
	@FXML
	private ImageView img9;
	@FXML
	private ImageView img10;
	
	@FXML
	private CheckBox check1;
	
	@FXML
	private CheckBox check2;
	@FXML
	private CheckBox check3;
	@FXML
	private CheckBox check4;
	@FXML
	private CheckBox check5;
	@FXML
	private CheckBox check6;
	@FXML
	private CheckBox check7;
	@FXML
	private CheckBox check8;
	@FXML
	private CheckBox check9;
	
	@FXML
	private CheckBox check10;
	
	@FXML
	private TextField text;
	
	@FXML
	private Text status;
	
	@FXML
	private Button createButton;
	
	
	private Alert alert = new Alert(AlertType.ERROR);
	/* After searching the images on flickr and storing the paths, we put the images on Image views in the scene*/
	public void setImages(ArrayList<String> images, String search, String name) {
		
		_search=search;
		_name=name;
		_orig=images;
		img1.setImage(new Image("file:"+images.get(0)));
		img2.setImage(new Image("file:"+images.get(1)));
		img3.setImage(new Image("file:"+images.get(2)));
		img4.setImage(new Image("file:"+images.get(3)));
		img5.setImage(new Image("file:"+images.get(4)));
		img6.setImage(new Image("file:"+images.get(5)));
		img7.setImage(new Image("file:"+images.get(6)));
		img8.setImage(new Image("file:"+images.get(7)));
		img9.setImage(new Image("file:"+images.get(8)));
		img10.setImage(new Image("file:"+images.get(9)));
	
	}
	/* This method creates a creation from the output audio file and the selected images*/
	public void create(ActionEvent event) throws IOException {
		if(check1.isSelected()) {
			_chosen.add(_orig.get(0));
		}
		if(check2.isSelected()) {
			_chosen.add(_orig.get(1));
		}
		if(check3.isSelected()) {
			_chosen.add(_orig.get(2));
		}
		if(check4.isSelected()) {
			_chosen.add(_orig.get(3));
		}
		if(check5.isSelected()) {
			_chosen.add(_orig.get(4));
		}
		if(check6.isSelected()) {
			_chosen.add(_orig.get(5));
		}
		if(check7.isSelected()) {
			_chosen.add(_orig.get(6));
		}
		if(check8.isSelected()) {
			_chosen.add(_orig.get(7));
		}
		if(check9.isSelected()) {
			_chosen.add(_orig.get(8));
		}
		if(check10.isSelected()) {
			_chosen.add(_orig.get(9));
		}
		/* must select one image or more*/
		if(_chosen.size()==0) {
			alert.setContentText("No Image Selected");
			alert.setTitle("No Image Selected");
			alert.setHeaderText("No Image Selected");
			alert.show();
		}else {
			status.setText("Creating...");
			CreationTask audiotask = new CreationTask(text.getText(),_name,_chosen,_search);
			Thread thread = new Thread(audiotask);
	 		thread.start();
	 		
	 		audiotask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event3) {
					
					/* After creating, play creation*/
					try {
						
	    				String playString = _name;
	    				
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
					}catch(IOException e) {
						
					}
					
					
				}
	 		});
		}
		
		
	}
	/* This method takes a bash command as string and runs the bash command*/
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
	/* This method returns to main menu and clears all unnecessary files*/
	public void buttonMenu(ActionEvent event) throws IOException {
		runCommand("rm -f hi.txt");
        runCommand("rm Creations/*.jpg");
        runCommand("rm Creations/*.wav");
        runCommand("rm Creations/*.mp3");
        runCommand("rm Audio/*.wav");
        runCommand("rm Audio/mylist.txt");
        runCommand("rm -r Audio");
		
        Parent createParent = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Scene createScene = new Scene(createParent, 500, 500);

        //This gets the stage info
        Stage createWindow = (Stage)((Node)event.getSource()).getScene().getWindow();

        createWindow.setScene(createScene);
        createWindow.show();
        
    }
	
	 @Override
	    public void initialize(URL location, ResourceBundle resources) {
		 /* cant move on with empty creation name*/
		 createButton.disableProperty().bind(
				    Bindings.isEmpty(text.textProperty())
				    
				    
				);
	    }
}
