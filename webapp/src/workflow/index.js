'use strict'
const { h, svg } = require('@tpp/htm-x')
const req = require('@tpp/req')
const { error_ } = require('../common.js')

const events = require('./events.js')
const toolbar = require('./toolbar.js')
const flow = require('./flow.js')

export function setup(store) {
  req.get('/stepmeta', (err, resp) => {
    if(err) return error_(err)
    addSVG(resp, m => store.event('workflow/stepmeta/got', m))
  })
  req.get('/eventmeta', (err, resp) => {
    if(err) return error_(err)
    addSVG(resp, m => store.event('workflow/eventmeta/got', m))
  })
}

export function init() {
  return {
    events: events.init(),
    toolbar: toolbar.init(),
    flow: flow.init(),
  }
}

export function reducer(state, type, payload) {
  return {
    events: events.reducer(state.events, type, payload),
    toolbar: toolbar.reducer(state.toolbar, type, payload),
    flow: flow.reducer(state.flow, type, payload, state.events),
  }
}

export function show(store, e) {
  let wk = h('.wk')
  e.appendChild(wk)

  toolbar.show(store.fork('toolbar'), wk)

  let fns = {
    currStep: () => {
      let tb = store.get('toolbar')
      let curr = tb.items[tb.selected]
      if(curr) return curr.code
    },
    events: () => store.get('events'),
  }
  flow.show(store.fork('flow'), fns, wk)
}

function addSVG(meta, cb) {
  let n = meta.length
  meta.forEach(m => {
    req.get(m.pic, (err, svg) => {
      n--
      m.pic = { svg: svg.response }
      if(n == 0) cb(meta)
    })
  })
}
