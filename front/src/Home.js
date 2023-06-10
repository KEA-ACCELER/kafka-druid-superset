import { Outlet } from "react-router-dom";
import { Menu } from "./Menu";

export const Home = () => {
    return (
        <div className="Home">
            <Menu />
            <Outlet />
        </div>
    );
};
