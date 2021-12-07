package tests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GetFilesRecursive {
    public static List <String> getFilesRecursively(File dir){
        List <String> ls = new ArrayList<String>();
        if (dir.isDirectory())
            for (File fObj : dir.listFiles()) {
                if(fObj.isDirectory()) {
                    ls.add(String.valueOf(fObj));
                    ls.addAll(getFilesRecursively(fObj));
                } else {
                    ls.add(String.valueOf(fObj));
                }
            }
        else
            ls.add(String.valueOf(dir));

        return ls;
    }

    public static void main(String[] args) {
        List <String> ls = getFilesRecursively(new File("/Users/jonporsche/Documents/Dev Projects.nosync"));
        for (String file:ls) {
            System.out.println(file);
        }
        System.out.println(ls.size());
    }
}