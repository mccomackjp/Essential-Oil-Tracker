package userInterface;

import essentailOils.EssentialOilsTracker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;


public class editSynonymsUI extends JFrame {

    private EssentialOilsTracker controller;

    private List<TreeSet<String>> synonyms;

    private List<JTextArea> inputs;

    private JPanel panel;


    public editSynonymsUI(EssentialOilsTracker controller, List<TreeSet<String>> synonyms){
        this.controller = controller;
        this.synonyms = synonyms;
        inputs = new ArrayList<>();
        initGUI();
    }

    private void initGUI() {
        setSize(new Dimension(900, 600));
        setTitle("Edit Synonyms");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        JButton addListButton = new JButton("Add Synonym List");
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        panel = new JPanel();

        addListButton.addActionListener(e-> addList());
        okButton.addActionListener(e-> updateSynonyms());
        cancelButton.addActionListener(e-> closeWindow());

        topPanel.add(addListButton);
        bottomPanel.add(okButton);
        bottomPanel.add(cancelButton);
        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
        add(new JScrollPane(panel), BorderLayout.CENTER);

        updateInputs();
        setVisible(true);
    }

    private void updateSynonyms() {
        for (int i=0; i<synonyms.size(); i++){
            Scanner scanner = new Scanner(inputs.get(i).getText());
            while (scanner.hasNextLine()){
                String line = scanner.nextLine().trim();
                if (line.length() > 0 && !synonyms.get(i).contains(line)){
                    synonyms.get(i).add(line);
                }
            }
        }
        appendSynonyms();
        controller.setSynonyms(synonyms);
        closeWindow();
    }

    private void appendSynonyms() {
        if (synonyms.size() < inputs.size()){
            for (int i=synonyms.size(); i<inputs.size(); i++){
                TreeSet<String> set = getInputAsTreeSet(inputs.get(i).getText());
                synonyms.add(set);
            }
        }
    }

    private TreeSet<String> getInputAsTreeSet(String text) {
        TreeSet<String> set = new TreeSet<>();
        Scanner scanner = new Scanner(text);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine().trim();
            if (line.length() > 0){
                set.add(line);
            }
        }
        return set;
    }


    private void closeWindow() {
        dispose();
    }

    private void updateInputs() {
        for (JTextArea input : inputs){
            JScrollPane sp = new JScrollPane(input);
            panel.add(sp);
        }
        revalidate();
    }

    private void addList() {
        JTextArea area = new JTextArea();
        area.setPreferredSize(new Dimension(200, 470));
        inputs.add(area);
        updateInputs();
    }
}
