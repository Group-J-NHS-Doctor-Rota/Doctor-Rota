import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import ShiftDetailsModal from './popups/ShiftDetailsModal';
import RequestLeaveModal from './popups/RequestLeaveModal';
import ProfileModal from './popups/ProfileModal';
import Logout from './popups/Logout';
import UserDetailsModal from './popups/UserDetailsModal';

const Nav = () => {
    return (
        <div className='temp' >
            <h1>NAVIGATION BAR </h1><br /> <br />
            < UserDetailsModal /><br />
            < RequestLeaveModal /> <br />
            < ProfileModal /> <br />
            < Logout /><br />
            < ShiftDetailsModal /> <br />
            {/* <Button variant='outline-warning'><a href="../login.html">Already have an account? Please log in</a></Button><br />
            <Button variant='outline-warning'><a href="../signup.html">New user? Sign up here</a></Button> */}
        </div>
    )
}

export default Nav;