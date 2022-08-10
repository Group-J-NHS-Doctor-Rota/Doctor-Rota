import { Modal, Form } from "react-bootstrap"
import styled from "styled-components"
import { useState, useEffect } from "react"
import { useUrl } from '../contexts/UrlContexts'

import CreateUserInput from "../components/CreateUserInput"


const CreateUsersModal = ({ create, setCreate }) => {
    const { getUrl } = useUrl()
    const url = getUrl()

    const [inputId, setInputId] = useState(1)

    const [userList, setUserList] = useState([
        { id: 0, username: '', email: '' },
    ])
    const onChange = (e, index) => {
        const list = [...userList]
        list[index][e.target.name] = e.target.value
        // setUserList({ ...userList, [e.target.name]: e.target.value })
        setUserList(list)
    }

    const handleAddInput = (user) => {
        setInputId(inputId+1)
        setUserList([...userList, { id: inputId, username: '', email: '' }])
    }

    const handleRemoveInput = (user) => {
        const newList = userList.filter((eachUser) => eachUser.id != user.id)

        setUserList(newList)
    }

    console.log(userList)

    // useEffect(() => {
    //     try {
    //         if (url !== undefined) {
    //             fetch(`${url}account?username=Steve Lin&email=steven123@gmail.com&username=Dennis liu`, {
    //                 mode: 'cors',
    //                 method: 'POST',
    //                 headers: {
    //                     'Access-Control-Allow-Origin': '*',
    //                     'Content-Type': 'application/json',
    //                     'Accept': 'application/json',
    //                     "Access-Control-Allow-Credentials": true,

    //                 }
    //             })
    //             .then(response => {
    //                 console.log(response)
    //             })
    //         }
    //     } catch (error) {
    //         console.log(error)
    //     }
    // }, [])

    const inputs = [
        {
            id: 1,
            name: "username",
            type: "text",
            placeholder: "Username",
            errorMessage:
                "Invalid Username!",
            label: "Username",
            pattern: "[a-zA-Z \./']+",
            required: true,
        },
        {
            id: 2,
            name: "email",
            type: "email",
            placeholder: "Email",
            errorMessage:
                "Invalid Email!",
            label: "Email",
            required: true,
        }
    ];

    const handleCancel = () => {

    }


    return (
        <>
            <Modal show={create}>
                <ModalContainer>
                    <div className="d-flex justify-content-between">
                        <ModalTitle className="my-5">Create Users</ModalTitle>
                    </div>

                    <CreateRegion className="p-4">
                        {userList.map((user, index) => {
                            // console.log(user)
                            return (
                                index + 1 <= 10 &&
                                (
                                    <div key={index} className="d-block mb-3">
                                        <div className="d-flex justify-content-between align-items-center">
                                            <ColumnName>New User&nbsp;{index + 1}</ColumnName>
                                            <div className="d-flex justify-content-center align-items-center">
                                                {
                                                    userList.length - 1 === index &&
                                                    (
                                                        <>
                                                            <Icon className="bi bi-plus-circle"
                                                                onClick={() => handleAddInput(user)} />
                                                        </>
                                                    )
                                                }
                                                {
                                                    userList.length !== 1 &&
                                                    (
                                                        <>
                                                            <Icon className="bi bi-dash-circle"
                                                                onClick={() => handleRemoveInput(user)} />
                                                        </>
                                                    )
                                                }
                                            </div>
                                        </div>

                                        {
                                            inputs.map(input => (
                                                <CreateUserInput
                                                    key={input.id}
                                                    {...input}
                                                    value={user[input.name]}
                                                    onChange={e => onChange(e, index)}
                                                />
                                            ))
                                        }

                                    </div>

                                )
                                || <p style={{ color: 'red' }}>The maximum is 10 users in one go!</p>
                            )
                        })}
                    </CreateRegion>

                    <div className="d-flex justify-content-center my-4">
                        <CloseButton className="m-2" onClick={() => setCreate(false)}>
                            Close
                        </CloseButton>
                        <ConfirmButton className="m-2" onClick={() => setCreate(false)}>
                            Create
                        </ConfirmButton>
                    </div>
                </ModalContainer>
            </Modal>
        </>
    )
}

export default CreateUsersModal

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

const CreateRegion = styled.div`
    overflow: scroll;
    height: 400px;
    border-radius: 5px;
    background-color: white;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
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

const Icon = styled.i`
    font-size: 23px;
    margin-left: 12px;
    cursor: pointer;
`