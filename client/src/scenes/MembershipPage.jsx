/** @format */

import { useGlobalContext } from '../hooks/useGlobalContext.js';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

const MembershipPage = () => {
	const { user } = useGlobalContext();
	const [membership, setMembership] = useState(null);
	const [membershipPackages, setMembershipPackages] = useState([]);

	const fetchMembershipPackages = async () => {
		const res = await fetch('/api/memberships/packages', {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json',
				Authorization: `Bearer ${user.token}`,
			},
		});
		return await res.json();
	};
	const fetchMembership = async () => {
		const res = await fetch(`/api/memberships/${user.userId}`, {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json',
				Authorization: `Bearer ${user.token}`,
			},
		});
		return await res.json();
	};

	useEffect(() => {
		if (user.token && !membership) {
			fetchMembership()
				.then(data => setMembership(data))
				.catch(err => console.log(err));
		}
	}, [user.token, membership]);

	useEffect(() => {
		if (user.token && membership) {
            fetchMembershipPackages()
                .then(data => setMembershipPackages(data))
                .catch(err => console.log(err));
        }
	}, [user, membership]);

	return (
		<div>
			<div>
				{user?.token ? (
					!membership ? (
						<Link to={'/payment'}>
							<button>Become a member now</button>
						</Link>
					) : (
						<h1>Welcome to the membership page</h1>
					)
				) : (
					<Link to={'/account'}>
						<button>Register first</button>
					</Link>
				)}
			</div>
			<div>
				{membershipPackages.length > 0 &&
					membershipPackages.map(membershipPackage => (
						<div key={membershipPackage.id}>
							<p>{membershipPackage.name}</p>
						</div>
					))}
			</div>
		</div>
	);
};

export default MembershipPage;
