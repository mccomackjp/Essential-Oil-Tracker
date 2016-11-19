package fileIO;

import essentailOils.EssentialOil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;


public class OilFileHandler {

    public static List<EssentialOil> loadOilFile(String path){
        List<EssentialOil> oils = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))){
            reader.readLine(); //ignore first line of file
            String line;
            while ((line = reader.readLine()) != null){
                EssentialOil oil = parseOilDataLine(line);
                if (oil!=null) {
                    oils.add(oil);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return oils;
    }

    public static void saveOilFile(String path, List<String> cells) throws IOException{
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

    public static List<TreeSet<String>> loadSynonymsFile(String path) throws IOException {
        ArrayList<TreeSet<String>> list = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line;
        while ((line = reader.readLine()) != null){
            String[] synonyms = line.split(",");
            TreeSet<String> set = new TreeSet<>();
            for (String s : synonyms){
                set.add(s.replace(",", "").trim());
            }
            list.add(set);
        }
        return list;
    }

    public static void saveSynonymsFile(String path, List<TreeSet<String>> synonyms)
            throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        for (TreeSet<String> set : synonyms){
            for (String s : set){
                writer.write(s+",");
            }
            writer.newLine();
        }
        writer.close();
    }

    private static EssentialOil parseOilDataLine(String line) {
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

