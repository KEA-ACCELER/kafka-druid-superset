import { useState } from "react";
import { Button } from "react-bootstrap";
import { getBusRide } from "../services/api";

export const Ride = () => {
    const [res, setRes] = useState("");
    const [startY, setStartY] = useState("");
    const [startM, setStartM] = useState("");
    const [endY, setEndY] = useState("");
    const [endM, setEndM] = useState("");

    const requestHandler = async () => {
        const body = {
            start_year: startY,
            start_month: startM,
            end_year: endY,
            end_month: endM,
        };
        setRes(await getBusRide(body));
    };

    return (
        <div className="Ride">
            <h1>Ride</h1>
            <div style={{ display: "flex", width: "90%", justifyContent: "center", marginTop: "30px" }}>
                <div style={{ marginRight: "50px" }}>
                    <h4>시작 연도</h4>
                    <input type="text" placeholder="2022" value={startY} onChange={(e) => setStartY(e.target.value)} />
                </div>
                <div style={{ marginRight: "50px" }}>
                    <h4>시작 월</h4>
                    <input type="text" placeholder="03" value={startM} onChange={(e) => setStartM(e.target.value)} />
                </div>
                <div style={{ marginRight: "50px" }}>
                    <h4>끝 연도</h4>
                    <input type="text" placeholder="2023" value={endY} onChange={(e) => setEndY(e.target.value)} />
                </div>
                <div style={{ marginRight: "50px" }}>
                    <h4>끝 월</h4>
                    <input type="text" placeholder="05" value={endM} onChange={(e) => setEndM(e.target.value)} />
                </div>
                <Button onClick={requestHandler}>정보 받아오기</Button>
            </div>
            <textarea style={{ width: "1000px", height: "500px", marginTop: "40px" }} value={res}></textarea>
        </div>
    );
};
