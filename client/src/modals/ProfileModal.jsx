import React, { useState, useEffect } from 'react'

import { Modal } from 'react-bootstrap'

import styled from 'styled-components'

export default function ProfileModal({ profile, setProfile }) {
    // const handleSubmit = e => e.preventDefault()
    const [information, setInformation] = useState()
    const accountId = 3 // temporary


    useEffect(() => {
        fetch('https://doctor-rota-spring-develop.herokuapp.com/account', {
            mode: 'cors',
            method: "GET",
            headers: {
                'Access-Control-Allow-Origin': '*',
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => setInformation(data.accounts.filter((account) => account.id == 1)[0]))
    }, [])

    const onChange = (e) => {
        setInformation({ ...information, [e.target.name]: e.target.value });
    };

    function handleSubmit(e) {
        e.preventDefault()
        
        try {
            fetch(`https://doctor-rota-spring-develop.herokuapp.com/account/${accountId}`, {
                mode: 'cors',
                method: 'PATCH',
                headers: {
                    'Access-Control-Allow-Origin': '*',
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                    "Access-Control-Allow-Credentials": true
                },
                body: JSON.stringify({

                }),
            })
                .then(response => response.json())
                .then(data => console.log(data))
        } catch (error) {
            console.log(error)
        }

        setProfile(false)
    }

    const handleIcon = () => {

    }


    return (
        <>
            <Modal show={profile}>
                <ModalContainer>
                    <ModalTitle className="my-5">Profile</ModalTitle>
                    <form id="profile" action="#" onSubmit={handleSubmit}>
                        <div className="d-block">

                            <div className="d-flex align-items-center my-3">
                                <Label className="d-flex me-3">
                                    <i className="bi bi-person-fill" style={{ fontSize: '30px' }} />
                                </Label>
                                <RowInfo className="d-flex mb-0">
                                    {

                                        information != undefined &&
                                        information.username
                                    }
                                </RowInfo>
                            </div>

                            <div className="d-flex align-items-center my-3">
                                <Label className="d-flex me-3">
                                    <i className="bi bi-person-badge-fill" style={{ fontSize: '30px' }} />
                                </Label>
                                <RowInfo className="d-flex mb-0">
                                    {
                                        information != undefined &&
                                        information.doctorId
                                    }
                                </RowInfo>
                            </div>

                            <div className='d-block'>
                                <div className="d-flex align-items-center my-3">
                                    <Label className="d-flex me-3">
                                        <i className="bi bi-envelope-fill" style={{ fontSize: '30px' }} />
                                    </Label>
                                    {
                                        information != undefined &&
                                        <Input type="email"
                                            name='email'
                                            value={information.email}
                                            autoComplete='off'
                                            onChange={onChange} />

                                    }
                                    <LockIcon className="bi bi-unlock-fill ms-2" />
                                </div>

                                <div className="d-flex align-items-center my-3">
                                    <Label className="d-flex me-3">
                                        <i className="bi bi-telephone-fill" style={{ fontSize: '30px' }} />
                                    </Label>
                                    {
                                        information != undefined &&
                                        <Input type="tel"
                                            name='phone'
                                            value={information.phone}
                                            autoComplete='off'
                                            onChange={onChange} />

                                    }
                                    <LockIcon className="bi bi-lock-fill ms-2" />
                                </div>
                            </div>
                            <div className="d-flex justify-content-center my-5">
                                <CloseButton className="m-2" onClick={() => setProfile(false)}>
                                    Close
                                </CloseButton>
                                <ConfirmButton className="m-2" onClick={() => setProfile(false)}>
                                    Update
                                </ConfirmButton>
                            </div>
                        </div>
                    </form>
                </ModalContainer>
            </Modal>
        </>
    )
}

const ModalContainer = styled.div`
    width: 80%;
    margin: 0 auto;
`

const ModalTitle = styled.h1`
    font-size: 32px;
    font-weight: bold;
    color: #035eb8;

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

const Label = styled.label`
    color: #035eb8;
    font-weight: bold;
`

const RowInfo = styled.p`
    font-size: 16px;
    color: #035eb8;
    margin-bottom: 0px;
`

const Input = styled.input`
    font-size: 16px !important;
    width: 100%;
    height: 36px;
    border: none;
    border-radius: 5px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
`

const LockIcon = styled.i`
    font-size: 20px;
    cursor: pointer;
`