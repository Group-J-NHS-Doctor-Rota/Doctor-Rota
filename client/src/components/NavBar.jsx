import React, { useState, useEffect } from 'react'

import { Outlet, useNavigate } from 'react-router-dom';

import { useUrl } from '../contexts/UrlContexts'

import LogoutModal from '../modals/LogoutModal'
import ProfileModal from '../modals/ProfileModal';
import RequestLeaveModal from '../modals/RequestLeaveModal';
import RefreshModal from '../modals/RefreshModal';

import styled from 'styled-components'
import ResetPasswordModal from '../modals/ResetPasswordModal';

export default function NavBar() {
    const auth = JSON.parse(localStorage.getItem('auth'))

    const [open, setOpen] = useState(false)
    const [profile, setProfile] = useState(false)
    const [logout, setLogout] = useState(false)
    const [leave, setLeave] = useState(false)
    const [reset, setReset] = useState(false)
    const [refresh, setRefresh] = useState(false)

    const { getUrl } = useUrl()

    const url = getUrl()

    useEffect(() => {
        const closeList = e => {
            if (e.target.id != "icon_list") {
                setOpen(false)
            }
        }

        document.addEventListener('click', closeList)
        return () => document.removeEventListener('click', closeList)
    }, [open])

    const [accountDetail, setAccountDetail] = useState()

    useEffect(() => {

        if (auth.id != undefined) {
            if(url != undefined){
                fetch(`${url}account/${auth.id}`, {
                    mode: 'cors',
                    method: "GET",
                    headers: {
                        'Access-Control-Allow-Origin': '*',
                        'Content-Type': 'application/json',
                        'Accept': 'application/json',
                        'token': auth.token
                    }
                })
                    .then(response => response.json())
                    .then(data => {
                        setAccountDetail(data)
                    })
            }
        }
    }, [auth.id])

    function toggleList(type) {
        setOpen(!open)

        if (type === "profile") {
            setProfile(true)
        }

        if (type === "leave") {
            setLeave(true)
        }

        if (type === "logout") {
            setLogout(true)
        }

        if (type === "reset") {
            setReset(true)
        }
    }

    function redirectPage(type) {
        setOpen(!open)
        if (type == "account") {
            navigate('/account')
        }

        if (type == "notification") {
            navigate('/notification')
        }
    }

    const navigate = useNavigate();

    return (
        <>
            <div className="d-block">
                <Navbar>
                    <div className='d-flex justify-content-between'>
                        <div onClick={() => navigate('/')} style={{ cursor: 'pointer' }}>
                            <Logo src='https://www.england.nhs.uk/nhsidentity/wp-content/themes/nhsengland-identity/templates/assets/img/global/nhs-logo.svg' />
                        </div>
                        <div className="d-flex justify-content-end me-2">

                            {
                                auth.level == 1 &&
                                (<RefreshButton className="my-2 me-4" onClick={() => setRefresh(true)}>Refresh Rota</RefreshButton>)
                            }

                            <i id="icon_list" className="bi bi-list" style={{ fontSize: '40px', cursor: 'pointer', color: 'white' }} onClick={() => toggleList()}></i>
                        </div>
                    </div>
                </Navbar>

                {
                    open &&
                    (<div className="d-flex justify-content-end">
                        <NavBarList className="me-2 mt-2 p-3">
                            <NavBarItem className="d-flex my-2" onClick={() => toggleList("profile")}>
                                <div className="d-flex align-middle mx-2">
                                    <i className="bi bi-person-fill" style={{ fontSize: '30px' }}></i>
                                </div>

                                <div className="d-flex align-items-center">
                                    <p className="mb-0">
                                        {
                                            accountDetail !== undefined &&
                                            accountDetail.username
                                        }
                                    </p>
                                </div>
                            </NavBarItem>

                            {
                                auth.level == 1 &&
                                <NavBarItem className="d-flex my-2" onClick={() => redirectPage('account')}>
                                    <div className="d-flex align-middle mx-2">
                                        <i className="bi bi-people-fill" style={{ fontSize: '30px' }}></i>
                                    </div>

                                    <div className="d-flex align-items-center">
                                        <p className="mb-0">Manage Accounts</p>
                                    </div>
                                </NavBarItem>
                            }

                            <NavBarItem className="d-flex my-2" onClick={() => toggleList("leave")}>
                                <div className="d-flex align-middle mx-2">
                                    <i className="bi bi-clipboard2-check-fill" style={{ fontSize: '30px' }}></i>
                                </div>

                                <div className="d-flex align-items-center">
                                    <p className="mb-0">Request Leave</p>
                                </div>
                            </NavBarItem>


                            <NavBarItem className="d-flex my-2" onClick={() => redirectPage('notification')}>
                                <div className="d-flex align-middle mx-2">
                                    <i className="bi bi-bell-fill" style={{ fontSize: '30px' }}></i>
                                </div>

                                <div className="d-flex align-items-center">
                                    <p className="mb-0">Nofitication</p>
                                </div>
                            </NavBarItem>

                            <NavBarItem className="d-flex my-2" onClick={() => toggleList("reset")}>
                                <div className="d-flex align-middle mx-2">
                                    <i className="bi bi-gear-wide-connected" style={{ fontSize: '30px' }}></i>
                                </div>

                                <div className="d-flex align-items-center">
                                    <p className="mb-0">Reset Password</p>
                                </div>
                            </NavBarItem>

                            <NavBarItem className="d-flex my-2" onClick={() => toggleList("logout")}>
                                <div className="d-flex align-middle mx-2">
                                    <i className="bi bi-box-arrow-in-right" style={{ fontSize: '30px' }}></i>
                                </div>

                                <div className="d-flex align-items-center">
                                    <p className="mb-0">Logout</p>
                                </div>
                            </NavBarItem>
                        </NavBarList>
                    </div>)
                }
            </div>

            <LogoutModal logout={logout} setLogout={setLogout} />
            <ProfileModal profile={profile} setProfile={setProfile} />
            <RequestLeaveModal leave={leave} setLeave={setLeave} />
            
            <ResetPasswordModal reset={reset} setReset={setReset} />
            <RefreshModal refresh={refresh} setRefresh={setRefresh} />

            <Outlet />
        </>
    )
}

const Navbar = styled.div`
    position: relative;
    width: 100%;
    height: 60px;
    background-color: #035eb8;
`

const NavBarList = styled.div`
    position: absolute;
    z-index: 99;
    min-width: 260px;
    min-height: 250px;
    background-color: white;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
    border-radius: 5px;
`

const NavBarItem = styled.div`
    border-radius: 5px;
    cursor: pointer;

    &:hover{
        background-color: #f5f9fe;

        p {
            color: #035eb8;
        }

        i {
            color: #035eb8;
        }
    }

`

const RefreshButton = styled.button`
    min-width: 100px;
    font-size: 20px;
    background-color: transparent;
    border: none;
    color: white;
    font-weight: bold;
    &:hover {
        border-bottom: 1.5px solid white
    }

    @media (max-width: 575px){
        font-size: 16px;
        min-width: 80px;
    }
`

const Logo = styled.img`
    height: 100%;
`

