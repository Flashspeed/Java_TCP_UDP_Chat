package Client;

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
import java.net.Socket;

class ClientActions
{
    private String ipAddress;

    private int portNumber;

    private PrintWriter pWOut = null;

    private Socket clientSocket;

    private ChatGUI clientGUI = new ChatGUI("Client", 800, 0);

    ClientActions(String ipAddress, int portNumber)
    {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        clientGUI.jBtnSend.addActionListener(new ClientSendMessage());
        clientGUI.jTxtAreaSendMessageBox.addKeyListener(new sendKeyListener());
    }

    public void runClient()
    {
        try
        {
            /* Open a socket */
            clientSocket = new Socket(ipAddress, portNumber);

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
            clientGUI.closeChatWindow();
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
                String textInTextArea = clientGUI.jTxtAreaViewMessageBox.getText();

                if (textInTextArea.isEmpty())
                {
                    clientGUI.jTxtAreaViewMessageBox.setText("Server: " + serverMessage);
                }
                else
                {
                    clientGUI.jTxtAreaViewMessageBox.setText(textInTextArea + System.lineSeparator() + "Server: " + serverMessage);
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

    private void sendToServer()
    {
        String clientMessage = clientGUI.jTxtAreaSendMessageBox.getText().trim();
        pWOut.println(clientMessage);
    }

    private void updateMessageView()
    {
        String clientMessage  = clientGUI.jTxtAreaSendMessageBox.getText().trim();
        String textInTextArea = clientGUI.jTxtAreaViewMessageBox.getText();

        if (textInTextArea.isEmpty())
        {
            clientGUI.jTxtAreaViewMessageBox.setText("(You) Client: " + clientMessage);
        }
        else
        {
            clientGUI.jTxtAreaViewMessageBox.setText(textInTextArea + System.lineSeparator() + "(You) Client: " + clientMessage.trim());
        }
    }

    public class ClientSendMessage implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(!clientGUI.jTxtAreaSendMessageBox.getText().isEmpty())
            {
                sendToServer();
                updateMessageView();
                clientGUI.clearSendMessageArea();
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

                    if(!clientGUI.jTxtAreaSendMessageBox.getText().isEmpty())
                    {
                        sendToServer();
                        updateMessageView();
                        clientGUI.clearSendMessageArea();
                    }
                    break;
            }
        }
    }
}
