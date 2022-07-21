import { Modal } from 'react-bootstrap'
// import FormInput from '../components/FormInput'

import styled from 'styled-components'

export default function ProfileModal({ profile, setProfile }) {
    const handleSubmit = e => e.preventDefault()

    return (
        <>
            <Modal show={profile}>
                <ModalContainer>
                    <ModalTitle className="my-5">Profile</ModalTitle>
                    <form id="profile" action="#" onSubmit={handleSubmit}>
                        <div className="d-block">

                            <div className="d-flex align-items-center my-3">
                                <Label className="d-flex me-3">
                                    <i className="bi bi-person-fill" style={{ fontSize: '30px' }} />
                                </Label>
                                <RowInfo className="d-flex mb-0">Dennis Liú</RowInfo>
                            </div>

                            <div className="d-flex align-items-center my-3">
                                <Label className="d-flex me-3">
                                    <i className="bi bi-person-badge-fill" style={{ fontSize: '30px' }} />
                                </Label>
                                <RowInfo className="d-flex mb-0">GB12138</RowInfo>
                            </div>

                            <div className='d-block'>
                                <div className="d-flex align-items-center my-3">
                                    <Label className="d-flex me-3">
                                        <i className="bi bi-envelope-fill" style={{ fontSize: '30px' }} />
                                    </Label>
                                    <Input type="email" placeholder="name@gmail.com (public)" />
                                    <LockIcon className="bi bi-unlock-fill ms-2" />
                                </div>

                                <div className="d-flex align-items-center my-3">
                                    <Label className="d-flex me-3">
                                        <i className="bi bi-telephone-fill" style={{ fontSize: '30px' }} />
                                    </Label>
                                    <Input type="tel" placeholder="&nbsp;&nbsp;+44 XXXXXXXXXX (only me and admin)" />
                                    <LockIcon className="bi bi-lock-fill ms-2" />
                                </div>
                            </div>
                            <div className="d-flex justify-content-center my-5">
                                <CloseButton className="m-2" onClick={() => setProfile(false)}>
                                    Close
                                </CloseButton>
                                <ConfirmButton className="m-2" onClick={() => setProfile(false)}>
                                    Update
                                </ConfirmButton>
                            </div>
                        </div>
                    </form>
                </ModalContainer>
            </Modal>
        </>
    )
}

const ModalContainer = styled.div`
    width: 80%;
    margin: 0 auto;
`

const ModalTitle = styled.h1`
    font-size: 32px;
    font-weight: bold;
    color: #168082;

    @media (max-width: 575px){
        font-size: 24px;
    }
`

const CloseButton = styled.button`
    min-width: 100px;
    font-size: 20px;
    background-color: white;
    border-radius: 5px;
    border: none;
    color: #168082;
    font-weight: bold;
    padding: 5px 10px;

    @media (max-width: 575px){
        font-size: 16px;
        min-width: 80px;
    }
`

const ConfirmButton = styled.button`
    min-width: 100px;
    font-size: 20px;
    background-color: #168082;
    border-radius: 5px;
    border: none;
    color: white;
    font-weight: bold;
    padding: 5px 10px;

    @media (max-width: 575px){
        font-size: 16px;
        min-width: 80px;
    }
`

const Label = styled.label`
    font-size: 25px;
    color: #168082;
    font-weight: bold;
`

const RowInfo = styled.p`
    font-size: 18px;
    color: #168082;
    margin-bottom: 0px;
`

const Input = styled.input`
    font-size: 16px !important;
    width: 100%;
    height: 36px;
    border: none;
    border-radius: 5px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
`

const LockIcon = styled.i`
    font-size: 20px;
    cursor: pointer;
`