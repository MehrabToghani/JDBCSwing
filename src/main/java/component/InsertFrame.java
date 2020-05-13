package component;

import source.MysqlFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class InsertFrame extends JFrame {

    private String tableName;
    private MysqlFrame parentFrame;
    ManagementPanel managementPanel;
    private JPanel framePanel = new JPanel();
    private JLabel header = new JLabel("Enter new record information");
    private JButton insert = new JButton("Insert");
    private JPanel inputPanel = new JPanel();
    JScrollPane pane = new JScrollPane();

    public InsertFrame(MysqlFrame parentFrame, String tableName , ManagementPanel managementPanel) {
        this.parentFrame = parentFrame;
        this.tableName = tableName;
        this.managementPanel = managementPanel;
        this.setTitle("MySql");
        this.setSize(400, 400);
        this.setMinimumSize(new Dimension(300, 300));
        Image iconBar = Toolkit.getDefaultToolkit().getImage("src/icon.png");
        this.setIconImage(iconBar);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        initComponents();
        this.setVisible(true);
        adjustmentComponentsSize();
    }

    private void initComponents() {
        framePanel.setBounds(0, 0, this.getWidth(), this.getHeight());
        framePanel.setBackground(new Color(52, 73, 94));
        framePanel.setLayout(null);
        this.add(framePanel);

        header.setForeground(Color.WHITE);
        header.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        header.setHorizontalAlignment(0);
        framePanel.add(header);

        insert.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        insert.setBackground(new Color(239, 239, 239));
        insert.setForeground(Color.BLACK);
        insert.setMargin(new Insets(1, 1, 1, 1));
        insert.setCursor(new Cursor(Cursor.HAND_CURSOR));
        insert.setSize(80, 30);
        framePanel.add(insert);

        pane.setBackground(framePanel.getBackground());
        pane.setBorder(null);
        pane.setLocation(30, 70);
        framePanel.add(pane);

        inputPanel.setOpaque(false);
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        framePanel.add(inputPanel);


        try {
            ResultSetMetaData rsm = parentFrame.getMysql().setQuery("select * from " + tableName + ";").getMetaData();

            JTextField[] inputField = new JTextField[rsm.getColumnCount()];

            for (int i = 0; i < rsm.getColumnCount(); i++) {
                JPanel jp = new JPanel();
                jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));

                JTextField jtf = new JTextField(rsm.getColumnName(i + 1));
                jtf.setMargin(new Insets(10, 10, 10, 10));
                jtf.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
                jtf.setForeground(new Color(106, 106, 106));
                jp.add(jtf);

                int finalI = i;
                jtf.addKeyListener(new KeyAdapter() {
                    boolean isEmpty = true;

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (isEmpty) {
                            jtf.setText("");
                        }
                        isEmpty = false;
                        jtf.setForeground(Color.BLACK);
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        try {
                            if (jtf.getText().equals("")) {
                                isEmpty = true;
                                jtf.setText(rsm.getColumnName(finalI + 1));
                                jtf.setForeground(new Color(106, 106, 106));
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                inputPanel.add(jp);
                inputField[i] = jtf;
            }

            pane.setViewportView(inputPanel);

            insert.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        try {
                            String q = "";
                            for (int i = 0; i < inputField.length; i++) {
                                q += inputField[i].getForeground() == Color.BLACK? "'" + inputField[i].getText() + "'" : "null";
                                q += (i == inputField.length - 1) ? "" : " , ";
                            }
                            parentFrame.getMysql().updateQuery("insert into " + tableName + " values (" + q + ");");
                            managementPanel.doSearch();
                            dispose();
                        } catch (SQLException ex) {
                            JOptionPane.showConfirmDialog(null, ex.getMessage(), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                adjustmentComponentsSize();
            }
        });
    }

    private void adjustmentComponentsSize() {
        framePanel.setSize(getWidth() - 10, getHeight());

        header.setSize(framePanel.getWidth(), 30);
        header.setLocation((framePanel.getWidth() - header.getWidth()) / 2, 10);

        insert.setLocation((framePanel.getWidth() - insert.getWidth()) / 2, framePanel.getHeight() - 90);

        pane.setVisible(false);
        pane.setSize(framePanel.getWidth() - 60, framePanel.getHeight() - 190);
        pane.setVisible(true);
    }
}
