// This app uses es6 new features
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';

// install react-router-dom package 6.3.0
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate
} from 'react-router-dom';

import PrivateRouter from './auth/PrivateRoute';

import { UrlProvider } from './contexts/UrlContexts';
import { AuthProvider } from './contexts/AuthContext';

import SignInPage from './layout/SignInPage';
// import SignUpPage from './layout/SignUpPage';
import Homepage from './layout/Homepage';
import AccountPage from './layout/AccountPage';
import NotificationPage from './layout/NotificationPage';
import NavBar from './components/NavBar';

const App = () => {
  return (
    <div>
      <UrlProvider>
        <AuthProvider>
          <Router>
            <Routes>
                <Route element={<PrivateRouter/>}>
                  <Route path="/" element={<NavBar />}>
                    <Route index path="/" element={<Homepage />} />
                    <Route path="account" element={<AccountPage />} />
                    <Route path="notification" element={<NotificationPage />} />
                  </Route>
                </Route>
                <Route path="signin" element={<SignInPage />} />

                {/* <Route path="signup" element={<SignUpPage />} /> */}
            </Routes>
          </Router>
        </AuthProvider>
      </UrlProvider>
    </div>
  );
}

export default App;
