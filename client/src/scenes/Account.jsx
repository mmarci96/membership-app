import {useEffect, useState} from 'react';
import UserDetailsForm from '../components/forms/UserDetailsForm';
import LoginForm from '../components/forms/LoginForm';
import SignupForm from '../components/forms/SignupForm';
import {useGlobalContext} from "../hooks/useGlobalContext.js";

const Account = () => {
	const { user } = useGlobalContext();
	const [userDetails, setUserDetails] = useState(null)
  const [isLogin, setIsLogin] = useState(true);
	const [isLoggedIn, setIsLoggedIn] = useState(false);

	const handleToggle = () => setIsLogin(!isLogin)

	const fetchUserDetails = async (token, userId) => {
		const res = await fetch(`/api/users/account/${userId}`, {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json',
				Authorization: `Bearer ${token}`,
			}
		});
		return await res.json();
	}


	useEffect(() => {
		const getUserDetails = async () => {
			if (user.token && user.userId) {
				setIsLoggedIn(true);
				try {
					const data = await fetchUserDetails(user.token, user.userId);
					if (data) {
						setUserDetails(data);
					}
				} catch (error) {
					console.log('User has not filled out form', error);
					// Optionally, handle different error statuses (e.g., show default values)
					setUserDetails(null); // Assuming 'null' means no details set yet
				}
			}
		};

		getUserDetails();
	}, [user.userId, user.token]);


	return (
		<div className='auth-container'>
			{!isLoggedIn ? (
				<div>
					<h2>{isLogin ? 'Login' : 'Sign Up'}</h2>
					{isLogin ? (
						<LoginForm  />
					) : (
						<SignupForm  />
					)}
					<button onClick={handleToggle}>
						{isLogin
							? "Don't have an account? Sign Up"
							: 'Already have an account? Log In'}
					</button>
				</div>
			) : (

				<UserDetailsForm userDetails={userDetails} />
			)}
		</div>
	);
}

export default Account