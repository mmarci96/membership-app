import { useState } from 'react';
import UserDetailsForm from '../components/forms/UserDetailsForm';
import LoginForm from '../components/forms/LoginForm';
import SignupForm from '../components/forms/SignupForm';

const Account = () => {
  const [isLogin, setIsLogin] = useState(true);
	const [isLoggedIn, setIsLoggedIn] = useState(false);

	const handleToggle = () => {
		setIsLogin(!isLogin);
	};

	const handleLoginSuccess = () => {
		setIsLoggedIn(true);
	};

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
				<UserDetailsForm />
			)}
		</div>
	);
}

export default Account