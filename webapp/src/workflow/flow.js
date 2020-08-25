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

export function reducer(state, type, payload, events) {
  switch(type) {
    case 'workflow/stepmeta/got': return {
      ...state,
      meta: payload,
    }

    case 'workflow/step/new':
      return newStep(state, type, payload, events)

    case 'workflow/step/selected': return {
      ...state,
      selected: payload
    }

    default: return state
  }
}

function newStep(state, type, payload, events) {
  state = { ...state  }
  let curr = state.steps[state.selected]
  let ndx = state.selected
  if(!curr) {
    if(!state.steps.length) {
      curr = { info: events[0], pos: { x: 10, y: 250 } }
      state.steps = state.steps.concat(curr)
      ndx = 0
    } else {
      curr = state.steps[state.steps.length-1]
      ndx = state.steps.length-1
    }
  }
  let n = opt(curr.info.numlinks, 1)
  if(curr.links && curr.links.length >= n) {
    console.log('will not add another link to ' + curr.name)
  } else {
    let links = opt(curr.links,[])
    curr = { ...curr, links: links.concat(state.steps.length) }
  }
  state.selected = state.steps.length
  state.steps = state.steps.concat(payload)
  state.steps[ndx] = curr
  return state
}

export function show(store, fns, e) {
  let canvas = svg({
    width: "100vw",
    height: "100vh",
    viewBox: "0 0 1100 700",
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
    let curr = fns.currStep()
    if(!curr) return
    let info = store.get('meta').filter(m => m.code == curr)
    if(info && info.length) {
      info = info[0]
      let pos = svgPos(canvas, pt, e)
      if(info.iconszhint) {
        pos.x -= info.iconszhint/2
        pos.y -= info.iconszhint/2
      }
      store.event("workflow/step/new", { info, pos })
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

  let exLines = []
  store.react('steps', steps => {
    for(let i = 0;i < steps.length;i++) {
      let step = steps[i]
      let links = opt(steps[i].links, [])
      let lines = opt(exLines[i],[])
      if(links.length != lines.length) {
        for(let i = 0;i < lines.length;i++) {
          canvas.removeChild(lines[i])
        }
      }
      lines = []
      exLines[i] = lines
      for(let i = 0;i < links.length;i++) {
        let l = line_1(step, steps[links[i]])
        if(!l) continue
        lines.push(l)
        canvas.insertBefore(l, canvas.firstChild)
      }
    }

    function line_1(from, to) {
      if(!from || !to) return

      let foff = opt(from.info.iconszhint,0) / 2
      let toff = opt(to.info.iconszhint,0) / 2

      return svg('line', {
        style: {
          stroke: "#000",
          'stroke-width': 2,
        },
        x1: from.pos.x + foff,
        y1: from.pos.y + foff,
        x2: to.pos.x + toff,
        y2: to.pos.y + toff,
      })
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
    onclick: e => store.event('workflow/step/selected', i)
  })

  let fn = store.react(`steps.${i}`, step => {
    if(step.info.iconszhint) {
      e.setAttribute('width', step.info.iconszhint)
      e.setAttribute('height', step.info.iconszhint)
    }
    e.setAttribute('x', step.pos.x)
    e.setAttribute('y', step.pos.y)
    e.c(svg(step.info.pic.svg))
  })

  return { fn, e }

}

