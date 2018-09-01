package utils;

import core.TableContext;
import javabean.TableInfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

// write .java source code to file
public class CreatePoClass {

   public static void create(String srcCode, String className) throws IOException {
        // store path
	    String classPath = "src/po";

        File file = new File(classPath);

        //System.out.println(file.getAbsolutePath());
        /*if(file == null){
            file.mkdirs();
        }*/
        file = new File(file.getAbsolutePath() + File.separator + className.substring(0, 1).toUpperCase() + className.substring(1, className.length()) + ".java");
        //System.out.println(file.getAbsolutePath());
        //file.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(srcCode);
        bw.flush();

        bw.close();

    }


   // test
    public static void main(String[] args) throws IOException {

        //create("");
        //create("aaannn", "Emp");

        Map<String, TableInfo> map = TableContext.tables;
        //System.out.println(map);

        JavaSrcCreator jsc = new JavaSrcCreator(map);
        System.out.println(jsc.createSrcCode("t1"));
        create(jsc.createSrcCode("t1"), "t1".toUpperCase());
    }

}
