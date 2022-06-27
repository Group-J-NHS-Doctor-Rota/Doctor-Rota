import React from 'react'

import { Modal } from 'react-bootstrap'

import '../css/general.css'
import styled from 'styled-components'

export default function LogoutModal({ logout, setLogout }){

    return (
        <>
            <Modal show={logout}>
                <div className="d-flex justify-content-center mb-4 mt-5">
                    <Title>Do you want to log out?</Title>
                </div>
                
                <div className="d-flex justify-content-center my-3">
                    <CloseButton className="m-2" onClick={() => setLogout(false)}>
                        Close
                    </CloseButton>

                    <ConfirmButton className="m-2" onClick={() => setLogout(false)}>
                        Confirm
                    </ConfirmButton>
                </div>
            </Modal>
        </>
    )
}

const Title = styled.h1`
    font-size: 32px;
    font-weight: bold;
    color: #168082;
`

const CloseButton = styled.button`
    min-width: 100px;
    font-size: 24px;
    background-color: white;
    border-radius: 5px;
    border: none;
    color: #168082;
    padding: 5px 10px;
`

const ConfirmButton = styled.button`
    min-width: 100px;
    font-size: 24px;
    background-color: #168082;
    border-radius: 5px;
    border: none;
    color: white;
    padding: 5px 10px;
`
