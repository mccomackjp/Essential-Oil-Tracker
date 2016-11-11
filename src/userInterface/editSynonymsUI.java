package userInterface;

import essentailOils.EssentialOilsTracker;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by mccomackjp on 11/10/2016.
 */
public class editSynonymsUI extends JFrame {

    private EssentialOilsTracker controller;

    private List<TreeSet<String>> synonyms;


    public editSynonymsUI(EssentialOilsTracker controller, List<TreeSet<String>> synonyms){
        this.controller = controller;
        this.synonyms = synonyms;
        initGUI();
    }

    private void initGUI() {
        setSize(900, 600);
        setTitle("Edit Synonyms");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        setLayout(layout);

        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        JButton addListButton = new JButton("Add Synonym List");
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");



        setVisible(true);
    }
}
