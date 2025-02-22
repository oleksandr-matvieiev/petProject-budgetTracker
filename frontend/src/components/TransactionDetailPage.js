import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import styled from "styled-components";

const Container = styled.div`
  padding: 20px;
  max-width: 500px;
  margin: 0 auto;
  background: white;
  border-radius: 10px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
`;

const Button = styled.button`
  margin-top: 10px;
  margin-right: 10px;
`;

const TransactionDetailPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [transaction, setTransaction] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchTransaction();
    }, []);

    const fetchTransaction = async () => {
        try {
            const token = localStorage.getItem("token");
            const res = await axios.get(`http://localhost:8080/api/transaction/get-transaction-by-id?id=${id}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setTransaction(res.data);
            setLoading(false);
        } catch (error) {
            console.error("Error fetching transaction", error);
            setLoading(false);
        }
    };

    const handleDelete = async () => {
        if (!window.confirm("Are you sure you want to delete this transaction?")) return;
        try {
            const token = localStorage.getItem("token");
            await axios.delete(`http://localhost:8080/api/transaction/delete-transaction?id=${id}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            alert("Transaction deleted!");
            navigate("/");
        } catch (error) {
            console.error("Error deleting transaction", error);
        }
    };

    return (
        <Container>
            {loading ? (
                <p>Loading...</p>
            ) : transaction ? (
                <>
                    <h2>Transaction Details</h2>
                    <p><strong>Amount:</strong> ${transaction.amount}</p>
                    <p><strong>Type:</strong> {transaction.type}</p>
                    <p><strong>Category:</strong> {transaction.category}</p>
                    <p><strong>Subcategory:</strong> {transaction.subCategory?.name}</p>
                    <p><strong>Date:</strong> {transaction.transactionDate}</p>
                    <p><strong>Description:</strong> {transaction.description || "No description"}</p>

                    <Button onClick={handleDelete} style={{ background: "red" }}>Delete</Button>
                    <Button onClick={() => alert("Update function will be implemented")}>Update</Button>
                </>
            ) : (
                <p>Transaction not found</p>
            )}
        </Container>
    );
};

export default TransactionDetailPage;
