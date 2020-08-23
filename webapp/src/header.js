'use strict'
const { h } = require('@tpp/htm-x')

import './header.css'

export function show(store, on) {
  on.appendChild(h('img', { src: "salesboxai-logo.png" }))
}
