const { defineConfig } = require("@vue/cli-service");
const MonacoWebpackPlugin = require("monaco-editor-webpack-plugin");

module.exports = defineConfig({
  transpileDependencies: true,
  chainWebpack(config) {
    // 整合MonacoEditor
    config.plugin("monaco").use(new MonacoWebpackPlugin({}));
  },
  publicPath: "./",
  outputDir: "dist",
  assetsDir: "static",
  indexPath: "index.html",
  devServer: {
    allowedHosts: [
        "valleys.qicp.vip",
    ],
    proxy: {
      "/api": {
        target: "http://192.168.0.110:8101",
        changeOrigin: true,
      }
    },
  },
});
