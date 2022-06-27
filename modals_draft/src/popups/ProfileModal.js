import { useState } from 'react';
import { Button, Modal, Form, Row, Col } from 'react-bootstrap';

const ProfileModal = () => {
    const [show, popup] = useState(false);
    const modalOpen = () => popup(true);
    const modalClose = () => popup(false);

    return (
        <div>
            <Button variant='success' size='xxl' onClick={modalOpen}>Profile</Button>
            <Modal show={show} onHide={modalClose} style={{
                width: '568px',
                height: '675px',
                backgroundColor: '#EDFCF9',
                boxShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
                borderRadius: '10px',
            }} className='font-popup screen-center'>
                <Modal.Header className='modal-heading'>
                    <Modal.Title className='font-title'>Profile</Modal.Title>
                </Modal.Header>
                <Modal.Body className='modal-body'>
                    <Form>
                        <Form.Group as={Row} className="mb-3" controlId="formPlaintext">
                            <Form.Label column sm="2">
                                Name
                            </Form.Label>
                            <Col sm="10" className='col-gap'>
                                <Form.Control plaintext readOnly defaultValue="Dennis Liú" />
                            </Col>
                        </Form.Group>
                        <Form.Group as={Row} className="mb-3" controlId="formPlaintextEmail">
                            <Form.Label column sm="2">
                                Staff ID
                            </Form.Label>
                            <Col sm="10">
                                <Form.Control plaintext readOnly defaultValue="GB12138" />
                            </Col>
                        </Form.Group>
                        <Row>
                            <Form.Label column lg={2}>
                                Dept.
                            </Form.Label>
                            <Col>
                                <Form.Control type="text" placeholder="Anaesthesia Department" />
                            </Col>
                        </Row>
                        <br />
                        <Row>
                            <Form.Label column lg={2}>
                                Email
                            </Form.Label>
                            <Col>
                                <Form.Control type="text" placeholder="email@nbt.nhs.uk" />
                            </Col>
                        </Row>
                        <br />
                    </Form>
                </Modal.Body>
                <div className='div-2'>
                    <Button className='button-bottom2 cancel-box' onClick={modalClose}>Cancel</Button>
                    <Button className='button-bottom2'>Update</Button>
                </div>
            </Modal>
        </div>
    )
}

export default ProfileModal;


{/* <Form>
  <Row>
    <Col>
      <Form.Control placeholder="First name" />
    </Col>
    <Col>
      <Form.Control placeholder="Last name" />
    </Col>
  </Row>
</Form> */}