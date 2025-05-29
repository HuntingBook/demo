/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}", // Scan all JS/TS/JSX/TSX files in src
  ],
  theme: {
    extend: {
      // You can extend MUI theme values here if needed, or keep it separate
      // For example, to use MUI palette colors with Tailwind:
      // colors: {
      //   primary: 'var(--mui-palette-primary-main)', // If you set CSS variables from MUI theme
      //   secondary: 'var(--mui-palette-secondary-main)',
      // },
    },
  },
  plugins: [],
  // corePlugins: { preflight: false } // Consider this if MUI's CssBaseline and Tailwind's preflight clash heavily.
                                    // Usually, they can coexist, with CssBaseline taking precedence for base element styling.
                                    // Or, ensure Tailwind's base is imported *after* MUI's baseline styles if issues arise.
}
