package fileIO;

import essentailOils.EssentialOil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class OilFileHandler {

    public static List<EssentialOil> loadFile(String path){
        List<EssentialOil> oils = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))){
            String line = reader.readLine();
            while ((line = reader.readLine()) != null){
                EssentialOil oil = parseOilDataLine(line);
                if (oil!=null) {
                    oils.add(oil);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return oils;
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

    public static EssentialOil parseOilDataLine(String line){
        EssentialOil oil = null;
        if (line.length()>5) {
            String[] oilData = line.split(",");
            String name = oilData[0].trim();
            String[] attributes = oilData[1].split(";");
            String[] clashes = oilData[2].split(";");
            double price = 0.0;
            if (oilData.length > 3 && oilData[3].length() > 0){
                price = Double.valueOf(oilData[3]);
            }
            String[] concentrations = null;
            if (oilData.length > 4){
                concentrations = oilData[4].split(";");
            }
            oil = new EssentialOil(name);
            oil.setPricePerOunce(price);
            for (String s : attributes) {
                oil.addAttribute(s.trim());
            }
            for (String s : clashes) {
                oil.addClash(s.trim());
            }
            if (concentrations != null) {
                for (String s : concentrations) {
                    if(!s.equals("pure")) {
                        oil.addConcentration(s);
                    }
                }
            }
        }
        return oil;
    }

}

