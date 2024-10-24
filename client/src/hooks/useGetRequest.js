import {useState} from "react";
import {useGlobalContext} from "./useGlobalContext.js";

const useGetRequest = () => {
    const { user } = useGlobalContext()
    const [error, setError] = useState(null)
    const [loading, setLoading] = useState(false)
    const handleGetData = async (path, token = user.token) => {
        try {
            setLoading(true);
            const res = await fetch(path, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });

            const text = await res.text();  // Read response as text
            const data = text && JSON.parse(text); // Parse JSON if possible

            setLoading(false);
            if (!res.ok) {
                setError('Error with status: ' + res.status);
            }
            return data;
        } catch (e) {
            console.log(e);
            setError('Error: ' + e.message);
        } finally {
            setLoading(false);  // Always set loading to false at the end
        }
    };


    return {
        error,
        loading,
        handleGetData
    }
}

export default useGetRequest