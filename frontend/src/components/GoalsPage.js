import { useState, useEffect } from "react";
import axios from "axios";
import styled from "styled-components";

const Container = styled.div`
  padding: 20px;
  max-width: 600px;
  margin: 0 auto;
`;

const GoalCard = styled.div`
  background: white;
  padding: 15px;
  margin-bottom: 10px;
  border-radius: 5px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
`;

const ProgressBar = styled.div`
  height: 10px;
  background: #ddd;
  border-radius: 5px;
  margin-top: 5px;
  overflow: hidden;
`;

const Progress = styled.div`
  height: 100%;
  width: ${({ progress }) => progress}%;
  background: #007bff;
`;

const GoalsPage = () => {
    const [goals, setGoals] = useState([]);
    const [newGoal, setNewGoal] = useState({ name: "", targetAmount: 0 });
    const [amountToAdd, setAmountToAdd] = useState({});

    const token=localStorage.getItem("token");

    const fetchGoals = async () => {

        const res = await axios.get(`http://localhost:8080/api/goals/get-goals`,{
            headers:{Authorization:`Bearer ${token}`}
        });
        setGoals(res.data);
    };
    useEffect(() => {
        fetchGoals();
    }, []);



    const createGoal = async () => {
        const userId = 1;
        await axios.post(`http://localhost:8080/api/goals/create`, null, {
            params: { userId, ...newGoal },
            headers:{Authorization:`Bearer ${token}`}
        });
        fetchGoals();
    };

    const updateGoal = async (goalId) => {
        if (!amountToAdd[goalId] || amountToAdd[goalId] <= 0) {
            alert("Enter a valid amount");
            return;
        }

        try {
            await axios.post("http://localhost:8080/api/goals/update", null, {
                params: { goalId, amount: amountToAdd[goalId] },
                headers: { Authorization: `Bearer ${token}` },
            });
            setAmountToAdd({ ...amountToAdd, [goalId]: "" });
            fetchGoals();
        } catch (error) {
            console.error("Error updating goal:", error);
        }
    };

    const deleteGoal = async (goalId) => {
        if (!window.confirm("Are you really wand to delete this goal?")) return;

        try {
            await axios.delete(`http://localhost:8080/api/goals/${goalId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            fetchGoals();
        } catch (error) {
            console.error("Error deleting goal:", error);
        }
    };

    return (
        <Container>
            <h2>🏆 Financial Goals</h2>
            <div>
                <input
                    type="text"
                    placeholder="Goal name"
                    value={newGoal.name}
                    onChange={(e) => setNewGoal({ ...newGoal, name: e.target.value })}
                />
                <input
                    type="number"
                    placeholder="Target amount"
                    value={newGoal.targetAmount}
                    onChange={(e) => setNewGoal({ ...newGoal, targetAmount: e.target.value })}
                />
                <button onClick={createGoal}>Add Goal</button>
            </div>
            {goals.map((goal) => (
                <GoalCard key={goal.id}>
                    <h3>{goal.name}</h3>
                    <p>Progress: ${goal.currentAmount} / ${goal.targetAmount}</p>
                    <ProgressBar>
                        <Progress progress={(goal.currentAmount / goal.targetAmount) * 100} />
                    </ProgressBar>

                    <input
                        type="number"
                        placeholder="amount"
                        value={amountToAdd[goal.id] || ""}
                        onChange={(e) => setAmountToAdd({ ...amountToAdd, [goal.id]: e.target.value })}
                    />
                    <button onClick={() => updateGoal(goal.id)}>Add amount</button>
                    <button danger onClick={() => deleteGoal(goal.id)}>Delete</button>
                </GoalCard>
            ))}
        </Container>
    );
};

export default GoalsPage;
