import "./App.css";
import { BrowserRouter, Route, Routes } from "react-router-dom";

import "bootstrap/dist/css/bootstrap.min.css";
import { Home } from "./pages/Home";
import { Main } from "./pages/Main";
import { Station } from "./pages/Station";
import { Ride } from "./pages/Ride";
import { Raw } from "./pages/Raw";
import { Target } from "./pages/Target";

function App() {
    return (
        <BrowserRouter>
            <div className="App">
                <Routes>
                    <Route path="/" element={<Home />}>
                        <Route index element={<Main />} />
                        <Route path="/station" element={<Station />} />
                        <Route path="/ride" element={<Ride />} />
                        <Route path="/raw" element={<Raw />} />
                        <Route path="/target" element={<Target />} />
                    </Route>
                </Routes>
            </div>
        </BrowserRouter>
    );
}

export default App;
