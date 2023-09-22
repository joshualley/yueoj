const { defineConfig } = require("@vue/cli-service");
const MonacoWebpackPlugin = require("monaco-editor-webpack-plugin");

module.exports = defineConfig({
  transpileDependencies: true,
  chainWebpack(config) {
    // 整合MonacoEditor
    config.plugin("monaco").use(new MonacoWebpackPlugin({}));
  },
});
