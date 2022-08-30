import { useEffect } from "react"

import { useLocation, Navigate, Outlet } from "react-router"


export default function PrivateRouter() {
    const auth = JSON.parse(localStorage.getItem('auth'))

    const location = useLocation()

    return (
        auth ? <Outlet /> : <Navigate to="/signin" state={{ from: location }} replace />
    )
}