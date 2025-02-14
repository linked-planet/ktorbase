import React, { FC, Fragment } from "react"
import Form, { Field, FormFooter, FormHeader } from "@atlaskit/form"
import TextField from "@atlaskit/textfield"
import { LoginData } from "../query/loginMutation"
import { LoadingButton } from "@linked-planet/ui-kit-ts"
import "./LoginPage.scss"

type LoginPageProps = {
	login: (formData: LoginData) => void
}

export const LoginPage: FC<LoginPageProps> = props => {
	return (
		<div id={"login-view"}>
			<div id={"login"}>
				<Form onSubmit={(formData: LoginData) => props.login(formData)}>
					{({ formProps, submitting }) => (
						<form {...formProps}>
							<FormHeader title="Login" />

							<Field name="username" defaultValue="">
								{({ fieldProps }) => (
									<TextField
										{...fieldProps}
										placeholder={"Username"}
									/>
								)}
							</Field>

							<Field name="password" defaultValue="">
								{({ fieldProps }) => (
									<TextField
										{...fieldProps}
										placeholder={"Password"}
										type={"password"}
									/>
								)}
							</Field>

							<FormFooter>
								<LoadingButton
									type="submit"
									loading={submitting}
								>
									Login
								</LoadingButton>
							</FormFooter>
						</form>
					)}
				</Form>
			</div>
		</div>
	)
}
