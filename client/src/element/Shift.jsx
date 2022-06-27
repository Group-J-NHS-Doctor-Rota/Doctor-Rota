import React from 'react'

import styled from 'styled-components'

export default function Shift({ type }){
    return (
        <List className="d-flex mb-1 p-2" style={{cursor: 'pointer'}}>
            {
                type == "night" && 
                <div className="d-flex justify-content-center align-items-center px-1 py-2 rounded" style={{backgroundColor: '#eaf0fa'}}>
                    <i className="bi bi-moon-fill" style={{color: '#084298'}}></i>
                </div>
                || type == "morning" &&
                <div className="d-flex justify-content-center align-items-center px-1 py-2 rounded" style={{backgroundColor: '#fff0c2'}}>
                    <i className="bi bi-brightness-high-fill" style={{color: '#ffc107'}}></i>
                </div>
            }

            <div className="d-flex align-items-center ms-2">
                <p className="mb-0" style={{fontSize: '13px'}}>10:00-12:00 Steven</p>
            </div>

        </List>
    )
}

const List = styled.div`
    width: 100%;
    border-radius: 5px;
    background-color: white;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);

    &:hover {
        transform: scale(1.05);
        font-weight: bold;
    }

`