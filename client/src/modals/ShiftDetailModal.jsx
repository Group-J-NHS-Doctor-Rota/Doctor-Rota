import React from 'react'

import { Modal } from 'react-bootstrap'

import styled from 'styled-components'

export default function ShiftModal({ shift, setShift }) {

    return (
        <>
            <Modal show={shift}>
                <ModalContainer>
                    <ModalTitle className="my-5">Shift details</ModalTitle>

                    <div className="d-block">
                        <div className="d-flex align-items-center my-3">
                            <div className="d-flex me-3">
                                <Icon className="bi bi-person-fill"></Icon>
                            </div>

                            <div className="d-flex">
                                <RowInfo className="mb-0">Steven Lin</RowInfo>
                            </div>
                        </div>

                        <div className="d-flex align-items-center my-3">
                            <div className="d-flex me-3">
                                <Icon className="bi bi-calendar3"></Icon>
                            </div>

                            <div className="d-flex">
                                <RowInfo className="mb-0">2022/06/21 08.00-18.00</RowInfo>
                            </div>
                        </div>

                        <div className="d-flex align-items-center my-3">
                            <div className="d-flex me-3">
                                <Icon className="bi bi-clipboard2-check-fill"></Icon>
                            </div>

                            <div className="d-flex">
                                <RowInfo className="mb-0">First On</RowInfo>
                            </div>
                        </div>

                        <div className="d-flex align-items-center my-3">
                            <div className="d-flex me-3">
                                <Icon className="bi bi-clock-history"></Icon>
                            </div>

                            <div className="d-flex">
                                <RowInfo className="mb-0">Long Day (12.5hrs)</RowInfo>
                            </div>
                        </div>
                    </div>

                    <div className="d-flex justify-content-center my-3">
                        <CloseButton className="m-2" onClick={() => setShift(false)}>
                            Close
                        </CloseButton>

                        <ConfirmButton className="m-2" onClick={() => setShift(false)}>
                            Swap
                        </ConfirmButton>
                    </div>
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
    color: #035eb8;

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
    color: #035eb8;
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

const Icon = styled.i`
    color: #035eb8;
    font-size: 30px;
`