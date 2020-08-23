'use strict'
const { h } = require('@tpp/htm-x')

import './nav.css'

export function init() {
}

export function reducer(state, type, payload) {
  switch(type) {
    case 'ac/unauthorized': return 'login'
    case 'ac/user': return 'dashboard'
  }
}

export function show(store, on) {
  on.appendChild(h('img', { src: "salesbox-logo-icon.png" }))
}
