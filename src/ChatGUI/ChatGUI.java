package ChatGUI;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class ChatGUI
{
    public String windowTitle = "";
    private int xLocation;
    private int yLocation;

    public String[] mode = {"Choose Mode", "TCP Server", "TCP Client", "UDP Server", "UDP Client"};

    public JTextArea jTxtAreaViewMessageBox = new JTextArea(20, 40);
    public JTextArea jTxtAreaSendMessageBox = new JTextArea(5, 50);

    public JTextField jTextFieldRemoteIp   = new JTextField("Remote IP", 10);
    public JTextField jTextFieldRemotePort = new JTextField("Remote PORT", 10);
    public JTextField jTextFieldLocalPort  = new JTextField("4444", 10);

    public JButton jBtnSend  = new JButton("Send");
    public JButton jBtnStart = new JButton("Start");

    private JFrame chatWindow;
    public JComboBox jComboMode = new JComboBox(mode);

    public ChatGUI(String windowTitle, int xLocation, int yLocation)
    {
        this.windowTitle = windowTitle;
        this.xLocation = xLocation;
        this.yLocation = yLocation;
        showChatWindow();
    }

    private void showChatWindow()
    {
        chatWindow = new JFrame(this.windowTitle);
        JPanel jPMain                    = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel jPanMessageView           = new JPanel(new FlowLayout(FlowLayout.CENTER)); //GridLayout(1, 2) FlowLayout();
        JPanel jPanMessageSend           = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel jPanConfig                = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel jPanReceivedMessagesTitle = new JPanel();

        JLabel jLblReceivedMessages = new JLabel("Recieved Messages");
        JLabel jLblSentTo           = new JLabel("Send To");

        final Color RED   = new Color(255, 0, 0, 170);
        final Color BLUE  = new Color(0, 0, 255, 50);
        final Color WHITE = new Color(255, 255, 255, 180);

        /* JPanel Attributes START */
        jPMain.add(jPanConfig);
        jPMain.add(jPanMessageView);
        jPMain.add(jPanMessageSend);

        jPanConfig.setBackground(RED);
        jPanConfig.setPreferredSize(new Dimension(200, 200));
        jPanConfig.add(jComboMode);
        jPanConfig.add(jTextFieldRemoteIp);
        jPanConfig.add(jTextFieldRemotePort);
        jPanConfig.add(jTextFieldLocalPort);
        jPanConfig.add(jBtnStart);

        jPMain.setBackground(BLUE);
        jPanMessageView.setBackground(RED);
        jPanMessageSend.setBackground(WHITE);

        jPanMessageView.add(jLblReceivedMessages);
        jPanMessageView.add(jTxtAreaViewMessageBox);
        jPanMessageSend.add(jTxtAreaSendMessageBox);
        jPanMessageSend.add(jBtnSend);
        /* JPanel Attributes END */

        /* JTextArea Attributes START */
        jTxtAreaViewMessageBox.setEditable(false);
        /* JTextArea Attributes END */

        /* JButton Attributes START */
        jBtnSend.setMinimumSize(new Dimension(300, 200));
        jBtnSend.setFocusPainted(false);
        /* JButton Attributes END */

        /* JLabel Attributes START */
        jLblReceivedMessages.setForeground(Color.WHITE);
        /* JLabel Attributes END */

        /* JCombo Attributes START */
        jComboMode.setPreferredSize(new Dimension(100, 20));
        /* JCombo Attributes END */

        /* JFrame Attributes START */
        chatWindow.setPreferredSize(new Dimension(800, 500));
        chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatWindow.getContentPane().add(jPMain);
        chatWindow.pack();
        chatWindow.setResizable(true);
        chatWindow.setLocation(xLocation, yLocation);
        chatWindow.setVisible(true);
        /* JFrame Attributes END */
    }

    public void closeChatWindow()
    {
        chatWindow.setVisible(false);
        chatWindow.dispose();
    }

    /**
     * Clears the text in the message send box
     */
    public void clearSendMessageArea()
    {
        jTxtAreaSendMessageBox.setText("");
    }

}
