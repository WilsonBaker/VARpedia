package sample;

import javafx.concurrent.Task;

public class CreateFlickrTask extends Task{
	
	private String _wikitSearch ;
	private String _name;
	private int _number;
	public CreateFlickrTask(String search, String name , String number) {
		_wikitSearch=search;
		_name=name;
		try {
			_number=Integer.parseInt(number);
			
		}catch(NumberFormatException e){
			
		}
		
	}
	@Override
    protected Object call() throws Exception {
		System.out.println(_number);
		System.out.println(_wikitSearch);
		System.out.println(_name);
		return null;
	}
}
