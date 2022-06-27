import { useState } from 'react';
import { Button, Modal, Form } from 'react-bootstrap';

const ShiftDetailsModal = () => {
    const [show, popup] = useState(false); // set initial popup state
    const modalOpen = () => popup(true);
    const modalClose = () => popup(false);

    return (
        <div>
            <Button variant='secondary' size='xxl' onClick={modalOpen}>Night Shift (Calendar)</Button>
            <Modal show={show} onHide={modalClose} style={{
                width: '568px',
                height: '675px',
                backgroundColor: '#EDFCF9',
                boxShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
                borderRadius: '10px',
            }} className='font-popup screen-center'>
                <Modal.Header className='modal-heading'>
                    <Modal.Title className='font-title'>Shift Details</Modal.Title>
                </Modal.Header>
                <Modal.Body className='modal-body'>
                    <div>
                        <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" fill="currentColor" class="bi bi-file-earmark-person" viewBox="0 0 16 16">
                            <path d="M11 8a3 3 0 1 1-6 0 3 3 0 0 1 6 0z" />
                            <path d="M14 14V4.5L9.5 0H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2zM9.5 3A1.5 1.5 0 0 0 11 4.5h2v9.255S12 12 8 12s-5 1.755-5 1.755V2a1 1 0 0 1 1-1h5.5v2z" />
                        </svg>
                        {/* 'Trainee' is to be replaced by fetchAPI */}
                        <span className='span-popup'>Dennis Liú</span>
                    </div>
                    <div className='div-1'>
                        <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" fill="currentColor" class="bi bi-calendar3" viewBox="0 0 16 16">
                            <path d="M14 0H2a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2zM1 3.857C1 3.384 1.448 3 2 3h12c.552 0 1 .384 1 .857v10.286c0 .473-.448.857-1 .857H2c-.552 0-1-.384-1-.857V3.857z" />
                            <path d="M6.5 7a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm-9 3a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm-9 3a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2z" />
                        </svg>
                        {/* 'Time' is to be replaced by fetchAPI */}
                        <span className='span-popup'>2022/06/21 08.00-18.00</span>
                    </div>
                    <div className='div-1'>
                        <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" fill="currentColor" class="bi bi-envelope-fill" viewBox="0 0 16 16">
                            <path d="M.05 3.555A2 2 0 0 1 2 2h12a2 2 0 0 1 1.95 1.555L8 8.414.05 3.555ZM0 4.697v7.104l5.803-3.558L0 4.697ZM6.761 8.83l-6.57 4.027A2 2 0 0 0 2 14h12a2 2 0 0 0 1.808-1.144l-6.57-4.027L8 9.586l-1.239-.757Zm3.436-.586L16 11.801V4.697l-5.803 3.546Z" />
                        </svg>
                        {/* 'Time' is to be replaced by fetchAPI */}
                        <span className='span-popup'>email@nbt.nhs.uk</span>
                    </div>
                </Modal.Body>
                <div className='mt1'>
                    <Button className='button-bottom single-button' style={{
                        display: 'block',
                    }} onClick={modalClose}>OK</Button>
                </div>
            </Modal>
        </div >
    )
}

export default ShiftDetailsModal;