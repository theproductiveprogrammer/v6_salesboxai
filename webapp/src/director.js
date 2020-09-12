'use strict'
const { h } = require('@tpp/htm-x')
const dux = require('@tpp/dux')

const header_ = require('./header.js')
const nav_ = require('./nav.js')
const um_ = require('./um.js')
const profile_ = require('./profile.js')
const dashboard_ = require('./dashboard.js')
const wk_ = require('./workflow/index.js')
const im_ = require('./importer.js')
const leads_ = require('./leads.js')
const lead_ = require('./lead.js')

import './director.css'

export function start(body) {
  let store = dux.createStore(reducer, init())

  ;[ nav_, profile_, wk_, im_, leads_, lead_ ].forEach(m => {
    if(m.setup) m.setup(store)
  })

  setupAccessControl(store)
  setupView(store, body)

  if(window) window.store = store
}

function init() {
  return {
    user: um_.init(),
    profile: profile_.init(),
    nav: nav_.init(),
    workflow: wk_.init(),
    imports: im_.init(),
    leads: leads_.init(),
    lead: lead_.init(),
  }
}

function reducer(state, type, payload) {
  return {
    user: um_.reducer(state.user, type, payload),
    profile: profile_.reducer(state.profile, type, payload),
    nav: nav_.reducer(state.nav, type, payload),
    workflow: wk_.reducer(state.workflow, type, payload),
    imports: im_.reducer(state.imports, type, payload),
    leads: leads_.reducer(state.leads, type, payload),
    lead: lead_.reducer(state.lead, type, payload),
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
  let currclose
  let crumbs = []
  store.react('nav', nav => {
    let keep = false
    if(currclose) currclose()
    currclose = null
    if(nav == 'back') {
      keep = true
      crumbs.pop()
      nav = crumbs[crumbs.length-1]
    } else {
      crumbs.push(nav)
    }
    currview = store.destroy(currview)
    display.innerHTML = ""
    switch(nav) {
      case 'lead':
        currview = store.fork('lead')
        return lead_.show(currview, display, keep)
      case 'leads':
        currview = store.fork('leads')
        currclose = leads_.close
        return leads_.show(currview, display, keep)
      case 'importer': {
        currview = store.fork('imports')
        return im_.show(currview, display, keep)
      }
      case 'dashboard': {
        currview = store.fork()
        return dashboard_.show(currview, display, keep)
      }
      case 'workflow': {
        currview = store.fork('workflow')
        return wk_.show(currview, display, keep)
      }
      case 'login': {
        currview = store.fork('user')
        return um_.showLogin(currview, display, keep)
      }
      case 'signup': {
        currview = store.fork('user')
        return um_.showSignup(currview, display, keep)
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
