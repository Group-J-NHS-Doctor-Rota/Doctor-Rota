import styled from 'styled-components'

import Pagination from '../components/Pagination'
import ManageModal from '../modals/ManageModal'
import { useState } from 'react'

export default function AccountPage() {
    const [currentPage, setCurrentPage] = useState(1)
    const [manage, setManage] = useState(false)

    const getContent = number => {
        let content = [];
        for (let i = 0; i < number; i++) {
            content.push(
                <AccountCard key={i} onClick={() => setManage(true)}>
                    <TableTd>Steven</TableTd>
                    <TableTd>stevenlin3263@gmail.com</TableTd>
                    <TableTd>1.0</TableTd>
                    <TableTd>employed</TableTd>
                </AccountCard>
            )
        }
        return content;
    }

    return (
        <div>

            <Container className="mt-3">
                <form id="search_account_form" className="d-flex mb-3" action="#" method="POST">
                    <SearchRegion className="me-3">
                        <div className="d-flex">
                            <i className="bi bi-search me-3" style={{ fontSize: '20px' }}></i>

                            <Input />
                        </div>
                    </SearchRegion>
                    <Button type="submit">Search</Button>
                </form>

                <table style={{ width: '100%' }}>
                    <tbody>
                        <AccountCard>
                            <TableTh>Name</TableTh>
                            <TableTh>Email</TableTh>
                            <TableTh>Time worked</TableTh>
                            <TableTh>Status</TableTh>
                        </AccountCard>

                        {getContent(6)}
                    </tbody>
                </table>
            </Container>
            <ManageModal manage={manage} setManage={setManage} />

            <Pagination currentPage={currentPage} totalPage={10} setCurrentPage={setCurrentPage} />
        </div>
    )
}

const Container = styled.div`
    margin: 0 auto;
    width: 90%;
`
const SearchRegion = styled.div`
    padding: 10px;
    max-width: 250px;
    width: 100%;
    border-radius: 5px;
    background-color: white;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
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
    background-color: #168082;
    border: none;
    border-radius: 5px;
`

const AccountCard = styled.tr`
    padding: 20px 0;
    width: 100%;
    border-radius: 5px;
    background-color: white;
    cursor: pointer;
    
    &:nth-child(even) {
        background-color: #EDFCF9;
    }
`

const TableTh = styled.th`
    padding: 10px 10px 20px 10px;
`

const TableTd = styled.td`
    padding: 10px;
`