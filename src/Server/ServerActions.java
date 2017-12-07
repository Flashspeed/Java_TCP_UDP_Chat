package Server;

import ChatGUI.ChatGUI;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerActions
{
    private Socket clientSocket;

    private PrintWriter pWOut;

    private ChatGUI serverGUI = new ChatGUI("Server", 0, 0);

    String remoteIp;
    String remotePort;
    String localPort;
    String mode;

    boolean serverIsRunning = false;
    volatile boolean startFlag = false;

    ServerActions()
    {
        serverGUI.jBtnSend.addActionListener(new ServerSendMessage());
        serverGUI.jBtnStart.addActionListener(new StartChatService());

        serverGUI.jTxtAreaSendMessageBox.addKeyListener(new sendKeyListener());
        serverGUI.jComboMode.addActionListener(new ModeSelectListener());
    }

    void runServer()
    {
        /* If the start flag is true then get out of the while loop and get to work. */
        while (!startFlag)
        {
            // Do nothing.
//            System.out.println("waiting for set flag");
        }

        if(this.mode.equals("tcp server"))
        {
            System.out.println("Server");
            /* Set all the valid fields for running the server. */
            this.localPort = serverGUI.jTextFieldLocalPort.getText();

            try
            {
            /* Create the server socket and  assign a port number to accept incoming messages from */
                ServerSocket serverSocket = new ServerSocket(Integer.parseInt(this.localPort));

                clientSocket = serverSocket.accept();

            /* Print Writer to convert the output of the client into bytes for data transfer */
                pWOut = new PrintWriter(clientSocket.getOutputStream(), true);

                getFromClient();

                pWOut.close();
                serverSocket.close();
                clientSocket.close();
            }
            catch (IOException e)
            {
                System.out.println("Something went wrong while trying to set up the server");
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e);
            }
        }

        if(this.mode.equals("tcp client"))
        {
            System.out.println("Client");

            /* Set all the valid fields for running the server. */
            this.remotePort = serverGUI.jTextFieldRemotePort.getText();
            this.remoteIp = serverGUI.jTextFieldRemoteIp.getText();

            try
            {
            /* Open a socket */
                clientSocket = new Socket(this.remoteIp, Integer.parseInt(remotePort));

                pWOut = new PrintWriter(clientSocket.getOutputStream(), true);

                getFromServer();

                pWOut.close();
                clientSocket.close();
            }
            catch (IOException e)
            {
                System.out.println("Something went wrong on the client");
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e);
                serverGUI.closeChatWindow();
            }
        }

    }

    private void getFromClient()
    {
        try
        {
            BufferedReader bRIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String clientMessage;

            /* If in.readLine is null then it's the end of the input stream */
            while ((clientMessage = bRIn.readLine()) != null)
            {
                String textInTextArea = serverGUI.jTxtAreaViewMessageBox.getText();

                if (textInTextArea.isEmpty())
                {
                    serverGUI.jTxtAreaViewMessageBox.setText("Client: " + clientMessage);
                }
                else
                {
                    serverGUI.jTxtAreaViewMessageBox.setText(textInTextArea + System.lineSeparator() + "Client: " + clientMessage);
                }
                System.out.println("From Client: " + clientMessage);
            }

            bRIn.close();
        }
        catch (IOException e)
        {
            System.out.println("Something went wrong while trying to get messages from the client");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void getFromServer()
    {
        try
        {
            BufferedReader bRIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String serverMessage;

            while ((serverMessage = bRIn.readLine()) != null)
            {
                String textInTextArea = serverGUI.jTxtAreaViewMessageBox.getText();

                if (textInTextArea.isEmpty())
                {
                    serverGUI.jTxtAreaViewMessageBox.setText("Server: " + serverMessage);
                }
                else
                {
                    serverGUI.jTxtAreaViewMessageBox.setText(textInTextArea + System.lineSeparator() + "Server: " + serverMessage);
                }
                System.out.println("From Server: " + serverMessage);
            }

            bRIn.close();
        }
        catch (IOException e)
        {
            System.out.println("Something went wrong while receiving from the server");
        }
    }


    private void sendToClient()
    {
        String serverMessage = serverGUI.jTxtAreaSendMessageBox.getText().trim();
        pWOut.println(serverMessage);
    }

    private void updateMessageView()
    {
        String serverMessage  = serverGUI.jTxtAreaSendMessageBox.getText().trim();
        String textInTextArea = serverGUI.jTxtAreaViewMessageBox.getText();

        if (textInTextArea.isEmpty())
        {
            serverGUI.jTxtAreaViewMessageBox.setText("(You) Server: " + serverMessage);
        }
        else
        {
            serverGUI.jTxtAreaViewMessageBox.setText(textInTextArea + System.lineSeparator() + "(You) Server: " + serverMessage.trim());
        }
    }

    public class ServerSendMessage implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (!serverGUI.jTxtAreaSendMessageBox.getText().isEmpty())
            {
                sendToClient();
                updateMessageView();
                serverGUI.clearSendMessageArea();
            }
        }
    }

    public class sendKeyListener implements KeyListener
    {
        @Override
        public void keyTyped(KeyEvent e)
        {

        }

        @Override
        public void keyPressed(KeyEvent e)
        {

        }

        @Override
        public void keyReleased(KeyEvent e)
        {
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_ENTER:

                    if (!serverGUI.jTxtAreaSendMessageBox.getText().isEmpty())
                    {
                        sendToClient();
                        updateMessageView();
                        serverGUI.clearSendMessageArea();
                    }
                    break;
            }
        }
    }

    public class ModeSelectListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            JComboBox modeComboBox = (JComboBox) e.getSource();
            remoteIp = serverGUI.jTextFieldRemoteIp.getText();
            remotePort = serverGUI.jTextFieldRemotePort.getText();
            localPort = serverGUI.jTextFieldLocalPort.getText();
            ServerActions.this.mode = ((String) modeComboBox.getSelectedItem()).toLowerCase();
            JOptionPane.showMessageDialog(null, remotePort);
        }
    }

    public class StartChatService implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            ServerActions.this.remoteIp = serverGUI.jTextFieldRemoteIp.getText();
            ServerActions.this.remotePort = serverGUI.jTextFieldRemotePort.getText();
            ServerActions.this.localPort = serverGUI.jTextFieldLocalPort.getText();

            JOptionPane.showMessageDialog(null,
                    String.format("Mode: %s \nRemote Ip: %s \nRemote port: %s\nLocal port: %s",
                            ServerActions.this.mode,
                            ServerActions.this.remoteIp,
                            ServerActions.this.remotePort,
                            ServerActions.this.localPort));
            startFlag = true;
        }

    }
}