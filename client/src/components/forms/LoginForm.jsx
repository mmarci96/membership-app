import {useState} from "react";
import FormInput from "./FormInput.jsx";

const LoginForm = () => {
    const [formData, setFormData] = useState({
        email: "",
        password: "",
        remember_me: false,
    })
    const [loading, setLoading] = useState(false);
    const [responseData, setResponseData] = useState(null);

    const handleChange = e => {
        const { name, type, value, checked } = e.target;

        setFormData(prev => ({
            ...prev,
            [name]: type === "checkbox" ? checked : value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        const requestBody = {
            email: formData.email,
            password: formData.password,
        }
        const res = await fetch("/api/auth/login", {
            method: "POST",
            body: JSON.stringify(requestBody),
            headers: {
                "Content-Type": "application/json",
            }
        })
        if (!res.ok) {
            console.error("Failed to login ");
        }
        const data = await res.json();
        console.log(data);
        setResponseData(data);
        setLoading(false);
    }

    return (
        <div>
            Login form
        <form onSubmit={handleSubmit}>
            <FormInput
                label={"Email address"}
                name="email"
                value={formData.email}
                onChange={handleChange}
                type="email"
            />
            <FormInput
                label={"Password"}
                name="password"
                value={formData.password}
                onChange={handleChange}
                type="password"
            />
            <FormInput
                label={"Stay signed in"}
                name="remember_me"
                value={formData.remember_me}
                type="checkbox"
                onChange={handleChange}
            />
            <button type={"submit"} onClick={handleSubmit}>
                Log in
            </button>
        </form>
        </div>
    )
}

export default LoginForm;