package essentailOils;

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
        //TODO implement
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

    public void addNewOil(String oilName, List<String> concentrations) {
        EssentialOil oil = new EssentialOil(oilName);
        oil.addConcentrations(concentrations);
        addAttributesFromConcentrations(oil, concentrations);
        addClashesFromConcentrations(oil, concentrations);
        oils.add(oil);
        updateOilOutPut(oils);
    }

    //TODO Troubleshoot ArrayIndexOutOfBoundsException: -1
    private void addAttributesFromConcentrations(EssentialOil oil, List<String> concentrations){
        for (int i = 0; i < concentrations.size(); i++){
            String name = concentrations.get(i);
            name = name.substring(name.indexOf("drops") + 5, name.length()).trim().toLowerCase();
            System.out.println(oils.get(oils.indexOf(name))); //troubleshootings
            oils.get(oils.indexOf(name)).getAttributes().forEach(attribute -> {
               if (!oil.containsAttribute(attribute)){
                   oil.addAttribute(attribute);
               }
            });
        }
    }

    private void addClashesFromConcentrations(EssentialOil oil, List<String> concentrations){
        String[] names = new String[concentrations.size()];
        for (int i = 0; i < names.length; i++){
            names[i] = concentrations.get(i);
            names[i] = names[i].substring(names[i].indexOf("drops") + 6, names[i].length());
            oils.get(oils.indexOf(names[i])).getClashes().forEach(clash -> {
                if (!oil.containsClash(clash)) {
                    oil.addClash(clash);
                }
            });
        }
    }



}