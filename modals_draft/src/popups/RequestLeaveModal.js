import { useState } from 'react';
import { Button, Modal, Form } from 'react-bootstrap';

const RequestLeaveModal = () => {
    const [show, popup] = useState(false);
    const modalOpen = () => popup(true);
    const modalClose = () => popup(false);

    return (
        <div>
            <Button variant='primary' size='xxl' onClick={modalOpen}>Request Leave</Button>
            <Modal show={show} onHide={modalClose} style={{
                width: '568px',
                height: '675px',
                backgroundColor: '#EDFCF9',
                boxShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
                borderRadius: '10px',
            }} className='font-popup screen-center'>
                {/* <Modal.Header className='modal-heading' closeButton> */}
                <Modal.Title className='font-title modal-head'>Request Leave</Modal.Title>
                {/* </Modal.Header> */}
                {/* <Modal.Body className='modal-body'> */}
                <Form className='modal-form'>
                    <div>
                        <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" fill="currentColor" class="bi bi-calendar3" viewBox="0 0 16 16">
                            <path d="M14 0H2a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2zM1 3.857C1 3.384 1.448 3 2 3h12c.552 0 1 .384 1 .857v10.286c0 .473-.448.857-1 .857H2c-.552 0-1-.384-1-.857V3.857z" />
                            <path d="M6.5 7a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm-9 3a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm-9 3a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2z" />
                        </svg>
                        <input type='date' className='time-field' />
                        <input type='time' className='time-field' />
                    </div>
                    <div className='div-1'>
                        Type
                        <Form.Select aria-label="Default select example" className='drop-down'>
                            <option>---please select---</option>
                            <option value="Annual Leave">Annual Leave</option>
                            <option value="Study Leave">Study Leave</option>
                            <option value="Others">Others(specify below)</option>
                        </Form.Select>
                    </div>
                    <Form.Group className="mb-3 div-1" controlId="exampleForm.ControlTextarea1">
                        <Form.Label>Comments</Form.Label>
                        <Form.Control as="textarea" rows={5}
                            placeholder='This field is required, please include the date, time, and reason'
                            className='comment-box' />
                    </Form.Group>
                </Form>
                {/* </Modal.Body> */}
                <div className='mt2'>
                    <Button className='button-bottom single-button'>Submit</Button>
                </div>
            </Modal>
        </div>
    )

}

export default RequestLeaveModal;