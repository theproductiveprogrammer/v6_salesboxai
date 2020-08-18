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
  workflowShow(store.fork('workflow'), e)

  getToolbar(store)

  if(window) window.salesbox = { store }
}

function reducer(state, type, payload) {
  return {
    workflow: workflowReducer(state.workflow, type, payload)
  }
}

function workflowInit() {
  return {
    toolbar: toolbarInit(),
  }
}

function workflowReducer(state, type, payload) {
  return {
    toolbar: toolbarReducer(state.toolbar, type, payload),
  }
}

function workflowShow(store, e) {
  drawToolbar(store.fork('toolbar'), e)
  drawCanvas(store, e)
}


import './toolbar.css'

function toolbarInit() {
  return { items: [], selected: -1 }
}

function toolbarReducer(state, type, payload) {
  switch(type) {
    case 'got/toolbar': return {
      items: payload,
      selected: 0
    }
    case 'toolbar/selected': return {
      items: state.items,
      selected: payload
    }
    default: return state
  }
}

function drawToolbar(store, e) {
  let tb = h('.toolbar')
  let icons = []
  e.appendChild(tb)

  store.react('items', items => {
    tb.setAttribute("width", 50 * items.length + 50)
    let i = 0
    for(;i < items.length;i++) {
      if(icons[i]) continue
      icons[i] = toolbarIcon(store, i)
      tb.appendChild(icons[i].e)
    }
    while(i < icons.length) {
      let icon = icons.pop()
      store.unreact(icon.fn)
      tb.removeChild(icon.e)
    }
  })

  store.react('selected', sel => {
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
    onclick: () => store.event('toolbar/selected', i)
  })

  let fn = store.react(`items.${i}`, tool => {
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
