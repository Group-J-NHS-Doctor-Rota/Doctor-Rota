import React, { useState } from 'react'

import ShiftDetailModal from '../modals/ShiftDetailModal'

import styled from 'styled-components'

export default function Shift({ data }) {
    const [shift, setShift] = useState(false)
    const [accountId, setAccountId] = useState(3)

    return (
        <>
            <List className="d-flex mb-1 p-2" onClick={() => setShift(true)}>
                {
                    data.type == 0 &&
                    <div className="d-flex justify-content-center align-items-center px-1 py-2 rounded" style={{ backgroundColor: '#f285003d' }}>
                        <i className="bi bi-brightness-alt-high-fill" style={{ color: '#f28500' }}></i>
                    </div>
                    ||
                    data.type == 1 &&
                    <div className="d-flex justify-content-center align-items-center px-1 py-2 rounded" style={{ backgroundColor: '#fff0c2' }}>
                        <i className="bi bi-brightness-high-fill" style={{ color: '#ffc107' }}></i>
                    </div>
                    ||
                    data.type == 2 &&
                    <div className="d-flex justify-content-center align-items-center px-1 py-2 rounded" style={{ backgroundColor: '#eaf0fa' }}>
                        <i className="bi bi-moon-fill" style={{ color: '#084298' }}></i>
                    </div>
                    ||
                    data.type == 3 &&
                    <div className="d-flex justify-content-center align-items-center px-1 py-2 rounded" style={{ backgroundColor: '#dc354521' }}>
                        <i className="bi bi-escape" style={{ color: '#E30613' }}></i>
                    </div>
                }

                <div className="d-flex align-items-center ms-2">
                    <p className="mb-0" style={{ fontSize: '13px' }}>{data.username}</p>
                </div>

            </List>

            <ShiftDetailModal data={data} shift={shift} setShift={setShift} accountId={accountId} />
        </>

    )
}

const List = styled.div`
    width: 100%;
    border-radius: 5px;
    cursor: pointer;
    background-color: white;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);

    &:hover {
        transform: scale(1.05);
        font-weight: bold;
    }
`