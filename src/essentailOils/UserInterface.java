package essentailOils;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.BadLocationException;
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

	public UserInterface(EssentialOilsTracker controller){
		this.controller = controller;
		initGUI();
	}

	private void initGUI() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        setLayout(layout);
        setTitle("Essential Oil Tracker");
        setSize(800, 900);

        KeyEventHandler keyHandler = new KeyEventHandler();

        String[][] tableData = new String[0][0];
        oilsListHeader = new String[] {"Oil Name","Attributes", "Clashes"};
        tableModel = new DefaultTableModel(tableData, oilsListHeader);
        JPanel topLayout = new JPanel();
		JButton filterButton = new JButton("Filter Results");
		filterInput = new JTextField(20);
        negativeFilterInput = new JTextField(20);
        oilsListTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(oilsListTable);
        scrollPane.setSize(this.getSize());
        filterButton.addActionListener(e-> filterOils());

        filterInput.addKeyListener(keyHandler);
        negativeFilterInput.addKeyListener(keyHandler);

        topLayout.add(filterButton);
        topLayout.add(new JLabel("Include:"));
        topLayout.add(filterInput);
        topLayout.add(new JLabel("Exclude clash:"));
        topLayout.add(negativeFilterInput);
        add(topLayout, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
	}


    public void updateOutput(String[][] oilOutput) {
        tableModel = new DefaultTableModel(oilOutput, oilsListHeader);
        oilsListTable.setModel(tableModel);
        oilsListTable.getColumnModel().getColumn(0).setCellRenderer(new CellRenderer());
        oilsListTable.getColumnModel().getColumn(1).setCellRenderer(new CellRenderer());
        oilsListTable.getColumnModel().getColumn(2).setCellRenderer(new CellRenderer());
    }

    private void filterOils() {
        controller.filterOilOutput(filterInput.getText().trim(),
                negativeFilterInput.getText().trim());
    }

    private class CellRenderer extends JTextArea implements TableCellRenderer{

        JTextArea textArea;

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