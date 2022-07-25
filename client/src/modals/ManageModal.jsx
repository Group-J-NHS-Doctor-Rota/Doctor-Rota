import styled from "styled-components"
import { Modal, Row, Col, Form } from "react-bootstrap"
import { useState, useEffect } from "react"

const ManageModal = ({ accountId, manage, setManage }) => {
    const [disable, setDisable] = useState(true)
    const [accountDetail, setAccountDetail] = useState()

    const toggleDisable = () => setDisable(!disable)

    useEffect(() => {
        if(accountId != undefined){
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
            .then(data => {
                setAccountDetail(data)
            })
        }        
    }, [accountId])

    const onChange = (e) => {
        setAccountDetail({ ...accountDetail, [e.target.name]: e.target.value });
        
    };
    console.log(accountDetail)

    function handleSubmit(e){
        e.preventDefault()


        try{
            fetch(`https://doctor-rota-spring-develop.herokuapp.com/account/${accountId}`, {
                mode: 'cors',
                method: 'PATCH',
                headers: {
                    'Access-Control-Allow-Origin': '*',
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                    "Access-Control-Allow-Credentials" : true
                },
                body: JSON.stringify({
                    annualLeave: 16
                }),
            })
            .then(response => response.json())
            .then(data => console.log(data))
        }catch(error){
            console.log(error)
        }

        setManage(false)
    }

    return (
        <>
            <Modal show={manage}>
                <ModalContainer>

                    <div className="d-flex justify-content-between align-items-center">
                        <ModalTitle className="my-5">
                            {   
                                accountDetail != undefined && accountDetail.username != undefined &&
                                accountDetail.username
                            }
                        </ModalTitle>
                        <EditIcon onClick={toggleDisable}
                        ><i className="bi bi-pencil-square" /></EditIcon>
                    </div>
                    <Form action="#" onSubmit={handleSubmit}>
                        <div className="show-grid align-items-center">
                            <Row>
                                {/* left */}
                                <Col xs={12} md={6}>
                                    <Label htmlFor="field-1" className="mt-0">Annual Leave Entitled</Label>
                                </Col>
                                {/* right */}

                                {
                                    accountDetail != undefined && accountDetail.annualLeave != undefined &&
                                    <Col xs={12} md={6}>
                                        <input 
                                            id="field-1"
                                            type="text"
                                            name="annualLeave"
                                            className="mt-0" 
                                            value={accountDetail.annualLeave}
                                            disabled={disable}
                                            autoFocus  
                                            onChange={onChange}
                                        />
                                    </Col>
                                }
                            </Row>

                            <Row>
                                <Col xs={12} md={6}>
                                    <Label htmlFor="field-2">Study Leave Entitled</Label>
                                </Col>
                                
                                {
                                    accountDetail != undefined && accountDetail.studyLeave != undefined &&
                                    <Col xs={12} md={6}>
                                        <input 
                                            id="field-2" 
                                            type="text"
                                            name="studyLeave"
                                            className="mt-2"
                                            value={accountDetail.studyLeave}
                                            disabled={disable} 
                                            onChange={onChange}
                                        />
                                    </Col>
                                }
                            </Row>

                            <Row>
                                <Col xs={12} md={6}>
                                    <Label htmlFor="field-3">Hours Required</Label>
                                </Col>
                
                                {
                                    accountDetail != undefined && accountDetail.workingHours != undefined &&
                                    <Col xs={12} md={6}>
                                        <input 
                                            id="field-3" 
                                            type="text"
                                            name="workingHours"
                                            className="mt-2" 
                                            value={accountDetail.workingHours}
                                            disabled={disable}
                                            onChange={onChange}
                                        />
                                    </Col>
                                }
                            </Row>

                            <Row>
                                <Col xs={12} md={6}>
                                    <Label htmlFor="field-3">WTE/FTE</Label>
                                </Col>
                                <Col xs={12} md={6}>
                                    <input 
                                        id="field-3" 
                                        type="text"
                                        placeholder="0.6 LTFT"
                                        className="mt-2" 
                                        disabled={disable} 
                                    />
                                </Col>
                            </Row>

                            <Row>
                                <Col xs={12} md={6}>
                                    <Label htmlFor="field-4">Account Type</Label>
                                </Col>
                                <Col xs={12} md={6}>
                                    {
                                        accountDetail != undefined && accountDetail.level == 1 && 
                                        <Form.Select 
                                            id="field-4"
                                            name="level"
                                            className="mt-2" 
                                            disabled={disable} 
                                            onChange={onChange}
                                            defaultValue="1"
                                        >
                                            <option value="0">Standard</option>
                                            <option value="1">Admin</option>
                                        </Form.Select>
                                        ||
                                        accountDetail != undefined && accountDetail.level == 0 && 
                                        <Form.Select 
                                            id="field-4"
                                            name="level"
                                            className="p-2 mt-2" 
                                            disabled={disable} 
                                            onChange={onChange}
                                            defaultValue="0"
                                        >
                                            <option value="0">Standard</option>
                                            <option value="1">Admin</option>
                                        </Form.Select>
                                    }
                                </Col>
                            </Row>

                            <Row>
                                <Col xs={12} md={6}>
                                    <Label htmlFor="field-6">Rota Type</Label>
                                </Col>
                                <Col xs={12} md={6}>
                                    <Form.Select className="p-2 mt-2" id="field-6" disabled={disable}>
                                        <option value="default_rota" defaultValue>-Please select-</option>
                                        <option value="first_on">First On</option>
                                        <option value="obstetric">Obstetric</option>
                                        <option value="third_on">Third On</option>
                                        <option value="fourth_on">Fourth On</option>
                                    </Form.Select>
                                </Col>
                            </Row>
                        </div>
                    </Form>

                    <div className="d-flex justify-content-center my-3">
                        <CloseButton className="m-2" type="button" onClick={() => {
                            setManage(false)
                            setDisable(true)
                        }}>
                            Close
                        </CloseButton>

                        <ConfirmButton className="m-2" type="submit" onClick={handleSubmit}>
                            Update
                        </ConfirmButton>
                    </div>
                </ModalContainer>
            </Modal>
        </>
    )
}

export default ManageModal

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

const Label = styled.label`
    display: block;
    font-size: 16px;
    font-weight: bold;
    color: #035eb8;
    margin: 13px 0;
`

const EditIcon = styled.div`
    font-size: 30px;
    color: #035eb8;
    font-weight: bold;
    cursor: pointer;
`

// const Select = styled.select`
//     font-size: 16px;
//     height: 36px;
//     width: 100%;
//     margin-bottom: 6px;
//     border: none;
//     border-radius: 5px;
//     box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
// `