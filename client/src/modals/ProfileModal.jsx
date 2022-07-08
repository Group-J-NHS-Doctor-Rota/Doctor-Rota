import { Modal, Form } from "react-bootstrap"

import { useNavigate } from "react-router-dom";

import styled from "styled-components";

const ProfileModal = ({ profile, setProfile }) => {
    const navigate = useNavigate();
    return (
        <>
            <Modal show={profile} style->
                <ModalContainer>
                    <ModalTitle className="my-5">Profile</ModalTitle>
                    <form id="profile" action="#">
                        <div className="d-block">
                            {/* one line of info */}
                            <div className="d-flex align-items-center my-3">
                                {/* icon */}
                                <Label className="d-flex me-3">
                                    <i className="bi bi-person-fill" style={{ fontSize: '30px' }} />
                                </Label>
                                {/* detail */}
                                <RowInfo className="d-flex mb-0">Dennis Liú</RowInfo>
                            </div>

                            <div className="d-flex align-items-center my-3">
                                <Label className="d-flex me-3">
                                    <i className="bi bi-envelope-fill" style={{ fontSize: '30px' }} />
                                </Label>
                                <Input type="email" placeholder="name@nbt.nhs.uk" />
                            </div>

                            <div className="d-flex align-items-center my-3">
                                <Label className="d-flex me-3">
                                    <i className="bi bi-telephone-fill" style={{ fontSize: '30px' }} />
                                </Label>
                                <Input type="tel" placeholder="+44 XXXXXXXXXX" />
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

export default ProfileModal;

const ModalContainer = styled.div`
    min-height: 475px;
    width: 80%;
    margin: 0 auto;
`

const ModalTitle = styled.h1`
    font-size: 32px;
    font-weight: bold;
    color: #168082;

    @media(max-width:575px) {
        font-size: 24px;
    }
`

const CloseButton = styled.button`
    min-width: 100px;
    font-size: 20px;
    background-color: #fff;
    border-radius: 5px;
    border: none;
    color: #168082;
    font-weight: bold;
    padding: 5px 10px;

    @media (max-width: 575px) {
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

    @media (max-width: 575px) {
        font-size: 16px;
        min-width: 80px;
    }
`

const Label = styled.label`
    font-size: 25px;
    color: #168082;
    font-weight: bold;
`

const RowInfo = styled.h3`
    font-size: 20px;
    color: #168082;
    margin-bottom: 0px;
`

const Input = styled.input`
    font-size: 18px;
    width: 100%;
    height: 36px;
    border: none;
    border-radius: 5px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
`

// const ProfileButton = styled.button`
//     padding: 5px 25px;
//     color: white;
//     font-size: 18px;
//     font-weight: bold;
//     background-color: #168082;
//     border: none;
//     border-radius: 5px;
// `