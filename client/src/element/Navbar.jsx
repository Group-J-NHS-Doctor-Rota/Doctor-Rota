import React, { useState } from 'react'

import { useHistory } from 'react-router-dom';

import LogoutModal from '../modal/LogoutModal'
import ProfileModal from '../modal/ProfileModal';
import RequestLeaveModal from '../modal/RequestLeaveModal';

import styled from 'styled-components'

export default function Navbar(){
    const [open, setOpen] = useState(false)
    const [profile, setProfile] = useState(false)
    const [logout, setLogout] = useState(false)
    const [leave, setLeave] = useState(false)

    function toggleList(type){
        setOpen(!open)

        if(type == "profile"){
            setProfile(true)
        }

        if(type == "leave"){
            setLeave(true)
        }

        if(type == "logout"){
            setLogout(true)
        }
    }

    const goPath = useHistory();

    return (
        <>
            <div className="d-block">
                <NavBar>
                    <div className="d-flex justify-content-end me-2">
                        <Button className="m-2">Generate rota</Button>
                        <i id="icon_list" className="bi bi-list" style={{fontSize: '40px', cursor: 'pointer', color: '#168082'}} onClick={() => toggleList()}></i>
                    </div>
                </NavBar>

                {
                    open &&
                    <div className="d-flex justify-content-end">
                        <ListCard className="me-2 mt-2 p-3">
                            <IconBackground className="d-flex my-2"  onClick={() => toggleList("profile")}>
                                <div className="d-flex align-middle mx-2">
                                    <i className="bi bi-person-fill" style={{ fontSize: '30px' }}></i>
                                </div>

                                <div className="d-flex align-items-center">
                                    <p className="mb-0">Steven Lin</p>
                                </div>
                            </IconBackground>

                            <IconBackground className="d-flex my-2" onClick={() => goPath.push('/account')}>
                                <div className="d-flex align-middle mx-2">
                                    <i className="bi bi-people-fill" style={{ fontSize: '30px' }}></i>
                                </div>

                                <div className="d-flex align-items-center">
                                    <p className="mb-0">Manage accounts</p>
                                </div>
                            </IconBackground>


                            <IconBackground className="d-flex my-2" onClick={() => toggleList("leave")}>
                                <div className="d-flex align-middle mx-2">
                                    <i className="bi bi-clipboard2-check-fill" style={{ fontSize: '30px' }}></i>
                                </div>

                                <div className="d-flex align-items-center">
                                    <p className="mb-0">Request Leave</p>
                                </div>
                            </IconBackground>


                            <IconBackground className="d-flex my-2" onClick={() => toggleList()}>
                                <div className="d-flex align-middle mx-2">
                                    <i className="bi bi-bell-fill" style={{ fontSize: '30px' }}></i>
                                </div>

                                <div className="d-flex align-items-center">
                                    <p className="mb-0">Nofitication</p>
                                </div>
                            </IconBackground>

                            <IconBackground className="d-flex my-2" onClick={() => toggleList("logout")}>
                                <div className="d-flex align-middle mx-2">
                                    <i className="bi bi-box-arrow-in-right" style={{ fontSize: '30px' }}></i>
                                </div>

                                <div className="d-flex align-items-center">
                                    <p className="mb-0">Logout</p>
                                </div>
                            </IconBackground>
                        </ListCard>
                    </div>
                }
            </div>

            <LogoutModal logout={logout} setLogout={setLogout}/>
            <ProfileModal profile={profile} setProfile={setProfile}/>
            <RequestLeaveModal leave={leave} setLeave={setLeave}/>
        </>
    )
}

const NavBar = styled.div`
    position: relative;
    width: 100%;
    height: 60px;
    background-color: #4CA6A7;
`

const ListCard = styled.div`
    position: absolute;
    z-index: 99;
    min-width: 260px;
    min-height: 250px;
    background-color: white;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
    border-radius: 5px;
`

const IconBackground = styled.div`
    border-radius: 5px;
    cursor: pointer;
    &:hover{
        background-color: #EDFCF9;
    }
`

const Button = styled.button`
    min-width: 100px;
    font-size: 20px;
    background-color: #168082;
    border-radius: 5px;
    border: none;
    color: white;
    font-weight: bold;
    padding: 5px 10px;

    @media (max-width: 575px){
        font-size: 16px;
        min-width: 80px;
    }
`

