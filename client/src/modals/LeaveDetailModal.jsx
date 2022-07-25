import styled from "styled-components";
import { Modal } from "react-bootstrap"
import { useState, useEffect } from "react";

const LeaveDetailModal = ({ leaveDetail, setLeaveDetail }) => {
    const [requestInfo, setRequstInfo] = useState()
    const [accountInfo, setAccountInfo] = useState()
    const accountId = 1 // temporary
    const leaveId = 0 // temporary

    useEffect(() => {
        accountId !== undefined &&
            fetch(`https://doctor-rota-spring-develop.herokuapp.com/notification/?accountId=${accountId}`, {
                mode: 'cors',
                method: "GET",
                headers: {
                    'Access-Control-Allow-Origin': '*',
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                }
            })
                .then(response => response.json())
                .then(data => setRequstInfo(data.leaveRequests))
    }, [accountId])

    useEffect(() => {
        // accountId !== undefined &&
        fetch(`https://doctor-rota-spring-develop.herokuapp.com/account/${accountId}`, {
            mode: 'cors',
            method: "GET",
            headers: {
                'Access-Control-Allow-Origin': '*',
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => setAccountInfo(data))
    }, [accountId])

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
                                <RowInfo className="mb-0">
                                    {
                                        accountInfo !== undefined &&
                                        accountInfo.username
                                    }
                                </RowInfo>
                            </div>
                        </div>

                        <div className="d-flex align-items-center my-3">
                            <div className="d-flex me-3">
                                <i className="bi bi-calendar3" style={{ fontSize: '30px' }}></i>
                            </div>

                            <div className="d-flex">
                                <RowInfo className="mb-0">
                                    {
                                        requestInfo !== undefined &&
                                        requestInfo[0].date
                                    }
                                </RowInfo>
                            </div>
                        </div>

                        <div className="d-flex align-items-center my-3">
                            <div className="d-flex me-3">
                                <i className="bi bi-clipboard2-minus-fill" style={{ fontSize: '30px' }}></i>
                            </div>

                            <div className="d-flex">
                                <RowInfo className="mb-0">
                                    {
                                        requestInfo !== undefined &&
                                        (
                                            requestInfo[leaveId].type === 0 &&
                                            <p className="mb-0">Annual Leave</p>
                                            || requestInfo[leaveId].type === 0 &&
                                            <p className="mb-0">Study Leave</p>
                                            || requestInfo[leaveId].type === 0 &&
                                            <p className="mb-0">NOC Request</p>
                                            || requestInfo[leaveId].type === 0 &&
                                            <p className="mb-0">Others</p>
                                        )
                                    }
                                </RowInfo>
                            </div>
                        </div>

                        <div className="d-flex align-items-center my-3">
                            <div className="d-flex me-3">
                                <i className="bi bi-chat-dots-fill" style={{ fontSize: '30px' }}></i>
                            </div>

                            <div className="d-flex">
                                <RowInfo className="mb-0">(200 OK)</RowInfo>
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
    color: #035eb8;

    @media (max-width: 575px){
        font-size: 24px;
    }
`

const ConfirmButton = styled.button`
    min-width: 100px;
    font-size: 20px;
    background-color: #035eb8;
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
    color: #035eb8;
    margin-bottom: 0px;
`