'use strict'
const { h, svg } = require('@tpp/htm-x')
const dux = require('@tpp/dux')

import './reset.css'

function main() {
  const e = document.createElement('div')
  document.body.appendChild(e)

  const store = dux.createStore(reducer, {
    workflow: workflowInit(),
  })
  workflowShow(store, e)

  getToolbar(store)

  if(window) window.salesbox = { store }
}

function reducer(state, type, payload) {
  return {
    workflow: workflowReducer(state.workflow, type, payload)
  }
}

import './workflow.css'

function workflowInit() {
  return {
    toolbar: { items: [], selected: -1 },
  }
}

function workflowReducer(state, type, payload) {
  switch(type) {
    case 'got/toolbar': return {
      ...state,
      toolbar: { items: payload, selected: 0 },
    }
    case 'workflow/toolbar/selected': return {
      ...state, 
      toolbar: { ...state.toolbar, selected: payload },
    }
    default: return state
  }
}

function workflowShow(store, e) {
  drawToolbar(store, e)
  drawCanvas(store, e)
}

function drawToolbar(store, e) {
  let tb = h('.toolbar')
  let icons = []
  e.appendChild(tb)

  store.react('workflow.toolbar.items', items => {
    tb.setAttribute("width", 50 * items.length + 50)
    for(let i = 0;i < items.length;i++) {
      if(icons[i]) continue
      icons[i] = toolbarIcon(store, i)
      tb.appendChild(icons[i].e)
    }
  })

  store.react('workflow.toolbar.selected', sel => {
    for(let i = 0;i < icons.length;i++) {
      if(sel == i) icons[i].e.classList.add('selected')
      else icons[i].e.classList.remove('selected')
    }
  })

}

function drawCanvas(store, e) {
  let canvas = svg({ viewBox: "0 0 500 500" })
  e.appendChild(canvas)
}


function toolbarIcon(store, i) {
  let sz = 32
  let e = h('.icon', {
    onclick: () => store.event('workflow/toolbar/selected', i)
  })

  let fn = store.react(`workflow.toolbar.items.${i}`, tool => {
    e.setAttribute('title', tool.name)
    e.c(
      svg({ width: sz, height: sz }, tool.icon)
    )
  })

  return { e, fn }
}

function getToolbar(store) {
  store.event('got/toolbar', [
    { name: 'Event', icon: require('./event.svg') },
    { name: 'Send Email', icon: require('./email.svg') },
    { name: 'Adaptive', icon: require('./adaptive.svg') },
    { name: 'Chat', icon: require('./chat.svg') },
    { name: 'Decide', icon: require('./decide.svg') },
    { name: 'Twitter', icon: require('./twitter.svg') },
    { name: 'LinkedIn', icon: require('./linkedin.svg') },
    { name: 'Salesforce', icon: require('./salesforce.svg') },
    { name: 'SMS', icon: require('./sms.svg') },
    { name: 'Add To List', icon: require('./listadd.svg') },
    { name: 'Facebook', icon: require('./facebook.svg') },
    { name: 'Meeting', icon: require('./meeting.svg') },
  ])
}

main()
