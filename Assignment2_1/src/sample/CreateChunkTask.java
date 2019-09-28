package sample;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.concurrent.Task;

public class CreateChunkTask extends Task{
	private String _lines;
	private String _name;
	private Object _voice;
	public CreateChunkTask(String lines, String name , Object voice) {
		_lines=lines;
		_name=name;
		_voice=voice;
	}
	
	@Override
    protected Object call() throws Exception {
    	if(_voice.equals("espeak")) {
    		try {
            	
            	String create = "espeak \""+_lines+"\" -w Audio/"+_name+".wav "  ;
                ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", create);

                Process process = builder.start();

                InputStream out = process.getInputStream();
                BufferedReader stdout = new BufferedReader(new InputStreamReader(out));
            	
            } catch(Exception ex) {
                ex.printStackTrace();
            }
    		
    	} else if (_voice.equals("festival")) {
    		 try {
    	        	
    	        	String create = "echo \""+_lines+"\" | text2wave -o Audio/"+_name+".wav "  ;
    	        	
    	            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", create);

    	            Process process = builder.start();

    	            InputStream out = process.getInputStream();
    	            BufferedReader stdout = new BufferedReader(new InputStreamReader(out));
    	        	
    	        } catch(Exception ex) {
    	            ex.printStackTrace();
    	        }
    	}
        

        return null;
	}

}
