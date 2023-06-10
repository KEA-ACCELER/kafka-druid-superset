import { useState } from "react";
import { Button } from "react-bootstrap";
import { postQueryRaw } from "../services/api";

export const Raw = () => {
    const [res, setRes] = useState("");
    const [query, setQuery] = useState("");

    const requestHandler = async () => {
        const body = {
            query: query,
        };
        setRes(await postQueryRaw(body));
    };
    return (
        <div className="Raw">
            <h1>Raw-DB</h1>
            <div style={{ display: "flex", width: "90%", justifyContent: "center", marginTop: "30px" }}>
                <div style={{ marginRight: "50px" }}>
                    <h4>Query</h4>
                    <input type="text" placeholder="SELECT * FROM seoulRide" style={{ width: "400px" }} />
                </div>

                <Button onClick={requestHandler}>쿼리 보내기</Button>
            </div>

            <textarea style={{ width: "1000px", height: "500px", marginTop: "40px" }} value={res}></textarea>
        </div>
    );
};
