import { API_URL } from "../config";
// src/api/userApi.js
const USERS_URL = `${API_URL}/api/users`;

export const fetchUserByEmail = async (email) => {
    const response = await fetch(`${USERS_URL}/by-email/${encodeURIComponent(email)}`);
    return response.json();
};

export const updateProfile = async (id, userData) => {
    const response = await fetch(`${USERS_URL}/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userData)
    });
    return response.json();
};