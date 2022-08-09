import React from 'react'

import { Modal, Form } from 'react-bootstrap'

import '../css/general.css'
import styled from 'styled-components'

export default function RequestLeaveModal({ leave, setLeave }){
    return (
        <>
            <Modal show={leave}>
                <Container>
                    <Title className="my-5">Request Leave</Title>

                    <Form action="#">
                        <div className="d-flex align-items-center my-3">
                            <i className="bi bi-calendar3 me-3" style={{ fontSize: '30px' }}></i>

                            <input type='date' className='time-field' />
                            <input type='time' className='time-field' />
                        </div>

                        <div className="d-block align-items-center my-3">
                            <ColumnName>Type</ColumnName>
                            <Form.Select aria-label="Default select example" className='drop-down'>
                                <option>---please select---</option>
                                <option value="Annual Leave">Annual Leave</option>
                                <option value="Study Leave">Study Leave</option>
                                <option value="Others">Others(specify below)</option>
                            </Form.Select>
                        </div>

                        <Form.Group className="mb-3 div-1" controlId="exampleForm.ControlTextarea1">
                            <ColumnName>Comments</ColumnName>
                            <Form.Control as="textarea" rows={5}
                                placeholder='This field is required, please include the date, time, and reason'
                             />
                        </Form.Group>

                        <div className="d-flex justify-content-center my-3">
                            <CloseButton className="m-2" onClick={() => setLeave(false)}>
                                Close
                            </CloseButton>

                            <ConfirmButton className="m-2" onClick={() => setLeave(false)}>
                                Confirm
                            </ConfirmButton>
                        </div>
                    </Form>
                </Container>
            </Modal>
        </>
    )
}

const Container = styled.div`
    width: 80%;
    margin: 0 auto;
`

const Title = styled.h1`
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

const Column = styled.p`
    font-size: 16px;
    color: #168082;
    margin-bottom: 0px;
`