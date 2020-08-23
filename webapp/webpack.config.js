'use strict'
module.exports = {
  entry: './src/app.js',
  devServer: {
    port: 6090,
  },
  module: {
    rules: [
      { test: /\.svg$/, use: 'svg-inline-loader' },
      { test: /\.css$/, use: ['style-loader', 'css-loader'] },
    ]
  }
}
