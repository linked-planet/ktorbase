import axios from "axios"

export const backend = axios.create({
	baseURL: "/",
	headers: {
		"Content-Type": "application/json",
	},
})
