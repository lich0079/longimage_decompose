package longimage_decompose;


import java.awt.Color;

import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

public class Decompose {
	public static void main(String[] args) {
		
		MarvinImagePlugin moravec = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.corner.moravec");
		moravec.load();
		moravec.setAttribute("threshold", 50000);
		
		// 1. Figures
		MarvinImage imageIn = MarvinImageIO.loadImage("./res/1452478632_10.jpg");
		MarvinAttributes attr = new MarvinAttributes();
		moravec.process(imageIn, null, attr);
		imageIn = showCorners(imageIn, attr, 6);
		MarvinImageIO.saveImage(imageIn, "./res/1452478632_10_out.jpg");
		
//		// 2. Face
//		moravec.setAttribute("threshold", 2000);
//		moravec.setAttribute("matrixSize", 7);
//		imageIn = MarvinImageIO.loadImage("./res/alan.jpg");
//		attr = new MarvinAttributes();
//		moravec.process(imageIn, null, attr);
//		imageIn = showCorners(imageIn, attr, 3);
//		MarvinImageIO.saveImage(imageIn, "./res/alanOut.png");
//		
//		// 3. House
//		moravec.setAttribute("threshold", 1500);
//		moravec.setAttribute("matrixSize", 7);
//		imageIn = MarvinImageIO.loadImage("./res/cards.jpg");
//		attr = new MarvinAttributes();
//		moravec.process(imageIn, null, attr);
//		imageIn = showCorners(imageIn, attr, 3);
//		MarvinImageIO.saveImage(imageIn, "./res/cardsOut.png");
	}
	
	private static MarvinImage showCorners(MarvinImage image, MarvinAttributes attr, int rectSize){
		MarvinImage ret = image.clone();
		int[][] cornernessMap = (int[][]) attr.get("cornernessMap");
		int rsize=0;
		for(int x=0; x<cornernessMap.length; x++){
			for(int y=0; y<cornernessMap[0].length; y++){
				// Is it a corner?
				if(cornernessMap[x][y] > 0){
					rsize = Math.min(Math.min(Math.min(x, rectSize), Math.min(cornernessMap.length-x, rectSize)), Math.min(Math.min(y, rectSize), Math.min(cornernessMap[0].length-y, rectSize)));
					ret.fillRect(x, y, rsize, rsize, Color.red);
				}				
			}
		}
		
		return ret;
	}
}