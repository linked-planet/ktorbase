import React, { FC, Fragment } from "react"
import {
	AtlassianNavigation,
	CustomProductHome,
	Profile,
} from "@atlaskit/atlassian-navigation"
import { Config, Session } from "../model/model"
import { MenuGroup } from "@atlaskit/menu"
import { LinkItem } from "@atlaskit/side-navigation"
import "./BannerComponent.scss"
import { Popover } from "@linked-planet/ui-kit-ts"

type BannerComponentProps = {
	session?: Session
	config?: Config
	logout: () => void
}

export const BannerComponent: FC<BannerComponentProps> = ({
	session,
	config,
	logout,
}) => {
	const [isProfilePopupOpen, setIsProfilePopupOpen] = React.useState(false)

	return (
		<div id="banner">
			<AtlassianNavigation
				label={"Ktorbase"}
				primaryItems={[]}
				renderProductHome={() => (
					<CustomProductHome
						iconAlt={"Ktorbase Icon"}
						iconUrl={"../assets/favicon.png"}
						logoAlt={"Ktorbase Logo"}
						logoUrl={"../assets/favicon.png"}
						siteTitle={"KtorBase"}
					/>
				)}
				renderProfile={() => {
					if (session && config) {
						return (
							<Popover.Root
								appearance="subtle"
								side="bottom"
								align="end"
								trigger={
									<Fragment>
										<Profile
											onClick={() =>
												setIsProfilePopupOpen(
													!isProfilePopupOpen,
												)
											}
											tooltip={session?.username}
											icon={
												<img
													src={
														"https://w7.pngwing.com/pngs/7/618/png-transparent-man-illustration-avatar-icon-fashion-men-avatar-face-fashion-girl-heroes.png"
													}
													style={{
														borderRadius: "50%",
														width: "32px",
														height: "32px",
													}}
												/>
											}
										/>
										<span>{session.username}</span>
									</Fragment>
								}
							>
								<div id="banner-menu">
									<MenuGroup>
										<LinkItem
											onClick={() => {
												setIsProfilePopupOpen(false)
												logout()
											}}
										>
											Logout
										</LinkItem>
									</MenuGroup>
								</div>
							</Popover.Root>
						)
					} else return null
				}}
			/>
		</div>
	)
}
