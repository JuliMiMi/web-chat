import javax.swing.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

public class chatClient {

    JTextField text;
    PrintWriter writer;
    Socket sock;
    JTextArea incoming;
    BufferedReader reader;

    public void go() {
        JFrame frame = new JFrame("Чатик");
        JPanel mainPanel = new JPanel();

        text = new JTextField(30);

        JButton sendButton = new JButton("Отправить сообщение");
        sendButton.addActionListener(new SendButtonListener());

        setUpNetworking();

        incoming = new JTextArea(15, 50);
        incoming.setLineWrap(true);
        incoming.setEditable(false);

        JScrollPane qScroller = new JScrollPane((incoming));
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(text);
        mainPanel.add(sendButton);
        mainPanel.add(qScroller);

        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(600, 400);
        frame.setVisible(true);
    }

    private void setUpNetworking() {
        try {
            sock = new Socket("127.0.0.1", 5000);
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(sock.getOutputStream());
            System.out.println("Установлено соединение");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public class SendButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            try {
                writer.println(text.getText());
                writer.flush();
            } catch (Exception ex) {
                ex.printStackTrace();

            }
            text.setText("");
            text.requestFocus();
        }
    }

    private class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println("READ" + message);
                    incoming.append(message + "\n");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        }


}


