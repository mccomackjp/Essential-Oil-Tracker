package essentailOils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mccomackjp on 10/24/2016.
 */
public class CSVfileHandler {

    String fileName;

    public CSVfileHandler(String fileName){
        this.fileName = fileName;
    }


    public static void saveFile(String path, List<String> cells) throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        for (String cell : cells){
            if (cell.equals(",")){
                writer.newLine();
            } else {
                writer.write(cell);
            }
        }
        writer.close();
    }


}
