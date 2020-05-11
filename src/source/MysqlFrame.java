package source;

import component.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MysqlFrame extends JFrame {

    private JPanel target = new LoginPanel(this);
    private Mysql mysql;
    private JPanel framePanel = new JPanel();

    public MysqlFrame() {
        this.setTitle("MySql");
        this.setSize(600, 400);
        this.setMinimumSize(new Dimension(350, 360));
        Image iconBar = Toolkit.getDefaultToolkit().getImage("icon.png");
        this.setIconImage(iconBar);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        initComponents();
        framePanel.add(target);
        this.setVisible(true);
    }

    private void initComponents() {
        framePanel.setBounds(0, 0, this.getWidth(), this.getHeight());
        framePanel.setBackground(new Color(52, 73, 94));
        framePanel.setLayout(null);
        this.add(framePanel);

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                framePanel.setSize(getSize());
            }
        });
    }

    public Color getBackgroundColor () {
        return framePanel.getBackground();
    }

    public void setMysql(Mysql mysql) {
        this.mysql = mysql;
    }

    public Mysql getMysql() {
        return mysql;
    }

    public void setDatabaseChoice() {
        target.setVisible(false);
        target.removeAll();
        target = new DatabaseChoicePanel(this);
        framePanel.add(target);
        target.setVisible(true);
    }

    public void setTableChoice() {
        target.setVisible(false);
        target.removeAll();
        target = new TableChoicePanel(this);
        framePanel.add(target);
        target.setVisible(true);
    }

    public void setCustomQuery() {
        target.setVisible(false);
        target.removeAll();
        target = new CustomQueryPanel(this);
        framePanel.add(target);
        target.setVisible(true);
    }

    public void setManagementPanel (String tableName) {
        target.setVisible(false);
        target.removeAll();
        target = new ManagementPanel(this , tableName);
        framePanel.add(target);
        target.setVisible(true);
    }
}
