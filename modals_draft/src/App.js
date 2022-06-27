import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Nav from './nav.jsx';
import Login from './authentication/Login.page';
import Signup from './authentication/Signup.page';
import { Routes, Route } from 'react-router-dom';


const App = () => {
  return (
    <Routes>
      <Route path='/' element={<Nav />} />
      <Route path='nav' element={<Nav />} />
      <Route path='login' element={<Login />} />
      <Route path='signup' element={<Signup />} />
    </Routes>
  )
}

export default App;