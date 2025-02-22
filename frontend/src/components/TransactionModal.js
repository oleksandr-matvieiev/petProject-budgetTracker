import {useEffect, useState} from "react";
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
    const [categories, setCategories] = useState([]);
    const [subCategories, setSubCategories] = useState([]);
    const [form, setForm] = useState({
        amount: "",
        type: "",
        category: "",
        subCategoryId: "",
        description: "",
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setForm({ ...form, [name]: value });

        if (name === "category") {
            fetchSubCategories(value);
        }
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

    const fetchCategories = async () => {
        try {
            const token = localStorage.getItem("token");
            const res = await axios.get("http://localhost:8080/api/category/get-all-category",{
                headers: { Authorization: `Bearer ${token}` },
            });
            setCategories(res.data);
        } catch (error) {
            console.error("Error while getting categories", error);
        }
    };

    const fetchSubCategories = async (category) => {
        try {
            const token = localStorage.getItem("token");
            const res = await axios.get("http://localhost:8080/api/category/get-all-subCategory-by-category", {
                params: { category },
                headers: { Authorization: `Bearer ${token}` },
            });
            setSubCategories(res.data);
            setForm((prev) => ({ ...prev, subCategoryId: res.data.length > 0 ? res.data[0].id : "" }));
        } catch (error) {
            console.error("Error while getting sub categories", error);
        }
    };
    useEffect(() => {
        fetchCategories();
    }, []);

    return (
        <ModalOverlay>
            <ModalContent>
                <h3>New transaction</h3>
                <form onSubmit={handleSubmit}>
                    <Input type={"double"} name="amount" placeholder="Amount" onChange={handleChange} required />
                    <Select name="type" onChange={handleChange}>
                        <option value="INCOME">Income</option>
                        <option value="EXPENSE">Expense</option>
                    </Select>
                    <Select name="category" onChange={handleChange} required>
                        <option value="">Choose category</option>
                        {categories.map((cat) => (
                            <option key={cat.id} value={cat.transactionCategory}>
                                {cat.transactionCategory}
                            </option>
                        ))}
                    </Select>
                    {subCategories.length > 0 && (
                        <Select name="subCategoryId" onChange={handleChange} required>
                            {subCategories.map((subCat) => (
                                <option key={subCat.id} value={subCat.id}>
                                    {subCat.name}
                                </option>
                            ))}
                        </Select>
                    )}


                    <Input type="text" name="description" placeholder="Description (optional)" onChange={handleChange} />
                    <button type="submit">Add</button>
                    <CloseButton onClick={onClose}>Close</CloseButton>
                </form>
            </ModalContent>
        </ModalOverlay>
    );
};

export default TransactionModal;
