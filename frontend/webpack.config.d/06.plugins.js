const webpack = require('webpack'); // to access built-in plugins

;(function (config) {
    config.plugins = [
        new webpack.ProvidePlugin({
            // required by some @atlaskit components, but Webpack5 does not provide node.js polyfills anymore
            process: 'process/browser'
        })
    ]
})(config);
