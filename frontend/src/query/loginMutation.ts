import { useMutation } from "@tanstack/react-query"
import { backend } from "../client"
import { SessionResponse } from "./currentUserQuery"

export type LoginData = {
	username: string
	password: string
}

export function useLoginMutation() {
	return useMutation({
		mutationKey: ["login"],
		mutationFn: ({
			username,
			password,
		}: LoginData): Promise<SessionResponse> => {
			return new Promise((resolve, reject) => {
				return backend
					.post(
						"/session",
						{ username, password },
						{
							headers: {
								Accept: "application/json",
								"Content-Type": "application/json",
							},
						},
					)
					.then(response => resolve(response.data))
					.catch(error => reject(error))
			})
		},
	})
}
