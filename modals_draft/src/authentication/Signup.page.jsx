import './Signup.styles.css'
import '../Screen.css'
import { Modal, Button, Form } from "react-bootstrap";

const Signup = () => {
    // if (window.matchMedia('(max-width: 500px)').matches) {
    //     return (
    //         <div class='whole-screen'>
    //             <div class='signup-screen'>
    //                 <div className='nhs-logo-screen'></div>
    //                 <h1 class='screen-header'>Sign up</h1>
    //             </div>
    //         </div >
    //     )
    // } else {
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
                    <Modal.Title className='font-title'>Sign up</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group
                            className="mb-3" controlId="exampleForm.ControlInput1">
                            <Form.Label>Username</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="&nbsp;Please enter your name"
                                className='input-box'
                                autoFocus />
                        </Form.Group>
                        <Form.Group
                            className="mb-3" controlId="exampleForm.ControlInput1">
                            <Form.Label>Email</Form.Label>
                            <Form.Control
                                type="email"
                                placeholder="&nbsp;name@nbt.nhs.uk"
                                className='input-box' />
                        </Form.Group>
                        <Form.Group
                            className="mb-3" controlId="exampleForm.ControlInput1">
                            <Form.Label>Password</Form.Label>
                            <Form.Control
                                type="password"
                                className='input-box' />
                        </Form.Group>
                        <Form.Group
                            className="mb-3" controlId="exampleForm.ControlInput1">
                            <Form.Label>Confirm Password</Form.Label>
                            <Form.Control
                                type="password"
                                className='input-box' />
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Button className='button-bottom btn-middle'>Create Account</Button>
                <div className='warning2'>Already have an account? <a href='#'>Log in</a></div>
            </Modal>
        </div>
    )
    // }
}

export default Signup;