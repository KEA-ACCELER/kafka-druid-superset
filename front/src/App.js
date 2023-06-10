import "./App.css";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { Home } from "./Home";
import "bootstrap/dist/css/bootstrap.min.css";
import { Main } from "./Main";
import { Station } from "./Station";
import { Ride } from "./Ride";
function App() {
    return (
        <BrowserRouter>
            <div className="App">
                <Routes>
                    <Route path="/" element={<Home />}>
                        <Route index element={<Main />} />
                        <Route path="/station" element={<Station />} />
                        <Route path="/ride" element={<Ride />} />
                    </Route>
                </Routes>
            </div>
        </BrowserRouter>
    );
}

export default App;
