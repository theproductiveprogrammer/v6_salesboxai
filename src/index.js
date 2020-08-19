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

  getStepMeta(store)

  if(window) window.salesbox = { store }
}

function getStepMeta(store) {
  let meta = [
    { name: 'Send Email', id: 'email' },
    { name: 'Adaptive', id: 'adaptive' },
    { name: 'Chat', id: 'chat' },
    { name: 'Decide', id: 'decide' },
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
    t.icon = require(`./icon-${t.id}.svg`)
    t.pic = { sz: szs[t.id] }
    t.pic.svg = require(`./step-${t.id}.svg`)
    return t
  })
  store.event('got/stepmeta', meta)
}


function reducer(state, type, payload) {
  return {
    workflow: workflowReducer(state.workflow, type, payload)
  }
}

function workflowInit() {
  return {
    events: eventInit(),
    toolbar: toolbarInit(),
    flow: flowInit(),
  }
}

function workflowReducer(state, type, payload) {
  return {
    toolbar: toolbarReducer(state.toolbar, type, payload),
    flow: flowReducer(state.flow, type, payload),
  }
}

function workflowShow(store, e) {
  drawToolbar(store.fork('toolbar'), e)
  let fns = {
    currStep: () => {
      let tb = store.get('toolbar')
      let curr = tb.items[tb.selected]
      if(curr) return curr.id
    },
    events: () => store.get('events'),
  }
  drawFlow(store.fork('flow'), fns, e)
}


function eventInit() {
  let svg = require('./step-event.svg')
  return [
    {
      name: 'Event: New Lead',
      pic: { sz: 64, svg: svg.replace('EVENT_TEXT', 'With New Lead') }
    },
    {
      name: 'Event: Email Open',
      pic: { sz: 64, svg: svg.replace('EVENT_TEXT', 'On Email Opened') }
    },
    {
      name: 'Event: Link Clicked',
      pic: { sz: 64, svg: svg.replace('EVENT_TEXT', 'On Link Click') }
    },
    {
      name: 'Event: Email Reply',
      pic: { sz: 64, svg: svg.replace('EVENT_TEXT', 'On Email Reply') }
    },
    {
      name: 'Event: Chat',
      pic: { sz: 64, svg: svg.replace('EVENT_TEXT', 'On Chat Msg') }
    },
  ]

}

import './toolbar.css'

function toolbarInit() {
  return { items: [], selected: -1 }
}

function toolbarReducer(state, type, payload) {
  switch(type) {
    case 'got/stepmeta': return {
      items: payload.map(m => {
        return { name:m.name, id: m.id, icon:m.icon }
      }),
      selected: 0
    }
    case 'toolbar/selected': return {
      ...state,
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

import './flow.css'

function flowInit() {
  return {
    meta: [],
    steps: [],
    selected: -1,
  }
}

function flowReducer(state, type, payload) {
  switch(type) {
    case 'got/stepmeta': return {
      ...state,
      meta: payload,
    }
    case 'step/add': return {
      ...state,
      selected: -1,
      steps: state.steps.concat(payload)
    }
    case 'step/selected': return {
      ...state,
      selected: payload
    }
    default: return state
  }
}

function drawFlow(store, fns, e) {
  let canvas = svg({
    width: "100vw",
    height: "100vh",
    viewBox: "0 0 800 600",
    preserveAspectRatio: "xMinYMin meet",
    ondblclick: add_icon_1,
    onclick: e => store.event('step/selected',opt(e.sel,-1))
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
    let curr = fns.currStep()
    if(!curr) return
    let info = store.get('meta').filter(m => m.id == curr)
    if(info && info.length) {
      info = info[0]
      let pos = svgPos(canvas, pt, e)
      if(info.pic.sz) {
        pos.x -= info.pic.sz/3
        pos.y -= info.pic.sz/3
      }
      store.event("step/add", {
        info,
        pos,
      })
    } else {
      console.error(`Failed finding info for step: ${curr}`)
    }
  }

  let ex = []

  store.react('steps', steps => {
    for(let i = ex.length;i < steps.length;i++) {
      let inf = dispStep(canvas, pt, store, i)
      ex.push(inf)
      canvas.appendChild(inf.e)
    }
  })

  store.react('selected', sel => {
    for(let i = 0;i < ex.length;i++) {
      ex[i].e.classList.remove('selected')
    }
    if(ex[sel]) ex[sel].e.classList.add('selected')
  })

}

function dispStep(canvas, pt, store, i) {
  let sz = 96
  let e = svg('svg.step', {
    width: sz, height: sz,
    onclick: (e) => e.sel = i
  })

  let fn = store.react(`steps.${i}`, step => {
    if(step.info.pic.sz) {
      e.setAttribute('width', step.info.pic.sz)
      e.setAttribute('height', step.info.pic.sz)
    }
    e.setAttribute('x', step.pos.x)
    e.setAttribute('y', step.pos.y)
    e.c(svg(step.info.pic.svg))
  })

  return { fn, e }

}

function svgPos(svg_, pt, e) {
  pt.x = e.clientX
  pt.y = e.clientY
  return pt.matrixTransform(svg_.getScreenCTM().inverse())
}

main()

function opt(v,d) { (v || v === 0) ? v : d }
