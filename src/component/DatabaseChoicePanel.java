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

public class DatabaseChoicePanel extends JPanel {

    private MysqlFrame frame;
    private JLabel welcomeLabel = new JLabel("Welcome");
    private JLabel selectDatabase = new JLabel("Select Database");
    private Table tablePane;
    private JLabel rowCount = new JLabel();
    private JButton select = new JButton("Select");
    private JLabel orLabel = new JLabel("Or");
    private JButton customQuery = new JButton("Custom Query");

    public DatabaseChoicePanel(MysqlFrame frame) {
        this.frame = frame;
        this.setLayout(null);
        this.setOpaque(false);
        initComponents();
        adjustComponentsSize();
    }

    private void initComponents() {
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
        welcomeLabel.setHorizontalAlignment(0);
        this.add(welcomeLabel);

        selectDatabase.setForeground(Color.WHITE);
        selectDatabase.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        selectDatabase.setHorizontalAlignment(0);
        this.add(selectDatabase);

        try {
            ResultSet rs = frame.getMysql().setQuery("show databases;");
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
                        if (e.getButton() == 1) {
                            doSelect();
                        }
                    }
                });

                customQuery.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            frame.setCustomQuery();
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

    private void doSelect() {
        if (tablePane.getTable().getSelectedRow() == -1) {
            return;
        }
        try {
            frame.getMysql().setQuery("use " + tablePane.getTable().getModel().getValueAt(tablePane.getTable().getSelectedRow(), 0));
            frame.setTableChoice();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void adjustComponentsSize() {
        setLocation(42, 30);
        setSize(frame.getWidth() - 100, frame.getHeight() - 60);

        welcomeLabel.setSize(100, 30);
        welcomeLabel.setLocation((getWidth() - welcomeLabel.getWidth()) / 2, 0);

        selectDatabase.setSize(140, 30);
        selectDatabase.setLocation((getWidth() - selectDatabase.getWidth()) / 2, 40);

        tablePane.setVisible(false);
        tablePane.setSize(getWidth(), getHeight() - 180);
        tablePane.setLocation(0, 80);
        tablePane.adjustmentTableSize(tablePane.getWidth());
        tablePane.setVisible(true);

        rowCount.setSize(getWidth(), 20);
        rowCount.setLocation(0, getHeight() - 100);

        select.setSize(80, 25);
        select.setLocation(getWidth() / 2 - 125, getHeight() - 70);

        orLabel.setSize(20, 20);
        orLabel.setLocation(getWidth() / 2 - 20, getHeight() - 70);

        customQuery.setSize(100, 25);
        customQuery.setLocation(getWidth() / 2 + 25, getHeight() - 70);
    }
}
