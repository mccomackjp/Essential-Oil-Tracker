package userInterface;

import essentailOils.EssentialOil;
import essentailOils.EssentialOilsTracker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class AddOilInterface extends JFrame {

    private JTextField oilNameInput;

    private JLabel output;

    private List<EssentialOil> oils;

    private ArrayList<String> concentrations;

    private int totalDrops;

    private EssentialOilsTracker controller;

    public AddOilInterface(EssentialOilsTracker controller, List<EssentialOil> oils){
        this.controller = controller;
        this.oils = oils;
        initGUI();

    }

    private void addConcentration(List<String> concentrations) {
        JTextField percentInput = new JTextField("5");
        Object[] selection = new Object[oils.size()];
        for(int i=0; i<selection.length; i++){
            selection[i] = oils.get(i).getName();
        }
        Object result = JOptionPane.showInputDialog(null, new Object[]{"Drop ratio of " +
                "Concentration: ", percentInput}, "Add mixture and concentration",
                JOptionPane.PLAIN_MESSAGE, null, selection, selection[0]);
        totalDrops += Integer.valueOf(percentInput.getText());
        concentrations.add(percentInput.getText() + " drops " + result.toString());
        updateOutput();
    }

    private void removeMix() {
        if (totalDrops > 0) {
            Object[] selection = concentrations.toArray();
            String result = (String) JOptionPane.showInputDialog(null, "Remove oil from mixture",
                    "Remove oil", JOptionPane.PLAIN_MESSAGE, null, selection, selection[0]);
            concentrations.remove(result);
            totalDrops -= getPercent(result);
            updateOutput();
        }
    }

    private int getPercent(String value) {
        int percent = 0;
        if (value != null && value.length() > 1){
            percent = Integer.valueOf(value.substring(0, value.indexOf(" ")));
        }
        return percent;
    }

    private void updateOutput(){
        String text = "<html><body>Total drops: " + String.valueOf(totalDrops) +
                "<br>";
        for (String s : concentrations){
            text += s + "<br>";
        }
        text += "</body></html>";
        output.setText(text);
    }

    private void closeWindow() {
        this.dispose();
    }

    private void updateOils() {
        if (oilNameInput.getText().equals("oil name")){
            controller.errorMessage("Please enter an oil name");
        } else if (totalDrops == 0){
            controller.errorMessage("Please add an oil to the mix");
        } else {
            addNewOil();
        }
    }

    private void addNewOil() {
        controller.addNewOil(oilNameInput.getText(), concentrations, totalDrops);
        closeWindow();
    }

    private void initGUI(){
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add oil");
        setSize(600, 400);
        BorderLayout layout = new BorderLayout();
        setLayout(layout);

        totalDrops = 0;
        JLabel nameLabel = new JLabel("Set Name: ");
        oilNameInput = new JTextField("oil name");
        JButton addMixButton = new JButton("Add oil to mix");
        JButton removeMixButton = new JButton("Removed oil from mix");
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        output = new JLabel("Total Concentration : " + String.valueOf(totalDrops)+  "\n");
        concentrations = new ArrayList<>();
        JPanel topPanel = new JPanel();
        JPanel botPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(output);

        addMixButton.addActionListener(e-> addConcentration(concentrations));
        removeMixButton.addActionListener(e-> removeMix());
        okButton.addActionListener(e-> updateOils());
        cancelButton.addActionListener(e-> closeWindow());

        topPanel.add(addMixButton);
        topPanel.add(removeMixButton);
        topPanel.add(nameLabel);
        topPanel.add(oilNameInput);
        botPanel.add(okButton);
        botPanel.add(cancelButton);
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(botPanel, BorderLayout.SOUTH);

        updateOutput();
        setVisible(true);
    }
}
