package Util;

import UI.UIConst;

import java.io.*;

public class FileUtil {
    /**
     * 获得指定文件的byte数组
     */
    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
                if (UIConst.DEBUG) {
                    System.out.println("文件转换正在进行中~~~~");
                }
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
            if(UIConst.DEBUG){
                System.out.println("文件转换已完成,已生成数组~~~");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 根据byte数组，生成文件
     */
    public static void getFile(byte[] bfile, String filePath, String fileName) {
        String tempName = null;
        int start = filePath.lastIndexOf("\\");
        if (start != -1 ) {
            tempName =  filePath.substring(start + 1);
        }
        fileName = tempName;
        File tempFile  = new File(filePath);
        String parentPath = tempFile.getParent();
        filePath = parentPath;
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
            if(UIConst.DEBUG){
                System.out.println("已生成文件~~~");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
