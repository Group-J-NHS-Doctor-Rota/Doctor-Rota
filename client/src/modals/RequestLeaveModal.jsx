import { useState } from 'react'

import { Modal, Form } from 'react-bootstrap'

import '../css/general.css'
import styled from 'styled-components'

export default function RequestLeaveModal({ leave, setLeave }) {
    const [type, setType] = useState('single')
    function getValue(e) {
        setType(e.target.value)
    }
    return (
        <>
            <Modal show={leave}>
                <ModalContainer>
                    <ModalTitle className="my-5">Request Leave</ModalTitle>

                    <Form action="#">
                        <div className="d-block align-items-center my-3">
                            <TextLabel className="mb-2">The type of leave</TextLabel>
                            <Form.Select aria-label="Default select example" className='drop-down'>
                                <option>---please select---</option>
                                <option value="Annual Leave">Annual Leave</option>
                                <option value="Study Leave">Study Leave</option>
                                <option value="Others">Others(specify below)</option>
                            </Form.Select>
                        </div>

                        <div className="d-block align-items-center my-3">
                            <TextLabel className="mb-2">Single day / Multiple days</TextLabel>
                            <div className="d-flex align-items-center">
                                <div className="me-3">
                                    <input type="radio" id="single" name="type" value="single" onClick={(e) => getValue(e)} defaultChecked />
                                    <label htmlFor="single" className="ms-2">Single day</label>
                                </div>

                                <div>
                                    <input type="radio" id="multiple" name="type" value="multiple" onClick={(e) => getValue(e)} />
                                    <label htmlFor="multiple" className="ms-2">Multiple days</label>
                                </div>
                            </div>
                        </div>

                        {
                            type == "single" &&
                            <div className="d-block">
                                <TextLabel className="mb-2">The start day</TextLabel>
                                <div className="d-flex">
                                    <Form.Select className="p-2 me-1" name="single_half_full" id="half_full">
                                        <option value="single_am">am</option>
                                        <option value="single_pm">pm</option>
                                        <option value="single_full">full</option>
                                    </Form.Select>

                                    <Form.Control type='date' className="w-100 p-2 ms-1" />
                                </div>
                            </div>
                            ||
                            type == "multiple" &&
                            <div className="d-block">
                                <TextLabel className="mb-2">The start day</TextLabel>
                                <div className="d-flex">
                                    <Form.Select className="p-2 me-1" name="multiple_start_half_full" id="half_full">
                                        <option value="multiple_start_am">am</option>
                                        <option value="multiple_start_pm">pm</option>
                                        <option value="multiple_start_full">full</option>
                                    </Form.Select>

                                    <Form.Control type='date' className="w-100 p-2 ms-1" />
                                </div>
                                <TextLabel className="mb-2">The end day</TextLabel>
                                <div className="d-flex">
                                    <Form.Select className="p-2 me-1" name="multiple_end_half_full" id="half_full">
                                        <option value="multiple_end_am">am</option>
                                        <option value="multiple_end_pm">pm</option>
                                        <option value="multiple_end_full">full</option>
                                    </Form.Select>

                                    <Form.Control type='date' className="w-100 p-2 ms-1" />
                                </div>
                            </div>
                        }

                        <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
                            <TextLabel className="my-3">Comments</TextLabel>
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
                </ModalContainer>
            </Modal>
        </>
    )
}

const ModalContainer = styled.div`
    min-height: 475px;
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

const TextLabel = styled.label`
    font-size: 16px;
    color: #168082;
    font-weight: bold;
`

// const RowInfo = styled.h3`
//     font-size: 20px;
//     color: #168082;
//     margin-bottom: 0px;
// `