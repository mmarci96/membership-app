import { useState, useEffect } from 'react';
const useAuthToken = () => {
    const [user, setUser] = useState({ userId: '', token: ''});
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);

    const updateUserFromLocalStorage = () => {
        const token = localStorage.getItem('token');
        const userId  = localStorage.getItem('userId');
        if (token) {
            setUser({ userId : userId, token: token });
        } else {
            setUser({ userId: '', token: '' });
        }
    };

    useEffect(() => {
        updateUserFromLocalStorage();
        setLoading(false);

        const handleStorageChange = (event) => {
            if (event.key === 'token' || event.key === 'userId') {
                updateUserFromLocalStorage();
            }
        };

        window.addEventListener('storage', handleStorageChange);

        return () => {
            window.removeEventListener('storage', handleStorageChange);
        };
    }, []);

    return { user, setUser, error, setError, loading };
}

export default useAuthToken