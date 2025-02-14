module.exports = {
	parser: "@typescript-eslint/parser",
	parserOptions: {
		ecmaVersion: 2022,
		sourceType: "module",
		ecmaFeatures: {
			jsx: true,
		},
	},
	plugins: [
		"@typescript-eslint",
		"react",
		"react-hooks",
		"react-refresh"
	],
	extends: [
		"eslint:recommended",
		"plugin:@typescript-eslint/recommended",
		"plugin:@atlaskit/design-system/recommended",
		"plugin:react/recommended",
		"plugin:react-hooks/recommended",
		"prettier",
	],
	settings: {
		react: {
			version: "detect",
		},
	},
	rules: {
		"react-hooks/rules-of-hooks": "error",
		"react-hooks/exhaustive-deps": "warn",
		"react-refresh/only-export-components": "warn",
		"@typescript-eslint/no-unused-vars": "off",
	},
	overrides: [
		{
			files: ["*.cjs"],
			rules: {
				"@typescript-eslint/no-var-requires": "off",
			},
			env: {
				node: true,
			},
		},
	],
}
