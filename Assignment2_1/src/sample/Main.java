package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    Stage window;
    Scene scene1, scene2;

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        window.setTitle("VARpedia");
        window.setScene(new Scene(root, 500, 500));
        window.show();
    }

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
    public static void main(String[] args) {
    	File temp = new File("Audio");
    	if(temp.exists()) {
    		
    	}else {
    		runCommand("mkdir Audio");
    	}
    	
    	File temp2 = new File("Creations");
    	if(temp2.exists()) {
    		
    	}else {
    		runCommand("mkdir Creations");
    	}
    	File temp3 = new File("Background");
    	if(temp3.exists()) {
    		
    	}else {
    		runCommand("mkdir Background");
    	}
    	DownloadBackgroundMusicTask musicTask = new DownloadBackgroundMusicTask();
		Thread thread = new Thread(musicTask);
		thread.start();
		runCommand("rm -f hi.txt");
        runCommand("rm Creations/*.jpg");
        runCommand("rm Creations/*.wav");
        runCommand("rm Creations/*.mp3");
        runCommand("rm Audio/*.wav");
        runCommand("rm Audio/mylist.txt");
        launch(args);
    }
}
