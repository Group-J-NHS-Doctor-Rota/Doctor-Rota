import { useState } from 'react'

// useNavigate instead of useHistory
import { Outlet, useNavigate } from 'react-router-dom'

import LogoutModal from '../modals/LogoutModal'
import ProfileModal from '../modals/ProfileModal'
import RequestLeaveModal from '../modals/RequestLeaveModal'

import styled from 'styled-components'

const NavBar = () => {
    const [barOpen, setBarOpen] = useState(false)
    const [profile, setProfile] = useState(false)
    const [logout, setLogout] = useState(false)
    const [leave, setLeave] = useState(false)

    const toggleBarList = (select) => {
        setBarOpen(!barOpen) // bar toggle logic

        // only if the barOpen=true, it opens modal and make barOpen=false
        if (select === "profile") setProfile(true)
        if (select === "leave") setLeave(true)
        if (select === "logout") setLogout(true)
    }

    const navigate = useNavigate()

    return (
        <>
            <div className='d-block'>

                {/* the bar at top right side */}
                <Navbar>
                    {/* NHS logo goes here*/}
                    {/* bar list */}
                    <div className='d-flex justify-content-end me-2'>
                        <GenerateButton className='my-2 me-3' onClick={
                            () => navigate('')}>Home</GenerateButton>
                        <GenerateButton className='my-2 me-3'>Generate Rota</GenerateButton>
                        <i id="icon-list" class="bi bi-list"
                            style={{
                                fontSize: '40px',
                                cursor: 'pointer',
                                color: '#168082',
                            }}
                            onClick={() => toggleBarList()}></i>
                    </div>
                </Navbar>

                {/* additional javascript to handle modals*/}
                {
                    barOpen &&
                    <div className='d-flex justify-content-end'>
                        <BarList className='me-2 mt-2 p-3'>

                            {/* align-items-center instead of align-middle */}
                            <BarItem className='d-flex align-items-center my-2' onClick={() => toggleBarList("profile")}>
                                <div className='d-flex mx-2'>
                                    <i className="bi bi-person-fill" style={{
                                        fontSize: '30px'
                                    }} />
                                </div>
                                <div className='d-flex'>
                                    {/* name called by fetch API */}
                                    <p className='mb-0'>Dennis Liú (admin)</p>
                                </div>
                            </BarItem>

                            <BarItem className='d-flex align-items-center my-2' onClick={() => navigate('account')}>
                                <div className='d-flex mx-2'>
                                    <i className="bi bi-people-fill" style={{
                                        fontSize: '30px'
                                    }} />
                                </div>
                                <div className='d-flex'>
                                    <p className='mb-0'>Manage</p>
                                </div>
                            </BarItem>

                            <BarItem className='d-flex align-items-center my-2' onClick={() => toggleBarList("leave")}>
                                <div className='d-flex mx-2'>
                                    <i className="bi bi-clipboard2-check-fill" style={{
                                        fontSize: '30px'
                                    }} />
                                </div>
                                <div className='d-flex'>
                                    {/* name called by fetch API */}
                                    <p className='mb-0'>Request Leave</p>
                                </div>
                            </BarItem>

                            <BarItem className='d-flex align-items-center my-2' onClick={() => navigate('notification')}>
                                <div className='d-flex mx-2'>
                                    <i className="bi bi-bell-fill" style={{
                                        fontSize: '30px'
                                    }} />
                                </div>
                                <div className='d-flex'>
                                    {/* name called by fetch API */}
                                    <p className='mb-0'>Notification</p>
                                </div>
                            </BarItem>

                            <BarItem className='d-flex align-items-center my-2' onClick={() => toggleBarList("logout")}>
                                <div className='d-flex mx-2'>
                                    <i className="bi bi-box-arrow-in-right" style={{
                                        fontSize: '30px'
                                    }} />
                                </div>
                                <div className='d-flex'>
                                    {/* name called by fetch API */}
                                    <p className='mb-0'>Logout</p>
                                </div>
                            </BarItem>
                        </BarList>
                    </div>
                }
                <LogoutModal logout={logout} setLogout={setLogout} />
                <ProfileModal profile={profile} setProfile={setProfile} />
                <RequestLeaveModal leave={leave} setLeave={setLeave} />

                {/* stick the navbar at the top*/}
                <Outlet />
            </div>
        </>
    )
}

export default NavBar

const Navbar = styled.div`
    position: relative;
    width: 100%;
    height: 60px;
    background-color: #4CA6A7;
`

// min-height make sure the box can be extended
const BarList = styled.div`
    position: absolute;
    z-index: 99;
    min-width: 260px;
    min-height: 250px; 
    background-color: white;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
    border-radius: 5px;
`

const BarItem = styled.div`
    border-radius: 5px;
    cursor: pointer;

    &:hover {
        background-color: #EDFCF9;
    }
`

const GenerateButton = styled.button`
    min-width: 100px;
    font-size: 20px;
    background-color: #168082;
    border-radius: 5px;
    border: none;
    color: white;
    font-weight: Normal;
    padding: 5px 10px;

    @media (max-width:350px) {
        font-size: 14px;
    }
`