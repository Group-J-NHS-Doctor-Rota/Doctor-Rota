// This app uses es6 new features
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';

// install react-router-dom package 6.3.0
import {
  BrowserRouter as Router,
  Routes,
  Route
} from 'react-router-dom';

import { UrlProvider } from './contexts/UrlContexts';

import SignInPage from './layout/SignInPage';
import SignUpPage from './layout/SignUpPage';
import Homepage from './layout/Homepage';
import AccountPage from './layout/AccountPage';
import NotificationPage from './layout/NotificationPage';
import NavBar from './components/NavBar';

const App = () => {
  return (
    <div>
      <UrlProvider>
      <Router>
        <Routes>
            <Route path="/" element={<NavBar />}>
              <Route index path="/" element={<Homepage />} />
              <Route path="account" element={<AccountPage />} />
              <Route path="notification" element={<NotificationPage />} />
            </Route>
            <Route path="signin" element={<SignInPage />} />
            <Route path="signup" element={<SignUpPage />} />
        </Routes>
      </Router>
      </UrlProvider>
    </div>
  );
}

export default App;
