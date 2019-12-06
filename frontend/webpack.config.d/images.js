config.module.rules.push(
    {
        test: /\.(jpg|png)$/,
        use: [
            'file-loader?name=[name].[ext]'
        ]
    }
);
