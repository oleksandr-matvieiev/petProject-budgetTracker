import { useEffect, useState } from "react";
import axios from "axios";
import styled from "styled-components";
import { Link } from "react-router-dom";
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

const FilterContainer = styled.div`
    display: flex;
    gap: 10px;
    margin-bottom: 20px;
`;

const Select = styled.select`
    padding: 8px;
    border-radius: 5px;
    border: 1px solid #ccc;
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
    const [filterType, setFilterType] = useState("");
    const [filterCategory, setFilterCategory] = useState("");

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
            console.error("Error fetching balance", error);
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
            console.error("Error fetching transactions", error);
        }
    };

    const fetchTransactionsByType = async (type) => {
        try {
            const token = localStorage.getItem("token");
            const res = await axios.get(`http://localhost:8080/api/transaction/find-transactions-by-type`, {
                params: { type },
                headers: { Authorization: `Bearer ${token}` },
            });
            setTransactions(res.data);
        } catch (error) {
            console.error("Error fetching transactions by type", error);
        }
    };

    const fetchTransactionsByCategory = async (category) => {
        try {
            const token = localStorage.getItem("token");
            const res = await axios.get(`http://localhost:8080/api/transaction/find-transaction-by-category`, {
                params: { category },
                headers: { Authorization: `Bearer ${token}` },
            });
            setTransactions(res.data);
        } catch (error) {
            console.error("Error fetching transactions by category", error);
        }
    };

    const handleFilterChange = (e) => {
        const { name, value } = e.target;

        if (name === "type") {
            setFilterType(value);
            value ? fetchTransactionsByType(value) : fetchTransactions();
        } else if (name === "category") {
            setFilterCategory(value);
            value ? fetchTransactionsByCategory(value) : fetchTransactions();
        }
    };

    const clearFilters = () => {
        setFilterType("");
        setFilterCategory("");
        fetchTransactions();
    };

    return (
        <Container>
            <Header>
                <a href="/settings">⚙ Settings (in develop)</a>
                <Balance>Balance: ${balance.toFixed(2)}</Balance>
            </Header>

            <h2>Last transactions</h2>

            {/* Фільтри */}
            <FilterContainer>
                <Select name="type" value={filterType} onChange={handleFilterChange}>
                    <option value="">Filter by Type</option>
                    <option value="INCOME">Income</option>
                    <option value="EXPENSE">Expense</option>
                </Select>

                <Select name="category" value={filterCategory} onChange={handleFilterChange}>
                    <option value="">Filter by Category</option>
                    <option value="FOOD">Food</option>
                    <option value="TRANSPORT">Transport</option>
                    <option value="HEALTH">Health</option>
                    <option value="ENTERTAINMENT">Entertainment</option>
                    <option value="SHOPPING">Shopping</option>
                    <option value="EDUCATION">Education</option>
                    <option value="PETS">Pets</option>
                    <option value="FINANCE">Finance</option>
                    <option value="OTHER">Other</option>
                </Select>

                <button onClick={clearFilters}>Clear Filters</button>
            </FilterContainer>

            <TransactionsTable>
                <thead>
                <tr>
                    <TableHeader>Amount</TableHeader>
                    <TableHeader>Type</TableHeader>
                    <TableHeader>Category</TableHeader>
                    <TableHeader>Date</TableHeader>
                    <TableHeader>Details</TableHeader>
                </tr>
                </thead>
                <tbody>
                {transactions.map((tx) => (
                    <TableRow key={tx.id}>
                        <TableCell>${tx.amount.toFixed(2)}</TableCell>
                        <TableCell>{tx.type}</TableCell>
                        <TableCell>{tx.category}</TableCell>
                        <TableCell>{tx.transactionDate}</TableCell>
                        <TableCell>
                            <Link to={`/transaction/${tx.id}`}>🔍 View</Link>
                        </TableCell>
                    </TableRow>
                ))}
                </tbody>
            </TransactionsTable>

            <AddButton onClick={() => setShowModal(true)}>+ Add new Transaction</AddButton>

            {showModal && <TransactionModal onClose={() => setShowModal(false)} />}
        </Container>
    );
};

export default HomePage;
