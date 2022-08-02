import React, { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom';
import { useUrl } from '../contexts/UrlContexts'
import FormInput from '../components/FormInput';
import { Modal } from 'react-bootstrap'

import styled from 'styled-components'

const ResetPasswordModal = ({ reset, setReset }) => {
    const { getUrl } = useUrl()
    const url = getUrl()

    const navigate = useNavigate();

    const [values, setValues] = useState({
        password: "",
        confirmPassword: "",
    });

    const onChange = (e) => {
        setValues({ ...values, [e.target.name]: e.target.value });
    };

    const inputs = [
        {
            id: 1,
            name: "password",
            type: "password",
            placeholder: "Password",
            errorMessage:
                "Password should be 8-20 characters and include at least 1 letter, 1 number and 1 special character!",
            label: "Password",
            pattern: `^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$`,
            required: true,
        },
        {
            id: 2,
            name: "confirmPassword",
            type: "password",
            placeholder: "Confirm Password",
            errorMessage: "Passwords don't match!",
            label: "Confirm Password",
            pattern: values.password,
            required: true,
        },
    ];

    const handleSubmit = e => {
        e.preventDefault();

        navigate('/')
    };

    const handleCancel = e => {
        setReset(false)
    }

    return (
        <>
            <Modal show={reset}>
                <ModalContainer>
                    <ModalTitle className="my-5">Reset Password</ModalTitle>
                    <form id="reset_password" onSubmit={handleSubmit}>
                        <div className="mb-4">
                            {inputs.map((input) => (
                                <FormInput
                                    key={input.id}
                                    {...input}
                                    value={values[input.name]}
                                    onChange={onChange}
                                />
                            ))}
                        </div>
                        <div className="d-flex justify-content-center my-5">
                            <CloseButton type="button" className="m-2" onClick={handleCancel}>
                                Close
                            </CloseButton>
                            <ConfirmButton type="sunbit" className="m-2">
                                Reset
                            </ConfirmButton>
                        </div>
                    </form>
                </ModalContainer>
            </Modal>
        </>
    )

}

export default ResetPasswordModal

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