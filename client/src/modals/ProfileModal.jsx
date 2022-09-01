import React, { useState, useEffect } from 'react'

import { useUrl } from '../contexts/UrlContexts' 

import FormInput from '../components/FormInput';

import { Modal } from 'react-bootstrap'

import styled from 'styled-components'

export default function ProfileModal({ profile, setProfile }) {
    const auth = JSON.parse(localStorage.getItem('auth'))
    const { getUrl } = useUrl()

    const url =  getUrl()

    const inputs = [
        {
            id: 1,
            name: "email",
            type: "email",
            placeholder: "Email",
            errorMessage:
                "Invalid Email!",
            label: "Email"
        },{
            id: 2,
            name: "phone",
            type: "tel",
            placeholder: "Phone number",
            errorMessage:
                "Invalid Phone number!",
            label: "Phone number"
        },
    ];


    const [information, setInformation] = useState({
        email: "",
        phone: ""
    })
    const [initial, setInitial] = useState({
        email: "",
        phone: ""
    })

    useEffect(() => {
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
            .then(result => {
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
    };

    function handleSubmit(e) {
        e.preventDefault()

        try {
            if(url != undefined){
                fetch(`${url}account/${auth.id}?phone=${information.phone}&email=${information.email}`, {
                    mode: 'cors',
                    method: 'PATCH',
                    headers: {
                        'Access-Control-Allow-Origin': '*',
                        'Content-Type': 'application/json',
                        'Accept': 'application/json',
                        "Access-Control-Allow-Credentials": true,
                        'token': auth.token
                    }
                })
            }
            setProfile(false)
        } catch (error) {
            console.log(error)
        }
    }

    function handleCancel(){
        setInformation({
            email: initial.email,
            phone: initial.phone,
            username: initial.username,
            doctorId: initial.doctorId
        })
        setProfile(false)
    }

    return (
        <>
            <Modal show={profile}>
                <ModalContainer>
                    <ModalTitle className="my-5">Profile</ModalTitle>
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

                            <form id="profile_form" onSubmit={handleSubmit}>
                                <div className="mb-4">
                                    {inputs.map((input) => (
                                        <div key={input.id} className="d-flex align-items-center my-3">
                                            <FormInput
                                                key={input.id}
                                                {...input}
                                                value={information[input.name]}
                                                display="false"
                                                icon="true"
                                                onChange={onChange}
                                            />
                                        </div>
                                    ))}
                                </div>
                                <div className="d-flex justify-content-center my-3">
                                    <CloseButton type="button" className="m-2" onClick={handleCancel}>
                                        Close
                                    </CloseButton>
                                    <ConfirmButton type="sunbit" className="m-2">
                                        Update
                                    </ConfirmButton>
                                </div>
                            </form>
                        </div>
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