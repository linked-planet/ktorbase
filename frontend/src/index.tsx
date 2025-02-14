import React from "react"
import ReactDOM from "react-dom"
import App from "./App"

import { ReactQueryDevtools } from "@tanstack/react-query-devtools"

// to improve behavior of array.filter, or fetch .json()
import "@total-typescript/ts-reset"

import {
	MutationCache,
	QueryCache,
	QueryClient,
	QueryClientProvider,
} from "@tanstack/react-query"
import { HashRouter } from "react-router-dom"
import { ToastFlagContainer } from "@linked-planet/ui-kit-ts"

const queryClient = new QueryClient({
	queryCache: new QueryCache({
		onError: (error, query) => {
			console.log("Query onError", query.options.queryKey, error)
		},
	}),
	defaultOptions: {
		queries: {
			retry: false,
			refetchOnWindowFocus: false,
		},
	},
	mutationCache: new MutationCache({
		onError: (error, vars, ctx, mutation) => {
			console.log(
				"Mutation onError: ",
				mutation.options.mutationKey,
				" error: ",
				error,
			)
		},
	}),
})

ReactDOM.render(
	<React.StrictMode>
		<HashRouter>
			<QueryClientProvider client={queryClient}>
				<App />
				<ReactQueryDevtools initialIsOpen={false} />
				<ToastFlagContainer />
			</QueryClientProvider>
		</HashRouter>
	</React.StrictMode>,
	document.getElementById("content"),
)
