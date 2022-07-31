import React from 'react'

import { Modal } from 'react-bootstrap'

import { useNavigate } from 'react-router-dom';

import '../css/general.css'
import styled from 'styled-components'

export default function LogoutModal({ logout, setLogout }) {
    const navigate = useNavigate()

    return (
        <>
            <Modal show={logout}>
                <div className="d-flex justify-content-center mb-4 mt-5">
                    <LogoutWarning>Do you want to log out?</LogoutWarning>
                </div>

                <div className="d-flex justify-content-center my-3">
                    <CloseButton className="m-2" onClick={() => setLogout(false)}>
                        Close
                    </CloseButton>

                    <ConfirmButton className="m-2" onClick={() => {
                        setLogout(false)
                        navigate('signin')
                    }}>
                        Confirm
                    </ConfirmButton>
                </div>
            </Modal>
        </>
    )
}

const LogoutWarning = styled.h1`
    font-size: 32px;
    font-weight: bold;
    color: #035eb8;
    padding: 10px;

    @media (max-width: 575px){
        font-size: 24px;
    }
`

const CloseButton = styled.button`
    min-width: 100px;
    font-size: 20px;
    background-color: white;
    border-radius: 5px;
    border: none;
    color: #035eb8;
    font-weight: bold;
    padding: 5px 10px;

    @media (max-width: 575px){
        font-size: 16px;
        min-width: 80px;
    }
`

const ConfirmButton = styled.button`
    min-width: 100px;
    font-size: 20px;
    background-color: #035eb8;
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
