import styled from "styled-components";
import { useNavigate } from "react-router-dom";

const SignInPage = () => {
    const navigate = useNavigate();
    return (
        <>
            <LoginDialog>
                <LoginContent>
                    <div className="d-flex justify-content-between">
                        <LoginTitle>Login</LoginTitle>
                        <img src="https://www.nbt.nhs.uk/themes/custom/nbt/logo.png"
                            alt="logo" width="120px" />
                    </div>
                    <form id="signin_form" action="#" method="post">
                        <div className="mb-3">
                            <LoginLabel for="username" className="mb-1">Username</LoginLabel>
                            <LoginInput id="username" type="text" className="mb-3" autoFocus />
                            <LoginLabel for="password" className="mb-1">Password</LoginLabel>
                            <LoginInput id="password" type="password" className="mb-3" />
                        </div>

                        <div className="d-flex justify-content-center mb-2">
                            <LoginButton onClick={() => navigate('')} >LOGIN</LoginButton>
                        </div>
                    </form>
                    <div className="d-flex justify-content-center mb-2">
                        <LoginLink onClick={() => navigate('password-reset')}>
                            Forgot your password?
                        </LoginLink>
                    </div>
                    <div>
                        <p className="mt-5">Don't have an account? &nbsp;Please&nbsp;
                            <LoginLink onClick={() => navigate('/')}>
                                contact admin
                            </LoginLink>.
                        </p>
                    </div>
                </LoginContent>
            </LoginDialog>
        </>
    )
}

export default SignInPage;

const LoginDialog = styled.div`
    margin: 100px auto 0 auto;
    max-width: 468px;
    min-height: 500px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
    border-radius: 5px;
    background-color: #EDFCF9;
`

const LoginContent = styled.div`
    margin: 0 auto;
    width: 75%;
    padding-top: 40px;
`

const LoginTitle = styled.h1`
    font-size: 32px;
    font-weight: bold;
    color: #168082;

    @media (max-width:575px) {
        font-size: 24px;
    }
`

const LoginInput = styled.input`
    font-size: 18px;
    width: 100%;
    height: 36px;
    border: none;
    border-radius: 5px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
`

const LoginLabel = styled.label`
    font-size: 20px;
    color: #168082;
    font-weight: bold;
`

const LoginButton = styled.button`
    padding: 5px 25px;
    color: white;
    font-size: 18px;
    font-weight: bold;
    background-color: #168082;
    border: none;
    border-radius: 5px;
`

const LoginLink = styled.a`
    
    text-decoration: none;
    cursor: pointer;
    font-weight: bold;
`