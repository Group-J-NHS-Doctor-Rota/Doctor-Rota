import styled from "styled-components";
import { Modal } from "react-bootstrap"

const LeaveDetailModal = ({ leaveDetail, setLeaveDetail }) => {

    return (
        <>
            <Modal show={leaveDetail}>
                <ModalContainer>
                    <ModalTitle className="my-5">Request Detail</ModalTitle>
                    <div className="d-block">
                        <div className="d-flex align-items-center my-3">
                            <div className="d-flex me-3">
                                <i className="bi bi-person-fill" style={{ fontSize: '30px' }}></i>
                            </div>

                            <div className="d-flex">
                                <RowInfo className="mb-0">Dennis Liú</RowInfo>
                            </div>
                        </div>

                        <div className="d-flex align-items-center my-3">
                            <div className="d-flex me-3">
                                <i className="bi bi-calendar3" style={{ fontSize: '30px' }}></i>
                            </div>

                            <div className="d-flex">
                                <RowInfo className="mb-0">Jul 25th - Jul 27th (a.m.)</RowInfo>
                            </div>
                        </div>

                        <div className="d-flex align-items-center my-3">
                            <div className="d-flex me-3">
                                <i className="bi bi-clipboard2-minus-fill" style={{ fontSize: '30px' }}></i>
                            </div>

                            <div className="d-flex">
                                <RowInfo className="mb-0">Study Leave</RowInfo>
                            </div>
                        </div>

                        <div className="d-flex align-items-center my-3">
                            <div className="d-flex me-3">
                                <i className="bi bi-chat-dots-fill" style={{ fontSize: '30px' }}></i>
                            </div>

                            <div className="d-flex">
                                <RowInfo className="mb-0">I am attending a 3-day first aid course in Cardiff, it is a mandantory step to pass the DBS check so that I can start working with vulnerable people. I will return to work on Wednesday afternoon.</RowInfo>
                            </div>
                        </div>
                    </div>

                    <div className="d-flex justify-content-center my-3">
                        <ConfirmButton className="m-2" onClick={() => setLeaveDetail(false)}>
                            OK
                        </ConfirmButton>
                    </div>
                </ModalContainer>

            </Modal>
        </>
    )

}

export default LeaveDetailModal

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

const RowInfo = styled.p`
    font-size: 18px;
    color: #168082;
    margin-bottom: 0px;
`