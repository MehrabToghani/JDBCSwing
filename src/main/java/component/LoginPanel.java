package component;

import source.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class LoginPanel extends JPanel {

    private MysqlFrame frame;
    private JLabel loginLabel = new JLabel("Login");
    private JLabel serverLabel = new JLabel("Server: ");
    private JTextField serverField = new JTextField("jdbc:mysql://localhost:3306");
    private JLabel userLabel = new JLabel("User: ");
    private JTextField userField = new JTextField("root");
    private JLabel passwordLabel = new JLabel("Password: ");
    private JPasswordField passwordField = new JPasswordField();
    private JButton login = new JButton("Login");
    private JTextField error = new JTextField();

    public LoginPanel(MysqlFrame frame) {
        this.frame = frame;
        this.setLayout(null);
        this.setOpaque(false);
        initComponents();
        adjustComponentsSize();
    }

    private void initComponents() {
        loginLabel.setForeground(Color.WHITE);
        loginLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
        loginLabel.setHorizontalAlignment(0);
        this.add(loginLabel);

        serverLabel.setForeground(Color.WHITE);
        serverLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        this.add(serverLabel);

        serverField.setMargin(new Insets(1 , 5 , 1 , 5));
        this.add(serverField);

        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        this.add(userLabel);

        userField.setMargin(new Insets(1 , 5 , 1 , 5));
        this.add(userField);

        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        this.add(passwordLabel);

        passwordField.setMargin(new Insets(1 , 5 , 1 , 5));
        this.add(passwordField);

        login.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        login.setBackground(new Color(239, 239, 239));
        login.setForeground(Color.BLACK);
        login.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.add(login);

        error.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        error.setHorizontalAlignment(0);
        error.setForeground(new Color(255, 221, 64));
        error.setEditable(false);
        error.setOpaque(false);
        error.setBorder(null);
        error.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        error.setVisible(false);
        this.add(error);

        login.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    doClick();
                }
            }
        });

        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                error.setVisible(false);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doClick();
                }
            }
        };

        serverField.addKeyListener(enterListener);
        userField.addKeyListener(enterListener);
        passwordField.addKeyListener(enterListener);

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                adjustComponentsSize();
            }
        });
    }

    private void doClick() {
        try {
            String password = "";
            for (char c : passwordField.getPassword()) {
                password += c;
            }
            Mysql mysql = new Mysql(serverField.getText().trim(), userField.getText().trim(), password);
            frame.setMysql(mysql);
            frame.setDatabaseChoice();
        } catch (ClassNotFoundException | SQLException ex) {
            error.setText(ex.getMessage());
            error.setCaretPosition(0);
            error.setVisible(true);
            passwordField.setText("");
        }
    }

    private void adjustComponentsSize() {
        setLocation(42 , 30);
        setSize(frame.getWidth() - 100, frame.getHeight() - 60);

        loginLabel.setSize(getWidth(), 30);
        loginLabel.setLocation((getWidth()-loginLabel.getWidth())/2 , 0);

        serverLabel.setSize(100 , 20);

        userLabel.setSize(100 , 20);

        passwordLabel.setSize(100 , 20);

        login.setSize(74 , 25);
        login.setLocation((getWidth()-login.getWidth())/2 , 210);

        error.setSize(this.getWidth() , 30);
        error.setLocation(0 , 250);

        if (frame.getWidth() > 400) {
            serverLabel.setLocation((frame.getWidth()-400)/2, 60);
            serverField.setSize( 200, 20);
            serverField.setLocation((frame.getWidth()-400)/2 + 100 , 60);

            userLabel.setLocation((frame.getWidth()-400)/2, 110);
            userField.setSize( 200, 20);
            userField.setLocation((frame.getWidth()-400)/2 + 100 , 110);

            passwordLabel.setLocation((frame.getWidth()-400)/2, 160);
            passwordField.setSize( 200, 20);
            passwordField.setLocation((frame.getWidth()-400)/2 + 100 , 160);
        } else {
            serverLabel.setLocation(0, 60);
            serverField.setSize( getWidth()-100, 20);
            serverField.setLocation(100 , 60);

            userLabel.setLocation(0, 110);
            userField.setSize( getWidth()-100, 20);
            userField.setLocation(100 , 110);

            passwordLabel.setLocation(0, 160);
            passwordField.setSize( getWidth()-100, 20);
            passwordField.setLocation(100 , 160);
        }
    }
}
