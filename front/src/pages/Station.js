import { useState } from "react";
import { Button } from "react-bootstrap";
import { getBusStop } from "../services/api";

export const Station = () => {
    const [res, setRes] = useState("");

    const requestHandler = async () => {
        setRes(await getBusStop());
    };

    return (
        <div className="Station">
            <h1>Station </h1>
            <div style={{ display: "flex", width: "90%", justifyContent: "center", marginTop: "30px" }}>
                <Button onClick={requestHandler}>정보 받아오기</Button>
            </div>
            <textarea style={{ width: "1000px", height: "500px", marginTop: "40px" }} value={res}></textarea>
        </div>
    );
};
