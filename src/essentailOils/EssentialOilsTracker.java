package essentailOils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
        numColumns = 3;
        oils = new ArrayList<>();
        filePath = "data/EssentialOilsData.csv";
		loadOilData();
		initGUI();
	}

	private void initGUI() {
		ui = new UserInterface(this);
	}

	private void loadOilData() {
        oils = OilFileHandler.loadFile(filePath);
	}

	public static void main(String[] args){
		EssentialOilsTracker eot = new EssentialOilsTracker();
        eot.filterOilOutput("", "");
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

    private boolean oilDoesNotClash(EssentialOil oil, List<EssentialOil> oils){

        return false;
    }

    private String[][] updateOilOutPut(List<EssentialOil> oils){
        String[][] data = new String[oils.size()][numColumns];
        for (int i=0; i<oils.size(); i++){
            data[i][0] = oils.get(i).getName();
            data[i][1] = oils.get(i).getAttributes().toString();
            data[i][2] = oils.get(i).getClashes().toString();
        }
        return data;
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
            if (line.length()>3) {
                String[] oilData = line.split(",");
                String name = oilData[0].trim();
                String[] attributes = oilData[1].split(";");
                String[] clashes = oilData[2].split(";");
                oil = new EssentialOil(name);
                for (String s : attributes) {
                    oil.addAttribute(s.trim());
                }
                for (String s : clashes) {
                    oil.addClash(s.trim());
                }
            }
            return oil;
        }

    }

}