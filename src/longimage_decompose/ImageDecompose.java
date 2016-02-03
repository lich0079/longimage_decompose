package longimage_decompose;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

public class ImageDecompose implements Runnable {

    private String fileName;
    private int top = 0;
    private int bottom = 0;
    private int left = 0;
    private int right = 0;
    private int w = 0;
    private int h = 0;

    private int index = 0 ;
    public ImageDecompose(String fileName) {
        super();
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try {
            File file = new File(fileName);
            BufferedImage image = ImageIO.read(file);
            System.out.println(fileName+","+w+" "+h);
            w = image.getWidth();
            h = image.getHeight();
            while (bottom < h) {
                Rect rect = findNextTopBottom(image, bottom);

                if(rect.top == -1){
                    return;
                }
                while (right < w) {
                    rect = findNextLeftRight(image, rect, right);
                    
                    if(rect.left != -1 &&
                            rect.right - rect.left > 100 &&
                            rect.bottom - rect.top > 100
                            ){
                        System.out.println(rect);
                        BufferedImage bi = image.getSubimage(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
                        
                        File outPutImage = new File(file.getParentFile().getAbsolutePath()+"/split/"+file.getName().substring(0, file.getName().length()-4)+"_"+index+++".jpg");
                        ImageOutputStream  ios =  ImageIO.createImageOutputStream(outPutImage);
                        
                        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");
                        ImageWriter writer = iter.next();
                        JPEGImageWriteParam jpegImageWriteParam = new JPEGImageWriteParam(Locale.US);
                        jpegImageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                        jpegImageWriteParam.setCompressionQuality(1.0f);
                        writer.setOutput(ios);
                        writer.write(null, new IIOImage(bi,null,null),jpegImageWriteParam);
                        writer.dispose();
                    }
                    right = rect.right + 1;
                    rect.left = -1;
                }
                right = 0;
                bottom = rect.bottom + 1;
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    private Rect findNextLeftRight(BufferedImage image, Rect rect, int x) {
        boolean prevXIsWhite = true;
        for (; x < w; x++) {
            boolean currentIsWhite = true;
            Set<Integer> pixels = new HashSet<Integer>();
            for (int y = rect.top; y < rect.bottom; y++) {
                int pixel = image.getRGB(x, y);
                pixels.add(pixel);
                if (pixel != -1) {
                    currentIsWhite = false;
                }
            }
            

            if (!currentIsWhite && prevXIsWhite) {
                rect.left = x;
            } else if ((currentIsWhite && !prevXIsWhite) 
                    || (rect.left != -1 && pixels.size()*100/(rect.bottom -rect.top) < 5)
                    ) {//only one color count as end line
                rect.right = x;
//                if(pixels.size()>1)System.out.println("column is right:"+x+", color size:"+pixels.size());
                return rect;
            }

            prevXIsWhite = currentIsWhite;
        }
        rect.right = x;
        return rect;
    }

    private Rect findNextTopBottom(BufferedImage image, int y) {
        boolean prevYIsWhite = true;
        Rect rect = new Rect();

        for (; y < h; y++) {
            boolean currentIsWhite = true;
            Set<Integer> pixels = new HashSet<Integer>();
            for (int x = 0; x < w; x++) {
                int pixel = image.getRGB(x, y);
                pixels.add(pixel);
//                printPixelARGB(pixel);
//                System.out.println("x,y: " + x + ", " + y+","+pixel);
                if (pixel != -1) {
                    currentIsWhite = false;
                }
            }

            if (!currentIsWhite && prevYIsWhite) {
                rect.top = y;
            } else if ((currentIsWhite && !prevYIsWhite) 
                    || (rect.top != -1 && pixels.size()*100/w < 5)
                    ) {//only one color count as end line
                rect.bottom = y;
//                System.out.println("line is bottom:"+y+", color size:"+pixels.size());
                return rect;
            }

            prevYIsWhite = currentIsWhite;
        }
        rect.bottom = y;
//        System.out.println("line is bottom:" + y);
        return rect;
    }
    
    public void printPixelARGB(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        System.out.println("argb: " + alpha + ", " + red + ", " + green + ", " + blue+","+pixel);
      }

    public static void main(String[] args) {
        ImageDecompose decompose = new ImageDecompose("/Users/zhayang/test.jpg");
        decompose.run();
    }
}
