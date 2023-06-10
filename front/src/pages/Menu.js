import { useContext, useEffect } from "react";

import { Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import "../css/Menu.css";

export const Menu = () => {
    const navigate = useNavigate();

    return (
        <div className="Menu">
            <Button className="btn" variant="outline-danger" onClick={() => navigate("/", { replace: true })}>
                <h4>HOME</h4>
            </Button>
            <Button className="btn" variant="outline-primary" onClick={() => navigate("/ride", { replace: true })}>
                <h4>버스 승하차 정보</h4>
            </Button>
            <Button className="btn" variant="outline-primary" onClick={() => navigate("/station", { replace: true })}>
                <h4>버스 정류소 정보</h4>
            </Button>
            <Button className="btn" variant="outline-success" onClick={() => navigate("/raw", { replace: true })}>
                <h4>Raw-DB</h4>
            </Button>
            <Button className="btn" variant="outline-success" onClick={() => navigate("/target", { replace: true })}>
                <h4>Target-DB</h4>
            </Button>
        </div>
    );
};
