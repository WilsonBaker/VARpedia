package sample;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photos.Size;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class CreationTask extends Task{
	private ArrayList<String> _rimages;
	
	private String _wikitSearch ;
	private String _name;
	private String _text;
	private String _empty=" ";
	
	/*private ObservableList audioList;*/
	
	private Float _duration = 0.0f;
	public CreationTask(String search, String name , ArrayList<String> rimages ,String text/*,ObservableList list*/) {
		_wikitSearch=search;
		_name=name;
		_rimages=rimages;
		_text=text;
		/*audioList = list;*/
		
		
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
	
	@Override
    protected Object call() throws Exception {
		
		String wavLength = "soxi -D Creations/output.wav"  ;
        
        
        ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", wavLength);

        Process process = builder.start();
       
        InputStream out = process.getInputStream();
        BufferedReader stdout = new BufferedReader(new InputStreamReader(out));
        String length = stdout.readLine();
        int exitStatus = process.waitFor();
       
        if(exitStatus ==0) {
        	 try {
 	        	_duration = Float.parseFloat(length);
 	        	
 	        }catch(NumberFormatException e){
 	        	
 	 
 	        }
        }
        
        Float each = _duration/(_rimages.size());
        
        
        runCommand("touch hi.txt");
        
        for (String i : _rimages) {
        	
        	runCommand("echo \"file '"+i+"'\nduration "+each+"\" >> hi.txt");

        }
        File temp = new File("Quiz/"+_text+".mp4");
        runCommand("echo \"file '"+_rimages.get(_rimages.size()-1)+"'\" >> hi.txt");
        
       
        runCommand("ffmpeg -y -f concat -safe 0 -i hi.txt  -i ./Creations/output.wav  -filter:v \"drawtext=fontfile=myfont.ttf:fontsize=30: fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:text='"+_wikitSearch+"\"  -c:v libx264 -c:a aac  -pix_fmt yuv420p  ./Creations/"+_name+".mp4 ; ffmpeg -y -i ./Creations/"+_name+".mp4 -t "+_duration+" ./Creations/"+_name+".mp4 ");
        if(!(temp.exists())){
        	runCommand("ffmpeg -y -f concat -safe 0 -i hi.txt  -i ./Creations/output.wav  -filter:v \"drawtext=fontfile=myfont.ttf:fontsize=30: fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:text='"+_empty+"\" -c:v libx264 -c:a aac  -pix_fmt yuv420p  ./Quiz/"+_text+".mp4 ; ffmpeg -y -i ./Quiz/"+_text+".mp4 -t "+_duration+" ./Quiz/"+_text+".mp4 ");
        }
        
        runCommand("rm -f hi.txt");
        runCommand("rm Creations/*.jpg");
        runCommand("rm Creations/*.wav");
        runCommand("rm Creations/*.mp3");
        runCommand("rm Audio/*.wav");
        runCommand("rm Audio/mylist.txt");
       
		
		
	        
            
		
		
		
		return null;
	}
}
