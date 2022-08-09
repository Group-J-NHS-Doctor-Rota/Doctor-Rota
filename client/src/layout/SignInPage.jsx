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
    // errorMessage: "Incorrect username format",
    // pattern: "[a-zA-Z \./']+",
    // pattern: `^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$`,
    // errorMessage: "Incorrect password format",

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

    const handleSubmit = e => {
        e.preventDefault()

        reset === false && navigate('/')

        if (reset === true) {

            if (information !== undefined) {
                try {
                    if (url !== undefined) {
                        fetch(`${url}passwordreset?username=${information.username}&email=${information.email}`, {
                            mode: 'cors',
                            method: 'PATCH',
                            headers: {
                                'Access-Control-Allow-Origin': '*',
                                'Content-Type': 'application/json',
                                'Accept': 'application/json',
                                "Access-Control-Allow-Credentials": true
                            }
                        })
                            .then(response => {
                                console.log(response)
                                console.log(response.status)
                            })
                    }
                } catch (error) {
                    console.log(error)
                }
            }
        }

    async function handleSubmit(e){
        e.preventDefault();

        login(values.username, values.password)
        
        setTimeout(() => {
            const auth = JSON.parse(localStorage.getItem('auth'))

            if(auth){
                navigate('/')
            }else{
                setError(true)
            }
        }, 600);
    };

    const onLoginChange = (e) => {
        setValues({ ...values, [e.target.name]: e.target.value });
    };

    const onResetChange = (e) => {
        setInformation({ ...information, [e.target.name]: e.target.value });
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
                        {
                            //login form
                            reset === false &&
                            (<div>
                                <div className="mb-4">

                                    {inputs.map((input) => (
                                        <FormInput
                                            key={input.id}
                                            {...input}
                                            value={values[input.name]}
                                            onChange={onLoginChange}
                                        />
                                    ))}
                                </div>

                                <div className="d-flex justify-content-center mb-3">
                                    <LoginBtn type="submit">LOGIN</LoginBtn>
                                </div>
                            </div>)



                            ||


                            // reset form
                            reset === true &&
                            (
                                <div>
                                    <div className="mb-4">

                                        {resetInputs.map((input) => (
                                            <FormInput
                                                key={input.id}
                                                {...input}
                                                value={information[input.name]}
                                                onChange={onResetChange}
                                            />
                                        ))}
                                    </div>

                                    <div className="d-flex justify-content-center mb-3">
                                        <LoginBtn type="submit"
                                            onClick={async () => {
                                                setStatus(true)
                                                setTimeout(() => {
                                                    setStatus(false)
                                                }, 10000)
                                            }}>Send Link</LoginBtn>
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
                                onClick={async () => {
                                    setContact(true)
                                    // alert("Please go to admin office or email admin")
                                    setTimeout(() => {
                                        setContact(false)
                                    }, 5000)
                                }}
                                style={{ cursor: 'pointer' }}>Contact Admin</Link></p>
                        </div>
                    </form>

                </Container>
            </LoginCard >

            {
                status === true &&
                (
                    <>
                        <OnClickMessage>
                            <i className="bi bi-info-square mx-2" />
                            Success! Please follow the instructions in the email.
                        </OnClickMessage>
                    </>
                )
            }

            {
                contact === true &&
                (
                    <>
                        <OnClickMessage>
                            <i className="bi bi-info-square mx-2" />
                            Please go to admin office or email admin.
                        </OnClickMessage>
                    </>
                )
            }


        </>

        <LoginCard>
            <Container>
                <div className="d-flex justify-content-between">
                    <Title>Login</Title>
                    <img src="https://www.nbt.nhs.uk/themes/custom/nbt/logo.png"
                        alt="logo" width="120px" />
                </div>
                <form id="signin_form" action="#" method="post" onSubmit={handleSubmit}>
                    <div>
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

                    {
                        error &&
                        <ErrorMessage>account is not match with password</ErrorMessage>
                    }

                    <div className="d-flex justify-content-center mt-3">
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
    cursor: pointer;
`

const OnClickMessage = styled.div`
    margin: 20px auto 0 auto;
    max-width: 268px;
    text-align: center;
    color: #035eb8;
    border: 1px solid rgba(3, 94, 184, 1);
    background-color: rgba(3, 94, 184, 0.3);
    border-radius: 4px;
    
const ErrorMessage = styled.p`
    font-size: 12px;
    padding: 3px;
    color: red;
    // display: none;
`