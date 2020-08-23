'use strict'
const { h } = require('@tpp/htm-x')

import './nav.css'

export function init() {
}

export function reducer(state, type, payload) {
  switch(type) {
    case 'auth/unauthorized': return 'signup'
    case 'auth/user': return 'dashboard'
    case 'auth/signup': return 'signup'
    case 'auth/login': return 'login'
    default: return state;
  }
}

export function show(store, on) {
  on.appendChild(h('img', { src: "salesbox-logo-icon.png" }))
}
