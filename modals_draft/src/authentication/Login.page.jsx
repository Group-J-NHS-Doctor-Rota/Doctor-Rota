import './Login.styles.css'
import { Modal, Button, Form } from "react-bootstrap";

const Login = () => {
    return (
        <div>
            <Modal show style={{
                width: '568px',
                height: '675px',
                backgroundColor: '#EDFCF9',
                boxShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
                borderRadius: '10px',
            }} className='font-popup screen-center'>
                <div className='nhs-logo'></div>
                <Modal.Header className="modal-heading">
                    <Modal.Title className='font-title'>Log in</Modal.Title>
                </Modal.Header>
                <Modal.Body className='modal-body'>
                    <Form>
                        <Form.Group
                            className="mb-3 div-1" controlId="exampleForm.ControlInput1">
                            <Form.Label>Username</Form.Label>
                            <Form.Control
                                type="email"
                                placeholder="&nbsp;name@nbt.nhs.uk"
                                className='input-box'
                                autoFocus />
                        </Form.Group>
                        <Form.Group
                            className="mb-3 div-1" controlId="exampleForm.ControlInput1">
                            <Form.Label>Password</Form.Label>
                            <Form.Control
                                type="password"
                                className='input-box' />
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Button className='button-bottom btn-middle mt2'>LOGIN</Button>
                <div className='go-center'><a href='#'>Forgot your password?</a></div>
                <div className='warning1'>Don't have an account? <a href='#'>Sign up</a></div>
            </Modal>
        </div>
    )
}


export default Login;

// https://www.nbt.nhs.uk/themes/custom/nbt/logo.png