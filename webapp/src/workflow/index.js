'use strict'
const { h, svg } = require('@tpp/htm-x')
const req = require('@tpp/req')
const { error_ } = require('../common.js')

const toolbar = require('./toolbar.js')
const flow = require('./flow.js')

export function setup(rootstore) {
  req.get('/stepmeta', (err, resp) => {
    if(err) return error_(err)
    addSVG(resp, m => rootstore.event('workflow/stepmeta/got', m))
  })
  req.get('/eventmeta', (err, resp) => {
    if(err) return error_(err)
    addSVG(resp, m => rootstore.event('workflow/eventmeta/got', m))
  })
  flow.setup(rootstore)
}

export function init() {
  return {
    toolbar: toolbar.init(),
    flow: flow.init(),
  }
}

export function reducer(state, type, payload) {
  return {
    toolbar: toolbar.reducer(state.toolbar, type, payload),
    flow: flow.reducer(state.flow, type, payload),
  }
}

export function show(store, e) {
  let wk = h('.wk')
  e.appendChild(wk)

  toolbar.show(store.fork('toolbar'), wk)
  flow.show(store, wk)
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
