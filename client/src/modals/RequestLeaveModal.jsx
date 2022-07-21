import React, { useState } from 'react'

import { Modal, Form } from 'react-bootstrap'

import '../css/general.css'
import styled from 'styled-components'

export default function RequestLeaveModal({ leave, setLeave }) {
    const [errorMsg, setErrorMsg] = useState({
        type_leave_required: false,
        single_day_required: false,
        single_day_invalid: false,
        multiple_start_day_required: false,
        multiple_end_day_required: false,
        multiple_start_day_invalid: false,
        multiple_end_day_invalid: false,
        multiple_start_end_invalid: false,
        comments_required: false
    })

    const [values, setValues] = useState({
        type_leave: "",
        type_day: "single",
        single_half_full: "full",
        single_day: "",
        multiple_start_half_full: "full",
        multiple_end_half_full: "full",
        multiple_start_day: "",
        multiple_end_day: "",
        comments: ""
    })

    function setValue(e) {
        setValues({ ...values, [e.target.name]: e.target.value })

        if(e.target.name == "type_leave"){
            if(e.target.value != ""){
                setErrorMsg({... errorMsg, ["type_leave_required"]: false})
            }
        }

        if(e.target.name == "single_day"){
            if(e.target.value != ""){
                setErrorMsg({... errorMsg, ["single_day_required"]: false})
            }else{
                setErrorMsg({... errorMsg, ["single_day_required"]: true})
            }

            const today = getTodayDate()

            if(!isValidDate(today, e.target.value)){
                setErrorMsg({... errorMsg, ["single_day_invalid"]: true})
            }else{
                setErrorMsg({... errorMsg, ["single_day_invalid"]: false})
            }
        }

        if(e.target.name == "multiple_start_day"){
            if(e.target.value != ""){
                setErrorMsg({... errorMsg, ["multiple_start_day_required"]: false})
            }else{
                setErrorMsg({... errorMsg, ["multiple_start_day_required"]: true})
            }

            const today = getTodayDate()
            
            if(!isValidDate(today, e.target.value)){
                setErrorMsg({... errorMsg, ["multiple_start_day_invalid"]: true})
            }else{
                setErrorMsg({... errorMsg, ["multiple_start_day_invalid"]: false})
            }
        }
        
        if(e.target.name == "multiple_end_day"){
            if(e.target.value != ""){
                setErrorMsg({... errorMsg, ["multiple_end_day_required"]: false})
            }
            
            const today = getTodayDate()
            
            if(!isValidDate(today, e.target.value)){            
                setErrorMsg({... errorMsg, ["multiple_end_day_invalid"]: true})
            }else{
                setErrorMsg({... errorMsg, ["multiple_end_day_invalid"]: false})

                if(values.multiple_start_day != "" && e.target.value != ""){

                    if(!isValidDate(values.multiple_start_day, values.multiple_end_day)){
                        setErrorMsg({... errorMsg, ["multiple_start_end_invalid"]: true})
                    }else{
                        setErrorMsg({... errorMsg, ["multiple_start_end_invalid"]: false})
                    }
                }
            }
        } 
        
        if(e.target.name == "comments"){
            if(e.target.value != ""){
                setErrorMsg({... errorMsg, ["comments_required"]: false})
            }
        }
    }


    function isValidDate(date1, date2){
        const num1 = parseInt(date1.replaceAll("-", ""))
        const num2 = parseInt(date2.replaceAll("-", ""))
        const result = num2 - num1
        
        return result > 0 ? true : false
    }

    function getTodayDate(){
        const today = new Date()
        const date = today.getDate()
        const month = today.getMonth() + 1
        const year = today.getFullYear()
        
        let stringDate = ""
        let stringMonth = ""
        
        if(date < 10){
            stringDate = "0" + date.toString()
        }else{
            stringDate = date.toString()
        }

        if(month < 10){
            stringMonth = "0" + month.toString()
        }else{
            stringMonth = month.toString()
        }

        return `${year}-${stringMonth}-${stringDate}`
    }

    function handleCancel(){
        setErrorMsg({
            type_leave_required: false,
            single_day_required: false,
            single_day_invalid: false,
            multiple_start_day_required: false,
            multiple_end_day_required: false,
            multiple_start_day_invalid: false,
            multiple_end_day_invalid: false,
            multiple_start_end_invalid: false
        })
        setValues({
            type_leave: "",
            type_day: "single",
            single_half_full: "full",
            single_day: "",
            multiple_start_half_full: "full",
            multiple_end_half_full: "full",
            multiple_start_day: "",
            multiple_end_day: ""
        })
        setLeave(false)
    }

    function checkDateValidation(){
        if(values.type_day == "single"){
            if(values.single_day == ""){
                setErrorMsg({... errorMsg, ["single_day_required"]: true})
                setLeave(true)
            }else{
                setErrorMsg({... errorMsg, ["single_day_required"]: false})

                const today = getTodayDate()
                if(!isValidDate(today, values.single_day)){
                    setErrorMsg({... errorMsg, ["single_day_invalid"]: true})
                    setLeave(true)
                }
                
            }
        }else if(values.type_day == "multiple"){
            if(values.multiple_start_day == ""){
                setErrorMsg({... errorMsg, ["multiple_start_day_required"]: true})
                setLeave(true)
            }else{
                setErrorMsg({... errorMsg, ["multiple_start_day_required"]: false})

                if(values.multiple_end_day == ""){
                    setErrorMsg({... errorMsg, ["multiple_end_day_required"]: true})
                    setLeave(true)
                }else{
                    setErrorMsg({... errorMsg, ["multiple_end_day_required"]: false})

                    const today = getTodayDate()
                    if(!isValidDate(today, values.multiple_start_day)){
                        setErrorMsg({... errorMsg, ["multiple_start_day_invalid"]: true})
                        setLeave(true)
                    }else{
                        setErrorMsg({... errorMsg, ["multiple_start_day_invalid"]: false})

                        if(!isValidDate(today, values.multiple_end_day)){
                            setErrorMsg({... errorMsg, ["multiple_end_day_invalid"]: true})
                            setLeave(true)
                        }else{
                            setErrorMsg({... errorMsg, ["multiple_end_day_invalid"]: false})

                            if(!isValidDate(values.multiple_start_day, values.multiple_end_day)){
                                setErrorMsg({... errorMsg, ["multiple_start_end_invalid"]: true})
                                setLeave(true)
                            }else{
                                setErrorMsg({... errorMsg, ["multiple_start_end_invalid"]: false})
                            }
                        }
                    }
                }
            }
        }
    }
    
    const handleSubmit = async (e) => {
        e.preventDefault()
        
        try{
            if(values.type_leave == ""){
                setErrorMsg({... errorMsg, ["type_leave_required"]: true})
                setLeave(true)
            }else{
                setErrorMsg({... errorMsg, ["type_leave_required"]: false})

                if(values.comments == ""){
                    setErrorMsg({... errorMsg, ["comments_required"]: true})
                    if(values.type_leave == "Study Leave"){
                        setLeave(true)
                    }else{
                        checkDateValidation()
                    }
                }else{
                    setErrorMsg({... errorMsg, ["comments_required"]: false})
                    checkDateValidation()
                }
            }
        }catch(error){
            console.log(error)
        }
    }

    return (
        <>
            <Modal show={leave}>
                <Container>
                    <Title className="my-5">Request Leave</Title>

                    <Form onSubmit={handleSubmit}>
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
                                errorMsg.type_leave_required &&
                                <ErrorMessage className="mb-0">You haven't select one of options</ErrorMessage>
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
                                                    <option value="full">Full</option>  
                                                    <option value="am" selected>a.m.</option>
                                                    <option value="pm">p.m.</option>
                                            </Form.Select>
                                            ||
                                            values.single_half_full == "pm" && 
                                            <Form.Select className="p-2 me-1" style={{ width: '30%' }} name="single_half_full" id="single_half_full" onChange={(e) => setValue(e)}>
                                                    <option value="full">Full</option>  
                                                    <option value="am">a.m.</option>
                                                    <option value="pm" selected>p.m.</option>
                                            </Form.Select>
                                            ||
                                            values.single_half_full == "full" && 
                                            <Form.Select className="p-2 me-1" style={{ width: '30%' }} name="single_half_full" id="single_half_full" onChange={(e) => setValue(e)}>
                                                    <option value="full" selected>Full</option>  
                                                    <option value="am">a.m.</option>
                                                    <option value="pm">p.m.</option>
                                            </Form.Select>
                                        }
                                        
                                        {
                                            values.single_day != "" &&
                                            <Form.Control type='date' name="single_day" value={values.single_day} className="w-100 p-2 ms-1" onChange={(e) => setValue(e)} />
                                            ||
                                            <Form.Control type='date' name="single_day" value="0" className="w-100 p-2 ms-1" onChange={(e) => setValue(e)} />
                                        }

                                    </div>
                                    {
                                        errorMsg.single_day_required &&
                                        <ErrorMessage className="mb-0">You haven't select one of date</ErrorMessage>
                                    }
                                    {
                                        errorMsg.single_day_invalid &&
                                        <ErrorMessage className="mb-0">Wrong date</ErrorMessage>
                                    }
                                </div>))
                            ||
                            (values.type_day === "multiple" &&
                                (<div className="d-block">
                                    <ColumnName className="mb-2">Start Date</ColumnName>

                                    <div className="d-flex">
                                        {
                                            values.multiple_start_half_full == "am" &&
                                            <Form.Select className="p-2 me-1" style={{ width: '30%' }} name="multiple_start_half_full" id="multiple_start_half_full" onChange={(e) => setValue(e)}>
                                                <option value="full">Full</option>
                                                <option value="am" selected>a.m.</option>
                                                <option value="pm">p.m.</option>
                                            </Form.Select>
                                            ||
                                            values.multiple_start_half_full == "pm" &&
                                            <Form.Select className="p-2 me-1" style={{ width: '30%' }} name="multiple_start_half_full" id="multiple_start_half_full" onChange={(e) => setValue(e)}>
                                                <option value="full" >Full</option>
                                                <option value="am">a.m.</option>
                                                <option value="pm" selected>p.m.</option>
                                            </Form.Select>
                                            ||
                                            values.multiple_start_half_full == "full" &&
                                            <Form.Select className="p-2 me-1" style={{ width: '30%' }} name="multiple_start_half_full" id="multiple_start_half_full" onChange={(e) => setValue(e)}>
                                                <option value="full" selected>Full</option>
                                                <option value="am">a.m.</option>
                                                <option value="pm">p.m.</option>
                                            </Form.Select>
                                        }

                                        {
                                            values.multiple_start_day != "" &&
                                            <Form.Control type='date' name="multiple_start_day" value={values.multiple_start_day} className="w-100 p-2 ms-1" onChange={(e) => setValue(e)}/>
                                            ||
                                            <Form.Control type='date' name="multiple_start_day" value="0" className="w-100 p-2 ms-1" onChange={(e) => setValue(e)}/>
                                        }
                                    </div>

                                        {
                                            errorMsg.multiple_start_day_required &&
                                            <ErrorMessage className="mb-0">You haven't select one of date</ErrorMessage>
                                        }
                                        {
                                            errorMsg.multiple_start_day_invalid &&
                                            <ErrorMessage className="mb-0">Wrong date</ErrorMessage>
                                        }

                                    <ColumnName className="my-2">End Date</ColumnName>

                                    <div className="d-flex">

                                        {
                                            values.multiple_end_half_full == "am" &&
                                            <Form.Select className="p-2 me-1" style={{ width: '30%' }} name="multiple_end_half_full" id="multiple_end_half_full" onChange={(e) => setValue(e)}>
                                                <option value="full">Full</option>
                                                <option value="am" selected>a.m.</option>
                                                <option value="pm">p.m.</option>
                                            </Form.Select>
                                            ||
                                            values.multiple_end_half_full == "pm" &&
                                            <Form.Select className="p-2 me-1" style={{ width: '30%' }} name="multiple_end_half_full" id="multiple_end_half_full" onChange={(e) => setValue(e)}>
                                                <option value="full">Full</option>
                                                <option value="am">a.m.</option>
                                                <option value="pm" selected>p.m.</option>
                                            </Form.Select>
                                            ||
                                            values.multiple_end_half_full == "full" &&
                                            <Form.Select className="p-2 me-1" style={{ width: '30%' }} name="multiple_end_half_full" id="multiple_end_half_full" onChange={(e) => setValue(e)}>
                                                <option value="full" selected>Full</option>
                                                <option value="am">a.m.</option>
                                                <option value="pm">p.m.</option>
                                            </Form.Select>
                                        }
                                        
                                        {
                                            values.multiple_end_day != "" &&
                                            <Form.Control type='date' name="multiple_end_day" value={values.multiple_end_day} className="w-100 p-2 ms-1" onChange={(e) => setValue(e)}/>
                                            ||
                                            <Form.Control type='date' name="multiple_end_day" value="0" className="w-100 p-2 ms-1" onChange={(e) => setValue(e)}/>
                                        }

                                    </div>
                                    {
                                        errorMsg.multiple_end_day_required &&
                                        <ErrorMessage className="mb-0">You haven't select one of date</ErrorMessage>
                                    }
                                    {
                                        errorMsg.multiple_end_day_invalid &&
                                        <ErrorMessage className="mb-0">Wrong date</ErrorMessage>
                                    }
                                    {
                                        errorMsg.multiple_start_end_invalid &&
                                        <ErrorMessage className="mb-0">Wrong date</ErrorMessage>
                                    }
                                </div>))
                        }

                        <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
                            <ColumnName className="my-3">Comments</ColumnName>
                            <Form.Control name="comments" as="textarea" rows={5}
                                placeholder='Please leave your reason here, no more than 2000 characters'
                                onChange={(e) => setValue(e)}
                            />
                            {
                                errorMsg.comments_required && values.type_leave == "Study Leave" && 
                                <ErrorMessage className="mb-0">if select the study leave, this field must be required</ErrorMessage>
                            }
                        </Form.Group>

                        <div className="d-flex justify-content-center my-3">
                            <CloseButton className="m-2" type="button" onClick={handleCancel}>
                                Cancel
                            </CloseButton>

                            <ConfirmButton className="m-2" type="button">
                                Submit
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

const ErrorMessage = styled.p`
    font-size: 12px;
    padding: 3px;
    color: red;
    // display: none;
`
