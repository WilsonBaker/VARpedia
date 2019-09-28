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

import javafx.concurrent.Task;

public class CreateFlickrTask extends Task{
	private ArrayList<String> _rimages = new ArrayList<String>();
	private ArrayList<String> _images = new ArrayList<String>();
	private String _wikitSearch ;
	private String _name;
	private int _number;
	private Float _duration = 0.0f;
	public CreateFlickrTask(String search, String name , String number) {
		_wikitSearch=search;
		_name=name;
		try {
			_number=Integer.parseInt(number);
			
		}catch(NumberFormatException e){
			
		}
		
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
	public static String getAPIKey(String key) throws Exception {
		// TODO fix the following based on where you will have your config file stored

		String config = System.getProperty("user.dir") 
				+ System.getProperty("file.separator")+ "flickr-api-keys.txt"; 
		
//		String config = System.getProperty("user.home")
//				+ System.getProperty("file.separator")+ "bin" 
//				+ System.getProperty("file.separator")+ "flickr-api-keys.txt"; 
		File file = new File(config); 
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		
		String line;
		while ( (line = br.readLine()) != null ) {
			if (line.trim().startsWith(key)) {
				br.close();
				return line.substring(line.indexOf("=")+1).trim();
			}
		}
		br.close();
		throw new RuntimeException("Couldn't find " + key +" in config file "+file.getName());
	}
	@Override
    protected Object call() throws Exception {
		
		runCommand("ffmpeg -y -i Creations/output.mp3 Creations/output.wav");
		
		try {
			String apiKey = getAPIKey("apiKey");
			String sharedSecret = getAPIKey("sharedSecret");
			
			Flickr flickr = new Flickr(apiKey, sharedSecret, new REST());
			
			String query = _wikitSearch;
			int resultsPerPage = _number;
			int page = 0;
			String path = System.getProperty("user.dir");
			
	        PhotosInterface photos = flickr.getPhotosInterface();
	        SearchParameters params = new SearchParameters();
	        params.setSort(SearchParameters.RELEVANCE);
	        params.setMedia("photos"); 
	        params.setText(query);
	        
	        PhotoList<Photo> results = photos.search(params, resultsPerPage, page);
	        
	        
	        for (Photo photo: results) {
	        	
	        	try {
	        		BufferedImage image = photos.getImage(photo,Size.LARGE);
		        	String filename = query.trim().replace(' ', '-')+"-"+System.currentTimeMillis()+"-"+photo.getId()+".jpg";
		        	File outputfile = new File(path+ "/Creations",filename);
		        	_images.add(path+ "/Creations/"+filename);
		        	ImageIO.write(image, "jpg", outputfile);
		        	
	        	} catch (FlickrException fe) {
	        		System.err.println("Ignoring image " +photo.getId() +": "+ fe.getMessage());
				}
	        	
	        }
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
	       
	        _duration = _duration/(_images.size());
	        
	        for (String i : _images) {
	        	String rimage = i.replace(".","new." );
	        	_rimages.add(rimage);
	        	runCommand("ffmpeg -i "+ i + " -vf scale=400:400 "+ rimage);
	        	
	        	
	        }
	        runCommand("touch hi.txt");
            
            for (String i : _rimages) {
            	
            	runCommand("echo \"file '"+i+"'\nduration "+_duration+"\" >> hi.txt");

            }
            
            runCommand("echo \"file '"+_rimages.get(_rimages.size()-1)+"'\" >> hi.txt");
            
            
            runCommand("ffmpeg  -f concat -safe 0 -i hi.txt  -i ./Creations/output.mp3  -filter:v \"format=yuv420p,drawtext=fontfile=myfont.ttf:fontsize=30: fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:text='awesome\"  -c:v libx264 -c:a aac -shortest  ./Creations/"+_name+".mp4");
            runCommand("rm -f hi.txt");
            runCommand("rm Creations/*.jpg");
            runCommand("rm Creations/*.wav");
            runCommand("rm Creations/*.mp3");
            runCommand("rm Audio/*.mp3");
            runCommand("rm Audio/mylist.txt");
            
            
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
		return null;
	}
}
