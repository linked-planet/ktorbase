import React from "react"
import { FC } from "react"
import { Config, Session } from "../model/model"

type WelcomePageProps = {
	session: Session
	config: Config
}

export const WelcomePage: FC<WelcomePageProps> = props => {
	return (
		<div>
			<p>Hello {props.session.username}</p>
			<p>
				Config BannerBackgroundColor:{" "}
				{props.config?.bannerBackgroundColor}
			</p>
			<p>
				Config BannerMenuBackgroundColor:{" "}
				{props.config?.bannerMenuBackgroundColor}
			</p>
		</div>
	)
}
