import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
	plugins: [react()],
	server: {
		headers: {
			'Content-Security-Policy':
				"default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline';",
		},
		proxy: {
			'/api': {
				target: 'http://backend:8080', // URL of your backend server
				changeOrigin: true,
			},
		},
	},
	build: {
		// Production build configurations
		outDir: 'dist',
	},
});
