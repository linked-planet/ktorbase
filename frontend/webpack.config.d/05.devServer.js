;(function (config) {
    if (config.devServer) {
        config.devServer.proxy = {
            '/**/*': {
                target: 'http://localhost:9090',
                bypass: function (req, res, proxyOptions) {
                    if (req.url === '/') {
                        return '/index.html';
                    }
                }
            }
        };
        config.devServer.client = {
            overlay: {
                errors: true,
                warnings: false
            }
        };
        config.devServer.open = false;
    }
})(config);
