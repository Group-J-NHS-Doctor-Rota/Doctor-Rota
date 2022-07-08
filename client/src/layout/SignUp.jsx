import React from 'react'

import styled from 'styled-components'

import '../css/general.css'

export default function SignUp(){
    return (
        <LoginCard>
            <Container>
                <form id="signin_form" action="/" method="post">
                    <div className="mb-4">
                        <Title className="mb-5">Sign up</Title>

                        <Label className="mt-2 mb-1" htmlFor="account">Username:</Label>
                        <Input id="account" type="text" className="mb-3"></Input>

                        <Label className="mt-2 mb-1" htmlFor="password">Password:</Label>
                        <Input id="password" type="password" className="mb-3"></Input>

                        <Label className="mt-2 mb-1" htmlFor="confirm_password">Confirm Password:</Label>
                        <Input id="confirm_password" type="password" className="mb-3"></Input>
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
    margin: 120px auto 0 auto;
    max-width: 468px;
    min-height: 475px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
    border-radius: 5px;
    background-color: #EDFCF9;
`

const Container = styled.div`
    margin: 0 auto;
    width: 70%;
    padding-top: 50px;
`

const Title = styled.h1`
    font-size: 32px;
    font-weight: bold;
    color: #168082;

    @media (max-width: 575px){
        font-size: 24px;
    }
`

const Input = styled.input`
    font-size: 18px;
    width: 100%;
    border: none;
    border-radius: 5px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
`

const Label = styled.label`
    font-size: 16px;
    color: #168082;
    font-weight: bold;
`

const LoginBtn = styled.button`
    padding: 5px 25px;
    color: white;
    font-size: 20px;
    font-weight: bold;
    background-color: #168082;
    border: none;
    border-radius: 5px;
`

const Link = styled.a`
    text-decoration: none;
    color: #168082;
`
