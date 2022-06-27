import React from 'react'

import styled from 'styled-components'

import '../css/general.css'

export default function SignIn(){
    return (
        <LoginCard>
            <Container>
                <form id="signin_form" action="#" method="post">
                    <div className="mb-3">
                        <Label for="account">Email:</Label>
                        <Input id="account" type="text" className="mb-3"></Input>

                        <Label for="password">Password:</Label>
                        <Input id="password" type="password" className="mb-3"></Input>
                    </div>

                    <div className="d-flex justify-content-center mb-2">
                        <LoginBtn>Submit</LoginBtn>
                    </div>
                    <div className="d-flex justify-content-center mb-2">
                        <Link href="#">Forget the password?</Link>
                    </div>

                    <div>
                        <p>Don't have an account? <Link href="#">Sign up</Link></p>
                    </div>
                </form>
            </Container>
        </LoginCard>
    )
}

const LoginCard = styled.div`
    margin: 50px auto 0 auto;
    max-width: 468px;
    min-height: 575px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
    border-radius: 5px;
    background-color: #EDFCF9;
`

const Container = styled.div`
    margin: 0 auto;
    width: 70%;
    padding-top: 100px;
`

const Input = styled.input`
    font-size: 18px;
    width: 100%;
    border: none;
    border-radius: 5px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
`

const Label = styled.label`
    font-size: 20px;
    color: #168082;
    font-weight: bold;
`

const LoginBtn = styled.button`
    padding: 5px 25px;
    color: white;
    font-size: 18px;
    font-weight: bold;
    background-color: #168082;
    border: none;
    border-radius: 5px;
`

const Link = styled.a`
    text-decoration: none;
`

