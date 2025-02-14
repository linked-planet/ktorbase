import { useMutation } from "@tanstack/react-query"
import { backend } from "../client"

export function useLogoutMutation() {
	return useMutation({
		mutationKey: ["save_general_config"],
		mutationFn: () => {
			return new Promise((resolve, reject) => {
				return backend
					.delete("/session")
					.then(response => resolve(response.data))
					.catch(error => reject(error))
			})
		},
	})
}
