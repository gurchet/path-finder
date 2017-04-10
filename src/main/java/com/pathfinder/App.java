package com.pathfinder;

import java.io.IOException;

import com.tavant.utils.TwfException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	String filePath = System.getProperty("user.dir") + "\\src\\test\\resources\\Repository - Copy.xls";
    	String baseUrl = "http://phptravels.com/demo/";
    	FindPath findpath = new FindPath(filePath,baseUrl);
    	 System.out.println( "Hello World! in pathfinder" );
    	try {
			findpath.getAllElementOfPage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TwfException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
    }
}
