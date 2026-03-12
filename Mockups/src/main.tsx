import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './app/App';
import { BackgroundOnlyPage } from './app/BackgroundOnlyPage';
import './styles/index.css';

const isBackgroundOnly = window.location.hash === '#background' || window.location.search.includes('background=1');

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    {isBackgroundOnly ? <BackgroundOnlyPage /> : <App />}
  </React.StrictMode>
);
