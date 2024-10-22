import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // URL of your backend server
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '') // Optionally, remove '/api' prefix from request
      }
    }
  }
})
