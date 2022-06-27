import React from 'react'

import styled from 'styled-components'

export default function Pagination({currentPage, totalPage, setCurrentPage}){

    function goToPage(page){
        setCurrentPage(page)
    }
    
    const getContent = number => {
        let content = [];
        let total = number + 1
        for (let i = 1; i < total; i++) {
            if(i == currentPage){
                content.push(<Wrapper key={i} className="d-flex justify-content-center align-items-center me-1" style={{backgroundColor: '#168082'}}><div className="mb-0" style={{color: 'white'}}>{i}</div></Wrapper>)
            }else{
                content.push(<Wrapper key={i} className="d-flex justify-content-center align-items-center me-1" onClick={() => goToPage(i)}><div className="mb-0">{i}</div></Wrapper>)
            }
        }
        return content;
    }

    return (
        <div className="d-flex justify-content-center my-3">
            {
                currentPage !== 1 &&
                <Wrapper className="d-flex justify-content-center align-items-center me-1" onClick={() => goToPage(currentPage-1)}>
                    <i className="bi bi-arrow-left-short" style={{fontSize: '24px'}}></i>
                </Wrapper>
            }
            
            {
                getContent(totalPage)
            }

            {
                currentPage !== totalPage &&
                <Wrapper className="d-flex justify-content-center align-items-center me-1" onClick={() => goToPage(currentPage+1)}>
                    <i className="bi bi-arrow-right-short" style={{fontSize: '24px'}}></i>
                </Wrapper>
            }

        </div>
    )
}

const Wrapper = styled.div`
    height: 40px;
    width: 30px;
    padding: 5px;
    border-radius: 5px;
    background-color: white;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
    cursor: pointer;
`