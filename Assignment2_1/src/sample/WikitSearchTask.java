package sample;

import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class WikitSearchTask extends Task {

	private String value;
    private ArrayList<String> lineArray = new ArrayList<String>();

    public  WikitSearchTask(String value) {
        this.value = value;
    }
    public ArrayList<String> getline() {
    	return lineArray;
    }
    @Override
    protected Object call() throws Exception {
    	
        try {
            String wikitSearch = "wikit "+value+" | grep -Po \"\\b(?<d>.*?\\.(?:\\s|$))\""  ;
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
                    System.out.println(line);
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
