package longimage_decompose;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Test {
	static boolean prevIsWhite = true;
	static int top = 0;
	static int bottom = 0;
	public static void main(String[] foo) {
		try {
		      // get the BufferedImage, using the ImageIO class
		      BufferedImage image = 
		        ImageIO.read(Test.class.getResource("./1452478632_10.jpg"));
		      
		      marchThroughImage(image);
		      
		      
		      int w = image.getWidth();
			    int h = image.getHeight();
			    System.out.println(w+" "+h+" "+top+" "+bottom);
			    BufferedImage bi =image.getSubimage(0, top, w, bottom - top);
//		      BufferedImage bi = new BufferedImage(w,bottom - top,BufferedImage.TYPE_INT_RGB);
		      
		      ImageIO.write(bi, "jpg",new File("./res/xxx"+System.currentTimeMillis()+".jpg"));
		    } catch (IOException e) {
		      System.err.println(e.getMessage());
		    }
	  }
	 
	  public static void printPixelARGB(int pixel) {
	    int alpha = (pixel >> 24) & 0xff;
	    int red = (pixel >> 16) & 0xff;
	    int green = (pixel >> 8) & 0xff;
	    int blue = (pixel) & 0xff;
	    System.out.println("argb: " + alpha + ", " + red + ", " + green + ", " + blue+","+pixel);
	  }
	 
	  private static void marchThroughImage(BufferedImage image) {
	    int w = image.getWidth();
	    int h = image.getHeight();
//	    System.out.println("width, height: " + w + ", " + h);
	 
	    for (int y = 0; y < h; y++) {
	    	boolean currentIsWhite = true;
	      for (int x = 0; x < w; x++) {
//	        System.out.println("x,y: " + x + ", " + y);
	        int pixel = image.getRGB(x, y);
//	        printPixelARGB(pixel);
//	        System.out.println("");
	        if(pixel != -1){
	        	currentIsWhite = false;
	        	
	        }
	      }
	      
	        if(!currentIsWhite && prevIsWhite){
	        	top = y;
	        	System.out.println("line is top:"+y);
	        }else if(currentIsWhite && !prevIsWhite){
	        	bottom = y;
	        	System.out.println("line is bottom:"+y);
	        }
	        
	        prevIsWhite = currentIsWhite;
	    }
	  }
	 
}
