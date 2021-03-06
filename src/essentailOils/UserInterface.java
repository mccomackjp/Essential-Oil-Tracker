package essentailOils;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author mccomackjp
 * @version 1.0
 * @created 16-Sep-2016 6:20:28 PM
 */
public class UserInterface extends JFrame{

    private JTable oilsListTable;

    private DefaultTableModel tableModel;

    private JTextField filterInput;

    private JTextField negativeFilterInput;

	private EssentialOilsTracker controller;

    private String[] oilsListHeader;

    private String[][] oilTable;

	public UserInterface(EssentialOilsTracker controller){
		this.controller = controller;
		initGUI();
	}

	private void initGUI() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        setLayout(layout);
        setTitle("Essential Oil Tracker");
        setSize(1100, 900);

        KeyEventHandler keyHandler = new KeyEventHandler();

        String[][] tableData = new String[0][0];
        oilsListHeader = new String[] {"Oil Name","Attributes", "Clashes", "Price per Ounce",
                "Concentrations"};
        tableModel = new DefaultTableModel(tableData, oilsListHeader);
        JPanel topPanel = new JPanel();
		JButton filterButton = new JButton("Filter Results");
        JButton addOilButton = new JButton("Add Oil");
        JButton saveListButton = new JButton("Save oils");
        JButton saveBackupButton = new JButton("Save backup file");
		filterInput = new JTextField(20);
        negativeFilterInput = new JTextField(20);
        oilsListTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(oilsListTable);
        scrollPane.setSize(this.getSize());
        filterButton.addActionListener(e-> filterOils());
        addOilButton.addActionListener(e-> addOil());
        saveListButton.addActionListener(e-> saveFile());
        saveBackupButton.addActionListener(e-> saveBackupFile());

        filterInput.addKeyListener(keyHandler);
        negativeFilterInput.addKeyListener(keyHandler);

        topPanel.add(filterButton);
        topPanel.add(new JLabel("Include:"));
        topPanel.add(filterInput);
        topPanel.add(new JLabel("Exclude clash:"));
        topPanel.add(negativeFilterInput);
        topPanel.add(addOilButton);
        topPanel.add(saveListButton);
        topPanel.add(saveBackupButton);
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
	}

    private void saveBackupFile() {
        controller.saveFile("data/oilsBackup.csv", oilTable);
    }

    private void saveFile() {
        controller.saveFile("data/oils.csv", oilTable);
    }

    public void errorMessage(String message){
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }


    public void updateOutput(String[][] oilOutput) {
        tableModel = new DefaultTableModel(oilOutput, oilsListHeader);
        oilsListTable.setModel(tableModel);
        oilsListTable.getColumnModel().getColumn(0).setCellRenderer(new CellRenderer());
        oilsListTable.getColumnModel().getColumn(1).setCellRenderer(new CellRenderer());
        oilsListTable.getColumnModel().getColumn(2).setCellRenderer(new CellRenderer());
        oilTable = oilOutput;
    }

    private void addOil(){
        new AddOilInterface(controller, controller.getOils());
    }

    private void filterOils() {
        controller.filterOilOutput(filterInput.getText().trim(),
                negativeFilterInput.getText().trim());
    }

    private class CellRenderer extends JTextArea implements TableCellRenderer{

        public JTextArea textArea;

        public CellRenderer(){
            textArea = new JTextArea();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            this.setText((String) value);
            this.setLineWrap(true);
            this.setWrapStyleWord(true);
            setSize(new Dimension(table.getColumnModel().getColumn(column).getWidth(), Short.MAX_VALUE));
            int rowHeight = table.getRowHeight(row);
            table.setRowHeight(row, Math.max(rowHeight, getPreferredSize().height));
            return this;
        }

    }

    private class KeyEventHandler implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER){
                filterOils();
            }
        }

    }

}