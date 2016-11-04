package essentailOils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author mccomackjp
 * @version 1.0
 * @created 16-Sep-2016 6:20:28 PM
 */
public class EssentialOilsTracker {

	private List<EssentialOil> oils;

	private UserInterface ui;

    private String filePath;

    private String oilOutput;

    private int numColumns;

	public EssentialOilsTracker(){
        numColumns = 5;
        oils = new ArrayList<>();
        filePath = "data/EssentialOilsData.csv";
		loadOilData();
		initGUI();
	}

	public static void main(String[] args){
		EssentialOilsTracker eot = new EssentialOilsTracker();
        eot.filterOilOutput("", "");
	}

    public void addOil(String oilData){
        oils.add(OilFileHandler.parseOilDataLine(oilData));
        updateOilOutPut(oils);
    }

    public void addOil(EssentialOil oil){
        oils.add(oil);
        updateOilOutPut(oils);
    }

    public void errorMessage(String message){
        ui.errorMessage(message);
    }

    public void filterOilOutput(String filter, String negFilter){
        boolean hasFilter = true;
        List<EssentialOil> filteredOils = new ArrayList<>();
        if (filter == null){
            filter = "";
        }
        if (negFilter == null){
            negFilter = "";
        }
        if (filter.equals("") && negFilter.equals("")){
            hasFilter = false;
        }
        for (EssentialOil oil : oils) {
            if (hasFilter) {
                if (oil.equals(filter)) {
                    filteredOils.add(oil);
                } else if (filter.equals("") && !negFilter.equals("") && !oil.containsClash(negFilter)) {
                    filteredOils.add(oil);
                } else if (!filter.equals("") && negFilter.equals("") && oil.containsAttribute(filter)) {
                    filteredOils.add(oil);
                } else if (oil.containsAttribute(filter) && !oil.containsClash(negFilter)) {
                    filteredOils.add(oil);
                }
            } else {
                filteredOils.add(oil);
            }
        }
        String[][] outPut = updateOilOutPut(filteredOils);
        ui.updateOutput(outPut);
    }

    private void initGUI() {
        ui = new UserInterface(this);
    }

    private void loadOilData() {
        oils = OilFileHandler.loadFile(filePath);
    }

    public List<EssentialOil> getOils() {
        return oils;
    }

    private boolean oilDoesNotClash(EssentialOil oil, List<EssentialOil> oils){

        return false;
    }

    private String[][] updateOilOutPut(List<EssentialOil> oils){
        String[][] data = new String[oils.size()][numColumns];
        for (int i=0; i<oils.size(); i++){
            data[i][0] = oils.get(i).getName();
            data[i][1] = oils.get(i).getAttributes().toString();
            data[i][2] = oils.get(i).getClashes().toString();
            data[i][3] = "$" + String.valueOf(oils.get(i).getPricePerOunce());
            Collection<String> concentrations = oils.get(i).getConcentrations();
            if (concentrations.size() > 0){
                data[i][4] = oils.get(i).getConcentrations().toString();
            } else {
                data[i][4] = "pure";
            }

        }
        return data;
    }

    public void saveFile(String path, String[][] data){
        List cells = new ArrayList<String>();
        String[] headers = {"Oil Name,","Attributes,", "Clashes,", "Price per Ounce,",
                "Concentrations,", ","} ;
        cells.addAll(Arrays.asList(headers));
        for (int i=0; i<data.length; i++){
            for (int j=0; j<data[i].length; j++){
                data[i][j] = data[i][j].replace(",", ";");
                data[i][j] = data[i][j].replace("[", "");
                data[i][j] = data[i][j].replace("]", "");
                data[i][j] += ",";
            }
            cells.addAll(Arrays.asList(data[i]));
            cells.add(",");
        }
        try {
            OilFileHandler.saveFile(path, cells);
        }catch (IOException e) {
                e.printStackTrace();
            }
    }

    private static class OilFileHandler {

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

        private static EssentialOil parseOilDataLine(String line){
            EssentialOil oil = null;
            if (line.length()>5) {
                String[] oilData = line.split(",");
                String name = oilData[0].trim();
                String[] attributes = oilData[1].split(";");
                String[] clashes = oilData[2].split(";");
                double price = 0.0;
                if (oilData.length > 3 && oilData[3].length() > 3){
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
                        oil.addConcentrations(s);
                    }
                }
            }
            return oil;
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

}