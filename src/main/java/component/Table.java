package component;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Table extends JScrollPane {

    private JTable table;
    private String[] columnNames;
    private String[][] data;

    public Table(ResultSet set) throws SQLException {
        initData(set);
        initComponent();
    }

    public JTable getTable() {
        return table;
    }

    public String[] getColumns() {
        return columnNames;
    }

    private void initData(ResultSet rs) throws SQLException {
        ResultSetMetaData rsm = rs.getMetaData();
        int columnCount = rsm.getColumnCount();

        columnNames = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = rsm.getColumnName(i + 1);
        }

        int rowCount = 0;
        while (rs.next()) {
            rowCount++;
        }

        rs.beforeFirst();
        data = new String[rowCount][columnCount];
        int index = 0;
        while (rs.next()) {
            for (int i = 0; i < columnCount; i++) {
                data[index][i] = rs.getString(i + 1);
            }
            index++;
        }
    }

    private void initComponent() {
        table = new JTable(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        table.getTableHeader().setForeground(Color.white);
        table.getTableHeader().setBackground(new Color(127, 140, 141));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columnNames.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        this.setViewportView(table);
    }

    public int getRowCount() {
        if (data == null) {
            return -1;
        }
        return data.length;
    }

    public void adjustmentTableSize(int width) {
        setVisible(false);
        if (width < table.getColumnCount() * 100) {
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setMinWidth(100);
            }
        } else {
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        }
        setVisible(true);
    }
}
