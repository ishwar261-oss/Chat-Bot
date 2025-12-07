/* Compile:
 *   javac -cp json.jar ChatBotGUI.java
 * Run:
 *   java -cp .;json.jar ChatBotGUI 
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.json.*;

public class ChatBotGUI extends JFrame {

    private static final String CHAT_API_URL = "https://openrouter.ai/api/v1/chat/completions";
    private static final String CHAT_API_KEY = System.getenv("OPENROUTER_API_KEY");
    private static final String MODEL = "gpt-4o-mini";

    private final java.util.List<Map<String,String>> conversationHistory = new ArrayList<>();
    private final String HISTORY_FILE = "mohini_history.json";

    private JPanel chatContainer;
    private JScrollPane scrollPane;
    private JTextField inputField;
    private JButton sendButton;

    public ChatBotGUI() {
        setTitle("💬 MOHINI AI – by @IDZz");
        setSize(820, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(20,20,20));

        // CHAT AREA
        chatContainer = new JPanel();
        chatContainer.setLayout(new BoxLayout(chatContainer, BoxLayout.Y_AXIS));
        chatContainer.setBackground(new Color(20,20,20));

        scrollPane = new JScrollPane(chatContainer);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        // INPUT AREA
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(new Color(30,30,30));

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setForeground(Color.WHITE);
        inputField.setBackground(new Color(45,45,45));
        inputField.setCaretColor(Color.WHITE);
        inputField.setBorder(BorderFactory.createEmptyBorder(10,15,10,15));

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setBackground(new Color(0,120,255));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        loadConversationHistory();

        ActionListener act = e -> sendMessage();
        inputField.addActionListener(act);
        sendButton.addActionListener(act);
    }

    private void sendMessage() {
        String userMessage = inputField.getText().trim();
        if (userMessage.isEmpty()) return;

        displayMessage("user", userMessage, true);
        addMessage("user", userMessage);
        inputField.setText("");

        JPanel typing = displayTypingIndicator();

        new Thread(() -> {
            try {
                String aiResponse = getAIResponse();
                SwingUtilities.invokeLater(() -> {
                    chatContainer.remove(typing);
                    displayMessage("assistant", aiResponse, true);
                    addMessage("assistant", aiResponse);
                    saveConversationHistory();
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    chatContainer.remove(typing);
                    displayMessage("assistant", " @ Error : NETWORK NOT AVAILABLE !!! " + ex.getMessage(), false);
                });
            }
        }).start();
    }

    private String getAIResponse() throws IOException {
        if (CHAT_API_KEY == null || CHAT_API_KEY.isEmpty())
            throw new IOException("Set OPENROUTER_API_KEY first.");

        JSONObject req = new JSONObject();
        req.put("model", MODEL);
        req.put("messages", new JSONArray(conversationHistory));
        req.put("max_tokens", 1000);

        HttpURLConnection conn = (HttpURLConnection) new URL(CHAT_API_URL).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + CHAT_API_KEY);
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(req.toString().getBytes(StandardCharsets.UTF_8));
        }

        int code = conn.getResponseCode();
        InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
        }

        if (code != 200)
            throw new IOException("HTTP " + code + ": " + sb.toString());

        JSONObject resp = new JSONObject(sb.toString());
        return resp.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
    }

    private void displayMessage(String role, String content, boolean animate) {
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.X_AXIS));
        messagePanel.setBackground(new Color(20,20,20));
        messagePanel.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));

        JTextArea msg = new JTextArea();
        msg.setLineWrap(true);
        msg.setWrapStyleWord(true);
        msg.setEditable(false);
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        msg.setBorder(BorderFactory.createEmptyBorder(10,15,10,15));
        msg.setMaximumSize(new Dimension(760, Integer.MAX_VALUE));

        if (role.equals("user")) {
            msg.setBackground(new Color(0,120,225));
            msg.setForeground(Color.WHITE);
            messagePanel.add(Box.createHorizontalGlue());
            messagePanel.add(msg);
        } else {
            msg.setBackground(new Color(40,40,40));
            msg.setForeground(Color.WHITE);
            messagePanel.add(msg);
            messagePanel.add(Box.createHorizontalGlue());
        }

        chatContainer.add(messagePanel);
        chatContainer.revalidate();
        chatContainer.repaint();
        autoScroll();

        if (!animate) {
            msg.setText(content);
            return;
        }

        new Thread(() -> {
            StringBuilder temp = new StringBuilder();
            for (char c : content.toCharArray()) {
                temp.append(c);
                String t = temp.toString();
                SwingUtilities.invokeLater(() -> {
                    msg.setText(t);
                    autoScroll();
                });
                try { Thread.sleep(18); } catch (Exception ignored) {}
            }
        }).start();
    }

    private JPanel displayTypingIndicator() {
        JLabel lbl = new JLabel("Mohini is typing...");
        lbl.setForeground(Color.PINK);
        lbl.setFont(new Font("Segoe UI", Font.ITALIC, 14));

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(new Color(20,20,20));
        p.add(lbl);

        chatContainer.add(p);
        chatContainer.revalidate();
        chatContainer.repaint();

        return p;
    }

    private void autoScroll() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar v = scrollPane.getVerticalScrollBar();
            v.setValue(v.getMaximum());
        });
    }

    private void addMessage(String role, String content) {
        if (conversationHistory.size() > 15)
            conversationHistory.remove(0);

        Map<String,String> m = new HashMap<>();
        m.put("role", role);
        m.put("content", content);
        conversationHistory.add(m);
    }

    private void saveConversationHistory() {
        try (FileWriter fw = new FileWriter(HISTORY_FILE)) {
            JSONArray arr = new JSONArray(conversationHistory);
            fw.write(arr.toString(2));
        } catch (Exception ignored) {}
    }

    private void loadConversationHistory() {
        File f = new File(HISTORY_FILE);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);

            JSONArray arr = new JSONArray(sb.toString());
            for (int i=0;i<arr.length();i++) {
                JSONObject o = arr.getJSONObject(i);
                addMessage(o.getString("role"), o.getString("content"));
                displayMessage(o.getString("role"), o.getString("content"), false);
            }
        } catch (Exception ignored) {}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatBotGUI().setVisible(true));
    }
}
