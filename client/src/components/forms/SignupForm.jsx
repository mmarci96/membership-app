/** @format */

import useForm from '../../hooks/useForm.js';
import FormInput from './FormInput.jsx';

const SignupForm = () => {
	const { formData, handleChange, handleSubmit, loading, error } = useForm(
		{
			email: '',
			password: '',
			password_confirmation: '',
			accepted: false,
		},
		'/api/auth/signup'
	);

	const handleSignup = async e => {
		const data = await handleSubmit(e);
		if (data) {
			window.location.reload();
		}
	};

	return (
		<div>
			<h2>Signup Form</h2>
			{error && <p className='error-message'>{error}</p>}
			<form onSubmit={handleSignup}>
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
					label={'Validate password'}
					name='password_confirmation'
					value={formData.password_confirmation}
					onChange={handleChange}
					type='password'
				/>
				<FormInput
					label={'Accept terms and conditions'}
					name='accepted'
					value={formData.accepted}
					type='checkbox'
					onChange={handleChange}
				/>
				<button
					type='submit'
					disabled={loading}
				>
					{loading ? 'Signing up...' : 'Sign up'}
				</button>
			</form>
		</div>
	);
};

export default SignupForm;
