import React, { useState, useEffect } from 'react'
import { useUrl } from '../contexts/UrlContexts'
import FormInput from '../components/FormInput';
import { Modal } from 'react-bootstrap'

import styled from 'styled-components'

const ResetPasswordModal = ({ reset, setReset }) => {
    const { getUrl } = useUrl()
    const url = getUrl()

    const auth = JSON.parse(localStorage.getItem('auth'))

    const [values, setValues] = useState({
        oldpassword: "",
        password: "",
        confirmPassword: "",
    });

    const [errorMsg, setErrorMsg] = useState({
        oldPassword_incorrect: false,
        new_old_password_same: false
    })

    const onChange = (e) => {
        setValues({ ...values, [e.target.name]: e.target.value });
    };

    const inputs = [
        {
            id: 1,
            name: "oldpassword",
            type: "password",
            placeholder: "Old Password",
            errorMessage:
                "Password should be 8-20 characters and include at least 1 letter, 1 number and 1 special character!",
            label: "Old Password",
            pattern: `^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$`,
            required: true,
        },
        {
            id: 2,
            name: "password",
            type: "password",
            placeholder: "Password",
            errorMessage:
                "Password should be 8-20 characters and include at least 1 letter, 1 number and 1 special character!",
            label: "New Password",
            pattern: `^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$`,
            required: true,
        },
        {
            id: 3,
            name: "confirmPassword",
            type: "password",
            placeholder: "Confirm Password",
            errorMessage: "Passwords don't match!",
            label: "Confirm Password",
            pattern: values.password,
            required: true,
        },
    ];

    const accountId = auth.id

    const handleSubmit = e => {
        e.preventDefault();

        try {
            if (url != undefined) {
                fetch(`${url}password?accountId=${accountId}`, {
                    mode: 'cors',
                    method: 'PATCH',
                    headers: {
                        'Access-Control-Allow-Origin': '*',
                        'Content-Type': 'application/json',
                        'Accept': 'application/json',
                        "Access-Control-Allow-Credentials": true,
                        "oldPassword": values.oldpassword,
                        "newPassword": values.password,
                        'token': auth.token
                    }
                })
                .then(response => {
                    if(response.status == 409){
                        setErrorMsg({
                            oldPassword_incorrect: true,
                            new_old_password_same: false
                        })
                    }else if(response.status == 405){
                        setErrorMsg({
                            oldPassword_incorrect: false,
                            new_old_password_same: true
                        })
                    }else{
                        setValues({
                            oldpassword: "",
                            password: "",
                            confirmPassword: "",
                        })
                        setErrorMsg({
                            oldPassword_incorrect: false,
                            new_old_password_same: false
                        })
                        setReset(false)
                    }
                })
            }
        } catch (error) {
            console.log(error)
        }
    };

    const handleCancel = e => {
        setValues({
            oldpassword: "",
            password: "",
            confirmPassword: "",
        })
        setErrorMsg({
            oldPassword_incorrect: false,
            new_old_Password_same: false
        })
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
                                    display="true"
                                    onChange={onChange}
                                />
                            ))}
                        </div>

                        {
                            errorMsg.oldPassword_incorrect &&
                            <div>
                                <ErrorMessage>Incorrect old password </ErrorMessage>
                            </div>
                            ||
                            errorMsg.new_old_password_same &&
                            <div>
                                <ErrorMessage>New password cannot be the same as old password </ErrorMessage>
                            </div>
                        }

                        <div className="d-flex justify-content-center my-3">
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

const ErrorMessage = styled.p`
    font-size: 12px;
    padding: 3px;
    color: red;
`
