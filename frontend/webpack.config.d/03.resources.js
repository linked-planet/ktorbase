;(function (config) {
    config.resolve.modules.push(
        "frontend/build/processedResources/js/main"
    );
    config.resolve.fallback = {
        "path": require.resolve("path-browserify")
    }
})(config);
