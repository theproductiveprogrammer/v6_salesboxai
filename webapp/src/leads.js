'use strict'
const { h } = require('@tpp/htm-x')
const { get, error_ } = require('./common.js')

import './leads.css'

let get_
export function setup(rootstore) {
  get_ = (url, data, cb) => get(rootstore, url, data, cb)
}

export function init() {
}

export function reducer(state, type, payload) {
  switch(type) {
    case 'leads/got': return payload
    default: return state
  }
}

export function show(store, on, keep) {
  let ld = h('.leads')
  on.appendChild(ld)
  if(!keep) getLeads(store)

  let title = h('.title', 'Leads')
  let tblbody = h('tbody')
  let tbl = h('table', [
    h('thead',
      h('tr', [
        h('th', 'Id'),
        h('th', 'First Name'),
        h('th', 'Last Name'),
        h('th', 'Email'),
      ])
    ),
    tblbody
  ])

  store.react(users => {
    tblbody.innerHTML = ''
    if(!users) return
    users.forEach(user => {
      tblbody.appendChild(h('tr', [
        h('td.nav-lead', {
          onclick: () => store.event('leads/lead/go', user.id)
        }, user.id),
        h('td', user.firstName),
        h('td', user.lastName),
        h('td', user.email),
      ]))
    })
  })


  ld.c(title, tbl)
}

function getLeads(store) {
  get_('leads', (err, leads) => {
    if(err) return error_(err, 'get/leads')
    store.event('leads/got', leads)
  })
}

