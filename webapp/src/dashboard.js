'use strict'
const { h } = require('@tpp/htm-x')

import './dashboard.css'

export function show(store, on) {
  on.appendChild(h('h1', "Dashboard"))
}
