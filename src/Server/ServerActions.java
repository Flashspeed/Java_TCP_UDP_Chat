package Server;

import ChatGUI.ChatGUI;

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
    private int portNumber;

    private Socket clientSocket;

    private PrintWriter pWOut;

    private ChatGUI serverGUI = new ChatGUI("Server", 0, 0);

    ServerActions(int portNumber)
    {
        this.portNumber = portNumber;
        serverGUI.jBtnSend.addActionListener(new ServerSendMessage());

        serverGUI.jTxtAreaSendMessageBox.addKeyListener(new sendKeyListener());
    }

    void runServer()
    {
        try
        {
            /* Create the server socket and  assign a port number to accept incoming messages from */
            ServerSocket serverSocket = new ServerSocket(this.portNumber);

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
            if(!serverGUI.jTxtAreaSendMessageBox.getText().isEmpty())
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

                    if(!serverGUI.jTxtAreaSendMessageBox.getText().isEmpty())
                    {
                        sendToClient();
                        updateMessageView();
                        serverGUI.clearSendMessageArea();
                    }
                    break;
            }
        }
    }
}
