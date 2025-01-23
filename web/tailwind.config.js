/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        'pure-white': 'var(--pure-white)',
        'cultured': 'var(--cultured)',
        'alizarin-crimson': 'var(--alizarin-crimson)',
        'cardinal': 'var(--cardinal)',
        'light-silver': 'var(--light-silver)',
      },
    },
  },
  plugins: [],
}

