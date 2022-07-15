import React, { useState } from 'react'

import { Modal, Form } from 'react-bootstrap'

import '../css/general.css'
import styled from 'styled-components'

export default function RequestLeaveModal({ leave, setLeave }) {
    const [errorMsg, setErrorMsg] = useState(false)

    const [values, setValues] = useState({
        type_leave: "",
        type_day: "single",
        single_half_full: "full",
        single_day: "",
        multiple_start_half_full: "full",
        multiple_end_half_full: "full",
        multiple_start_day: "",
        multiple_end_day: ""
    })

    function setValue(e) {
        setValues({ ...values, [e.target.name]: e.target.value })
    }

    // console.log(values)

    const handleSubmit = (e) => {
        e.preventDefault()

        try{
            console.log(leave)
            setErrorMsg(true)
        }catch(error){
            console.log(error)
        }
    }

    return (
        <>
            <Modal show={leave}>
                <Container>
                    <Title className="my-5">Request Leave</Title>

                    <Form action="#" onSubmit={handleSubmit}>
                        <div className="d-block align-items-center my-3">
                            <ColumnName className="mb-2">Type of Leave</ColumnName>
                            <Form.Select name="type_leave" aria-label="Default select example" className='drop-down' onChange={(e) => setValue(e)}>
                                <option default>---please select---</option>
                                <option value="Annual Leave">Annual Leave</option>
                                <option value="Study Leave">Study Leave</option>
                                <option value="Not On Call">Not On Call</option>
                                <option value="Others">Others(specify below)</option>
                            </Form.Select>

                            {
                                errorMsg &&
                                <ErrorMessage className="mb-0">error message</ErrorMessage>
                            }
                        </div>

                        <div className="d-block align-items-center my-3">
                            <ColumnName className="mb-2">Day Type</ColumnName>
                            <div className="d-flex align-items-center">
                                <div className="me-3">
                                    <input type="radio" id="single" name="type_day" value="single" onClick={(e) => setValue(e)} defaultChecked />
                                    <label htmlFor="single" className="ms-2">Single day</label>
                                </div>

                                <div>
                                    <input type="radio" id="multiple" name="type_day" value="multiple" onClick={(e) => setValue(e)} />
                                    <label htmlFor="multiple" className="ms-2">Multiple days</label>
                                </div>
                            </div>
                        </div>

                        {
                            (values.type_day === "single" &&
                                (<div className="d-block">
                                    <ColumnName className="mb-2">Start Date</ColumnName>

                                    <div className="d-flex">
                                        {
                                            values.single_half_full == "am" && 
                                            <Form.Select className="p-2 me-1" style={{ width: '30%' }} name="single_half_full" id="single_half_full" onChange={(e) => setValue(e)}>
                                                    <option value="am" selected>a.m.</option>
                                                    <option value="pm">p.m.</option>
                                                    <option value="full">Full Day</option>  
                                            </Form.Select>
                                            ||
                                            values.single_half_full == "pm" && 
                                            <Form.Select className="p-2 me-1" style={{ width: '30%' }} name="single_half_full" id="single_half_full" onChange={(e) => setValue(e)}>
                                                    <option value="am">a.m.</option>
                                                    <option value="pm" selected>p.m.</option>
                                                    <option value="full">Full Day</option>  
                                            </Form.Select>
                                            ||
                                            values.single_half_full == "full" && 
                                            <Form.Select className="p-2 me-1" style={{ width: '30%' }} name="single_half_full" id="single_half_full" onChange={(e) => setValue(e)}>
                                                    <option value="am">a.m.</option>
                                                    <option value="pm">p.m.</option>
                                                    <option value="full" selected>Full Day</option>  
                                            </Form.Select>
                                        }

                                        {
                                            values.single_day != "" &&
                                            <Form.Control type='date' name="single_day" value={values.single_day} className="w-100 p-2 ms-1" onChange={(e) => setValue(e)} />
                                            ||
                                            <Form.Control type='date' name="single_day" value="0" className="w-100 p-2 ms-1" onChange={(e) => setValue(e)} />
                                        }
                                    </div>
                                </div>))
                            ||
                            (values.type_day === "multiple" &&
                                (<div className="d-block">
                                    <ColumnName className="mb-2">Start Date</ColumnName>

                                    <div className="d-flex">
                                        {
                                            values.multiple_start_half_full == "am" &&
                                            <Form.Select className="p-2 me-1" style={{ width: '30%' }} name="multiple_start_half_full" id="multiple_start_half_full" onChange={(e) => setValue(e)}>
                                                <option value="am" selected>a.m.</option>
                                                <option value="pm">p.m.</option>
                                                <option value="full">Full Day</option>
                                            </Form.Select>
                                            ||
                                            values.multiple_start_half_full == "pm" &&
                                            <Form.Select className="p-2 me-1" style={{ width: '30%' }} name="multiple_start_half_full" id="multiple_start_half_full" onChange={(e) => setValue(e)}>
                                                <option value="am">a.m.</option>
                                                <option value="pm" selected>p.m.</option>
                                                <option value="full" >Full Day</option>
                                            </Form.Select>
                                            ||
                                            values.multiple_start_half_full == "full" &&
                                            <Form.Select className="p-2 me-1" style={{ width: '30%' }} name="multiple_start_half_full" id="multiple_start_half_full" onChange={(e) => setValue(e)}>
                                                <option value="am">a.m.</option>
                                                <option value="pm">p.m.</option>
                                                <option value="full" selected>Full Day</option>
                                            </Form.Select>
                                        }

                                        {
                                            values.multiple_start_day != "" &&
                                            <Form.Control type='date' name="multiple_start_day" value={values.multiple_start_day} className="w-100 p-2 ms-1" onChange={(e) => setValue(e)}/>
                                            ||
                                            <Form.Control type='date' name="multiple_start_day" value="0" className="w-100 p-2 ms-1" onChange={(e) => setValue(e)}/>
                                        }

                                    </div>

                                    <ColumnName className="my-2">End Date</ColumnName>

                                    <div className="d-flex">
                                        {
                                            values.multiple_end_half_full == "am" &&
                                            <Form.Select className="p-2 me-1" style={{ width: '30%' }} name="multiple_end_half_full" id="multiple_end_half_full" onChange={(e) => setValue(e)}>
                                                <option value="am" selected>a.m.</option>
                                                <option value="pm">p.m.</option>
                                                <option value="full">Full Day</option>
                                            </Form.Select>
                                            ||
                                            values.multiple_end_half_full == "pm" &&
                                            <Form.Select className="p-2 me-1" style={{ width: '30%' }} name="multiple_end_half_full" id="multiple_end_half_full" onChange={(e) => setValue(e)}>
                                                <option value="am">a.m.</option>
                                                <option value="pm" selected>p.m.</option>
                                                <option value="full">Full Day</option>
                                            </Form.Select>
                                            ||
                                            values.multiple_end_half_full == "full" &&
                                            <Form.Select className="p-2 me-1" style={{ width: '30%' }} name="multiple_end_half_full" id="multiple_end_half_full" onChange={(e) => setValue(e)}>
                                                <option value="am">a.m.</option>
                                                <option value="pm">p.m.</option>
                                                <option value="full" selected>Full Day</option>
                                            </Form.Select>
                                        }
                                        
                                        {
                                            values.multiple_end_day != "" &&
                                            <Form.Control type='date' name="multiple_end_day" value={values.multiple_end_day} className="w-100 p-2 ms-1" onChange={(e) => setValue(e)}/>
                                            ||
                                            <Form.Control type='date' name="multiple_end_day" value="0" className="w-100 p-2 ms-1" onChange={(e) => setValue(e)}/>
                                        }
                                    </div>
                                </div>))
                        }

                        <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
                            <ColumnName className="my-3">Comments</ColumnName>
                            <Form.Control as="textarea" rows={5}
                                placeholder='This field is required, please include the date, time, and reason'
                            />
                        </Form.Group>

                        <div className="d-flex justify-content-center my-3">
                            <CloseButton className="m-2" onClick={() => setLeave(false)}>
                                Cancel
                            </CloseButton>

                            {
                                errorMsg == false &&
                                <ConfirmButton className="m-2" onClick={() => setLeave(false)}>
                                    Submit
                                </ConfirmButton>
                                ||
                                <ConfirmButton className="m-2">
                                    Submit
                                </ConfirmButton>
                            }
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

const ErrorMessage = styled.p`
    font-size: 12px;
    padding: 3px;
    color: red;
    // display: none;
`
