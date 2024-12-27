import { fileURLToPath, URL } from 'node:url'

import { defineConfig, UserConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import babel from "vite-plugin-babel";
import commonjs from 'vite-plugin-commonjs';

import AutoImport from 'unplugin-auto-import/vite' // 自动导入
import Components from 'unplugin-vue-components/vite' // 组件注册
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers' // elementPlus
// @ts-ignore
import fs from 'fs'
// @ts-ignore
import dotenv from 'dotenv'
import path from 'path'

// https://vitejs.dev/config/
export default defineConfig(({ mode }): UserConfig => {
  // 根据环境变量加载环境变量文件
  const ASR_ENV = dotenv.parse(fs.readFileSync(`.env.${mode}`))
  return {
    base: ASR_ENV.VITE_PUBLIC_PATH, // 部署基路径
    server: {
      open: true, // 是否主动唤醒浏览器
      host: '0.0.0.0',
      port: 2400,
      proxy: {
        [ASR_ENV.VITE_BASE_API]: {
          target: `${ASR_ENV.VITE_BASE_PATH}`,
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/api/, '')
        }
      }
    },
    plugins: [
      vue(),
      commonjs(),
      babel({
        include: ['src/**/*'],
        babelConfig: {
          presets: [
            [
              "@babel/preset-env",
              {
                "targets": "defaults"
              }
            ],
            "@babel/preset-typescript"
          ],
          plugins: [
            '@babel/plugin-transform-runtime',
          ]
        }
      }),
      AutoImport({
        resolvers: [ElementPlusResolver()], //对于element puls的配置
        imports: [
          'vue', //自动引入的vue的ref等方法
          'vue-router', //引入useRoute等方法
          {
            //对于vue-router的type的扩展,配置后可以直接使用
            from: 'vue-router',
            imports: ['RouteLocationRaw'],
            type: true
          }
        ],
        dts: 'src/utils/auto-imports.d.ts'
      }),
      Components({
        resolvers: [ElementPlusResolver()] //对于element plus的配置
      })
    ],
    resolve: {
      alias: [
        {
          find: '@',
          replacement: path.resolve(__dirname, 'src')
        }
      ]
        // '@': fileURLToPath(new URL('./src', import.meta.url))
    },
    css: {
      preprocessorOptions: {
        scss: {
          charset: false
        }
      }
    },
    build: {
      target: 'esnext',
      outDir: 'dist', // 指定输出路径
      assetsDir: 'assets', // 指定生成静态资源的存放路径
      minify: false, // 混淆器,terser构建后文件体积更小 ,boolean | 'terser' | 'esbuild',默认使用esbuild
      sourcemap: false, // 是否产出soucemap.json
      manifest: false, // 是否产出maifest.json
      cssCodeSplit: true,
      emptyOutDir: true,
      // reportCompressedSize: true,
      chunkSizeWarningLimit: 1500,
      terserOptions: {
        compress: {
          drop_console: true, // 生产环境移除console
          drop_debugger: true // 生产环境移除debugger
        }
      },
      rollupOptions: {
        output: {
            compact: true,
            entryFileNames: "index/[name].js",
            chunkFileNames: "index/js/[name].js",
            // assetFileNames: "static/[ext]/[name].[ext]",
            manualChunks:(id)=>{
              if (id.includes('node_modules')) {
                 return "vendor"
              }
            },
            assetFileNames: (assetInfo)=>{
              if (assetInfo.name?.endsWith(".css")) {
                 return "index/css/[name].[ext]";
              }
              if (/\.(png|jpe?g|gif|svg|webp|ico)$/.test(assetInfo.name ?? "")) {
                 return "index/img/[name].[ext]";
              }
              return "index/assets/[name].[ext]";
            }
        }
      }
    }
  }
})
