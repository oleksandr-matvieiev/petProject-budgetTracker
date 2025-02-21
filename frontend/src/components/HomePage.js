// src/pages/HomePage.jsx
import { useEffect, useState } from "react";
import axios from "axios";
import styled from "styled-components";
import TransactionModal from "../components/TransactionModal";

const Container = styled.div`
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
`;

const Header = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
`;

const Balance = styled.div`
  font-size: 20px;
  font-weight: bold;
  color: #007bff;
`;

const TransactionsTable = styled.table`
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
`;

const TableHeader = styled.th`
  background-color: #007bff;
  color: white;
  padding: 10px;
`;

const TableRow = styled.tr`
  &:nth-child(even) {
    background-color: #f2f2f2;
  }
`;

const TableCell = styled.td`
  padding: 10px;
  border: 1px solid #ddd;
`;

const AddButton = styled.button`
  margin-top: 20px;
`;

const HomePage = () => {
    const [balance, setBalance] = useState(0);
    const [transactions, setTransactions] = useState([]);
    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        fetchBalance();
        fetchTransactions();
    }, []);

    const fetchBalance = async () => {
        try {
            const token = localStorage.getItem("token");
            const res = await axios.get("http://localhost:8080/api/user/get-balance", {
                headers: { Authorization: `Bearer ${token}` },
            });
            setBalance(res.data);
        } catch (error) {
            console.error("Помилка отримання балансу", error);
        }
    };

    const fetchTransactions = async () => {
        try {
            const token = localStorage.getItem("token");
            const res = await axios.get("http://localhost:8080/api/transaction/get-all-transactions", {
                headers: { Authorization: `Bearer ${token}` },
            });
            setTransactions(res.data);
        } catch (error) {
            console.error("Помилка отримання транзакцій", error);
        }
    };

    return (
        <Container>
            <Header>
                <a href="/settings">⚙ Налаштування</a>
                <Balance>Баланс: ${balance.toFixed(2)}</Balance>
            </Header>

            <h2>Останні транзакції</h2>
            <TransactionsTable>
                <thead>
                <tr>
                    <TableHeader>Сума</TableHeader>
                    <TableHeader>Тип</TableHeader>
                    <TableHeader>Категорія</TableHeader>
                    <TableHeader>Дата</TableHeader>
                </tr>
                </thead>
                <tbody>
                {transactions.map((tx) => (
                    <TableRow key={tx.id}>
                        <TableCell>${tx.amount.toFixed(2)}</TableCell>
                        <TableCell>{tx.type}</TableCell>
                        <TableCell>{tx.category}</TableCell>
                        <TableCell>{tx.transactionDate}</TableCell>
                    </TableRow>
                ))}
                </tbody>
            </TransactionsTable>

            <AddButton onClick={() => setShowModal(true)}>+ Додати транзакцію</AddButton>

            {showModal && <TransactionModal onClose={() => setShowModal(false)} />}
        </Container>
    );
};

export default HomePage;
