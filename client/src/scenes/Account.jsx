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
	const handleLoginSuccess = () => setIsLoggedIn(true)
	const fetchUserDetails = async (token) => {
		const res = await fetch(`/api/users/account/${user.userId}`, {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json',
				Authorization: `Bearer ${token}`,
			}
		});
		return await res.json();
	}


	useEffect(() => {
		if(user.userId){
			setIsLoggedIn(true)
		}
		if(user.token){
			fetchUserDetails(user.token)
				.then(data => setUserDetails(data))
		}
	}, [user]);

	return (
		<div className='auth-container'>
			{!isLoggedIn ? (
				<div>
					<h2>{isLogin ? 'Login' : 'Sign Up'}</h2>
					{isLogin ? (
						<LoginForm onLoginSuccess={handleLoginSuccess} />
					) : (
						<SignupForm onSignupSuccess={handleLoginSuccess} />
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