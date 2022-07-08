// This app uses es6 new features
// install bootstrap, react-bootstrap and bootstrap-icons
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';

// install react-router-dom package
import {
  BrowserRouter as Router,
  Routes,
  Route
} from 'react-router-dom'; // Q: BrowserRouter not in index.js?

import SignInPage from './pages/SignInPage';
import SignUpPage from './pages/SignUpPage';
import Homepage from './pages/Homepage';
import AccountPage from './pages/AccountPage';
import NotificationPage from './pages/NotificationPage';
import NavBar from './components/NavBar';

const App = () => {
  return (
    <div>
      <Router>
        {/* Routes instead of Switch */}
        <Routes>
          {/* element instead of component attribute, 
          {< />} instead of {} in element*/}
          {/* exact: ensure no partial match*/}
          <Route path="/" element={<NavBar />}>
            <Route index path="/" element={<Homepage />} />
            <Route path="account" element={<AccountPage />} />
            <Route path="notification" element={<NotificationPage />} />
          </Route>
          <Route path="signin" element={<SignInPage />} />
          <Route path="signup" element={<SignUpPage />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
