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
    steps: stepsInit(),
  }
}

function workflowReducer(state, type, payload) {
  return {
    toolbar: toolbarReducer(state.toolbar, type, payload),
    steps: stepsReducer(state.steps, type, payload)
  }
}

function workflowShow(store, e) {
  drawToolbar(store.fork('toolbar'), e)
  drawSteps(store.fork('steps'), () => {
    let state = store.get()
    return state.toolbar.items[state.toolbar.selected]
  }, e)
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

import './steps.css'

function stepsInit() {
  return []
}

function stepsReducer(state, type, payload) {
  switch(type) {
    case 'step/add': return state.concat(payload)
    default: return state
  }
}

function drawSteps(store, currStep, e) {
  let canvas = svg({
    width: "100vw",
    height: "100vh",
    viewBox: "0 0 800 600",
    preserveAspectRatio: "xMinYMin meet",
    onclick: add_icon_1,
  })
  let pt = canvas.createSVGPoint()
  e.appendChild(canvas)

  function add_icon_1(e) {
    let curr = currStep()
    if(!curr) return
    store.event("step/add", {
      curr,
      x: e.clientX,
      y: e.clientY
    })
  }

  let ex = []

  store.react(steps => {
    for(let i = ex.length;i < steps.length;i++) {
      let step = stepItem(canvas, pt, store, i)
      ex.push(step)
      canvas.appendChild(step.e)
    }
  })

}

function stepItem(canvas, pt, store, i) {
  let sz = 96
  let e = svg('svg', { width: sz, height: sz })

  let fn = store.react(i, step => {
    let pos = svgPos(canvas, pt, step)
    pos.x -= sz/3
    pos.y -= sz/3
    e.setAttribute('x', pos.x)
    e.setAttribute('y', pos.y)
    e.c(svg(step.curr.icon))
  })

  return { fn, e }

}

function svgPos(svg_, pt, pos) {
  pt.x = pos.x
  pt.y = pos.y
  return pt.matrixTransform(svg_.getScreenCTM().inverse())
}

main()
