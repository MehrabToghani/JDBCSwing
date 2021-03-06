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

public class TableChoicePanel extends JPanel {

    private MysqlFrame frame;
    private JButton back = new JButton("<Back");
    private JLabel selectTable = new JLabel("Select Table");
    private Table tablePane;
    private JLabel rowCount = new JLabel();
    private JButton select = new JButton("Select");
    private JLabel orLabel = new JLabel("Or");
    private JButton customQuery = new JButton("Custom Query");

    public TableChoicePanel(MysqlFrame frame) {
        this.frame = frame;
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

        selectTable.setForeground(Color.WHITE);
        selectTable.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        selectTable.setHorizontalAlignment(0);
        this.add(selectTable);

        try {
            ResultSet rs = frame.getMysql().setQuery("show tables;"); //select * from university.instructor;
            try {
                tablePane = new Table(rs);
                this.add(tablePane);

                rowCount.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
                rowCount.setForeground(Color.WHITE);
                rowCount.setText(tablePane.getRowCount() > 0 ? (tablePane.getRowCount() > 1 ? tablePane.getRowCount() + " rows" : "One row") : "Empty");
                this.add(rowCount);

                select.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
                select.setBackground(new Color(239, 239, 239));
                select.setForeground(new Color(145, 145, 145));
                this.add(select);

                orLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
                orLabel.setForeground(Color.WHITE);
                this.add(orLabel);

                customQuery.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
                customQuery.setMargin(new Insets(1, 1, 1, 1));
                customQuery.setBackground(new Color(239, 239, 239));
                customQuery.setForeground(Color.BLACK);
                customQuery.setCursor(new Cursor(Cursor.HAND_CURSOR));
                this.add(customQuery);

                back.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            frame.setDatabaseChoice();
                        }
                    }
                });

                tablePane.getTable().getSelectionModel().addListSelectionListener(listSelectionEvent -> {
                    if (tablePane.getTable().getSelectedRow() == -1) {
                        select.setForeground(new Color(145, 145, 145));
                        select.setCursor(null);
                    } else {
                        select.setForeground(Color.BLACK);
                        select.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    }
                });

                tablePane.getTable().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                            doSelect();
                        }
                    }
                });

                select.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        doSelect();
                    }
                });

                customQuery.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            frame.setCustomQuery("table");
                        }
                    }
                });

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
    }

    private void doSelect () {
        if (tablePane.getTable().getSelectedRow() == -1) {
            return;
        }
        frame.setManagementPanel((String) tablePane.getTable().getModel().getValueAt(tablePane.getTable().getSelectedRow(), 0));
    }

    private void adjustComponentsSize() {
        setLocation(42, 30);
        setSize(frame.getWidth() - 100, frame.getHeight() - 60);

        back.setBounds(0, 0, 50, 25);

        selectTable.setSize(getWidth(), 30);
        selectTable.setLocation((getWidth() - selectTable.getWidth()) / 2, 0);

        tablePane.setVisible(false);
        tablePane.setSize(getWidth(), getHeight() - 140);
        tablePane.setLocation(0, 40);
        tablePane.adjustmentTableSize(tablePane.getWidth());
        tablePane.setVisible(true);

        rowCount.setSize(getWidth() , 20);
        rowCount.setLocation(0 , getHeight() - 100);

        select.setSize(80, 25);
        select.setLocation(getWidth() / 2 - 125, getHeight() - 70);

        orLabel.setSize(20, 20);
        orLabel.setLocation(getWidth() / 2 - 20, getHeight() - 70);

        customQuery.setSize(100, 25);
        customQuery.setLocation(getWidth() / 2 + 25, getHeight() - 70);
    }
}
