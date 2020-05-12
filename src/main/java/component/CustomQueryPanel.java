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

public class CustomQueryPanel extends JPanel {

    private MysqlFrame frame;
    private JButton back = new JButton("<Back");
    private JLabel queryLabel = new JLabel("Enter your query");
    private JTextArea queryField = new JTextArea();
    private JScrollPane queryPane = new JScrollPane(queryField);
    private JButton submit = new JButton("Submit");
    private Table tablePane;
    private JLabel rowCount = new JLabel();
    private JTextArea message = new JTextArea();
    private JScrollPane messagePane = new JScrollPane(message);

    public CustomQueryPanel(MysqlFrame frame) {
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

        queryLabel.setForeground(Color.WHITE);
        queryLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        queryLabel.setHorizontalAlignment(0);
        this.add(queryLabel);

        queryField.setMargin(new Insets(2, 5, 2, 5));
        queryField.setLineWrap(true);
        queryField.setWrapStyleWord(true);

        this.add(queryPane);

        submit.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        submit.setBackground(new Color(239, 239, 239));
        submit.setForeground(Color.BLACK);
        submit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.add(submit);

        messagePane.setBorder(null);
        message.setBackground(frame.getBackgroundColor());
        message.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        message.setForeground(new Color(255, 221, 64));
        message.setLineWrap(true);
        message.setEditable(false);
        message.setWrapStyleWord(true);
        message.setBorder(null);
        message.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        this.add(messagePane);

        rowCount.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        rowCount.setForeground(Color.WHITE);
        rowCount.setVisible(false);
        this.add(rowCount);

        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    frame.setDatabaseChoice();
                }
            }
        });

        submit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    messagePane.setVisible(false);
                    rowCount.setVisible(false);
                    if (tablePane != null) {
                        setVisible(false);
                        remove(tablePane);
                        setVisible(true);
                    }
                    try {
                        ResultSet rs = frame.getMysql().setQuery(queryField.getText());
                        tablePane = new Table(rs);
                        add(tablePane);
                        rowCount.setText(tablePane.getRowCount() > 0 ? (tablePane.getRowCount() > 1 ? tablePane.getRowCount() + " rows" : "One row") : "Empty");
                        rowCount.setVisible(true);
                        adjustComponentsSize();
                    } catch (SQLException ex) {
                        if (ex.getMessage().trim().equalsIgnoreCase("Can not issue data manipulation statements with executeQuery().")) {
                            try {
                                int n = frame.getMysql().updateQuery(queryField.getText());
                                if (n == 0) {
                                    message.setText("Successful");
                                } else {
                                    message.setText("Successful\n" + n + (n > 1 ? " rows updated" : "row updated"));
                                }
                                messagePane.setVisible(true);
                            } catch (SQLException exc) {
                                message.setText(exc.getMessage());
                                message.setCaretPosition(0);
                                messagePane.setVisible(true);
                            }
                        } else {
                            message.setText(ex.getMessage());
                            message.setCaretPosition(0);
                            messagePane.setVisible(true);
                        }
                    } catch (NullPointerException ex) {
                        message.setText("Successful");
                        messagePane.setVisible(true);
                    }
                }
            }
        });

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                adjustComponentsSize();
            }
        });
    }

    private void adjustComponentsSize() {
        setLocation(42, 30);
        setSize(frame.getWidth() - 100, frame.getHeight() - 60);

        back.setBounds(0, 0, 50, 25);

        queryLabel.setSize(140, 30);
        queryLabel.setLocation((getWidth() - queryLabel.getWidth()) / 2, 0);

        queryPane.setVisible(false);
        queryPane.setSize(getWidth(), 30);
        queryPane.setLocation(0, 40);
        queryPane.setVisible(true);

        submit.setSize(80, 25);
        submit.setLocation((getWidth() - submit.getWidth()) / 2, 75);

        if (messagePane.isVisible()) {
            messagePane.setVisible(false);
            messagePane.setSize(getWidth(), getHeight() - 150);
            messagePane.setLocation(0, 110);
            messagePane.setVisible(true);
        } else {
            messagePane.setSize(getWidth(), getHeight() - 150);
            messagePane.setLocation(0, 110);
        }

        if (tablePane != null) {
            tablePane.setVisible(false);
            tablePane.setSize(getWidth(), getHeight() - 160);
            tablePane.setLocation(0, 110);
            tablePane.adjustmentTableSize(tablePane.getWidth());
            tablePane.setVisible(true);

            rowCount.setSize(getWidth(), 20);
            rowCount.setLocation(0, getHeight() - 50);
        }
    }
}
