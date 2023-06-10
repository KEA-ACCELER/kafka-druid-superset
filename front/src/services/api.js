import axios from "axios";

const API_URL = "http://localhost:3000";

export const getBusRide = async (body) => {
    const res = await axios
        .post(`${API_URL}/api/bus-ride`, body)
        .then((res) => {
            console.log(res);
            return res;
        })
        .catch((err) => {
            console.log(err);
            return err;
        });

    return res;
};
export const getBusStop = async () => {
    const res = await axios
        .post(`${API_URL}/api/bus-stop`)
        .then((res) => {
            console.log(res);
            return res;
        })
        .catch((err) => {
            console.log(err);
            return err;
        });

    return res;
};
export const postQueryRaw = async (body) => {
    const res = await axios
        .post(`${API_URL}/api/raw-db`, body)
        .then((res) => {
            console.log(res);
            return res;
        })
        .catch((err) => {
            console.log(err);
            return err;
        });

    return res;
};
export const postQueryTarget = async (body) => {
    const res = await axios
        .post(`${API_URL}/api/target-db`, body)
        .then((res) => {
            console.log(res);
            return res;
        })
        .catch((err) => {
            console.log(err);
            return err;
        });

    return res;
};
