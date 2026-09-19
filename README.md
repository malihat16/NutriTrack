# 🥗 NutriTrack
 
**Clinical Nutrition Insights App** — an Android app that turns raw dietary data into clear, actionable health insights.
 
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=flat&logo=jetpackcompose&logoColor=white)
![Room](https://img.shields.io/badge/Room-DB-blue)
![Retrofit](https://img.shields.io/badge/Retrofit-API-green)
![Gemini](https://img.shields.io/badge/Google%20Gemini-GenAI-8E75B2?style=flat&logo=google&logoColor=white)
 
---
 
## Overview
 
NutriTrack visualises clinical dietary scores using the **HEIFA (Healthy Eating Index for Australian Adults)** framework, helping users understand their eating patterns and get personalised, AI-generated nutrition guidance — all backed by a clean, testable MVVM architecture.
 
## ✨ Features
 
- **HEIFA-based dietary scoring** — visualises clinical nutrition scores in a way that's easy to understand at a glance
- **Google Gemini (GenAI) integration** — turns raw scores into contextual, plain-language nutrition tips
- **FruityVice API integration** (via Retrofit) — live nutritional data lookups
- **Multi-user authentication** — supports multiple user profiles securely
- **Admin dashboard** — a secure, separate view with aggregate HEIFA analytics
- **Persistent local storage** — normalised patient/user data stored with Room for reliable offline access
## 🏗️ Architecture
 
Built with **MVVM (Model-View-ViewModel)**:
 
```
UI (Jetpack Compose)
      ↓ observes
ViewModel  ──────────────► Repository
      ↑                         │
   UI State              ┌──────┴──────┐
                          │             │
                       Room DB     Retrofit
                     (local data)  (FruityVice API,
                                    Gemini GenAI)
```
 
- **Jetpack Compose** — declarative UI layer
- **Room** — local, normalised persistence for user and dietary data
- **Retrofit** — networking layer for the FruityVice API and Gemini integration
- **ViewModel + StateFlow** — UI state management, decoupled from the view layer
## 🛠️ Tech Stack
 
| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose |
| Architecture | MVVM |
| Local Storage | Room DB |
| Networking | Retrofit |
| AI / GenAI | Google Gemini API |
| External Data | FruityVice API |
 

 
## 🚀 Getting Started
 
### Prerequisites
 
- Android Studio (Giraffe or later recommended)
- JDK 17+
- A Google Gemini API key ([get one here](https://ai.google.dev/))
### Setup
 
1. Clone the repo:
```bash
   git clone https://github.com/malihat16/NutriTrack.git
   cd NutriTrack
```
 
2. Add your API keys to `local.properties` (this file is git-ignored and should never be committed):
```properties
   GEMINI_API_KEY=your_gemini_api_key_here
   FRUITYVICE_BASE_URL=https://www.fruityvice.com/api/
```
 
3. Open the project in Android Studio, let Gradle sync, then run on an emulator or physical device.

 
## 🗺️ Roadmap
 
- [ ] Export/share HEIFA reports as PDF
- [ ] Historical trend charts for dietary scores
- [ ] Push notifications for check-in reminders
- [ ] Unit test coverage for ViewModels and repositories
## 👩‍💻 Author
 
**Maliha Tariq**
[Portfolio](https://maliha-folio.vercel.app/) · [LinkedIn](https://www.linkedin.com/in/malihatariqq) · [GitHub](https://github.com/malihat16)
 
