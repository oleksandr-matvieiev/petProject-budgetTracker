// src/components/LoginForm.jsx
import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";

const FormWrapper = styled.div`
    max-width: 400px;
    margin: 50px auto;
    padding: 20px;
    background: white;
    border-radius: 10px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
`;

const Input = styled.input`
  width: 100%;
  padding: 10px;
  margin: 10px 0;
  border: 1px solid #ccc;
  border-radius: 5px;
`;

const LoginForm = () => {
    const [user, setUser] = useState({ username: "", password: "" });
    const navigate = useNavigate();

    const handleChange = (e) => {
        setUser({ ...user, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const res = await axios.post("http://localhost:8080/api/auth/login", user);
            localStorage.setItem("token", res.data);
            alert("Log in successfully");
            navigate("/");
        } catch (error) {
            alert("Error while logging in!");
        }
    };

    return (
        <FormWrapper>
            <h2>Log in</h2>
            <form onSubmit={handleSubmit}>
                <Input type="text" name="email" placeholder="Email" onChange={handleChange} />
                <Input type="password" name="password" placeholder="Password" onChange={handleChange} />
                <button type="submit">Log in</button>
            </form>
        </FormWrapper>
    );
};

export default LoginForm;
