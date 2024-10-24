/** @format */

import { useState } from 'react';

const useForm = (initialValues, endpoint, auth = null) => {
	const [formData, setFormData] = useState(initialValues);
	const [loading, setLoading] = useState(false);
	const [error, setError] = useState(null);

	const handleChange = e => {
		const { name, value, type, checked } = e.target;
		setFormData(prev => ({
			...prev,
			[name]: type === 'checkbox' ? checked : value,
		}));
	};
  const handleFetch = async token => {
		if (token) {
			return fetch(endpoint, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
					Authorization: `Bearer ${token}`,
				},
				body: JSON.stringify(formData),
			});
		} else {
			return fetch(endpoint, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				},
				body: JSON.stringify(formData),
			});
		}
	};

	const handleSubmit = async e => {
		e.preventDefault();
		setLoading(true);
		setError(null);
		try {
			const res = await handleFetch(auth);

			if (!res.ok) {
				setError('Submission failed.');
			}

			const data = await res.json();
			setLoading(false);
			return data; // Return data if needed for parent component
		} catch (err) {
			setError(err.message);
			setLoading(false);
		}
	};

  
      

	return {
		formData,
		handleChange,
		handleSubmit,
		loading,
		error,
		setFormData
	};
};

export default useForm;
