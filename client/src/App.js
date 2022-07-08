import { BrowserRouter as Router, Switch, Route } from 'react-router-dom'

import SignInPage from './layout/SignInPage'
import SignUpPage from './layout/SignUpPage';
import Homgepage from './layout/Homepage';
import AccountPage from './layout/AccountPage';

import 'bootstrap/dist/css/bootstrap.min.css'
import "bootstrap-icons/font/bootstrap-icons.css";

function App() {
  return (
    <div>
      <Router>
        <Switch>
          <Route exact path="/" component={Homgepage} />
          <Route path="/signin" component={SignInPage} />
          <Route path="/signup" component={SignUpPage} />
          <Route path="/account" component={AccountPage} />
        </Switch>
      </Router>
    </div>
  );
}

export default App;
