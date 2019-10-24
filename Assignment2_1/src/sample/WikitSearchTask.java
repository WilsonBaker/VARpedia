package sample;

import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
/* This task gets the lines from wikipedia of the selected search term*/
public class WikitSearchTask extends Task {

	private String value;
    private ArrayList<String> lineArray = new ArrayList<String>();
    
    public  WikitSearchTask(String value) {
        this.value = value;
    }
    /*This method returns the lines to be used by the selection scene*/
    public ArrayList<String> getline() {
    	return lineArray;
    }
    @Override
    /* This method runs the wikit bash command to receive the lines on another thread*/
    protected Object call() throws Exception {
    	
        try {
            String wikitSearch = "wikit  "+value+" | grep -Po \"\\b(?<d>.*?\\.(?:\\s|$))\""  ;
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", wikitSearch);

            Process process = builder.start();

            InputStream out = process.getInputStream();
            BufferedReader stdout = new BufferedReader(new InputStreamReader(out));
            String line;
            int exitStatus = process.waitFor();
            
            if(exitStatus ==0) {
            	
            	while ((line = stdout.readLine()) != null ) {
            		
                    lineArray.add(line);
                    updateMessage(line);
                    
                    }
            	
            } else {
            	this.cancel();
            	process.destroy();
            }
            
         
            
                


        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
