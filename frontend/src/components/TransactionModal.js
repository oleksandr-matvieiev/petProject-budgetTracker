// src/components/TransactionModal.jsx
import { useState } from "react";
import axios from "axios";
import styled from "styled-components";

const ModalOverlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
`;

const ModalContent = styled.div`
  background: white;
  padding: 20px;
  border-radius: 10px;
  width: 300px;
`;

const Input = styled.input`
  width: 100%;
  padding: 8px;
  margin: 5px 0;
  border: 1px solid #ccc;
  border-radius: 5px;
`;

const Select = styled.select`
  width: 100%;
  padding: 8px;
  margin: 5px 0;
  border: 1px solid #ccc;
  border-radius: 5px;
`;

const CloseButton = styled.button`
  background: red;
  margin-top: 10px;
`;

const TransactionModal = ({ onClose }) => {
    const [form, setForm] = useState({
        amount: "",
        type: "INCOME",
        category: "SALARY",
        description: "",
    });

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = localStorage.getItem("token");
            await axios.post("http://localhost:8080/api/transaction/create-new-transaction", null, {
                params: form,
                headers: { Authorization: `Bearer ${token}` },
            });
            alert("Transaction was added!");
            onClose();
        } catch (error) {
            console.error("Error while adding transaction", error);
        }
    };

    return (
        <ModalOverlay>
            <ModalContent>
                <h3>New transaction</h3>
                <form onSubmit={handleSubmit}>
                    <Input type="number" name="amount" placeholder="Amount" onChange={handleChange} required />
                    <Select name="type" onChange={handleChange}>
                        <option value="INCOME">Income</option>
                        <option value="EXPENSE">Expense</option>
                    </Select>
                    <Select name="category" onChange={handleChange}>
                        <option value="HOUSING">Housing</option>
                        <option value="FOOD">Food</option>
                        <option value="HEALTH">Health</option>
                        <option value="ENTERTAINMENT">Entertainment</option>
                        <option value="SHOPPING">Shopping</option>
                        <option value="EDUCATION">Education</option>
                        <option value="PETS">Pets</option>
                        <option value="FINANCE">Finance</option>
                        <option value="OTHER">Other</option>
                    </Select>
                    <Input type="text" name="description" placeholder="Description (optional)" onChange={handleChange} />
                    <button type="submit">Add</button>
                    <CloseButton onClick={onClose}>Close</CloseButton>
                </form>
            </ModalContent>
        </ModalOverlay>
    );
};

export default TransactionModal;
