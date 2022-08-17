import React, { useState } from 'react'

import FormInput from '../components/FormInput';

import { useAuth } from '../contexts/AuthContext'

import { useNavigate } from 'react-router-dom';

import styled from 'styled-components'

import { useUrl } from '../contexts/UrlContexts'

import '../css/general.css'

export default function SignIn() {

    const { getUrl } = useUrl()
    const url = getUrl()
    
    const { login } = useAuth() 

    const [values, setValues] = useState({
        username: "",
        password: ""
    });

    const [information, setInformation] = useState({
        username: "",
        email: ""
    });

    const [reset, setReset] = useState(false)
    const [status, setStatus] = useState(false)
    const [contact, setContact] = useState(false)

    const [error, setError] = useState(false)

    const inputs = [
        {
            id: 1,
            name: "username",
            type: "text",
            placeholder: "Username",
            label: "Username",
            required: true,
        },
        {
            id: 2,
            name: "password",
            type: "password",
            placeholder: "Password",
            label: "Password",
            required: true,
        }
    ];

    const resetInputs = [
        {
            id: 3,
            name: "username",
            type: "text",
            placeholder: "Username",
            errorMessage:
                "If you forget the username, please contact admin!",
            label: "Username",
            pattern: "[a-zA-Z \./']+",
            required: true,
        },
        {
            id: 4,
            name: "email",
            type: "email",
            placeholder: "Email",
            errorMessage:
                "Invalid email!",
            label: "Email",
            required: true,
        }
    ];

    const handleResetSubmit = e => {
        e.preventDefault()

        if (reset === true) {
            if (information !== undefined) {
                // console.log(information)
                // when can not find email or username?
                try {
                    if (url !== undefined) {
                        fetch(`${url}passwordreset?username=${information.username}&email=${information.email}`, {
                            mode: 'cors',
                            method: 'PATCH',
                            headers: {
                                'Access-Control-Allow-Origin': '*',
                                'Content-Type': 'application/json',
                                'Accept': 'application/json'
                            }
                        })
                        .then(response => {
                            console.log(response)
                            // CORS problem here
                        })
                    }
                } catch (error) {
                    console.log(error)
                }
            }
        }
    }

    function handleSubmit(e){
        e.preventDefault();

        login(values.username, values.password)
        
        setTimeout(() => {
            const auth = JSON.parse(localStorage.getItem('auth'))

            if(auth){
                navigate('/')
            }else{
                setError(true)
            }
        }, 1000);
    };

    
    const onLoginChange = (e) => {
        setValues({ ...values, [e.target.name]: e.target.value });
    };

    const onResetChange = (e) => {
        setInformation({ ...information, [e.target.name]: e.target.value });
    };

    const navigate = useNavigate()

    return (
        <div>
            <div>
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
                                {
                                //login form
                                reset === false &&
                                (
                                    <form id="signin_form" action="#" method="post" onSubmit={handleSubmit}>
                                        <div>
        
                                            {inputs.map((input) => (
                                                <FormInput
                                                    key={input.id}
                                                    {...input}
                                                    value={values[input.name]}
                                                    display="true"
                                                    onChange={onLoginChange}
                                                />
                                            ))}
                                        </div>

                                        {
                                            error &&
                                            <ErrorMessage>account is not match with password</ErrorMessage>
                                        }
        
                                        <div className="d-flex justify-content-center mt-3">
                                            <LoginBtn type="submit">LOGIN</LoginBtn>
                                        </div>
                                    </form>
                                )
                                ||
                                // reset form
                                reset === true &&
                                (
                                    <form id="reset_form" action="#" method="post" onSubmit={handleResetSubmit}>
                                        <div className="mb-4">
    
                                            {resetInputs.map((input) => (
                                                <FormInput
                                                    key={input.id}
                                                    {...input}
                                                    value={information[input.name]}
                                                    display="true"
                                                    onChange={onResetChange}
                                                />
                                            ))}
                                        </div>
    
                                        <div className="d-flex justify-content-center mb-3">
                                            <LoginBtn type="submit"
                                                onClick={() => {
                                                    setStatus(true)
                                                    setTimeout(() => {
                                                        setStatus(false)
                                                    }, 10000)
                                                }}>Send Link</LoginBtn>
                                        </div>
                                    </form>
                                )
                            }
                            <div className="d-flex justify-content-center my-3"
                                onClick={() => setReset(!reset)}>
                                {reset === false &&
                                    (<Link>Forgot your password?</Link>)
                                    ||
                                    (<Link>Login</Link>)
                                }
                            </div>
    
                            <div className="mb-5">
                                <p>Don't have an account? <Link
                                    onClick={() => {
                                        setContact(true)
                                        setTimeout(() => {
                                            setContact(false)
                                        }, 5000)
                                    }}
                                    style={{ cursor: 'pointer' }}>Contact Admin</Link></p>
                            </div>
    
                    </Container>
                </LoginCard >
    
                {
                    status === true &&
                    (
                        <div>
                            <OnClickMessage>
                                <i className="bi bi-info-square mx-2" />
                                Success! Please follow the instructions in the email.
                            </OnClickMessage>
                        </div>
                    )
                }
    
                {
                    contact === true &&
                    (
                        <div>
                            <OnClickMessage>
                                <i className="bi bi-info-square mx-2" />
                                Please go to admin office or email admin.
                            </OnClickMessage>
                        </div>
                    )
                }
            </div>
        </div>
    )
}

const LoginCard = styled.div`
    margin: 90px auto 0 auto;
    max-width: 468px;
    min-height: 520px;
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
    cursor: pointer;
`

const OnClickMessage = styled.div`
    margin: 20px auto 0 auto;
    max-width: 268px;
    text-align: center;
    color: #035eb8;
    border: 1px solid rgba(3, 94, 184, 1);
    background-color: #f5f9fe;
    border-radius: 4px;
`

const ErrorMessage = styled.p`
    font-size: 12px;
    padding: 3px;
    color: red;
`