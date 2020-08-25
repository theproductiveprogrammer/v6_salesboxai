'use strict'
const { h } = require('@tpp/htm-x')

import './nav.css'

export function init() {
}

export function reducer(state, type, payload) {
  switch(type) {
    case 'ac/unauthorized': return 'login'
    case 'ac/user': return 'dashboard'
    case 'um/signup': return 'signup'
    case 'um/login': return 'login'
    case 'dash/workflow/go': return 'workflow'
    case 'nav/home': return 'dashboard'
    default: return state;
  }
}

export function show(store, on) {
  on.appendChild(h('img.home', {
    src: "salesbox-logo-icon.png",
    onclick: () => store.event('nav/home')
  }))
}
