import React, { useState } from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router';
import App from './app/App';
import { BackgroundOnlyPage } from './app/BackgroundOnlyPage';
import { SiteHomePage } from './app/SiteHomePage';
import { TitlePage, hasSeenTitle } from './app/TitlePage';
import './styles/index.css';

const isBackgroundOnly = window.location.hash === '#background' || window.location.search.includes('background=1');

const rawBase = import.meta.env.BASE_URL ?? '/';
const routerBasename = rawBase.replace(/\/$/, '') || undefined;

function AppRoute() {
  const [showTitle, setShowTitle] = useState(() => !hasSeenTitle());
  if (showTitle) return <TitlePage onStart={() => setShowTitle(false)} />;
  return <App />;
}

function Root() {
  if (isBackgroundOnly) return <BackgroundOnlyPage />;
  return (
    <BrowserRouter basename={routerBasename}>
      <Routes>
        <Route path="/" element={<SiteHomePage />} />
        <Route path="/app" element={<AppRoute />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <Root />
  </React.StrictMode>
);
