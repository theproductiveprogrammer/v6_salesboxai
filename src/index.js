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
    selected: null,
  }
}

function workflowReducer(state, type, payload) {
  return {
    toolbar: toolbarReducer(state.toolbar, type, payload),
    steps: stepsReducer(state.steps, type, payload),
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
  let tools = [
    { name: 'Event', n_: 'event' },
    { name: 'Send Email', n_: 'email' },
    { name: 'Adaptive', n_: 'adaptive' },
    { name: 'Chat', n_: 'chat' },
    { name: 'Decide', n_: 'decide' },
    { name: 'Twitter', n_: 'twitter' },
    { name: 'LinkedIn', n_: 'linkedin' },
    { name: 'Salesforce', n_: 'salesforce' },
    { name: 'SMS', n_: 'sms' },
    { name: 'Add To List', n_: 'listadd' },
    { name: 'Facebook', n_: 'facebook' },
    { name: 'Meeting', n_: 'meeting' },
  ]
  let szs = {
    event: 64,
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
  tools = tools.map(t => {
    t.icon = require(`./icon-${t.n_}.svg`)
    t.pic = { sz: szs[t.n_] }
    t.pic.svg = require(`./step-${t.n_}.svg`)
    return t
  })
  store.event('got/toolbar', tools)
}

import './steps.css'

function stepsInit() {
  return {
    tasks: [],
    selected: null,
  }
}

function stepsReducer(state, type, payload) {
  switch(type) {
    case 'step/add': return {
      ...state,
      selected: null,
      tasks: state.tasks.concat(payload)
    }
    case 'step/selected': return {
      ...state,
      selected: payload
    }
    default: return state
  }
}

function drawSteps(store, currStep, e) {
  let canvas = svg({
    width: "100vw",
    height: "100vh",
    viewBox: "0 0 800 600",
    preserveAspectRatio: "xMinYMin meet",
    ondblclick: add_icon_1,
  })
  let pt = canvas.createSVGPoint()
  let filter = svg('filter#sel', svg('feDropShadow', {
    dx: 0, dy: 0,
    stdDeviation: 1,
    "flood-color": "#3333ff",
  }))
  canvas.c(filter)
  e.appendChild(canvas)

  function add_icon_1(e) {
    let curr = currStep()
    if(!curr) return
    store.event("step/add", {
      tool: curr,
      x: e.clientX,
      y: e.clientY
    })
  }

  let ex = []

  store.react('tasks', tasks => {
    for(let i = ex.length;i < tasks.length;i++) {
      let task = stepTask(canvas, pt, store, i)
      ex.push(task)
      canvas.appendChild(task.e)
    }
  })

  store.react('selected', sel => {
    for(let i = 0;i < ex.length;i++) {
      ex[i].e.classList.remove('selected')
    }
    if(!sel && sel !== 0) return
    ex[sel].e.classList.add('selected')
  })

}

function stepTask(canvas, pt, store, i) {
  let sz = 96
  let e = svg('svg.step', {
    width: sz, height: sz,
    onclick: () => store.event('step/selected', i)
  })

  let fn = store.react(`tasks.${i}`, task => {
    let pos = svgPos(canvas, pt, task)
    let tool = task.tool
    if(tool.pic.sz) {
      e.setAttribute('width', tool.pic.sz)
      e.setAttribute('height', tool.pic.sz)
      pos.x -= tool.pic.sz/3
      pos.y -= tool.pic.sz/3
    }
    e.setAttribute('x', pos.x)
    e.setAttribute('y', pos.y)
    let img = tool.pic.svg ? tool.pic.svg : tool.icon
    e.c(svg(img))
  })

  return { fn, e }

}

function svgPos(svg_, pt, pos) {
  pt.x = pos.x
  pt.y = pos.y
  return pt.matrixTransform(svg_.getScreenCTM().inverse())
}

main()
