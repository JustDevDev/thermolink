# ThermoLink Frontend

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
npm dev
```

The application will be available at `http://localhost:3001`

### Build

Create a production build:

```bash
npm build
```

### Linting

Run ESLint:

```bash
npm lint
```

## Project Structure

```
src/
├── components/
│   ├── basic-components/    # Reusable UI components
│   └── common-components/   # Shared components
├── pages/                   # Page components
├── stores/                  # MobX stores
├── routers/                # Routing configuration
├── utils/                  # Utility functions
├── i18n/                   # Internationalization
└── public/
    └── locales/           # Translation files
```

## Internationalization

The application supports multiple languages through i18next. Translation files are located in:

- `public/locales/en.json` (English)
- `public/locales/cs.json` (Czech)

## Environment Variables

Required environment variables:

- `VITE_API_URL`: Backend API URL
- `VITE_WS_URL`: WebSocket URL
- `VITE_GOOGLE_AUTH_URL`: Google Authentication URL

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Scripts

- `npm dev` - Start development server
- `npm build` - Create production build
- `npm lint` - Run ESLint
- `npm preview` - Preview production build locally

## Browser Support

The application supports all modern browsers:

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)
