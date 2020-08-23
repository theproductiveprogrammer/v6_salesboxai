'use strict'
const { h } = require('@tpp/htm-x')

import './nav.css'

export function init() {
}

export function reducer(state, type, payload) {
}

export function show(store, on) {
  on.appendChild(h('img', { src: "salesbox-logo-icon.png" }))
}
