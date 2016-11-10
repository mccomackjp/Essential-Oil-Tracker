package essentailOils;

import userInterface.UserInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

//TODO add error logger
//TODO add synonym list

/**
 * @author mccomackjp
 * @version 1.0
 * @created 16-Sep-2016 6:20:28 PM
 */
public class EssentialOilsTracker {

	private List<EssentialOil> oils;

	private UserInterface ui;

    private String filePath;

    private int numColumns;

    private List<TreeSet<String>> synonyms;


    public EssentialOilsTracker(){
        numColumns = 5;
        oils = new ArrayList<>();
        filePath = "data/oils.csv";
        synonyms = new ArrayList<>();
		loadOilData();
		initGUI();
	}

	public static void main(String[] args){
		EssentialOilsTracker eot = new EssentialOilsTracker();
        eot.filterOilOutput("", "");
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
        ui.updateOutput(buildOilTable(filteredOils));
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
        //TODO implement
        return false;
    }

    private String[][] buildOilTable(List<EssentialOil> oils){
        String[][] data = new String[oils.size()][numColumns];
        for (int i=0; i<oils.size(); i++){
            data[i][0] = oils.get(i).getName();
            data[i][1] = oils.get(i).getAttributes().toString();
            data[i][2] = oils.get(i).getClashes().toString();
            data[i][3] = String.valueOf(oils.get(i).getPricePerOunce());
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
            OilFileHandler.saveFile(path, cells);
        }catch (IOException e) {
                e.printStackTrace();
            }
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



}