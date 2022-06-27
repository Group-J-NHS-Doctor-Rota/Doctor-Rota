import { useState } from 'react';
import { Button, Modal } from 'react-bootstrap';

const UserDetailsModal = () => {
    const [show, popup] = useState(false);
    const modalOpen = () => popup(true);
    const modalClose = () => popup(false);

    return (
        <div>
            <Button variant='secondary' size='xxl' onClick={modalOpen}>Dennis Liú</Button>
            <p>(This component is the list item in 'manage account' page)</p>
            <Modal show={show} onHide={modalClose} style={{
                width: '568px',
                height: '675px',
                backgroundColor: '#EDFCF9',
                boxShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
                borderRadius: '10px',
            }} className='font-popup screen-center'>
                <Modal.Header className='modal-heading'>
                    <Modal.Title className='font-title'>Dennis</Modal.Title>
                </Modal.Header>
            </Modal>
        </div>
    )
}

export default UserDetailsModal;