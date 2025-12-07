# Mohini AI ChatBot – Java GUI 💬

[![Java](https://img.shields.io/badge/Java-8+-blue?logo=java)](https://www.java.com/)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)
[![OpenRouter API](https://img.shields.io/badge/OpenRouter-GPT--4o--mini-orange)](https://openrouter.ai/)

**Mohini AI** is a desktop chatbot built in Java using Swing. It connects to the OpenRouter API with GPT-4o-mini to provide intelligent AI responses. The chatbot features a sleek dark-themed GUI, animated typing effects, and saves conversation history for continuity.

---

## Features ✨

- AI-powered responses via **GPT-4o-mini**
- Interactive **Java Swing GUI** with dark theme
- **Animated typing** for realistic responses
- Saves **conversation history** in `mohini_history.json`
- Scrollable chat interface with automatic scrolling
- Distinct colors for **user** and **AI** messages
- Handles network/API errors gracefully

---

## Quick Setup ⚡

### Requirements

- Java 8 or higher
- `json.jar` library for JSON parsing
- OpenRouter API key set as environment variable: `OPENROUTER_API_KEY`

### Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/mohini-ai-chatbot.git
cd mohini-ai-chatbot
