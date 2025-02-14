# Frontend Application Development

The entry point of the application (or the plugin's frontend) is in the src/app/ directory.

## Setups

### Single Page Apps

For a single page application one can simply start with the provided src/apps/**frontend** folder containing the index.tsx as entrypoint. This app will result in a dist/**frontend.js**, which will dynamically load other bundles.

Also, a css bundles will be made. This is named dist/*[plugin-name].css*, since it is valid for all apps in case of a multi app setup.

### Multi Apps

Each folder within the src/apps/ directory is taken as separate application. Each folder must contain an index.tsx providing the entrypoint of the application.
The resulting bundles need to be included in each applications script HTML tag is named after the folder.
Again, component shared by multiple apps are separated in a separate bundled and referenced by the app bundle. There is no need to manually include the dependency bundles, therefore their names contain hashes.

### CSS

There is currently no possibility to build css bundles per app. All styles for all apps are added into one css bundle file named *[plugin-name].css*, which is also added in the module_jira.vm Velocity template.

If several apps use the same selectors, there will be style clashes (i.g. several apps define different styles for #content). Avoid to only use class names and ids, but use css modules as they are automatically namespaced. Import the styles and set the class names using the ones provided by the module. This has also the advantage you still have an actual style referenced and your IDE will show an error when the style is removed.

```ts
import styles from "style.module.css"

export function exampleDiv() {
	return <div className={styles.exampleDiv}>Example</div>
}
```

```css
#exampleDiv {
	background-color: red;
}
```


## Usage
- **npm run build**
  > builds a production build into ./dist
- **npm run preview**
  > creates a production build und serves it in a webserver.
- **npm run dev**
  > runs the setup in development modus (no bundles are build)
- **npm run eslint**
  > runs eslint as linter to find problems
- **npm run prettier**
  > runs prettier code formatter on the ./src directory
- **npm run tsc**
  > runs the typescript to check types and build

