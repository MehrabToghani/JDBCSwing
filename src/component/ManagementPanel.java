package component;

import source.MysqlFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagementPanel extends JPanel {

    private MysqlFrame frame;
    private String tableName;
    private JButton back = new JButton("<Back");
    private JButton insert = new JButton("Insert");
    private JButton edit = new JButton("Edit");
    private JButton delete = new JButton("Delete");
    private JButton searchButton = new JButton("Search");
    private JTextField searchField = new JTextField();
    private JLabel searchLabel = new JLabel("All Records.");
    private Table tablePane;
    private JLabel rowCount = new JLabel();

    public ManagementPanel(MysqlFrame frame, String tableName) {
        this.frame = frame;
        this.tableName = tableName;
        this.setLayout(null);
        this.setOpaque(false);
        initComponents();
        adjustComponentsSize();
    }

    private void initComponents() {
        back.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        back.setMargin(new Insets(1, 1, 1, 1));
        back.setBackground(new Color(239, 239, 239));
        back.setForeground(Color.BLACK);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.add(back);

        insert.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        insert.setBackground(new Color(239, 239, 239));
        insert.setForeground(Color.BLACK);
        insert.setMargin(new Insets(1, 1, 1, 1));
        insert.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.add(insert);

        edit.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        edit.setBackground(new Color(239, 239, 239));
        edit.setMargin(new Insets(1, 1, 1, 1));
        this.add(edit);

        delete.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        delete.setBackground(new Color(239, 239, 239));
        delete.setMargin(new Insets(1, 1, 1, 1));
        this.add(delete);

        searchField.setMargin(new Insets(1, 5, 1, 5));
        this.add(searchField);

        searchButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        searchButton.setBackground(new Color(239, 239, 239));
        searchButton.setForeground(Color.BLACK);
        searchButton.setMargin(new Insets(1, 1, 1, 1));
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.add(searchButton);

        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        searchLabel.setHorizontalAlignment(0);
        this.add(searchLabel);

        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    frame.setTableChoice();
                }
            }
        });

        try {
            ResultSet rs = frame.getMysql().setQuery("select * from " + tableName + ";");
            try {
                tablePane = new Table(rs);
                this.add(tablePane);

                rowCount.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
                rowCount.setForeground(Color.WHITE);
                rowCount.setText(tablePane.getRowCount() > 0 ? (tablePane.getRowCount() > 1 ? tablePane.getRowCount() + " rows" : "One row") : "Empty");
                this.add(rowCount);

                setSelectedState();

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                adjustComponentsSize();
            }
        });

        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    doSearch();
                }
            }
        });

        insert.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    new InsertFrame(frame, tableName, ManagementPanel.this);
                }
            }
        });

        edit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    String[] values = new String[tablePane.getColumns().length];
                    for (int i = 0; i < values.length; i++) {
                        values[i] = (String) tablePane.getTable().getModel().getValueAt(tablePane.getTable().getSelectedRow(), i);
                    }
                    new UpdateFrame(frame, tableName, ManagementPanel.this , values);
                }
            }
        });

        delete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (tablePane.getTable().getSelectedRow() == -1) {
                        return;
                    }
                    int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?", "Alert", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (answer == JOptionPane.YES_OPTION) {
                        try {
                            String str = "";
                            for (int i = 0; i < tablePane.getColumns().length; i++) {
                                Object value = tablePane.getTable().getModel().getValueAt(tablePane.getTable().getSelectedRow(), i);
                                if (value != null) {
                                    str += tablePane.getColumns()[i].trim() + " = '" + value + "'";
                                } else {
                                    str += tablePane.getColumns()[i].trim() + " is null";
                                }
                                str += (i == tablePane.getColumns().length - 1) ? " " : " and ";
                            }
                            frame.getMysql().updateQuery("delete from " + tableName + " where " + str);
                            doSearch();
                        } catch (SQLException ex) {
                            JOptionPane.showConfirmDialog(null, ex.getMessage(), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }

    private void setSelectedState() {
        edit.setForeground(new Color(145, 145, 145));
        edit.setCursor(null);
        delete.setForeground(new Color(145, 145, 145));
        delete.setCursor(null);

        tablePane.getTable().getSelectionModel().addListSelectionListener(listSelectionEvent -> {
            if (tablePane.getTable().getSelectedRow() == -1) {
                edit.setForeground(new Color(145, 145, 145));
                edit.setCursor(null);
                delete.setForeground(new Color(145, 145, 145));
                delete.setCursor(null);
            } else {
                edit.setForeground(Color.BLACK);
                edit.setCursor(new Cursor(Cursor.HAND_CURSOR));
                delete.setForeground(Color.BLACK);
                delete.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
    }

    public void doSearch() {
        try {
            searchField.setText(searchField.getText().trim());
            setVisible(false);
            remove(tablePane);
            setVisible(true);
            String query = "select * from " + tableName + " where ";
            for (int i = 0; i < tablePane.getColumns().length; i++) {
                query += tablePane.getColumns()[i] + " like '%" + searchField.getText() + "%'";
                query += (i == tablePane.getColumns().length - 1) ? " " : " or ";
            }
            ResultSet rs = frame.getMysql().setQuery(query);
            tablePane = new Table(rs);
            add(tablePane);
            setSelectedState();
            rowCount.setText(tablePane.getRowCount() > 0 ? (tablePane.getRowCount() > 1 ? tablePane.getRowCount() + " rows" : "One row") : "Empty");
            searchLabel.setText(searchField.getText().equals("") ? "All Records." : "Results for " + searchField.getText());
            adjustComponentsSize();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void adjustComponentsSize() {
        setLocation(42, 30);
        setSize(frame.getWidth() - 100, frame.getHeight() - 60);

        back.setBounds(0, 0, 50, 25);

        insert.setSize(80, 25);
        insert.setLocation((getWidth() - insert.getWidth() * 3 - 20) / 2, 40);

        edit.setSize(80, 25);
        edit.setLocation((getWidth() - insert.getWidth()) / 2, 40);

        delete.setSize(80, 25);
        delete.setLocation((getWidth() + insert.getWidth() + 20) / 2, 40);

        searchField.setSize(150, 25);
        searchField.setLocation((getWidth() - 250) / 2, 75);

        searchButton.setSize(80, 25);
        searchButton.setLocation(searchField.getLocation().x + searchField.getWidth() + 20, 75);

        searchLabel.setSize(getWidth(), 30);
        searchLabel.setLocation(0, 100);

        if (tablePane != null) {
            tablePane.setVisible(false);
            tablePane.setSize(getWidth(), getHeight() - 180);
            tablePane.setLocation(0, 130);
            tablePane.adjustmentTableSize(tablePane.getWidth());
            tablePane.setVisible(true);

            rowCount.setSize(getWidth(), 20);
            rowCount.setLocation(0, getHeight() - 50);
        }
    }
}
