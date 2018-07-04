package com.alex10011.repush.util;

import java.io.*;

/**
 * @Auther: yl
 * @Date: 2018/6/21 10:35
 * @Description:
 */
public class CommonFileUtil {
    /*
     *按行读取文件
     */
    public static void readFile(String filePath) {
        File file = new File(filePath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                System.out.println(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /*
     *以追加方式写文件
     */
    public static void writeFile(String filePath, String conent) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true)));
            out.write(conent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     *创建文件
     */
    public static void createFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            //System.out.println("文件已存在");
        } else {
            try {
                File fileParent = file.getParentFile();
                if (fileParent != null) {
                    if (!fileParent.exists()) {
                        fileParent.mkdirs();
                    }
                }
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     *以追加方式写文件，效率低
     */
    public static void writeFileByFileWriter(String filePath, String content) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(new File(filePath), true);
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isExistFile(String filePath){
        File file=new File(filePath);
        return file.exists();
    }

}
