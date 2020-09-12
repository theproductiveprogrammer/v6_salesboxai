'use strict'
const { h } = require('@tpp/htm-x')

import './nav.css'

let anon
export function setup(rootstore) {
  anon = () => rootstore.get('user') == null
}

export function init() {
}

export function reducer(state, type, payload) {
  switch(type) {
    case 'ac/unauthorized': return 'login'
    case 'ac/user': return 'dashboard'
    case 'um/signup': return 'signup'
    case 'um/login': return 'login'
    case 'dash/workflow/go': return 'workflow'
    case 'dash/importer/go': return 'importer'
    case 'dash/leads/go': return 'leads'
    case 'nav/home': return anon() ? 'login' : 'dashboard'
    case 'leads/lead/go': return 'lead'
    case 'lead/goback': return 'back'
    default: return state;
  }
}

export function show(store, on) {
  on.appendChild(h('img.home', {
    src: "salesbox-logo-icon.png",
    onclick: () => store.event('nav/home')
  }))
}
