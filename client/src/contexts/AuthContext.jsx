import React, { useState, useEffect, useContext } from 'react'

import { useUrl } from './UrlContexts'

import useLocalStorage from '../hook/useLocalStorage'

const AuthContext = React.createContext()

export function useAuth() {
    return useContext(AuthContext)
}

export function AuthProvider({ children }) {
    const [values, setValues] = useLocalStorage('auth', {
        id: "",
        token: "",
        level: ""
    })

    const { getUrl } = useUrl()

    const url =  getUrl()

    function login(username, password){
        try{
            if(url != undefined){
                console.log(username)
                console.log(password)
                fetch(`${url}login?username=${username}`, {
                    mode: 'cors',
                    method: "GET",
                    headers: {
                        'Access-Control-Allow-Origin': '*',
                        'Content-Type': 'application/json',
                        'Accept': 'application/json',
                        'password': password,
                        'Access-Control-Allow-Credentials': true
                    }
                })
                .then(response => response.json())
                .then(data => {
                    setValues({
                        id: data.accountId,
                        token: data.token,
                        level: data.level
                    })
                })
            }
        }catch(error){
            console.log(error)
        }
    }

    const value = {
        login
    }

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    )
}