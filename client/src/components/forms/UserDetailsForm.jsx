/** @format */

import { useEffect, useState } from 'react';
import useForm from '../../hooks/useForm.js';
import FormInput from './FormInput.jsx';

const UserDetailsForm = ({userDetails}) => {
  const [authToken, setAuthToken] = useState(null);
	const { formData, handleChange, handleSubmit, loading, error, setFormData } = useForm(
		{
			firstName: '',
			lastName: '',
			phoneNumber: '',
			address: '',
			city: '',
			country: '',
			userId: null,
		} ,
		'/api/users/account',
    authToken
	);

	useEffect(() => {
		// Retrieve the user ID from localStorage when the component mounts
		const userId = localStorage.getItem('userId');
    const token = localStorage.getItem('token');
    token && setAuthToken(token);
		if (userId) {
			// Update the formData with the retrieved user ID
			handleChange({ target: { name: 'userId', value: userId, type: 'text' } });
		}
	}, []);
	useEffect(() => {
		if(userDetails !== null){
			setFormData(userDetails)
		}
	},[userDetails])

	return (
		<div>
			<h2>Complete Your Account Details</h2>
			{error && <p className='error-message'>{error}</p>}
			<form onSubmit={handleSubmit}>
				<FormInput
					label={'First Name'}
					name='firstName'
					value={formData.firstName}
					onChange={handleChange}
					type='text'
				/>
				<FormInput
					label={'Last Name'}
					name='lastName'
					value={formData.lastName}
					onChange={handleChange}
					type='text'
				/>
				<FormInput
					label={'Phone Number'}
					name='phoneNumber'
					value={formData.phoneNumber}
					onChange={handleChange}
					type='text'
				/>
				<FormInput
					label={'Address'}
					name='address'
					value={formData.address}
					onChange={handleChange}
					type='text'
				/>
				<FormInput
					label={'City'}
					name='city'
					value={formData.city}
					onChange={handleChange}
					type='text'
				/>
				<FormInput
					label={'Country'}
					name='country'
					value={formData.country}
					onChange={handleChange}
					type='text'
				/>
				<button
					type='submit'
					disabled={loading}
				>
					{loading ? 'Submitting...' : 'Submit Details'}
				</button>
			</form>
		</div>
	);
};

export default UserDetailsForm;
