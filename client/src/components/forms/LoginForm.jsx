/** @format */

import useForm from '../../hooks/useForm.js';
import FormInput from './FormInput';

const LoginForm = ({ onLoginSuccess }) => {
	const { formData, handleChange, handleSubmit, loading, error } = useForm(
		{
			email: '',
			password: '',
			remember_me: false,
		},
		'/api/auth/login'
	);

	const handleLogin = async e => {
		const data = await handleSubmit(e);
		if (data) {
			localStorage.setItem('userId', data?.userId);
			localStorage.setItem('token', data?.token);
			onLoginSuccess();
		}
	};

	return (
		<div>
			<h2>Login Form</h2>
			{error && <p className='error-message'>{error}</p>}
			<form onSubmit={handleLogin}>
				<FormInput
					label={'Email address'}
					name='email'
					value={formData.email}
					onChange={handleChange}
					type='email'
				/>
				<FormInput
					label={'Password'}
					name='password'
					value={formData.password}
					onChange={handleChange}
					type='password'
				/>
				<FormInput
					label={'Stay signed in'}
					name='remember_me'
					type='checkbox'
					checked={formData.remember_me}
					onChange={handleChange}
				/>
				<button
					type='submit'
					disabled={loading}
				>
					{loading ? 'Logging in...' : 'Log in'}
				</button>
			</form>
		</div>
	);
};

export default LoginForm;
