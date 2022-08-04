import React, { useState } from 'react'

import FormInput from '../components/FormInput';

import { useAuth } from '../contexts/AuthContext'

import { useNavigate } from 'react-router-dom';

import styled from 'styled-components'

import '../css/general.css'

export default function SignIn() {
    const { login } = useAuth() 

    const [values, setValues] = useState({
        username: "",
        password: ""
    });

    const inputs = [
        {
            id: 1,
            name: "username",
            type: "text",
            placeholder: "Username",
            errorMessage:
                "Incorrect username format",
            label: "Username",
            pattern: "[a-zA-Z \./']+",
            required: true,
        },
        {
            id: 2,
            name: "password",
            type: "password",
            placeholder: "Password",
            errorMessage:
                "Incorrect password format",
            label: "Password",
            pattern: `^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$`,
            required: true,
        }
    ];

    function handleSubmit(e){
        e.preventDefault();

        login(values.username, values.password)
        // can try again

        setTimeout(() => {
            navigate('/')
        }, 1000);
    };

    const onChange = (e) => {
        setValues({ ...values, [e.target.name]: e.target.value });
    };

    const navigate = useNavigate()

    return (
        <LoginCard>
            <Container>
                <div className="d-flex justify-content-between">
                    <Title>Login</Title>
                    <img src="https://www.nbt.nhs.uk/themes/custom/nbt/logo.png"
                        alt="logo" width="120px" />
                </div>
                <form id="signin_form" action="#" method="post" onSubmit={handleSubmit}>
                    <div className="mb-4">
                        {/* <Title className="mb-5">Login</Title> */}

                        {inputs.map((input) => (
                            <FormInput
                                key={input.id}
                                {...input}
                                value={values[input.name]}
                                onChange={onChange}
                            />
                        ))}
                    </div>

                    <div className="d-flex justify-content-center mb-3">
                        <LoginBtn type="submit">LOGIN</LoginBtn>
                    </div>
                    <div className="d-flex justify-content-center mb-3"
                        onClick={() => navigate('/reset-password')}>
                        <Link href="#">Forgot your password?</Link>
                    </div>

                    <div className="mb-2">
                        <p>Don't have an account? <Link
                            style={{ cursor: 'pointer' }}>Contact Admin</Link></p>
                    </div>
                </form>
            </Container>
        </LoginCard >
    )
}

const LoginCard = styled.div`
    margin: 90px auto 0 auto;
    max-width: 468px;
    min-height: 500px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
    border-radius: 5px;
    background-color: #f5f9fe;
`

const Container = styled.div`
    margin: 0 auto;
    width: 70%;
    padding-top: 70px;
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

const Link = styled.a`
    text-decoration: none;
    color: #035eb8;
`

