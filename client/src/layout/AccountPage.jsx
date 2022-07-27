import { useState, useEffect } from 'react'

import { Form } from "react-bootstrap"

import { useUrl } from '../contexts/UrlContexts' 

import Pagination from '../components/Pagination'
import ManageModal from '../modals/ManageModal'
import CreateUsersModal from '../modals/CreateUsersModal'

import useWindowDimensions from '../hook/useWindowDimensions'

import styled from 'styled-components'

export default function AccountPage() {
    const [currentPage, setCurrentPage] = useState(1)
    const [totalPage, setTotalPage] = useState(0)
    
    const [accounts, setAccounts] = useState()
    const [accountId, setAccountId] = useState()
    const [search, setSearch] = useState("")
    
    const [manage, setManage] = useState(false)
    const [create, setCreate] = useState(false)


    const { width } = useWindowDimensions();

    const { url, getUrl } = useUrl()

    useEffect(() => {
        getUrl()
    }, [])

    useEffect(() => {
        fetch('https://doctor-rota-spring-develop.herokuapp.com/account', {
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
                // const totalNumber = data.accounts.length
                // setTotalPage(Math.ceil(totalNumber/6))
                // setAccounts(data.accounts)
                const result = data.accounts.filter(result => result.username.includes(search))
                setTotalPage(Math.ceil(result.length/6))
                setAccounts(result)
            })
    }, [search])

    function handleClick(id) {
        setAccountId(id)
        setManage(true)
    }

    const getTableContent = () => {
        let content = [];

        accounts.filter(account => (currentPage) * 6 >= account.id && account.id > (currentPage - 1) * 6)
            .map((account) => (
                content.push(
                    <AccountCard key={account.id} onClick={() => handleClick(account.id)}>
                        <TableTd>{account.username}</TableTd>
                        <TableTd>{account.email}</TableTd>
                        <TableTd>{account.timeWorked}</TableTd>
                        {
                            account.doctorStatus == 1 &&
                            <TableTd>employed</TableTd>
                            ||
                            account.doctorStatus == 0 &&
                            <TableTd>not employed</TableTd>
                        }
                    </AccountCard>
                )
            ))
        return content;
    }

    const getCardContent = () => {
        let content = [];

        accounts.filter(account => (currentPage) * 6 > account.id && account.id > (currentPage - 1) * 6)
            .map((account) => (
                content.push(
                    <AccountCardV2 key={account.id} className="d-block m-1 p-3" onClick={() => handleClick(account.id)}>
                        <div className="d-flex justify-content-between">
                            <div>Name</div>
                            <div>{account.username}</div>
                        </div>
                        <div className="d-flex justify-content-between">
                            <div>Email</div>
                            <div>{account.email}</div>
                        </div>
                        <div className="d-flex justify-content-between">
                            <div>WTE</div>
                            <div>1.0</div>
                        </div>
                        <div className="d-flex justify-content-between">
                            <div>Status</div>
                            <div>employed</div>
                        </div>
                    </AccountCardV2>
                )
            ))
        return content;
    }

    function onChange(e){
        setSearch(e.target.value)
    }

    function handleSubmit(e){
        if(e && e.preventDefault){
            e.preventDefault()
        }        

        try{
            fetch('https://doctor-rota-spring-develop.herokuapp.com/account', {
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
                const result = data.accounts.filter(result => result.username.includes(search))
                setTotalPage(Math.ceil(result.length/6))
                setAccounts(result)
            })
        }catch(error){
            console.log(error)
        }
    }

    return (
        <div>
            <PageContainer className="mt-3">
                <div className='d-flex justify-content-between'>
                    <div>
                        <Form id="search_account_form" className="d-flex mb-3" onSubmit={handleSubmit}>
                            <SearchRegion className="me-3">
                                <div className="d-flex">
                                    <i className="bi bi-search me-3" style={{ fontSize: '20px' }}></i>

                                    <Input placeholder="Search" onChange={onChange}/>
                                </div>
                            </SearchRegion>
                            {/* <Button type="submit" onClick={() => handleSubmit()}>Search</Button> */}
                        </Form>
                    </div>
                    <Button className='mb-3' onClick={() => { setCreate(true) }}><i className="bi bi-person-plus-fill me-2" />Create Users</Button>
                </div>
                {
                    width > 765 &&
                    <table style={{ width: '100%' }}>
                        <tbody>
                            <AccountCard>
                                <TableTh>Name</TableTh>
                                <TableTh>Email</TableTh>
                                <TableTh>WTE</TableTh>
                                <TableTh>Status</TableTh>
                            </AccountCard>

                            {
                                accounts != undefined &&
                                getTableContent()
                            }
                        </tbody>
                    </table>
                    ||
                    <AccountGrid>
                        {
                            accounts != undefined &&
                            getCardContent()
                        }
                    </AccountGrid>
                }

            </PageContainer >

            <ManageModal accountId={accountId} manage={manage} setManage={setManage} />
            <CreateUsersModal create={create} setCreate={setCreate} />
            <Pagination currentPage={currentPage} totalPage={totalPage} setCurrentPage={setCurrentPage} />
        </div >
    )
}

const PageContainer = styled.div`
    margin: 0 auto;
    width: 90%;
`
const SearchRegion = styled.div`
    padding: 10px;
    max-width: 250px;
    width: 100%;
    border-radius: 5px;
    background-color: white;
    box-shadow: 0 0px 8px 0px rgba(0, 0, 0, 0.1), 0 6px 2px 0 rgba(0, 0, 0, 0.08);
`

const Input = styled.input`
    font-size: 18px;
    width: 100%;
    border: none;
    border-radius: 5px;
    outline: none;
`

const Button = styled.button`
    padding: 5px 25px;
    color: white;
    font-size: 20px;
    font-weight: bold;
    background-color: #035eb8;
    border: none;
    border-radius: 5px;
    box-shadow: 0 0px 8px 0px rgba(0, 0, 0, 0.1), 0 6px 2px 0 rgba(0, 0, 0, 0.08);

    @media{
        display: block !important;
    }
`

const AccountCard = styled.tr`
    padding: 20px 0;
    width: 100%;
    border-radius: 5px;
    background-color: white;
    cursor: pointer;
    
    &:nth-child(even) {
        background-color: #f5f9fe;
    }
`

const TableTh = styled.th`
    padding: 10px 10px 20px 10px;
`

const TableTd = styled.td`
    padding: 10px;
`

const AccountGrid = styled.div`
    display: grid;
    grid-template-columns: 1fr;

    @media (min-width: 575px){
        grid-template-columns: repeat(2, 1fr)
    }

`

const AccountCardV2 = styled.div`
    background-color: white;
    border: none;
    border-radius: 5px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
    cursor: pointer;
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