import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import {GlobalStyles} from "./styles/GlobalStyles";
import RegisterForm from "./components/RegisterForm";
import LoginForm from "./components/LoginForm";
import HomePage from "./components/HomePage";
import TransactionDetailPage from "./components/TransactionDetailPage";
import GoalsPage from "./components/GoalsPage";

const App = () => {
    return (
        <Router>
            <GlobalStyles/>
            <Routes>
                <Route path="/" element={<HomePage/>}/>
                <Route path="/login" element={<LoginForm/>}/>
                <Route path="/register" element={<RegisterForm/>}/>
                <Route path="/transaction/:id" element={<TransactionDetailPage />} />
                <Route path="/goals" element={<GoalsPage/>}/>
            </Routes>
        </Router>
    );
};

export default App;
