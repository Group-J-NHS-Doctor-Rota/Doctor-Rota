import { Modal } from "react-bootstrap"
import styled from "styled-components"
import { useState } from "react"
import { useUrl } from '../contexts/UrlContexts'

import FormInput from '../components/FormInput';

import CreateUserInput from "../components/CreateUserInput"


const CreateUsersModal = ({ create, setCreate }) => {
    const auth = JSON.parse(localStorage.getItem('auth'))

    const { getUrl } = useUrl()
    const url = getUrl()

    const [inputId, setInputId] = useState(1)
    const [userList, setUserList] = useState([
        { id: 0, username: '', email: '' },
    ])

    const [errorMsg, setErrorMsg] = useState({
        usernameRepeated: false,
    })

    const onChange = (e, index) => {
        const list = [...userList]
        list[index][e.target.name] = e.target.value
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
            label: "Email"
        }
    ];

    const handleCancel = () => {
        setUserList([
            { id: 0, username: '', email: '' },
        ])
        setInputId(1)
        setErrorMsg({
            usernameRepeated: false,
        })
        setCreate(false)
    }

    // function handleString(){
    //     let string = ""
        
    //     for(let i = 0; i < userList.length; i++){
    //         string = string + "username="
    //         string = string + userList[i].username
    //         string = string + "&"
    //         string = string + "email="
    //         string = string + userList[i].email

    //         if(i != userList.length - 1){
    //             string = string + "&"
    //         }
    //     }

    //     return string
    // }

    function containsDuplicates(array){        
        // call API get all account username here
        // 重複的帳號列在下方一起顯示
        if (array.length !== new Set(array).size) {
            return true;
        }
        
        return false;
    }
    
    function handleSubmit(e){
        e.preventDefault()
        
        let array = []
        
        for(let i = 0; i < userList.length; i++){
            array.push(userList[i].username)
        }

        if(containsDuplicates(array)){
            setErrorMsg({... errorMsg, ["usernameRepeated"]: true})
        }else{
            
            try {
                if (url !== undefined) {
                    for(let i = 0; i < userList.length; i++){
                        fetch(`${url}account?username=${userList[i].username}&email=${userList[i].email}`, {
                            mode: 'cors',
                            method: 'POST',
                            headers: {
                                'Access-Control-Allow-Origin': '*',
                                'Content-Type': 'application/json',
                                'Accept': 'application/json',
                                "Access-Control-Allow-Credentials": true,
                                'token': auth.token
                            }
                        })
                        .then(response => {
                            if(response.status == 409){
                                setErrorMsg({... errorMsg, ["usernameRepeated"]: true})
                            }  
                        })
                    }
                }
            } catch (error) {
                console.log(error)
            }
        }
    }

    return (
        <>
            <Modal show={create}>
                <ModalContainer>
                    <div className="d-flex justify-content-between">
                        <ModalTitle className="my-5">Create Users</ModalTitle>
                    </div>

                    <form id="create_form" onSubmit={handleSubmit}>
                        <CreateRegion className="p-4">
                            {userList.map((user, index) => {
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
                                                    <FormInput
                                                        key={input.id}
                                                        {...input}
                                                        value={user[input.name]}
                                                        display="false"
                                                        onChange={e => onChange(e, index)}
                                                    />
                                                ))
                                            }

                                        </div>

                                    )
                                    || <p key="11" style={{ color: 'red' }}>The maximum is 10 users in one go!</p>
                                )
                            })}
                        </CreateRegion>
                        
                        {
                            errorMsg.usernameRepeated &&
                            <ErrorMessage className="mt-2 mb-0">Username are repeated</ErrorMessage>
                        }

                        <div className="d-flex justify-content-center my-4">
                            <CloseButton 
                                className="m-2"      
                                onClick={handleCancel}
                            >
                                Close
                            </CloseButton>
                            <ConfirmButton 
                                className="m-2" 
                            >
                                Create
                            </ConfirmButton>
                        </div>
                    </form>
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

const ErrorMessage = styled.p`
    font-size: 12px;

    padding: 3px;
    color: red;
`