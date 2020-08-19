'use strict'
const dux = require('@tpp/dux')

const workflow = require('./workflow/index.js')

import './reset.css'


function main() {
  const e = document.createElement('div')
  document.body.appendChild(e)

  const store = dux.createStore(reducer, {
    workflow: workflow.init(),
  })
  workflow.show(store.fork('workflow'), e)

  getStepMeta(store)

  if(window) window.salesbox = { store }
}

function getStepMeta(store) {
  let meta = [
    { name: 'Send Email', id: 'email' },
    { name: 'Adaptive', id: 'adaptive' },
    { name: 'Chat', id: 'chat' },
    { name: 'Decide', id: 'decide', numlinks: 2 },
    { name: 'Twitter', id: 'twitter' },
    { name: 'LinkedIn', id: 'linkedin' },
    { name: 'Salesforce', id: 'salesforce' },
    { name: 'SMS', id: 'sms' },
    { name: 'Add To List', id: 'listadd' },
    { name: 'Facebook', id: 'facebook' },
    { name: 'Meeting', id: 'meeting' },
  ]
  let szs = {
    email: 168,
    adaptive: 64,
    chat: 168,
    decide: 40,
    twitter: 96,
    linkedin: 168,
    salesforce: 168,
    sms: 168,
    listadd: 96,
    facebook: 168,
    meeting: 64,
  }
  meta = meta.map(t => {
    t.icon = require(`./workflow/icon/${t.id}.svg`)
    t.pic = { sz: szs[t.id] }
    t.pic.svg = require(`./workflow/step/${t.id}.svg`)
    return t
  })
  store.event('got/stepmeta', meta)
}


function reducer(state, type, payload) {
  return {
    workflow: workflow.reducer(state.workflow, type, payload)
  }
}

main()
