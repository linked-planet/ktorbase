import React, { useCallback, useEffect } from "react"
import {
	PageLayout,
	showErrorFlag,
	showSuccessFlag,
} from "@linked-planet/ui-kit-ts"

import "./App.scss"
import { BannerComponent } from "./components/BannerComponent"
import { useCurrentUserQuery } from "./query/currentUserQuery"
import { WelcomePage } from "./page/WelcomePage"
import { LoginPage } from "./page/LoginPage"
import PageLoadingSpinner from "./components/PageLoadingSpinner"
import { useLogoutMutation } from "./query/logoutMutation"
import { useLoginMutation } from "./query/loginMutation"

enum AppState {
	Loading = "loading",
	Login = "login",
	Loaded = "loaded",
	Error = "error",
}

function App() {
	const [appState, setAppState] = React.useState(AppState.Loading)
	const currentUserQuery = useCurrentUserQuery()
	const login = useLoginMutation()
	const logout = useLogoutMutation()

	useEffect(() => {
		if (
			!currentUserQuery.isLoading &&
			!currentUserQuery.error &&
			currentUserQuery.data
		) {
			setAppState(AppState.Loaded)
		} else if (!currentUserQuery.isLoading && currentUserQuery.error) {
			setAppState(AppState.Login)
		}
	}, [currentUserQuery, setAppState])

	const renderContent = useCallback(() => {
		switch (appState) {
			case AppState.Loading:
				return <PageLoadingSpinner title="Loading app..." />
			case AppState.Login:
				return (
					<LoginPage
						login={loginData => {
							login
								.mutateAsync(loginData)
								.then(async response => {
									await currentUserQuery.refetch()
									setAppState(AppState.Loaded)
									showSuccessFlag({
										title: "Login successful",
										description: `Welcome ${response.session.username}!`,
									})
								})
								.catch(() =>
									showErrorFlag({
										title: "Login failed",
										description:
											"Are username and password correct?",
									}),
								)
						}}
					/>
				)
			case AppState.Error:
				return (
					<div>
						<p>Es ist ein Fehler aufgetreten.</p>
					</div>
				)
			case AppState.Loaded:
				return (
					<WelcomePage
						session={currentUserQuery.data!.session}
						config={currentUserQuery.data!.config}
					/>
				)
		}
	}, [appState, setAppState, currentUserQuery, login])

	return (
		<main id="content">
			<BannerComponent
				session={currentUserQuery.data?.session}
				config={currentUserQuery.data?.config}
				logout={() =>
					logout
						.mutateAsync()
						.then(async () => {
							await currentUserQuery.refetch()
							setAppState(AppState.Login)
							localStorage.clear()
							showSuccessFlag({
								title: "Logout successful",
								description: "",
							})
						})
						.catch(() => setAppState(AppState.Error))
				}
			/>
			<div id="content">{renderContent()}</div>
		</main>
	)
}

export default App
