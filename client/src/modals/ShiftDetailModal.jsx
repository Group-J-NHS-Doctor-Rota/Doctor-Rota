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
                                <i className="bi bi-person-fill" style={{ fontSize: '30px' }}></i>
                            </div>

                            <div className="d-flex">
                                <RowInfo className="mb-0">Steven Lin</RowInfo>
                            </div>
                        </div>

                        <div className="d-flex align-items-center my-3">
                            <div className="d-flex me-3">
                                <i className="bi bi-calendar3" style={{ fontSize: '30px' }}></i>
                            </div>

                            <div className="d-flex">
                                <RowInfo className="mb-0">2022/06/21 08.00-18.00</RowInfo>
                            </div>
                        </div>

                        <div className="d-flex align-items-center my-3">
                            <div className="d-flex me-3">
                                <i className="bi bi-envelope-fill" style={{ fontSize: '30px' }}></i>
                            </div>

                            <div className="d-flex">
                                <RowInfo className="mb-0">email@nbt.nhs.uk</RowInfo>
                            </div>
                        </div>

                        <div className="d-flex align-items-center my-3">
                            <div className="d-flex me-3">
                                <i className="bi bi-telephone-fill" style={{ fontSize: '30px' }}></i>
                            </div>

                            <div className="d-flex">
                                <RowInfo className="mb-0">+44 XXXXXXXXXX</RowInfo>
                            </div>
                        </div>

                        <div className="d-flex align-items-center my-3">
                            <div className="d-flex me-3">
                                <i className="bi bi-person-workspace" style={{ fontSize: '30px' }}></i>
                            </div>

                            <div className="d-flex">
                                <RowInfo className="mb-0">Request/review blood tests</RowInfo>
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

const ColumnName = styled.h3`
    font-size: 16px;
    font-weight: bold;
    color: #168082;
    margin-bottom: 0px;
`

const RowInfo = styled.p`
    font-size: 18px;
    color: #168082;
    margin-bottom: 0px;
`