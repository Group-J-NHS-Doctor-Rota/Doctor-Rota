import React, { useState } from 'react'

import styled from 'styled-components'

import '../css/general.css'
import FormInput from '../components/FormInput';
import { useNavigate } from 'react-router-dom';

export default function SignUp() {
    const navigate = useNavigate();

    const [values, setValues] = useState({
        username: "",
        email: "",
        password: "",
        confirmPassword: "",
    });

    const inputs = [
        {
            id: 1,
            name: "username",
            type: "text",
            placeholder: "Username",
            errorMessage:
                "Username should be 3-16 characters and shouldn't include any special character!",
            label: "Username",
            pattern: "^[A-Za-z0-9]{3,16}$",
            required: true,
        },
        {
            id: 2,
            name: "email",
            type: "email",
            placeholder: "Email",
            errorMessage: "It should be a valid email address!",
            label: "Email",
            required: true,
        },
        {
            id: 3,
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
            id: 4,
            name: "confirmPassword",
            type: "password",
            placeholder: "Confirm Password",
            errorMessage: "Passwords don't match!",
            label: "Confirm Password",
            pattern: values.password,
            required: true,
        },
    ];

    const handleSubmit = (e) => {
        e.preventDefault();

        navigate('/')
    };

    const onChange = (e) => {
        setValues({ ...values, [e.target.name]: e.target.value });
    };

    return (
        <LoginCard>
            <Container>
                <div className="d-flex justify-content-between">
                    <Title>Sign up</Title>
                    <img src="https://www.nbt.nhs.uk/themes/custom/nbt/logo.png"
                        alt="logo" width="120px" />
                </div>
                <form id="signin_form" action="/" method="post" onSubmit={handleSubmit}>
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

                    <div className="d-flex justify-content-center mb-3">
                        <LoginBtn type="submit">Submit</LoginBtn>
                    </div>
                </form>
            </Container>
        </LoginCard>
    )
}

const LoginCard = styled.div`
    margin: 90px auto 0 auto;
    max-width: 468px;
    min-height: 525px;
    padding: 0 0 25px 0;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
    border-radius: 5px;
    background-color: #f5f9fe;
`

const Container = styled.div`
    margin: 0 auto;
    width: 70%;
    padding-top: 50px;
`

const Title = styled.h1`
    font-size: 32px;
    font-weight: bold;
    color: #035eb8;

    @media (max-width: 575px){
        font-size: 24px;
    }
`

const LoginBtn = styled.button`
    padding: 5px 25px;
    color: white;
    font-size: 20px;
    font-weight: bold;
    background-color: #035eb8;
    border: none;
    border-radius: 5px;
`

