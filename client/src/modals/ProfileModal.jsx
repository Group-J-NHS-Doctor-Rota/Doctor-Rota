import React, { useState, useEffect } from 'react'

import { useUrl } from '../contexts/UrlContexts' 

import { Modal } from 'react-bootstrap'

import styled from 'styled-components'

export default function ProfileModal({ profile, setProfile }) {
    const auth = JSON.parse(localStorage.getItem('auth'))
    const { getUrl } = useUrl()

    const url =  getUrl()

    const [information, setInformation] = useState({
        email: "",
        phone: ""
    })
    const [initial, setInitial] = useState({
        email: "",
        phone: ""
    })
    const accountId = 1

    const [errorMsg, setErrorMsg] = useState({
        email_invalid: false,
        phone_invalid: false
    })

    useEffect(() => {
        if(url != undefined){
            fetch(`${url}account`, {
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
                const result = data.accounts.filter((account) => account.id == 1)[0]
                setInformation(result)
                setInitial({
                    email: result.email,
                    phone: result.phone,
                    username: result.username,
                    doctorId: result.doctorId
                })
            })
        }
    }, [])

    const onChange = (e) => {
        setInformation({ ...information, [e.target.name]: e.target.value });
        checkInputValidation(e.target.name, e.target.value)
    };

    function checkInputValidation(name, value){
        const numberOfValue = value.length
        
        if(name == "email"){
            if(value.slice(numberOfValue-4, numberOfValue) == ".com" && value.includes("@")){
                setErrorMsg({... errorMsg, ["email_invalid"]: false})
            }else{
                setErrorMsg({... errorMsg, ["email_invalid"]: true})
            }
        }
        if(name == "phone"){
            const pattern = /^[0-9]+$/
            if(!pattern.test(value)){
                setErrorMsg({... errorMsg, ["phone_invalid"]: true})
            }else{
                setErrorMsg({... errorMsg, ["phone_invalid"]: false})
            }
        }
    }

    function handleSubmit(e) {
        e.preventDefault()

        if(errorMsg.email_invalid != true && errorMsg.phone_invalid != true){
            try {
                if(url != undefined){
                    fetch(`${url}account/${accountId}?phone=${information.phone}&email=${information.email}`, {
                        mode: 'cors',
                        method: 'PATCH',
                        headers: {
                            'Access-Control-Allow-Origin': '*',
                            'Content-Type': 'application/json',
                            'Accept': 'application/json',
                            "Access-Control-Allow-Credentials": true
                        }
                    })
                    .then(response => console.log(response))
                }

                setProfile(false)
            } catch (error) {
                console.log(error)
            }
        }else{
            setProfile(true)
        }

    }

    function handleCancel(){
        setErrorMsg({
            email_invalid: false,
            phone_invalid: false
        })
        setInformation({
            email: initial.email,
            phone: initial.phone,
            username: initial.username,
            doctorId: initial.doctorId
        })
        setProfile(false)
    }

    const handleIcon = () => {

    }

    return (
        <>
            <Modal show={profile}>
                <ModalContainer>
                    <ModalTitle className="my-5">Profile</ModalTitle>
                    <form id="profile" onSubmit={handleSubmit}>
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
                                {
                                    errorMsg.email_invalid &&
                                    <div>
                                        <div className="d-flex align-items-center mt-3">
                                            <Label htmlFor="email" className="d-flex me-3">
                                                <i className="bi bi-envelope-fill" style={{ fontSize: '30px' }} />
                                            </Label>
                                            {
                                                information != undefined &&
                                                <Input 
                                                    type="email"
                                                    name='email'
                                                    value={information.email}
                                                    autoComplete='off'
                                                    onChange={onChange} 
                                                />
                                            }
                                            <LockIcon className="bi bi-unlock-fill ms-2" />
                                        </div>
                                        <div>
                                            <ErrorMessage>The email format is wrong</ErrorMessage>
                                        </div>
                                    </div>
                                    ||
                                    <div className="d-flex align-items-center my-3">
                                        <Label htmlFor="email" className="d-flex me-3">
                                            <i className="bi bi-envelope-fill" style={{ fontSize: '30px' }} />
                                        </Label>
                                        {
                                            information != undefined &&
                                            <Input 
                                                type="email"
                                                name='email'
                                                value={information.email}
                                                autoComplete='off'
                                                onChange={onChange} 
                                            />
                                        }
                                        <LockIcon className="bi bi-unlock-fill ms-2" />
                                    </div>
                                }

                                {
                                    errorMsg.phone_invalid &&
                                    <div>
                                        <div className="d-flex align-items-center mt-3">
                                            <Label htmlFor="phone" className="d-flex me-3">
                                                <i className="bi bi-telephone-fill" style={{ fontSize: '30px' }} />
                                            </Label>
                                            {
                                                information != undefined &&
                                                <Input 
                                                    type="tel"
                                                    name='phone'
                                                    value={information.phone}
                                                    autoComplete='off'
                                                    onChange={onChange} 
                                                />

                                            }
                                            <LockIcon className="bi bi-lock-fill ms-2" />
                                        </div>
                                        <div>
                                            <ErrorMessage>The phone format is wrong</ErrorMessage>
                                        
                                        </div>
                                    </div>
                                    ||
                                    <div className="d-flex align-items-center my-3">
                                        <Label htmlFor="phone" className="d-flex me-3">
                                            <i className="bi bi-telephone-fill" style={{ fontSize: '30px' }} />
                                        </Label>
                                        {
                                            information != undefined &&
                                            <Input 
                                                type="tel"
                                                name='phone'
                                                value={information.phone}
                                                autoComplete='off'
                                                onChange={onChange} 
                                            />
                                        }
                                        <LockIcon className="bi bi-lock-fill ms-2" />
                                    </div>
                                }
                            </div>
                            <div className="d-flex justify-content-center my-5">
                                <CloseButton type="button" className="m-2" onClick={handleCancel}>
                                    Close
                                </CloseButton>
                                <ConfirmButton type="sunbit" className="m-2">
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

const ErrorMessage = styled.p`
    font-size: 12px;
    padding: 3px;
    color: red;
`