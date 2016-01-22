package longimage_decompose;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
	static ExecutorService pools = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);
	
	public static void main(String[] args) throws IOException {
		if(args != null && args.length >0){
		    String fileDir = args[0];
		    File file = new File(fileDir);
		    if(file.isDirectory()){
		        new File(file.getAbsolutePath()+"/split").mkdir();
		        File[] files= file.listFiles(new FileFilter() {
                    
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.isFile();
                    }
                });
		        for (File f : files) {
                    String path = f.getAbsolutePath();
                    if(path.contains("DS_Store")){
                        continue;
                    }
                    pools.execute(new ImageDecompose(path));
                }
		        pools.shutdown();
		    }
		}else {
		    System.out.println("no input parameter for dir path");
		}
	}
}
