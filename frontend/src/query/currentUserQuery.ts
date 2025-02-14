import { useQuery } from "@tanstack/react-query"
import { backend } from "../client"
import { Session, Config } from "../model/model"

export type SessionResponse = {
	session: Session
	config: Config
}

export function useCurrentUserQuery() {
	return useQuery(["current_user"], (): Promise<SessionResponse> => {
		return new Promise((resolve, reject) => {
			return backend
				.get("/session", {
					headers: {
						Accept: "application/json",
					},
				})
				.then(response => resolve(response.data))
				.catch(error => reject(error))
		})
	})
}
