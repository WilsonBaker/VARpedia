package sample;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.concurrent.Task;
/* This task creates chunks from the selected lines in the GUI*/
public class CreateChunkTask extends Task{
	private String _lines;
	private String _name;
	private Object _voice;
	/* This constructor gets the lines, voice and name of chunk*/
	public CreateChunkTask(String lines, String name , Object voice) {
		_lines=lines;
		_name=name;
		_voice=voice;
	}
	/*When this task is carried out on a thread it creates chunk files with the selected lines*/
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
    	/* for other voice*/	
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
