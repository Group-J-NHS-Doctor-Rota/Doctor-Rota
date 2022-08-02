import React, { useState } from 'react'

import FormInput from '../components/FormInput';

import { useNavigate } from 'react-router-dom';

import styled from 'styled-components'

import '../css/general.css'

export default function SignIn() {
    const [values, setValues] = useState({
        username: "",
        password: ""
    });

    const [reset, setReset] = useState(false)
    const [status, setStatus] = useState(false)

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
            name: "password",
            type: "password",
            placeholder: "Password",
            errorMessage:
                "Password should be 8-20 characters and include at least 1 letter, 1 number and 1 special character!",
            label: "Password",
            pattern: `^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$`,
            required: true,
        }
    ];

    const resetInputs = [
        {
            id: 1,
            name: "username",
            type: "text",
            placeholder: "Username",
            errorMessage:
                "If you forget the username, please contact admin!",
            label: "Username",
            pattern: "^[A-Za-z0-9]{3,16}$",
            required: true,
        },
        {
            id: 2,
            name: "email",
            type: "email",
            placeholder: "Email",
            errorMessage:
                "Invalid email!",
            label: "Email",
            required: true,
        }
    ];

    const handleSubmit = (e) => {
        e.preventDefault();

        reset === false && navigate('/')
    };

    const onChange = (e) => {
        setValues({ ...values, [e.target.name]: e.target.value });
    };

    const navigate = useNavigate()

    return (
        <>
            <LoginCard>
                <Container>
                    <div className="d-flex justify-content-between">

                        {reset === false &&
                            (<div><Title>Login</Title></div>)
                            ||
                            reset === true &&
                            (<div><Title>Reset</Title></div>)
                        }
                        <img src="https://www.nbt.nhs.uk/themes/custom/nbt/logo.png"
                            alt="logo" width="120px" />
                    </div>

                    <form id="signin_form" action="#" method="post" onSubmit={handleSubmit}>
                        {reset === false &&
                            (<div>
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

                                <div className="d-flex justify-content-center mb-3">
                                    <LoginBtn type="submit">LOGIN</LoginBtn>
                                </div>
                            </div>)
                            ||
                            reset === true &&
                            (
                                <div>
                                    <div className="mb-4">

                                        {resetInputs.map((input) => (
                                            <FormInput
                                                key={input.id}
                                                {...input}
                                                value={values[input.name]}
                                                onChange={onChange}
                                            />
                                        ))}
                                    </div>

                                    <div className="d-flex justify-content-center mb-3">
                                        <LoginBtn type="submit" onClick={() => setStatus(true)}>Send Link</LoginBtn>
                                    </div>
                                </div>
                            )
                        }
                        <div className="d-flex justify-content-center mb-3"
                            onClick={() => setReset(!reset)}>
                            {reset === false &&
                                (<Link>Forgot your password?</Link>)
                                ||
                                (<Link>Login</Link>)
                            }
                        </div>

                        <div>
                            <p>Don't have an account? <Link
                                onClick={() => navigate('/signup')}
                                style={{ cursor: 'pointer' }}>Contact Admin</Link></p>
                        </div>
                    </form>

                </Container>
            </LoginCard >

            {
                status === true &&
                (
                    <>
                        <OnSubmitMessage>
                            <i className="bi bi-info-square mx-2" />
                            Success! Please follow the instructions in the email.
                        </OnSubmitMessage>
                    </>
                )
            }


        </>
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

const Link = styled.a`
    text-decoration: none;
    color: #035eb8;
    cursor: pointer;
`

const OnSubmitMessage = styled.div`
    margin: 20px auto 0 auto;
    max-width: 268px;
    text-align: center;
    color: #035eb8;
    border: 1px solid rgba(3, 94, 184, 1);
    background-color: rgba(3, 94, 184, 0.3);
    border-radius: 4px;
`