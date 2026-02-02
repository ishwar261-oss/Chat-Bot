# 💬 Mohini AI – Your Virtual Best Friend

**Mohini AI** is a desktop chatbot built in **Java Swing**, designed to be a **warm, emotionally intelligent virtual friend**. It uses the **OpenRouter GPT-4o-mini model** to chat naturally, remember past conversations, and provide **support, encouragement, and companionship**. The application also features **Text-to-Speech (TTS)** to speak responses aloud.

---

## 🌟 Features

* **Human-like Conversations** Mohini talks like a caring friend, with emotional intelligence and natural language.
  
* **Persistent Chat History** Conversations are saved locally (`mohini_history.json`) and loaded automatically.

* **Animated Typing & Text Display** Messages appear with a typing animation to simulate real-time chatting.

* **Text-to-Speech (TTS)** Mohini can speak her messages aloud using the system’s TTS engine. Includes **play**, **stop**, and **resume** buttons.

* **Customizable Avatar** Displays an avatar (`mohini.png`) for a friendly chat experience.

* **Beautiful UI** Dark theme, color-coded chat bubbles, responsive scrolling, and stylish input area.

* **Support for Emojis & Emotional Context** Mohini interprets emojis and converts them to text cues for TTS.

---

## 📦 Requirements

* **Java 8+**
* **json.jar** (for JSON handling)
* **Windows** (for TTS via PowerShell)  
    *Note: Linux/macOS support requires TTS adjustment.*
* **OpenRouter API Key** Set your API key as an environment variable:
    ```bash
    OPENROUTER_API_KEY=your_api_key_here
    ```

---

## 🛠 Installation & Setup

1.  **Clone or download the repository:**
    ```bash
    git clone [https://github.com/yourusername/mohini-ai.git](https://github.com/yourusername/mohini-ai.git)
    cd mohini-ai
    ```
2.  **Add `json.jar`** to your classpath.
3.  **Set your OpenRouter API key** in your environment variables.
4.  **Place your avatar image** as `mohini.png` in the project directory (optional, defaults to a placeholder).

---

