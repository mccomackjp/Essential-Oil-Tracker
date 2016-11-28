package essentailOils;

import fileIO.OilFileHandler;
import userInterface.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

//TODO fix save and exit to save all oils, not just displayed oils


public class EssentialOilsTracker {

	private List<EssentialOil> oils;

	private UserInterface ui;

    private int numColumns;

    private List<TreeSet<String>> synonyms;

    private Logger logger;


    public EssentialOilsTracker(){
        try {
            synonyms = OilFileHandler.loadSynonymsFile("data/synonymsData.csv");
            logger = Logger.getLogger("Error Log");
            FileHandler fileHandler = new FileHandler("data/errorLog.txt");
            fileHandler.setFormatter(new SimpleFormatter());
        } catch (IOException e){
            synonyms = new ArrayList<>();
        } finally{
            numColumns = 5;
            oils = new ArrayList<>();
            loadOilData("data/oils.csv");
            initGUI();
        }
	}

	public static void main(String[] args){
		EssentialOilsTracker eot = new EssentialOilsTracker();
        eot.filterOilOutput("", "");
	}

	public void logError(String message, Exception e){
	    logger.warning(message + ": "  + e.getMessage());
    }

    public void loadNewOilFile(String filePath){
        loadOilData(filePath);
    }

    public void errorMessage(String message){
        ui.errorMessage(message);
    }

    public List<EssentialOil> getOils() {
        return oils;
    }

    public void filterOilOutput(String filter, String negFilter){
        boolean hasFilter = true;
        List<EssentialOil> filteredOils = new ArrayList<>();
        TreeSet<String> filterSet = getSynonymSet(filter);
        TreeSet<String> negFilterSet = getSynonymSet(negFilter);
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
                } else if (filter.equals("") && !negFilter.equals("")
                        && !oil.containsClash(negFilter, negFilterSet)) {
                    filteredOils.add(oil);
                } else if (!filter.equals("") && negFilter.equals("")
                        && oil.containsAttribute(filter, filterSet)) {
                    filteredOils.add(oil);
                } else if (oil.containsAttribute(filter) && !oil.containsClash(negFilter)) {
                    filteredOils.add(oil);
                }
            } else {
                filteredOils.add(oil);
            }
        }
        ui.updateOutput(buildOilTable(filteredOils));
    }

    private TreeSet<String> getSynonymSet(String filter) {
        TreeSet<String> set = null;
        for (TreeSet<String> treeSet : synonyms){
            if (treeSet.contains(filter)){
                set = treeSet;
                break;
            }
        }
        return set;
    }

    private void initGUI() {
        ui = new UserInterface(this);
    }

    private void loadOilData(String filePath) {
        oils = OilFileHandler.loadOilFile(filePath);
    }

    private String[][] buildOilTable(List<EssentialOil> oils){
        DecimalFormat df = new DecimalFormat("0.00");
        String[][] data = new String[oils.size()][numColumns];
        for (int i=0; i<oils.size(); i++){
            data[i][0] = oils.get(i).getName();
            data[i][1] = oils.get(i).getAttributes().toString();
            data[i][2] = oils.get(i).getClashes().toString();
            data[i][3] = df.format(oils.get(i).getPricePerOunce());
            if (oils.get(i).getConcentrations().size() > 0){
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
            OilFileHandler.saveOilFile(path, cells);
        }catch (IOException e) {
                errorMessage("Failed to save oil file");
                logError("Failed to save oil file", e);
            }
    }

    public void setSynonyms(List<TreeSet<String>> list){
        synonyms = list;
    }

    public void addNewOil(String oilName, List<String> concentrations, int totalDrops) {
        EssentialOil oil = new EssentialOil(oilName);
        oil.addConcentrations(concentrations);
        addAttributesFromConcentrations(oil, concentrations);
        addClashesFromConcentrations(oil, concentrations);
        addPricePerOunce(oil, concentrations, totalDrops);
        oils.add(oil);
        ui.updateOutput(buildOilTable(oils));
    }

    public void editSynonyms() {
        new editSynonymsUI(this, synonyms);
    }

    private void addPricePerOunce(EssentialOil oil, List<String> concentrations, int totalDrops) {
        double totalPrice = 0.0;
        for (String con : concentrations){
            EssentialOil temp = new EssentialOil(con.substring(con.indexOf("drops ") + 6,
                    con.length()));
            temp = oils.get(oils.indexOf(temp));
            double price = temp.getPricePerOunce();
            if (price <= 0){
                ui.errorMessage("Price per ounce for " + temp.getName() + " is 0");
            } else {
                totalPrice += price * Double.valueOf(con.substring(0, con.indexOf(" ")));
            }
        }
        oil.setPricePerOunce(totalPrice/totalDrops);
    }

    private void addAttributesFromConcentrations(EssentialOil oil, List<String> concentrations){
        for (int i = 0; i < concentrations.size(); i++){
            String name = concentrations.get(i);
            name = name.substring(name.indexOf("drops") + 5, name.length()).trim();
            oils.get(oils.indexOf(new EssentialOil(name))).getAttributes().forEach(attribute -> {
               if (!oil.containsAttribute(attribute)){
                   oil.addAttribute(attribute);
               }
            });
        }
    }

    private void addClashesFromConcentrations(EssentialOil oil, List<String> concentrations){
        for (int i = 0; i < concentrations.size(); i++){
            String name = concentrations.get(i);
            name = name.substring(name.indexOf("drops") + 5, name.length()).trim();
            oils.get(oils.indexOf(new EssentialOil(name))).getClashes().forEach(clash -> {
                if (!oil.containsClash(clash)) {
                    oil.addClash(clash);
                }
            });
        }
    }

    public void exit() {
        System.exit(0);
    }

    public void saveSynonyms() {
        try {
            OilFileHandler.saveSynonymsFile("data/synonymsData.csv", synonyms);
        } catch (IOException e){
            errorMessage(e.getMessage());
            logError("Failed to save synonym file", e);
        }
    }
}