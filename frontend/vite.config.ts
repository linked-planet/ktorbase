import { defineConfig } from "vite"
import react from "@vitejs/plugin-react-swc"
import checker from "vite-plugin-checker"

const port = 8081

export default defineConfig({
	build: {
		target: "es2022",
		sourcemap: true,
		cssCodeSplit: false, // we only want 1 global style bundle, because we need to load it in the velocity template, and there is currently no possibility to packgage the css per entry point (July, 2023)
	},
	server: {
		port,
		hmr: {
			overlay: true,
		},
		proxy: {
			["/session"]: {
				target: "http://localhost:9090",
			},
		},
	},
	plugins: [
		react(),
		// run typescript type checker
		checker({
			typescript: true,
			eslint: {
				lintCommand: "eslint './src/**/*.{ts,tsx}'",
			},
		}),
	],
})
