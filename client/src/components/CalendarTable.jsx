import React, { useState, useEffect } from 'react'

import CalendarDay from './CalendarDay'
import useWindowDimensions from '../hook/useWindowDimensions'

import { useUrl } from '../contexts/UrlContexts' 

import dayjs from 'dayjs'

import styled from 'styled-components'

// https://date.nager.at/swagger/index.html
// https://day.js.org/docs/en/plugin/dev-helper

export default function CalendarTable({ year, month }) {
    const { width } = useWindowDimensions();

    let isLeapYear = require('dayjs/plugin/isLeapYear')
    dayjs.extend(isLeapYear)

    let isLeap = dayjs(`${year}`).isLeapYear()

    const bigMonth = [1, 3, 5, 7, 8, 10, 12]
    const smallMonth = [4, 6, 9, 11]

    const [bankHolidays, setBankHolidays] = useState([])
    const [allShift, setAllShift] = useState()

    const { getUrl } = useUrl()

    const url =  getUrl()

    useEffect(() => {
        fetch(`https://date.nager.at/api/v3/PublicHolidays/${year}/GB`)
            .then(response => response.json())
            .then(data => {
                const holidays = data.map((holiday) => ({
                    day: getDate(holiday.date),
                    month: getMonth(holiday.date),
                    name: holiday.localName
                }))

                setBankHolidays(holidays)
            })

        fetch(`${url}shift/${year}?accountId=1`, {
            mode: 'cors',
            method: "GET",
            headers: {
                'Access-Control-Allow-Origin': '*',
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        })
        .then(response => response.json())
        .then(data => setAllShift(data.shifts))
    }, [year])


    function getMonth(date) {
        if (date.slice(5, 7)[0] === 0) {
            return date.slice(6, 7)
        }
        return date.slice(5, 7)
    }
    
    function getDate(date) {
        if (date.slice(8, 10)[0] === 0) {
            return date.slice(9, 10)
        }
        return date.slice(8, 10)
    }
    
    function getHoliday(day) {
        for (let i = 0; i < bankHolidays.length; i++) {
            if (bankHolidays[i].day == day && bankHolidays[i].month == month) {
                return bankHolidays[i].name
            }
        }
        return false
    }

    function theWeek(){
        const date = year + "-" + month + "-" + "1"

        return new Date(date).getDay()
    }

    const getContent = number => {
        let content = [];
        const theWeekResult = theWeek()

        for(let i = 0; i < theWeekResult-1; i++){
            content.push(
                <GridItems key={`grid-items-${i}`}/>
            )
        }

        for (let i = 1; i < number + 1; i++) {
            let result = getHoliday(i)
            content.push(
                ((result == false) &&
                    (<GridItems key={i}>
                        <h3 className="my-2 mx-2">{i}</h3>
                        <CalendarDay allShift={allShift} year={year} month={month} day={i}/>
                    </GridItems>))
                || ((result != false) &&
                    (<GridItems key={i}>
                        <div className="d-flex">
                            <h3 className="my-2 mx-2">{i}</h3>
                            {
                                (result.length > 14 && width >= 1200 &&
                                    <h3 className="my-2 mx-1">{result.slice(0, 14)} ...</h3>)
                                ||
                                (result.length > 14 && width >= 1100 && width < 1200 &&
                                    <h3 className="my-2 mx-1">{result.slice(0, 10)} ...</h3>)
                                ||
                                (result.length > 8 && width >= 915 && width < 1200 &&
                                    <h3 className="my-2 mx-1">{result.slice(0, 6)} ...</h3>)
                                ||
                                (<h3 className="my-2 mx-1">{result}</h3>)
                            }
                        </div>
                        <CalendarDay allShift={allShift} year={year} month={month} day={i} holiday={result}/>
                    </GridItems>))
            )
        }
        return content;
    }

    return (
        <Table className='mb-5'>
            <CalendarGrid>
                {
                    (bigMonth.includes(month) && getContent(31))
                    ||
                    (smallMonth.includes(month) && getContent(30))
                    ||
                    (month === 2 && isLeap === true && getContent(29))
                    ||
                    (month === 2 && isLeap === false && getContent(28))
                }
            </CalendarGrid>
        </Table>
    )
}

const Table = styled.div`
    width: 100%;
    padding: 10px;
    column-gap:10px;
    border-radius: 5px;
    background-color: white;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
`

const CalendarGrid = styled.div`
    display: grid;
    grid-template-columns: 1fr;
    
    @media (min-width: 915px){
        grid-template-columns: repeat(7, 1fr)
    }
`
const GridItems = styled.div`
    padding: 5px;
    border-radius: 5px;
    cursor: pointer;

    &:hover {
        background-color: #f5f9fe;
    }

    h3 {
        font-size: 13px;
    }
`
