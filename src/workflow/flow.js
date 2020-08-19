'use strict'
const { h, svg } = require('@tpp/htm-x')

const { opt, svgPos } = require('../../util.js')

import './flow.css'

export function init() {
  return {
    meta: [],
    steps: [],
    selected: -1,
  }
}

export function reducer(state, type, payload) {
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

export function show(store, fns, e) {
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
    onclick: e => e.sel = i
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

