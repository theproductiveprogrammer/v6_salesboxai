'use strict'
const { h } = require('@tpp/htm-x')
const dux = require('@tpp/dux')

const header_ = require('./header.js')
const nav_ = require('./nav.js')
const um_ = require('./um.js')
const dashboard_ = require('./dashboard.js')
const wk_ = require('./workflow/index.js')

import './director.css'

export function start(body) {
  let store = setupStore()
  setupView(store, body)
  setupAccessControl(store, body)

  if(window) window.salesbox = { store }
}

function setupStore() {
  return dux.createStore(reducer, init())
}

function init() {
  return {
    um: um_.init(),
    nav: nav_.init(),
    workflow: wk_.init(),
  }
}

function reducer(state, type, payload) {
  return {
    um: um_.reducer(state.um, type, payload),
    nav: nav_.reducer(state.nav, type, payload),
    workflow: wk_.reducer(state.workflow, type, payload),
  }
}

function setupView(store, body) {
  let nav = h('.nav')
  let header = h('.header')
  let display = h('.display')
  let app = h('.app', [nav, header, display])
  body.appendChild(app)

  header_.show(store, header)
  nav_.show(store.fork('nav'), nav)

  let currview
  store.react('nav', nav => {
    currview = store.destroy(currview)
    display.innerHTML = ""
    switch(nav) {
      case 'dashboard': {
        return dashboard_.show(store, display)
      }
      case 'workflow': {
        currview = store.fork('workflow')
        return wk_.show(currview, display)
      }
      case 'login': {
        currview = store.fork('user')
        return um_.showLogin(currview, display)
      }
      case 'signup': {
        currview = store.fork('user')
        return um_.showSignup(currview, display)
      }
      default: {
        display.innerHTML = 'Cannot show page "' + nav + '"'
      }
    }
  })
}

function setupAccessControl(store, body) {
  store.react('user', user => {
    if(user == null) return store.event('ac/unauthorized')
    else store.event('ac/user', user)
  })
}
