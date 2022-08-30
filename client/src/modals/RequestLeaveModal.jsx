import React, { useState, useEffect } from 'react'

import { useUrl } from '../contexts/UrlContexts' 

import { Modal, Form } from 'react-bootstrap'

import '../css/general.css'
import styled from 'styled-components'

export default function RequestLeaveModal({ leave, setLeave }) {
    // need to fix

    // const toDay = new Date().toISOString().substring(0, 10);
    const auth = JSON.parse(localStorage.getItem('auth'))
    const { getUrl } = useUrl()

    const url =  getUrl()

    const [errorMsg, setErrorMsg] = useState({
        type_leave_required: false,
        single_day_required: false,
        single_day_invalid: false,
        multiple_start_day_required: false,
        multiple_end_day_required: false,
        multiple_start_day_invalid: false,
        multiple_end_day_invalid: false,
        multiple_start_end_invalid: false,
        comments_required: false,
        annual_leave_invalid: false,
        study_leave_invalid: false
    })

    const [values, setValues] = useState({
        type_leave: "",
        type_day: "single",
        single_half_full: "0",
        single_day: "",
        multiple_start_half_full: "0",
        multiple_end_half_full: "0",
        multiple_start_day: "",
        multiple_end_day: "",
        comments: ""
    })

    const [leaves, setLeaves] = useState({
        annualLeave: 0,
        studyLeave: 0
    })

    useEffect(() => {
        if(url != undefined){
            const newUrl = `${url}leaves?accountId=${auth.id}`
            
            fetch(newUrl, {
                mode: 'cors',
                method: 'GET',
                headers: {
                    'Access-Control-Allow-Origin': '*',
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                    'token': auth.token
                }
            })
            .then(response => response.json())
            .then(data => setLeaves({
                annualLeave: data.annualLeave,
                studyLeave: data.studyLeave
            }))
        }
    }, [])


    function setValue(e) {
        setValues({ ...values, [e.target.name]: e.target.value })

        if(e.target.name == "type_leave"){
            if(e.target.value != ""){
                setErrorMsg({... errorMsg, ["type_leave_required"]: false})
            }
            if(e.target.value == 0){
                if(leaves.annualLeave <= 0){
                    setErrorMsg({... errorMsg, ["annual_leave_invalid"]: true })
                }else{
                    setErrorMsg({... errorMsg, ["annual_leave_invalid"]: false })
                }
            }
            if(e.target.value == 1){
                if(leaves.studyLeave <= 0){
                    setErrorMsg({... errorMsg, ["study_leave_invalid"]: true })
                }else{
                    setErrorMsg({... errorMsg, ["study_leave_invalid"]: false })
                }
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


                if(values.multiple_end_day != "" && e.target.value != ""){
                    if(!isValidDate(e.target.value, values.multiple_end_day)){
                        setErrorMsg({... errorMsg, ["multiple_start_end_invalid"]: true})
                    }else{
                        setErrorMsg({... errorMsg, ["multiple_start_end_invalid"]: false})
                    }
                }
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
                    if(!isValidDate(values.multiple_start_day, e.target.value)){
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

    function isSameDate(date1, date2){
        const num1 = parseInt(date1.replaceAll("-", ""))
        const num2 = parseInt(date2.replaceAll("-", ""))
        const result = num2 - num1
        
        return result == 0 ? true : false
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
            single_half_full: "0",
            single_day: "",
            multiple_start_half_full: "0",
            multiple_end_half_full: "0",
            multiple_start_day: "",
            multiple_end_day: "",
            comments: ""
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
                }else{
                    setErrorMsg({... errorMsg, ["single_day_invalid"]: false})

                    handleSendForm()
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

                                handleSendForm()
                            }
                        }
                    }
                }
            }
        }
    }

    function handleSendForm(){
        if(url != undefined){
            if(values.type_day == "single"){
                try{
                    fetch(`${url}request/leave?accountId=${auth.id}&date=${values.single_day}&type=${values.type_leave}&length=${values.single_half_full}&note=${values.comments}`, {
                        mode: 'cors',
                        method: "POST",
                        headers: {
                            'Access-Control-Allow-Origin': '*',
                            'Content-Type': 'application/json',
                            'Accept': 'application/json',
                            'token': auth.token
                        }
                    })
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
                        single_half_full: "0",
                        single_day: "",
                        multiple_start_half_full: "0",
                        multiple_end_half_full: "0",
                        multiple_start_day: "",
                        multiple_end_day: "",
                        comments: ""
                    })
                    setLeave(false)

                }catch(error){
                    console.log(error)
                }
            }
    
            if(values.type_day == "multiple"){
                const end_day = values.multiple_end_day
                let start_day = values.multiple_start_day

                try{
                    fetch(`${url}request/leave?accountId=${auth.id}&date=${start_day}&type=${values.type_leave}&length=${values.multiple_start_half_full}&note=${values.comments}`, {
                        mode: 'cors',
                        method: "POST",
                        headers: {
                            'Access-Control-Allow-Origin': '*',
                            'Content-Type': 'application/json',
                            'Accept': 'application/json',
                            'token': auth.token
                        }
                    })
                }catch(error){
                    console.log(error)
                }

                while(!isSameDate(start_day, end_day)){
                    let result = new Date(start_day)
                    result.setDate(result.getDate() + 1)

                    const date = result.getDate()
                    const month = result.getMonth() + 1
                    const year = result.getFullYear()
                    
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

                    start_day = `${year}-${stringMonth}-${stringDate}`

                    if(isSameDate(start_day, end_day)){
                        try{
                            fetch(`${url}request/leave?accountId=${auth.id}&date=${start_day}&type=${values.type_leave}&length=${values.multiple_end_half_full}&note=${values.comments}`, {
                                mode: 'cors',
                                method: "POST",
                                headers: {
                                    'Access-Control-Allow-Origin': '*',
                                    'Content-Type': 'application/json',
                                    'Accept': 'application/json',
                                    'token': auth.token
                                }
                            })
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
                                single_half_full: "0",
                                single_day: "",
                                multiple_start_half_full: "0",
                                multiple_end_half_full: "0",
                                multiple_start_day: "",
                                multiple_end_day: "",
                                comments: ""
                            })
                            setLeave(false)
                        }catch(error){
                            console.log(error)
                        }
                    }else{
                        try{
                            fetch(`${url}request/leave?accountId=${auth.id}&date=${start_day}&type=${values.type_leave}&length=0&note=${values.comments}`, {
                                mode: 'cors',
                                method: "POST",
                                headers: {
                                    'Access-Control-Allow-Origin': '*',
                                    'Content-Type': 'application/json',
                                    'Accept': 'application/json',
                                    'token': auth.token
                                }
                            })
                        }catch(error){
                            console.log(error)
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
                        if(errorMsg.study_leave_invalid != true && errorMsg.annual_leave_invalid != true){
                            checkDateValidation()
                        }
                    }
                }else{
                    setErrorMsg({... errorMsg, ["comments_required"]: false})
                    if(errorMsg.study_leave_invalid != true && errorMsg.annual_leave_invalid != true){
                        checkDateValidation()
                    }

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
                                <option value="0">Annual Leave</option>
                                <option value="1">Study Leave</option>

                                <option value="2">Not On Call</option>
                                <option value="9">Others(specify below)</option>
                            </Form.Select>

                            {
                                errorMsg.type_leave_required &&
                                <ErrorMessage className="mb-0">this field cannot be empty</ErrorMessage>
                            }
                            {
                                errorMsg.annual_leave_invalid && values.type_leave == 0 &&
                                <ErrorMessage className="mb-0">your annual leave is run out</ErrorMessage>
                            }
                            {
                                errorMsg.study_leave_invalid && values.type_leave == 1 &&
                                <ErrorMessage className="mb-0">your study leave is run out</ErrorMessage>
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
                                            values.single_half_full == "1" && 
                                            <Form.Select 
                                                id="single_half_full" 
                                                name="single_half_full" 
                                                className="p-2 me-1" 
                                                style={{ width: '30%' }} 
                                                onChange={(e) => setValue(e)}
                                                defaultValue="1"
                                            >
                                                <option value="0">Full</option>  
                                                <option value="1">a.m.</option>
                                                <option value="2">p.m.</option>
                                            </Form.Select>
                                            ||
                                            values.single_half_full == "2" && 
                                            <Form.Select 
                                                id="single_half_full" 
                                                name="single_half_full" 
                                                className="p-2 me-1" 
                                                style={{ width: '30%' }} 
                                                onChange={(e) => setValue(e)}
                                                defaultValue="2"
                                            >
                                                <option value="0">Full</option>  
                                                <option value="1">a.m.</option>
                                                <option value="2">p.m.</option>
                                            </Form.Select>
                                            ||
                                            values.single_half_full == "0" && 
                                            <Form.Select 
                                                id="single_half_full" 
                                                name="single_half_full" 
                                                className="p-2 me-1" 
                                                style={{ width: '30%' }} 
                                                onChange={(e) => setValue(e)}
                                                defaultValue="0"
                                            >
                                                <option value="0">Full</option>  
                                                <option value="1">a.m.</option>
                                                <option value="2">p.m.</option>
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
                                        <ErrorMessage className="mb-0">this field cannot be empty</ErrorMessage>
                                    }
                                    {
                                        errorMsg.single_day_invalid &&
                                        <ErrorMessage className="mb-0">start day cannot be earlier than today</ErrorMessage>
                                    }
                                </div>))
                            ||
                            (values.type_day === "multiple" &&
                                (<div className="d-block">
                                    <ColumnName className="mb-2">Start Date</ColumnName>

                                    <div className="d-flex">
                                        {
                                            values.multiple_start_half_full == "1" &&
                                            <Form.Select 
                                                id="multiple_start_half_full" 
                                                name="multiple_start_half_full" 
                                                className="p-2 me-1" 
                                                style={{ width: '30%' }} 
                                                onChange={(e) => setValue(e)}
                                                defaultValue="1"
                                            >
                                                <option value="0">Full</option>
                                                <option value="1">a.m.</option>
                                                <option value="2">p.m.</option>
                                            </Form.Select>
                                            ||
                                            values.multiple_start_half_full == "2" &&
                                            <Form.Select 
                                                id="multiple_start_half_full" 
                                                name="multiple_start_half_full" 
                                                className="p-2 me-1" 
                                                style={{ width: '30%' }} 
                                                onChange={(e) => setValue(e)}
                                                defaultValue="2"
                                            >
                                                <option value="0" >Full</option>
                                                <option value="1">a.m.</option>
                                                <option value="2">p.m.</option>
                                            </Form.Select>
                                            ||
                                            values.multiple_start_half_full == "0" &&
                                            <Form.Select 
                                                id="multiple_start_half_full" 
                                                name="multiple_start_half_full" 
                                                className="p-2 me-1" 
                                                style={{ width: '30%' }} 
                                                onChange={(e) => setValue(e)}
                                                defaultValue="0"
                                            >
                                                <option value="0">Full</option>
                                                <option value="1">a.m.</option>
                                                <option value="2">p.m.</option>
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
                                            <ErrorMessage className="mb-0">this field cannot be empty</ErrorMessage>
                                        }
                                        {
                                            errorMsg.multiple_start_day_invalid &&
                                            <ErrorMessage className="mb-0">start day cannot be earlier than today</ErrorMessage>
                                        }

                                    <ColumnName className="my-2">End Date</ColumnName>

                                    <div className="d-flex">

                                        {
                                            values.multiple_end_half_full == "1" &&
                                            <Form.Select 
                                                id="multiple_end_half_full" 
                                                name="multiple_end_half_full" 
                                                className="p-2 me-1" 
                                                style={{ width: '30%' }} 
                                                onChange={(e) => setValue(e)}
                                                defaultValue="1"
                                            >
                                                <option value="0">Full</option>
                                                <option value="1">a.m.</option>
                                                <option value="2">p.m.</option>
                                            </Form.Select>
                                            ||
                                            values.multiple_end_half_full == "2" &&
                                            <Form.Select 
                                                id="multiple_end_half_full" 
                                                name="multiple_end_half_full" 
                                                className="p-2 me-1" 
                                                style={{ width: '30%' }} 
                                                onChange={(e) => setValue(e)}
                                                defaultValue="2"
                                            >
                                                <option value="0">Full</option>
                                                <option value="1">a.m.</option>
                                                <option value="2">p.m.</option>
                                            </Form.Select>
                                            ||
                                            values.multiple_end_half_full == "0" &&
                                            <Form.Select 
                                                id="multiple_end_half_full" 
                                                name="multiple_end_half_full" 
                                                className="p-2 me-1" 
                                                style={{ width: '30%' }} 
                                                onChange={(e) => setValue(e)}
                                                defaultValue="0"
                                            >
                                                <option value="0">Full</option>
                                                <option value="1">a.m.</option>
                                                <option value="2">p.m.</option>
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
                                        <ErrorMessage className="mb-0">this field cannot be empty</ErrorMessage>
                                    }
                                    {
                                        errorMsg.multiple_end_day_invalid &&
                                        <ErrorMessage className="mb-0">end day cannot be earlier than today</ErrorMessage>
                                    }
                                    {
                                        errorMsg.multiple_start_end_invalid &&
                                        <ErrorMessage className="mb-0">end day cannot be earlier than start day</ErrorMessage>
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

                            <ConfirmButton className="m-2" type="button" onClick={handleSubmit}>
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

const ColumnName = styled.h3`
    font-size: 16px;
    font-weight: bold;
    color: #035eb8;
    margin-bottom: 0px;
`

const ErrorMessage = styled.p`
    font-size: 12px;
    padding: 3px;
    color: red;
    // display: none;
`
