package component;

import source.MysqlFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class UpdateFrame extends JFrame {

    private String tableName;
    private MysqlFrame parentFrame;
    ManagementPanel managementPanel;
    String[] values;
    private JPanel framePanel = new JPanel();
    private JLabel header = new JLabel("Update record information");
    private JButton update = new JButton("update");
    private JPanel inputPanel = new JPanel();
    JScrollPane pane = new JScrollPane();

    public UpdateFrame(MysqlFrame parentFrame, String tableName, ManagementPanel managementPanel, String[] values) {
        this.parentFrame = parentFrame;
        this.tableName = tableName;
        this.managementPanel = managementPanel;
        this.values = values;
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

        update.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        update.setBackground(new Color(239, 239, 239));
        update.setForeground(Color.BLACK);
        update.setMargin(new Insets(1, 1, 1, 1));
        update.setCursor(new Cursor(Cursor.HAND_CURSOR));
        framePanel.add(update);

        pane.setBackground(framePanel.getBackground());
        pane.setBorder(null);
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

                inputPanel.add(jp);
                inputField[i] = jtf;

                int finalI = i;
                jtf.addKeyListener(new KeyAdapter() {
                    boolean isEmpty = true;

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (jtf.getForeground() == Color.BLACK) {
                            isEmpty = false;
                        }
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
            }

            pane.setViewportView(inputPanel);

            update.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        try {
                            String q = "";
                            String w = "";
                            for (int i = 0; i < inputField.length; i++) {
                                q += rsm.getColumnName(i + 1) + " = ";
                                q += inputField[i].getForeground() == Color.BLACK ? "'" + inputField[i].getText() + "'" : "null";
                                q += (i == inputField.length - 1) ? "" : " , ";
                                w += rsm.getColumnName(i + 1);
                                w += values[i] != null ? " = '" + values[i] + "'" : " is null";
                                w += (i == values.length - 1) ? "" : " and ";
                            }
                            parentFrame.getMysql().updateQuery("update " + tableName + " set " + q + " where " + w + " limit 1;");
                            managementPanel.doSearch();
                            dispose();
                        } catch (SQLException ex) {
                            JOptionPane.showConfirmDialog(null, ex.getMessage(), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            for (int i = 0; i < inputField.length; i++) {
                if (values[i] != null) {
                    inputField[i].setText(values[i]);
                    inputField[i].setForeground(Color.BLACK);
                }
            }

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

        update.setSize(80, 30);
        update.setLocation((framePanel.getWidth() - update.getWidth()) / 2, framePanel.getHeight() - 90);

        pane.setVisible(false);
        pane.setSize(framePanel.getWidth() - 60, framePanel.getHeight() - 190);
        pane.setLocation(30, 70);
        pane.setVisible(true);
    }
}
