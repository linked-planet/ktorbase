import React from "react"
import { LoadingSpinner } from "@linked-planet/ui-kit-ts"

export default function PageLoadingSpinner({ title }: { title?: string }) {
	return (
		<div className="w-full py-48 flex flex-col justify-center items-center">
			<LoadingSpinner size="large" />
			{title && <div className="m-auto mt-8">{title}</div>}
		</div>
	)
}
