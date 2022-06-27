import { useState } from 'react';
import { Button, Modal } from 'react-bootstrap';

const Logout = () => {
    const [show, popup] = useState(false);
    const modalOpen = () => popup(true);
    const modalClose = () => popup(false);

    return (
        <div>
            <Button variant='warning' size='xxl' onClick={modalOpen}>Log out</Button>
            <Modal show={show} onHide={modalClose} style={{
                width: '568px',
                height: '255px',
                backgroundColor: '#EDFCF9',
                boxShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
                borderRadius: '10px',
            }} className='font-popup screen-center'>
                <div className='warning-logout'>
                    <p>Do you want to log out?</p>
                </div>
                <div className='div-3'>
                    <Button className='button-bottom2 cancel-box' onClick={modalClose}>Cancel</Button>
                    <Button className='button-bottom2'>Confirm</Button>
                </div>
            </Modal>
        </div>
    )

}

export default Logout;