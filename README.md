<div style="display: flex; align-items: center; gap: 1rem">
  <img src="frontend/public/logo.svg" alt="ThermoLink Logo" width="80" />
</div>

# About ThermoLink

ThermoLink is a demonstration web application for real-time temperature monitoring. This project serves as a functional example of how to build a modern web interface for temperature monitoring systems.

The repository is divided into two main parts:

- **Frontend**: A React-based web interface
- **Backend**: API services and data processing

# Frontend

A modern React application built with TypeScript, featuring a responsive design, internationalization, and Material-UI components.

## Tech Stack

- **Framework:** React 18.3
- **Build Tool:** Vite 6.0
- **Language:** TypeScript 5.6
- **Styling:**
  - Material-UI (MUI) 6.2
  - Tailwind CSS 3.4
  - Emotion
- **State Management:** MobX 6.13
- **Routing:** React Router DOM 7.0
- **Internationalization:** i18next
- **Package Manager:** npm 9.15

## Features

- 🎨 Responsive design with Material-UI components
- 🌐 Multi-language support (English, Czech)
- 🔐 Authentication system
- 🎯 Dashboard interface
- 🎨 Light/Dark theme support
- 📱 Mobile-friendly interface
- 🔍 Type-safe development with TypeScript

## Getting Started

### Prerequisites

- Node.js (LTS version recommended)
- npm 9.15 or higher

### Installation

1. Clone the repository:

```bash
git clone [repository-url]
```

2. Install dependencies:

```bash
npm install
```

3. Create a `.env` file in the root directory with the following variables:

```env
VITE_API_URL=your_api_url
VITE_WS_URL=your_websocket_url
VITE_GOOGLE_AUTH_URL=your_google_auth_url
```

### Development

Start the development server:

```bash
npm run dev
```

The application will be available at `http://localhost:3001`
