import flowbite from 'flowbite/plugin'
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}",
    "node_modules/flowbite-react/lib/**/*.js"
],
  theme: {
    extend: {},
  },
  plugins: [flowbite],
  darkMode:'class'
}
