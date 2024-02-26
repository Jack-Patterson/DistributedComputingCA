package pattersonjack.distributedcomputingca.Client;

import pattersonjack.distributedcomputingca.Shared.Commands.Command;
import pattersonjack.distributedcomputingca.Shared.HostData;
import pattersonjack.distributedcomputingca.Shared.SMPMessage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class RunSMPClientGui extends JFrame {
    private SMPClient client;

    private final JTextField hostnameField;
    private final JTextField portField;
    private final JButton connectButton;
    private final JButton sendButton;
    private final JButton helpButton;
    private final JComboBox<String> commandDropdown;
    private final JTextArea messageInputArea;
    private final String[] commands = {"login", "logout", "getmessages", "uploadmessage"};

    public static void main(String[] args) {
        new RunSMPClientGui().createGUI();
    }

    public RunSMPClientGui() {
        hostnameField = new JTextField();
        portField = new JTextField();
        connectButton = new JButton();

        sendButton = new JButton();
        helpButton = new JButton();
        commandDropdown = new JComboBox<>();
        messageInputArea = new JTextArea();
    }

    private void createGUI() {
        setTitle("SMP Client");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel parentPanel = constructParentPanel();
        setContentPane(parentPanel);

        JPanel connectPanel = constructConnectPanel();
        add(connectPanel, BorderLayout.NORTH);

        JPanel smpPanel = constructSMPPanel();
        add(smpPanel, BorderLayout.CENTER);

        setMessagingInterfaceEnabled(false);

        setVisible(true);
    }

    private void connectToServer(ActionEvent e) {
        try {
            String hostname = hostnameField.getText();
            int port = Integer.parseInt(portField.getText());

            HostData hostData = new HostData(hostname, port);
            client = new SMPClient(hostData);

            JOptionPane.showMessageDialog(this, "Connected successfully to the server!");
            setConnectionInterfaceEnabled(false);
            setMessagingInterfaceEnabled(true);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this, "Failed to connect to the server: " + ioe.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, portField.getText() + " is not a valid port number. Please only input an integer.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sendMessage(ActionEvent e) {
        String command = String.valueOf(commandDropdown.getSelectedItem());
        String messageText = messageInputArea.getText();
        String fullMessage = command + " " + messageText;
        try {
            client.sendMessage(fullMessage);
            SMPMessage response = client.receiveMessage();

            if (command.equals(Command.logoutPrefix)) {
                JOptionPane.showMessageDialog(this, "Logging off", "Logout", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(this, response.message());
                messageInputArea.setText("");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showHelp() {
        JOptionPane.showMessageDialog(this, """
                You may now send commands to the server. The commands are as follows:
                1. login [username] [password] - Used for logging in to the server.
                2. logout - Used for logging out of the server.
                3. uploadmessage [message] - Used to upload a message to the server.
                4. getmessages [(OPTIONAL) index] - Will retrieve all messages from the server.
                Please input commands with all required arguments as shown above. e.g. login jack 12345
                \s""");
    }

    private JPanel constructParentPanel() {
        JPanel parentPanel = new JPanel();
        parentPanel.setLayout(new BoxLayout(parentPanel, BoxLayout.Y_AXIS));
        parentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        return parentPanel;
    }

    private JPanel constructConnectPanel() {
        JPanel connectPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        connectPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        connectPanel.add(new JLabel("Hostname:"));
        connectPanel.add(hostnameField);

        connectPanel.add(new JLabel("Port:"));
        connectPanel.add(portField);

        connectButton.setText("Connect");
        connectButton.addActionListener(this::connectToServer);
        connectPanel.add(connectButton);

        return connectPanel;
    }

    private JPanel constructSMPPanel() {
        JPanel smpPanel = new JPanel();
        smpPanel.setLayout(new BorderLayout(10, 10));

        JPanel dropDownHelpPanel = new JPanel();
        dropDownHelpPanel.setLayout(new FlowLayout());

        for (String item : commands) {
            commandDropdown.addItem(item);
        }
        dropDownHelpPanel.add(commandDropdown);

        helpButton.setText("Help");
        helpButton.addActionListener(e -> showHelp());

        dropDownHelpPanel.add(helpButton);

        smpPanel.add(dropDownHelpPanel, BorderLayout.NORTH);

        smpPanel.add(new JScrollPane(messageInputArea), BorderLayout.CENTER);

        sendButton.setText("Send");
        sendButton.addActionListener(this::sendMessage);
        smpPanel.add(sendButton, BorderLayout.SOUTH);

        return smpPanel;
    }

    private void setConnectionInterfaceEnabled(boolean isEnabled) {
        hostnameField.setEnabled(isEnabled);
        portField.setEnabled(isEnabled);
        connectButton.setEnabled(isEnabled);
    }

    private void setMessagingInterfaceEnabled(boolean isEnabled) {
        commandDropdown.setEnabled(isEnabled);
        messageInputArea.setEnabled(isEnabled);
        sendButton.setEnabled(isEnabled);
        helpButton.setEnabled(isEnabled);

        if (isEnabled)
            showHelp();
    }
}
