package essentailOils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mccomackjp on 10/25/2016.
 */
public class AddOilInterface extends JFrame {

    private JTextField oilNameInput;

    private JLabel output;

    private List<EssentialOil> oils;

    ArrayList<String> concentrations;

    private int totalConcentration;

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
        totalConcentration += Integer.valueOf(percentInput.getText());
        concentrations.add(percentInput.getText() + " drops " + result.toString());
        updateOutput();
    }

    private void removeMix() {
        Object[] selection = concentrations.toArray();
        String result = (String) JOptionPane.showInputDialog(null, "Remove oil from mixture",
                "Remove oil", JOptionPane.PLAIN_MESSAGE, null, selection, selection[0]);
        concentrations.remove(result);
        totalConcentration -= getPercent(result);
        updateOutput();
    }

    private int getPercent(String value) {
        int percent = 0;
        if (value != null && value.length() > 1){
            percent = Integer.valueOf(value.substring(0, value.indexOf(" ")));
        }
        return percent;
    }

    private void updateOutput(){
        String text = "<html><body>Total drops: " + String.valueOf(totalConcentration) +
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
        } else if (totalConcentration == 0){
            controller.errorMessage("Please add an oil to the mix");
        } else {
            addNewOil();
        }
    }

    private void addNewOil() {
        EssentialOil oil = new EssentialOil(oilNameInput.getText());
        for (Object s : concentrations.toArray()){
            System.out.println(s);
        }
    }

    private void initGUI(){
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add oil");
        setSize(600, 400);
        BorderLayout layout = new BorderLayout();
        setLayout(layout);

        totalConcentration = 0;
        JLabel nameLabel = new JLabel("Set Name: ");
        oilNameInput = new JTextField("oil name");
        JButton addMixButton = new JButton("Add oil to mix");
        JButton removeMixButton = new JButton("Removed oil from mix");
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        output = new JLabel("Total Concentration : " + String.valueOf(totalConcentration)+  "\n");
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
