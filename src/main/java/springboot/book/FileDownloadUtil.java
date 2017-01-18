package springboot.book;

import java.io.*;

public class FileDownloadUtil {

	// 文件流 参数为下载地址 和 本地存放地址含文件名
	public static boolean download(byte[] bs, String fileName)
			throws Exception {

		// 读取数据长度
		int len;
		// 判断文件目录是否存在
		String path = fileName.substring(0, fileName.lastIndexOf("/"));
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		// 输出的文件流
		OutputStream os = new FileOutputStream(fileName);

			os.write(bs);
		// 完毕关闭所有链接
		os.close();
		return true;
	}

	
	  public static String readTxtFile(String filePath){
		     String TotalString="";
	        try {
	                String encoding="UTF-8";
	                File file=new File(filePath);
	                if(file.isFile() && file.exists()){ //判断文件是否存在
	                    InputStreamReader read = new InputStreamReader(
	                    new FileInputStream(file),encoding);//考虑到编码格式
	                    BufferedReader bufferedReader = new BufferedReader(read);
	                    String CurrentLine="";
	                    while((CurrentLine = bufferedReader.readLine()) != null){
	                    	TotalString += CurrentLine + "\r\n";
	                    }
	                    read.close();
	        }else{
	            System.out.println("找不到指定的文件");
	        }
	        } catch (Exception e) {
	            System.out.println("读取文件内容出错");
	            e.printStackTrace();
	        }
	       return TotalString;
	    }

}
